package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.*;
import br.com.meli.fresh.model.exception.BuyerNotFoundException;
import br.com.meli.fresh.model.exception.InvalidEnumCartStatusException;
import br.com.meli.fresh.model.exception.ProductNotFoundException;
import br.com.meli.fresh.repository.*;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;

import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartServiceImpl implements ICrudService<Cart> {

    private ICartRepository cartRepository;
    private IProductRepository productRepository;
    private IBuyerRepository buyerRepository;
    private ICartItemRepository cartItemRepository;
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

            System.out.println(item.getQuantity());
            cartItem.setQuantity(item.getQuantity());

            cartItem.setProduct(opProduct);

            return cartItem;
        }).collect(Collectors.toList());

       cart.setItems(cartItems);
        return   cartRepository.save(cart);
    }

    @Override
    public Cart update(String id, Cart cart) {
        return null;
    }

    @Override
    public Cart getById(String id) {
        return null;
    }

    @Override
    public Page<Cart> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    public void createOrder(CartStatus status, CartItem order, String buyerId) {

    }

    public void isQuantityValid(String productId, int quantity){

        List<Batch> batches = batchRepository.findAllByProduct_Id(productId);
        int sum = 0;

        for (Batch batch : batches) {
            sum += batch.getCurrentQuantity();
        }
        if (sum < quantity){
            throw new IllegalArgumentException();
        }

    }

    public void decrementOfBatchAndProductQuantity(String productId, int quantity){
        List<Batch> batches = batchRepository.findAllByProduct_Id(productId);
        //reduce
        int total = quantity;
        for(Batch batch : batches){
            if(batch.getCurrentQuantity() <= total){
                total -= batch.getCurrentQuantity();
                batch.setCurrentQuantity(0);
            } else {
                batch.setCurrentQuantity(batch.getCurrentQuantity() - total);
            }
            if(total == 0){
                break;
            }
        }
        batchRepository.saveAll(batches);
    }


}
