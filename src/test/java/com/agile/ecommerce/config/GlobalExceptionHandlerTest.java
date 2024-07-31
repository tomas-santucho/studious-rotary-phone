package com.agile.ecommerce.config;

import com.agile.ecommerce.order.exception.OrderNotFoundException;
import com.agile.ecommerce.orderItem.exception.OrderItemNotFoundException;
import com.agile.ecommerce.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Nested
    @DisplayName("handleValidationExceptions Tests")
    class HandleValidationExceptionsTests {

        @Test
        @DisplayName("should handle validation exceptions and return BAD_REQUEST status")
        void shouldHandleValidationExceptionsAndReturnBadRequest() {
            // Given
            var bindingResult = mock(BindingResult.class);
            var ex = new MethodArgumentNotValidException(null, bindingResult);
            var fieldError = new FieldError("objectName", "fieldName", "defaultMessage");
            when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

            // When
            ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(ex);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).containsEntry("fieldName", "defaultMessage");
        }
    }

    @Nested
    @DisplayName("handleOrderNotFoundException Tests")
    class HandleOrderNotFoundExceptionTests {

        @Test
        @DisplayName("should handle order not found exceptions and return NOT_FOUND status")
        void shouldHandleOrderNotFoundExceptionAndReturnNotFound() {
            // Given
            var ex = new OrderNotFoundException(1L);

            // When
            ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleOrderNotFoundException(ex);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).containsEntry("error", "Order 1 not found.");
        }
    }

    @Nested
    @DisplayName("handleOrderItemNotFoundException Tests")
    class HandleOrderItemNotFoundExceptionTests {

        @Test
        @DisplayName("should handle order item not found exceptions and return NOT_FOUND status")
        void shouldHandleOrderItemNotFoundExceptionAndReturnNotFound() {
            // Given
            var ex = new OrderItemNotFoundException(1L);

            // When
            ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleOrderItemNotFoundException(ex);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).containsEntry("error", "OrderItem 1 not found.");
        }
    }

    @Nested
    @DisplayName("handleProductNotFoundException Tests")
    class HandleProductNotFoundExceptionTests {

        @Test
        @DisplayName("should handle product not found exceptions and return NOT_FOUND status")
        void shouldHandleProductNotFoundExceptionAndReturnNotFound() {
            // Given
            var ex = new ProductNotFoundException(1L);

            // When
            ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleProductNotFoundException(ex);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).containsEntry("error", "Product 1 not found");
        }
    }
}
