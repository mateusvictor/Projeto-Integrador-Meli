package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.connectors.RestClient;
import br.com.meli.fresh.model.*;
import br.com.meli.fresh.model.exception.*;
import br.com.meli.fresh.repository.IBatchRepository;
import br.com.meli.fresh.repository.ICartRepository;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.repository.IUserRepository;
import br.com.meli.fresh.security.UserSpringSecurity;
import br.com.meli.fresh.services.ICartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartServiceImpl implements ICartService {

    private final ICartRepository cartRepository;
    private final IProductRepository productRepository;
    private final IUserRepository buyerRepository;
    private final IBatchRepository batchRepository;
    private final UserAuthenticatedService userAuthenticatedService;



    @Override
    @Transactional()
    public Cart create(Cart cart) {
        cart.setDate(LocalDateTime.now());

        UserSpringSecurity auth = validationUser();

        User opBuyer = buyerRepository.findById(auth.getId())
                .orElseThrow(() -> new UserNotFoundException(auth.getId()));

        cart.setBuyer(opBuyer);

        List<CartItem> cartItems = cart.getItems().stream().map(item -> {
            CartItem cartItem = new CartItem();

            Product opProduct = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException(item.getProduct().getId()));

            haveEnoughProducts(opProduct.getBatchList(), item.getQuantity());
            cartItem.setQuantity(item.getQuantity());
            cartItem.setProduct(opProduct);

            return cartItem;
        }).collect(Collectors.toList());

        cart.setItems(cartItems);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional()
    public Cart update(String id) {
        UserSpringSecurity auth = validationUser();

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart not found."));

        if (cart.getCartStatus().equals(CartStatus.CLOSE)) {
            return cart;
        }

        cart.getItems().forEach(cartItem -> decreaseTheQuantityOfBatchProducts(cartItem.getProduct().getId(), cartItem.getQuantity()));

        cart.setCartStatus(CartStatus.CLOSE);
        User user = buyerRepository.findByEmail(auth.getEmail());
        RestClient.NotifyBuyAction(user);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public Cart getById(String id) {
        validationUser();
        return cartRepository.findById(id).orElseThrow(() -> new CartNotFoundException("Cart not found."));
    }

    // checks if the expiration date of a batch of products is greater than three weeks
    private boolean dueDateIsGreaterThanThreeWeeks(LocalDate dueDate) {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        int weekDueDateNumber = dueDate.get(weekFields.weekOfWeekBasedYear());
        int weekTotal = weekDueDateNumber - weekNumber;

        return weekTotal >= 3;
    }

    // checks if the quantity of products is enough for cart
    private void haveEnoughProducts(List<Batch> batches, int quantity) {
        String productName = null;
        int sum = 0;

        for (Batch batch : batches) {
            if(productName == null) {
                productName = batch.getProduct().getName();
            }
            if (dueDateIsGreaterThanThreeWeeks(batch.getDueDate())) {
                sum += batch.getCurrentQuantity();
            }
            if (sum >= quantity) {
                break;
            }
        }

        if (sum < quantity) {
            throw new InsufficientQuantityOfProductException("Insufficient quantity of the product " + productName + " for the request.");
        }
    }


    private void decreaseTheQuantityOfBatchProducts(String productId, int quantity) {
        List<Batch> batches = batchRepository.findAllByProduct_Id(productId);
        haveEnoughProducts(batches, quantity);

        int total = quantity;
        List<Batch> batchesUpdated = new ArrayList<>();
        for (Batch batch : batches) {
            if (total >= batch.getCurrentQuantity()) {
                total -= batch.getCurrentQuantity();
                batch.setCurrentQuantity(0);
            } else {
                batch.setCurrentQuantity(batch.getCurrentQuantity() - total);
                total = 0;
            }
            batchesUpdated.add(batch);
            if (total == 0) {
                break;
            }
        }
        batchRepository.saveAll(batchesUpdated);
    }

    private UserSpringSecurity validationUser() {
        UserSpringSecurity auth = userAuthenticatedService.authenticated();

        if(auth == null || (!auth.hasRole(Role.BUYER) && !auth.hasRole(Role.ADMIN))) {
            throw new UserNotAllowedException("User not allowed.");
        }

        return auth;
    }
}
