package com.loofi.auth.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeCredRequest {
  private String oldCredential;
  private String newCredential;
  private String identifier;
}
