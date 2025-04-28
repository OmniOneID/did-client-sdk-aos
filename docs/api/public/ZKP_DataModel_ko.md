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

Android ZKP DataModel
==

- 주제: ZKP 기반 데이터모델
- 작성: 박동준
- 일자: 2025-04-30
- 버전: v1.0.0

| 버전     | 일자         | 변경 내용 |
| ------ | ---------- | ----- |
| v1.0.0 | 2025-04-30 | 초안 |


<div style="page-break-after: always;"></div>

# 목차

- [1. CredentialRequest](#1-credentialrequest)
    - [1.1. BlindedCredentialSecrets](#11-blindedcredentialsecrets)
    - [1.2. BlindedCredentialSecretsCorrectnessProof](#12-blindedcredentialsecretscorrectnessproof)
- [2. Credential](#2-credential)
    - [2.1. CredentialSignature](#21-credentialsignature)
    - [2.2. SignatureCorrectnessProof](#22-signaturecorrectnessproof)
- [3. ReferentInfo](#3-referentinfo)
- [4. AvailableReferent](#4-availablereferent)
    - [4.1. AttrReferent](#41-attrreferent)
    - [4.2. PredicateReferent](#42-predicatereferent)
    - [4.3. SubReferent](#43-subreferent)
- [5. Proof](#5-proof)
    - [5.1. SubProof](#51-subproof)
        - [5.1.1. PrimaryProof](#511-primaryproof)
    - [5.2. AggregatedProof](#52-aggregatedproof)
    - [5.3. RequestedProof](#53-requestedproof)
    - [5.4. Identifiers](#54-identifiers)
---

<br>

## 1. CredentialRequest

### Description

`ZKP 기반 Credential 발급 요청 객체`

### Declaration

```java
public class CredentialRequest {
    String proverDid;
    String credDefId;
    BigInteger nonce;
    BlindedCredentialSecrets blindedMs;
    BlindedCredentialSecretsCorrectnessProof blindedMsCorrectnessProof;
}
```

### Property

| Name                      | Type                                     | Description              | **M/O** | **Note** |
| ------------------------- | ---------------------------------------- | ------------------------ | ------- | -------- |
| proverDid                 | String                                   | Prover의 DID              | M       |          |
| credDefId                 | String                                   | Credential Definition ID | M       |          |
| nonce                     | BigInteger                               | 인증용 nonce                | M       |          |
| blindedMs                 | BlindedCredentialSecrets                 | Blind된 Master Secret     | M       |          |
| blindedMsCorrectnessProof | BlindedCredentialSecretsCorrectnessProof | 블라인딩 정당성 증명 객체           | M       |          |
<br>


### 1.1. BlindedCredentialSecrets

#### Description

`블라인드 서명을 사용하여 발급자가 Credential을 서명할 때, 발급자가 특정 속성을 알지 못하도록 숨기는 값`

#### Declaration

```java
public class BlindedCredentialSecrets {
    String u;
    List<String> hiddenAttributes;
}
```

#### Property

| Name                | Type                     | Description          | **M/O** | **Note** |
| ------------------- | ------------------------ | -------------------- | ------- | -------- |
| u                   | String                   | Blind된 Master Secret | M       |          |
| hiddenAttributes    | List                     | 숨겨진 속성 이름 목록      | M       |          |

<br>

### 1.2. BlindedCredentialSecretsCorrectnessProof

#### Description

`BlindedCredentialSecrets의 정당성 증명`

#### Declaration

```java
public class BlindedCredentialSecretsCorrectnessProof {
    BigInteger c;
    BigInteger vDashCap;
    Map<String, BigInteger> mCaps;
    Map<String, BigInteger> rCaps;
}
```

#### Property

| Name     | Type                     | Description | **M/O** | **Note** |
| -------- | ------------------------ | ----------- | ------- | -------- |
| c        | BigInteger               | Challenge 값 | M       |          |
| vDashCap | BigInteger               | 블라인딩 검증 값   | M       |          |
| mCaps    | Map<String, BigInteger> | m 값에 대한 대응값 | M       |          |
| rCaps    | Map<String, BigInteger> | r 값에 대한 대응값 | M       |          |

<br>

## 2. Credential

### Description

`발급 받은 Credential 객체`

### Declaration

```java
public class Credential {
    String credentialId;
    String schemaId;
    String credDefId;
    Map<String, CredentialValue> values;
    CredentialSignature signature;
    SignatureCorrectnessProof signatureCorrectnessProof;
}
```

### Property

| Name                      | Type                          | Description              | **M/O** | **Note** |
| ------------------------- | ----------------------------- | ------------------------ | ------- | -------- |
| credentialId              | String                        | Credential 고유 식별자        | M       |          |
| schemaId                  | String                        | Schema 식별자               | M       |          |
| credDefId                 | String                        | Credential Definition ID | M       |          |
| values                    | Map<String, CredentialValue> | 속성 이름-값 쌍                | M       |          |
| signature                 | CredentialSignature           | Credential 서명 정보         | M       |          |
| signatureCorrectnessProof | SignatureCorrectnessProof     | 서명의 정당성 증명 정보            | M       |          |

<br>

### 2.1. CredentialSignature

#### Description

`Credential(자격 증명)의 서명(Signature) 정보를 포함`

#### Declaration

```java
public class CredentialSignature {
    PrimaryCredentialSignature pCredential;
}
```

#### Property

| Name        | Type                       | Description                  | **M/O** | **Note** |
| ----------- | -------------------------- | ---------------------------- | ------- | -------- |
| pCredential | PrimaryCredentialSignature | "Primary 각 서명 값과 데이터 | O       |          |

<br>

### 2.2. SignatureCorrectnessProof

#### Description

`서명(Signature)의 무결성을 검증하는 데 사용되는 SignatureCorrectnessProof 데이터`

#### Declaration

```java
public class SignatureCorrectnessProof {
    BigInteger se;
    BigInteger c;
}
```

#### Property

| Name | Type       | Description                                | **M/O** | **Note** |
| ---- | ---------- | ------------------------------------------ | ------- | -------- |
| se   | BigInteger | 서명이 변조되지 않았음을 증명하는 해시 값.           | O       |          |
| c    | BigInteger | 정합성을 검증하기 위한 보조 해시 값.               | O       |          |

<br>


## 3. ReferentInfo

### Description

`사용자가 Proof 생성을 위해 선택한 Referent 정보`

### Declaration

```java
public class ReferentInfo {
    Map<String, Referent> referents;
}
```

### Property

| Name      | Type                   | Description            | **M/O** | **Note** |
| --------- | ---------------------- | ---------------------- | ------- | -------- |
| referents | Map<String, Referent> | credentialId 기반의 참조 정보 | M       |          |

<br>

## 4. AvailableReferent

### Description

`Holder가 보유한 Credential 중 Proof 생성에 사용 가능한 속성 정보`

### Declaration

```java
public class AvailableReferent {
    Map<String, AttrReferent> selfAttrReferent;
    Map<String, AttrReferent> attrReferent;
    Map<String, PredicateReferent> predicateReferent;
}
```

### Property

| Name              | Type                            | Description  | **M/O** | **Note** |
| ----------------- | ------------------------------- | ------------ | ------- | -------- |
| selfAttrReferent  | Map<String, AttrReferent>      | 자가 증명 속성     | O       |          |
| attrReferent      | Map<String, AttrReferent>      | 제출 가능한 일반 속성 | O       |          |
| predicateReferent | Map<String, PredicateReferent> | 조건 기반 속성     | O       |          |

<br>

### 4.1. AttrReferent

#### Description

`증명 생성 과정에서 속성과 관련된 정보를 표현한 객체. 특정 속성을 선택적으로 공개/비공개하여 검증자에게 증명`

#### Declaration

```java
public class AttrReferent {
    String name;
    boolean checkRevealed;
    List<SubReferent> attrSubReferent;
}
```

#### Property

| Name              | Type                            | Description  | **M/O** | **Note** |
| ----------------- | ------------------------------- | ------------ | ------- | -------- |
| name              | String                          | 어트리뷰트 이름  | O       |          |
| checkRevealed     | boolean                         | 공개/비공개 유무 | O       |          |
| attrSubReferent   | List<SubReferent>               | SubReferent  | O       |          |


### 4.2. PredicateReferent

#### Description

`속성 값에 대한 특정 조건을 검증할때 사용`

#### Declaration

```java
public class PredicateReferent {
    String name;
    boolean checkRevealed;
    List<SubReferent> attrSubReferent;
}
```

#### Property

| Name              | Type                            | Description  | **M/O** | **Note** |
| ----------------- | ------------------------------- | ------------ | ------- | -------- |
| name              | String                          | 어트리뷰트 이름  | O       |          |
| checkRevealed     | boolean                         | 공개/비공개 유무 | O       |          |
| attrSubReferent   | List<SubReferent>               | SubReferent  | O       |          |



### 4.3. SubReferent

#### Description

`특정 속성과 관련된 정보를 담음`

#### Declaration

```java
public class SubReferent {
    String raw;
    String credentialId;
    String credentialDefId;
}
```

#### Property

| Name              | Type                    | Description  | **M/O** | **Note** |
| ----------------- | ----------------------- | ------------ | ------- | -------- |
| raw               | String                  |  속성의 원본 값  | O       |          |
| credentialId      | String                  | 속성이 포함된 credential ID | O       |          |
| credentialDefId   | String                  | 발급자가 특정 스키마에 대한 증명서 정의            | O       |          |



## 5. Proof

### Description

`ZKP 기반으로 생성된 최종 증명 객체`

### Declaration

```java
public class Proof {
    List<SubProof> proofs;
    AggregatedProof aggregatedProof;
    RequestedProof requestedProof;
    List<Identifiers> identifiers;
}
```

### Property

| Name            | Type              | Description             | **M/O** | **Note** |
| --------------- | ----------------- | ----------------------- | ------- | -------- |
| proofs          | List<SubProof>    | 서브 증명 리스트            | M       |          |
| aggregatedProof | AggregatedProof   | 집계된 ZKP 증명 값         | M       |          |
| requestedProof  | RequestedProof    | 요청된 속성에 대응하는 증명    | M       |          |
| identifiers     | List<Identifiers> | 사용된 Credential 식별 정보 | M       |          |

<br>

### 5.1. SubProof

#### Description

`기본 증명 정보를 포함`

#### Declaration

```java
public class SubProof {
    PrimaryProof primaryProof;
}
```

#### Property

| Name              | Type                              | Description | **M/O** | **Note** |
| ----------------- | --------------------------------- | ----------- | ------- | -------- |
| primaryProof      | PrimaryProof                      | 기본 증명     | O       |          |

<br>

### 5.2. AggregatedProof

#### Description

`여러 증명을 하나의 ZKP로 집계한 구조`

#### Declaration

```java
public class AggregatedProof {
    BigInteger cHash;
    List<String> cList;
}
```

#### Property

| Name  | Type       | Description            | **M/O** | **Note** |
| ----- | ---------- | ---------------------- | ------- | -------- |
| cHash | BigInteger | 집계된 challenge 값        | M       |          |
| cList | List       | 서브 증명 리스트의 challenge 값 | M       |          |

<br>

### 5.3. RequestedProof

#### Description

`증명 시 요청된 속성에 대해 실제 제출한 속성들을 분류`

#### Declaration

```java
public class RequestedProof {
    Map<String, String> selfAttestedAttrs;
    Map<String, Map<String, String>> revealedAttrs;
    Map<String, Map<String, String>> unrevealedAttrs;
    Map<String, Map<String, String>> predicates;
}
```

#### Property

| Name              | Type                             | Description | **M/O** | **Note** |
| ----------------- | ---------------------------------| ----------- | ------- | -------- |
| selfAttestedAttrs | Map<String, String>              | 자가 증명 속성    | O       |          |
| revealedAttrs     | Map<String, Map<String,String>> | 공개된 속성      | O       |          |
| unrevealedAttrs   | Map<String, Map<String,String>> | 비공개 속성      | O       |          |
| predicates        | Map<String, Map<String,String>> | 조건 기반 속성 증명 | O       |          |

<br>

### 5.4. Identifiers

#### Description

`증명을 만들기 위해 어떤 식별정보가 사용되었는지 포함`

#### Declaration

```java
public class Identifiers {
    String schemaId;
    String credDefId;
}
```

#### Property

| Name      | Type   | Description                  | **M/O** | **Note** |
| --------- | ------ | ---------------------------- | ------- | -------- |
| schemaId  | String | 참조된 Credential Schema ID     | M       |          |
| credDefId | String | 참조된 Credential Definition ID | M       |          |

<br>
