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
- 주제: ZKPManager
- 작성: 김은정
- 일자: 2026-10-28
- 버전: v3.0.0

| 버전     | 일자         | 변경 내용 |
| ------ | ---------- | ----- |
| v3.0.0 | 2026-10-30 | Referent 속성을 referent key 로 보관 |
| v1.0.0 | 2025-04-30 | 최초 작성 |

# 목차

- [APIs](#api-목록)
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

# API 목록

## 1. constructor

### Description

ZKPManager 자바 생성자.

### Declaration

```java
ZKPManager(String fileName, Context context);
```

### Parameters

| Parameter | Type    | Description     | **M/O** | **비고**  |
| --------- | ------- | --------------- | ------- | -------- |
| fileName  | String  | 저장 파일명        | M       |          |
| context   | Context | Android context | M       |          |

### Returns

| Type       | Description   | **M/O** | **비고** |
| ---------- | ------------- | ------- | ------ |
| ZKPManager | ZKPManager 객체 | M       |        |



## 2. getCredentials

### Description

지정한 credential ID를 기능으로 검색 및 가져오기

### Declaration

```java
List<CredentialInfo> getCredentials(List<String> identifiers)
```

### Parameters

| Parameter   | Type | Description       | **M/O** | **비고**  |
| ----------- | ---- | ----------------- | ------- | ------  |
| identifiers | List | Credential ID 목록 |  M      |         |

### Returns

| Type | Description       | **M/O** | **비고**  |
| ---- | ----------------- | ------- | ------  |
| List | 저장된 credential 목록 |  M      |         |

## 3. getAllCredentials

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

## 4. deleteCredentials

### Description

지정한 credential ID 목록을 가지고 저장된 credential 삭제

### Declaration

```java
void deleteCredentials(List<String> identifiers)
```

### Parameters

| Parameter   | Type | Description      | **M/O** | **비고**  |
| ----------- | ---- | ---------------- | ------- | ------  |
| identifiers | List | Credential ID 목록 | M | |

### Returns

void

## 5. deleteAllCredentials

### Description

저장된 모든 credential 삭제

### Declaration

```java
void deleteAllCredentials()
```

### Returns

void


## 6. isAnyCredentialsSaved

### Description

저장된 credential 유무 확인

### Declaration

```java
boolean isAnyCredentialsSaved()
```

### Returns

boolean

## 7. createCredentialRequest

### Description

ZKP 기능의 Credential Request 생성

### Declaration

```java
ZkpRequestCredentialBuilder createCredentialRequest(String proverDid,
                                                    CredentialPrimaryPublicKey credentialPublicKey,
                                                    CredentialOffer credOffer)
```

### Parameters

| Parameter           | Type                       | Description           | **M/O** | **비고** |
| ------------------- | -------------------------- | --------------------- | ------- | -------- |
| proverDid           | String                     | Prover DID            | M       |          |
| credentialPublicKey | CredentialPrimaryPublicKey | Credential Public Key | M       |          |
| credOffer           | CredentialOffer            | Credential Offer      | M       |          |

### Returns

| Type                       | Description                  | **M/O** | **비고** |
| -------------------------- | ---------------------------- | ------- | -------- |
| CredentialRequestContainer | Credential Request Container | M       |          |
|                            |                              |         |          |

## 8. verifyAndStoreCredential

### Description

발금된 credential의 유효성 검증 및 저장

### Declaration

```java
boolean verifyAndStoreCredential(CredentialRequestMeta credentialRequestMeta,
                                  CredentialPrimaryPublicKey credentialPrimaryPublicKey,
                                  Credential credential)
```

### Parameters

| Parameter                  | Type                       | Description                       | **M/O** | **비고** |
| -------------------------- | -------------------------- | --------------------------------- | ------- | -------- |
| CredentialRequestMeta      | CredentialRequestMeta      | Blinded Master Secret Data, nonce | M       |          |
| credentialPrimaryPublicKey | CredentialPrimaryPublicKey | Credential Public Key             | M       |          |
| credential                 | Credential                 | Credential Object                 | M       |          |
|                            |                            |                                   |         |          |


### Returns

| Type    | Description         | **M/O** | **비고** |
| ------- | ------------------- | ------- | -------- |
| boolean | Verification result | M       |          |
|         |                     |         |          |

## 9. searchCredentials

### Description

ProofRequest에 일치하는 credential을 검색하고 사용 가능한 Referent 목록 만들기

### Declaration

```java
ZkpSearchCredentialBuilder searchCredentials(ProofRequest proofRequest)
```

### Parameters

| Parameter    | Type         | Description       | **M/O** | **비고** |
| ------------ | ------------ | ----------------- | ------- | -------- |
| proofRequest | ProofRequest | ZKP Proof Request | M       |          |
|              |              |                   |         |          |

### Returns

| Type                       | Description               | **M/O** | **비고** |
| -------------------------- | ------------------------- | ------- | -------- |
| ZkpSearchCredentialBuilder | Credential Search Builder | M       |          |
|                            |                           |         |          |

## 10. createReferent

### Description

사용자가 선택한 Referent 정보를 기반으로 각 credential에 대한 Referent 생성
각 속성은 referent key 로 보관하고, 속성 이름은 `ReferentAttributeValue` 에 담는다. 같은 credential 에 같은 referent key 가 두 번 오면 `MSDKWLT04603` 을 던진다.

### Declaration

```java
ReferentInfo createReferent(List<UserReferent> customReferents) throws WalletCoreException, UtilityException
```

### Parameters

| Parameter       | Type | Description             | **M/O** | **비고** |
| --------------- | ---- | ----------------------- | ------- | -------- |
| customReferents | List | User Selected Referents | M       |          |
|                 |      |                         |         |          |

### Returns

| Type                     | Description      | **M/O** | **비고** |
| ------------------------ | ---------------- | ------- | -------- |
| ReferentInfo             | Referent 정보    | M       |          |
|                          |                  |         |          |

## 11. createProof

### Description

사용자의 credential과 referent을 기반으로 ZKP proof 생성
요청된 모든 attribute · predicate 는 referent key 별로 정확히 한 번 선택되어야 하며, 이름이 proof request 와 같고 credential 에 있어야 한다. 아니면 `MSDKWLT04500` · `MSDKWLT04501` · `MSDKWLT04603` · `MSDKWLT04000`(알 수 없는 referent key) 을 던진다.

### Declaration

```java
Proof createProof(ProofRequest proofRequest, List<ProofParam> proofParams, Map<String, String> selfAttributes)
```

### Parameters

| Parameter      | Type                | Description                 | **M/O** | **비고** |
| -------------- | ------------------- | --------------------------- | ------- | -------- |
| proofRequest   | ProofRequest        | ZKP Proof Request           | M       |          |
| proofParams    | List                | Referent Mapping Parameters | M       |          |
| selfAttributes | Map<String, String> | Self-Attested Attributes    | M       |          |

### Returns

| Type  | Description | **M/O** | **비고** |
| ----- | ----------- | ------- | -------- |
| Proof | Proof       | M       |          |
