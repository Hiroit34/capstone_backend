package it.epicode.whatsnextbe.service;

import it.epicode.whatsnextbe.dto.request.role.RoleRequest;
import it.epicode.whatsnextbe.dto.response.role.RoleResponse;
import it.epicode.whatsnextbe.model.Role;
import it.epicode.whatsnextbe.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    //GET, GET BY ID, POST, PUT, DELETE

    @Autowired
    private RoleRepository roleRepository;

    //GET ALL
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    //GET BY ID
    public RoleResponse getRoleById(Long id) {
        if (roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role with id " + id + " not found");
        }
        Role entity = roleRepository.findById(id).get();
        RoleResponse response = new RoleResponse();
        BeanUtils.copyProperties(entity, response );
        return response;
    }

    //POST
    public RoleResponse createRole(RoleRequest role) {
        Role entity = new Role();
        BeanUtils.copyProperties(role, entity);
        RoleResponse response = new RoleResponse();
        BeanUtils.copyProperties(entity, response );
        roleRepository.save(entity);
        return response;
    }

    //PUT
    public RoleResponse updateRole(Long id, RoleRequest role) {
        if(!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role with id " + id + " not found");
        }
        Role entity = roleRepository.findById(id).get();
        BeanUtils.copyProperties(role, entity);
        roleRepository.save(entity);
        RoleResponse response = new RoleResponse();
        BeanUtils.copyProperties(entity, response );
        return response;
    }

    //DELETE
    public String deleteRole(Long id) {
        if(!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role with id " + id + " not found");
        }
        roleRepository.deleteById(id);
        return "Role with id " + id + " deleted";
    }
}
