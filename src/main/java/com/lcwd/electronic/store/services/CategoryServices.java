package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

import java.awt.print.Pageable;

public interface CategoryServices {

    //create
    CategoryDto create(CategoryDto categoryDto);

    //update
    CategoryDto update(CategoryDto categoryDto,Integer categoryId);

    //delete
    void delete(Integer categoryId);


    //getall
    PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

    //get single category
    CategoryDto get(Integer categoryId);

}
