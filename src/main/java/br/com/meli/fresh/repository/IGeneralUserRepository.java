package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.GeneralUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGeneralUserRepository extends JpaRepository<GeneralUser, String> {
}
