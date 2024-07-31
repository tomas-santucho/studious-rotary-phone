package com.agile.ecommerce.product.core;

import com.agile.ecommerce.product.data.ProductRepository;
import com.agile.ecommerce.product.domain.Product;
import com.agile.ecommerce.product.dto.ProductDto;
import com.agile.ecommerce.product.exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    public Page<Product> getAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getById(long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public ProductDto add(ProductDto dto) {
        var product = mapper.map(dto, Product.class);
        var savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Transactional
    public ProductDto update(ProductDto dto) throws ProductNotFoundException {
        if (dto.id()==null){
            throw new ProductNotFoundException();
        }
        if (!productRepository.existsById(dto.id())) {
            throw new ProductNotFoundException(dto.id());
        }
        var product = mapper.map(dto, Product.class);
        var updatedProduct = productRepository.save(product);
        return mapper.map(updatedProduct, ProductDto.class);
    }

    @Transactional
    public void delete(long id) throws ProductNotFoundException {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
