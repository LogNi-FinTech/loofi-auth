package com.loofi.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.loofi.auth.entity.enums.CredentialType;
import com.loofi.auth.entity.enums.UserCredentialState;

import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.Valid;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_credential")
public class UserCredential {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String userIdentifier; //userName, mobile, email
  private String salt;
  private String credential; // pin password
  @Enumerated(value = EnumType.STRING)
  private CredentialType credentialType;
  @Enumerated(value = EnumType.STRING)
  private UserCredentialState state; // user active or BLOCK
  private Integer wrongCredCount;
  @LastModifiedDate
  private Instant lastModifiedDate;
  @CreatedDate
  private Instant createdDate;
  private String createdBy;
  private String lastModifiedBy;
  @Valid
  @JsonIgnore
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "user_authority",
    joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
  @BatchSize(size = 20)
  private Set<Authority> authorities = new HashSet<>();

}
