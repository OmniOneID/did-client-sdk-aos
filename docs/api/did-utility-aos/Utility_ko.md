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

- 주제: CryptoUtils / MultibaseUtils / DigestUtils
- 작성: Sangjun Kim
- 일자: 2024-07-10
- 버전: v1.0.0

| 버전   | 일자       | 변경 내용                 |
| ------ | ---------- | -------------------------|
| v1.0.0 | 2024-07-10 | 초기 작성                 |


<div style="page-break-after: always;"></div>

# 목차
- [APIs](#api-목록)
    - [CryptoUtils](#cryptoutils)
        - [1. generateNonce](#1-generatenonce)
        - [2. generateECKeyPair](#2-generateerkeypair)
        - [3. generateSharedSecret](#3-generatesharedsecret)
        - [4. pbkdf2](#4-pbkdf2)
        - [5. encrypt](#5-encrypt)
        - [6. decrypt](#6-decrypt)
    - [MultibaseUtils](#multibaseutils)
        - [1. encode](#1-encode)
        - [2. decode](#2-decode)
    - [DigestUtils](#digetutils)
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


# API 목록
## CryptoUtils
암호화 기능 관련 클래스

### 1. generateNonce

#### Description
`랜덤한 nonce 생성.`

#### Declaration

```java
static byte[] generateNonce(int size) throws Exception
```


#### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| size    | int    | nonce length |M| |

#### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| byte[]  | nonce |M| |


#### Usage
```java
byte[] salt = CryptoUtils.generateNonce(20);
```

<br>

### 2. generateECKeyPair

#### Description
`ECDH 용도의 키 쌍 생성.`

#### Declaration

```java
static EcKeyPair generateECKeyPair(EcType.EC_TYPE ecType) throws Exception
```

#### Parameters

| Name      | Type   | Description                | **M/O** | **Note**            |
|-----------|--------|----------------------------|---------|---------------------|
| ecType | EC_TYPE | EC 알고리즘 타입          | M       | [EC_TYPE](#1-ec_type) |

#### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| EcKeyPair | 키쌍 객체               | M       | |



#### Usage
```java
EcKeyPair dhKeyPair = CryptoUtils.generateECKeyPair(EcType.EC_TYPE.SECP256_R1);
```

<br>

### 3. generateSharedSecret

#### Description
`ECDH 비밀 공유키 생성`

#### Declaration

```java
static byte[] generateSharedSecret(EcType.EC_TYPE ecType, byte[] privateKey, byte[] publicKey) throws Exception
```

#### Parameters

| Name      | Type   | Description                | **M/O** | **Note**            |
|-----------|--------|----------------------------|---------|---------------------|
| ecType | EC_TYPE | EC 알고리즘 타입          | M       | [EC_TYPE](#1-ec_type) |
| privateKey | byte[] | 개인키          | M       |  |
| publicKey | byte[] | 공개키          | M       |  |

#### Returns

| Type | Description                |**M/O** | **비고** |
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
`PBKDF2 알고리즘으로 password 기반으로 키 파생`

#### Declaration

```java
static byte[] pbkdf2(byte[] password, byte[] salt, int iterations, int derivedKeyLength) throws Exception
```


#### Parameters
| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| password    | byte[]    | seed 데이터 |M| |
| salt    | byte[] | salt |    M    |  |
| iterations    | int | iterations |    M    | |
| derivedKeyLength   | int  | 파생시킬 키의 길이  |    M    |  |

#### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| byte[]  | 파생된 데이터 |M| |


#### Usage
```java
byte[] salt = CryptoUtils.generateNonce(20);
byte[] symmetricKey = CryptoUtils.pbkdf2("password".getByte(), salt, 2048, 48);          
```

<br>

### 5. encrypt

#### Description
`암호화 `

#### Declaration

```java
static byte[] encrypt(byte[] plain, CipherInfo info, byte[] key, byte[] iv) throws Exception
```

#### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| plain    | byte[]    | 암호화 할 데이터 |M| |
| info    | CipherInfo | 암호화 정보 |    M    | [CipherInfo](#2-cipherinfo) |
| key    | byte[] | 대칭키 |    M    | |
| iv   | byte[]  | iv  |    M    |  |

#### Returns


| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| byte[]  | 암호화된 데이터 |M| |


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
`복호화`

#### Declaration

```java
static byte[] decrypt(byte[] cipher, CipherInfo info, byte[] key, byte[] iv) throws Exception
```

#### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| cipher    | byte[]    | 암호화 된 데이터 |M| |
| info    | CipherInfo | 암호화 정보 |    M    | [CipherInfo](#2-cipherinfo) |
| key    | byte[] | 대칭키 |    M    | |
| iv   | byte[]  | iv  |    M    |  |


#### Returns
| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| byte[]  | 복호화 된 데이터 |M| |

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
Multibase 인코딩/디코딩 클래스

### 1. encode

#### Description
`Multibase 인코딩`

#### Declaration

```java
static String encode(MultibaseType.MULTIBASE_TYPE type, byte[] data)
```

#### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| type    | MULTIBASE_TYPE    | Multibase 타입 |M|  [MULTIBASE_TYPE](#7-multibase_type) |
| data    | byte[]    | 인코딩 할 데이터 |M|  |

#### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| byte[]  | 인코딩 된 문자열 |M| |


#### Usage
```java
 String secretStr = MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_16, sharedSecret);
```

<br>

### 2. decode

#### Description
`인코딩 된 문자열`

#### Declaration

```java
static byte[] decode(String encoded) throws Exception
```

#### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| encoded    | String    | 인코딩 된 문자열 |M|  |

#### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| byte[]  | 디코딩 된 데이터 |M| |



#### Usage
```java
byte[] salt = MultibaseUtils.decode("f6c646576656c6f7065726c3139383540676d61696c2e636f6d");
```

<br>

## DigestUtils
Hash 기능 유틸 클래스

### 1. getDigest

#### Description
`Hash`

#### Declaration

```java
static byte[] getDigest(byte[] source, DIGEST_ENUM digestEnum)
```


#### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| source    | byte[]    |  해시할 데이터 |M| |
| digestEnum    | DIGEST_ENUM    |  해시 알고리즘 |M|[DIGEST_ENUM](#8-digest_enum)|


#### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| byte[]  | 해시 된 데이터 |M| |


#### Usage
```java
byte[] digest = DigestUtils.getDigest(plainData, DigestEnum.DIGEST_ENUM.SHA_256);
```

<br>


# Enumerators
## 1. EC_TYPE

### Description

`EC키 알고리즘 타입`

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

`암호화 타입`

### Declaration

```java
public enum ENCRYPTION_TYPE {
     AES("AES");
}
```
<br>

## 3. ENCRYPTION_MODE

### Description

`운용모드`

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

`대칭키 길이`

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

`암복호화 타입`

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

`패딩 타입`

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

`Multibase 인코딩 타입`

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

`해시 알고리즘`

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

`EC키쌍 정보`

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
| ecType | EC_TYPE | EC 알고리즘 타입          | M       | [EC_TYPE](#1-ec_type) |
| privateKey | byte[] | 개인키          | M       |  |
| publicKey | byte[] | 공개키          | M       |  |

<br>

## 2. CipherInfo

### Description

`암호화 정보`

### Declaration

```java
public class CipherInfo {
    ENCRYPTION_TYPE type; "암호화 타입 enum. ex) AES"
    ENCRYPTION_MODE mode; "운용모드 enum. ex) CBC, ECB"
    SYMMETRIC_KEY_SIZE size; "대칭키 길이 enum. ex) 128, 256"
    SYMMETRIC_PADDING_TYPE padding; "패딩 enum. ex) NOPAD, PKCS5Padding"
}
```

### Property

| Name          | Type               | Description                      | **M/O** | **Note**                    |
|---------------|--------------------|----------------------------------|---------|-----------------------------|
| type | ENCRYPTION_TYPE | 암호화 타입          | M       | [ENCRYPTION_TYPE](#2-encryption_type) |
| mode | ENCRYPTION_MODE | 운용모드          | M       | [ENCRYPTION_MODE](#3-encryption_mode) |
| size | SYMMETRIC_KEY_SIZE | 대칭키 길이         | M       | [SYMMETRIC_KEY_SIZE](#4-symmetric_key_size) |
| padding | SYMMETRIC_PADDING_TYPE | 패딩          | M       | [SYMMETRIC_PADDING_TYPE](#6-symmetric_padding_type) |