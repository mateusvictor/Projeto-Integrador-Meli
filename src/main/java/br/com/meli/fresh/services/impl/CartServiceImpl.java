package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.*;
import br.com.meli.fresh.model.exception.BuyerNotFoundException;
import br.com.meli.fresh.model.exception.CartNotFoundException;
import br.com.meli.fresh.model.exception.InsufficientQuantityOfProductException;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
import br.com.meli.fresh.repository.*;
import br.com.meli.fresh.services.ICartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartServiceImpl implements ICartService {

    private ICartRepository cartRepository;
    private IProductRepository productRepository;
    private IBuyerRepository buyerRepository;
    private IBatchRepository batchRepository;

    @Override
    public Cart create(Cart cart) {
        cart.setDate(LocalDateTime.now());

        Buyer opBuyer = buyerRepository.findById(cart.getBuyer().getId())
                .orElseThrow(() -> new BuyerNotFoundException("Buyer not found."));

        cart.setBuyer(opBuyer);

        List<CartItem> cartItems = cart.getItems().stream().map(item -> {
            CartItem cartItem = new CartItem();

            Product opProduct = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found."));

            isQuantityValid(opProduct.getBatchList(), item.getQuantity());
            cartItem.setQuantity(item.getQuantity());
            cartItem.setProduct(opProduct);

            return cartItem;
        }).collect(Collectors.toList());

        cart.setItems(cartItems);
        return cartRepository.save(cart);
    }

    @Override
    public Cart update(String id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart not found."));

        if (cart.getCartStatus().equals(CartStatus.CLOSE)) {
            return cart;
        }

        cart.getItems().stream().forEach(cartItem -> {
            decrementOfBatchAndProductQuantity(cartItem.getProduct().getId(), cartItem.getQuantity());
        });

        cart.setCartStatus(CartStatus.CLOSE);
        return cartRepository.save(cart);
    }

    @Override
    public Cart getById(String id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart not found."));

        return cart;
    }


    private boolean dueDateIsNoLessThanThreeWeeks(LocalDate dueDate) {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        int weekDueDateNumber = dueDate.get(weekFields.weekOfWeekBasedYear());
        int weekTotal = weekDueDateNumber - weekNumber;

        if (weekTotal >= 3) {
            return true;
        }
        return false;
    }

    private void isQuantityValid(List<Batch> batches, int quantity) {
        String productName = null;
        int sum = 0;

        for (Batch batch : batches) {
            if(productName == null) {
                productName = batch.getProduct().getName();
            }
            if (dueDateIsNoLessThanThreeWeeks(batch.getDueDate())) {
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

    private void decrementOfBatchAndProductQuantity(String productId, int quantity) {
        List<Batch> batches = batchRepository.findAllByProduct_Id(productId);
        isQuantityValid(batches, quantity);

        int total = quantity;
        List<Batch> batchesUpdated = new ArrayList<>();
        for (Batch batch : batches) {
            if (total >= batch.getCurrentQuantity()) {
                total -= batch.getCurrentQuantity();
                batch.setCurrentQuantity(0);
            } else if (total < batch.getCurrentQuantity()) {
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
}
