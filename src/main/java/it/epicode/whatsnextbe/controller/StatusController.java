package it.epicode.whatsnextbe.controller;

import it.epicode.whatsnextbe.dto.request.status.StatusRequest;
import it.epicode.whatsnextbe.dto.response.status.StatusResponse;
import it.epicode.whatsnextbe.model.Status;
import it.epicode.whatsnextbe.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    StatusService statusService;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Status>> getAllStatus() {
        List<Status> status = statusService.getAll();
        if (status.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(status);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponse> getStatusById(@PathVariable Long id) {
        if (statusService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(statusService.getById(id));
    }

    // POST
    @PostMapping
    public ResponseEntity<StatusResponse> createCategory(@RequestBody StatusRequest request) {
        return ResponseEntity.ok(statusService.createStatus(request));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<StatusResponse> modifyStatus(@PathVariable Long id, @RequestBody StatusRequest request) {
        return ResponseEntity.ok(statusService.modifyStatus(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStatus(@PathVariable Long id) {
        return ResponseEntity.ok(statusService.deleteStatus(id));
    }

}
