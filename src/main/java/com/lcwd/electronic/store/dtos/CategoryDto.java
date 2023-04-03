package com.lcwd.electronic.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CategoryDto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String categoryId;

    //@NotBlank
   // @Min(value = 4,message = "title must be of min 4 character")
    private String title;

   // @NotBlank(message = "description required")
    private String description;

    private String coverImage;
}
