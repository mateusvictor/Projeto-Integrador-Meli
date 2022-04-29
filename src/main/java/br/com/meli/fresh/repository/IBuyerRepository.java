package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBuyerRepository extends JpaRepository<Buyer, String> {

    Buyer findByEmail(String email);
}
