package com.company.sales.errorhandling;

public class SalesServiceException extends RuntimeException {

  public SalesServiceException(String message) {
    super(message);
  }
}
