package com.felipe.ecommerce_auth_server.controllers;

import com.felipe.ecommerce_auth_server.dtos.CreateUserDTO;
import com.felipe.ecommerce_auth_server.dtos.ResponseDTO;
import com.felipe.ecommerce_auth_server.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/internal")
public class InternalController {
  private final UserService userService;

  public InternalController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseDTO createUser(@RequestBody CreateUserDTO userDTO) {
    return this.userService.create(userDTO);
  }
}
