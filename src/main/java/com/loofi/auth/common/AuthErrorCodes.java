package com.loofi.auth.common;

import java.util.HashMap;
import java.util.Map;

public class AuthErrorCodes {
  public static final String INVALID_CREDENTIAL = "4001";
  public static final String BLOCKED_CREDENTIAL = "4022";

  public static final String INTERNAL_ERROR = "5001";

  // Error mapping
  public static final Map<String, String> ERROR_MAP = new HashMap<String, String>();

  static {
    ERROR_MAP.put(BLOCKED_CREDENTIAL, "Block Credential");
    ERROR_MAP.put(INVALID_CREDENTIAL, "INVALID_CREDENTIAL");
    ERROR_MAP.put(INTERNAL_ERROR, "INTERNAL_ERROR");
  }
}
