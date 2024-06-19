package it.epicode.whatsnextbe.controller;

import it.epicode.whatsnextbe.dto.request.task.TaskRequest;
import it.epicode.whatsnextbe.dto.response.task.TaskResponse;
import it.epicode.whatsnextbe.model.Task;
import it.epicode.whatsnextbe.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Task>> getAllTask() {
        List<Task> task = taskService.getAllTasks();
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
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request, Principal principal) {
        TaskResponse createdTask = taskService.createTask(request, principal);
        return ResponseEntity.ok(createdTask);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> modifyTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        TaskResponse updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        String responseMessage = taskService.deleteTask(id);
        return ResponseEntity.ok(responseMessage);
    }

}
