package com.agile.ecommerce.product.rest;

import com.agile.ecommerce.product.core.ProductService;
import com.agile.ecommerce.product.domain.Product;
import com.agile.ecommerce.product.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@DisplayName("ProductController Tests")
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductController(productService)).build();
    }

    @Nested
    @DisplayName("getProductById Tests")
    class GetProductByIdTests {

        @Test
        @DisplayName("should return product when found")
        void shouldReturnProductWhenFound() throws Exception {
            // Given
            long productId = 1L;
            Product product = new Product();
            when(productService.getById(productId)).thenReturn(product);

            // When / Then
            mockMvc.perform(get("/api/products/{id}", productId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            Mockito.verify(productService, Mockito.times(1)).getById(productId);
        }
    }

    @Nested
    @DisplayName("addProduct Tests")
    class AddProductTests {

        @Test
        @DisplayName("should create and return product")
        void shouldCreateAndReturnProduct() throws Exception {
            // Given
            ProductDto productDto = new ProductDto(1L,"name", "description", BigDecimal.valueOf(10.0), 5);
            when(productService.add(any(ProductDto.class))).thenReturn(productDto);

            // When / Then
            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"name\",\"description\":\"description\",\"price\":10.0,\"quantity\":5}"))
                    .andExpect(status().isCreated());
            Mockito.verify(productService, Mockito.times(1)).add(any(ProductDto.class));
        }
    }

    @Nested
    @DisplayName("updateProduct Tests")
    class UpdateProductTests {

        @Test
        @DisplayName("should update and return product")
        void shouldUpdateAndReturnProduct() throws Exception {
            // Given
            var productDto = new ProductDto(1L,"name", "description", BigDecimal.valueOf(10.0), 5);
            when(productService.update(any(ProductDto.class))).thenReturn(productDto);

            // When / Then
            mockMvc.perform(put("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"name\",\"description\":\"description\",\"price\":10.0,\"quantity\":5}"))
                    .andExpect(status().isOk());
            Mockito.verify(productService, Mockito.times(1)).update(any(ProductDto.class));
        }
    }

    @Nested
    @DisplayName("deleteProduct Tests")
    class DeleteProductTests {

        @Test
        @DisplayName("should delete product by id")
        void shouldDeleteProductById() throws Exception {
            // Given
            long productId = 1L;
            doNothing().when(productService).delete(productId);

            // When / Then
            mockMvc.perform(delete("/api/products/{id}", productId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
            Mockito.verify(productService, Mockito.times(1)).delete(productId);
        }
    }
}
