package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.GeneralUser;
import br.com.meli.fresh.model.exception.EmailAlreadyExistsException;
import br.com.meli.fresh.repository.IGeneralUserRepository;
import br.com.meli.fresh.services.ICrudService;
import br.com.meli.fresh.services.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GeneralUserServiceImpl implements ICrudService<GeneralUser> {

    private final IGeneralUserRepository repository;

    @Override
    public GeneralUser create(GeneralUser generalUser) {
        return this.saveEntity(generalUser);
    }

    @Override
    public GeneralUser update(String id, GeneralUser generalUser) {
        this.repository.findById(id).orElseThrow(()->new EntityNotFoundException("User not Found!"));
        generalUser.setId(id);
        return this.saveEntity(generalUser);
    }

    private GeneralUser saveEntity(GeneralUser generalUser) {
        try{
            return this.repository.save(generalUser);
        }catch(DataIntegrityViolationException e){
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    @Override
    public GeneralUser getById(String id) {
        return this.repository.findById(id).orElseThrow(()->new EntityNotFoundException("User not Found!"));
    }

    @Override
    public Page<GeneralUser> getAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public void delete(String id) {
        this.repository.delete(this.repository.findById(id).orElseThrow(()->new EntityNotFoundException("User not Found!")));

    }
}
