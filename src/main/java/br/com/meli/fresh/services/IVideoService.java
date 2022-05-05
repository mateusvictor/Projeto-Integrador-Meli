package br.com.meli.fresh.services;

import br.com.meli.fresh.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVideoService {
    Video createByBuyer(Video t);
    Video getById(String id);
    Page<Video> getAllByProduct(String productId, Pageable pageable);
    Page<Video> getAllBySeller(boolean status, Pageable pageable);
    Video changeApproval(String id, boolean approvalStatus);
    void deleteById(String id);
}
