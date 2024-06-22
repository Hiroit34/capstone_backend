package it.epicode.whatsnextbe.controller;

import it.epicode.whatsnextbe.dto.request.status.StatusTaskRequest;
import it.epicode.whatsnextbe.dto.request.task.TaskRequest;
import it.epicode.whatsnextbe.dto.request.task.TaskRequestUpdate;
import it.epicode.whatsnextbe.dto.response.task.TaskResponse;
import it.epicode.whatsnextbe.dto.response.task.TaskResponseLight;
import it.epicode.whatsnextbe.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<TaskResponseLight>> getAllTask() {
        List<TaskResponseLight> task = taskService.getAllTasks();
        if (task.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        if (taskService.getTaskById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // POST
    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request, Principal principal) {
        TaskResponse createdTask = taskService.createTask(request, principal);
        return ResponseEntity.ok(createdTask);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> modifyTask(@PathVariable Long id, @RequestBody TaskRequestUpdate request) {
        TaskResponse updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long id, @RequestBody StatusTaskRequest request) {
        TaskResponse updatedTask = taskService.updateTaskStatus(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    // DELETE
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        String responseMessage =taskService.deleteTask(id);
        if (responseMessage.equals("Task deleted successfully")) {
            return ResponseEntity.ok(responseMessage);
        } else {
            return ResponseEntity.status(403).body(responseMessage); // 403 Forbidden for permission issues
        }
    }

}
