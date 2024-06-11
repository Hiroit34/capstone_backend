package it.epicode.whatsnextbe.category;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    //Metodo GET che restituisce tutte le categorie
    public List<Category> findAll() {
        return repository.findAll();
    }
    // Metodo GET che restituisce una categoria fornendo un' ID.
    public Response findById(Long id) {
        // Verifico l' esistenza di una Category con ID fornito.
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("GET: Category with provided id not found");
        }
        // Se la Category esiste. Creo una variabile entity ed utilizzo il metodo findById
        // per poterlo assegnare ad entity
        Category entity = repository.findById(id).get();
        // Creo la istanza di Response che si mostra al Client
        Response response = new Response();
        // Copio le proprieta dell'entity nella response
        BeanUtils.copyProperties(entity, response);
        // Ritorno la response.
        return response;
    }

    // Metodo POST che permette di creare una nuova categoria che ha come paramentro
    // la request effettuata dal client con il body.
    public Response create(Request request) {
        // Creo una istanza di Category chiamata entity.
        Category entity = new Category();
        // Copio le proprieta della request(client-side) nell'entity
        BeanUtils.copyProperties(request, entity);
        // Creo una istanza di Response con le proprieta da mostrare al client
        Response response = new Response();
        // Copio le proprieta' dell'entity che sono state fillate nella response.
        BeanUtils.copyProperties(entity, response);
        // Salvo l'entity nel database
        repository.save(entity);
        // Ritorno la response da mostrare al Client.
        return response;
    }

    //Metodo PUT dove viene fornito un'ID e il Body(request9)
    public  Response modify(Long id, Request request) {
        if(!repository.existsById(id)) {
            throw new EntityNotFoundException("PUT: Category with provided ID not found");
        }
        Category entity = repository.findById(id).get();
        BeanUtils.copyProperties(request, entity);
        repository.save(entity);
        Response response = new Response();
        BeanUtils.copyProperties(entity, response);
        return response;
    }


    // Metodo DELETE tramite id
    public String delete(Long id) {
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("DELETE: Category with privided ID not found");
        }
        repository.deleteById(id);
        return "Category deleted successfully";
    }
}
