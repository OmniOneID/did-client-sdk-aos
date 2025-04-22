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

Android Wallet SDK API
==

- Subject: WalletAPI
- Writer: Sangjun Kim
- Date: 2024-08-19
- Version: v1.0.0

| Version | Date       | History                 |
| ------- | ---------- | ------------------------|
| v1.0.0  | 2024-08-19 | Initial                 |


<div style="page-break-after: always;"></div>

# Table of Contents
- [APIs](#api-list)
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
# API List
## 0. constructor

### Description
 `WalletApi construct`

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
 `Check whether DeviceKey Wallet exists.`

### Declaration

```java
public boolean isExistWallet()
```

### Parameters

N/A

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Returns whether the wallet exists. | M       |          |

### Usage

```java
boolean exists = walletApi.isExistWallet();
```

<br>

## 2. createWallet

### Description
`Create a DeviceKey Wallet.`

### Declaration

```java
public boolean createWallet(String walletUrl, String tasUrl) throws Exception
```

### Parameters

| Name      | Type   | Description                      | **M/O** | **Note** |
|-----------|--------|----------------------------------|---------|----------|
| walletUrl    | String | Wallet URL                          | M       |          |
| tasUrl | String | TAS URL                       | M       |          |


### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Returns whether wallet creation was successful. | M       |          |

### Usage

```java
boolean success = walletApi.createWallet();
```

<br>

## 3. deleteWallet

### Description
`Delete DeviceKey Wallet..`

### Declaration

```java
public void deleteWallet() throws Exception
```

### Parameters

Void

### Returns

N/A

### Usage

```java
walletApi.deleteWallet();
```

<br>

## 4. createWalletTokenSeed

### Description
`Generate a wallet token seed.`

### Declaration

```java
public WalletTokenSeed createWalletTokenSeed(WalletTokenPurpose.WALLET_TOKEN_PURPOSE purpose, String pkgName, String userId) throws Exception
```

### Parameters

| Name      | Type   | Description                             | **M/O** | **Note** |
|-----------|--------|----------------------------------|---------|----------|
| purpose   | WALLET_TOKEN_PURPOSE |Wallet token purpose                       | M       |[WALLET_TOKEN_PURPOSE](#1-wallet_token_purpose)         |
| pkgName   | String | CA Package Name                        | M       |          |
| userId    | String | User ID                        | M       |          |

### Returns

| Type            | Description                  | **M/O** | **Note** |
|-----------------|-----------------------|---------|----------|
| WalletTokenSeed | Wallet Token Seed Object   | M       |[WalletTokenSeed](#1-wallettokenseed)          |

### Usage

```java
WalletTokenSeed tokenSeed = walletApi.createWalletTokenSeed(purpose, "org.opendid.did.ca", "user_id");
```

<br>

## 5. createNonceForWalletToken

### Description
`Generate a nonce for creating wallet tokens.`

### Declaration

```java
public String createNonceForWalletToken(WalletTokenData walletTokenData) throws Exception
```

### Parameters

| Name           | Type           | Description                  | **M/O** | **Note** |
|----------------|----------------|-----------------------|---------|----------|
| walletTokenData | WalletTokenData | Wallet token Data      | M       |[WalletTokenData](#2-wallettokendata)          |

### Returns

| Type    | Description              | **M/O** | **Note** |
|---------|-------------------|---------|----------|
| String  | Nonce for wallet token generation | M       |          |

### Usage

```java
String nonce = walletApi.createNonceForWalletToken(walletTokenData);
```

<br>

## 6. bindUser

### Description
`Perform user personalization in Wallet.`

### Declaration

```java
public boolean bindUser(String hWalletToken) throws Exception
```

### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | Wallet Token                  | M       |          |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| Bool    | Returns whether personalization was successful. | M       |          |

### Usage

```java
boolean success = walletApi.bindUser("hWalletToken");
```

<br>

## 7. unbindUser

### Description
`Perform user depersonalization.`

### Declaration

```java
public boolean unbindUser(String hWalletToken) throws Exception
```

### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | Wallet Token                  | M       |          |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Returns whether depersonalization was successful. | M       |          |

### Usage

```java
boolean success = walletApi.unbindUser("hWalletToken");
```

<br>

## 8. registerLock

### Description
`Sets the lock status of the wallet.`

### Declaration

```java
public boolean registerLock(String hWalletToken, String passCode, boolean isLock) throws Exception
```

### Parameters

| Name         | Type   | Description                        | **M/O** | **Note** |
|--------------|--------|-----------------------------|---------|----------|
| hWalletToken | String | Wallet Token                   | M       |          |
| passcode     | String | Unlock PIN               | M       |          |
| isLock       | boolean | Whether the lock is activated           | M       |          |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| Bool | Returns whether the lock setup was successful. | M       |          |

### Usage

```java
boolean success = walletApi.registerLock("hWalletToken", "123456", true);
```

<br>

## 9. authenticateLock

### Description
`Perform authentication to unlock the wallet.`

### Declaration

```java
public void authenticateLock(String passCode) throws Exception
```

### Parameters

| Name         | Type   | Description                        | **M/O** | **Note** |
|--------------|--------|-----------------------------|---------|----------|
| passCode     | String |Unlock PIN               | M       | PIN set when registerLock          | 

### Returns

Void

### Usage

```java
walletApi.authenticateLock("hWalletToken", "123456");
```

<br>

## 10. createHolderDIDDoc

### Description
`Create a user DID Document.`

### Declaration

```java
public DIDDocument createHolderDIDDoc(String hWalletToken) throws Exception
```

### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | Wallet Token                  | O       |          |

### Returns

| Type         | Description                  | **M/O** | **Note** |
|--------------|-----------------------|---------|----------|
| DIDDocument  | DID Document   | M       |          |

### Usage

```java
DIDDocument didDoc = walletApi.createHolderDIDDoc("hWalletToken");
```

<br>

## 11. createSignedDIDDoc

### Description
`Creates a signed user DID Document object.`

### Declaration

```java
public SignedDidDoc createSignedDIDDoc(DIDDocument ownerDIDDoc) throws Exception
```

### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| ownerDIDDoc  | DIDDocument | Owner's DID document object                 | M       |          |

### Returns

| Type            | Description                  | **M/O** | **Note** |
|-----------------|-----------------------|---------|----------|
| SignedDidDoc | Signed DID Document Object   | M       |[SignedDIDDoc](#4-signeddiddoc)          |

### Usage

```java
SignedDidDoc signedDidDoc = walletApi.createSignedDIDDoc(ownerDIDDoc);
```

<br>

## 12. getDIDDocument

### Description
`Retrieve the DID Document.`

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
`Generate a PIN key pair for signing and store it in your Wallet.`

### Declaration

```java
public void generateKeyPair(String hWalletToken, String passcode) throws Exception
```

### Parameters

| Name         | Type   | Description                        | **M/O** | **Note** |
|--------------|--------|-----------------------------|---------|----------|
| hWalletToken | String |Wallet Token                   | M       |          |
| passCode     | String |PIN for signing               | M       | When generating a key for PIN signing      | 

### Returns

Void

### Usage

```java
walletApi.generateKeyPair("hWalletToken", "123456");
```

<br>

## 14. isLock

### Description
`Check the lock type of the wallet.`

### Declaration

```java
public boolean isLock() throws Exception
```

### Parameters
 Void

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Returns the wallet lock type. | M       |          |

### Usage

```java
boolean isLocked = walletApi.isLock();
```

<br>

## 15. getSignedWalletInfo

### Description
`signed wallet information.`

### Declaration

```java
public SignedWalletInfo getSignedWalletInfo() throws Exception
```

### Parameters

Void

### Returns

| Type             | Description                    | **M/O** | **Note** |
|------------------|-------------------------|---------|----------|
| SignedWalletInfo | Signed WalletInfo object      | M       |[SignedWalletInfo](#5-signedwalletinfo)          |

### Usage

```java
SignedWalletInfo signedInfo = walletApi.getSignedWalletInfo();
```

<br>

## 16. requestRegisterUser

### Description
`Request user registration.`

### Declaration

```java
public CompletableFuture<String> requestRegisterUser(String hWalletToken, String tasUrl, String txId, String serverToken, SignedDidDoc signedDIDDoc) throws Exception
```

### Parameters

| Name         | Type           | Description                        | **M/O** | **Note** |
|--------------|----------------|-----------------------------|---------|----------|
| tasUrl | String         | TAS URL                   | M       |          |
| hWalletToken | String         | Wallet Token                   | M       |          |
| txId     | String       | Transaction Code               | M       |          |
| serverToken     | String       | Server Token                | M       |          |
| signedDIDDoc|SignedDidDoc | Signed DID Document Object   | M       |[SignedDIDDoc](#4-signeddiddoc)          |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| String | Returns the result of performing the user registration protocol. | M       |          |

### Usage

```java
String _M132_RequestRegisterUser = walletApi.requestRegisterUser("hWalletToken", "txId", "hServerToken", signedDIDDoc).get();
```

<br>

## 17. getSignedDIDAuth

### Description
`Perform DIDAuth signing.`

### Declaration

```java
public DIDAuth getSignedDIDAuth(String authNonce, String passcode) throws Exception
```

### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| authNonce  | String | Profile auth nonce                  | M       |          |
|passcode|String | User passcode   | M       |          |

### Returns

| Type            | Description                  | **M/O** | **Note** |
|-----------------|-----------------------|---------|----------|
| DIDAuth   | Signed DIDAuth object   | M       |[DIDAuth](#6-didauth)          |

### Usage

```java
DIDAuth signedDIDAuth = walletApi.getSignedDIDAuth("authNonce", "123456");
```

<br>

## 18. requestIssueVc

### Description
`Request for issuance of VC.`

### Declaration

```java
public CompletableFuture<String> requestIssueVc(String hWalletToken, String tasUrl, String apiGateWayUrl, String txId, String serverToken, String refId, String authNonce, IssueProfile profile, DIDAuth signedDIDAuth) throws Exception
```

### Parameters

| Name        | Type           | Description                        | **M/O** | **Note** |
|-------------|----------------|-----------------------------|---------|----------|
| tasUrl | String         | TAS URL                  | M       |          |
| hWalletToken | String         | Wallet Token                 | M       |          |
| txId     | String       | Transaction Code                | M       |          |
| serverToken     | String       | Server Token                | M       |          |
| refId     | String       | Reference ID                | M       |          |
| profile|IssueProfile | Issue Profile   | M       |[datamodel link]          |
| signedDIDAuth|DIDAuth | Signed DID Document object   | M       |[DIDAuth](#6-didauth)         |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| String | VC ID | M       |Returns the ID of the VC issued on success          |

### Usage

```java
String vcId = walletApi.requestIssueVc("hWalletToken", "txId", "hServerToken", "refId", profile, signedDIDAuth).get();
```

<br>

## 19. requestRevokeVc

### Description
`Request for revocation of VC.`

### Declaration

```java
public CompletableFuture<String> requestRevokeVc(String hWalletToken, String tasUrl, String serverToken, String txId, String vcId, String issuerNonce, String passcode, VerifyAuthType.VERIFY_AUTH_TYPE authType) throws Exception

```

### Parameters

| Name        | Type           | Description                        | **M/O** | **Note** |
|-------------|----------------|-----------------------------|---------|----------|
| hWalletToken | String         | Wallet Token                   | M       |          |
| tasUrl | String         | TAS URL                   | M       |          |
| txId     | String       | Transaction code               | M       |          |
| serverToken     | String       | Server Token                | M       |          |
| vcId     | String       | VC ID                | M       |          |
| issuerNonce|String | Issuer nonce   | M       |[datamodel link]          |
| passcode|String | PIN for signing   | M       |[DIDAuth](#6-didauth)         |
| authType|VERIFY_AUTH_TYPE | Submission authentication method type   | M       |       |


### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| String | txId | M       |Returns the Transaction ID of the VC revoked on success          |

### Usage

```java
String result = walletApi.requestRevokeVc("hWalletToken", "hServerToken", "txId", "vcId", "issuerNonce", "123456").get();
```

<br>

## 20. getAllCredentials

### Description
`Get all VCs stored in the Wallet.`

### Declaration

```java
public List<VerifiableCredential> getAllCredentials(String hWalletToken) throws Exception
```

### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | Wallet Token                  | M       |          |

### Returns

| Type            | Description                | **M/O** | **Note** |
|-----------------|---------------------|---------|----------|
| List&lt;VerifiableCredential&gt; | VC List object  | M       |          |

### Usage

```java
List<VerifiableCredential> vcList = walletApi.getAllCredentials("hWalletToken");
```

<br>

## 21. getCredentials

### Description
`Query a specific VC.`

### Declaration

```java
public List<VerifiableCredential> getCredentials(String hWalletToken, List<String> identifiers) throws Exception
```

### Parameters

| Name           | Type   | Description                       | **M/O** | **Note** |
|----------------|--------|----------------------------|---------|----------|
| hWalletToken   | String | Wallet Token                  | M       |          |
| identifiers   | List&lt;String&gt;   | List of VC IDs to be searched               | M       |          |

### Returns

| Type        | Description                | **M/O** | **Note** |
|-------------|---------------------|---------|----------|
| List&lt;VerifiableCredential&gt;  | VC List object    | M       |          |

### Usage

```java
List<VerifiableCredential> vcList = walletApi.getCredentials("hWalletToken", List.of("vcId"));
```

<br>

## 22. deleteCredentials

### Description
`Delete a specific VC.`

### Declaration

```java
public void deleteCredentials(String hWalletToken, String vcId) throws Exception
```

### Parameters

| Name           | Type   | Description                       | **M/O** | **Note** |
|----------------|--------|----------------------------|---------|----------|
| hWalletToken   | String | Wallet Token                  | M       |          |
| vcId   | String   | VC ID to be deleted             | M       |          |

### Returns
Void

### Usage

```java
walletApi.deleteCredentials("hWalletToken", "vcId");
```

<br>

## 23. createEncVp

### Description
`Generate encrypted VP and accE2e.`

### Declaration

```java
public ReturnEncVP createEncVp(String hWalletToken, String vcId, List<String> claimCode, ReqE2e reqE2e, String passcode, String nonce, VerifyAuthType.VERIFY_AUTH_TYPE authType) throws Exception

```

### Parameters

| Name        | Type           | Description                        | **M/O** | **Note** |
|-------------|----------------|-----------------------------|---------|----------|
| hWalletToken | String         | Wallet Token                   | M       |          |
| vcId     | String       | VC ID               | M       |          |
| claimCode     | List&lt;String&gt;       | Claim Code to Submit                | M       |          |
| reqE2e     | ReqE2e       | E2E encryption/decryption information                | M       |        |
|passcode|String | PIN for signing   | M       |          |
| nonce|String | nonce   | M       |       |
| authType|VERIFY_AUTH_TYPE | Submission authentication method type   | M       |       |

### Returns

| Type   | Description              | **M/O** | **Note** |
|--------|-------------------|---------|----------|
| ReturnEncVP  | Encrypted VP Object| M       |acce2e object, encVp Multibas encoded value      |

### Usage

```java
EncVP encVp = walletApi.createEncVp("hWalletToken", "vcId", List.of("claim_code"), reqE2e, "123456", "nonce", VERIFY_AUTH_TYPE.PIN);
```

<br>

## 24. registerBioKey

### Description
`Register the biometric authentication key for signing.`

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
`Authentication is performed to use the biometric authentication key for signing.`

### Declaration

```java
public void authenticateBioKey(Fragment fragment, Context context) throws Exception
```

### Parameters

| Name         | Type     | Description                        | **M/O** | **Note** |
|--------------|----------|-----------------------------|---------|----------|
| hWalletToken | String   | Wallet Token                   | M       |          |
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
`Add a Proof object to the object that needs to be signed.`

### Declaration

```java
public ProofContainer addProofsToDocument(ProofContainer document, List<String> keyIds, String did, int type, String passcode, boolean isDIDAuth) throws Exception
```

### Parameters

| Name         | Type         | Description                        | **M/O** | **Note** |
|--------------|--------------|-----------------------------|---------|----------|
| document     | ProofContainer     | Document object that inherits Proof object                       | M       |          |
| keyIds       | List&lt;String&gt;       | Key ID List to sign                 | M       |          |
| did     | String     | Signature target DID                        | M       |          |
| type       | int       | 1 : deviceKey DID Document, 2: holder DID document                 | M       |          |
| passcode     | String     | PIN for signing                        | O       | When signing a PIN key        |
| isDIDAuth       | boolean       | true if it is a DIDAuth object / false otherwise              | M       |          |

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| ProofContainer | Original object including Proof object | M       |          |

### Usage

```java
DIDDocument signedDIDDoc = (DIDDocument) walletApi.addProofsToDocument(didDocument, List.of("PIN"), "DID", 2, "123456", false);
```

<br>


## 27. isSavedBioKey

### Description
`Check if there is a saved biometric authentication key.`

### Declaration

```java
public boolean isSavedBioKey() throws Exception
```

### Parameters

Void

### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Returns whether a biometric authentication key exists | M       |          |

### Usage

```java
boolean hasBioKey = walletApi.isSavedBioKey();
```

<br>

## 28. changePin

### Description
`Change PIN for signing`

### Declaration

```java
public void changePin(String keyId, String oldPin, String newPin) throws Exception
```

### Parameters

| Name   | Type   | Description   | **M/O** | **Note** |
| ------ | ------ | ------------- | ------- | -------- |
| keyId     | String | key ID for signing | M       |          |
| oldPIN | String | old PIN      | M       |          |
| newPIN | String | new PIN    | M       |          |

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
`Change PIN for Unlock`

### Declaration

```java
public void changeLock(String oldPin, String newPin) throws Exception
```

### Parameters

| Name   | Type   | Description   | **M/O** | **Note** |
| ------ | ------ | ------------- | ------- | -------- |
| oldPIN | String | old PIN      | M       |          |
| newPIN | String | new PIN    | M       |          |

### Returns


### Usage

```java
String oldPin = "123456";
String newPin = "654321";
walletApi.changeLock(oldPin, newPin);
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