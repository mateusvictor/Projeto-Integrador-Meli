package br.com.meli.fresh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICrudService <T> {
    T create(T t);
    T update(String id, T t);
    T getById(String id);
    Page<T> getAll(Pageable pageable);
    void delete(String id);
}
