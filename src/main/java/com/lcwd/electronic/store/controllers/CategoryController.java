package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.services.CategoryServices;
import com.lcwd.electronic.store.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryServices categoryServices;

    @Autowired
    private FileService fileService;

    @Value("${category.cover.image.path}")
    private String imageUploadPath;

    @PostMapping
    public ResponseEntity<CategoryDto>createCategory (@RequestBody CategoryDto categoryDto){
        log.info("Entering request for create category");
        CategoryDto categoryDto1 = categoryServices.create(categoryDto);
        log.info("completed request for create category");
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);

    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,@PathVariable Integer categoryId){
        log.info("Entering request for update category");
        CategoryDto update = categoryServices.update(categoryDto, categoryId);
        log.info("completed request for update category");
        return new ResponseEntity<>(update,HttpStatus.OK);
    }
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable Integer categoryId){
        log.info("Entering request for delete category");
        categoryServices.delete(categoryId);
        ApiResponseMessage response = ApiResponseMessage.builder().message("category is deleted ").status(HttpStatus.OK).success(true).build();
        log.info("completed request for delete category");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false)int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false)String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false)String sortDir){
        PageableResponse<CategoryDto> pageableResponse = categoryServices.getAll(pageNumber, pageSize, sortBy, sortDir);
        log.info("completed request for get all category");
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> get(@PathVariable Integer categoryId){
        log.info("Entering request for get single category");
        CategoryDto categoryDto = categoryServices.get(categoryId);
        log.info("completed request for get single category");
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }

    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("coverImage") MultipartFile image, @PathVariable Integer categoryId) throws IOException {

        log.info("entering request for upload image");
        String imageName = fileService.uploadImage(image, imageUploadPath);

        CategoryDto category = categoryServices.get(categoryId);
        category.setCoverImage(imageName);
        CategoryDto updateCategory = categoryServices.update(category,categoryId);

        ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).message("image upload successfully!!").success(true).status(HttpStatus.CREATED).build();
        log.info("completed request for upload user");
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);

    }

    @GetMapping("/image/{categoryId}")
    public void serveUserImage(@PathVariable Integer categoryId, HttpServletResponse response) throws IOException {
        CategoryDto category = categoryServices.get(categoryId);
        log.info("user image name:{}",category.getCoverImage());

        InputStream resource = fileService.getResource(imageUploadPath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
