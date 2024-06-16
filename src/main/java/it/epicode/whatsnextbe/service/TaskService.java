package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.dto.request.task.TaskRequest;
import it.epicode.whatsnextbe.dto.response.task.TaskCompeteResponse;
import it.epicode.whatsnextbe.model.Task;
import it.epicode.whatsnextbe.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    //GET, GET BY ID, POST, PUT, DELETE

    @Autowired
    private TaskRepository taskRepository;

    //GET ALL
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    //GET BY ID
    public TaskCompeteResponse getTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task with id " + id + " not found");
        }
        Task entity = taskRepository.findById(id).get();
        TaskCompeteResponse response = new TaskCompeteResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    //POST
    public TaskCompeteResponse createTask(TaskRequest request) {
        Task entity = new Task();
        BeanUtils.copyProperties(request, entity);
        taskRepository.save(entity);
        TaskCompeteResponse response = new TaskCompeteResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    //PUT
    public TaskCompeteResponse updateTask(Long id, TaskRequest request) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task with id " + id + " not found");
        }
        Task entity = taskRepository.findById(id).get();
        BeanUtils.copyProperties(request, entity);
        taskRepository.save(entity);
        TaskCompeteResponse response = new TaskCompeteResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    public String deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task with id " + id + " not found");
        }
        taskRepository.deleteById(id);
        return "Task with id " + id + " deleted";
    }

}
