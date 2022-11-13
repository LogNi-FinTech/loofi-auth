package com.loofi.auth.exception;

public class InvalidCredException extends Exception {
  String errorCode;

  public InvalidCredException() {

  }

  public InvalidCredException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}
