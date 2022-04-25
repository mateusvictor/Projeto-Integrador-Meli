package br.com.meli.fresh.controller.exception;

import br.com.meli.fresh.dto.response.ErrorDTO;
import br.com.meli.fresh.model.exception.BuyerNotFoundException;
import br.com.meli.fresh.model.exception.EmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerHandlerException {
    @ExceptionHandler(BuyerNotFoundException.class)
    protected ResponseEntity<?> handleBuyerNotFoundException(BuyerNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("BuyerNotFoundException", e.getMessage()));
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    protected ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("EmailAlreadyExistsException", e.getMessage()));
    }
}
