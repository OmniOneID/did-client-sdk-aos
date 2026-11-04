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
- Writer: 김은정
- Date: 2026-10-28
- Version: v3.0.0

| Version | Date       | History                                      |
| ------- | ---------- | -------------------------------------------- |
| v3.0.0  | 2026-10-30 | OID4VC · OID4VP · Proximity API 및 createVp 추가, 시그니처 소스 정합 |
| v2.0.1  | 2025-10-14 | DID 관련 함수 및 authenticatePin 추가 |
| v2.0.0  | 2024-04-30 | ZKP 추가                                     |
| v1.0.0  | 2024-08-19 | 초기 작성                                    |


<div style="page-break-after: always;"></div>

# 목차
- [APIs](#api-목록)
    - [1. constructor](#1-constructor)
    - [2. Wallet](#2-wallet)
        - [2.1. isExistWallet](#21-isexistwallet)
        - [2.2. createWallet](#22-createwallet)
        - [2.3. deleteWallet](#23-deletewallet)
        - [2.4. createWalletTokenSeed](#24-createwallettokenseed)
        - [2.5. createNonceForWalletToken](#25-createnonceforwallettoken)
        - [2.6. createLocalWalletToken](#26-createlocalwallettoken)
        - [2.7. bindUser](#27-binduser)
        - [2.8. unbindUser](#28-unbinduser)
        - [2.9. requestRegisterUser](#29-requestregisteruser)
        - [2.10. getSignedWalletInfo](#210-getsignedwalletinfo)
    - [3. DIDKey](#3-didkey)
        - [3.1. createHolderDIDDoc](#31-createholderdiddoc)
        - [3.2. createSignedDIDDoc](#32-createsigneddiddoc)
        - [3.3. getDIDDocument](#33-getdiddocument)
        - [3.4. generateKeyPair](#34-generatekeypair)
        - [3.5. getSignedDIDAuth](#35-getsigneddidauth)
        - [3.6. updateHolderDIDDoc](#36-updateholderdiddoc)
        - [3.7. saveDocument](#37-savedocument)
        - [3.8. deleteKey](#38-deletekey)
        - [3.9. requestUpdateUser](#39-requestupdateuser)
        - [3.10. requestRestoreUser](#310-requestrestoreuser)
    - [4. Credential](#4-credential)
        - [4.1. requestIssueVc](#41-requestissuevc)
        - [4.2. requestRevokeVc](#42-requestrevokevc)
        - [4.3. getAllCredentials](#43-getallcredentials)
        - [4.4. getCredentials](#44-getcredentials)
        - [4.5. deleteCredentials](#45-deletecredentials)
        - [4.6. createEncVp](#46-createencvp)
        - [4.7. addProofsToDocument](#47-addproofstodocument)
        - [4.8. isAnyCredentialsSaved](#48-isanycredentialssaved)
        - [4.9. createVp](#49-createvp)
    - [5. ZKP](#5-zkp)
        - [5.1. createZkpReferent](#51-createzkpreferent)
        - [5.2. createEncZkpProof](#52-createenczkpproof)
        - [5.3. searchZkpCredentials](#53-searchzkpcredentials)
        - [5.4. getAllZkpCredentials](#54-getallzkpcredentials)
        - [5.5. isAnyZkpCredentialsSaved](#55-isanyzkpcredentialssaved)
        - [5.6. getZkpCredentials](#56-getzkpcredentials)
    - [6. SecurityAuth](#6-securityauth)
        - [6.1. registerLock](#61-registerlock)
        - [6.2. authenticateLock](#62-authenticatelock)
        - [6.3. isLock](#63-islock)
        - [6.4. registerBioKey](#64-registerbiokey)
        - [6.5. authenticateBioKey](#65-authenticatebiokey)
        - [6.6. changePin](#66-changepin)
        - [6.7. changeLock](#67-changelock)
        - [6.8. authenticatePin](#68-authenticatepin)
    - [7. OID4VC](#7-oid4vc)
        - [7.1. requestIssueOID4VC](#71-requestissueoid4vc)
        - [7.2. requestRevokeOID4VC](#72-requestrevokeoid4vc)
        - [7.3. getAllOID4VCs](#73-getalloid4vcs)
        - [7.4. getOID4VCs](#74-getoid4vcs)
        - [7.5. deleteOID4VCs](#75-deleteoid4vcs)
        - [7.6. isAnyOID4VCSaved](#76-isanyoid4vcsaved)
    - [8. OID4VP](#8-oid4vp)
        - [8.1. matchCredentials](#81-matchcredentials)
        - [8.2. createVpToken](#82-createvptoken)
    - [9. Proximity](#9-proximity)
        - [9.1. matchMdocRequest](#91-matchmdocrequest)
        - [9.2. createDeviceResponse](#92-createdeviceresponse)
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
## 1. constructor

#### Description
 `WalletApi 생성자`

#### Declaration

```java
public static WalletApi getInstance(Context context);
```

#### Parameters

| Name      | Type   | Description                             | **M/O** | **Note** |
|-----------|--------|----------------------------------|---------|----------|
| context   | Context |                       | M       |          |

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| WalletApi | WalletApi instance | M       |          |

#### Usage

```java
WalletApi walletApi = WalletApi.getInstatnce(context)
```

<br>

## 2. Wallet

### 2.1. isExistWallet

#### Description
 `DeviceKey Wallet 존재 유무를 확인한다.`

#### Declaration

```java
public boolean isExistWallet()
```

#### Parameters

N/A

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Wallet의 존재 여부를 반환한다. | M       |          |

#### Usage

```java
boolean exists = walletApi.isExistWallet();
```

<br>

### 2.2. createWallet

#### Description
`DeviceKey Wallet을 생성한다.`

#### Declaration

```java
public boolean createWallet(String walletUrl, String tasUrl) throws Exception
```

#### Parameters

| Name      | Type   | Description                      | **M/O** | **Note** |
|-----------|--------|----------------------------------|---------|----------|
| walletUrl    | String | Wallet URL                          | M       |          |
| tasUrl | String | TAS URL                       | M       |          |


#### Returns

| Type    | Description                       | **M/O** | **Note** |
| ------- | --------------------------------- | ------- | -------- |
| boolean | Wallet 생성 성공 여부를 반환한다. | M       |          |

#### Usage

```java
boolean success = walletApi.createWallet(walletUrl, tasUrl);
```

<br>

### 2.3. deleteWallet

#### Description
`DeviceKey Wallet을 삭제한다.`

#### Declaration

```java
public void deleteWallet(boolean deleteAll) throws Exception
```

#### Parameters

| Name      | Type    | Description    | **M/O** | **Note** |
| --------- | ------- | -------------- | ------- | -------- |
| deleteAll | boolean | 월렛 삭제 범위 | M       |          |


#### Returns

N/A

#### Usage

```java
walletApi.deleteWallet(deleteAll);
```

<br>

### 2.4. createWalletTokenSeed

#### Description
`월렛 토큰 시드를 생성한다.`

#### Declaration

```java
public WalletTokenSeed createWalletTokenSeed(WalletTokenPurpose.WALLET_TOKEN_PURPOSE purpose, String pkgName, String userId) throws Exception
```

#### Parameters

| Name      | Type   | Description                             | **M/O** | **Note** |
|-----------|--------|----------------------------------|---------|----------|
| purpose   | WALLET_TOKEN_PURPOSE |token 사용 목적                       | M       |[WALLET_TOKEN_PURPOSE](#1-wallet_token_purpose)         |
| pkgName   | String | 인가앱 Package Name                       | M       |          |
| userId    | String | 사용자 ID                        | M       |          |

#### Returns

| Type            | Description                  | **M/O** | **Note** |
|-----------------|-----------------------|---------|----------|
| WalletTokenSeed | 월렛 토큰 시드 객체   | M       |[WalletTokenSeed](#1-wallettokenseed)          |

#### Usage

```java
WalletTokenSeed tokenSeed = walletApi.createWalletTokenSeed(purpose, "org.opendid.did.ca", "user_id");
```

<br>

### 2.5. createNonceForWalletToken

#### Description
`월렛 토큰 생성을 위한 nonce를 생성한다.`

#### Declaration

```java
public String createNonceForWalletToken(String apiGateWayUrl, WalletTokenData walletTokenData) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| apiGateWayUrl | String | API Gateway URL | M |  |
| walletTokenData | WalletTokenData | 월렛 토큰 데이터 | M | [WalletTokenData](#2-wallettokendata) |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| String | 월렛 토큰 생성용 nonce | M |  |

#### Usage

```java
String nonce = walletApi.createNonceForWalletToken(apiGateWayUrl, walletTokenData);
```

<br>

### 2.6. createLocalWalletToken

#### Description
`CAS 와 통신하지 않고 월렛 토큰을 로컬에서 발급한다.`

월렛이 온라인 개인화(`bindUser`)를 한 번 거친 뒤부터, 조회·제출 목적(`LIST_VC`, `DETAIL_VC`,
`PRESENT_VP`, `LIST_VC_AND_PRESENT_VP`)에 한해 사용할 수 있다. 동기 함수이며 네트워크 통신이
없다. `createNonceForWalletToken` 과 달리 nonce 가 아니라 hWalletToken 자체를 반환한다. 저장된
토큰은 교체되므로, 어느 경로로든 이전에 받은 토큰은 더 이상 검증되지 않는다.

#### Declaration

```java
public String createLocalWalletToken(WalletTokenPurpose.WALLET_TOKEN_PURPOSE purpose, String pkgName) throws Exception
```

#### Parameters

| Name      | Type   | Description                             | **M/O** | **Note** |
|-----------|--------|----------------------------------|---------|----------|
| purpose   | WALLET_TOKEN_PURPOSE | token 사용 목적. 위 4종 중 하나      | M       |[WALLET_TOKEN_PURPOSE](#1-wallet_token_purpose)         |
| pkgName   | String | 인가앱 Package Name                       | M       |          |

#### Returns

| Type    | Description              | **M/O** | **Note** |
|---------|-------------------|---------|----------|
| String  | 해당 purpose 를 받는 API 에 전달할 hWalletToken | M       |          |

#### Throws

- `MSDKWLT05002` (`ERR_CODE_WALLET_VERIFY_PARAMETER_FAIL`): `pkgName` 이 비어 있거나 `purpose` 가 4종 밖
- `MSDKWLT05046` (`ERR_CODE_WALLET_NOT_PERSONALIZED`): 월렛이 개인화된 적이 없음

#### Usage

```java
String hWalletToken = walletApi.createLocalWalletToken(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC, "org.opendid.did.ca");
List<VerifiableCredential> credentials = walletApi.getAllCredentials(hWalletToken);
```

<br>

### 2.7. bindUser

#### Description
`Wallet에 사용자 개인화를 수행한다.`

#### Declaration

```java
public boolean bindUser(String hWalletToken) throws Exception
```

#### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | 월렛토큰                  | M       |          |

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | 개인화 성공 여부를 반환한다. | M       |          |

#### Usage

```java
boolean success = walletApi.bindUser("hWalletToken");
```

<br>

### 2.8. unbindUser

#### Description
`사용자 비개인화를 수행한다.`

#### Declaration

```java
public boolean unbindUser(String hWalletToken) throws Exception
```

#### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | 월렛토큰                  | M       |          |

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | 비개인화 성공 여부를 반환한다. | M       |          |

#### Usage

```java
boolean success = walletApi.unbindUser("hWalletToken");
```

<br>


### 2.9. requestRegisterUser

#### Description
`사용자 등록을 요청한다.`

#### Declaration

```java
public CompletableFuture<String> requestRegisterUser(String hWalletToken, String tasUrl, String txId, String serverToken, SignedDidDoc signedDIDDoc) throws Exception
```

#### Parameters

| Name         | Type           | Description                        | **M/O** | **Note** |
|--------------|----------------|-----------------------------|---------|----------|
| tasUrl | String         | TAS URL                   | M       |          |
| hWalletToken | String         | 월렛토큰                   | M       |          |
| txId     | String       | 거래코드               | M       |          |
| serverToken     | String       | 서버토큰                | M       |          |
| signedDIDDoc|SignedDidDoc | 서명된 DID Document 객체   | M       |[SignedDIDDoc](#4-signeddiddoc)          |

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| String | 사용자 등록 프로토콜 수행 결과를 반환핟다. | M       |          |

#### Usage

```java
String _M132_RequestRegisterUser = walletApi.requestRegisterUser("hWalletToken", tasUrl, "txId", "hServerToken", signedDIDDoc).get();
```

<br>


### 2.10. getSignedWalletInfo

#### Description
`서명된 Wallet 정보를 조회한다.`

#### Declaration

```java
public SignedWalletInfo getSignedWalletInfo() throws Exception
```

#### Parameters

Void

#### Returns

| Type             | Description                    | **M/O** | **Note** |
|------------------|-------------------------|---------|----------|
| SignedWalletInfo | 서명된 WalletInfo 객체       | M       |[SignedWalletInfo](#5-signedwalletinfo)          |

#### Usage

```java
SignedWalletInfo signedInfo = walletApi.getSignedWalletInfo();
```

<br>


## 3. DIDKey

### 3.1. createHolderDIDDoc

#### Description
`사용자 DID Document를 생성한다.`

#### Declaration

```java
public DIDDocument createHolderDIDDoc(String hWalletToken) throws Exception
```

#### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | 월렛토큰                  | M       |          |

#### Returns

| Type         | Description                  | **M/O** | **Note** |
|--------------|-----------------------|---------|----------|
| DIDDocument  | 사용자 DID Document   | M       |          |

#### Usage

```java
DIDDocument didDoc = walletApi.createHolderDIDDoc("hWalletToken");
```

<br>

### 3.2. createSignedDIDDoc

#### Description
`서명된 사용자 DID Document 객체를 생성한다.`

#### Declaration

```java
public SignedDidDoc createSignedDIDDoc(DIDDocument ownerDIDDoc) throws Exception
```

#### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| ownerDIDDoc  | DIDDocument | 소유자의 DID Document 객체                 | M       |          |

#### Returns

| Type            | Description                  | **M/O** | **Note** |
|-----------------|-----------------------|---------|----------|
| SignedDidDoc | 서명된 DID Document 객체   | M       |[SignedDIDDoc](#4-signeddiddoc)          |

#### Usage

```java
SignedDidDoc signedDidDoc = walletApi.createSignedDIDDoc(ownerDIDDoc);
```

<br>

### 3.3. getDIDDocument

#### Description
`DID Document를 조회한다.`

#### Declaration

```java
public DIDDocument getDIDDocument(int type) throws Exception
```

#### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| type  | int | 1 : deviceKey DID Document, 2: holder DID document                  | M       |          |

#### Returns

| Type         | Description                  | **M/O** | **Note** |
|--------------|-----------------------|---------|----------|
| DIDDocument  | DID Document       | M       |          |

#### Usage

```java
DIDDocument didDoc = walletApi.getDIDDocument(1);
```

<br>

### 3.4. generateKeyPair

#### Description
`서명을 위한 PIN 키 쌍을 생성하여 Wallet에 저장한다.`

#### Declaration

```java
public void generateKeyPair(String hWalletToken, String passcode) throws Exception
```

#### Parameters

| Name         | Type   | Description                        | **M/O** | **Note** |
|--------------|--------|-----------------------------|---------|----------|
| hWalletToken | String |월렛토큰                   | M       |          |
| passCode     | String |서명용 PIN               | M       | PIN 서명용 키 생성 시        | 

#### Returns

Void

#### Usage

```java
walletApi.generateKeyPair("hWalletToken", "123456");
```

<br>

### 3.5. getSignedDIDAuth

#### Description
`DIDAuth 서명을 수행한다.`

#### Declaration

```java
public DIDAuth getSignedDIDAuth(String authNonce, String passcode) throws Exception
```

#### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| authNonce  | String | profile의 auth nonce                  | M       |          |
|passcode|String | 서명용 PIN   | M       |          |

#### Returns

| Type            | Description                  | **M/O** | **Note** |
|-----------------|-----------------------|---------|----------|
| DIDAuth   | 서명된 DIDAuth 객체   | M       |[DIDAuth](#6-didauth)          |

#### Usage

```java
DIDAuth signedDIDAuth = walletApi.getSignedDIDAuth("authNonce", "123456");
```

<br>


### 3.6. updateHolderDIDDoc

#### Description
`제공된 지갑 토큰을 사용하여 사용자의 기존 DID 문서를 업데이트합니다.`

#### Declaration

```java
public DIDDocument updateHolderDIDDoc(String hWalletToken) throws WalletException, UtilityException, WalletCoreException
```

#### Parameters

| Name         | Type   | Description | **M/O** | **Note** |
| ------------ | ------ | ----------- | ------- | -------- |
| hWalletToken | String | 월렛 토큰     | M       |          |

#### Returns

| Type        | Description  | **M/O** | **Note** |
| ----------- | ------------ | ------- | -------- |
| DIDDocument | DID 도큐먼트 | M       |          |


#### Usage

```java
walletApi.updateHolderDIDDoc(hWalletToken);
```

<br>



### 3.7. saveDocument

#### Description
`사용자의 DID 문서를 영구 저장소에 저장합니다.`

#### Declaration

```java
public void saveDocument() throws WalletException, WalletCoreException, UtilityException

```

#### Parameters
N/A

#### Returns
N/A

#### Usage

```java
walletApi.saveDocument()
```

<br>

### 3.8. deleteKey

#### Description
`제공된 지갑 토큰에 필요한 권한이 있는지 확인한 후, 사용자의 DID(탈중앙화 식별자) 문서와 연결된 지정된 키를 삭제합니다.`

#### Declaration

```java
public void deleteKey(String hWalletToken, List<String> keyIds) throws WalletCoreException, UtilityException, WalletException
```

#### Parameters

| Name         | Type         | Description | **M/O** | **Note** |
| ------------ | ------------ | ----------- | ------- | -------- |
| hWalletToken | String       | 월렛토큰    | M       |          |
| keyIds       | List<String> | 키 IDs      | M       |          |

#### Returns
N/A

#### Usage

```java
walletApi.deleteKey(ProtocolData.getInstance(context).gethWalletToken(), List.of("bio"));
```

<br>


### 3.9. requestUpdateUser

#### Description
`지정된 지갑 토큰, 서버 토큰, 서명된 DID 인증 및 거래 ID를 사용하여 사용자 DID 업데이트를 요청합니다.`

#### Declaration

```java
CompletableFuture<String> requestUpdateUser(String hWalletToken, String tasUrl, String serverToken, DIDAuth signedDIDAuth, SignedDidDoc signedDIDDoc, String txId) throws Exception
```

#### Parameters

| Name          | Type         | Description              | **M/O** | **Note** |
| ------------- | ------------ | ------------------------ | ------- | -------- |
| hWalletToken  | String       | 월렛 토큰                | M       |          |
| tasUrl        | String       | TAS url                  | M       |          |
| serverToken   | String       | 서버 토큰                | M       |          |
| signedDIDAuth | DIDAuth      | 서명된 DID 인증 객체     | M       |          |
| signedDIDDoc  | SignedDidDoc | 서명된 DID Document 객체 | M       |          |
| txId          | String       | 트랜젝션 ID              | M       |          |

#### Returns

| Type   | Description | **M/O** | **Note** |
| ------ | ----------- | ------- | -------- |
| String | txId        | M       |          |

#### Usage

```java
walletApi.requestUpdateUser(hWalletToken, tasUrl, hServerToken, didAuth, signedDidDoc, txId).get();
```

<br>

### 3.10. requestRestoreUser

#### Description
`제공된 지갑 토큰, 서버 토큰, 서명된 DID 인증 및 거래 ID를 사용하여 사용자 복원을 요청합니다.`

#### Declaration

```java
CompletableFuture<String> requestRestoreUser(String hWalletToken, String tasUrl, String serverToken, DIDAuth signedDIDAuth, String txId) throws Exception
```

#### Parameters

| Name          | Type    | Description          | **M/O** | **Note** |
| ------------- | ------- | -------------------- | ------- | -------- |
| hWalletToken  | String  | 월렛 토큰              | M       |          |
| tasUrl        | String  | TAS url              | M       |          |
| serverToken   | String  | 서버 토큰              | M       |          |
| signedDIDAuth | DIDAuth | 서명된 DID 인증 객체      | M       |          |
| txId          | String  | 트랜젝션 ID            | M       |          |

#### Returns

| Type   | Description | **M/O** | **Note** |
| ------ | ----------- | ------- | -------- |
| String | txId        | M       |          |

#### Usage

```java
walletApi.requestRestoreUser(hWalletToken, tasUrl, hServerToken, didAuth, txId).get();
```

<br>

## 4. Credential

### 4.1. requestIssueVc

#### Description
`VC 발급을 요청한다.`

#### Declaration

```java
public CompletableFuture<String> requestIssueVc(String hWalletToken, String url, String apiGateWayUrl, String serverToken, String refId, IssueProfile profile, DIDAuth signedDIDAuth, String txId) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |
| url | String | TAS 또는 Proxy URL | M |  |
| apiGateWayUrl | String | API Gateway URL | M |  |
| serverToken | String | 서버토큰 | M |  |
| refId | String | 참조번호 | M |  |
| profile | IssueProfile | Issue Profile | M |  |
| signedDIDAuth | DIDAuth | 서명된 DID 인증 객체 | M | [DIDAuth](#6-didauth) |
| txId | String | 거래코드 | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| String | VC ID | M | 성공 시 발급된 VC의 ID를 반환한다 |

#### Usage

```java
String vcId = walletApi.requestIssueVc("hWalletToken", url, apiGateWayUrl, "hServerToken", "refId", profile, signedDIDAuth, "txId").get();
```

<br>

### 4.2. requestRevokeVc

#### Description
`VC 폐기를 요청한다.`

#### Declaration

```java
public CompletableFuture<String> requestRevokeVc(String hWalletToken, String url, String serverToken, String txId, String vcId, String issuerNonce, String passcode, VerifyAuthType.VERIFY_AUTH_TYPE authType) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |
| url | String | TAS 또는 Proxy URL | M |  |
| serverToken | String | 서버토큰 | M |  |
| txId | String | 거래코드 | M |  |
| vcId | String | VC ID | M |  |
| issuerNonce | String | 발급처 nonce | M |  |
| passcode | String | 서명용 PIN | M |  |
| authType | VERIFY_AUTH_TYPE | 제출 인증수단 타입 | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| String | txId | M | 성공 시 거래코드를 반환한다 |

#### Usage

```java
String result = walletApi.requestRevokeVc("hWalletToken", url, "hServerToken", "txId", "vcId", "issuerNonce", "123456", VERIFY_AUTH_TYPE.PIN).get();
```

<br>

### 4.3. getAllCredentials

#### Description
`Wallet에 저장된 모든 VC를 조회한다.`

#### Declaration

```java
public List<VerifiableCredential> getAllCredentials(String hWalletToken) throws Exception
```

#### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | 월렛토큰                  | M       |          |

#### Returns

| Type            | Description                | **M/O** | **Note** |
|-----------------|---------------------|---------|----------|
| List&lt;VerifiableCredential&gt; | VC List 객체  | M       |          |

#### Usage

```java
List<VerifiableCredential> vcList = walletApi.getAllCredentials("hWalletToken");
```

<br>

### 4.4. getCredentials

#### Description
`특정 VC를 조회한다.`

#### Declaration

```java
public List<VerifiableCredential> getCredentials(String hWalletToken, List<String> identifiers) throws Exception
```

#### Parameters

| Name           | Type   | Description                       | **M/O** | **Note** |
|----------------|--------|----------------------------|---------|----------|
| hWalletToken   | String | 월렛토큰                  | M       |          |
| identifiers   | List&lt;String&gt;   | 조회 대상 VC ID List               | M       |          |

#### Returns

| Type        | Description                | **M/O** | **Note** |
|-------------|---------------------|---------|----------|
| List&lt;VerifiableCredential&gt;  | VC List 객체    | M       |          |

#### Usage

```java
List<VerifiableCredential> vcList = walletApi.getCredentials("hWalletToken", List.of("vcId"));
```

<br>

### 4.5. deleteCredentials

#### Description
`특정 VC를 삭제한다.`

#### Declaration

```java
public void deleteCredentials(String hWalletToken, String vcId) throws Exception
```

#### Parameters

| Name           | Type   | Description                       | **M/O** | **Note** |
|----------------|--------|----------------------------|---------|----------|
| hWalletToken   | String | 월렛토큰                  | M       |          |
| vcId   | String   | 삭제 대상 VC ID             | M       |          |

#### Returns
Void

#### Usage

```java
walletApi.deleteCredentials("hWalletToken", "vcId");
```

<br>

### 4.6. createEncVp

#### Description
`검증자 가입증명서 VC 를 검증한 뒤 암호화된 VP 와 accE2e 를 생성한다.`

#### Declaration

```java
public ReturnEncVP createEncVp(String hWalletToken, List<ClaimInfo> claimInfos, VerifyProfile verifyProfile, String apiGateWayUrl, String passcode) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |
| claimInfos | List&lt;ClaimInfo&gt; | 제출할 VC ID 와 클레임 코드 목록 | M | [ClaimInfo](../private/VCManager_ko.md#2-claiminfo) |
| verifyProfile | VerifyProfile | 검증자 정보와 E2E 요청을 담은 검증 프로파일 | M |  |
| apiGateWayUrl | String | 검증자 가입증명서 VC 검증에 쓰는 API Gateway URL | M |  |
| passcode | String | 서명용 PIN | O | PIN 키로 서명할 때 |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| ReturnEncVP | 암호화된 VP 객체 | M | acce2e 객체, encVp 멀티베이스 인코딩 값 |

#### Usage

```java
ReturnEncVP encVp = walletApi.createEncVp("hWalletToken", List.of(new ClaimInfo("vcId", List.of("claim_code"))), verifyProfile, apiGateWayUrl, "123456");
```

<br>

### 4.7. addProofsToDocument

#### Description
`서명이 필요한 객체에 Proof객체를 추가한다.`

#### Declaration

```java
public ProofContainer addProofsToDocument(ProofContainer document, List<String> keyIds, String did, int type, String passcode, boolean isDIDAuth) throws Exception
```

#### Parameters

| Name         | Type         | Description                        | **M/O** | **Note** |
|--------------|--------------|-----------------------------|---------|----------|
| document     | ProofContainer     | Proof객체를 상속받은 문서 객체                        | M       |          |
| keyIds       | List&lt;String&gt;       | 서명할 Key ID List                 | M       |          |
| did     | String     | 서명 대상 DID                        | M       |          |
| type       | int       | 1 : deviceKey DID Document, 2: holder DID document                 | M       |          |
| passcode     | String     | 서명용 PIN                        | O       | PIN 키 서명 시         |
| isDIDAuth       | boolean       | DIDAuth객체일경우 true / 이외에는 false               | M       |          |

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| ProofContainer | Proof객체를 포함한 원객체 | M       |          |

#### Usage

```java
DIDDocument signedDIDDoc = (DIDDocument) walletApi.addProofsToDocument(didDocument, List.of("PIN"), "DID", 2, "123456", false);
```

<br>

### 4.8. isAnyCredentialsSaved

#### Description
`Holder 의 월렛에 저장된 VC 가 있는지 확인한다.`

#### Declaration

```java
public boolean isAnyCredentialsSaved() throws WalletException
```

#### Parameters

N/A

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| boolean | VC 가 하나 이상 저장돼 있으면 `true` | M |  |

#### Usage

```java
if (!walletApi.isAnyCredentialsSaved()) {
    ...
}
```

<br>

### 4.9. createVp

#### Description
`지정한 클레임으로 VP 를 생성한다(암호화하지 않음).`

#### Declaration

```java
public VerifiablePresentation createVp(String hWalletToken, List<ClaimInfo> claimInfos, String passcode, String verifierNonce, OIDV4VPChallenge challenge) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |
| claimInfos | List&lt;ClaimInfo&gt; | 제출할 VC ID 와 클레임 코드 목록 | M | [ClaimInfo](../private/VCManager_ko.md#2-claiminfo) |
| passcode | String | 서명용 PIN | O | PIN 키로 서명할 때 |
| verifierNonce | String | 검증자 nonce | M |  |
| challenge | OIDV4VPChallenge | domain 과 challenge | M | [OIDV4VPChallenge](./DataModel_ko.md#8-oidv4vpchallenge) |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| VerifiablePresentation | VP | M |  |

#### Usage

```java
VerifiablePresentation vp = walletApi.createVp("hWalletToken", claimInfos, "123456", verifierNonce, challenge);
```

<br>

## 5. ZKP

### 5.1. createZkpReferent

#### Description
`사용자가 선택한 참조 대상 정보를 기반으로 각 자격 증명에 대한 참조 대상을 생성`

#### Declaration

```java
public ReferentInfo createZkpReferent(List<UserReferent> customReferents) throws Exception
```

#### Parameters

| Name            | Type | Description                                  | **M/O** | **Note** |
| --------------- | ---- | -------------------------------------------- | ------- | -------- |
| customReferents | List | ZKP 생성에 포함될 사용자 정의 참조 대상 목록           | M       |          |

#### Returns

| Type         | Description                                       | **M/O** | **Note** |
| ------------ | --------------------------------------------------| ------- | -------- |
| ReferentInfo | 자격 증명 생성을 위한 ZKP 참조 객체를 구축하는 데 사용되는 객체 | M       |          |

#### Usage

```java

ReferentInfo referentInfo = WalletApi.getInstance(getContext()).createZkpReferent(customReferents);
```

<br>

### 5.2. createEncZkpProof

#### Description
`사용자의 크리덴셜과 referent 를 기반으로 ZKP 증명을 생성한다.`

#### Declaration

```java
public P311RequestVo createEncZkpProof(String hWalletToken, List<ProofParam> proofParams, Map<String, String> selfAttributes,
                                       ProofRequestProfile proofRequestProfile, String txId, String apiGateWayUrl) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |
| proofParams | List&lt;ProofParam&gt; | 크리덴셜과 referent 를 포함한 증명 파라미터 목록 | M |  |
| selfAttributes | Map&lt;String, String&gt; | 자기 진술 속성 | M |  |
| proofRequestProfile | ProofRequestProfile | 검증자의 증명 요청을 담은 프로파일 | M |  |
| txId | String | 거래코드 | M |  |
| apiGateWayUrl | String | 검증자 검증에 쓰는 API Gateway URL | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| P311RequestVo | 암호화된 ZKP 증명을 담은 요청 객체 | M |  |

#### Usage

```java
P311RequestVo requestVo = WalletApi.getInstance(context).createEncZkpProof(hWalletToken, proofParams, selfAttr, proofRequestProfile, txId, apiGateWayUrl);
```

<br>

### 5.3. searchZkpCredentials

#### Description
`ProofRequest와 일치하는 자격 증명을 검색하고 사용 가능한 참조 대상 목록을 생성`

#### Declaration

```java
public AvailableReferent searchZkpCredentials(String hWalletToken, ProofRequest proofRequest) throws Exception
```

#### Parameters

| Name         | Type         | Description                               | **M/O** | **Note** |
| ------------ | ------------ | ----------------------------------------- | ------- | -------- |
| hWalletToken | String       | 월렛 토큰                                   | M       |          |
| proofRequest | ProofRequest | 검증자가 지정한 필수 속성과 술어를 포함하는 증명 요청  | M       |          |


#### Returns

| Type              | Description                                                          | **M/O** | **Note** |
| ----------------- | -------------------------------------------------------------------- | ------- | -------- |
| AvailableReferent | ZKP를 구성하는 데 사용할 수 있는 일치하는 자격 증명과 참조 항목 목록이 포함된 객체      | M       |          |

#### Usage

```java
AvailableReferent availableReferent = walletApi.searchZkpCredentials(VerifyProof.getInstance(activity).hWalletToken, proofRequestProfileVo.getProofRequestProfile().getProfile().getProofRequest());
```

<br>

### 5.4. getAllZkpCredentials

#### Description
`저장된 모든 자격 증명을 검색`

#### Declaration

```java
public ArrayList<Credential> getAllZkpCredentials(String hWalletToken) throws Exception
```

#### Parameters

| Name         | Type   | Description | **M/O** | **Note** |
| ------------ | ------ | ----------- | ------- | -------- |
| hWalletToken | String | 월렛 토큰   | M       |          |


#### Returns

| Type | Description                                                       | **M/O** | **Note** |
| ---- | ----------------------------------------------------------------- | ------- | -------- |
| List | ZKP 기반 검증 가능한 프레젠테이션에 사용할 수 있는 자격 증명 목록                | M       |          |

#### Usage

```java
List<Credential> zkpVcList = walletApi.getAllZkpCredentials(hWalletToken);
```

<br>

### 5.5. isAnyZkpCredentialsSaved

#### Description
`자격 증명이 저장되었는지 확인`

#### Declaration

```java
public boolean isAnyZkpCredentialsSaved() throws throws Exception
```

#### Parameters

N/A

#### Returns

| Type         | Description                                                          | **M/O** | **Note** |
| ------------ | -------------------------------------------------------------------- | ------- | -------- |
| boolean      | 지갑에 ZKP 호환 자격 증명이 하나 이상 저장된 경우입니다. 그렇지 않은 경우 false      | M       |          |

#### Usage

```java
if (walletApi.isAnyZkpCredentialsSaved()) {
    ...
}
```

<br>

### 5.6. getZkpCredentials

#### Description
`주어진 자격 증명 ID를 기반으로 자격 증명을 검색`

#### Declaration

```java
public List<Credential> getZkpCredentials(String hWalletToken, List<String> identifiers) throws Exception
```

#### Parameters

| Name         | Type   | Description           | **M/O** | **Note** |
| ------------ | ------ | --------------------- | ------- | -------- |
| hWalletToken | String | 월렛 토큰               | M       |          |
| identifiers  | List   | 자격 증명 식별자 목록      | M       |          |

#### Returns

| Type | Description                                                                    | **M/O** | **Note** |
| ---- | ------------------------------------------------------------------------------ | ------- | -------- |
| List | ZKP 기반 프레젠테이션에 사용할 수 있는 주어진 식별자에 해당하는 자격 증명 목록                   | M       |          |

#### Usage

```java
List<Credential> credentialList = walletApi.getZkpCredentials(hWalletToken, List.of(vcId));
```

<br>



## 6. SecurityAuth

### 6.1. registerLock

#### Description
`Wallet의 잠금 상태를 설정한다.`

#### Declaration

```java
public boolean registerLock(String hWalletToken, String passCode, boolean isLock) throws Exception
```

#### Parameters

| Name         | Type   | Description                        | **M/O** | **Note** |
|--------------|--------|-----------------------------|---------|----------|
| hWalletToken | String | 월렛토큰                   | M       |          |
| passCode     | String | Unlock PIN               | M       |          |
| isLock       | boolean | 잠금 활성화 여부            | M       |          |

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | 잠금 설정 성공 여부를 반환한다. | M       |          |

#### Usage

```java
boolean success = walletApi.registerLock("hWalletToken", "123456", true);
```

<br>

### 6.2. authenticateLock

#### Description
`Wallet의 Unlock을 위한 인증을 수행한다.`

#### Declaration

```java
public void authenticateLock(String passCode) throws Exception
```

#### Parameters

| Name         | Type   | Description                        | **M/O** | **Note** |
|--------------|--------|-----------------------------|---------|----------|
| passCode     | String |Unlock PIN               | M       | registerLock 시 설정한 PIN          | 

#### Returns

Void

#### Usage

```java
walletApi.authenticateLock("123456");
```

<br>


### 6.3. isLock

#### Description
`Wallet의 잠금 타입을 조회한다.`

#### Declaration

```java
public boolean isLock() throws Exception
```

#### Parameters
 Void

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Wallet 잠금 타입을 반환한다. | M       |          |

#### Usage

```java
boolean isLocked = walletApi.isLock();
```

<br>


### 6.4. registerBioKey

#### Description
`서명용 생체인증 키를 등록한다.`

#### Declaration

```java
public void registerBioKey(Context ctx) throws WalletException
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| ctx | Context |  | M |  |

#### Returns

N/A

#### Usage

```java
walletApi.registerBioKey(context);
```

<br>

### 6.5. authenticateBioKey

#### Description
`서명용 생체인증 키 사용을 위한 인증을 진행한다.`

#### Declaration

```java
public void authenticateBioKey(Context ctx) throws WalletCoreException, WalletException
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| ctx | Context |  | M |  |

#### Returns

N/A

#### Usage

```java
walletApi.authenticateBioKey(context);
```

<br>

### 6.6. changePin

#### Description
`서명용 PIN 변경`

#### Declaration

```java
public void changePin(String keyId, String oldPin, String newPin) throws Exception
```

#### Parameters

| Name   | Type   | Description   | **M/O** | **Note** |
| ------ | ------ | ------------- | ------- | -------- |
| keyId     | String | 서명용 키아이디 | M       |          |
| oldPIN | String | 현재 PIN      | M       |          |
| newPIN | String | 변경할 PIN    | M       |          |

#### Returns


#### Usage

```java
String oldPin = "123456";
String newPin = "654321";
walletApi.changePin(Constants.KEY_ID_PIN, oldPin, newPin);
```

<br>

### 6.7. changeLock

#### Description
`잠금용 PIN 을 변경한다.`

#### Declaration

```java
public void changeLock(String oldPassCode, String newPassCode) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| oldPassCode | String | old PIN | M |  |
| newPassCode | String | new PIN | M |  |

#### Returns

N/A

#### Usage

```java
walletApi.changeLock("123456", "654321");
```

<br>

### 6.8. authenticatePin

#### Description
`핀 인증을 진행`

#### Declaration

```java
public void authenticatePin(String id, byte[] pin) throws Exception
```

#### Parameters

| Name | Type   | Description               | **M/O** | **Note** |
| ---- | ------ | ------------------------- | ------- | -------- |
| id   | String | 키 ID                     | M       |          |
| pin  | byte[] | 사용자가 입력한 핀 데이터       | M       |          |

#### Returns

N/A

#### Usage

```java
try {
    walletApi.authenticatePin("pin", oldPin.getBytes());
} catch {}
```

<br>



## 7. OID4VC

### 7.1. requestIssueOID4VC

#### Description
`발급자에게 OID4VC 크리덴셜을 요청하고 저장한다.` proof JWT 는 holder DID 키(`#pin` 또는 `#bio`)로 서명한다.

#### Declaration

```java
public CompletableFuture<String> requestIssueOID4VC(String hWalletToken, IssuerMetadataResponse metadata, TokenResponse token, String passcode, String configurationId, String credentialIdentifier, String apiGatewayUrl) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |
| metadata | IssuerMetadataResponse | 발급자 메타데이터 | M | [IssuerMetadataResponse](./DataModel_ko.md#6-issuermetadataresponse) |
| token | TokenResponse | 토큰 응답 | M | [TokenResponse](./DataModel_ko.md#7-tokenresponse) |
| passcode | String | 서명용 PIN. `null` 또는 빈 값이면 BIO 키로 서명 | O |  |
| configurationId | String | 크리덴셜 configuration 식별자 | M |  |
| credentialIdentifier | String | 크리덴셜 식별자. `null` 이면 토큰에서 구한다 | O |  |
| apiGatewayUrl | String | 발급자 서명 검증에 쓰는 API Gateway URL | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| String | 저장된 크리덴셜 ID | M |  |

#### Usage

```java
String vcId = walletApi.requestIssueOID4VC("hWalletToken", metadata, token, "123456", configurationId, null, apiGatewayUrl).get();
```

<br>

### 7.2. requestRevokeOID4VC

#### Description
`저장된 OID4VC(SD-JWT VC / mdoc) 의 폐기를 발급자에게 요청한다.` nonce 를 받아 JWT 폐기 토큰에 서명한 뒤 `credential_revocation_endpoint` 로 POST 한다.

#### Declaration

```java
public CompletableFuture<Void> requestRevokeOID4VC(String hWalletToken, IssuerMetadataResponse metadata, String vcId, String passcode) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |
| metadata | IssuerMetadataResponse | 발급자 메타데이터. `nonce_endpoint` 와 `credential_revocation_endpoint` 필수 | M | [IssuerMetadataResponse](./DataModel_ko.md#6-issuermetadataresponse) |
| vcId | String | 폐기할 저장 OID4VC 의 ID | M |  |
| passcode | String | 저장된 kid 가 pin 이면 필수. bio 이면 생체인증을 쓴다 | O |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| CompletableFuture&lt;Void&gt; | 발급자가 200 OK 로 응답하면 정상 완료 | M |  |

#### Throws

- `ExecutionException`: nonce 요청 또는 폐기 POST 실패. 폐기 POST 실패의 cause 는 `CommunicationException`

#### Usage

```java
walletApi.requestRevokeOID4VC("hWalletToken", metadata, "vcId", "123456").get();
```

<br>

### 7.3. getAllOID4VCs

#### Description
`저장된 OID4VC 크리덴셜을 모두 조회한다.`

#### Declaration

```java
public List<OID4VCCredentialItem> getAllOID4VCs(String hWalletToken) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| List&lt;OID4VCCredentialItem&gt; | OID4VC 크리덴셜 전체 | M | [OID4VCCredentialItem](./DataModel_ko.md#1-oid4vccredentialitem) |

#### Usage

```java
List<OID4VCCredentialItem> items = walletApi.getAllOID4VCs("hWalletToken");
```

<br>

### 7.4. getOID4VCs

#### Description
`지정한 식별자의 OID4VC 크리덴셜을 조회한다.`

#### Declaration

```java
public List<OID4VCCredentialItem> getOID4VCs(String hWalletToken, List<String> identifiers) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |
| identifiers | List&lt;String&gt; | 크리덴셜 식별자 목록 | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| List&lt;OID4VCCredentialItem&gt; | 식별자에 해당하는 OID4VC 크리덴셜 | M | [OID4VCCredentialItem](./DataModel_ko.md#1-oid4vccredentialitem) |

#### Usage

```java
List<OID4VCCredentialItem> items = walletApi.getOID4VCs("hWalletToken", List.of("vcId"));
```

<br>

### 7.5. deleteOID4VCs

#### Description
`지정한 식별자의 OID4VC 크리덴셜을 삭제한다.`

#### Declaration

```java
public void deleteOID4VCs(String hWalletToken, List<String> identifiers) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |
| identifiers | List&lt;String&gt; | 크리덴셜 식별자 목록 | M |  |

#### Returns

N/A

#### Usage

```java
walletApi.deleteOID4VCs("hWalletToken", List.of("vcId"));
```

<br>

### 7.6. isAnyOID4VCSaved

#### Description
`Holder 의 월렛에 저장된 OID4VC 크리덴셜이 있는지 확인한다.`

#### Declaration

```java
public boolean isAnyOID4VCSaved() throws WalletException
```

#### Parameters

N/A

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| boolean | 하나 이상 저장돼 있으면 `true` | M |  |

#### Usage

```java
if (!walletApi.isAnyOID4VCSaved()) {
    ...
}
```

<br>

## 8. OID4VP

### 8.1. matchCredentials

#### Description
`검증자의 OID4VP 인가 요청(DCQL)에 저장된 크리덴셜을 매칭한다.`

#### Declaration

```java
public List<MatchedCredential> matchCredentials(String hWalletToken, AuthorizationRequest authRequest) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |
| authRequest | AuthorizationRequest | DCQL 쿼리를 담은 검증자 인가 요청 | M | [AuthorizationRequest](./DataModel_ko.md#2-authorizationrequest) |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| List&lt;MatchedCredential&gt; | 쿼리를 만족하는 제출 가능 항목 | M | [MatchedCredential](./DataModel_ko.md#3-matchedcredential) |

#### Usage

```java
List<MatchedCredential> matched = walletApi.matchCredentials("hWalletToken", authRequest);
```

<br>

### 8.2. createVpToken

#### Description
`선택한 크리덴셜로 OID4VP 응답 본문을 만든다.` form-urlencoded `vp_token` 제출이거나, `direct_post.jwt` 이면 암호화된 `response` 다.

#### Declaration

```java
public byte[] createVpToken(String hWalletToken, AuthorizationRequest authRequest, List<MatchedCredential> matchedCredentials, String passcode) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |
| authRequest | AuthorizationRequest | 검증자 인가 요청 | M | [AuthorizationRequest](./DataModel_ko.md#2-authorizationrequest) |
| matchedCredentials | List&lt;MatchedCredential&gt; | 제출할 크리덴셜·클레임. 비어 있으면 안 된다 | M | [MatchedCredential](./DataModel_ko.md#3-matchedcredential) |
| passcode | String | PIN 바인딩 키면 PIN, BIO 키면 `null` | O |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| byte[] | 앱이 제출할 form-urlencoded 응답 본문 | M |  |

#### Throws

- `MSDKWLT06000` (`ERR_CODE_OID4VC_MANAGER_INVALID_PARAMETER`): `matchedCredentials` 가 null 이거나 비어 있음

#### Usage

```java
byte[] body = walletApi.createVpToken("hWalletToken", authRequest, matched, "123456");
```

<br>

## 9. Proximity

### 9.1. matchMdocRequest

#### Description
`근접 통신으로 받은 mdoc DeviceRequest 에 저장된 mdoc 크리덴셜을 매칭한다.`

#### Declaration

```java
public List<MdocRequestedDocument> matchMdocRequest(String hWalletToken, byte[] deviceRequest) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |
| deviceRequest | byte[] | CBOR 인코딩된 DeviceRequest. 비어 있으면 안 된다 | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| List&lt;MdocRequestedDocument&gt; | 요청 문서와 매칭된 크리덴셜 | M | [MdocRequestedDocument](./DataModel_ko.md#4-mdocrequesteddocument) |

#### Throws

- `MSDKWLT06000` (`ERR_CODE_OID4VC_MANAGER_INVALID_PARAMETER`): `deviceRequest` 가 null 이거나 비어 있음

#### Usage

```java
List<MdocRequestedDocument> docs = walletApi.matchMdocRequest("hWalletToken", deviceRequest);
```

<br>

### 9.2. createDeviceResponse

#### Description
`선택한 문서로 mdoc DeviceResponse 를 만든다.`

#### Declaration

```java
public MdocDeviceResponse createDeviceResponse(String hWalletToken, byte[] deviceRequest, byte[] sessionTranscript, List<MdocRequestedDocument> selected, String passcode) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | 월렛토큰 | M |  |
| deviceRequest | byte[] | CBOR 인코딩된 DeviceRequest. 비어 있으면 안 된다 | M |  |
| sessionTranscript | byte[] | CBOR 인코딩된 SessionTranscript(태그 없음 또는 #6.24 태그). 비어 있으면 안 된다 | M |  |
| selected | List&lt;MdocRequestedDocument&gt; | `matchMdocRequest` 결과에서 고른 문서 | M | [MdocRequestedDocument](./DataModel_ko.md#4-mdocrequesteddocument) |
| passcode | String | PIN 바인딩 키면 PIN, BIO 키면 `null` | O |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| MdocDeviceResponse | DeviceResponse 바이트와 문서별 device 인증 방식 | M | [MdocDeviceResponse](./DataModel_ko.md#5-mdocdeviceresponse) |

#### Throws

- `MSDKWLT06000` (`ERR_CODE_OID4VC_MANAGER_INVALID_PARAMETER`): `deviceRequest` 또는 `sessionTranscript` 가 null 이거나 비어 있음

#### Usage

```java
MdocDeviceResponse res = walletApi.createDeviceResponse("hWalletToken", deviceRequest, sessionTranscript, selected, "123456");
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
    RESTORE_DID(7),
    ISSUE_VC(8),
    REMOVE_VC(9),
    PRESENT_VP(10),
    LIST_VC(11),
    DETAIL_VC(12),
    CREATE_DID_AND_ISSUE_VC(13),
    LIST_VC_AND_PRESENT_VP(14);
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