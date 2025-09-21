package com.SENA.DISTRIBUIDORA_LA_DORADA.Util;

import java.util.Base64;
import java.security.SecureRandom;

public class GenerateKey {
    public static void main(String[] args) {
        byte[] key = new byte[64]; // 512 bits
        new SecureRandom().nextBytes(key);
        System.out.println(Base64.getEncoder().encodeToString(key));
    }
}
