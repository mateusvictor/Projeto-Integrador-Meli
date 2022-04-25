package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.OrderStatus;
import br.com.meli.fresh.model.ProductOrder;
import br.com.meli.fresh.model.PurchaseOrder;
import br.com.meli.fresh.repository.IBuyerRepository;
import br.com.meli.fresh.repository.IPurchaseOrderRepository;
import br.com.meli.fresh.repository.IProductRepository;
import br.com.meli.fresh.services.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PurchaseOrderServiceImpl implements ICrudService<PurchaseOrder> {

    private IPurchaseOrderRepository productOrderRepository;
    private IProductRepository productRepository;
    private IBuyerRepository buyerRepository;

    @Override
    public PurchaseOrder create(PurchaseOrder purchaseOrder) {

       List<ProductOrder> productOrderList = purchaseOrder.getProducts()
                .stream().map(po -> {
                    ProductOrder npo = new ProductOrder();
                    npo.setQuantity(po.getQuantity());
                    npo.setProduct(productRepository.getById(po.getId()));
                    npo.setPurchaseOrder(purchaseOrder);
                    return npo;
                }).collect(Collectors.toList());

       purchaseOrder.setProducts(productOrderList);
       purchaseOrder.setBuyer(buyerRepository.getById(purchaseOrder.getBuyer().getId()));

        return productOrderRepository.save(purchaseOrder);
    }

    @Override
    public PurchaseOrder update(String id, PurchaseOrder purchaseOrder) {
        return null;
    }

    @Override
    public PurchaseOrder getById(String id) {
        return null;
    }

    @Override
    public Page<PurchaseOrder> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    public void createOrder(OrderStatus status, ProductOrder order, String buyerId){

    }
}
