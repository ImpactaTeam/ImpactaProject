package Impacta.Project.FeiraOnline.controller;


import Impacta.Project.FeiraOnline.exception.BusinessRuleException;
import Impacta.Project.FeiraOnline.exception.OrderNotFoundException;
import Impacta.Project.FeiraOnline.rest.ApiErrors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleRegraNegocioException(BusinessRuleException ex){
        String mensagemError = ex.getMessage();
        return new ApiErrors(mensagemError);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ApiErrors handlePedidoNotFoundException(OrderNotFoundException ex ){
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleMethodNotValidException(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map( erro -> erro.getDefaultMessage() )
                .collect(Collectors.toList());
        return new ApiErrors(errors);
    }
}
