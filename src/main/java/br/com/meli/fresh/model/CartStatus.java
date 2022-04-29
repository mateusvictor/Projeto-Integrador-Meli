package br.com.meli.fresh.model;

public enum CartStatus {
    OPEN("OPEN"),
    CLOSE("CLOSE");

    public final String cartStatus;
    CartStatus(String cartStatus) {
        this.cartStatus = cartStatus;
    }

    public String getCartStatus() {
        return cartStatus;
    }

    static public boolean isEnumValid(String codeName){
        CartStatus[] lists = CartStatus.values();
        for(CartStatus list : lists)
            if (list.cartStatus.equals(codeName))
                return true;
            return false;
    }
}
