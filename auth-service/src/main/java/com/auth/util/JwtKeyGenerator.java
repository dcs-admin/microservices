package com.auth.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64EncodedKey = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Secure Key (Base64): " + base64EncodedKey);
    }
}
