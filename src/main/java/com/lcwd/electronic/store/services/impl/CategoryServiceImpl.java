package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.services.CategoryServices;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryServices {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        log.info("Initially Dao call for create category");
        Category category = mapper.map(categoryDto, Category.class);

        Category save = categoryRepository.save(category);
        log.info("Completed Dao call for create category");
        return mapper.map(save,CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, Integer categoryId) {

        log.info("Initially Dao call for update category");
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found exception"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category save = categoryRepository.save(category);
        log.info("Completed Dao call for update category");
        return mapper.map(save,CategoryDto.class);
    }

    @Override
    public void delete(Integer categoryId) {
        log.info("Initially Dao call for delete category");
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException());
        log.info("Completed Dao call for delete category");
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        log.info("Initially Dao call for get all category");
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        log.info("completed Dao call for get all category");
        return pageableResponse;
    }

    @Override
    public CategoryDto get(Integer categoryId) {
        log.info("Initially Dao call for get single category");
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException());
        log.info("completed Dao call for get single category");
        return mapper.map(category,CategoryDto.class);
    }
}
