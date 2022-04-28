package br.com.meli.fresh.controller.exception;

import br.com.meli.fresh.dto.response.ErrorDTO;
import br.com.meli.fresh.model.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> argumentNotValidException(MethodArgumentNotValidException err) {
        List<ObjectError> allErrors = err.getBindingResult().getAllErrors();
        List<ErrorDTO> errorDTOS = new ArrayList<>();
        allErrors.forEach(
                objectError -> errorDTOS.add(
                        new ErrorDTO("MethodArgumentNotValidException", objectError.getDefaultMessage()))
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTOS);
    }

    @ExceptionHandler(BuyerNotFoundException.class)
    protected ResponseEntity<?> handleBuyerNotFoundException(BuyerNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("BuyerNotFoundException", e.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> productNotFoundException(ProductNotFoundException err){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("ProductNotFoundException", err.getMessage()));
    }

    @ExceptionHandler(InvalidEnumCartStatusException.class)
    public ResponseEntity<?> invalidEnumCartStatusException(InvalidEnumCartStatusException err){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("InvalidEnumCartStatusException", err.getMessage()));
    }

    @ExceptionHandler(InsufficientQuantityOfProductException.class)
    public ResponseEntity<?> insufficientQuantityOfProductException(InsufficientQuantityOfProductException err){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("InsufficientQuantityOfProductException", err.getMessage()));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<?> cartNotFoundException(CartNotFoundException err){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("CartNotFoundException", err.getMessage()));
    }



}
