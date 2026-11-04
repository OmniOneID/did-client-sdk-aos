
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

Android ZKPManager API

==
- Subject: ZKPManager
- Author: Eunjeong Kim
- Date: 2026-10-28
- Version: v3.0.0

| Version | Date       | Description    |
|---------|------------|----------------|
| v3.0.0  | 2026-10-30 | Referent attributes keyed by referent key |
| v1.0.0  | 2025-04-30 | Initial Draft  |

# Table of Contents

- [APIs](#api-list)
  - [1. constructor](#1-constructor)
  - [2. getCredentials](#2-getcredentials)
  - [3. getAllCredentials](#3-getallcredentials)
  - [4. deleteCredentials](#4-deletecredentials)
  - [5. deleteAllCredentials](#5-deleteallcredentials)
  - [6. isAnyCredentialsSaved](#6-isanycredentialssaved)
  - [7. createCredentialRequest](#7-createcredentialrequest)
  - [8. verifyAndStoreCredential](#8-verifyandstorecredential)
  - [9. searchCredentials](#9-searchcredentials)
  - [10. createReferent](#10-createreferent)
  - [11. createProof](#11-createproof)

# API List

## 1. constructor

### Description

Java constructor for ZKPManager.

### Declaration

```java
ZKPManager(String fileName, Context context);
```

### Parameters

| Parameter | Type    | Description         | **M/O** | Note |
|-----------|---------|---------------------|---------|------|
| fileName  | String  | File name to save   | M       |      |
| context   | Context | Android context     | M       |      |

### Returns

| Type       | Description         | **M/O** | Note |
|------------|---------------------|---------|------|
| ZKPManager | ZKPManager object   | M       |      |


## 2. getCredentials

### Description

Retrieve credentials based on given credential IDs.

### Declaration

```java
List<CredentialInfo> getCredentials(List<String> identifiers)
```

### Parameters

| Parameter   | Type | Description              | **M/O** | Note |
|-------------|------|--------------------------| -- | -- |
| identifiers | List | List of credential IDs   | M | |

### Returns

| Type | Description              | **M/O** | Note |
|------|--------------------------| -- | -- |
| List | List of stored credentials | M | |


## 3. getAllCredentials

### Description

Retrieve all stored credentials.

### Declaration

```java
ArrayList<CredentialInfo> getAllCredentials()
```

### Returns

| Type      | Description              | **M/O** | Note |
|-----------|--------------------------| -- | -- |
| ArrayList | All stored credentials   | M | |


## 4. deleteCredentials

### Description

Delete stored credentials matching the given list of IDs.

### Declaration

```java
void deleteCredentials(List<String> identifiers)
```

### Parameters

| Parameter   | Type | Description              | **M/O** | Note |
|-------------|------|--------------------------| -- | -- |
| identifiers | List | List of credential IDs   | M | |

### Returns

void


## 5. deleteAllCredentials

### Description

Delete all stored credentials.

### Declaration

```java
void deleteAllCredentials()
```

### Returns

void


## 6. isAnyCredentialsSaved

### Description

Check if any credentials are saved.

### Declaration

```java
boolean isAnyCredentialsSaved()
```

### Returns

boolean


## 7. createCredentialRequest

### Description

Create a Credential Request for ZKP functionality.

### Declaration

```java
ZkpRequestCredentialBuilder createCredentialRequest(String proverDid,
                                                    CredentialPrimaryPublicKey credentialPublicKey,
                                                    CredentialOffer credOffer)
```

### Parameters

| Parameter           | Type                       | Description           | **M/O** | Note |
| ------------------- | -------------------------- | --------------------- | ------- | ---- |
| proverDid           | String                     | Prover DID            | M       |      |
| credentialPublicKey | CredentialPrimaryPublicKey | Credential Public Key | M       |      |
| credOffer           | CredentialOffer            | Credential Offer      | M       |      |
|                     |                            |                       |         |      |

### Returns

| Type                       | Description                  | **M/O** | Note |
| -------------------------- | ---------------------------- | ------- | ---- |
| CredentialRequestContainer | Credential Request Container | M       |      |
|                            |                              |         |      |


## 8. verifyAndStoreCredential

### Description

Verify the issued credential and store it.

### Declaration

```java
boolean verifyAndStoreCredential(CredentialRequestMeta credentialRequestMeta,
                                  CredentialPrimaryPublicKey credentialPrimaryPublicKey,
                                  Credential credential)
```

### Parameters

| Parameter                  | Type                       | Description                       | **M/O** | Note |
| -------------------------- | -------------------------- | --------------------------------- | ------- | ---- |
| credentialRequestMeta      | CredentialRequestMeta      | Blinded Master Secret Data, nonce | M       |      |
| credentialPrimaryPublicKey | CredentialPrimaryPublicKey | Credential Public Key             | M       |      |
| credential                 | Credential                 | Credential Object                 | M       |      |
|                            |                            |                                   |         |      |

### Returns

| Type    | Description         | **M/O** | Note |
| ------- | ------------------- | ------- | ---- |
| boolean | Verification result | M       |      |
|         |                     |         |      |


## 9. searchCredentials

### Description

Search credentials matching the ProofRequest and generate a list of available Referents.

### Declaration

```java
ZkpSearchCredentialBuilder searchCredentials(ProofRequest proofRequest)
```

### Parameters

| Parameter    | Type         | Description       | **M/O** | Note |
| ------------ | ------------ | ----------------- | ------- | ---- |
| proofRequest | ProofRequest | ZKP Proof Request | M       |      |
|              |              |                   |         |      |

### Returns

| Type                       | Description               | **M/O** | Note |
| -------------------------- | ------------------------- | ------- | ---- |
| ZkpSearchCredentialBuilder | Credential Search Builder | M       |      |
|                            |                           |         |      |


## 10. createReferent

### Description

Create Referents for each credential based on the user-selected referent information.
Each attribute is stored under its referent key, with the attribute name kept in `ReferentAttributeValue`. The same referent key given twice for the same credential throws `MSDKWLT04603`.

### Declaration

```java
ReferentInfo createReferent(List<UserReferent> customReferents) throws WalletCoreException, UtilityException
```

### Parameters

| Parameter       | Type | Description             | **M/O** | Note |
| --------------- | ---- | ----------------------- | ------- | ---- |
| customReferents | List | User Selected Referents | M       |      |
|                 |      |                         |         |      |

### Returns

| Type                     | Description      | **M/O** | Note |
| ------------------------ | ---------------- | ------- | ---- |
| ReferentInfo             | Referent information | M       |      |
|                          |                  |         |      |


## 11. createProof

### Description

Generate a ZKP proof based on the user's credentials and referents.
Every requested attribute and predicate must be selected exactly once under its referent key, with a name matching the proof request and present in the credential. Otherwise `MSDKWLT04500`, `MSDKWLT04501`, `MSDKWLT04603` or `MSDKWLT04000` (unknown referent key) is thrown.

### Declaration

```java
Proof createProof(ProofRequest proofRequest, List<ProofParam> proofParams, Map<String, String> selfAttributes)
```

### Parameters

| Parameter      | Type                | Description                 | **M/O** | Note |
| -------------- | ------------------- | --------------------------- | ------- | ---- |
| proofRequest   | ProofRequest        | ZKP Proof Request           | M       |      |
| proofParams    | List                | Referent Mapping Parameters | M       |      |
| selfAttributes | Map<String, String> | Self-Attested Attributes    | M       |      |

### Returns

| Type  | Description | **M/O** | Note |
| ----- | ----------- | ------- | ---- |
| Proof | Proof       | M       |      |
|       |             |         |      |