package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.services.ProductServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServices productServices;

    //create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        log.info("Entering request for create Product");
        ProductDto productDto1 = productServices.create(productDto);
        log.info("completed request for create product");
        return new ResponseEntity<>(productDto1, HttpStatus.CREATED);

    }

    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("productId") String productId, @RequestBody ProductDto productDto) {
        log.info("Entering request for update product");
        ProductDto updateProductDto = productServices.update(productDto, productId);
        log.info("completed request for update product");
        return new ResponseEntity<>(updateProductDto, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {
        log.info("Entering request for delete product");
        productServices.delete(productId);
        ApiResponseMessage message = ApiResponseMessage.builder().message("product Deleted Successfully!!!").success(true).status(HttpStatus.OK).build();
        log.info("completed request for delete product");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //get single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {
        ProductDto productDto = productServices.get(productId);
        log.info("completed request for get single Product");
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                      @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                                                                      @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        log.info("completed request for get all product");
        return new ResponseEntity<>(productServices.getAll(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    //get live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                   @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                   @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                                                                   @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        log.info("completed request for get all live product");
        return new ResponseEntity<>(productServices.getAllLive(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);

    }

    //search

    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(@PathVariable String query,
                                                                   @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                   @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                   @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                                                                   @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        log.info("completed request for get all live product");
        return new ResponseEntity<>(productServices.searchByTitle(query,pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);

    }

}
