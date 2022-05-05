package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.ProductComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

public class ProductCommentFactory {

    public static ProductComment createCommentA(){
        return ProductComment.builder().id("1").product(ProductFactory.getFreshProductA()).buyer(UserFactory.getUserEntityA()).description("testA").build();
    }

    public static ProductComment createCommentB(){
        return ProductComment.builder().id("2").product(ProductFactory.getFreshProductA()).buyer(UserFactory.getUserEntityB()).description("testB").build();
    }

    public static Page<ProductComment> createPageProductCommment(){
        List<ProductComment> list = new ArrayList<>();
        list.add(createCommentA());
        list.add(createCommentB());
        Page<ProductComment> page = new PageImpl<>(list);
        return page;
    }

}
