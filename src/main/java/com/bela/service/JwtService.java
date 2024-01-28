package com.bela.service;

import com.bela.util.JwtUtil;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class JwtService {

    public static void main(String[] args) throws UnrecoverableKeyException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {

        JwtUtil util = new JwtUtil();
        String jwt = util.generateJwtToken();

        util.printStructure(jwt);

    }
}
