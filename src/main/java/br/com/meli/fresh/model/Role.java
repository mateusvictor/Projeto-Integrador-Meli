package br.com.meli.fresh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {

    SELLER(0, "ROLE_SELLER"),
    BUYER(1, "ROLE_BUYER"),
    WAREHOUSEMANAGER(2, "ROLE_WAREHOUSEMANAGER"),
    ADMIN(3, "ROLE_ADMIN");

    private int cod;
    private String name;

    public static Role toEnum(Integer cod) {
        if (cod == null) return null;

        return Arrays.stream(Role.values()).filter(role -> {
            return cod.equals(role.getCod());
        }).findFirst().orElseThrow(
                () -> new IllegalArgumentException("Id inválido: " + cod)
        );
    }
}
