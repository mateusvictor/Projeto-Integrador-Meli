package br.com.meli.fresh.unit;

import br.com.meli.fresh.factory.ProductFactory;
import br.com.meli.fresh.factory.UserFactory;
import br.com.meli.fresh.factory.VideoFactory;
import br.com.meli.fresh.model.Product;
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
import br.com.meli.fresh.services.impl.UserAuthenticatedService;
import br.com.meli.fresh.services.impl.VideoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class VideoServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IVideoRepository videoRepository;

    @Mock
    private UserAuthenticatedService authService;

    @InjectMocks
    private VideoServiceImpl videoService;

    private final int ROLE_BUYER = 1;
    private final int ROLE_SELLER = 3;

    public UserSpringSecurity getAuthenticatedUser(User user){
        Collection<GrantedAuthority> grantedAuthorities = user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new UserSpringSecurity(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                grantedAuthorities
        );
    }


    @Test
    void testCreateVideoByUser() {
        User user = UserFactory.getUserEntityA();
        user.setRoles(Set.of(ROLE_BUYER));
        Product product = ProductFactory.createProduct();
        Video video = VideoFactory.createVideo();
        video.setUser(user);
        video.setProduct(product);

        Mockito.when(authService.authenticated()).thenReturn(this.getAuthenticatedUser(user));
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));
        Mockito.when(videoRepository.save(video)).thenReturn(video);

        Video response = videoService.createByBuyer(video);
        assertNotNull(response);
        assertEquals(video.getTitle(), response.getTitle());
        assertEquals(video.getUrl(), response.getUrl());
        assertEquals(video.getLength(), response.getLength());
    }

    @Test
    void testCreateVideoByUserThrowsUserNotAllowedException() {
        User user = UserFactory.getUserEntityA();
        user.setRoles(Set.of(ROLE_SELLER));
        Video video = VideoFactory.createVideo();
        video.setUser(user);

        Mockito.when(authService.authenticated()).thenThrow(UserNotAllowedException.class);

        assertThrows(UserNotAllowedException.class, () -> videoService.createByBuyer(video));
    }

    @Test
    void testCreateVideoByUserThrowsUserNotFoundException() {
        User user = UserFactory.getUserEntityA();
        user.setId("123");
        user.setRoles(Set.of(ROLE_BUYER));
        Video video = VideoFactory.createVideo();
        video.setUser(user);

        Mockito.when(authService.authenticated()).thenReturn(this.getAuthenticatedUser(user));
        Mockito.when(userRepository.findById(Mockito.any())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> videoService.createByBuyer(video));
    }

    @Test
    void testCreateVideoByUserThrowsProductNotFoundException() {
        User user = UserFactory.getUserEntityA();
        user.setRoles(Set.of(ROLE_BUYER));
        Product product = ProductFactory.createProduct();
        product.setId("123");
        Video video = VideoFactory.createVideo();
        video.setUser(user);
        video.setProduct(product);

        Mockito.when(authService.authenticated()).thenReturn(this.getAuthenticatedUser(user));
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findById(Mockito.any())).thenThrow(ProductNotFoundException.class);

        assertThrows(ProductNotFoundException.class, () -> videoService.createByBuyer(video));
    }


    @Test
    void testGetVideoById() {
        Video video = VideoFactory.createVideo();
        video.setId("123");
        Mockito.when(videoRepository.findById(video.getId())).thenReturn(Optional.of(video));

        assertNotNull(videoService.getById(video.getId()));
    }

    @Test
    void testGetVideoByIdThrowsVideoNotFoundException() {
        assertThrows(VideoNotFoundException.class, () -> videoService.getById("invalid_id"));
    }

    @Test
    void testGetAllByProduct() {
        Product product = ProductFactory.createProduct();
        product.setId("123");
        Video video = VideoFactory.createVideo();

        Pageable pageable = Pageable.unpaged();

        Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));
        Mockito.when(videoRepository.findAllByProduct_IdAndApproved(product.getId(), true, pageable)).thenReturn(new PageImpl<>(Collections.singletonList(video)));

        Page<Video> response = videoService.getAllByProduct(product.getId(),  pageable);
        List<Video> result = response.stream().collect(Collectors.toList());
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllByProductThrowsProductNotFoundException() {
        Pageable pageable = Pageable.unpaged();
        assertThrows(ProductNotFoundException.class, () -> videoService.getAllByProduct("invalid_id",  pageable));
    }

    @Test
    void testGetAllBySeller() {
        User user = UserFactory.getUserEntityA();
        user.setId("123");
        user.setRoles(Set.of(ROLE_SELLER));
        Video video = VideoFactory.createVideo();
        video.setUser(user);

        Pageable pageable = Pageable.unpaged();

        Mockito.when(authService.authenticated()).thenReturn(this.getAuthenticatedUser(user));
        Mockito.when(videoRepository.findAllByUser_IdAndApproved(user.getId(), true, pageable)).thenReturn(new PageImpl<>(Collections.singletonList(video)));

        Page<Video> response = videoService.getAllBySeller(true, pageable);
        List<Video> result = response.stream().collect(Collectors.toList());
        assertEquals(1, result.size());
    }


    @Test
    void testGetAllBySellerThrowsUserNotAllowedException() {
        User user = UserFactory.getUserEntityA();
        user.setId("123");
        // role without permission
        user.setRoles(Set.of(ROLE_BUYER));
        Video video = VideoFactory.createVideo();
        video.setUser(user);

        Mockito.when(authService.authenticated()).thenReturn(this.getAuthenticatedUser(user));
        Pageable pageable = Pageable.unpaged();

        assertThrows(UserNotAllowedException.class, () -> videoService.getAllBySeller(true, pageable));
    }


    @Test
    void testChangeApproval() {
        User user = UserFactory.getUserEntityA();
        user.setId("123");
        user.setRoles(Set.of(ROLE_SELLER));
        Video video = VideoFactory.createVideo();
        video.setId("123");
        // set default status video for false
        video.setApproved(false);
        video.setUser(user);

        Mockito.when(authService.authenticated()).thenReturn(this.getAuthenticatedUser(user));
        Mockito.when(videoRepository.findById(video.getId())).thenReturn(Optional.of(video));
        Mockito.when(videoRepository.save(video)).thenReturn(video);

        Video response = videoService.changeApproval(video.getId(), true);

        assertTrue(response.isApproved());
    }

    @Test
    void testChangeApprovalThrowsVideoNotFoundException() {
        User user = UserFactory.getUserEntityA();
        user.setId("123");
        user.setRoles(Set.of(ROLE_SELLER));

        Mockito.when(authService.authenticated()).thenReturn(this.getAuthenticatedUser(user));
        Mockito.when(videoRepository.findById("invalid_id")).thenThrow(VideoNotFoundException.class);

        assertThrows(VideoNotFoundException.class, () -> videoService.changeApproval("invalid_id", true));

    }


    @Test
    void testChangeApprovalThrowsUserNotAllowedException() {
        User user = UserFactory.getUserEntityA();
        user.setId("123");
        // role without permission
        user.setRoles(Set.of(ROLE_BUYER));

        assertThrows(UserNotAllowedException.class, () -> videoService.changeApproval("invalid_id", true));
    }

    @Test
    void testDeleteById() {
        User user = UserFactory.getUserEntityA();
        user.setId("123");
        user.setRoles(Set.of(ROLE_SELLER));
        Video video = VideoFactory.createVideo();
        video.setId("123");

        videoRepository.deleteById(video.getId());
        Mockito.verify(videoRepository, Mockito.times(1)).deleteById(video.getId());
    }


    @Test
    void testDeleteByIdThrowsUserNotAllowedException() {
        User user = UserFactory.getUserEntityA();
        user.setId("123");
        // role without permission
        user.setRoles(Set.of(ROLE_BUYER));
        Video video = VideoFactory.createVideo();
        video.setId("123");

        assertThrows(UserNotAllowedException.class, () -> videoService.deleteById("invalid_id"));
    }

}
