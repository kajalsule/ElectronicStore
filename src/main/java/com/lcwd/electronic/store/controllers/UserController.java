package com.lcwd.electronic.store.controllers;


import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {



    @Autowired
    private UserServices userServices;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto)
    {
        log.info("Entering request for create user");
        UserDto userDto1=userServices.createUser(userDto);
        log.info("completed request for create user");
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);

    }

    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable ("userId") String userId,@RequestBody UserDto userDto)
    {
        log.info("Entering request for update user");
        UserDto updatedUserDto = userServices.updateUser(userDto,userId);
        log.info("completed request for update user");
        return new ResponseEntity<>(updatedUserDto,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId)
    {
        log.info("Entering request for delete user");
        userServices.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage.builder().message("User Deleted Successfully!!!").success(true).status(HttpStatus.OK).build();
        log.info("completed request for delete user");
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @GetMapping
    //get all
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(@RequestParam(value="pageNumber",defaultValue="0",required=false) int pageNumber,
                                                                 @RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
                                                                 @RequestParam(value="sortBy",defaultValue="name",required=false) String sortBy,
                                                                 @RequestParam(value="sortDir",defaultValue="asc",required=false) String sortDir)
    {
        log.info("completed request for get all user");
        return new ResponseEntity<>(userServices.getAllUser(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    //get single
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId)
    {
        log.info("completed request for get single user");
        return new ResponseEntity<>(userServices.getUserById(userId),HttpStatus.OK);
    }

    //get email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
    {
        log.info("completed request for get user by email");
        return new ResponseEntity<>(userServices.getUserByEmail(email),HttpStatus.OK);
    }

    //search

    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords)
    {
        log.info("completed request for search user");
        return new ResponseEntity<>(userServices.searchUser(keywords),HttpStatus.OK);
    }

    //upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage")MultipartFile image,@PathVariable String userId) throws IOException {

        log.info("entering request for upload image");
        String imageName = fileService.uploadImage(image, imageUploadPath);

        UserDto user = userServices.getUserById(userId);
        user.setUserImage(imageName);
       UserDto updateuser = userServices.updateUser(user,userId);

        ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).message("image upload successfully!!").success(true).status(HttpStatus.CREATED).build();
        log.info("completed request for upload user");
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);

    }

    //serve user image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userServices.getUserById(userId);
        log.info("user image name:{}",user.getUserImage());

        InputStream resource = fileService.getResource(imageUploadPath, user.getUserImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
