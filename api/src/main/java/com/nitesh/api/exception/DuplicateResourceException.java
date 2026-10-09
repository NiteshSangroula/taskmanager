package com.nitesh.api.exception;

public class DuplicateResourceException extends RuntimeException {
  public DuplicateResourceException(String message) {
    super(message);
  }

}
