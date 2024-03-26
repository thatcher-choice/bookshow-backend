package com.springboot.bookshow.exception;

public class InsufficientSeatsException extends RuntimeException {
	public InsufficientSeatsException() {
        super("Insufficient available seats for the requested number of tickets.");
    }

    public InsufficientSeatsException(String message) {
        super(message);
    }
}
