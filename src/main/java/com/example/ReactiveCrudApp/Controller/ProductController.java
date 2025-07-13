package com.example.ReactiveCrudApp.Controller;

import com.example.ReactiveCrudApp.Dto.ProductDto;
import com.example.ReactiveCrudApp.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Mono<ResponseEntity<Flux<ProductDto>>> getAllProducts(){
        return Mono.just(ResponseEntity.ok().body(productService.getProducts()));
    }

    @GetMapping("/{product-id}")
    public Mono<ResponseEntity<Mono<ProductDto>>> getProduct(@PathVariable("product-id") String productId){
        return Mono.just(ResponseEntity.ok().body(productService.getProductById(productId)));
    }

    @PostMapping("/create-product")
    public Mono<ResponseEntity<Mono<ProductDto>>> createProduct(@RequestBody ProductDto productDto){
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(productDto)));
    }

    @PutMapping("/update-product/{product-id}")
    public Mono<ResponseEntity<Mono<ProductDto>>> updateProduct(@PathVariable("product-id") String productId, @RequestBody ProductDto productDto){
        productDto.setId(productId);
        return Mono.just(ResponseEntity.ok().body(productService.updateProduct(productDto)));
    }

    @DeleteMapping("/{product-id}")
    public Mono<ResponseEntity<Mono<String>>> deleteProduct(@PathVariable("product-id") String productId){
        return Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(productService.deleteProduct(productId)));
    }
}

