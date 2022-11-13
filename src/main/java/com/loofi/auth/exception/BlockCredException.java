package com.loofi.auth.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockCredException extends Exception{
  String errorCode;

  public BlockCredException() {

  }

  public BlockCredException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}
