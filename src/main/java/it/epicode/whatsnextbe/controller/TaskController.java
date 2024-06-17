package it.epicode.whatsnextbe.controller;

import it.epicode.whatsnextbe.dto.request.task.TaskRequest;
import it.epicode.whatsnextbe.dto.response.task.TaskCompleteResponse;
import it.epicode.whatsnextbe.model.Task;
import it.epicode.whatsnextbe.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TaskCompleteResponse> getTaskById(@PathVariable Long id) {
        if (taskService.getTaskById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // POST
    @PostMapping
    public ResponseEntity<TaskCompleteResponse> createCategory(@RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<TaskCompleteResponse> modifyTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }

}
