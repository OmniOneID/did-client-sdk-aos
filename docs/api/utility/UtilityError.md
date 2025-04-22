---
puppeteer:
    pdf:
        format: A4
        displayHeaderFooter: true
        landscape: false
        scale: 0.8
        margin:
            top: 1.2cm
            right: 1cm
            bottom: 1cm
            left: 1cm
    image:
        quality: 100
        fullPage: false
---

Android UtilityError
==

- Topic: UtilityError
- Author: Sangjun Kim
- Date: 2024-08-12
- Version: v1.0.0

| Version          | Date       | Changes                  |
| ---------------- | ---------- | ------------------------ |
| v1.0.0  | 2024-08-12 | Initial version          |

<div style="page-break-after: always;"></div>

# Table of Contents

- [UtilityError](#utilityerror)
- [Error Code](#error-code)
  - [1. Common](#1-common)
  - [2. CryptoUtils](#2-cryptoutils)
  - [3. MultibaseUtils](#3-multibaseutils)
  - [4. DigestUtils](#4-digestutils)


## UtilityError

### Description
Error struct for Utility operations. It has code and message pair.
Code starts with MSDKUTL.

### Declaration
```java
public enum UtilityErrorCode {
    private final String utilityCode = "MSDKUTL";
    private int code;
    private String msg;
}
```

### Property

| Name    | Type   | Description            | **M/O** | **Note** |
|---------|--------|------------------------|---------|----------|
| code    | String | Error code              | M       |          |
| message | String | Error description       | M       |          |

<br>

# Error Code

## 1. Common

| Error Code   | Error Message                        | Description                       | Action Required                   |
|--------------|--------------------------------------|-----------------------------------|-----------------------------------|
| MSDKUTLxx000 | Invalid parameter : {name}           | Given parameter is invalid        | Check proper data type and length |

<br>

## 2. CryptoUtils

| Error Code      | Error Message                                      | Description                            | Action Required                        |
|-----------------|----------------------------------------------------|----------------------------------------|----------------------------------------|
| MSDKUTL00100    | Fail to create random key                   | Failure during random key generation   | Ensure random key generation logic     |
| MSDKUTL00101    | Fail to derive public key                          | Failed to derive public key            | Verify key derivation steps            |
| MSDKUTL00102    | Fail to generate shared secret using ECDH | ECDH shared secret generation failed   | Ensure correct ECDH implementation     |
| MSDKUTL00103    | Fail to derive key using PBKDF2                    | PBKDF2 key derivation failed           | Check PBKDF2 parameters and input      |
| MSDKUTL00200    | Fail to convert public key to external representation | Public key conversion failed  | Verify key format and conversion logic |
| MSDKUTL00201    | Fail to convert private key to external representation | Private key conversion failed | Check private key format and logic     |
| MSDKUTL00202    | Fail to convert private key to object     | Private key object conversion failed   | Check private key input and logic      |
| MSDKUTL00203    | Fail to convert public key to object      | Public key object conversion failed    | Check public key input and logic       |
| MSDKUTL00300    | Fail to encrypt using AES                          | AES encryption failure                 | Verify AES encryption implementation   |
| MSDKUTL00301    | Fail to decrypt using AES                          | AES decryption failure                 | Verify AES decryption implementation   |
| MSDKUTL00900    | Unsupported ECType : {type}                        | Unsupported elliptic curve type        | Use a supported EC type                |

<br>

## 3. MultibaseUtils

| Error Code      | Error Message                         | Description                         | Action Required                   |
|-----------------|---------------------------------------|-------------------------------------|-----------------------------------|
| MSDKUTL01100    | Fail to decode                        | Decoding process failed             | Verify encoding and input format  |
| MSDKUTL01900    | Unsupported encoding type : {type}    | Unsupported encoding type specified | Use a supported encoding type     |

<br>

## 4. DigestUtils

| Error Code      | Error Message                           | Description                         | Action Required                   |
|-----------------|-----------------------------------------|-------------------------------------|-----------------------------------|
| MSDKUTL02900    | Unsupported algorithm type : {type}     | Unsupported digest algorithm type   | Use a supported algorithm type    |

  <br>