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

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerHandlerException {
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("UserNotFoundException", e.getMessage()));
    }
    @ExceptionHandler(UserWithThisEmailAlreadyExists.class)
    protected ResponseEntity<?> handleUserWithThisEmailAlreadyExists(UserWithThisEmailAlreadyExists e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("UserWithThisEmailAlreadyExists", e.getMessage()));
    }

    @ExceptionHandler(UserNotAllowedException.class)
    protected ResponseEntity<?> handleUserNotAllowedException(UserNotAllowedException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("UserNotAllowedException", e.getMessage()));
    }

    @ExceptionHandler(WarehouseManagerAlreadyDefined.class)
    protected ResponseEntity<?> handleWarehouseManagerAlreadyDefined(WarehouseManagerAlreadyDefined e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("WarehouseManagerAlreadyDefined", e.getMessage()));
    }

    @ExceptionHandler(WarehouseNotFoundException.class)
    protected ResponseEntity<?> handleWarehouseNotFoundException(WarehouseNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("WarehouseNotFoundException", e.getMessage()));
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> productNotFoundException(ProductNotFoundException err){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
    }

    @ExceptionHandler(ProductsNotFoundException.class)
    public ResponseEntity<?> productsNotFoundException(ProductsNotFoundException err){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err.getMessage());
    }

    @ExceptionHandler(WarehouseManagerCanNotBeDeletedException.class)
    public ResponseEntity<?> handleWarehouseManagerCanNotBeDeletedException(WarehouseManagerCanNotBeDeletedException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("WarehouseManagerCanNotBeDeletedException", e.getMessage()));
    }

    @ExceptionHandler(SaleProductNotFoundException.class)
    public ResponseEntity<?> saleProductNotFoundException(SaleProductNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO("SaleProductNotFoundException", e.getMessage()));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException err) {

        List<String> messageErrors = err.getConstraintViolations().stream().map(e -> {
            return e.getMessage();
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageErrors);
    }

}
