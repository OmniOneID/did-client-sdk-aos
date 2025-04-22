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

Android KeyManager Core SDK API
==

- Topic: KeyManager
- Author: Sangjun Kim
- Date: 2024-07-08
- Version: v1.0.0

| Version | Date       | Changes                  |
| ------- | ---------- | ------------------------ |
| v1.0.0  | 2024-07-08 | Initial version          |

<div style="page-break-after: always;"></div>

# Contents
- [APIs](#api-list)
    - [1. constructor](#1-constructor)
    - [2. isAnyKeySaved](#2-isanykeysaved)
    - [3. isKeySaved](#3-iskeysaved)
    - [4. generateKey](#4-generatekey)
    - [5. changePin](#5-changepin)
    - [6. getKeyInfos(by IDs)](#6-getkeyinfosby-ids)
    - [7. getKeyInfos(by VerifyAuthType)](#7-getkeyinfosby-verifyauthtype)
    - [8. deleteKeys](#8-deletekeys)
    - [9. deleteAllKeys](#9-deleteallkeys)
    - [10. sign](#10-sign)
    - [11. verify](#11-verify)
- [Enumerators](#enumerators)
    - [1. ALGORITH_TYPE](#1-algorithm_type)
    - [2. WALLET_METHOD_TYPE](#2-wallet_method_type)
    - [3. KEYSTORE_ACCESS_METHOD](#3-keystore_access_method)
    - [4. STORAGE_OPTION](#4-storage_option)
    - [5. KEY_ACCESS_METHOD](#5-key_access_method)
    - [6. AUTH_TYPE](#6-auth_type)
- [Value Object](#value-object)
    - [1. KeyGenRequest](#1-keygenrequest)
    - [2. WalletKeyGenRequest](#2-walletkeygenrequest)
    - [3. KeyGenWalletMethodType](#3-keygenwalletmethodtype)
    - [4. SecureKeyGenRequest](#4-securekeygenrequest)
    - [5. KeyInfo](#5-keyinfo)

# APIs
## 1. Constructor

### Description
`KeyManager constructor`

### Declaration

```java
KeyManager(String fileName, Context context);
```

### Parameters

| Parameter | Type   | Description                | **M/O** | **Remarks** |
|-----------|--------|----------------------------|---------|-------------|
| fileName  | string | File name | M | The file name of the wallet to be stored by KeyManager |
| context   | Context | Context | M | |

### Returns

| Type       | Description       | **M/O** | **Remarks** |
|------------|-------------------|---------|-------------|
| KeyManager | KeyManager object | M | |

### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);
```
<br>

## 2. isAnyKeySaved

### Description
`Checks if any key is saved.`

### Declaration

```java
boolean isAnyKeySaved();
```

### Parameters

n/a

### Returns

| Type    | Description            | **M/O** | **Remarks** |
|---------|------------------------|---------|-------------|
| boolean | Indicates if any key is saved | M | |

### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

if (keyManager.isAnyKeySaved()) {
    List<KeyInfo> keyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.ANY);
}
```
<br>

## 3. isKeySaved

### Description
`Checks if the specified key is saved.`

### Declaration

```java
boolean isKeySaved(String id);
```

### Parameters
| Parameter | Type   | Description  | **M/O** | **Remarks** |
|-----------|--------|--------------|---------|-------------|
| id        | string | Key ID       | M       | Key ID      |

### Returns
| Type    | Description               | **M/O** | **Remarks** |
|---------|---------------------------|---------|-------------|
| boolean | Indicates if the key is saved | M       |             |

### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

if (!keyManager.isKeySaved("FREE")) {
    WalletKeyGenRequest keyGenInfo = new WalletKeyGenRequest(
        "FREE",
        AlgorithmType.ALGORITHM_TYPE.SECP256R1,
        StorageOption.STORAGE_OPTION.WALLET,
        new KeyGenWalletMethodType()
    );
}
```

<br>

## 4. generateKey

### Description
`Generates a signing key pair.`

### Declaration

```java
void generateKey(KeyGenRequest keyGenRequest) throws Exception
```

### Parameters
| Parameter     | Type          | Description           | **M/O** | **Remarks**               |
|---------------|---------------|-----------------------|---------|---------------------------|
| keyGenRequest | KeyGenRequest | Key generation request object | M       | [KeyGenRequest](#1-keygenrequest) |

### Returns
void

### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

// Free
WalletKeyGenRequest freeKeyRequest = new WalletKeyGenRequest(
    "FREE",
    AlgorithmType.ALGORITHM_TYPE.SECP256R1,
    StorageOption.STORAGE_OPTION.WALLET,
    new KeyGenWalletMethodType()
);
keyManager.generateKey(freeKeyRequest);

// PIN
WalletKeyGenRequest pinKeyRequest = new WalletKeyGenRequest(
    "PIN",
    AlgorithmType.ALGORITHM_TYPE.SECP256R1,
    StorageOption.STORAGE_OPTION.WALLET,
    new KeyGenWalletMethodType("111111".getBytes())
);
keyManager.generateKey(pinKeyRequest);

// BIO
SecureKeyGenRequest bioKeyRequest = new SecureKeyGenRequest(
    "BIO",
    AlgorithmType.ALGORITHM_TYPE.SECP256R1,
    StorageOption.STORAGE_OPTION.KEYSTORE,
    KeyStoreAccessMethod.KEYSTORE_ACCESS_METHOD.BIOMETRY
);
keyManager.generateKey(bioKeyRequest);
```

<br>

## 5. changePin

### Description
`Changes the signing PIN key.`

### Declaration

```java
void changePin(String id, byte[] oldPin, byte[] newPin) throws Exception
```

### Parameters

| Parameter | Type   | Description  | **M/O** | **Remarks** |
|-----------|--------|--------------|---------|-------------|
| id        | string | Key ID       | M       |             |
| oldPin    | byte[] | Current PIN  | M       |             |
| newPin    | byte[] | New PIN      | M       |             |

### Returns

void

### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

keyManager.changePin("PIN", "password".getBytes(), "newPassword".getBytes());
```

<br>

## 6. getKeyInfos(by IDs)

### Description
`Returns key information for the specified list of IDs stored in the wallet.`

### Declaration

```java
List<KeyInfo> getKeyInfos(List<String> ids) throws Exception
```

### Parameters

| Parameter | Type                 | Description     | **M/O** | **Remarks** |
|-----------|----------------------|-----------------|---------|-------------|
| ids       | List&lt;String&gt;   | List of key IDs | M       |             |

### Returns

| Type                   | Description        | **M/O** | **Remarks**          |
|------------------------|--------------------|---------|----------------------|
| List&lt;KeyInfo&gt;    | List of key information | M       | [KeyInfo](#5-keyinfo) |

### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

List<KeyInfo> keyInfos = keyManager.getKeyInfos(List.of("PIN", "BIO"));
```

<br>

## 7. getKeyInfos(by VerifyAuthType)

### Description
`Returns key information for the specified Verify AuthType stored in the wallet.`

### Declaration

```java
List<KeyInfo> getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE keyType) throws Exception
```

### Parameters

| Parameter | Type                    | Description          | **M/O** | **Remarks**                             |
|-----------|-------------------------|----------------------|---------|-----------------------------------------|
| keyType   | VERIFY_AUTH_TYPE        | Verification auth type | M       | [VERIFY_AUTH_TYPE](#7-verify_auth_type) |

### Returns

| Type                   | Description        | **M/O** | **Remarks**          |
|------------------------|--------------------|---------|----------------------|
| List&lt;KeyInfo&gt;    | List of key information | M       | [KeyInfo](#5-keyinfo) |

### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

// Get all keys
List<KeyInfo> keyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.ANY);

// Get Free keys
List<KeyInfo> freeKeyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.FREE);

// Get PIN keys
List<KeyInfo> pinKeyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.PIN);

// Get Bio keys
List<KeyInfo> bioKeyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.BIO);

// Get PIN or Bio keys
List<KeyInfo> orKeyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.PIN_OR_BIO);

// Get PIN and Bio keys
List<KeyInfo> andKeyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.PIN_AND_BIO);
```

<br>

## 8. deleteKeys

### Description
`Deletes the keys corresponding to the specified list of IDs from the wallet. Keys stored in the Keystore are also deleted.`

### Declaration

```java
void deleteKeys(List<String> ids) throws Exception
```

### Parameters

| Parameter | Type                 | Description     | **M/O** | **Remarks** |
|-----------|----------------------|-----------------|---------|-------------|
| ids       | List&lt;String&gt;   | List of key IDs | M       |             |

### Returns

void

### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

keyManager.deleteKeys(List.of("PIN", "BIO"));
```

<br>

## 9. deleteAllKeys

### Description
`Deletes the wallet file. If the keys are stored in the Keystore, they are also deleted.`

### Declaration

```java
void deleteAllKeys() throws Exception
```

### Parameters

n/a

### Returns

void

### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

if (keyManager.isAnyKeySaved()) {
    keyManager.deleteAllKeys();
}
```

<br>

## 10. sign

### Description
`Signs data using the signing key stored in the wallet.`

### Declaration

```java
byte[] sign(String id, byte[] pin, byte[] digest) throws Exception
```

### Parameters

| Parameter | Type   | Description         | **M/O** | **Remarks**                         |
|-----------|--------|---------------------|---------|-------------------------------------|
| id        | string | Key ID              | M       |                                     |
| pin       | byte[] | PIN                 | O       | Null if not a PIN key               |
| digest    | byte[] | Hash of the data to be signed | M       |                                     |

### Returns

| Type    | Description   | **M/O** | **Remarks** |
|---------|----------------|---------|-------------|
| byte[]  | Signature      | M       |             |

### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

byte[] plainData = "Test".getBytes();
byte[] digest = DigestUtils.getDigest(plainData, DigestEnum.DIGEST_ENUM.SHA_256);

// Not a PIN key
byte[] signature = keyManager.sign("FREE", null, digest);

// PIN key
byte[] signature = keyManager.sign("PIN", pinData, digest);
```

<br>

## 11. verify

### Declaration

```java
void verify(AlgorithmType.ALGORITHM_TYPE algorithmType, byte[] publicKey, byte[] digest, byte[] signature) throws Exception
```

### Description
`Verifies the signature using the specified public key.`

### Parameters

| Parameter     | Type                   | Description          | **M/O** | **Remarks**                              |
|---------------|------------------------|----------------------|---------|------------------------------------------|
| algorithmType | ALGORITHM_TYPE         | Algorithm type       | M       | [ALGORITHM_TYPE](#1-algorithm_type)      |
| publicKey     | byte[]                 | Public key           | M       |                                          |
| digest        | byte[]                 | Hash of the data     | M       |                                          |
| signature     | byte[]                 | Signature            | M       |                                          |

### Returns

void

### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

byte[] plainData = "Test".getBytes();
byte[] digest = DigestUtils.getDigest(plainData, DigestEnum.DIGEST_ENUM.SHA_256);

byte[] publicKey = MultibaseUtils.decode("mAqqzZ4mBeuQ+i8fwQPZ0bKhW6KgCzwW+djg+hsOQCG/o");
byte[] signature = MultibaseUtils.decode("f1f86c9060978bb9dd96b2cc969177fcbb1f79ac88214e76cfa8ed07b0d01bcdb9fe6fd2508d9b4f8f705286973318fd402fca2d64e14f4ff21e449cce2c31850d5");

keyManager.verify(AlgorithmType.ALGORITHM_TYPE.SECP256R1, publicKey, digest, signature);
```

# Enumerators
## 1. ALGORITHM_TYPE

### Description

`Algorithm types (defined in DataModel SDK)`

### Declaration

```java
public enum ALGORITHM_TYPE {
    RSA("Rsa"),
    SECP256K1("Secp256k1"),
    SECP256R1("Secp256r1");
}
```

## 2. WALLET_METHOD_TYPE

### Description

`Wallet access types`

### Declaration

```java
public enum WALLET_METHOD_TYPE {
    NONE("NONE"),
    PIN("PIN");
}
```

## 3. KEYSTORE_ACCESS_METHOD

### Description

`Keystore access types`

### Declaration

```java
public enum KEYSTORE_ACCESS_METHOD {
    NONE("NONE"),
    BIOMETRY("BIOMETRY");
}
```

## 4. STORAGE_OPTION

### Description

`Key storage options`

### Declaration

```java
public enum STORAGE_OPTION {
    WALLET(0),
    KEYSTORE(1);
}
```

## 5. KEY_ACCESS_METHOD

### Description

`Key storage and access types`

### Declaration

```java
public enum KEY_ACCESS_METHOD {
    WALLET_NONE(0),
    WALLET_PIN(1),
    KEYSTORE_NONE(8),
    KEYSTORE_BIOMETRY(9);
}
```

## 6. AUTH_TYPE

### Description

`Key access types (defined in DataModel SDK)`

### Declaration

```java
public enum AUTH_TYPE {
    FREE(1),
    PIN(2),
    BIO(4);
}
```

## 7. VERIFY_AUTH_TYPE

### Description

`Submission authentication types (defined in DataModel SDK)`

### Declaration

```java
public enum VERIFY_AUTH_TYPE implements IntEnum {
    ANY(0x00000000),
    FREE(0x00000001),
    PIN(0x00000002),
    BIO(0x00000004),
    PIN_OR_BIO(0x00000006),
    PIN_AND_BIO(0x00008006);
}
```

# Value Object

## 1. KeyGenRequest

### Description

`Key generation request`

### Declaration

```java
public class KeyGenRequest {
    String id;
    AlgorithmType.ALGORITHM_TYPE algorithmType;
    StorageOption.STORAGE_OPTION storage;
}
```

### Property

| Name          | Type              | Description            | **M/O** | **Note**                               |
|---------------|-------------------|------------------------|---------|----------------------------------------|
| algorithmType | ALGORITHM_TYPE    | Algorithm type for Key | M       | [ALGORITHM_TYPE](#1-algorithm_type)    |
| id            | String            | Key name               | M       |                                        |
| storage       | STORAGE_OPTION    | Key storage            | M       | [STORAGE_OPTION](#4-storage_option)    |


## 2. WalletKeyGenRequest

### Description

`Wallet key generation request - Inherits from KeyGenRequest`

### Declaration

```java
public class WalletKeyGenRequest extends KeyGenRequest {
    AlgorithmType.ALGORITHM_TYPE algorithmType;
    String id;
    KeyGenWalletMethodType methodType;
    StorageOption.STORAGE_OPTION storage = STORAGE_OPTION.WALLET;
}
```

### Property

| Name        | Type                   | Description           | **M/O** | **Note**                                |
|-------------|------------------------|-----------------------|---------|-----------------------------------------|
| methodType  | KeyGenWalletMethodType | Method type for Wallet| M       | [KeyGenWalletMethodType](#3-keygenwalletmethodtype)|


## 3. KeyGenWalletMethodType

### Description

`Access type for wallet key generation request`

### Declaration

```java
public class KeyGenWalletMethodType {
    WALLET_METHOD_TYPE walletMethodType;
    private byte[] pin;
            
    public enum WALLET_METHOD_TYPE {
        NONE("NONE"),
        PIN("PIN");
    }
}
```

### Property

| Name              | Type                | Description                 | **M/O** | **Note**                               |
|-------------------|---------------------|-----------------------------|---------|----------------------------------------|
| walletMethodType  | WALLET_METHOD_TYPE  | Access method for Wallet    | M       | [WALLET_METHOD_TYPE](#2-wallet_method_type) |
| pin               | byte[]              | PIN for signing             | M       |                                        |


## 4. SecureKeyGenRequest

### Description

`Keystore key generation request - Inherits from KeyGenRequest`

### Declaration

```java
public class SecureKeyGenRequest extends KeyGenRequest {
    AlgorithmType.ALGORITHM_TYPE algorithmType = ALGORITHM_TYPE.SECP256R1;
    String id;
    KeyStoreAccessMethod.KEYSTORE_ACCESS_METHOD accessMethod;
    StorageOption.STORAGE_OPTION storage = STORAGE_OPTION.KEYSTORE;
}
```

### Property

| Name          | Type                      | Description                     | **M/O** | **Note**                               |
|---------------|---------------------------|---------------------------------|---------|----------------------------------------|
| accessMethod  | KEYSTORE_ACCESS_METHOD    | Access method for Keystore      | M       | [KEYSTORE_ACCESS_METHOD](#3-keystore_access_method) |

## 5. KeyInfo

### Description

`Key information`

### Declaration

```java
public class Meta {
    String id;
}
public class KeyInfo extends Meta {
    AuthType.AUTH_TYPE authType;
    AlgorithmType.ALGORITHM_TYPE algorithmType;
    String publicKey;
    KeyAccessMethod.KEY_ACCESS_METHOD accessMethod;
}
```

### Property

| Name          | Type               | Description                                | **M/O** | **Note**                           |
|---------------|--------------------|--------------------------------------------|---------|------------------------------------|
| algorithmType | ALGORITHM_TYPE     | Algorithm type for Key                     | M       | [ALGORITHM_TYPE](#1-algorithm_type) |
| id            | String             | Key name                                   | M       |                                    |
| accessMethod  | KEY_ACCESS_METHOD  | Indicates key storage and its access method | M       | [KEY_ACCESS_METHOD](#5-key_access_method) |
| authType      | AUTH_TYPE          | Access method                              | M       | [AUTH_TYPE](#6-auth_type)          |
| publicKey     | String             | Public key                                 | M       | Encoded by Multibase               |