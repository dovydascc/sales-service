package com.company.sales.errorhandling;

import com.company.sales.features.products.dtos.ProductDto;
import com.company.sales.features.products.dtos.TestProductDto;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mock controller for testing Exception Handling in our application
 */
@RestController
@RequestMapping("/api/v1/test/")
class MockExceptionController {

  private final ProductDto productDto = TestProductDto.airMaxShoes();

  @GetMapping("/ok")
  String ok() {
    return "ok";
  }

  @GetMapping("/argument-not-valid")
  void argumentNotValid() throws MethodArgumentNotValidException {
    var someMethod = ReflectionUtils.findMethod(MockExceptionController.class, "argumentNotValid");
    var errors = new BeanPropertyBindingResult(productDto, "product");
    errors.rejectValue("name", "UNIQUE", "Please enter unique name");
    errors.rejectValue("warrantyDays", "MINIMUM", "Please enter a valid warranty");

    throw new MethodArgumentNotValidException(
        new MethodParameter(Objects.requireNonNull(someMethod), -1),
        errors
    );
  }

  @GetMapping("/some-throwable")
  void someThrowable() {
    throw new SalesServiceException("testing");
  }
}