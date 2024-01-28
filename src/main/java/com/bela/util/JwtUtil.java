package com.bela.util;

import io.jsonwebtoken.*;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String ALG = "alg";
    private static final String ISSUER = "issuer";
    private static final String USER = "user";
    private static final String CERTS_BASE = "src/main/resources/certs/";
    private static final String PRIVATE_KEY = CERTS_BASE + "certificate.p12";
    private static final String PUBLIC_KEY = CERTS_BASE + "certificate.pem";

    public String generateJwtToken() throws UnrecoverableKeyException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        Claims claims = Jwts.claims().setSubject("bela-test-subject");
        claims.put(ISSUER, "http://localhost");
        claims.put(USER, "bela");

        Map<String,Object> headers = new HashMap<>();
        headers.put(Header.TYPE,Header.JWT_TYPE);
        headers.put(ALG,SignatureAlgorithm.RS256.getValue());

        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + Duration.ofHours(1).toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setHeader(headers)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.RS256, generatePrivateKey())
                .compact();
    }

    public void printStructure(String token) throws FileNotFoundException, CertificateException {
        Jws<Claims> parseClaimsJws = Jwts.parser()
                .setSigningKey(generatePublicKey())
                .parseClaimsJws(token);

        System.out.println("Header     : " + parseClaimsJws.getHeader());
        System.out.println("Body       : " + parseClaimsJws.getBody());
        System.out.println("Signature  : " + parseClaimsJws.getSignature());
    }
    private Key generatePrivateKey() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        File file = new File(PRIVATE_KEY);
        InputStream keyStream = new FileInputStream(file.getAbsoluteFile());
        char[] password = "".toCharArray(); //remove -nodes while generating certs and update password here.

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(keyStream,password);
        return keyStore.getKey("myKey", password);
    }

    private PublicKey generatePublicKey() throws CertificateException, FileNotFoundException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        InputStream certificateInputStream = new FileInputStream(PUBLIC_KEY);
        Certificate certificate = certificateFactory.generateCertificate(certificateInputStream);

        return certificate.getPublicKey();
    }
}
