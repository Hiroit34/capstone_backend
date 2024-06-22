package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.dto.request.status.StatusTaskRequest;
import it.epicode.whatsnextbe.dto.request.task.TaskRequest;
import it.epicode.whatsnextbe.dto.request.task.TaskRequestUpdate;
import it.epicode.whatsnextbe.dto.response.task.TaskResponse;
import it.epicode.whatsnextbe.dto.response.task.TaskResponseLight;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
            System.out.println("Authentication object is null");;
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
            TaskResponseLight dto = new TaskResponseLight();
            dto.setId(task.getId());
            dto.setName(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setStatus(task.getStatus().getStatus().toString());
            dto.setUsers(task.getUsers().stream().map(UserMapper::convertToUserResponseLight).collect(Collectors.toList()));
            System.out.println(task.getUsers());
            return dto;
        }).collect(Collectors.toList());

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
        System.out.println(taskRepository.findById(taskId).get());
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            boolean isAdmin = currentUser.getRoles().stream().anyMatch(role -> role.getTypeRole().equals("ADMIN"));

            if (isAdmin || (task.getUsers().size() == 1 && task.getUsers().contains(currentUser))) {
                for (User user : task.getUsers()) {
                    user.getTasks().remove(task);
                    userRepository.save(user);
                }
                task.setUsers(new ArrayList<>());
                taskRepository.save(task);
                taskRepository.deleteById(taskId);
                Task test = taskRepository.findAll().getFirst();
                System.out.println(test.getId());
                System.out.println("Task deleted successfully " + task.getId());
                return "Task deleted successfully";
            } else {
                return "You do not have permission to delete this task. Ask to an ADMIN";
            }
        } else {
            return "Task not found";
        }
    }

    public TaskResponse createTask(TaskRequest request, Principal principal) {
        Task entityTask = new Task();
        BeanUtils.copyProperties(request, entityTask);

        if (request.getStatus() == null) {
            Optional<Status> defaultStatus = statusRepository.findByStatus(Status.StatusType.NON_COMPLETATO);
            if (defaultStatus.isPresent()) {
                entityTask.setStatus(defaultStatus.get());
            } else {
                Status newStatus = new Status(Status.StatusType.NON_COMPLETATO);
                statusRepository.save(newStatus); // Save the new status
                entityTask.setStatus(newStatus);
            }
        } else {
            entityTask.setStatus(request.getStatus());
        }

        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + principal.getName()));

        List<User> users = new ArrayList<>();
        System.out.println(request.getUserIds());
        if (authenticationService.isAdmin(currentUser.getId())) {
            for (Long userId: request.getUserIds()) {
                System.out.println(request.getUserIds());
                User userFound = userRepository.findById(userId).get();
                System.out.println(userFound);
                userFound.getTasks().add(entityTask);
                entityTask.getUsers().add(userFound);
            }
        } else {
            users = List.of(currentUser);
            currentUser.getTasks().add(entityTask);
            entityTask.getUsers().add(currentUser);
        }
        entityTask.setUsers(users);
        System.out.println(users);

        Category entityCategory = categoryRepository.findById(request.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategory().getId()));
        entityTask.setCategory(entityCategory);

        Task savedTask = taskRepository.save(entityTask);
        System.out.println(savedTask);

        TaskResponse response = new TaskResponse();
        BeanUtils.copyProperties(savedTask, response);
        return response;
    }

//    public TaskResponse updateTask(Long id, TaskRequestUpdate request) {
//        Optional<Task> task = taskRepository.findById(id);
//        if (task.isPresent()) {
//            Task entityTask = task.get();
//            BeanUtils.copyProperties(request, entityTask, "id");
//
//            if (request.getStatus() != null) {
//                Optional<Status> statusOpt = statusRepository.findByStatus(request.getStatus().getStatus());
//                statusOpt.ifPresent(entityTask::setStatus);
//            }
//            return getTaskCompleteResponse(request, entityTask);
//        }
//        throw new RuntimeException("Task not found with id: " + id);
//    }

    public TaskResponse updateTask(Long id, TaskRequestUpdate request) {
        User currentUser = userService.getCurrentUser(); // Supponendo che tu abbia un metodo per ottenere l'utente corrente
        Optional<Task> taskOpt = taskRepository.findById(id);

        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();

            boolean isAdmin = currentUser.getRoles().stream()
                    .anyMatch(role -> role.getTypeRole().equals("ADMIN"));

            boolean isTaskAssignedToCurrentUser = task.getUsers().size() == 1 && task.getUsers().contains(currentUser);

            if (isAdmin || isTaskAssignedToCurrentUser) {
                BeanUtils.copyProperties(request, task, "id");

                if (request.getStatus() != null) {
                    Optional<Status> statusOpt = statusRepository.findByStatus(request.getStatus().getStatus());
                    statusOpt.ifPresent(task::setStatus);
                }
                taskRepository.save(task);
                return getTaskCompleteResponse(request, task);
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
                    System.out.println(statusOpt);
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

    private TaskResponse getTaskCompleteResponse(TaskRequestUpdate request, Task entityTask) {

        Category category = categoryRepository.findById(request.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategory().getId()));
        entityTask.setCategory(category);

        Task savedTask = taskRepository.save(entityTask);
        TaskResponse response = new TaskResponse();
        BeanUtils.copyProperties(savedTask, response);

        return response;
    }

}
