package br.com.meli.fresh.services;

public interface IInboundOrderService <T>{
    T create(T t);
    T update(String id, T t);
    T getById(String id);
}
