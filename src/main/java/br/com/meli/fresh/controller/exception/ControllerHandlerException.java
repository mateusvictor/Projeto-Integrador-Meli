package br.com.meli.fresh.controller.exception;

import br.com.meli.fresh.dto.response.ErrorDTO;

import br.com.meli.fresh.model.exception.*;
import br.com.meli.fresh.services.exception.EntityNotFoundException;
import br.com.meli.fresh.services.exception.InsufficientAvailableSpaceException;
import br.com.meli.fresh.services.exception.InvalidSectionTypeException;
import br.com.meli.fresh.services.exception.InvalidWarehouseManagerException;
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
    @ExceptionHandler(BuyerNotFoundException.class)
    protected ResponseEntity<?> handleBuyerNotFoundException(BuyerNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("BuyerNotFoundException", e.getMessage()));
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    protected ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("EmailAlreadyExistsException", e.getMessage()));
    }
    @ExceptionHandler(WarehouseManagerNotFoundException.class)
    protected ResponseEntity<?> handleWarehouseManagerNotFoundException(WarehouseManagerNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("WarehouseManagerNotFoundException", e.getMessage()));
    }
    @ExceptionHandler(WarehouseNotFoundException.class)
    protected ResponseEntity<?> handleWarehouseNotFoundException(WarehouseNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("WarehouseNotFoundException", e.getMessage()));
    }

    @ExceptionHandler(WarehouseAlreadyDefinedException.class)
    protected ResponseEntity<?> handleWarehouseAlreadyDefined(WarehouseAlreadyDefinedException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("WarehouseAlreadyDefinedException", e.getMessage()));
    }


    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException err){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO("EntityNotFoundException", err.getMessage()));
    }

    @ExceptionHandler(InsufficientAvailableSpaceException.class)
    protected ResponseEntity<?> handleInsufficientAvailableSpaceException(InsufficientAvailableSpaceException err){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("InsufficientAvailableSpaceException", err.getMessage()));
    }

    @ExceptionHandler(InvalidSectionTypeException.class)
    protected ResponseEntity<?> handleInvalidSectionTypeException(InvalidSectionTypeException err){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("InvalidSectionTypeException", err.getMessage()));
    }

    @ExceptionHandler(InvalidWarehouseManagerException.class)
    protected ResponseEntity<?> handleInvalidWarehouseManagerException(InvalidWarehouseManagerException err){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("InvalidWarehouseManagerException", err.getMessage()));
    }

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


}
