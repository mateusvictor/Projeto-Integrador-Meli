package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.exception.UserNotFoundException;
import br.com.meli.fresh.model.exception.UserWithThisEmailAlreadyExists;
import br.com.meli.fresh.repository.IUserRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements ICrudService<User> {

    private final IUserRepository repository;

    private final BCryptPasswordEncoder pe;

    @Override
    public User create(User user) {
        user.setPassword(pe.encode(user.getPassword()));
        return this.saveEntity(user);
    }

    @Override
    public User update(String id, User user) {
        this.repository.findById(id).orElseThrow(()-> new UserNotFoundException((id)));
        user.setId(id);
        return this.saveEntity(user);
    }

    private User saveEntity(User user) {
        try{
            return this.repository.save(user);
        }catch(DataIntegrityViolationException e){
            throw new UserWithThisEmailAlreadyExists(user.getEmail());
        }
    }

    @Override
    public User getById(String id) {
        return this.repository.findById(id).orElseThrow(()-> new UserNotFoundException((id)));
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public void delete(String id) {
        this.repository.delete(this.repository.findById(id).orElseThrow(()-> new UserNotFoundException((id))));

    }
}
