package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISellerRepository extends JpaRepository<Seller, String> {

    Seller findByEmail(String email);
}
