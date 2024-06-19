package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.dto.request.status.StatusRequest;
import it.epicode.whatsnextbe.dto.response.status.StatusResponse;
import it.epicode.whatsnextbe.model.Status;
import it.epicode.whatsnextbe.repository.StatusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    //GET
    public List<Status> getAll() {
        return statusRepository.findAll();
    }

    //GET BY ID
    public StatusResponse getById(Long id) {
        if (!statusRepository.existsById(id)) {
            throw new EntityNotFoundException("No status found with id: " + id);
        }
        Status entity = statusRepository.findById(id).get();
        StatusResponse response = new StatusResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    //POST
//    public StatusResponse createStatus(StatusRequest request) {
//        Status entity = new Status();
//        StatusResponse response = new StatusResponse();
//        BeanUtils.copyProperties(request, entity);
//        BeanUtils.copyProperties(entity, response);
//        statusRepository.save(entity);
//        return response;
//    }

    //PUT
    public StatusResponse modifyStatus(Long id, StatusRequest request) {
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

    //DELETE
    public String deleteStatus(Long id) {
        if (!statusRepository.existsById(id)) {
            throw new EntityNotFoundException("No status found with id: " + id);
        }
        statusRepository.deleteById(id);
        return "Status deleted";
    }
}
