package com.agile.ecommerce.product.core;

import com.agile.ecommerce.product.data.ProductRepository;
import com.agile.ecommerce.product.domain.Product;
import com.agile.ecommerce.product.dto.ProductDto;
import com.agile.ecommerce.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        modelMapper = mock(ModelMapper.class);
        productService = new ProductService(productRepository, modelMapper);
    }

    @Nested
    @DisplayName("getAll Tests")
    class GetAllTests {

        @Test
        @DisplayName("should return paginated list of products")
        void shouldReturnPaginatedListOfProducts() {
            Pageable pageable = PageRequest.of(0, 10);
            Product product = new Product();
            product.setId(1L);
            Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);

            when(productRepository.findAll(pageable)).thenReturn(page);

            Page<Product> result = productService.getAll(pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            verify(productRepository, times(1)).findAll(pageable);
        }
    }

    @Nested
    @DisplayName("getById Tests")
    class GetByIdTests {

        @Test
        @DisplayName("should return product when found")
        void shouldReturnProductWhenFound() throws ProductNotFoundException {
            Product product = new Product();
            product.setId(1L);

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            Product result = productService.getById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(productRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("should throw exception when product not found")
        void shouldThrowExceptionWhenProductNotFound() {
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ProductNotFoundException.class, () -> productService.getById(1L));

            verify(productRepository, times(1)).findById(1L);
        }
    }

    @Nested
    @DisplayName("add Tests")
    class AddTests {

        @Test
        @DisplayName("should add and return product")
        void shouldAddAndReturnProduct() {
            ProductDto productDto = new ProductDto(1L,"ProductName", "ProductDescription", BigDecimal.valueOf(100.0), 10);
            Product product = new Product();
            product.setId(1L);

            when(modelMapper.map(productDto, Product.class)).thenReturn(product);
            when(productRepository.save(any(Product.class))).thenReturn(product);
            when(modelMapper.map(product, ProductDto.class)).thenReturn(productDto);

            ProductDto result = productService.add(productDto);

            assertNotNull(result);
            assertEquals(productDto.name(), result.name());
            verify(productRepository, times(1)).save(any(Product.class));
        }
    }

    @Nested
    @DisplayName("update Tests")
    class UpdateTests {

        @Test
        @DisplayName("should update and return product")
        void shouldUpdateAndReturnProduct() throws ProductNotFoundException {
            ProductDto productDto = new ProductDto(1L,"ProductName", "ProductDescription", BigDecimal.valueOf(100.0), 10);
            Product product = new Product();
            product.setId(1L);

            when(productRepository.existsById(1L)).thenReturn(true);
            when(modelMapper.map(productDto, Product.class)).thenReturn(product);
            when(productRepository.save(any(Product.class))).thenReturn(product);
            when(modelMapper.map(product, ProductDto.class)).thenReturn(productDto);

            ProductDto result = productService.update(productDto);

            assertNotNull(result);
            assertEquals(productDto.name(), result.name());
            verify(productRepository, times(1)).existsById(1L);
            verify(productRepository, times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("should throw exception when product not found")
        void shouldThrowExceptionWhenProductNotFound() {
            ProductDto productDto = new ProductDto(1L,"ProductName", "ProductDescription", BigDecimal.valueOf(100.0), 10);

            when(productRepository.existsById(productDto.id())).thenReturn(false);

            assertThrows(ProductNotFoundException.class, () -> productService.update(productDto));

            verify(productRepository, times(1)).existsById(productDto.id());
        }
    }

    @Nested
    @DisplayName("delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("should delete product by id")
        void shouldDeleteProductById() throws ProductNotFoundException {
            when(productRepository.existsById(1L)).thenReturn(true);

            productService.delete(1L);

            verify(productRepository, times(1)).existsById(1L);
            verify(productRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("should throw exception when product not found")
        void shouldThrowExceptionWhenProductNotFound() {
            when(productRepository.existsById(1L)).thenReturn(false);

            assertThrows(ProductNotFoundException.class, () -> productService.delete(1L));

            verify(productRepository, times(1)).existsById(1L);
        }
    }
}
