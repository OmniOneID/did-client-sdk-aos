# Android ZKPManager Core SDK API

- Subject: ZKPManager
- Author: Dongjun Park
- Date: 2025-04-07
- Version: v1.0.0

| Version | Date       | Description    |
|---------|------------|----------------|
| v1.0.0  | 2025-04-07 | Initial Draft  |

# Table of Contents

- [APIs](#api-list)
  - [1. constructor](#1-constructor)
  - [2. addCredentials](#2-addcredentials)
  - [3. getCredentials](#3-getcredentials)
  - [4. getAllCredentials](#4-getallcredentials)
  - [5. deleteCredentials](#5-deletecredentials)
  - [6. deleteAllCredentials](#6-deleteallcredentials)
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

| Parameter   | Type | Description              |
|-------------|------|--------------------------|
| identifiers | List | List of credential IDs   |

### Returns

| Type | Description              |
|------|--------------------------|
| List | List of stored credentials |


## 3. getAllCredentials

### Description

Retrieve all stored credentials.

### Declaration

```java
ArrayList<CredentialInfo> getAllCredentials()
```

### Returns

| Type      | Description              |
|-----------|--------------------------|
| ArrayList | All stored credentials   |


## 4. deleteCredentials

### Description

Delete stored credentials matching the given list of IDs.

### Declaration

```java
void deleteCredentials(List<String> identifiers)
```

### Parameters

| Parameter   | Type | Description              |
|-------------|------|--------------------------|
| identifiers | List | List of credential IDs   |

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

| Parameter           | Type                       | Description            |
|---------------------|----------------------------|------------------------|
| proverDid           | String                     | Prover DID             |
| credentialPublicKey | CredentialPrimaryPublicKey | Credential Public Key  |
| credOffer           | CredentialOffer            | Credential Offer       |

### Returns

| Type                       | Description                    |
|----------------------------|--------------------------------|
| CredentialRequestContainer | Credential Request Container   |


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

| Parameter                  | Type                       | Description                            |
|----------------------------|----------------------------|----------------------------------------|
| credentialRequestMeta      | CredentialRequestMeta      | Blinded Master Secret Data, nonce      |
| credentialPrimaryPublicKey | CredentialPrimaryPublicKey | Credential Public Key                  |
| credential                 | Credential                 | Credential Object                      |

### Returns

| Type    | Description         |
|---------|---------------------|
| boolean | Verification result |


## 9. searchCredentials

### Description

Search credentials matching the ProofRequest and generate a list of available Referents.

### Declaration

```java
ZkpSearchCredentialBuilder searchCredentials(ProofRequest proofRequest)
```

### Parameters

| Parameter    | Type         | Description         |
|--------------|--------------|---------------------|
| proofRequest | ProofRequest | ZKP Proof Request   |

### Returns

| Type                       | Description                |
|----------------------------|----------------------------|
| ZkpSearchCredentialBuilder | Credential Search Builder  |


## 10. createReferent

### Description

Create Referents for each credential based on the user-selected referent information.

### Declaration

```java
ZkpCreateReferentBuilder createReferent(List<UserReferent> customReferents)
```

### Parameters

| Parameter       | Type | Description               |
|------------------|------|---------------------------|
| customReferents | List | User Selected Referents   |

### Returns

| Type                     | Description         |
|--------------------------|---------------------|
| ZkpCreateReferentBuilder | Referent Builder    |


## 11. createProof

### Description

Generate a ZKP proof based on the user's credentials and referents.

### Declaration

```java
Proof createProof(ProofRequest proofRequest, List<ProofParam> proofParams, Map<String, String> selfAttributes)
```

### Parameters

| Parameter      | Type                 | Description                  |
|----------------|----------------------|------------------------------|
| proofRequest   | ProofRequest         | ZKP Proof Request            |
| proofParams    | List                 | Referent Mapping Parameters  |
| selfAttributes | Map<String, String> | Self-Attested Attributes     |

### Returns

| Type  | Description |
|--------|-------------|
| Proof | Proof        |