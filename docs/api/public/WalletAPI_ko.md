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

Android Wallet API
==

- Subject: WalletAPI
- Writer: Sangjun Kim
- Date: 2024-08-19
- Version: v1.0.0

| Version | Date       | History                 |
| ------- | ---------- | ------------------------|
| v1.0.0  | 2024-08-19 | 초기 작성                 |


<div style="page-break-after: always;"></div>

# 목차
- [APIs](#api-목록)
    - [0. constructor](#0-constructor)
    - [1. isExistWallet](#1-isexistwallet)
    - [2. createWallet](#2-createwallet)
    - [3. deleteWallet](#3-deletewallet)
    - [4. createWalletTokenSeed](#4-createwallettokenseed)
    - [5. createNonceForWalletToken](#5-createnonceforwallettoken)
    - [6. bindUser](#6-binduser)
    - [7. unbindUser](#7-unbinduser)
    - [8. registerLock](#8-registerlock)
    - [9. authenticateLock](#9-authenticatelock)
    - [10. createHolderDIDDoc](#10-createholderdiddoc)
    - [11. createSignedDIDDoc](#11-createsigneddiddoc)
    - [12. getDIDDocument](#12-getdiddocument)
    - [13. generateKeyPair](#13-generatekeypair)
    - [14. isLock](#14-islock)
    - [15. getSignedWalletInfo](#15-getsignedwalletinfo)
    - [16. requestRegisterUser](#16-requestregisteruser)
    - [17. getSignedDIDAuth](#17-getsigneddidauth)
    - [18. requestIssueVc](#18-requestissuevc)
    - [19. requestRevokeVc](#19-requestrevokevc)
    - [20. getAllCredentials](#20-getallcredentials)
    - [21. getCredentials](#21-getcredentials)
    - [22. deleteCredentials](#22-deletecredentials)
    - [23. createEncVp](#23-createencvp)
    - [24. registerBioKey](#24-registerbiokey)
    - [25. authenticateBioKey](#25-authenticatebiokey)
    - [26. addProofsToDocument](#26-addproofstodocument)
    - [27. isSavedBioKey](#27-issavedbiokey)
    - [28. changePin](#28-changepin)
    - [29. changeLock](#29-changelock)

- [Enumerators](#enumerators)
    - [1. WALLET_TOKEN_PURPOSE](#1-wallet_token_purpose)
- [Value Object](#value-object)
    - [1. WalletTokenSeed](#1-wallettokenseed)
    - [2. WalletTokenData](#2-wallettokendata)
    - [3. Provider](#3-provider)
    - [4. SignedDIDDoc](#4-signeddiddoc)
    - [5. SignedWalletInfo](#5-signedwalletinfo)
    - [6. DIDAuth](#6-didauth)
# API 목록
## 0. constructor

### Description
 `WalletApi 생성자`

### Declaration

```java
public static WalletApi getInstance(Context context);
```

### Parameters

| Name      | Type   | Description                             | **M/O** | **Note** |
|-----------|--------|----------------------------------|---------|----------|
| context   | Context |                       | M       |          |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| WalletApi | WalletApi instance | M       |          |

### Usage

```java
WalletApi walletApi = WalletApi.getInstatnce(context)
```

<br>

## 1. isExistWallet

### Description
 `DeviceKey Wallet 존재 유무를 확인한다.`

### Declaration

```java
public boolean isExistWallet()
```

### Parameters

N/A

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Wallet의 존재 여부를 반환한다. | M       |          |

### Usage

```java
boolean exists = walletApi.isExistWallet();
```

<br>

## 2. createWallet

### Description
`DeviceKey Wallet을 생성한다.`

### Declaration

```java
public boolean createWallet() throws Exception
```

### Parameters

| Name      | Type   | Description                      | **M/O** | **Note** |
|-----------|--------|----------------------------------|---------|----------|
| walletUrl    | String | Wallet URL                          | M       |          |
| tasUrl | String | TAS URL                       | M       |          |


### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Wallet 생성 성공 여부를 반환한다. | M       |          |

### Usage

```java
boolean success = walletApi.createWallet();
```

<br>

## 3. deleteWallet

### Description
`소유자의 지갑 데이터를 삭제합니다. deleteAll 플래그에 따라 모든 지갑 관련 데이터(CA 패키지 정보, 사용자 데이터 및 토큰 포함)를 삭제하거나 부분 삭제를 수행합니다.
삭제 프로세스는 데이터베이스 레코드에 대해 비동기적으로 실행되며, 핵심 지갑 삭제 로직도 호출합니다.`

### Declaration

```java
public void deleteWallet(booelan deleteAll) throws WalletCoreException
```

### Parameters

| Name      | Type    | Description | **M/O** | **Note**                                    |
| --------- | ------- | ----------- | ------- | --------------------------------------------|
| deleteAll | boolean |true로 설정 시 deviceKey와 holderKey 모두 삭제, false로 설정 시 holderKey만 삭제.| M |   |

### Returns

N/A

### Usage

```java
walletApi.deleteWallet(true);
```

<br>

## 4. createWalletTokenSeed

### Description
`월렛 토큰 시드를 생성한다.`

### Declaration

```java
public WalletTokenSeed createWalletTokenSeed(WalletTokenPurpose.WALLET_TOKEN_PURPOSE purpose, String pkgName, String userId) throws Exception
```

### Parameters

| Name      | Type   | Description                             | **M/O** | **Note** |
|-----------|--------|----------------------------------|---------|----------|
| purpose   | WALLET_TOKEN_PURPOSE |token 사용 목적                       | M       |[WALLET_TOKEN_PURPOSE](#1-wallet_token_purpose)         |
| pkgName   | String | 인가앱 Package Name                       | M       |          |
| userId    | String | 사용자 ID                        | M       |          |

### Returns

| Type            | Description                  | **M/O** | **Note** |
|-----------------|-----------------------|---------|----------|
| WalletTokenSeed | 월렛 토큰 시드 객체   | M       |[WalletTokenSeed](#1-wallettokenseed)          |

### Usage

```java
WalletTokenSeed tokenSeed = walletApi.createWalletTokenSeed(purpose, "org.opendid.did.ca", "user_id");
```

<br>

## 5. createNonceForWalletToken

### Description
`월렛 토큰 생성을 위한 nonce를 생성한다.`

### Declaration

```java
public String createNonceForWalletToken(WalletTokenData walletTokenData) throws Exception
```

### Parameters

| Name           | Type           | Description                  | **M/O** | **Note** |
|----------------|----------------|-----------------------|---------|----------|
| walletTokenData | WalletTokenData | 월렛 토큰 데이터      | M       |[WalletTokenData](#2-wallettokendata)          |

### Returns

| Type    | Description              | **M/O** | **Note** |
|---------|-------------------|---------|----------|
| String  | wallet token 생성을 위한 nonce | M       |          |

### Usage

```java
String nonce = walletApi.createNonceForWalletToken(walletTokenData);
```

<br>

## 6. bindUser

### Description
`Wallet에 사용자 개인화를 수행한다.`

### Declaration

```java
public boolean bindUser(String hWalletToken) throws Exception
```

### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | 월렛토큰                  | M       |          |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | 개인화 성공 여부를 반환한다. | M       |          |

### Usage

```java
boolean success = walletApi.bindUser("hWalletToken");
```

<br>

## 7. unbindUser

### Description
`사용자 비개인화를 수행한다.`

### Declaration

```java
public boolean unbindUser(String hWalletToken) throws Exception
```

### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | 월렛토큰                  | M       |          |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | 비개인화 성공 여부를 반환한다. | M       |          |

### Usage

```java
boolean success = walletApi.unbindUser("hWalletToken");
```

<br>

## 8. registerLock

### Description
`Wallet의 잠금 상태를 설정한다.`

### Declaration

```java
public boolean registerLock(String hWalletToken, String passCode, boolean isLock) throws Exception
```

### Parameters

| Name         | Type   | Description                        | **M/O** | **Note** |
|--------------|--------|-----------------------------|---------|----------|
| hWalletToken | String | 월렛토큰                   | M       |          |
| passCode     | String | Unlock PIN               | M       |          |
| isLock       | boolean | 잠금 활성화 여부            | M       |          |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | 잠금 설정 성공 여부를 반환한다. | M       |          |

### Usage

```java
boolean success = walletApi.registerLock("hWalletToken", "123456", true);
```

<br>

## 9. authenticateLock

### Description
`Wallet의 Unlock을 위한 인증을 수행한다.`

### Declaration

```java
public void authenticateLock(String passCode) throws Exception
```

### Parameters

| Name         | Type   | Description                        | **M/O** | **Note** |
|--------------|--------|-----------------------------|---------|----------|
| passCode     | String |Unlock PIN               | M       | registerLock 시 설정한 PIN          | 

### Returns

Void

### Usage

```java
walletApi.authenticateLock("hWalletToken", "123456");
```

<br>

## 10. createHolderDIDDoc

### Description
`사용자 DID Document를 생성한다.`

### Declaration

```java
public DIDDocument createHolderDIDDoc(String hWalletToken) throws Exception
```

### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | 월렛토큰                  | M       |          |

### Returns

| Type         | Description                  | **M/O** | **Note** |
|--------------|-----------------------|---------|----------|
| DIDDocument  | 사용자 DID Document   | M       |          |

### Usage

```java
DIDDocument didDoc = walletApi.createHolderDIDDoc("hWalletToken");
```

<br>

## 11. createSignedDIDDoc

### Description
`서명된 사용자 DID Document 객체를 생성한다.`

### Declaration

```java
public SignedDidDoc createSignedDIDDoc(DIDDocument ownerDIDDoc) throws Exception
```

### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| ownerDIDDoc  | DIDDocument | 소유자의 DID Document 객체                 | M       |          |

### Returns

| Type            | Description                  | **M/O** | **Note** |
|-----------------|-----------------------|---------|----------|
| SignedDidDoc | 서명된 DID Document 객체   | M       |[SignedDIDDoc](#4-signeddiddoc)          |

### Usage

```java
SignedDidDoc signedDidDoc = walletApi.createSignedDIDDoc(ownerDIDDoc);
```

<br>

## 12. getDIDDocument

### Description
`DID Document를 조회한다.`

### Declaration

```java
public DIDDocument getDIDDocument(int type) throws Exception
```

### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| type  | int | 1 : deviceKey DID Document, 2: holder DID document                  | M       |          |

### Returns

| Type         | Description                  | **M/O** | **Note** |
|--------------|-----------------------|---------|----------|
| DIDDocument  | DID Document       | M       |          |

### Usage

```java
DIDDocument didDoc = walletApi.getDIDDocument("hWalletToken", 1);
```

<br>

## 13. generateKeyPair

### Description
`서명을 위한 PIN 키 쌍을 생성하여 Wallet에 저장한다.`

### Declaration

```java
public void generateKeyPair(String hWalletToken, String passcode) throws Exception
```

### Parameters

| Name         | Type   | Description                        | **M/O** | **Note** |
|--------------|--------|-----------------------------|---------|----------|
| hWalletToken | String |월렛토큰                   | M       |          |
| passCode     | String |서명용 PIN               | M       | PIN 서명용 키 생성 시        | 

### Returns

Void

### Usage

```java
walletApi.generateKeyPair("hWalletToken", "123456");
```

<br>

## 14. isLock

### Description
`Wallet의 잠금 타입을 조회한다.`

### Declaration

```java
public boolean isLock() throws Exception
```

### Parameters
 Void

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Wallet 잠금 타입을 반환한다. | M       |          |

### Usage

```java
boolean isLocked = walletApi.isLock();
```

<br>

## 15. getSignedWalletInfo

### Description
`서명된 Wallet 정보를 조회한다.`

### Declaration

```java
public SignedWalletInfo getSignedWalletInfo() throws Exception
```

### Parameters

Void

### Returns

| Type             | Description                    | **M/O** | **Note** |
|------------------|-------------------------|---------|----------|
| SignedWalletInfo | 서명된 WalletInfo 객체       | M       |[SignedWalletInfo](#5-signedwalletinfo)          |

### Usage

```java
SignedWalletInfo signedInfo = walletApi.getSignedWalletInfo();
```

<br>

## 16. requestRegisterUser

### Description
`사용자 등록을 요청한다.`

### Declaration

```java
public CompletableFuture<String> requestRegisterUser(String hWalletToken, String tasUrl, String txId, String serverToken, SignedDidDoc signedDIDDoc) throws Exception
```

### Parameters

| Name         | Type           | Description                        | **M/O** | **Note** |
|--------------|----------------|-----------------------------|---------|----------|
| tasUrl | String         | TAS URL                   | M       |          |
| hWalletToken | String         | 월렛토큰                   | M       |          |
| txId     | String       | 거래코드               | M       |          |
| serverToken     | String       | 서버토큰                | M       |          |
| signedDIDDoc|SignedDidDoc | 서명된 DID Document 객체   | M       |[SignedDIDDoc](#4-signeddiddoc)          |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| String | 사용자 등록 프로토콜 수행 결과를 반환핟다. | M       |          |

### Usage

```java
String _M132_RequestRegisterUser = walletApi.requestRegisterUser("hWalletToken", "txId", "hServerToken", signedDIDDoc).get();
```

<br>

## 17. getSignedDIDAuth

### Description
`DIDAuth 서명을 수행한다.`

### Declaration

```java
public DIDAuth getSignedDIDAuth(String authNonce, String passcode) throws Exception
```

### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| authNonce  | String | profile의 auth nonce                  | M       |          |
|passcode|String | 서명용 PIN   | M       |          |

### Returns

| Type            | Description                  | **M/O** | **Note** |
|-----------------|-----------------------|---------|----------|
| DIDAuth   | 서명된 DIDAuth 객체   | M       |[DIDAuth](#6-didauth)          |

### Usage

```java
DIDAuth signedDIDAuth = walletApi.getSignedDIDAuth("authNonce", "123456");
```

<br>

## 18. requestIssueVc

### Description
`VC 발급을 요청한다.`

### Declaration

```java
public CompletableFuture<String> requestIssueVc(String hWalletToken, String txId, String serverToken, String refId, String authNonce, IssueProfile profile, DIDAuth signedDIDAuth) throws Exception
```

### Parameters

| Name        | Type           | Description                        | **M/O** | **Note** |
|-------------|----------------|-----------------------------|---------|----------|
| tasUrl | String         | TAS URL                  | M       |          |
| hWalletToken | String         | 월렛토큰                   | M       |          |
| txId     | String       | 거래코드               | M       |          |
| serverToken     | String       | 서버토큰                | M       |          |
| refId     | String       | 참조번호                | M       |          |
| profile|IssueProfile | Issue Profile   | M       |[데이터모델 참조]          |
| signedDIDAuth|DIDAuth | 서명된 DID Document 객체   | M       |[DIDAuth](#6-didauth)         |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| String | VC ID | M       |성공 시 발급된 VC의 ID를 반환한다          |

### Usage

```java
String vcId = walletApi.requestIssueVc("hWalletToken", "txId", "hServerToken", "refId", profile, signedDIDAuth).get();
```

<br>

## 19. requestRevokeVc

### Description
`VC 폐기를 요청한다.`

### Declaration

```java
public CompletableFuture<String> requestRevokeVc(String hWalletToken, String serverToken, String txId, String vcId, String issuerNonce, String passcode) throws Exception

```

### Parameters

| Name        | Type           | Description                        | **M/O** | **Note** |
|-------------|----------------|-----------------------------|---------|----------|
| hWalletToken | String         | 월렛토큰                   | M       |          |
| tasUrl | String         | TAS URL                   | M       |          |
| txId     | String       | 거래코드               | M       |          |
| serverToken     | String       | 서버토큰                | M       |          |
| vcId     | String       | VC ID                | M       |          |
| issuerNonce|String | 발급처 nonce   | M       |[데이터모델 참조]          |
| passcode|String | 서명용 PIN   | M       |[DIDAuth](#6-didauth)         |
| authType|VERIFY_AUTH_TYPE | 제출 인증수단 타입   | M       |       |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| String | txId | M       |성공 시 거래코드를 반환한다          |

### Usage

```java
String result = walletApi.requestRevokeVc("hWalletToken", "hServerToken", "txId", "vcId", "issuerNonce", "123456").get();
```

<br>

## 20. getAllCredentials

### Description
`Wallet에 저장된 모든 VC를 조회한다.`

### Declaration

```java
public List<VerifiableCredential> getAllCredentials(String hWalletToken) throws Exception
```

### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | 월렛토큰                  | M       |          |

### Returns

| Type            | Description                | **M/O** | **Note** |
|-----------------|---------------------|---------|----------|
| List&lt;VerifiableCredential&gt; | VC List 객체  | M       |          |

### Usage

```java
List<VerifiableCredential> vcList = walletApi.getAllCredentials("hWalletToken");
```

<br>

## 21. getCredentials

### Description
`특정 VC를 조회한다.`

### Declaration

```java
public List<VerifiableCredential> getCredentials(String hWalletToken, List<String> identifiers) throws Exception
```

### Parameters

| Name           | Type   | Description                       | **M/O** | **Note** |
|----------------|--------|----------------------------|---------|----------|
| hWalletToken   | String | 월렛토큰                  | M       |          |
| identifiers   | List&lt;String&gt;   | 조회 대상 VC ID List               | M       |          |

### Returns

| Type        | Description                | **M/O** | **Note** |
|-------------|---------------------|---------|----------|
| List&lt;VerifiableCredential&gt;  | VC List 객체    | M       |          |

### Usage

```java
List<VerifiableCredential> vcList = walletApi.getCredentials("hWalletToken", List.of("vcId"));
```

<br>

## 22. deleteCredentials

### Description
`특정 VC를 삭제한다.`

### Declaration

```java
public void deleteCredentials(String hWalletToken, String vcId) throws Exception
```

### Parameters

| Name           | Type   | Description                       | **M/O** | **Note** |
|----------------|--------|----------------------------|---------|----------|
| hWalletToken   | String | 월렛토큰                  | M       |          |
| vcId   | String   | 삭제 대상 VC ID             | M       |          |

### Returns
Void

### Usage

```java
walletApi.deleteCredentials("hWalletToken", "vcId");
```

<br>

## 23. createEncVp

### Description
`암호화된 VP를 생성한다.`

### Declaration

```java
public ReturnEncVP createEncVp(String hWalletToken, String vcId, List<String> claimCode, ReqE2e reqE2e, String passcode, String nonce, VerifyAuthType.VERIFY_AUTH_TYPE authType) throws Exception

```

### Parameters

| Name        | Type           | Description                        | **M/O** | **Note** |
|-------------|----------------|-----------------------------|---------|----------|
| hWalletToken | String         | 월렛토큰                   | M       |          |
| vcId     | String       | VC ID               | M       |          |
| claimCode     | List&lt;String&gt;       | 제출할 클레임 코드                | M       |          |
| reqE2e     | ReqE2e       | E2E 암복호화 정보                | M       |데이터모델 참조        |
|passcode|String | 서명용 PIN   | M       |          |
| nonce|String | nonce   | M       |       |
| authType|VERIFY_AUTH_TYPE | 제출 인증수단 타입   | M       |       |

### Returns

| Type   | Description              | **M/O** | **Note** |
|--------|-------------------|---------|----------|
| ReturnEncVP  | 암호화 VP 객체| M       |acce2e 객체, encVp 멀티베이스 인코딩 값      |

### Usage

```java
EncVP encVp = walletApi.createEncVp("hWalletToken", "vcId", List.of("claim_code"), reqE2e, "123456", "nonce", VERIFY_AUTH_TYPE.PIN);
```

<br>

## 24. registerBioKey

### Description
`서명용 생체 인증 키를 등록한다.`

### Declaration

```java
public void registerBioKey(Context context)
```

### Parameters

| Name         | Type     | Description                        | **M/O** | **Note** |
|--------------|----------|-----------------------------|---------|----------|
| context       | Context   |        | M       |          |

### Returns
N/A

### Usage

```java
walletApi.registerBioKey("hWalletToken", context);
```

<br>

## 25. authenticateBioKey

### Description
`서명을 위한 생체 인증 키를 사용하기 위하여 인증을 수행한다.`

### Declaration

```java
public void authenticateBioKey(Fragment fragment, Context context) throws Exception
```

### Parameters

| Name         | Type     | Description                        | **M/O** | **Note** |
|--------------|----------|-----------------------------|---------|----------|
| hWalletToken | String   | 월렛토큰                   | M       |          |
| fragment       | Fragment   |       | M       |          |
| context       | Context   |        | M       |          |

### Returns

N/A

### Usage

```java
walletApi.authenticateBioKey(fragment.this, context);
```

<br>

## 26. addProofsToDocument

### Description
`서명이 필요한 객체에 Proof객체를 추가한다.`

### Declaration

```java
public ProofContainer addProofsToDocument(ProofContainer document, List<String> keyIds, String did, int type, String passcode, boolean isDIDAuth) throws Exception
```

### Parameters

| Name         | Type         | Description                        | **M/O** | **Note** |
|--------------|--------------|-----------------------------|---------|----------|
| document     | ProofContainer     | Proof객체를 상속받은 문서 객체                        | M       |          |
| keyIds       | List&lt;String&gt;       | 서명할 Key ID List                 | M       |          |
| did     | String     | 서명 대상 DID                        | M       |          |
| type       | int       | 1 : deviceKey DID Document, 2: holder DID document                 | M       |          |
| passcode     | String     | 서명용 PIN                        | O       | PIN 키 서명 시         |
| isDIDAuth       | boolean       | DIDAuth객체일경우 true / 이외에는 false               | M       |          |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| ProofContainer | Proof객체를 포함한 원객체 | M       |          |

### Usage

```java
DIDDocument signedDIDDoc = (DIDDocument) walletApi.addProofsToDocument(didDocument, List.of("PIN"), "DID", 2, "123456", false);
```

<br>


## 27. isSavedBioKey

### Description
`저장된 생체 인증 키가 있는지 확인한다.`

### Declaration

```java
public boolean isSavedBioKey() throws Exception
```

### Parameters

Void

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | 생체 인증 키 존재 여부를 반환한다. | M       |          |

### Usage

```java
boolean hasBioKey = walletApi.isSavedBioKey();
```

<br>

## 28. changePin

### Description
`서명용 PIN 변경`

### Declaration

```java
public void changePin(String keyId, String oldPin, String newPin) throws Exception
```

### Parameters

| Name   | Type   | Description   | **M/O** | **Note** |
| ------ | ------ | ------------- | ------- | -------- |
| keyId     | String | 서명용 키아이디 | M       |          |
| oldPIN | String | 현재 PIN      | M       |          |
| newPIN | String | 변경할 PIN    | M       |          |

### Returns


### Usage

```java
String oldPin = "123456";
String newPin = "654321";
walletApi.changePin(Constants.KEY_ID_PIN, oldPin, newPin);
```

<br>

## 29. changeLock

### Description
`Unlock PIN 변경`

### Declaration

```java
public void changeLock(String oldPin, String newPin) throws Exception
```

### Parameters

| Name   | Type   | Description   | **M/O** | **Note** |
| ------ | ------ | ------------- | ------- | -------- |
| oldPIN | String | 현재 PIN      | M       |          |
| newPIN | String | 변경할 PIN    | M       |          |

### Returns


### Usage

```java
String oldPin = "123456";
String newPin = "654321";
walletApi.changeLock(oldPin, newPin);
```

<br>


## 30. updateHolderDIDDoc

### Description
`제공된 지갑 토큰을 사용하여 사용자의 기존 DID 문서를 업데이트합니다.`

### Declaration

```java
public DIDDocument updateHolderDIDDoc(String hWalletToken) throws WalletException, UtilityException, WalletCoreException
```

### Parameters

| Name         | Type   | Description | **M/O** | **Note** |
| ------------ | ------ | ----------- | ------- | -------- |
| hWalletToken | String | 월렛 토큰     | M       |          |

### Returns


### Usage

```java
walletApi.updateHolderDIDDoc(hWalletToken);
```

<br>

## 31. saveDocument

### Description
`사용자의 DID 문서를 영구 저장소에 저장합니다.`

### Declaration

```java
public void saveDocument() throws WalletException, WalletCoreException, UtilityException

```

### Parameters
N/A

### Returns
N/A

### Usage

```java
walletApi.saveDocument()
```

<br>

## 32. deleteKey

### Description
`제공된 지갑 토큰에 필요한 권한이 있는지 확인한 후, 사용자의 DID(탈중앙화 식별자) 문서와 연결된 지정된 키를 삭제합니다.`

### Declaration

```java
public void deleteKey(String hWalletToken, List<String> keyIds) throws WalletCoreException, UtilityException, WalletException
```

### Parameters

| Name         | Type         | Description | **M/O** | **Note** |
| ------------ | ------------ | ----------- | ------- | -------- |
| hWalletToken | String       | 월렛토큰    | M       |          |
| keyIds       | List<String> | 키 IDs      | M       |          |

### Returns
N/A

### Usage

```java
walletApi.deleteKey(ProtocolData.getInstance(context).gethWalletToken(), List.of("bio"));
```

<br>

## 33. isAnyCredentialsSaved

### Description
`사용자의 지갑에 증명서가 저장되어 있는지 확인합니다.`

### Declaration

```java
public void isAnyCredentialsSaved() throws WalletException
```

### Parameters

N/A

### Returns
boolean

### Usage

```java

if (!walletApi.isAnyCredentialsSaved()) {
    ...
}
```

<br>

# Enumerators
## 1. WALLET_TOKEN_PURPOSE

### Description

`WalletToken purpose`

### Declaration

```java
public enum WALLET_TOKEN_PURPOSE {
    PERSONALIZE(1),
    DEPERSONALIZE(2),
    PERSONALIZE_AND_CONFIGLOCK(3),
    CONFIGLOCK(4),
    CREATE_DID(5),
    UPDATE_DID(6),
    ISSUE_VC(7),
    REMOVE_VC(8),
    PRESENT_VP(9),
    LIST_VC(10),
    DETAIL_VC(11),
    CREATE_DID_AND_ISSUE_VC(12),
    LIST_VC_AND_PRESENT_VP(13);
}
```
<br>

# Value Object

## 1. WalletTokenSeed

### Description

`인가앱이 월렛에 월렛토큰 생성 요청 시 전달하는 데이터`

### Declaration

```java
public class WalletTokenSeed {
    WalletTokenPurpose.WALLET_TOKEN_PURPOSE purpose;
    String pkgName;
    String nonce;
    String validUntil;
    String userId;
}
```

### Property

| Name          | Type            | Description                | **M/O** | **Note**               |
|---------------|-----------------|----------------------------|---------|------------------------|
| purpose | WALLET_TOKEN_PURPOSE   | token 사용 목적     |    M    |[WALLET_TOKEN_PURPOSE](#1-wallet_token_purpose)|
| pkgName   | String | 인가앱 Package Name                       | M       |          |
| nonce    | String | wallet nonce                        | M       |          |
| validUntil    | String | token 만료일시                        | M       |          |
| userId    | String | 사용자 ID                        | M       |          |
<br>

## 2. WalletTokenData

### Description

`인가앱이 월렛에 월렛토큰 생성 요청 시 월렛이 생성하여 인가앱으로 전달하는 데이터`

### Declaration

```java
public class WalletTokenData {
    WalletTokenSeed seed;
    String sha256_pii;
    Provider provider;
    String nonce;
    Proof proof;
}
```

### Property

| Name          | Type            | Description                | **M/O** | **Note**               |
|---------------|-----------------|----------------------------|---------|------------------------|
| seed | WalletTokenSeed   | WalletToken Seed     |    M    |[WalletTokenSeed](#1-wallettokenseed)|
| sha256_pii   | String | 사용자 PII의 해시값                 | M       |          |
| provider    | Provider | wallet 사업자 정보                        | M       | [Provider](#3-provider)         |
| nonce    | String | provider nonce                      | M       |          |
| proof    | Proof | provider proof                        | M       |          |
<br>

## 3. Provider

### Description

`Provider 정보`

### Declaration

```java
public class Provider {
    String did;
    String certVcRef;
}
```

### Property

| Name          | Type            | Description                | **M/O** | **Note**               |
|---------------|-----------------|----------------------------|---------|------------------------|
| did    | String | provider DID                      | M       |          |
| certVcRef    | String | provider 가입증명서 VC URL                        | M       |          |
<br>

## 4. SignedDIDDoc

### Description

`월렛이 holder의 DID Document를 서명하여 controller에게 등록을 요청하기 위한 문서의 데이터`

### Declaration

```java
public class SignedDidDoc {
    String ownerDidDoc;
    Wallet wallet;
    String nonce;
    Proof proof;
}
```

### Property

| Name          | Type            | Description                | **M/O** | **Note**               |
|---------------|-----------------|----------------------------|---------|------------------------|
| ownerDidDoc    | String | ownerDidDoc의 multibase 인코딩 값                      | M       |          |
| wallet    | Wallet | wallet의 id와 wallet의 DID로 구성된 객체                        | M       |          |
| nonce    | String | wallet nonce                        | M       |          |
| proof    | Proof | wallet proof                        | M       |          |
<br>

## 5. SignedWalletInfo

### Description

`서명 된 walletinfo 데이터`

### Declaration

```java
public class SignedWalletInfo {
    Wallet wallet;
    String nonce;
    Proof proof;
}
```

### Property

| Name          | Type            | Description                | **M/O** | **Note**               |
|---------------|-----------------|----------------------------|---------|------------------------|
| wallet    | Wallet | wallet의 id와 wallet의 DID로 구성된 객체                        | M       |          |
| nonce    | String | wallet nonce                        | M       |          |
| proof    | Proof | wallet proof                        | M       |          |
<br>

## 6. DIDAuth

### Description

`DID Auth 데이터`

### Declaration

```java
public class DIDAuth {
    String did;
    String authNonce;
    Proof proof;
}
```

### Property

| Name          | Type            | Description                | **M/O** | **Note**               |
|---------------|-----------------|----------------------------|---------|------------------------|
| did    | String | 인증 대상자의 DID                        | M       |          |
| authNonce    | String | DID Auth 용 nonce                        | M       |          |
| proof    | Proof | authentication proof                        | M       |          |
<br>