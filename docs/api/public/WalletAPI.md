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
- Writer: Eunjeong Kim
- Date: 2026-10-28
- Version: v3.0.0

| Version | Date       | History                                      |
| ------- | ---------- | -------------------------------------------- |
| v3.0.0  | 2026-10-30 | Add OID4VC, OID4VP, Proximity APIs and createVp; sync signatures with source |
| v2.0.1  | 2025-10-14 | Add DID-related function and authenticatePin |
| v2.0.0  | 2025-04-30 | ZKP                                          |
| v1.0.0  | 2024-08-19 | Initial                                      |


<div style="page-break-after: always;"></div>

# Table of Contents
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
 `WalletApi construct`

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
 `Check whether DeviceKey Wallet exists.`

#### Declaration

```java
public boolean isExistWallet()
```

#### Parameters

N/A

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Returns whether the wallet exists. | M       |          |

#### Usage

```java
boolean exists = walletApi.isExistWallet();
```

<br>

### 2.2. createWallet

#### Description
`Create a DeviceKey Wallet.`

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

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Returns whether wallet creation was successful. | M       |          |

#### Usage

```java
boolean success = walletApi.createWallet(walletUrl, tasUrl);
```

<br>

### 2.3. deleteWallet

#### Description
`Delete DeviceKey Wallet..`

#### Declaration

```java
public void deleteWallet(boolean deleteAll) throws Exception
```

#### Parameters

| Name      | Type    | Description           | **M/O** | **Note** |
| --------- | ------- | --------------------- | ------- | -------- |
| deleteAll | boolean | Wallet deletion scope | M       |          |

#### Returns

N/A

#### Usage

```java
walletApi.deleteWallet(deleteAll);
```

<br>

### 2.4. createWalletTokenSeed

#### Description
`Generate a wallet token seed.`

#### Declaration

```java
public WalletTokenSeed createWalletTokenSeed(WalletTokenPurpose.WALLET_TOKEN_PURPOSE purpose, String pkgName, String userId) throws Exception
```

#### Parameters

| Name      | Type   | Description                             | **M/O** | **Note** |
|-----------|--------|----------------------------------|---------|----------|
| purpose   | WALLET_TOKEN_PURPOSE |Wallet token purpose                       | M       |[WALLET_TOKEN_PURPOSE](#1-wallet_token_purpose)         |
| pkgName   | String | CA Package Name                        | M       |          |
| userId    | String | User ID                        | M       |          |

#### Returns

| Type            | Description                  | **M/O** | **Note** |
|-----------------|-----------------------|---------|----------|
| WalletTokenSeed | Wallet Token Seed Object   | M       |[WalletTokenSeed](#1-wallettokenseed)          |

#### Usage

```java
WalletTokenSeed tokenSeed = walletApi.createWalletTokenSeed(purpose, "org.opendid.did.ca", "user_id");
```

<br>

### 2.5. createNonceForWalletToken

#### Description
`Generate a nonce for creating wallet tokens.`

#### Declaration

```java
public String createNonceForWalletToken(String apiGateWayUrl, WalletTokenData walletTokenData) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| apiGateWayUrl | String | API Gateway URL | M |  |
| walletTokenData | WalletTokenData | Wallet token Data | M | [WalletTokenData](#2-wallettokendata) |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| String | Nonce for wallet token generation | M |  |

#### Usage

```java
String nonce = walletApi.createNonceForWalletToken(apiGateWayUrl, walletTokenData);
```

<br>

### 2.6. createLocalWalletToken

#### Description
`Issue a wallet token locally, without contacting the CAS.`

Available once the wallet has been personalized online (`bindUser`), and only for the read and
presentation purposes: `LIST_VC`, `DETAIL_VC`, `PRESENT_VP`, `LIST_VC_AND_PRESENT_VP`. The call
is synchronous and performs no network I/O. Unlike `createNonceForWalletToken` it returns the
hWalletToken itself, not a nonce. The stored token is replaced, so a token obtained earlier by
either path stops verifying.

#### Declaration

```java
public String createLocalWalletToken(WalletTokenPurpose.WALLET_TOKEN_PURPOSE purpose, String pkgName) throws Exception
```

#### Parameters

| Name      | Type   | Description                             | **M/O** | **Note** |
|-----------|--------|----------------------------------|---------|----------|
| purpose   | WALLET_TOKEN_PURPOSE | Wallet token purpose. One of the four above. | M       |[WALLET_TOKEN_PURPOSE](#1-wallet_token_purpose)         |
| pkgName   | String | CA Package Name                        | M       |          |

#### Returns

| Type    | Description              | **M/O** | **Note** |
|---------|-------------------|---------|----------|
| String  | hWalletToken to pass to the APIs that accept the given purpose | M       |          |

#### Throws

- `MSDKWLT05002` (`ERR_CODE_WALLET_VERIFY_PARAMETER_FAIL`): `pkgName` is empty, or `purpose` is not one of the four
- `MSDKWLT05046` (`ERR_CODE_WALLET_NOT_PERSONALIZED`): the wallet has never been personalized

#### Usage

```java
String hWalletToken = walletApi.createLocalWalletToken(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC, "org.opendid.did.ca");
List<VerifiableCredential> credentials = walletApi.getAllCredentials(hWalletToken);
```

<br>

### 2.7. bindUser

#### Description
`Perform user personalization in Wallet.`

#### Declaration

```java
public boolean bindUser(String hWalletToken) throws Exception
```

#### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | Wallet Token                  | M       |          |

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| Bool    | Returns whether personalization was successful. | M       |          |

#### Usage

```java
boolean success = walletApi.bindUser("hWalletToken");
```

<br>

### 2.8. unbindUser

#### Description
`Perform user depersonalization.`

#### Declaration

```java
public boolean unbindUser(String hWalletToken) throws Exception
```

#### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | Wallet Token                  | M       |          |

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Returns whether depersonalization was successful. | M       |          |

#### Usage

```java
boolean success = walletApi.unbindUser("hWalletToken");
```

<br>

### 2.9. requestRegisterUser

#### Description
`Request user registration.`

#### Declaration

```java
public CompletableFuture<String> requestRegisterUser(String hWalletToken, String tasUrl, String txId, String serverToken, SignedDidDoc signedDIDDoc) throws Exception
```

#### Parameters

| Name         | Type           | Description                        | **M/O** | **Note** |
|--------------|----------------|-----------------------------|---------|----------|
| tasUrl | String         | TAS URL                   | M       |          |
| hWalletToken | String         | Wallet Token                   | M       |          |
| txId     | String       | Transaction Code               | M       |          |
| serverToken     | String       | Server Token                | M       |          |
| signedDIDDoc|SignedDidDoc | Signed DID Document Object   | M       |[SignedDIDDoc](#4-signeddiddoc)          |

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| String | Returns the result of performing the user registration protocol. | M       |          |

#### Usage

```java
String _M132_RequestRegisterUser = walletApi.requestRegisterUser("hWalletToken", tasUrl, "txId", "hServerToken", signedDIDDoc).get();
```

<br>

### 2.10. getSignedWalletInfo

#### Description
`signed wallet information.`

#### Declaration

```java
public SignedWalletInfo getSignedWalletInfo() throws Exception
```

#### Parameters

Void

#### Returns

| Type             | Description                    | **M/O** | **Note** |
|------------------|-------------------------|---------|----------|
| SignedWalletInfo | Signed WalletInfo object      | M       |[SignedWalletInfo](#5-signedwalletinfo)          |

#### Usage

```java
SignedWalletInfo signedInfo = walletApi.getSignedWalletInfo();
```

<br>


## 3. DIDKey

### 3.1. createHolderDIDDoc

#### Description
`Create a user DID Document.`

#### Declaration

```java
public DIDDocument createHolderDIDDoc(String hWalletToken) throws Exception
```

#### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | Wallet Token                  | O       |          |

#### Returns

| Type         | Description                  | **M/O** | **Note** |
|--------------|-----------------------|---------|----------|
| DIDDocument  | DID Document   | M       |          |

#### Usage

```java
DIDDocument didDoc = walletApi.createHolderDIDDoc("hWalletToken");
```

<br>

### 3.2. createSignedDIDDoc

#### Description
`Creates a signed user DID Document object.`

#### Declaration

```java
public SignedDidDoc createSignedDIDDoc(DIDDocument ownerDIDDoc) throws Exception
```

#### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| ownerDIDDoc  | DIDDocument | Owner's DID document object                 | M       |          |

#### Returns

| Type            | Description                  | **M/O** | **Note** |
|-----------------|-----------------------|---------|----------|
| SignedDidDoc | Signed DID Document Object   | M       |[SignedDIDDoc](#4-signeddiddoc)          |

#### Usage

```java
SignedDidDoc signedDidDoc = walletApi.createSignedDIDDoc(ownerDIDDoc);
```

<br>

### 3.3. getDIDDocument

#### Description
`Retrieve the DID Document.`

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
`Generate a PIN key pair for signing and store it in your Wallet.`

#### Declaration

```java
public void generateKeyPair(String hWalletToken, String passcode) throws Exception
```

#### Parameters

| Name         | Type   | Description                        | **M/O** | **Note** |
|--------------|--------|-----------------------------|---------|----------|
| hWalletToken | String |Wallet Token                   | M       |          |
| passCode     | String |PIN for signing               | M       | When generating a key for PIN signing      | 

#### Returns

Void

#### Usage

```java
walletApi.generateKeyPair("hWalletToken", "123456");
```

<br>

### 3.5. getSignedDIDAuth

#### Description
`Perform DIDAuth signing.`

#### Declaration

```java
public DIDAuth getSignedDIDAuth(String authNonce, String passcode) throws Exception
```

#### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| authNonce  | String | Profile auth nonce                  | M       |          |
|passcode|String | User passcode   | M       |          |

#### Returns

| Type            | Description                  | **M/O** | **Note** |
|-----------------|-----------------------|---------|----------|
| DIDAuth   | Signed DIDAuth object   | M       |[DIDAuth](#6-didauth)          |

#### Usage

```java
DIDAuth signedDIDAuth = walletApi.getSignedDIDAuth("authNonce", "123456");
```

<br>


### 3.6. updateHolderDIDDoc

#### Description
`Updates the existing DID document for the holder using the provided wallet token.`

#### Declaration

```java
public DIDDocument updateHolderDIDDoc(String hWalletToken) throws WalletException, UtilityException, WalletCoreException
```

#### Parameters

| Name         | Type   | Description | **M/O** | **Note** |
| ------------ | ------ | ----------- | ------- | -------- |
| hWalletToken | String | wallet token     | M       |          |

#### Returns


#### Usage

```java
walletApi.updateHolderDIDDoc(hWalletToken);
```

<br>

### 3.7. saveDocument

#### Description
`Saves the holder’s DID document into persistent storage.`

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
`Deletes the specified keys associated with the holder’s DID document after verifying that the provided wallet token has the required permissions.`

#### Declaration

```java
public void deleteKey(String hWalletToken, List<String> keyIds) throws WalletCoreException, UtilityException, WalletException
```

#### Parameters

| Name         | Type         | Description                     | **M/O** | **Note** |
| ------------ | ------------ | ------------------------------- | ------- | -------- |
| hWalletToken | String       | wallet token                    | M       |          |
| keyIds       | List<String> | a list of key IDs to be deleted | M       |          |

#### Returns
N/A

#### Usage

```java
walletApi.deleteKey(ProtocolData.getInstance(context).gethWalletToken(), List.of("bio"));
```

<br>

### 3.9. requestUpdateUser

#### Description
`Requests user DID update with the given wallet token, server token, signed DID authentication, and transaction ID.`

#### Declaration

```java
CompletableFuture<String> requestUpdateUser(String hWalletToken, String tasUrl, String serverToken, DIDAuth signedDIDAuth, SignedDidDoc signedDIDDoc, String txId) throws Exception
```

#### Parameters

| Name          | Type         | Description                    | **M/O** | **Note** |
| ------------- | ------------ | ------------------------------ | ------- | -------- |
| hWalletToken  | String       | Wallet Token                   | M       |          |
| tasUrl        | String       | TAS url                        | M       |          |
| serverToken   | String       | Server Token                   | M       |          |
| signedDIDAuth | DIDAuth      | Signed DID Authenticate Object | M       |          |
| signedDIDDoc  | SignedDidDoc | Signed DID Document Object     | M       |          |
| txId          | String       | txId                           | M       |          |

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
`Requests user restoration with the given wallet token, server token, signed DID authentication, and transaction ID.`

#### Declaration

```java
CompletableFuture<String> requestRestoreUser(String hWalletToken, String tasUrl, String serverToken, DIDAuth signedDIDAuth, String txId) throws Exception
```

#### Parameters

| Name          | Type    | Description                    | **M/O** | **Note** |
| ------------- | ------- | ------------------------------ | ------- | -------- |
| hWalletToken  | String  | Wallet Token                   | M       |          |
| tasUrl        | String  | TAS url                        | M       |          |
| serverToken   | String  | Server Token                   | M       |          |
| signedDIDAuth | DIDAuth | Signed DID Authenticate Object | M       |          |
| txId          | String  | txId                           | M       |          |

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
`Request for issuance of VC.`

#### Declaration

```java
public CompletableFuture<String> requestIssueVc(String hWalletToken, String url, String apiGateWayUrl, String serverToken, String refId, IssueProfile profile, DIDAuth signedDIDAuth, String txId) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |
| url | String | TAS or Proxy URL | M |  |
| apiGateWayUrl | String | API Gateway URL | M |  |
| serverToken | String | Server Token | M |  |
| refId | String | Reference ID | M |  |
| profile | IssueProfile | Issue Profile | M |  |
| signedDIDAuth | DIDAuth | Signed DID Auth object | M | [DIDAuth](#6-didauth) |
| txId | String | Transaction Code | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| String | VC ID | M | Returns the ID of the VC issued on success |

#### Usage

```java
String vcId = walletApi.requestIssueVc("hWalletToken", url, apiGateWayUrl, "hServerToken", "refId", profile, signedDIDAuth, "txId").get();
```

<br>

### 4.2. requestRevokeVc

#### Description
`Request for revocation of VC.`

#### Declaration

```java
public CompletableFuture<String> requestRevokeVc(String hWalletToken, String url, String serverToken, String txId, String vcId, String issuerNonce, String passcode, VerifyAuthType.VERIFY_AUTH_TYPE authType) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |
| url | String | TAS or Proxy URL | M |  |
| serverToken | String | Server Token | M |  |
| txId | String | Transaction code | M |  |
| vcId | String | VC ID | M |  |
| issuerNonce | String | Issuer nonce | M |  |
| passcode | String | PIN for signing | M |  |
| authType | VERIFY_AUTH_TYPE | Submission authentication method type | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| String | txId | M | Returns the Transaction ID of the VC revoked on success |

#### Usage

```java
String result = walletApi.requestRevokeVc("hWalletToken", url, "hServerToken", "txId", "vcId", "issuerNonce", "123456", VERIFY_AUTH_TYPE.PIN).get();
```

<br>

### 4.3. getAllCredentials

#### Description
`Get all VCs stored in the Wallet.`

#### Declaration

```java
public List<VerifiableCredential> getAllCredentials(String hWalletToken) throws Exception
```

#### Parameters

| Name          | Type   | Description                       | **M/O** | **Note** |
|---------------|--------|----------------------------|---------|----------|
| hWalletToken  | String | Wallet Token                  | M       |          |

#### Returns

| Type            | Description                | **M/O** | **Note** |
|-----------------|---------------------|---------|----------|
| List&lt;VerifiableCredential&gt; | VC List object  | M       |          |

#### Usage

```java
List<VerifiableCredential> vcList = walletApi.getAllCredentials("hWalletToken");
```

<br>

### 4.4. getCredentials

#### Description
`Query a specific VC.`

#### Declaration

```java
public List<VerifiableCredential> getCredentials(String hWalletToken, List<String> identifiers) throws Exception
```

#### Parameters

| Name           | Type   | Description                       | **M/O** | **Note** |
|----------------|--------|----------------------------|---------|----------|
| hWalletToken   | String | Wallet Token                  | M       |          |
| identifiers   | List&lt;String&gt;   | List of VC IDs to be searched               | M       |          |

#### Returns

| Type        | Description                | **M/O** | **Note** |
|-------------|---------------------|---------|----------|
| List&lt;VerifiableCredential&gt;  | VC List object    | M       |          |

#### Usage

```java
List<VerifiableCredential> vcList = walletApi.getCredentials("hWalletToken", List.of("vcId"));
```

<br>

### 4.5. deleteCredentials

#### Description
`Delete a specific VC.`

#### Declaration

```java
public void deleteCredentials(String hWalletToken, String vcId) throws Exception
```

#### Parameters

| Name           | Type   | Description                       | **M/O** | **Note** |
|----------------|--------|----------------------------|---------|----------|
| hWalletToken   | String | Wallet Token                  | M       |          |
| vcId   | String   | VC ID to be deleted             | M       |          |

#### Returns
Void

#### Usage

```java
walletApi.deleteCredentials("hWalletToken", "vcId");
```

<br>

### 4.6. createEncVp

#### Description
`Verify the verifier's certificate VC, then generate the encrypted VP and accE2e.`

#### Declaration

```java
public ReturnEncVP createEncVp(String hWalletToken, List<ClaimInfo> claimInfos, VerifyProfile verifyProfile, String apiGateWayUrl, String passcode) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |
| claimInfos | List&lt;ClaimInfo&gt; | VC IDs and claim codes to submit | M | [ClaimInfo](../private/VCManager.md#2-claiminfo) |
| verifyProfile | VerifyProfile | Verify profile carrying the verifier information and the E2E request | M |  |
| apiGateWayUrl | String | API Gateway URL used to verify the verifier's certificate VC | M |  |
| passcode | String | PIN for signing | O | When signing with a PIN key |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| ReturnEncVP | Encrypted VP Object | M | acce2e object, encVp Multibase encoded value |

#### Usage

```java
ReturnEncVP encVp = walletApi.createEncVp("hWalletToken", List.of(new ClaimInfo("vcId", List.of("claim_code"))), verifyProfile, apiGateWayUrl, "123456");
```

<br>

### 4.7. addProofsToDocument

#### Description
`Add a Proof object to the object that needs to be signed.`

#### Declaration

```java
public ProofContainer addProofsToDocument(ProofContainer document, List<String> keyIds, String did, int type, String passcode, boolean isDIDAuth) throws Exception
```

#### Parameters

| Name      | Type               | Description                                        | **M/O** | **Note**               |
| --------- | ------------------ | -------------------------------------------------- | ------- | ---------------------- |
| document  | ProofContainer     | Document object that inherits Proof object         | M       |                        |
| keyIds    | List&lt;String&gt; | Key ID List to sign                                | M       |                        |
| did       | String             | Signature target DID                               | M       |                        |
| type      | int                | 1 : deviceKey DID Document, 2: holder DID document | M       |                        |
| passcode  | String             | PIN for signing                                    | O       | When signing a PIN key |
| isDIDAuth | boolean            | true if it is a DIDAuth object / false otherwise   | M       |                        |

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| ProofContainer | Original object including Proof object | M       |          |

#### Usage

```java
DIDDocument signedDIDDoc = (DIDDocument) walletApi.addProofsToDocument(didDocument, List.of("PIN"), "DID", 2, "123456", false);
```

<br>


### 4.8. isAnyCredentialsSaved

#### Description
`Checks whether any credentials are saved in the holder’s wallet.`

#### Declaration

```java
public boolean isAnyCredentialsSaved() throws WalletException
```

#### Parameters

N/A

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| boolean | `true` if at least one credential is saved | M |  |

#### Usage

```java
if (!walletApi.isAnyCredentialsSaved()) {
    ...
}
```

<br>

### 4.9. createVp

#### Description
`Generate a VP (not encrypted) for the given claims.`

#### Declaration

```java
public VerifiablePresentation createVp(String hWalletToken, List<ClaimInfo> claimInfos, String passcode, String verifierNonce, OIDV4VPChallenge challenge) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |
| claimInfos | List&lt;ClaimInfo&gt; | VC IDs and claim codes to submit | M | [ClaimInfo](../private/VCManager.md#2-claiminfo) |
| passcode | String | PIN for signing | O | When signing with a PIN key |
| verifierNonce | String | Verifier nonce | M |  |
| challenge | OIDV4VPChallenge | Domain and challenge | M | [OIDV4VPChallenge](./DataModel.md#8-oidv4vpchallenge) |

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
`Create Referents for each credential based on the user-selected referent information.`

#### Declaration

```java
public ReferentInfo createZkpReferent(List<UserReferent> customReferents) throws Exception
```

#### Parameters

| Name            | Type | Description                                                            | **M/O** | **Note** |
| --------------- | ---- | ---------------------------------------------------------------------- | ------- | -------- |
| customReferents | List | A list of user-defined referents to be included in the ZKP generation. | M       |          |

#### Returns

| Type         | Description                                                          | **M/O** | **Note** |
| ------------ | -------------------------------------------------------------------- | ------- | -------- |
| ReferentInfo | An object used to build ZKP referents for credential proof creation. | M       |          |

#### Usage

```java

ReferentInfo referentInfo = WalletApi.getInstance(getContext()).createZkpReferent(customReferents);
```

<br>

### 5.2. createEncZkpProof

#### Description
`Generate a ZKP proof based on the user's credentials and referents.`

#### Declaration

```java
public P311RequestVo createEncZkpProof(String hWalletToken, List<ProofParam> proofParams, Map<String, String> selfAttributes,
                                       ProofRequestProfile proofRequestProfile, String txId, String apiGateWayUrl) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |
| proofParams | List&lt;ProofParam&gt; | Proof parameters including credentials and referents | M |  |
| selfAttributes | Map&lt;String, String&gt; | Self-attested attributes | M |  |
| proofRequestProfile | ProofRequestProfile | Profile containing the verifier's proof request | M |  |
| txId | String | Transaction code | M |  |
| apiGateWayUrl | String | API Gateway URL used to verify the verifier | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| P311RequestVo | Request object carrying the encrypted ZKP proof | M |  |

#### Usage

```java
P311RequestVo requestVo = WalletApi.getInstance(context).createEncZkpProof(hWalletToken, proofParams, selfAttr, proofRequestProfile, txId, apiGateWayUrl);
```

<br>

### 5.3. searchZkpCredentials

#### Description
`Search credentials matching the ProofRequest and generate a list of available Referents.`

#### Declaration

```java
public AvailableReferent searchZkpCredentials(String hWalletToken, ProofRequest proofRequest) throws Exception
```

#### Parameters

| Name         | Type         | Description                                                                                | **M/O** | **Note** |
| ------------ | ------------ | ------------------------------------------------------------------------------------------ | ------- | -------- |
| hWalletToken | String       | Wallet Token                                                                               | M       |          |
| proofRequest | ProofRequest | The proof request containing required attributes and predicates specified by the verifier. | M       |          |


#### Returns

| Type         | Description                                                          | **M/O** | **Note** |
| ------------ | -------------------------------------------------------------------- | ------- | -------- |
| AvailableReferent | An object containing a list of matching credentials and referents that can be used to construct a ZKP. | M       |          |

#### Usage

```java
AvailableReferent availableReferent = walletApi.searchZkpCredentials(VerifyProof.getInstance(activity).hWalletToken, proofRequestProfileVo.getProofRequestProfile().getProfile().getProofRequest());
```

<br>

### 5.4. getAllZkpCredentials

#### Description
`Retrieve all stored credentials.`

#### Declaration

```java
public ArrayList<Credential> getAllZkpCredentials(String hWalletToken) throws Exception
```

#### Parameters

| Name   | Type   | Description   | **M/O** | **Note** |
| ------ | ------ | ------------- | ------- | -------- |
| hWalletToken | String | Wallet Token      | M       |          |


#### Returns

| Type         | Description                                                          | **M/O** | **Note** |
| ------------ | -------------------------------------------------------------------- | ------- | -------- |
| List | A list of credentials that can be used for ZKP-based verifiable presentations. | M       |          |

#### Usage

```java
List<Credential> zkpVcList = walletApi.getAllZkpCredentials(hWalletToken);
```

<br>

### 5.5. isAnyZkpCredentialsSaved

#### Description
`Check if any credentials are saved.`

#### Declaration

```java
public boolean isAnyZkpCredentialsSaved() throws throws Exception
```

#### Parameters

N/A

#### Returns

| Type         | Description                                                          | **M/O** | **Note** |
| ------------ | -------------------------------------------------------------------- | ------- | -------- |
| boolean | if at least one ZKP-compatible credential is stored in the wallet; {@code false} otherwise. | M       |          |

#### Usage

```java
if (walletApi.isAnyZkpCredentialsSaved()) {
    ...
}
```

<br>

### 5.6. getZkpCredentials

#### Description
`Retrieve credentials based on given credential IDs.`

#### Declaration

```java
public List<Credential> getZkpCredentials(String hWalletToken, List<String> identifiers) throws Exception
```

#### Parameters

| Name   | Type   | Description   | **M/O** | **Note** |
| ------ | ------ | ------------- | ------- | -------- |
| hWalletToken | String | Wallet Token      | M       |          |
| identifiers | List | A list of credential identifiers    | M       |          |

#### Returns

| Type         | Description                                                          | **M/O** | **Note** |
| ------------ | -------------------------------------------------------------------- | ------- | -------- |
| List | A list of credentials corresponding to the given identifiers that can be used for ZKP-based presentations. | M       |          |

#### Usage

```java
List<Credential> credentialList = walletApi.getZkpCredentials(hWalletToken, List.of(vcId));
```

<br>


## 6. SecurityAuth

### 6.1. registerLock

#### Description
`Sets the lock status of the wallet.`

#### Declaration

```java
public boolean registerLock(String hWalletToken, String passCode, boolean isLock) throws Exception
```

#### Parameters

| Name         | Type   | Description                        | **M/O** | **Note** |
|--------------|--------|-----------------------------|---------|----------|
| hWalletToken | String | Wallet Token                   | M       |          |
| passcode     | String | Unlock PIN               | M       |          |
| isLock       | boolean | Whether the lock is activated           | M       |          |

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| Bool | Returns whether the lock setup was successful. | M       |          |

#### Usage

```java
boolean success = walletApi.registerLock("hWalletToken", "123456", true);
```

<br>

### 6.2. authenticateLock

#### Description
`Perform authentication to unlock the wallet.`

#### Declaration

```java
public void authenticateLock(String passCode) throws Exception
```

#### Parameters

| Name         | Type   | Description                        | **M/O** | **Note** |
|--------------|--------|-----------------------------|---------|----------|
| passCode     | String |Unlock PIN               | M       | PIN set when registerLock          | 

#### Returns

Void

#### Usage

```java
walletApi.authenticateLock("123456");
```

<br>



### 6.3. isLock

#### Description
`Check the lock type of the wallet.`

#### Declaration

```java
public boolean isLock() throws Exception
```

#### Parameters
 Void

#### Returns

| Type    | Description                | **M/O** | **Note** |
|---------|---------------------|---------|----------|
| boolean | Returns the wallet lock type. | M       |          |

#### Usage

```java
boolean isLocked = walletApi.isLock();
```

<br>


### 6.4. registerBioKey

#### Description
`Register the biometric authentication key for signing.`

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
`Authentication is performed to use the biometric authentication key for signing.`

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
`Change PIN for signing`

#### Declaration

```java
public void changePin(String keyId, String oldPin, String newPin) throws Exception
```

#### Parameters

| Name   | Type   | Description   | **M/O** | **Note** |
| ------ | ------ | ------------- | ------- | -------- |
| keyId     | String | key ID for signing | M       |          |
| oldPIN | String | old PIN      | M       |          |
| newPIN | String | new PIN    | M       |          |

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
`Change PIN for Unlock`

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
`Proceed with PIN authentication`

#### Declaration

```java
public void authenticatePin(String id, byte[] pin) throws Exception
```

#### Parameters

| Name | Type   | Description                  | **M/O** | **Note** |
| ---- | ------ | ---------------------------- | ------- | -------- |
| id   | String | key ID                       | M       |          |
| pin  | byte[] | PIN data entered by the user | M       |          |

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
`Request an OID4VC credential from the issuer and store it.` The proof JWT is signed with the holder DID key (`#pin` or `#bio`).

#### Declaration

```java
public CompletableFuture<String> requestIssueOID4VC(String hWalletToken, IssuerMetadataResponse metadata, TokenResponse token, String passcode, String configurationId, String credentialIdentifier, String apiGatewayUrl) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |
| metadata | IssuerMetadataResponse | Issuer metadata | M | [IssuerMetadataResponse](./DataModel.md#6-issuermetadataresponse) |
| token | TokenResponse | Token response | M | [TokenResponse](./DataModel.md#7-tokenresponse) |
| passcode | String | PIN for signing; `null` or empty to sign with the BIO key | O |  |
| configurationId | String | Credential configuration identifier | M |  |
| credentialIdentifier | String | Credential identifier; `null` to resolve it from the token | O |  |
| apiGatewayUrl | String | API Gateway URL used to verify the issuer signature | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| String | Stored credential ID | M |  |

#### Usage

```java
String vcId = walletApi.requestIssueOID4VC("hWalletToken", metadata, token, "123456", configurationId, null, apiGatewayUrl).get();
```

<br>

### 7.2. requestRevokeOID4VC

#### Description
`Request the issuer to revoke a stored OID4VC (SD-JWT VC / mdoc).` Requests a nonce, signs a JWT revocation token, and POSTs it to `credential_revocation_endpoint`.

#### Declaration

```java
public CompletableFuture<Void> requestRevokeOID4VC(String hWalletToken, IssuerMetadataResponse metadata, String vcId, String passcode) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |
| metadata | IssuerMetadataResponse | Issuer metadata; `nonce_endpoint` and `credential_revocation_endpoint` are required | M | [IssuerMetadataResponse](./DataModel.md#6-issuermetadataresponse) |
| vcId | String | ID of the stored OID4VC to revoke | M |  |
| passcode | String | Required when the stored kid is pin; biometric authentication is used when it is bio | O |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| CompletableFuture&lt;Void&gt; | Completes normally when the issuer responds with 200 OK | M |  |

#### Throws

- `ExecutionException`: the nonce request or the revocation POST failed; for the revocation POST the cause is a `CommunicationException`

#### Usage

```java
walletApi.requestRevokeOID4VC("hWalletToken", metadata, "vcId", "123456").get();
```

<br>

### 7.3. getAllOID4VCs

#### Description
`Retrieve all stored OID4VC credentials.`

#### Declaration

```java
public List<OID4VCCredentialItem> getAllOID4VCs(String hWalletToken) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| List&lt;OID4VCCredentialItem&gt; | All OID4VC credentials | M | [OID4VCCredentialItem](./DataModel.md#1-oid4vccredentialitem) |

#### Usage

```java
List<OID4VCCredentialItem> items = walletApi.getAllOID4VCs("hWalletToken");
```

<br>

### 7.4. getOID4VCs

#### Description
`Retrieve the OID4VC credentials with the given identifiers.`

#### Declaration

```java
public List<OID4VCCredentialItem> getOID4VCs(String hWalletToken, List<String> identifiers) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |
| identifiers | List&lt;String&gt; | Credential identifiers | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| List&lt;OID4VCCredentialItem&gt; | OID4VC credentials matching the identifiers | M | [OID4VCCredentialItem](./DataModel.md#1-oid4vccredentialitem) |

#### Usage

```java
List<OID4VCCredentialItem> items = walletApi.getOID4VCs("hWalletToken", List.of("vcId"));
```

<br>

### 7.5. deleteOID4VCs

#### Description
`Delete the OID4VC credentials with the given identifiers.`

#### Declaration

```java
public void deleteOID4VCs(String hWalletToken, List<String> identifiers) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |
| identifiers | List&lt;String&gt; | Credential identifiers | M |  |

#### Returns

N/A

#### Usage

```java
walletApi.deleteOID4VCs("hWalletToken", List.of("vcId"));
```

<br>

### 7.6. isAnyOID4VCSaved

#### Description
`Check whether any OID4VC credential is saved in the holder's wallet.`

#### Declaration

```java
public boolean isAnyOID4VCSaved() throws WalletException
```

#### Parameters

N/A

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| boolean | `true` if at least one credential is saved | M |  |

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
`Match stored credentials against the verifier's OID4VP authorization request (DCQL).`

#### Declaration

```java
public List<MatchedCredential> matchCredentials(String hWalletToken, AuthorizationRequest authRequest) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |
| authRequest | AuthorizationRequest | Verifier's authorization request carrying the DCQL query | M | [AuthorizationRequest](./DataModel.md#2-authorizationrequest) |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| List&lt;MatchedCredential&gt; | Submittable items satisfying the query | M | [MatchedCredential](./DataModel.md#3-matchedcredential) |

#### Usage

```java
List<MatchedCredential> matched = walletApi.matchCredentials("hWalletToken", authRequest);
```

<br>

### 8.2. createVpToken

#### Description
`Create the OID4VP response body for the selected credentials.` A form-urlencoded `vp_token` submission, or an encrypted `response` for `direct_post.jwt`.

#### Declaration

```java
public byte[] createVpToken(String hWalletToken, AuthorizationRequest authRequest, List<MatchedCredential> matchedCredentials, String passcode) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |
| authRequest | AuthorizationRequest | Verifier's authorization request | M | [AuthorizationRequest](./DataModel.md#2-authorizationrequest) |
| matchedCredentials | List&lt;MatchedCredential&gt; | Credentials/claims to present; must not be empty | M | [MatchedCredential](./DataModel.md#3-matchedcredential) |
| passcode | String | PIN when a PIN-bound key is used; `null` for a BIO key | O |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| byte[] | Form-urlencoded response body for the app to submit | M |  |

#### Throws

- `MSDKWLT06000` (`ERR_CODE_OID4VC_MANAGER_INVALID_PARAMETER`): `matchedCredentials` is null or empty

#### Usage

```java
byte[] body = walletApi.createVpToken("hWalletToken", authRequest, matched, "123456");
```

<br>

## 9. Proximity

### 9.1. matchMdocRequest

#### Description
`Match stored mdoc credentials against an mdoc DeviceRequest received over proximity.`

#### Declaration

```java
public List<MdocRequestedDocument> matchMdocRequest(String hWalletToken, byte[] deviceRequest) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |
| deviceRequest | byte[] | CBOR-encoded DeviceRequest; must not be empty | M |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| List&lt;MdocRequestedDocument&gt; | Requested documents and the matched credentials | M | [MdocRequestedDocument](./DataModel.md#4-mdocrequesteddocument) |

#### Throws

- `MSDKWLT06000` (`ERR_CODE_OID4VC_MANAGER_INVALID_PARAMETER`): `deviceRequest` is null or empty

#### Usage

```java
List<MdocRequestedDocument> docs = walletApi.matchMdocRequest("hWalletToken", deviceRequest);
```

<br>

### 9.2. createDeviceResponse

#### Description
`Create the mdoc DeviceResponse for the selected documents.`

#### Declaration

```java
public MdocDeviceResponse createDeviceResponse(String hWalletToken, byte[] deviceRequest, byte[] sessionTranscript, List<MdocRequestedDocument> selected, String passcode) throws Exception
```

#### Parameters

| Name | Type | Description | **M/O** | **Note** |
|------|------|-------------|---------|----------|
| hWalletToken | String | Wallet Token | M |  |
| deviceRequest | byte[] | CBOR-encoded DeviceRequest; must not be empty | M |  |
| sessionTranscript | byte[] | CBOR-encoded SessionTranscript, bare or #6.24-tagged; must not be empty | M |  |
| selected | List&lt;MdocRequestedDocument&gt; | Documents chosen from `matchMdocRequest` | M | [MdocRequestedDocument](./DataModel.md#4-mdocrequesteddocument) |
| passcode | String | PIN when a PIN-bound key is used; `null` for a BIO key | O |  |

#### Returns

| Type | Description | **M/O** | **Note** |
|------|-------------|---------|----------|
| MdocDeviceResponse | DeviceResponse bytes and the device auth method per document | M | [MdocDeviceResponse](./DataModel.md#5-mdocdeviceresponse) |

#### Throws

- `MSDKWLT06000` (`ERR_CODE_OID4VC_MANAGER_INVALID_PARAMETER`): `deviceRequest` or `sessionTranscript` is null or empty

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