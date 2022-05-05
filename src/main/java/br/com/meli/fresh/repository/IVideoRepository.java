package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVideoRepository extends JpaRepository<Video, String> {
    Page<Video> findAllByProduct_IdAndApproved(String productId, Boolean approved, Pageable pageable);
    Page<Video> findAllByUser_IdAndApproved(String userId, Boolean approved, Pageable pageable);

}
