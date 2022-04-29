package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.model.exception.BuyerNotFoundException;
import br.com.meli.fresh.model.exception.EmailAlreadyExistsException;
import br.com.meli.fresh.repository.IBuyerRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BuyerServiceImpl implements ICrudService<Buyer> {

    private final IBuyerRepository repository;

    private final BCryptPasswordEncoder pe;

    @Override
    public Buyer create(Buyer buyer) {
        try{
            buyer.setPassword(pe.encode(buyer.getPassword()));
            return this.repository.save(buyer);
        }catch(DataIntegrityViolationException e){
            throw new EmailAlreadyExistsException("Email already exists!");
        }

    }

    @Override
    public Buyer update(String id, Buyer buyer) {
        buyer.setId(id);
        this.repository.findById(id).orElseThrow(()-> new BuyerNotFoundException("Buyer not found!"));
        return this.repository.save(buyer);
    }

    @Override
    public Buyer getById(String id) {
        return this.repository.findById(id).orElseThrow(()-> new BuyerNotFoundException("Buyer not found!"));
    }

    @Override
    public Page<Buyer> getAll(Pageable pageable) {

        return repository.findAll(pageable);
    }

    @Override
    public void delete(String id) {
        this.repository.delete(this.repository.findById(id).orElseThrow(()-> new BuyerNotFoundException("Buyer not found!")));

    }
}
