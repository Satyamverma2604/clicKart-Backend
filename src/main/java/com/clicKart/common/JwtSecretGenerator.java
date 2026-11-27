package com.clicKart.common;



import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;

public class JwtSecretGenerator {

    public static void main(String[] args) {

        SecretKey key = Jwts.SIG.HS256.key().build();

        String secret = Encoders.BASE64.encode(key.getEncoded());

        System.out.println("JWT Secret:");
        System.out.println(secret);
    }
}