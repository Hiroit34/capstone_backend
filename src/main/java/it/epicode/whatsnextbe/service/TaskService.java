package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.dto.request.task.TaskRequest;
import it.epicode.whatsnextbe.dto.response.task.TaskResponse;
import it.epicode.whatsnextbe.model.Category;
import it.epicode.whatsnextbe.model.Status;
import it.epicode.whatsnextbe.model.Task;
import it.epicode.whatsnextbe.model.User;
import it.epicode.whatsnextbe.repository.CategoryRepository;
import it.epicode.whatsnextbe.repository.StatusRepository;
import it.epicode.whatsnextbe.repository.TaskRepository;
import it.epicode.whatsnextbe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    //GET, GET BY ID, POST, PUT, DELETE

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private AuthenticationSerivce authenticationService;

    // GET ALL
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // GET BY ID
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

    // DELETE TASK
    public String deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return "Task deleted successfully.";
        }
        return "Task not found with id: " + id;
    }

    // CREATE A NEW TASK
//    public TaskCompleteResponse createTask(TaskRequest request) {
//        Task entityTask = new Task();
//        BeanUtils.copyProperties(request, entityTask);
//        setDefaultStatus(entityTask);
//
//        return getTaskCompleteResponse(request, entityTask);
//    }
    public TaskResponse createTask(TaskRequest request, Principal principal) {
        Task entityTask = new Task();
        BeanUtils.copyProperties(request, entityTask);

        // Set default status if not provided
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

        // Retrieve current user
        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + principal.getName()));

        // Check if the user is an admin
        List<User> users;
        if (authenticationService.isAdmin(currentUser.getId())) {
            // Admin can assign the task to any users
            users = request.getUserIds().stream()
                    .map(userId -> userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)))
                    .collect(Collectors.toList());
        } else {
            // Regular user can only assign the task to themselves
            users = List.of(currentUser);
        }
        entityTask.setUsers(users);

        // Set category
        Category entityCategory = categoryRepository.findById(request.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategory().getId()));
        entityTask.setCategory(entityCategory);

        // Save the task
        Task savedTask = taskRepository.save(entityTask);

        // Create response
        TaskResponse response = new TaskResponse();
        BeanUtils.copyProperties(savedTask, response);
        return response;
    }



    // UPDATE TASK
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            Task entityTask = task.get();
            BeanUtils.copyProperties(request, entityTask, "id");

            if (request.getStatus() != null) {
                Optional<Status> statusOpt = statusRepository.findByStatus(request.getStatus().getStatus());
                statusOpt.ifPresent(entityTask::setStatus);
            }

            return getTaskCompleteResponse(request, entityTask);
        }
        throw new RuntimeException("Task not found with id: " + id);
    }

    private TaskResponse getTaskCompleteResponse(TaskRequest request, Task entityTask) {
        List<User> users = request.getUserIds().stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)))
                .collect(Collectors.toList());
        entityTask.setUsers(users);

        Category category = categoryRepository.findById(request.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategory().getId()));
        entityTask.setCategory(category);

        Task savedTask = taskRepository.save(entityTask);
        TaskResponse response = new TaskResponse();
        BeanUtils.copyProperties(savedTask, response);

        return response;
    }

    private void setDefaultStatus(Task task) {
        if (task.getStatus() == null) {
            Optional<Status> defaultStatusOpt = statusRepository.findByStatus(Status.StatusType.NON_COMPLETATO);
            if (defaultStatusOpt.isPresent()) {
                task.setStatus(defaultStatusOpt.get());
            } else {
                Status defaultStatus = new Status(Status.StatusType.NON_COMPLETATO);
                statusRepository.save(defaultStatus);
                task.setStatus(defaultStatus);
            }
        }
    }
}
