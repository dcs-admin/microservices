package com.anji.product.controller;

import com.anji.product.entity.Product;
import com.anji.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    @Autowired
    private ProductRepository productRepository;


    @PostMapping("/")
    public Product saveProduct(@RequestBody Product product){
        log.info("Post:saveProduct:"+product);
        return this.productRepository.save(product);
    }


    @GetMapping("/{productId}")
    public Product findProductId(@PathVariable long productId) throws Exception {
        log.info("Get:findProductId:"+productId);
        Optional<Product> optional =  this.productRepository.findById(productId);
        if(optional.isPresent()){
            return optional.get();
        }else{
            throw new Exception("Unable to find with Id:"+productId);
        }
    }

}
