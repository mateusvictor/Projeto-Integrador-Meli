package br.com.meli.fresh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductCommentService <T>{
    T create(T t, String idProduto);
//    T update(String id, T t);
    T getById(String idProduto, String idComment);
    Page<T> getAll(String idProduto, Pageable pageable);
    void delete(String idProduto, String idComment);
}
