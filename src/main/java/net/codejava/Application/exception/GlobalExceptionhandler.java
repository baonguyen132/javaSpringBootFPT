package net.codejava.Application.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import net.codejava.Application.dto.request.APIResponse;

@ControllerAdvice
public class GlobalExceptionhandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIResponse<String>> handlingRuntimeException(RuntimeException e) {

        APIResponse<String> response = new APIResponse<>() ;
        response.setCode(ErrorCode.UNCATEGRORIZED_EXCEPTION.getCode());
        response.setMessage(ErrorCode.UNCATEGRORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse<String>> handlingAppException(AppException e) {

        ErrorCode errorCode = e.getErrorCode() ;

        APIResponse<String> response = new APIResponse<>() ;
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse<String>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        
        String enumkey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY ;

        try {
            errorCode = ErrorCode.valueOf(enumkey) ;
        }
        catch (IllegalArgumentException ex) {
        }

        APIResponse<String> response = new APIResponse<>() ;
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response) ;
    }
    
}
