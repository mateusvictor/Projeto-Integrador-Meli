package br.com.meli.fresh.model.exception;

public class VideoNotFoundException extends RuntimeException {
    public VideoNotFoundException(String id) {
        super("Video was not found for the given ID! ID:" + id);
    }
}
