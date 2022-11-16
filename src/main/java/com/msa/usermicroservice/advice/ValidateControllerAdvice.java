package com.msa.usermicroservice.advice;

import com.msa.usermicroservice.dto.ResponseComDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidateControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseComDto> handleValidationExceptions(MethodArgumentNotValidException ex){
        //최상단 에러만 가져옴
        ObjectError objectError = ex.getBindingResult().getAllErrors().get(0);
        String msg = objectError.getDefaultMessage();

        return new ResponseEntity<>(
                ResponseComDto.builder()
                        .resultObj(objectError)
                        .resultMsg(msg)
                        .build(), HttpStatus.BAD_REQUEST
        );
    }
}
