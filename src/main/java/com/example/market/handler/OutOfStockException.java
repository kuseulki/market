package com.example.market.handler;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String message){
        super(message);
    }
}
