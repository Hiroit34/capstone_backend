package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.dto.request.status.StatusTaskRequest;
import it.epicode.whatsnextbe.dto.response.status.StatusResponse;
import it.epicode.whatsnextbe.model.Status;
import it.epicode.whatsnextbe.repository.StatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository statusRepository;

    public List<Status> getAll() {
        return statusRepository.findAll();
    }

    public StatusResponse getById(Long id) {
        if (!statusRepository.existsById(id)) {
            throw new EntityNotFoundException("No status found with id: " + id);
        }
        Status entity = statusRepository.findById(id).get();
        StatusResponse response = new StatusResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    public StatusResponse modifyStatus(Long id, StatusTaskRequest request) {
        if (!statusRepository.existsById(id)) {
            throw new EntityNotFoundException("No status found with id: " + id);
        }
        Status entity = statusRepository.findById(id).get();
        BeanUtils.copyProperties(request, entity);
        statusRepository.save(entity);
        StatusResponse response = new StatusResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    public String deleteStatus(Long id) {
        if (!statusRepository.existsById(id)) {
            throw new EntityNotFoundException("No status found with id: " + id);
        }
        statusRepository.deleteById(id);
        return "Status deleted";
    }
}
