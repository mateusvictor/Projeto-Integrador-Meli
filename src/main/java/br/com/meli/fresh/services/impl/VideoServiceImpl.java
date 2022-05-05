package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.Role;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.Video;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
import br.com.meli.fresh.model.exception.UserNotAllowedException;
import br.com.meli.fresh.model.exception.UserNotFoundException;
import br.com.meli.fresh.model.exception.VideoNotFoundException;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.repository.IUserRepository;
import br.com.meli.fresh.repository.IVideoRepository;
import br.com.meli.fresh.security.UserSpringSecurity;
import br.com.meli.fresh.services.IVideoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class VideoServiceImpl implements IVideoService {

    private final IVideoRepository videoRepository;
    private final UserAuthenticatedService userAuthenticatedService;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;


    @Override
    @Transactional
    public Video createByBuyer(Video video) {
        UserSpringSecurity auth = validationUser(Role.BUYER);
        User user = userRepository.findById(auth.getId()).orElseThrow(() -> new UserNotFoundException("User not found."));
        Product product = productRepository.findById(video.getProduct().getId()).orElseThrow(() -> new ProductNotFoundException("Product not found."));

        video.setProduct(product);
        video.setUser(user);

        return videoRepository.save(video);
    }

    @Override
    @Transactional(readOnly = true)
    public Video getById(String id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new VideoNotFoundException(id));

        return video;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Video> getAllByProduct(String productId, Pageable pageable) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        return videoRepository.findAllByProduct_IdAndApproved(product.getId(), true, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Video> getAllBySeller(boolean status, Pageable pageable) {
        UserSpringSecurity auth = validationUser(Role.SELLER);
        return videoRepository.findAllByUser_IdAndApproved(auth.getId(), status, pageable);
    }

    @Override
    @Transactional
    public Video changeApproval(String id, boolean approvalStatus) {
        validationUser(Role.SELLER);
        Video video = this.getById(id);
        video.setApproved(approvalStatus);
        return videoRepository.save(video);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        validationUser(Role.SELLER);
        videoRepository.deleteById(id);
    }

    private UserSpringSecurity validationUser(Role userRole) {
        UserSpringSecurity auth = userAuthenticatedService.authenticated();

        if(auth == null || (!auth.hasRole(userRole) && !auth.hasRole(Role.ADMIN))) {
            throw new UserNotAllowedException("User not allowed.");
        }

        return auth;
    }


}