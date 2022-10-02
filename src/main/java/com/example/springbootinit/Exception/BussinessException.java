package com.example.springbootinit.Exception;

public class BussinessException extends RuntimeException{
    public BussinessException() {
        super();
    }

    public BussinessException(String msg) {
        super(msg);
    }
}
