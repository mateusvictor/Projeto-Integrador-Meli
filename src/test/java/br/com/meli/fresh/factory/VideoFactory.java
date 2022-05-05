package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.Product;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.Video;

public final class VideoFactory {


    public static Video createVideo() {
        Video video = Video.builder()
                .id(null)
                .title("Any title")
                .url("https://www.youtube.com")
                .length(360)
                .product(new Product())
                .user(new User())
                .approved(false)
                .build();

        return video;
    }

    public static Video createVideo(User user, Product product) {
        Video video = Video.builder()
                .id(null)
                .title("Any title")
                .url("https://www.youtube.com")
                .length(360)
                .product(product)
                .user(user)
                .approved(false)
                .build();

        return video;
    }
}
