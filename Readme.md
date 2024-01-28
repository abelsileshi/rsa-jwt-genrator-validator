# Use openssl to generate self signed certs for testing
- Create the key and cert(-nodes creates without password, means no DES(no-des) encryption ) 
```
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out certificate.pem -days 365 -nodes 
```

- Create pkcs12 file
```
openssl pkcs12 -export -out certificate.p12 -inkey key.pem -in certificate.pem -name myKey
```

- Read pkcs12 file
```
keytool —v -list -storetype pkcs12 -keystore certificate.p12 -storepass password
```