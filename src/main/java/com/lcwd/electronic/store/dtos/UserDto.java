package com.lcwd.electronic.store.dtos;

import lombok.*;

import javax.persistence.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto{

    private String userId;



    private String name;

    private String email;
    private String password;
    private String gender;

    private String about;

    private String userImage;

}
