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

Android Utility SDK API
==

- Subject: CryptoUtils / MultibaseUtils / DigestUtils
- Author: Sangjun Kim
- Date: 2024-07-10
- Version: v1.0.0

| Version | Date       | Changes                  |
| ------- | ---------- | ------------------------ |
| v1.0.0  | 2024-07-10 | Initial version          |


<div style="page-break-after: always;"></div>

# Table of Contents
- [APIs](#api-list)
    - [CryptoUtils](#cryptoutils)
        - [1. generateNonce](#1-generatenonce)
        - [2. generateECKeyPair](#2-generateeckeypair)
        - [3. generateSharedSecret](#3-generatesharedsecret)
        - [4. pbkdf2](#4-pbkdf2)
        - [5. encrypt](#5-encrypt)
        - [6. decrypt](#6-decrypt)
    - [MultibaseUtils](#multibaseutils)
        - [1. encode](#1-encode)
        - [2. decode](#2-decode)
    - [DigestUtils](#digestutils)
        - [1. getDigest](#1-getdigest) 
- [Enumerators](#enumerators)
    - [1. EC_TYPE](#1-ec_type)
    - [2. ENCRYPTION_TYPE](#2-encryption_type)
    - [3. ENCRYPTION_MODE](#3-encryption_mode)
    - [4. SYMMETRIC_KEY_SIZE](#4-symmetric_key_size)
    - [5. SYMMETRIC_CIPHER_TYPE](#5-symmetric_cipher_type)
    - [6. SYMMETRIC_PADDING_TYPE](#6-symmetric_padding_type)
    - [7. MULTIBASE_TYPE](#7-multibase_type)
    - [8. DIGEST_ENUM](#8-digest_enum)
- [Value Object](#value-object)
    - [1. EcKeyPair](#1-eckeypair)
    - [2. CipherInfo](#2-cipherinfo)


# API List
## CryptoUtils
Class related to cryptographic functions

### 1. generateNonce

#### Description
`Generates a random nonce.`

#### Declaration

```java
static byte[] generateNonce(int size) throws Exception
```

#### Parameters

| Parameter | Type   | Description                | **M/O** | **Note** |
|-----------|--------|----------------------------|---------|-------------|
| size    | int    | nonce length |M| |

#### Returns

| Type | Description                |**M/O** | **Note** |
|------|----------------------------|---------|-------------|
| byte[]  | nonce |M| |

#### Usage
```java
byte[] salt = CryptoUtils.generateNonce(20);
```

<br>

### 2. generateECKeyPair

#### Description
`Generates a key pair for ECDH usage.`

#### Declaration

```java
static EcKeyPair generateECKeyPair(EcType.EC_TYPE ecType) throws Exception
```

#### Parameters

| Name      | Type   | Description                | **M/O** | **Note**            |
|-----------|--------|----------------------------|---------|---------------------|
| ecType | EC_TYPE | EC Algorithm Type          | M       | [EC_TYPE](#1-ec_type) |

#### Returns

| Type | Description                |**M/O** | **Note** |
|------|----------------------------|---------|---------|
| EcKeyPair | Key Pair Object               | M       | |



#### Usage
```java
EcKeyPair dhKeyPair = CryptoUtils.generateECKeyPair(EcType.EC_TYPE.SECP256_R1);
```

<br>

### 3. generateSharedSecret

#### Description
`ECDH Generates Shared Secret Key`

#### Declaration

```java
static byte[] generateSharedSecret(EcType.EC_TYPE ecType, byte[] privateKey, byte[] publicKey) throws Exception
```

#### Parameters

| Name      | Type   | Description                | **M/O** | **Note**            |
|-----------|--------|----------------------------|---------|---------------------|
| ecType | EC_TYPE | EC algorithm type          | M       | [EC_TYPE](#1-ec_type) |
| privateKey | byte[] | Private key          | M       |  |
| publicKey | byte[] | Public key          | M       |  |

#### Returns

| Type | Description                |**M/O** | **Note** |
|------|----------------------------|---------|---------|
| byte[]  | Shared Secret |M| |



#### Usage
```java
byte[] privKey = MultibaseUtils.decode("zHr5d9pyMRnyz2aByr6dYdV5kdfnWRUkiFxjSaoFJwecs");
byte[] serverPubKey = MultibaseUtils.decode("f02eb2044610ba2c3960f9d91196bdb1c5498beebcb04983ed6ebe2329c3612907c");

byte[] sharedSecret = CryptoUtils.generateSharedSecret(EcType.EC_TYPE.SECP256_R1, privKey, serverPubKey );
```

<br>

### 4. pbkdf2

#### Description
`Derives a key based on password using PBKDF2 algorithm`

#### Declaration

```java
static byte[] pbkdf2(byte[] password, byte[] salt, int iterations, int derivedKeyLength) throws Exception
```

#### Parameters
| Parameter | Type   | Description                | **M/O** | **Note** |
|-----------|--------|----------------------------|---------|---------|
| password    | byte[]    | seed data |M| |
| salt    | byte[] | salt |    M    |  |
| iterations    | int | iterations |    M    | |
| derivedKeyLength   | int  | length of the derived key  |    M    |  |

#### Returns

| Type | Description                |**M/O** | **Note** |
|------|----------------------------|---------|---------|
| byte[]  | derived data |M| |


#### Usage
```java
byte[] salt = CryptoUtils.generateNonce(20);
byte[] symmetricKey = CryptoUtils.pbkdf2("password".getBytes(), salt, 2048, 48);          
```

<br>

### 5. encrypt

#### Description
`Encryption`

#### Declaration

```java
static byte[] encrypt(byte[] plain, CipherInfo info, byte[] key, byte[] iv) throws Exception
```

#### Parameters

| Parameter | Type   | Description                | **M/O** | **Note** |
|-----------|--------|----------------------------|---------|---------|
| plain    | byte[]    | Data to be encrypted |M| |
| info    | CipherInfo | Encryption information |    M    | [CipherInfo](#2-cipherinfo) |
| key    | byte[] | Symmetric key |    M    | |
| iv   | byte[]  | Initialization vector (iv)  |    M    |  |

#### Returns

| Type | Description                |**M/O** | **Note** |
|------|----------------------------|---------|---------|
| byte[]  | Encrypted data |M| |


#### Usage
```java
byte[] encData = CryptoUtils.encrypt(
                    plainData,
                    new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                    MultibaseUtils.decode("fbfaee9e0506786f61d2a97e29d034ae47955c68791049a6377ebe8d1bedb7ef4"),
                    MultibaseUtils.decode("z75M7MfQsC4p2rTxeKxYh2M")
                    );
```

<br>

### 6. decrypt

#### Description
`Decrypt`

#### Declaration

```java
static byte[] decrypt(byte[] cipher, CipherInfo info, byte[] key, byte[] iv) throws Exception
```

#### Parameters

| Parameter | Type   | Description                | **M/O** | **Note** |
|-----------|--------|----------------------------|---------|-------------|
| cipher    | byte[]    | Encrypted data |M| |
| info    | CipherInfo | Encryption information |    M    | [CipherInfo](#2-cipherinfo) |
| key    | byte[] | Symmetric key |    M    | |
| iv   | byte[]  | iv  |    M    |  |

#### Returns
| Type | Description                |**M/O** | **Note** |
|------|----------------------------|---------|-------------|
| byte[]  | Decrypted data |M| |

#### Usage
```java
byte[] decData = CryptoUtils.decrypt(
                    cipher,
                    new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                    MultibaseUtils.decode("fbfaee9e0506786f61d2a97e29d034ae47955c68791049a6377ebe8d1bedb7ef4"),
                    MultibaseUtils.decode("z75M7MfQsC4p2rTxeKxYh2M")
                    );
```

<br>

## MultibaseUtils
Multibase Encoding/Decoding Class

### 1. encode

#### Description
`Multibase Encoding`

#### Declaration

```java
static String encode(MultibaseType.MULTIBASE_TYPE type, byte[] data)
```

#### Parameters

| Parameter | Type   | Description                | **M/O** | **Note** |
|-----------|--------|----------------------------|---------|-------------|
| type    | MULTIBASE_TYPE    | Multibase type |M|  [MULTIBASE_TYPE](#7-multibase_type) |
| data    | byte[]    | Data to be encoded |M|  |

#### Returns

| Type | Description                |**M/O** | **Note** |
|------|----------------------------|---------|-------------|
| byte[]  | Encoded string |M| |

#### Usage
```java
 String secretStr = MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_16, sharedSecret);
```

<br>

### 2. decode

#### Description
`Encoded string`

#### Declaration

```java
static byte[] decode(String encoded) throws Exception
```

#### Parameters

| Parameter | Type   | Description                | **M/O** | **Note** |
|-----------|--------|----------------------------|---------|-------------|
| encoded    | String    | Encoded string | M |  |

#### Returns

| Type | Description                |**M/O** | **Note** |
|------|----------------------------|---------|-------------|
| byte[]  | Decoded data | M | |

#### Usage
```java
byte[] salt = MultibaseUtils.decode("f6c646576656c6f7065726c3139383540676d61696c2e636f6d");
```

<br>

## DigestUtils
Hash function utility class

### 1. getDigest

#### Description
`Hash`

#### Declaration

```java
static byte[] getDigest(byte[] source, DIGEST_ENUM digestEnum)
```

#### Parameters

| Parameter | Type   | Description                | **M/O** | **Note** |
|-----------|--------|----------------------------|---------|------------|
| source    | byte[]    |  Data to be hashed |M| |
| digestEnum    | DIGEST_ENUM    |  Hash algorithm |M|[DIGEST_ENUM](#8-digest_enum)|


#### Returns

| Type | Description                |**M/O** | **Note** |
|------|----------------------------|---------|------------|
| byte[]  | Hashed data |M| |


#### Usage
```java
byte[] digest = DigestUtils.getDigest(plainData, DigestEnum.DIGEST_ENUM.SHA_256);
```

<br>

# Enumerators
## 1. EC_TYPE

### Description

`EC Key Algorithm Type`

### Declaration

```java
public enum EC_TYPE {
    SECP256_K1("Secp256k1"),
    SECP256_R1("Secp256r1");
}
```
<br>

## 2. ENCRYPTION_TYPE

### Description

`Encryption Type`

### Declaration

```java
public enum ENCRYPTION_TYPE {
     AES("AES");
}
```
<br>

## 3. ENCRYPTION_MODE

### Description

`Operation Mode`

### Declaration

```java
public enum ENCRYPTION_MODE {
     CBC("CBC"),
     ECB("ECB");
}
```
<br>

## 4. SYMMETRIC_KEY_SIZE

### Description

`Symmetric Key Length`

### Declaration

```java
public enum SYMMETRIC_KEY_SIZE {
    AES_128("128"),
    AES_256("256");
}
```
<br>

## 5. SYMMETRIC_CIPHER_TYPE

### Description

`Encryption and Decryption Type`

### Declaration

```java
public enum SYMMETRIC_CIPHER_TYPE {
    AES128CBC("AES-128-CBC"),
    AES128ECB("AES-128-ECB"),
    AES256CBC("AES-256-CBC"),
    AES256ECB("AES-256-ECB");
}
```
<br>

## 6. SYMMETRIC_PADDING_TYPE

### Description

`Padding Type`

### Declaration

```java
public enum SYMMETRIC_PADDING_TYPE {
    NOPAD("NOPAD","NOPAD"),
    PKCS5("PKCS5","PKCS5Padding");
}
```
<br>

## 7. MULTIBASE_TYPE

### Description

`Multibase Encoding Type`

### Declaration

```java
public enum MULTIBASE_TYPE {
    BASE_16("f"),
    BASE_16_UPPER("F"),
    BASE_58_BTC("z"),
    BASE_64("m"),
    BASE_64_URL("u");
}
```
<br>

## 8. DIGEST_ENUM

### Description

`hash algorithm`

### Declaration

```java
public enum DIGEST_ENUM {
    SHA_256("SHA256"),
    SHA_384("SHA384"),
    SHA_512("SHA512");
}
```
<br>

# Value Object

## 1. EcKeyPair

### Description

`EC key pair information`

### Declaration

```java
public class EcKeyPair {
    EcType.EC_TYPE ecType;
    byte[] privateKey;
    byte[] publicKey;
}
```

### Property

| Name          | Type               | Description                      | **M/O** | **Note**                    |
|---------------|--------------------|----------------------------------|---------|-----------------------------|
| ecType | EC_TYPE | EC algorithm type          | M       | [EC_TYPE](#1-ec_type) |
| privateKey | byte[] | Private key          | M       |  |
| publicKey | byte[] | Public key          | M       |  |

<br>

## 2. CipherInfo

### Description

`Encryption Information`

### Declaration

```java
public class CipherInfo {
    ENCRYPTION_TYPE type; "Encryption type enum. ex) AES"
    ENCRYPTION_MODE mode; "Operation mode enum. ex) CBC, ECB"
    SYMMETRIC_KEY_SIZE size; "Symmetric key size enum. ex) 128, 256"
    SYMMETRIC_PADDING_TYPE padding; "Padding enum. ex) NOPAD, PKCS5Padding"
}
```

### Property

| Name          | Type               | Description                      | **M/O** | **Note**                    |
|---------------|--------------------|----------------------------------|---------|-----------------------------|
| type | ENCRYPTION_TYPE | Encryption Type          | M       | [ENCRYPTION_TYPE](#2-encryption_type) |
| mode | ENCRYPTION_MODE | Operation Mode          | M       | [ENCRYPTION_MODE](#3-encryption_mode) |
| size | SYMMETRIC_KEY_SIZE | Symmetric Key Size         | M       | [SYMMETRIC_KEY_SIZE](#4-symmetric_key_size) |
| padding | SYMMETRIC_PADDING_TYPE | Padding          | M       | [SYMMETRIC_PADDING_TYPE](#6-symmetric_padding_type) |
