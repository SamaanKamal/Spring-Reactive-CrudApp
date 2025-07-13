package com.example.ReactiveCrudApp.Service;

import com.example.ReactiveCrudApp.Dto.ProductDto;
import com.example.ReactiveCrudApp.Entity.Product;
import com.example.ReactiveCrudApp.Repository.ProductRepository;
import com.example.ReactiveCrudApp.Utils.ProductUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Flux<ProductDto> getProducts(){
        return productRepository.findAll().map(ProductUtil::productEntityToProductDto);
    }
    public Mono<ProductDto> getProductById(String productId){
        return productRepository.findById(productId).map(ProductUtil::productEntityToProductDto);
    }

    public Mono<ProductDto> saveProduct(ProductDto productDto){
        return productRepository.save(ProductUtil.productDtoToProductEntity(productDto)).map(ProductUtil::productEntityToProductDto);
    }

    public Mono<ProductDto> updateProduct(ProductDto productDto){
        Mono<Product> productMono = productRepository.findById(productDto.getId());
        if(productMono != null) {
            Product product = new Product();
            BeanUtils.copyProperties(productDto, product);
            return productRepository.save(product).map(ProductUtil::productEntityToProductDto);
        }
        return null;
    }
    public Mono<String> deleteProduct(String productId){
        Mono<Product> productMono = productRepository.findById(productId);
        if(productMono != null) {
            productRepository.deleteById(productId);
            return Mono.just("deleted Product with Id: "+ productId);
        }
        return Mono.just("error happened here");
    }
}
