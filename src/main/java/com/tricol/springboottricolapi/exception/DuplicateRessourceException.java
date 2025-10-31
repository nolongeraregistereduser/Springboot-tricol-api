package com.tricol.springboottricolapi.exception;

public class DuplicateRessourceException extends BusinessException{

    public DuplicateRessourceException(String resource, String field, String value) {
        super(String.format("Duplicate %s: %s with %s already exists.", resource, field, value));

    }
}
