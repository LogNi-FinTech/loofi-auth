package com.loofi.auth.controller;

import com.loofi.auth.exception.BlockCredException;
import com.loofi.auth.exception.InvalidCredException;
import com.loofi.auth.model.ChangeCredRequest;
import com.loofi.auth.model.LoginRequest;
import com.loofi.auth.service.UserCredentialService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController("/api")
@RequiredArgsConstructor
public class AuthController {
  private final UserCredentialService userCredentialService;

  @PostMapping(value = "/check/auth")
  public void checkCredResponse(@RequestBody @Valid LoginRequest loginRequest) throws BlockCredException, InvalidCredException {
    userCredentialService.validateCredential(loginRequest.getIdentifier(), loginRequest.getCredential());
  }

  @PutMapping(value = "/change/auth")
  public void checkCredResponse(@RequestBody @Valid ChangeCredRequest changeCredRequest) throws BlockCredException, InvalidCredException {
    userCredentialService.validateCredential(changeCredRequest.getIdentifier(), changeCredRequest.getOldCredential());
    userCredentialService.changeCredential(changeCredRequest.getIdentifier(), changeCredRequest.getNewCredential());
  }

  @PutMapping(value = "/reset/auth")
  public void resetPin(@RequestBody @Valid ChangeCredRequest changeCredRequest) {
    userCredentialService.changeCredential(changeCredRequest.getIdentifier(), changeCredRequest.getNewCredential());
  }

}
