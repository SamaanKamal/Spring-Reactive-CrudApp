package com.example.ReactiveCrudApp.Utils;

import com.example.ReactiveCrudApp.Dto.ProductDto;
import com.example.ReactiveCrudApp.Entity.Product;
import org.springframework.beans.BeanUtils;

import java.beans.BeanProperty;

public class ProductUtil {
    public static ProductDto productEntityToProductDto(Product product){
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        return productDto;
    }

    public static Product productDtoToProductEntity(ProductDto productDto){
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }
}
