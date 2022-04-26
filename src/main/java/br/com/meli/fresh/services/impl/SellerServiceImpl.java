package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Seller;
import br.com.meli.fresh.model.exception.EmailAlreadyExistsException;
import br.com.meli.fresh.model.exception.SellerNotFoundException;
import br.com.meli.fresh.repository.ISellerRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SellerServiceImpl implements ICrudService<Seller> {

    private final ISellerRepository repository;

        @Override
        public Seller create(Seller seller) {
            try{
                return this.repository.save(seller);
            }catch(DataIntegrityViolationException e){
                throw new EmailAlreadyExistsException("Email already exists!");//////Alterar
            }
    }

    @Override
    public Seller update(String id, Seller seller) {
        seller.setId(id);
        this.repository.findById(id).orElseThrow(()-> new SellerNotFoundException("Seller not found!"));/////alterar
        return this.repository.save(seller);
    }

    @Override
    public Seller getById(String id) {
        return this.repository.findById(id).orElseThrow(()-> new SellerNotFoundException("Seller not found!"));
    }

    @Override
    public Page<Seller> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void delete(String id) {
        this.repository.delete(this.repository.findById(id).orElseThrow(()-> new SellerNotFoundException("Seller not found!")));
    }
}
