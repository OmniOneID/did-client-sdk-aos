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

Android SecureEncryptor Core SDK API
==

- Topic: DidManager
- Author: Sangjun Kim
- Date: 2024-07-10
- Version: v1.0.0

| Version | Date       | Change Details            |
| ------- | ---------- | ------------------------- |
| v1.0.0  | 2024-07-10 | Initial version           |


<div style="page-break-after: always;"></div>

# Table of Contents
- [APIs](#api-list)
    - [1. encrypt](#1-encrypt)
    - [2. decrypt](#2-decrypt)

# APIs
## 1. encryptor

### Description
`Encrypts data using the encryption key from Keystore.`

### Declaration

```java
static byte[] encrypt(byte[] plainData, Context context) throws Exception
```

### Parameters

| Parameter | Type   | Description                | **M/O** | **Remarks** |
|-----------|--------|----------------------------|---------|-------------|
| plainData | byte[] | Data to be encrypted       | M       |             |
| context   | Context| Context                    | M       | Needed to create a key in Keystore if not present |

### Returns

| Type   | Description           | **M/O** | **Remarks** |
|--------|-----------------------|---------|-------------|
| byte[] | Encrypted data        | M       |             |


### Usage
```java
byte[] encData = SecureEncryptor.encrypt("plainData".getBytes(), context);
```

<br>

## 2. decrypt

### Description
`Decrypts data using the decryption key from Keystore.`

### Declaration

```java
static byte[] decrypt(byte[] cipherData) throws Exception
```

### Parameters

| Parameter  | Type   | Description      | **M/O** | **Remarks** |
|------------|--------|------------------|---------|-------------|
| cipherData | byte[] | Encrypted data   | M       |             |

### Returns

| Type   | Description         | **M/O** | **Remarks** |
|--------|---------------------|---------|-------------|
| byte[] | Decrypted data      | M       |             |



### Usage
```java
byte[] decData = SecureEncryptor.decrypt(cipherData);
```

<br>