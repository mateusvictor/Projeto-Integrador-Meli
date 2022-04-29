package br.com.meli.fresh.model;

public enum UserType {
    WAREHOUSEMANAGER(0),
    BUYER(1),
    SELLER(2);
    public int value;

    UserType(int value) {
        this.value = value;
    }
}
