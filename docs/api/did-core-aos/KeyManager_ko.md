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

- 주제: KeyManager
- 작성: Sangjun Kim
- 일자: 2024-07-08
- 버전: v1.0.0

| 버전   | 일자       | 변경 내용                 |
| ------ | ---------- | -------------------------|
| v1.0.0 | 2024-07-08 | 초기 작성                 |


<div style="page-break-after: always;"></div>

# 목차
- [APIs](#api-목록)
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

# API 목록
## 1. Constructor

### Description
`KeyManager costructor`

### Declaration

```java
KeyManager(String fileName, Context context);
```


### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| fileName    | string    | 파일명 |M|KeyManager에서 저장할 월렛의 파일명|
| context    | Context | context |M|  |

### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| KeyManager  | KeyManager 객체 |M| |


### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);
```

<br>

## 2. isAnyKeySaved

### Description
`저장된 키의 여부를 확인합니다.`

### Declaration

```java
boolean isAnyKeySaved();
```

### Parameters

n/a

### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| boolean  | 저장된 키 유무 |M| |



### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this)

if (keyManager.isAnyKeysSaved()) {
  List<KeyInfo> KeyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.ANY);
}
```

<br>

## 3. isKeySaved

### Description
`해당키의 저장여부를 확인합니다.`

### Declaration

```java
boolean isKeySaved(String id)
```

### Parameters
| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| id    | string    | 키아이디 |M|키아이디|


### Returns
| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| boolean  | 해당키 저장 유무 |M| |



### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

if(!keyManager.isKeySaved("FREE")) {
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
`서명키쌍을 생성합니다.`

### Declaration

```java
void generateKey(KeyGenRequest keyGenRequest) throws Exception
```

### Parameters
| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| keyGenRequest    | KeyGenRequest    | 키생성요청 객체 |M| [KeyGenRequest](#1-keygenrequest)|


### Returns
void


### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

//Free
WalletKeyGenRequest freeKeyRequest  = new WalletKeyGenRequest(
                    "FREE",
                    AlgorithmType.ALGORITHM_TYPE.SECP256R1,
                    StorageOption.STORAGE_OPTION.WALLET,
                    new KeyGenWalletMethodType()
                    );
keyManager.generateKey(freeKeyRequest);

//pin
WalletKeyGenRequest pinKeyRequest = new WalletKeyGenRequest(
                "PIN",
                AlgorithmType.ALGORITHM_TYPE.SECP256R1,
                StorageOption.STORAGE_OPTION.WALLET,
                new KeyGenWalletMethodType("111111".getBytes())
                );
keyManager.generateKey(pinKeyRequest);

//bio
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
`서명용 PIN키를 변경합니다. `

### Declaration

```java
void changePin(String id, byte[] oldPin, byte[] newPin) throws Exception
```

### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| id    | string    | 키아이디 |M| |
| oldPin    | byte[] | 현재 PIN |M| |
| newPin   | byte[] | 변경할 PIN |M| |


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
`월렛에 저장된 키 중 해당 아이디 리스트의 키정보를 반환합니다.`

### Declaration

```java
List<KeyInfo> getKeyInfos(List<String> ids) throws Exception
```

### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| ids    | List&lt;String&gt;    | 키아이디 리스트 |M| |


### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| List&lt;KeyInfo&gt; | 키정보 리스트 |M|[KeyInfo](#5-keyinfo) |



### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

List<KeyInfo> keyInfos = keyManager.getKeyInfos(List.of("PIN","BIO"));
```

<br>

## 7. getKeyInfos(by VerifyAuthType)

### Description
`월렛에 저장된 키 중 해당 Verify AuthType의 키정보를 반환합니다.`

### Declaration

```java
List<KeyInfo> getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE keyType) throws Exception
```

### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| keyType    | VERIFY_AUTH_TYPE    | 제출인증타입 |M|[VERIFY_AUTH_TYPE](#7-verify_auth_type) |


### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| List&lt;KeyInfo&gt; | 키정보 리스트 |M|[KeyInfo](#5-keyinfo) |




### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

//Get All keys
List<KeyInfo> KeyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.ANY);

//Get Free keys
List<KeyInfo> freeKeyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.FREE);

//Get Pin keys
List<KeyInfo> pinKeyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.PIN);

//Get Bio keys
List<KeyInfo> bioKeyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.BIO);

//Get Pin or Bio keys
List<KeyInfo> orKeyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.PIN_OR_BIO);

//Get PIN and Bio keys
List<KeyInfo> andKeyInfos = keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.PIN_AND_BIO);
```

<br>

## 8. deleteKeys

### Description
`월렛에 저장된 키 중 해당 아이디 리스트의 키를 삭제합니다. Keystore에 저장되어 있는 키도 함께 삭제합니다.`

### Declaration

```java
void deleteKeys(List<String> ids) throws Exception
```

### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| ids    | List&lt;String&gt;    | 키아이디 리스트 |M| |


### Returns

void




### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

keyManager.deleteKeys(List.of("PIN","BIO"));
```

<br>

## 9. deleteAllKeys

### Description
`월렛파일을 삭제합니다. 해당 키가 Keystore에 있는 경우 함께 삭제합니다.`

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

if (keyManager.isAnyKeysSaved()) {
  keyManager.deleteAllKeys();
}
```

<br>

## 10. sign

### Description
`월렛에 저장된 서명키를 통하여 데이터를 서명합니다.`

### Declaration

```java
byte[] sign(String id, byte[] pin, byte[] digest) throws Exception
```

### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| id    | string    | 키 아이디 |M| |
| pin    | byte[] | PIN |O| PIN키가 아닐경우 null|
| digest    | byte[] |서명원문의 해시값  |M| |


### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| byte[]  | 서명값 |M| |



### Usage
```java
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

byte[] plainData = "Test".getBytes();
byte[] digest = DigestUtils.getDigest(plainData, DigestEnum.DIGEST_ENUM.SHA_256);
  
//Not Pin key
byte[] signature = keyManager.sign("FREE", null, digest);

//Pin key
byte[] signature = keyManager.sign("PIN", pinData, digest);
```

<br>

## 11. verify

### Declaration

```java
void verify(AlgorithmType.ALGORITHM_TYPE algorithmType, byte[] publicKey, byte[] digest, byte[] signature) throws Exception
```

### Description
`서명값을 해당 공개키로 검증합니다.`

### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| algorithmType    | ALGORITHM_TYPE    | 알고리즘타입 |M|[ALGORITHM_TYPE](#1-algorithm_type)|
| publicKey    | byte[]    | 공개키 |M| |
| digest   | byte[]    | 서명원문의 해시값 |M| |
| signature   | byte[]    | 서명값 |M| |


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

`알고리즘 타입 (DataModel SDK에 정의)`

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

`월렛 접근 타입`

### Declaration

```java
public enum WALLET_METHOD_TYPE {
    NONE("NONE"),
    PIN("PIN");
}
```

## 3. KEYSTORE_ACCESS_METHOD

### Description

'Keystore 접근 타입`

### Declaration

```java
public enum KEYSTORE_ACCESS_METHOD {
    NONE("NONE"),
    BIOMETRY("BIOMETRY");
}
```

## 4. STORAGE_OPTION

### Description

`키 저장소`

### Declaration

```java
public enum STORAGE_OPTION {
    WALLET(0),
    KEYSTORE(1);
}
```

## 5. KEY_ACCESS_METHOD

### Description

`키 저장소 및 접근타입`

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

`키 접근 타입 (DataModel SDK에 정의)`

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

`제출 인증 타입 (DataModel SDK에 정의)`

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

`키생성요청`

### Declaration

```java
public class KeyGenRequest {
    String id;
    AlgorithmType.ALGORITHM_TYPE algorithmType;
    StorageOption.STORAGE_OPTION storage;
}
```

### Property

| Name          | Type            | Description                | **M/O** | **Note**               |
|---------------|-----------------|----------------------------|---------|------------------------|
| algorithmType | ALGORITHM_TYPE   | Algorithm type for Key     |    M    |[ALGORITHM_TYPE](#1-algorithm_type)|
| id            | String          | Key name                   |    M    |                        |
| storage       | STORAGE_OPTION   | Key storage                |    M    |[STORAGE_OPTION](#4-storage_option)|

## 2. WalletKeyGenRequest

### Description

`월렛 키생성요청 - KeyGenRequest를 상속`

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

| Name          | Type               | Description              | **M/O** | **Note**                    |
|---------------|--------------------|--------------------------|---------|-----------------------------|
| methodType  | KeyGenWalletMethodType | Method type for Wallet |    M    |[KeyGenWalletMethodType](#3-keygenwalletmethodtype)|

## 3. KeyGenWalletMethodType

### Description

`월렛키생성요청시 접근타입`

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

| Name          | Type                      | Description                     | **M/O** | **Note**                           |
|---------------|---------------------------|---------------------------------|---------|------------------------------------|
| walletMethodType  | WALLET_METHOD_TYPE | Access method for Wallet|    M    |[WALLET_METHOD_TYPE](#2-wallet_method_type)|
| pin        | byte[]                    | 서명용 PIN  |    M    |                                    |


## 4. SecureKeyGenRequest

### Description

`키스토어 키생성요청 - KeyGenRequest를 상속`

### Declaration

```java
// Declaration in Swift
public class SecureKeyGenRequest extends KeyGenRequest {
    AlgorithmType.ALGORITHM_TYPE algorithmType = ALGORITHM_TYPE.SECP256R1;
    String id;
    KeyStoreAccessMethod.KEYSTORE_ACCESS_METHOD accessMethod;
    StorageOption.STORAGE_OPTION storage = STORAGE_OPTION.KEYSTORE;
}
```

### Property

| Name          | Type                      | Description                     | **M/O** | **Note**                           |
|---------------|---------------------------|---------------------------------|---------|------------------------------------|
| accessMethod  | KEYSTORE_ACCESS_METHOD | Access method for Keystore|    M    |[KEYSTORE_ACCESS_METHOD](#3-keystore_access_method)|



## 5. KeyInfo

### Description

`키정보`

### Declaration

```java
// Declaration in Swift
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

| Name          | Type            | Description                                | **M/O** | **Note**                 |
|---------------|-----------------|--------------------------------------------|---------|--------------------------|
| algorithmType | ALGORITHM_TYPE   | Algorithm type for Key                     |    M    |[ALGORITHM_TYPE](#1-algorithmtype)  |
| id            | String          | Key name                                   |    M    |                          |
| accessMethod  | KEY_ACCESS_METHOD | Indicate key storage and its access method |    M    |[KEY_ACCESS_METHOD](#5-keyaccessmethod)|
| authType      | AUTH_TYPE        | Access method                              |    M    |[AUTH_TYPE](#6-authtype)       |
| publicKey     | String          | Public key                                 |    M    |Encoded by Multibase      |