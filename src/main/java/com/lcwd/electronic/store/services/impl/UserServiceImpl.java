package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserServices {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;
    @Override
    public UserDto createUser(UserDto userDto) {

        String userId= UUID.randomUUID().toString();
        userDto.setUserId(userId);
        log.info("Initially Dao call for create user");
        User user= dtoToEntity(userDto);
        User savedUser = userRepository.save(user);

        UserDto newDto=entityToDto(savedUser);
        log.info("Completed Dao call for create user");
        return newDto;
    }


    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        log.info("Initially Dao call for update user");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with given id"));

        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setUserImage(userDto.getUserImage());

        User updatedUser = userRepository.save(user);
        UserDto updatedDto = entityToDto(updatedUser);
        log.info("Completed Dao call for update user");
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        log.info("Initially Dao call for delete user");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        userRepository.delete(user);
        log.info("COmpleted Dao call for delete user");

    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

        log.info("Initially Dao call for get all user");
        Sort sort =(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page=userRepository.findAll(pageable);

        PageableResponse<UserDto> response = Helper.getPageableResponse(page,UserDto.class);
        log.info("Completed Dao call for get all user");
        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        log.info("Initially Dao call for get user by Id");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("userid not found"));
        log.info("Completed Dao call for get user by Id");
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        log.info("Initially Dao call for get user by email user");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found with given email and password"));
        log.info("Completed Dao call for get user by email");
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        log.info("Initially Dao call for search user");
        List<User> users=userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList=users.stream().map(user->entityToDto(user)).collect(Collectors.toList());
        log.info("Completed Dao call for search user");
        return dtoList;
    }



    private UserDto entityToDto(User savedUser) {

//      UserDto userDto=UserDto.builder()
//              .userId(savedUser.getUserId())
//              .name(savedUser.getName())
//              .email(savedUser.getEmail())
//              .password(savedUser.getPassword())
//              .about(savedUser.getAbout())
//              .gender(savedUser.getGender())
//              .imageName(savedUser.getImageName())
//              .build();



      return mapper.map(savedUser,UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {

//        User user= User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();

        return mapper.map(userDto,User.class);
    }
}
