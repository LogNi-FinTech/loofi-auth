package com.loofi.auth.service;

import com.loofi.auth.entity.UserCredential;
import com.loofi.auth.entity.enums.UserCredentialState;
import com.loofi.auth.exception.BlockCredException;
import com.loofi.auth.exception.InvalidCredException;
import com.loofi.auth.repository.UserCredentialRepository;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import lombok.RequiredArgsConstructor;

import static com.loofi.auth.common.AuthErrorCodes.BLOCKED_CREDENTIAL;
import static com.loofi.auth.common.AuthErrorCodes.ERROR_MAP;
import static com.loofi.auth.common.AuthErrorCodes.INVALID_CREDENTIAL;

@Service
@RequiredArgsConstructor
public class UserCredentialService {
  public static final String SHA_256 = "SHA-256";
  private final UserCredentialRepository userCredentialRepository;
  @Value("${wrong.cred.count:3}")
  private Integer wrongCredentialCount;


  @Transactional
  public void createCredential(String identifier, String credential) {
    UserCredential userCredential = new UserCredential();
    userCredential.setUserIdentifier(identifier);
    userCredential.setLastModifiedDate(Instant.now());
    userCredential.setSalt(this.newSalt());
    userCredential.setState(UserCredentialState.ACTIVE);
    userCredential.setCredential(digest(SHA_256, userCredential.getSalt(), credential));
    userCredentialRepository.save(userCredential);
  }

  @Transactional
  public void validateCredential(String identifier, String credential) throws BlockCredException, InvalidCredException {

    UserCredential userCredential = userCredentialRepository.findByUserIdentifier(identifier).orElseThrow(() -> new RuntimeException("Not Found"));
    String credDigest = digest(SHA_256, userCredential.getSalt(), credential);
    if (!credDigest.equals(userCredential.getCredential())) {
      int wrongCredCount = userCredential.getWrongCredCount() != null ? userCredential.getWrongCredCount() : 0;
      if (wrongCredCount == wrongCredentialCount) {
        userCredential.setState(UserCredentialState.BLOCK);
        userCredentialRepository.save(userCredential);
        throw new BlockCredException(BLOCKED_CREDENTIAL, ERROR_MAP.get(BLOCKED_CREDENTIAL));
      }
      userCredential.setWrongCredCount(wrongCredCount + 1);
      userCredentialRepository.save(userCredential);
      throw new InvalidCredException(INVALID_CREDENTIAL, ERROR_MAP.get(INVALID_CREDENTIAL));
    }
  }


  @Transactional
  public void unblockAccount(String identifier) {
    UserCredential userCredential = userCredentialRepository.findByUserIdentifier(identifier).orElseThrow(() -> new RuntimeException("Account Cred Not Found"));
    userCredential.setWrongCredCount(0);
    userCredential.setState(UserCredentialState.ACTIVE);
    userCredentialRepository.save(userCredential);
  }

  @Transactional
  public void changeCredential(String identifier, String credential) {

    UserCredential userCredential = userCredentialRepository.findByUserIdentifier(identifier).orElseThrow(() -> new RuntimeException("Account Cred Not Found"));
    String credDigest = digest(SHA_256, userCredential.getSalt(), credential);
    userCredential.setCredential(credDigest);
    userCredential.setLastModifiedDate(Instant.now());
    userCredentialRepository.save(userCredential);
  }

  private static String digest(final String algorithm, final String salt, final String string) {
    if (string == null) {
      return null;
    }
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance(algorithm);
    } catch (final NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
    md.reset();
    try {
      if (salt != null) {
        md.update(salt.getBytes("UTF-8"));
      }
      return toHex(md.digest(string.getBytes("UTF-8")));
    } catch (final UnsupportedEncodingException e) {
      // Never happens as UTF-8 is always supported
      return null;
    }
  }

  private static String toHex(final byte[] bytes) {
    final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    final char[] chars = new char[bytes.length * 2];
    int j = 0;
    int k;
    for (final byte element : bytes) {
      k = element;
      chars[j++] = hexDigits[(k >>> 4) & 0x0F];
      chars[j++] = hexDigits[k & 0x0F];
    }
    return new String(chars);
  }

  public String newSalt() {
    return RandomStringUtils.randomAlphanumeric(32);
  }
}
