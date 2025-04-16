# Android ZKPManager Core SDK API

- 투지: ZKPManager
- 작성: Dongjun Park
- 일자: 2025-04-07
- 버전: v1.0.0

| 버전     | 일자         | 변경 내용 |
| ------ | ---------- | ----- |
| v1.0.0 | 2025-04-07 | 최초 작성 |

# 목차

- [APIs](#api-목록)
  - [1. constructor](#1-constructor)
  - [2. addCredentials](#3-addcredentials)
  - [3. getCredentials](#4-getcredentials)
  - [4. getAllCredentials](#5-getallcredentials)
  - [5. deleteCredentials](#6-deletecredentials)
  - [6. deleteAllCredentials](#7-deleteallcredentials)
  - [7. createCredentialRequest](#8-createcredentialrequest)
  - [8. verifyAndStoreCredential](#9-verifyandstorecredential)
  - [9. searchCredentials](#10-searchcredentials)
  - [10. createReferent](#11-createreferent)
  - [11. createProof](#12-createproof)

# API 목록

## 1. constructor

### Description

ZKPManager 자바 생성자.

### Declaration

```java
ZKPManager(Context context);
ZKPManager(String fileName, Context context);
```

### Parameters

| Parameter | Type    | Description     | **M/O** | **비고**   |
| --------- | ------- | --------------- | ------- | -------- |
| fileName  | String  | 저장 파일명           | O       |  |
| context   | Context | Android context | M       |          |

### Returns

| Type       | Description   | **M/O** | **비고** |
| ---------- | ------------- | ------- | ------ |
| ZKPManager | ZKPManager 객체 | M       |        |


## 2. addCredentials

### Description

지정한 Credential 정보를 저장.

### Declaration

```java
void addCredentials(Credential credential, String credentialId)
```

### Parameters

| Parameter    | Type       | Description       |
| ------------ | ---------- | ----------------- |
| credential   | Credential | 저장할 credential 객체 |
| credentialId | String     | 특정 credential의 ID |

### Returns

void

## 3. getCredentials

### Description

지정한 credential ID를 기능으로 검색 및 가져오기

### Declaration

```java
List<CredentialInfo> getCredentials(List<String> identifiers)
```

### Parameters

| Parameter   | Type | Description      |
| ----------- | ---- | ---------------- |
| identifiers | List | Credential ID 목록 |

### Returns

| Type | Description       |
| ---- | ----------------- |
| List | 저장된 credential 목록 |

## 4. getAllCredentials

### Description

저장된 모든 credential 목록 가져오기

### Declaration

```java
ArrayList<CredentialInfo> getAllCredentials()
```

### Returns

| Type      | Description      |
| --------- | ---------------- |
| ArrayList | 모든 credential 목록 |

## 5. deleteCredentials

### Description

지정한 credential ID 목록을 가지고 저장된 credential 삭제

### Declaration

```java
void deleteCredentials(List<String> identifiers)
```

### Parameters

| Parameter   | Type | Description      |
| ----------- | ---- | ---------------- |
| identifiers | List | Credential ID 목록 |

### Returns

void

## 6. deleteAllCredentials

### Description

저장된 모든 credential 삭제

### Declaration

```java
void deleteAllCredentials()
```

### Returns

void

## 7. createCredentialRequest

### Description

ZKP 기능의 Credential Request 생성

### Declaration

```java
ZkpRequestCredentialBuilder createCredentialRequest(String proverDid,
                                                    CredentialPrimaryPublicKey credentialPublicKey,
                                                    CredentialOffer credOffer,
                                                    LinkedHashMap<String, String> credentialValueMap,
                                                    BigInteger proverNonce)
```

### Parameters

| Parameter           | Type                           | Description           |
| ------------------- | ------------------------------ | --------------------- |
| proverDid           | String                         | Prover DID            |
| credentialPublicKey | CredentialPrimaryPublicKey     | Credential Public Key |
| credOffer           | CredentialOffer                | Credential Offer      |
| credentialValueMap  | LinkedHashMap<String, String> | Attribute Value Map   |
| proverNonce         | BigInteger                     | Nonce for prover      |

### Returns

| Type                        | Description                |
| --------------------------- | -------------------------- |
| ZkpRequestCredentialBuilder | Credential Request Builder |

## 8. verifyAndStoreCredential

### Description

발금된 credential의 유효성 검증 및 저장

### Declaration

```java
boolean verifyAndStoreCredential(MasterSecretBlindingData masterSecretBlindingData,
                                  CredentialPrimaryPublicKey credentialPrimaryPublicKey,
                                  String credentialId,
                                  Credential credential,
                                  BigInteger proverNonce)
```

### Parameters

| Parameter                  | Type                       | Description                |
| -------------------------- | -------------------------- | -------------------------- |
| masterSecretBlindingData   | MasterSecretBlindingData   | Blinded Master Secret Data |
| credentialPrimaryPublicKey | CredentialPrimaryPublicKey | Credential Public Key      |
| credentialId               | String                     | Credential ID              |
| credential                 | Credential                 | Credential Object          |
| proverNonce                | BigInteger                 | Nonce for prover           |

### Returns

| Type    | Description         |
| ------- | ------------------- |
| boolean | Verification result |

## 9. searchCredentials

### Description

ProofRequest에 일치하는 credential을 검색하고 사용 가능한 Referent 목록 만들기

### Declaration

```java
ZkpSearchCredentialBuilder searchCredentials(ProofRequest proofRequest)
```

### Parameters

| Parameter    | Type         | Description       |
| ------------ | ------------ | ----------------- |
| proofRequest | ProofRequest | ZKP Proof Request |

### Returns

| Type                       | Description               |
| -------------------------- | ------------------------- |
| ZkpSearchCredentialBuilder | Credential Search Builder |

## 10. createReferent

### Description

사용자가 선택한 Referent 정보를 기반으로 각 credential에 대한 Referent 만들기

### Declaration

```java
ZkpCreateReferentBuilder createReferent(List<UserReferent> customReferents)
```

### Parameters

| Parameter       | Type | Description             |
| --------------- | ---- | ----------------------- |
| customReferents | List | User Selected Referents |

### Returns

| Type                     | Description      |
| ------------------------ | ---------------- |
| ZkpCreateReferentBuilder | Referent Builder |

## 11. createProof

### Description

사용자의 credential과 referent을 기반으로 ZKP proof 생성

### Declaration

```java
ZkpCreateProofBuilder createProof(ProofRequest proofRequest,
                                  List<ProofParam> proofParams,
                                  Map<String, String> selfAttributes)
```

### Parameters

| Parameter      | Type                 | Description                 |
| -------------- | -------------------- | --------------------------- |
| proofRequest   | ProofRequest         | ZKP Proof Request           |
| proofParams    | List                 | Referent Mapping Parameters |
| selfAttributes | Map<String, String> | Self-Attested Attributes    |

### Returns

| Type                  | Description   |
| --------------------- | ------------- |
| ZkpCreateProofBuilder | Proof Builder |
