package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.dto.request.status.StatusTaskRequest;
import it.epicode.whatsnextbe.dto.request.task.TaskRequest;
import it.epicode.whatsnextbe.dto.request.task.TaskRequestUpdate;
import it.epicode.whatsnextbe.dto.response.task.TaskResponse;
import it.epicode.whatsnextbe.dto.response.task.TaskResponseLight;
import it.epicode.whatsnextbe.dto.response.user.UserResponseLight;
import it.epicode.whatsnextbe.error.ResourceNotFoundException;
import it.epicode.whatsnextbe.mapper.UserMapper;
import it.epicode.whatsnextbe.model.Category;
import it.epicode.whatsnextbe.model.Status;
import it.epicode.whatsnextbe.model.Task;
import it.epicode.whatsnextbe.model.User;
import it.epicode.whatsnextbe.repository.CategoryRepository;
import it.epicode.whatsnextbe.repository.StatusRepository;
import it.epicode.whatsnextbe.repository.TaskRepository;
import it.epicode.whatsnextbe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatusRepository statusRepository;
    private final AuthenticationSerivce authenticationService;
    private final UserService userService;

    @Transactional
    public List<TaskResponseLight> getAllTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            System.out.println("Authentication object is null");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        List<Task> tasks;
        if (isAdmin) {
            tasks = taskRepository.findAll();
        } else {
            Long userId = authenticationService.getCurrentUserId();
            Optional<User> user = userRepository.findById(userId);

            tasks = taskRepository.findAllByUsersIdOrShared(user);
        }

        return tasks.stream().map(task -> {
            if (task.getStatus() == null) {
                task.setStatus(getDefaultStatus());
            }
            TaskResponseLight dto = new TaskResponseLight();
            dto.setId(task.getId());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setStatus(task.getStatus().getStatus().toString());
            dto.setCategory(task.getCategory());
            dto.setUsers(task.getUsers().stream().map(UserMapper::convertToUserResponseLight).collect(Collectors.toList()));
            System.out.println(task.getUsers());
            return dto;
        }).collect(Collectors.toList());
    }
    private Status getDefaultStatus() {
        return statusRepository.findByStatus(Status.StatusType.NON_COMPLETATO)
                .orElseGet(() -> {
                    Status newStatus = new Status(Status.StatusType.NON_COMPLETATO);
                    return statusRepository.save(newStatus);
                });
    }

    public TaskResponse getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            Task entity = task.get();
            TaskResponse response = new TaskResponse();
            BeanUtils.copyProperties(entity, response);
            return response;
        } else {
            throw new EntityNotFoundException("Task with id " + id + " not found");
        }
    }

    @Transactional
    public String deleteTask(Long taskId) {
        User currentUser = userService.getCurrentUser();
        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            boolean isAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getTypeRole().equals("ADMIN"));

            if (isAdmin || (task.getUsers().size() == 1 && task.getUsers().contains(currentUser))) {
                // Rimuovi la task dalla lista delle task di ogni utente
                for (User user : task.getUsers()) {
                    user.getTasks().remove(task);
                }
                task.getUsers().clear();

                // Gestisci la categoria della task
                if (task.getCategory() != null) {
                    task.getCategory().getTasks().remove(task);
                    task.setCategory(null);
                }

                // Persisti le modifiche prima dell'eliminazione
                taskRepository.save(task);

                // Elimina la task
                taskRepository.deleteById(taskId);
                taskRepository.flush();

                System.out.println("Task deleted successfully " + task.getId());
                return "Task deleted successfully";
            } else {
                return "You do not have permission to delete this task. Ask an ADMIN";
            }
        } else {
            return "Task not found";
        }
    }

    public TaskResponse createTask(TaskRequest request, Principal principal) {
        System.out.println("TaskRequest received: " + request); // Log completo della richiesta

        Task entityTask = new Task();
        BeanUtils.copyProperties(request, entityTask);

        System.out.println("Task entity after copying properties: " + entityTask); // Log completo dell'entità task dopo la copia delle proprietà

        // Gestione dello status
        Status status = (request.getStatus() == null)
                ? getDefaultStatus()
                : statusRepository.findByStatus(request.getStatus())
                .orElseGet(() -> statusRepository.save(new Status(request.getStatus())));
        entityTask.setStatus(status);

        System.out.println("Task entity after setting status: " + entityTask); // Log dell'entità task dopo aver settato lo status

        // Recupero dell'utente corrente
        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + principal.getName()));

        List<User> users = new ArrayList<>();
        System.out.println("User IDs from request: " + request.getUserIds());

        if (authenticationService.isAdmin(currentUser.getId())) {
            for (Long userId : request.getUserIds()) {
                if (userId != null) {
                    User userFound = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
                    users.add(userFound);
                } else {
                    throw new IllegalArgumentException("User ID must not be null");
                }
            }
        } else {
            users = List.of(currentUser);
        }

        // Impostazione degli utenti nella task
        entityTask.setUsers(users);
        for (User user : users) {
            user.getTasks().add(entityTask);
        }

        System.out.println("Task entity after setting users: " + entityTask); // Log dell'entità task dopo aver settato gli utenti

        // Verifica e impostazione della categoria
        if (request.getCategory().getId() == null) {
            throw new IllegalArgumentException("Category ID must not be null");
        }
        Category entityCategory = categoryRepository.findById(request.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategory().getId()));
        entityTask.setCategory(entityCategory);

        System.out.println("Task entity after setting category: " + entityTask); // Log dell'entità task dopo aver settato la categoria

        // Impostazione dello stato condiviso
        if (request.getUserIds().size() > 1) {
            entityTask.setShared(true);
        }

        // Salvataggio della task
        Task savedTask = taskRepository.save(entityTask);

        System.out.println("Saved task entity: " + savedTask); // Log dell'entità task salvata

        // Creazione della risposta
        TaskResponse response = new TaskResponse();
        BeanUtils.copyProperties(savedTask, response);

        System.out.println("TaskResponse to be returned: " + response); // Log della risposta finale

        return response;
    }

    @Transactional
    public TaskResponseLight updateTask(Long id, TaskRequestUpdate request) {
        User currentUser = userService.getCurrentUser();
        Optional<Task> taskOpt = taskRepository.findById(id);
        System.out.println(taskOpt);

        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            System.out.println("riga 209" + task);
            boolean isAdmin = currentUser.getRoles().stream()
                    .anyMatch(role -> role.getTypeRole().equals("ADMIN"));

            boolean isTaskAssignedToCurrentUser = task.getUsers().contains(currentUser);

            if (isAdmin || isTaskAssignedToCurrentUser) {
                task.setTitle(request.getTitle());
                task.setDescription(request.getDescription());

                if (request.getStatus() != null) {
                    Optional<Status> statusOpt = statusRepository.findByStatus(request.getStatus());
                    statusOpt.ifPresent(task::setStatus);
                }

                if (request.getCategory() != null && request.getCategory().getId() != null) {
                    Category category = categoryRepository.findById(request.getCategory().getId())
                            .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategory().getId()));
                    task.setCategory(category);
                } else {
                    task.setCategory(null);
                }

                task.getUsers().clear();
                System.out.println("lista degli utenti" + task.getUsers());
                taskRepository.saveAndFlush(task);

                // Aggiungi le nuove associazioni degli utenti
                if (request.getUserIds() != null && !request.getUserIds().isEmpty()) {
                    List<User> users = userRepository.findAllById(request.getUserIds());
                    System.out.println("lista degli utenti trovati nella repository " + users);
                    task.setUsers(users);
                    System.out.println("Utenti nella task aggiornata: " + task.getUsers());
                    taskRepository.saveAndFlush(task);
                }

                System.out.println("Saving task: " + task.getUsers() );
                taskRepository.saveAndFlush(task);

                return getTaskCompleteResponse(task);
            } else {
                throw new AccessDeniedException("You do not have permission to update this task.");
            }
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }

    @Transactional
    public TaskResponse updateTaskStatus(Long id, StatusTaskRequest request) {
        User currentUser = userService.getCurrentUser(); // Supponendo che tu abbia un metodo per ottenere l'utente corrente
        Optional<Task> taskOpt = taskRepository.findById(id);

        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();

            boolean isAdmin = currentUser.getRoles().stream()
                    .anyMatch(role -> role.getTypeRole().equals("ADMIN"));

            boolean isTaskAssignedToCurrentUser = task.getUsers().contains(currentUser);

            if (isAdmin || isTaskAssignedToCurrentUser) {
                Optional<Status> statusOpt = statusRepository.findByStatus(Status.StatusType.valueOf(request.getStatus()));
                if (statusOpt.isPresent()) {
                    task.setStatus(statusOpt.get());
                    taskRepository.save(task);
                    return new TaskResponse(task.getStatus());
                } else {
                    throw new ResourceNotFoundException("Status not found: " + request.getStatus());
                }
            } else {
                throw new AccessDeniedException("You do not have permission to update this task.");
            }
        } else {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
    }

    private TaskResponseLight getTaskCompleteResponse(Task task) {
        TaskResponseLight response = new TaskResponseLight();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus().getStatus().toString());
        response.setCategory(task.getCategory());
        response.setUsers(task.getUsers().stream()
                .map(user -> new UserResponseLight(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList()));
        return response;
    }
}
