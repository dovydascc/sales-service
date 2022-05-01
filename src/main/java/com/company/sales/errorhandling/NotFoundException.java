package com.company.sales.errorhandling;

public class NotFoundException extends SalesServiceException {

  public NotFoundException(String message) {
    super(message);
  }
}
