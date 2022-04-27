package br.com.meli.fresh.services;

import br.com.meli.fresh.model.Cart;

public interface ICartService {
    Cart create(Cart t);
    Cart update(String id);
    Cart getById(String id);
}
