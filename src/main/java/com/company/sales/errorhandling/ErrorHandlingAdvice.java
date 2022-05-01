package com.company.sales.errorhandling;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.time.ZonedDateTime;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global error handler for all REST APIs. It is implemented to catch all exceptions.
 */
@Slf4j
@ResponseBody
@ControllerAdvice
class ErrorHandlingAdvice {

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler
  ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
    log.info("MethodArgumentNotValidException: {}", e.getMessage());

    var errors = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fe -> String.format("%s: %s", fe.getField(), fe.getDefaultMessage()))
        .toList();

    return getErrorResponse(e.getClass().getSimpleName(), "Invalid request, see data", errors);
  }

  /**
   * Data Integrity constraint violations are thrown by database constraint validations. Violations are created by
   * invalid request body or query params.
   *
   * @param e the exception
   * @return a uniform error response
   */
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler
  ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    log.info("DataIntegrityViolationException: {}", e.getMessage());

    if (!(e.getCause() instanceof ConstraintViolationException ex)) {
      return handleThrowable(e);
    }

    var errors = ex.getConstraintViolations().stream()
        .map(cv -> String.format("%s: %s", cv.getMessage(), cv.getInvalidValue()))
        .toList();

    return getErrorResponse(e.getClass().getSimpleName(), "Invalid request params, see data", errors);
  }

  /**
   * Generic error handler, for all errors unhandled above. This will return HTTP 500 Internal Server Error.
   *
   * @param e the exception
   * @return a uniform error response
   */
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler
  ErrorResponse handleThrowable(Throwable e) {
    log.error("InternalServerError: ", e);
    return getErrorResponse(e.getClass().getSimpleName(), "Internal server error, please contact support", null);
  }

  private ErrorResponse getErrorResponse(String error, String message, Object data) {
    return new ErrorResponse(ZonedDateTime.now(), error, message, data);
  }
}
