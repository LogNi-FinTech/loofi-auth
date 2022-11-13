package com.loofi.auth.entity;

import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Authority {
  @NotNull
  @Id
  @Column(length = 50)
  private String name;
}
