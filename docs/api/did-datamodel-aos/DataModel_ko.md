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

Android DataModel SDK
==

- 주제: DataModel
- 작성: Sangjun Kim
- 일자: 2024-08-28
- 버전: v1.0.0

| 버전   | 일자       | 변경 내용                 |
| ------ | ---------- | -------------------------|
| v1.0.0 | 2024-08-28 | 초기 작성                 |


<div style="page-break-after: always;"></div>


# 목차
- [CoreVo](#corevo)
    - [1. DIDDocument](#1-diddocument)
        - [1.1. VerificationMethod](#11-verificationmethod)
        - [1.2. Service](#12-service)
    - [2. VerifiableCredential](#2-verifiablecredential)
        - [2.1. Issuer](#21-issuer)
        - [2.2. Evidence](#22-evidence)
        - [2.3. CredentialSchema](#23-credentialschema)
        - [2.4. CredentialSubject](#24-credentialsubject)
        - [2.5. Claim](#25-claim)
        - [2.6. Internationalization](#26-internationalization)
    - [3. VerifiablePresentation](#3-verifiablepresentation)
    - [4. Proof](#4-proof)
        - [4.1 VCProof](#41-vcproof)
    - [5. Profile](#5-profile)
        - [5.1. IssueProfile](#51-issueprofile)
            - [5.1.1. Profile](#511-profile)
                - [5.1.1.1. CredentialSchema](#5111-credentialschema)
                - [5.1.1.2. Process](#5112-process)
        - [5.2 VerifyProfile](#52-verifyprofile)
            - [5.2.1. Profile](#521-profile)
                - [5.2.1.1. ProfileFilter](#5211-profilefilter)
                    - [5.2.1.1.1. CredentialSchema](#52111-credentialschema)
                - [5.2.1.2. Process](#5212-process)
        - [5.3. LogoImage](#53-logoimage)
        - [5.4. ProviderDetail](#54-providerdetail)
        - [5.5. ReqE2e](#55-reqe2e)
    - [6. VCSchema](#6-vcschema)
        - [6.1. VCMetadata](#61-vcmetadata)
        - [6.2. CredentialSubject](#62-credentialsubject)
            - [6.2.1. Claim](#621-claim)
                - [6.2.1.1. Namespace](#6211-namespace)
                - [6.2.1.2. ClaimDef](#6212-claimdef)
- [SeviceVo](#servicevo)
    - [1. Protocol](#1-protocol)
        - [1.1. BaseRequest](#11-baserequest)
            - [1.1.1. P131RequestVo](#111-p131requestvo)
            - [1.1.2. P132RequestVo](#112-p132requestvo)
            - [1.1.3. P210RequestVo](#113-p210requestvo)
            - [1.1.4. P310RequestVo](#114-p310requestvo)
        - [1.2. BaseResponse](#12-baseresponse)
            - [1.2.1. P131ResponseVo](#121-p131responsevo)
            - [1.2.2. P132ResponseVo](#122-p132responsevo)
            - [1.2.3. P210ResponseVo](#123-p210responsevo)
            - [1.2.4. P310ResponseVo](#124-p310responsevo)
    - [2. Token](#2-token)
        - [2.1. ServerTokenSeed](#21-servertokenseed)
            - [2.1.1. AttestedAppInfo](#211-attestedappinfo)
                - [2.1.1.1. provider](#2111-provider)
            - [2.1.2. SignedWalletInfo](#212-signedwalletinfo)
                - [2.1.2.1. Wallet](#2121-wallet)
        - [2.2. ServerTokenData](#22-servertokendata)
        - [2.3. WalletTokenSeed](#23-wallettokenseed)
        - [2.4. WalletTokenData](#24-wallettokendata)
    - [3. SecurityChannel](#3-securitychannel)
        - [3.1. ReqEcdh](#31-reqecdh)
        - [3.2. AccEcdh](#32-accecdh)
        - [3.3. AccE2e](#33-acce2e)
        - [3.4. E2e](#34-e2e)
        - [3.5. DIDAuth](#35-didauth)
    - [4. DIDDoc](#4-diddoc)
        - [4.1. DidDocVo](#41-diddocvo)
        - [4.2. AttestedDidDoc](#42-attesteddiddoc)
        - [4.3. SignedDidDoc](#43-signeddiddoc)
    - [5. Offer](#5-offer)
        - [5.1. IssueOfferPayload](#51-issueofferpayload)
        - [5.2. VerifyOfferPayload](#52-verifyofferpayload)
    - [6. Issue VC](#6-issue-vc)
        - [6.1. ReqVC](#61-reqvc)
            - [6.1.1. Profile](#611-profile)
        - [6.2. VCPlanList](#62-vcplanlist)
            - [6.2.1. VCPlan](#621-vcplan)
                - [6.2.1.1. Option](#6211-option)
- [Enumerators](#enumerators)
    - [1. DID_KEY_TYPE](#1-did_key_type)
    - [2. DID_SERVICE_TYPE](#2-did_service_type)
    - [3. PROOF_PURPOSE](#3-proof_purpose)
    - [4. PROOF_TYPE](#4-proof_type)
    - [5. AUTH_TYPE](#5-auth_type)
    - [6. EVIDENCE_TYPE](#6-evidence_type)
    - [7. PRESENCE](#7-presence)
    - [8. PROFILE_TYPE](#8-profile_type)
    - [9. LOGO_IMAGE_TYPE](#9-logo_image_type)
    - [10. CLAIM_TYPE](#10-claim_type)
    - [11. CLAIM_FORMAT](#11-claim_format)
    - [12. LOCATION](#12-location)
    - [13. SYMMETRIC_PADDING_TYPE](#13-symmetric_padding_type)
    - [14. SYMMETRIC_CIPHER_TYPE](#14-symmetric_cipher_type)
    - [15. ALGORITHM_TYPE](#15-algorithm_type)
    - [16. CREDENTIAL_SCHEMA_TYPE](#16-credential_schema_type)
    - [17. ELLIPTIC_CURVE_TYPE](#17-elliptic_curve_type)
    - [18. VERIFY_AUTH_TYPE](#18-verify_auth_type)
    - [19. ROLE_TYPE](#19-role_type)
    - [20. SERVER_TOKEN_PURPOSE](#20-server_token_purpose)
    - [21. WALLET_TOKEN_PURPOSE](#21-wallet_token_purpose)
- [Apis](#apis)
    - [1. deserialize](#1-deserialize)
    - [2. convertFrom](#2-convertfrom)
    - [3. convertTo](#3-convertto)

<br>

# CoreVo

## 1. CredentialRequest

### Description

`탈중앙 식별자를 위한 문서`

### Declaration

```java
public class CredentialRequest {

    private String proverDID;
    
    private String credDefId;
    
    private BigInteger nonce;
    
    private BlindedCredentialSecrets blindedMs;

    private BlindedCredentialSecretsCorrectnessProof blindedMsCorrectnessProof;
}
```

### Property

| Name                 | Type                 | Description                            | **M/O** | **Note**                    |
|----------------------|----------------------|----------------------------------------|---------|-----------------------------|
| proverDID            | String               | 소유자 DID                               |    M    |                             | 
| credDefId            | String               |                                        |    M    |                             | 
| nonce                | BigInteger           | DID controller의 did                |    M    |   | 
| blindedMs            | BlindedCredentialSecrets               | 공개키가 포함된 DID 키 목록                   |    M    |          | 
| blindedMsCorrectnessProof      | BlindedCredentialSecretsCorrectnessProof             | Assertion 키 이름 목록             |    O    |   | 
| authentication       | List\<String>             | Authentication 키 이름 목록         |    O    |   | 
| keyAgreement         | List\<String>             | Key Agreement 키 이름 목록         |    O    |   | 
| capabilityInvocation | List\<String>             | Capability Invocation 키 이름 목록  |    O    |   | 
| capabilityDelegation | List\<String>             | Capability Delegation 키 이름 목록  |    O    |   | 
| service              | List\<Service>            | 서비스 목록                        |    O    |[Service](#12-service)  | 
| created              |  String              | 생성 시간                            |    M    | | 
| updated              |  String              | 갱신 시간                            |    M    | | 
| versionId            |  String              | DID 버전 id                         |    M    | | 
| deactivated          |  Bool                | True: 비활성화, False: 활성화            |    M    |   | 
| proof                |  Proof               | 소유자 proof                            |    O    |[Proof](#4-proof)| 
| proofs               |  [Proof]             | 소유자 proof 목록                    |    O    |[Proof](#4-proof)| 

<br>

## 1.1. VerificationMethod

### Description

`공개키가 포함된 DID 키 목록`

### Declaration

```java
public class VerificationMethod {
    String id;
    DIDKeyType.DID_KEY_TYPE type;
    String controller;
    String publicKeyMultibase;
    AuthType.AUTH_TYPE authType;
    String status;
}
```

### Property

| Name               | Type       | Description                            | **M/O** | **Note**              |
|--------------------|------------|----------------------------------------|---------|-----------------------|
| id                 | String     | 키 이름                               |    M    |                       | 
| type               | DID_KEY_TYPE | 키 종류                              |    M    | [DID_KEY_TYPE](#1-did_key_type) | 
| controller         | String     | 키 controller의 DID                   |    M    |                       | 
| publicKeyMultibase | String     | 공개키                       |    M    | 멀티베이스 인코딩됨  | 
| authType           | AUTH_TYPE   | 키 사용을 위한 인증 방법     |    M    | [AUTH_TYPE](#5-auth_type) | 

<br>

## 1.2. Service

### Description

`서비스 목록`

### Declaration

```java
public class Service {
    String id;
    DIDServiceType.DID_SERVICE_TYPE type;
    List<String> serviceEndpoint;
}
```

### Property

| Name            | Type           | Description                | **M/O** | **Note**                  |
|-----------------|----------------|----------------------------|---------|---------------------------|
| id              | String         | 서비스 id                 |    M    |                           | 
| type            | DID_SERVICE_TYPE | 서비스 종류               |    M    | [DID_SERVICE_TYPE](#2-did_service_type)| 
| serviceEndpoint | List\<String>       | 서비스로의 URL 목록 |    M    |                           | 

<br>

## 2. VerifiableCredential

### Description

`탈중앙화된 전자 인증서, 이하 VC`

### Declaration

```java
public class VerifiableCredential {

    @SerializedName("@context")
    List<String> context;
    String id;
    List<String> type;
    Issuer issuer;
    String issuanceDate;
    String validFrom;
    String validUntil;
    String encoding;
    String formatVersion;
    String language;
    List<Evidence> evidence;
    CredentialSchema credentialSchema;
    CredentialSubject credentialSubject;
    VCProof proof;
}
```

### Property

| Name              | Type              | Description                      | **M/O** | **Note**                    |
|-------------------|-------------------|----------------------------------|---------|-----------------------------|
| context           | List\<String>          | JSON-LD context                  |    M    |                             |
| id                | String            | VC id                            |    M    |                             |
| type              | List\<String>          | VC 종류 목록                 |    M    |                             |
| issuer            | Issuer            | 발급처 정보               |    M    | [Issuer](#21-issuer)         |
| issuanceDate      | String            | 발급 시간                |    M    |                             |
| validFrom         | String            | VC의 유효 시작 시간  |    M    |                             |
| validUntil        | String            | VC의 만료 시간 |    M    |                             |
| encoding          | String            | VC 인코딩 종류                 |    M    | Default(UTF-8)              |
| formatVersion     | String            | VC 포맷 버전                |    M    |                             |
| language          | String            | VC 언어 코드                 |    M    |                             |
| evidence          | List\<Evidence>        | 증거                           |    M    | [Evidence](#22-evidence) |
| credentialSchema  | CredentialSchema  | Credential schema                |    M    | [CredentialSchema](#23-credentialschema)                            |
| credentialSubject | CredentialSubject | Credential subject               |    M    | [CredentialSubject](#24-credentialsubject)                            |
| proof             | VCProof           | 발급처 proof                     |    M    | [VCProof](#41-vcproof)                            |

<br>

## 2.1 Issuer

### Description

`이슈어 정보`

### Declaration

```java
public class Issuer {
    String id;
    String name;
    String certVCRef;
}
```

## Property

| Name      | Type   | Description                       | **M/O** | **Note**                 |
|-----------|--------|-----------------------------------|---------|--------------------------|
| id        | String | 이슈어 DID                      |    M    |                          |
| name      | String | 이슈어 name                     |    O    |                          |
| certVCRef | String | 가입증명서 URL                    |    O    |                          |

<br>

## 2.2 Evidence

### Description

`증거 서류 확인`

### Declaration

```java
public class Evidence {
    String id;
    EvidenceType.EVIDENCE_TYPE type;
    String verifier;
    String evidenceDocument;
    Presence.PRESENCE subjectPresence;
    Presence.PRESENCE documentPresence;
}
```

## Property

| Name             | Type         | Description                      | **M/O** | **Note**                 |
|------------------|--------------|----------------------------------|---------|--------------------------|
| id              | String       | 증거 정보의 URL                      |    O    |                          |
| type             | EVIDENCE_TYPE | 증거 종류                            |    M    | [EVIDENCE_TYPE](#6-evidence_type) |
| verifier         | String       | 증거 검증처                          |    M    |                          |
| evidenceDocument | String       | 증거 문서 이름                           |    M    |                          |
| subjectPresence  | PRESENCE     | 주체 표현 종류                       |    M    | [PRESENCE](#7-presence)        |
| documentPresence | PRESENCE     | 문서 표현 종류                         |    M    | [PRESENCE](#7-presence)   |

<br>

## 2.3 CredentialSchema

### Description

`Credential schema`

### Declaration

```java
public class CredentialSchema {
    String id;
    CredentialSchemaType.CREDENTIAL_SCHEMA_TYPE type;
}
```

## Property

| Name      | Type                 | Description           | **M/O** | **Note**                 |
|-----------|----------------------|-----------------------|---------|--------------------------|
| id        | String               |  VC schema URL    |    M    |                          |
| type      | CREDENTIAL_SCHEMA_TYPE | VC Schema 포맷 종류 |    M    |  [CREDENTIAL_SCHEMA_TYPE](#16-credential_schema_type)   |

<br>

## 2.4 CredentialSubject

### Description

`Credential subject`

### Declaration

```java
public class CredentialSubject {
    String id;
    List<Claim> claims;
}
```

## Property

| Name      | Type    | Description   | **M/O** | **Note**                 |
|-----------|---------|---------------|---------|--------------------------|
| id        | String  | 주체 DID   |    M    |                          |
| claims    | List\<Claim> | Claim 목록 |    M    | [Claim](#25-claim)           |

<br>

## 2.5 Claim

### Description

`주체 정보`

### Declaration

```java
public class Claim {
    String code;
    String caption;
    String value;
    ClaimType.CLAIM_TYPE type;
    ClaimFormat.CLAIM_FORMAT format;
    boolean hideValue;
    Location.LOCATION location;
    String digestSRI;
    Map<String, Internationalization> i18n;
}
```

## Property

| Name      | Type                         | Description                  | **M/O** | **Note**                 |
|-----------|------------------------------|------------------------------|---------|--------------------------|
| code      | String                       | Claim 코드                   |    M    |                          |
| caption   | String                       | Claim 이름                   |    M    |                          |
| value     | String                       | Claim 값                  |    M    |                          |
| type      | CLAIM_TYPE                    | Claim 종류                   |    M    | [CLAIM_TYPE](#11-claim_type)         |
| format    | ClaimFormat                  | Claim 포맷                 |    M    | [CLAIM_FORMAT](#12-claim_format)           |
| hideValue | Bool                         | 값 숨김                   |    O    | Default(false)           |
| location  | LOCATION                     | 값 위치                   |    O    | Default(inline) <br> [LOCATION](#12-location) |
| digestSRI | String                       | Digest Subresource Integrity |    O    |                          |
| i18n      |Map\<String, Internationalization> | 국제화                  |    O    | [Internationalization](#26-internationalization) |

<br>

## 2.6 Internationalization

### Description

`국제화`

### Declaration

```java
public class Internationalization {
    String caption;
    String value;
    String digestSRI;
}
```

## Property

| Name      | Type   | Description                  | **M/O** | **Note**                 |
|-----------|--------|------------------------------|---------|--------------------------|
| caption   | String | Claim 이름                   |    M    |                          |
| value     | String | Claim 값                  |    O    |                          |
| digestSRI | String | Digest Subresource Integrity |    O    | Claim 값의 해시값      |

<br>

## 3. VerifiablePresentation

### Description

`주체 서명된 VC 목록, 이하 VP`

### Declaration

```java
public class VerifiablePresentation {

    @SerializedName("@context")
    List<String> context;
    String id;
    List<String> type;
    String holder;
    String validFrom;
    String validUntil;
    String verifierNonce;
    List<VerifiableCredential> verifiableCredential;
    Proof proof;
    List<Proof> proofs;
}
```

### Property

| Name                 | Type                   | Description                      | **M/O** | **Note**                 |
|----------------------|------------------------|----------------------------------|---------|--------------------------|
| context              | List\<String>               | JSON-LD context                  |    M    |                          |
| id                   | String                 | VP ID                            |    M    |                          |
| type                 | List\<String>               | VP 종류 목록                  |    M    |                          |
| holder               | String                 | 소유자 DID                       |    M    |                          |
| validFrom            | String                 | VP의 유효 시작 시간            |    M    |                          |
| validUntil           | String                 | VP의 만료 시간                 |    M    |                          |
| verifierNonce        | String                 | 검증자 nonce                   |    M    |                          |
| verifiableCredential | List\<VerifiableCredential> | VC 목록                       |    M    | [VerifiableCredential](#2-verifiablecredential)   |
| proof                | Proof                  | 소유자 proof                      |    O    | [Proof](#4-proof) | 
| proofs               | List\<Proof>                | 소유자 proof 목록             |    O    | [Proof](#4-proof)    | 

<br>

## 4. Proof

### Description

`소유자 proof`

### Declaration

```java
public class Proof {
    String created;
    ProofPurpose.PROOF_PURPOSE proofPurpose;
    String verificationMethod;
    ProofType.PROOF_TYPE type;
    String proofValue;
}
```

### Property

| Name               | Type         | Description                      | **M/O** | **Note**                 |
|--------------------|--------------|----------------------------------|---------|--------------------------|
| created            | String       | 생성시간                 |    M    |    | 
| proofPurpose       | PROOF_PURPOSE | Proof 목적                    |    M    | [PROOF_PURPOSE](#3-proof_purpose) | 
| verificationMethod | String       | Proof 서명에 사용된 Key URL |    M    |                          | 
| type               | PROOF_TYPE    | Proof 종류                       |    O    | [PROOF_TYPE](#4-proof_type)   |
| proofValue         | String       | 서명값                         |    O    |                          |

<br>

## 4.1 VCProof

### Description

`발급처 proof`

### Declaration

```java
public class VCProof extends Proof {
    List<String> proofValueList;
}
```

### Property

| Name               | Type         | Description                      | **M/O** | **Note**                 |
|--------------------|--------------|----------------------------------|---------|--------------------------|
| created            | String       | 생성 시간                |    M    |        | 
| proofPurpose       | PROOF_PURPOSE | Proof 목적                    |    M    | [PROOF_PURPOSE](#3-proof_purpose)  | 
| verificationMethod | String       | Proof 서명에 사용된 Key URL |    M    |                          | 
| type               | PROOF_TYPE    | Proof 종류                       |    O    | [PROOF_TYPE](#4-proof_type)   |
| proofValue         | String       | 서명값                  |    O    |                          |
| proofValueList     | List\<String>     | 서명값 목록             |    O    |                          |

<br>

## 5. Profile

## 5.1 IssueProfile

### Description

`발급 프로파일`

### Declaration

```java
public class IssueProfile {
    String id;
    ProfileType.PROFILE_TYPE type;
    String title;
    String description;
    LogoImage logo;
    String encoding;
    String language;
    Profile profile;
    Proof proof;
}
```

### Property

| Name        | Type        | Description           | **M/O** | **Note**                 |
|-------------|-------------|-----------------------|---------|--------------------------|
| id          | String      | 프로파일 ID            |    M    |                          |
| type        | PROFILE_TYPE | 프로파일 종류          |    M    | [PROFILE_TYPE](#8-profile_type)    |
| title       | String      | 프로파일 타이틀         |    M    |                          |
| description | String      | 프로파일 설명   |    O    |                          |
| logo        | LogoImage   | 로고 이미지            |    O    | [LogoImage](#53-logoimage)         |
| encoding    | String      | 프로파일 인코딩 종류 |    M    |                          |
| language    | String      | 프로파일 언어 코드 |    M    |                          |
| profile     | Profile     | 프로파일 컨텐츠      |    M    | [Profle](#511-profile)           |
| proof       | Proof       | 소유자 proof           |    O    | [Proof](#4-proof)      |

<br>

## 5.1.1 Profile

### Description

`프로파일 컨텐츠`

### Declaration

```java
public class Profile {
    ProviderDetail issuer;
    CredentialSchema credentialSchema;
    Process process;
}
```

### Property

| Name             | Type             | Description           | **M/O** | **Note**                 |
|------------------|------------------|-----------------------|---------|--------------------------|
| issuer           | ProviderDetail   | 발급처 정보    |    M    | [ProviderDetail](#54-providerdetail)   |
| credentialSchema | CredentialSchema | VC schema 정보 |    M    | [CredentialSchema](#5111-credentialschema)          |
| process          | Process          | 발급 절차       |    M    |  [Process](#5112-process)     |

<br>

## 5.1.1.1 CredentialSchema

### Description

`VC schema 정보`

### Declaration

```java
public class CredentialSchema {
    String id;
    CredentialSchemaType.CREDENTIAL_SCHEMA_TYPE type;
    String value;
}
```

### Property

| Name  | Type                 | Description           | **M/O** | **Note**                 |
|-------|----------------------|-----------------------|---------|--------------------------|
| id    | String               | VC schema URL     |    M    |                          |
| type  | CREDENTIAL_SCHEMA_TYPE | VC schema 포맷 종류 |    M    | [CREDENTIAL_SCHEMA_TYPE](#16-credential_schema_type)      |
| value | String               | VC schema             |    O    | 멀티베이스 인코딩됨     |

<br>

## 5.1.1.2 Process

### Description

`발급 절차`

### Declaration

```java
public class Process {
    List<String> endpoints;
    ReqE2e reqE2e;
    String issuerNonce;
}
```

### Property

| Name        | Type     | Description         | **M/O** | **Note**                 |
|-------------|----------|---------------------|---------|--------------------------|
| endpoints   | List\<String> | Endpoint 목록    |    M    |                          |
| reqE2e      | ReqE2e   | 요청 정보 |    M    | Proof 없음 <br> [ReqE2e](#55-reqe2e)     |
| issuerNonce | String   | 발급처 nonce        |    M    |                          |

<br>

## 5.2 VerifyProfile

### Description

`검증 프로파일`

### Declaration

```java
public class VerifyProfile {
    String id;
    ProfileType.PROFILE_TYPE type;
    String title;
    String description;
    LogoImage logo;
    String encoding;
    String language;
    Profile profile;
    Proof proof;
}
```

### Property

| Name        | Type        | Description           | **M/O** | **Note**                 |
|-------------|-------------|-----------------------|---------|--------------------------|
| id          | String      | 프로파일 ID            |    M    |                          |
| type        | PROFILE_TYPE | 프로파일 종류          |    M    | [PROFILE_TYPE](#8-profile_type)         |
| title       | String      | 프로파일 타이틀         |    M    |                          |
| description | String      | 프로파일 설명   |    O    |                          |
| logo        | LogoImage   | 로고 이미지            |    O    |  [LogoImage](#53-logoimage)        |
| encoding    | String      | 프로파일 인코딩 종류 |    M    |                          |
| language    | String      | 프로파일 언어 코드 |    M    |                          |
| profile     | Profile     | 프로파일 컨텐츠      |    M    | [Profile](#521-profile)      |
| proof       | Proof       | 소유자 proof           |    O    | [Proof](#4-proof)       |

<br>

## 5.2.1 Profile

### Description

`프로파일 컨텐츠`

### Declaration

```java
public class Profile {
    ProviderDetail verifier;
    ProfileFilter filter;
    Process process;
}
```

### Property

| Name     | Type           | Description              | **M/O** | **Note**                 |
|----------|----------------|--------------------------|---------|--------------------------|
| verifier | ProviderDetail | 검증자 정보     |    M    |  [ProviderDetail](#54-providerdetail)       |
| filter   | ProfileFilter  | 제출을 위한 필터링 정보 |    M    | [ProfileFilter](#5211-profilefilter)          |
| process  | Process        | VP 제출 방법 |    M    |[Process](#5212-process)       |

<br>

## 5.2.1.1 ProfileFilter

### Description

`제출을 위한 필터링 정보`

### Declaration

```java
public class ProfileFilter {
    List<CredentialSchema> credentialSchemas;
}
```

### Property

| Name              | Type               | Description                                | **M/O** | **Note**                 |
|-------------------|--------------------|--------------------------------------------|---------|--------------------------|
| credentialSchemas | List\<CredentialSchema> | 제출가능한 VC Schema 별 Claim과 발급처           |    M    | [CredentialSchema](#52111-credentialschema)      |

<br>

## 5.2.1.1.1 CredentialSchema

### Description

`제출가능한 VC Schema 별 Claim과 발급처`

### Declaration

```java
 public class CredentialSchema {
    String id;
    CredentialSchemaType.CREDENTIAL_SCHEMA_TYPE type;
    String value;
    List<String> displayClaims;
    List<String> requiredClaims;
    List<String> allowedIssuers;
}
```

### Property

| Name           | Type                 | Description           | **M/O** | **Note**                 |
|----------------|----------------------|-----------------------|---------|--------------------------|
| id             | String               | VC schema URL     |    M    |                          |
| type           | CREDENTIAL_SCHEMA_TYPE | VC schema 포맷 종류 |    M    | [CREDENTIAL_SCHEMA_TYPE](#16-credential_schema_type)    |
| value          | String               | VC schema             |    O    | 멀티베이스 인코딩됨     |
| displayClaims  | List\<String>             | 사용자 화면에 노출될 claims 목록        |    O    |                          |
| requiredClaims | List\<String>             | 필요 claims 목록       |    O    |                          |
| allowedIssuers | List\<String>             | 허용된 발급처의 DID 목록       |    O    |                          |

<br>

## 5.2.1.2 Process

### Description

`Method for VP submission`

### Declaration

```java
public class Process {
    List<String> endpoints;
    ReqE2e reqE2e;
    String verifierNonce;
    VerifyAuthType.VERIFY_AUTH_TYPE authType;
}
```

### Property

| Name          | Type           | Description                    | **M/O** | **Note**                 |
|---------------|----------------|--------------------------------|---------|--------------------------|
| endpoints     | List\<String>       | Endpoint 목록               |    O    |                          |
| reqE2e        | ReqE2e         | 요청 정보            |    M    | Proof 없음 <br> [ReqE2e](#55-reqe2e)     |
| verifierNonce | String         | 발급처 nonce                   |    M    |                          |
| authType      | VERIFY_AUTH_TYPE | 제출용 인증수단                  |    O    | [VERIFY_AUTH_TYPE](#18-verify_auth_type)   |

<br>

## 5.3. LogoImage

### Description

`로고 이미지`

### Declaration

```java
public class LogoImage {
    private LOGO_IMAGE_TYPE format;
    private String link;
    private String value;
}
```

### Property

| Name   | Type          | Description         | **M/O** | **Note**                 |
|--------|---------------|---------------------|---------|--------------------------|
| format | LOGO_IMAGE_TYPE | 이미지 포맷       |    M    | [LOGO_IMAGE_TYPE](#9-logo_image_type)     |
| link   | String        | 로고 이미지 URL  |    O    | 멀티베이스 인코딩됨     |
| value  | String        | 이미지 값         |    O    | 멀티베이스 인코딩됨     |

<br>

## 5.4. ProviderDetail

### Description

`제공자 상세 정보`

### Declaration

```java
public class ProviderDetail {
    String did;
    String certVCRef;
    String name;
    String description;
    LogoImage logo;
    String ref;
}
```

### Property

| Name        | Type      | Description                       | **M/O** | **Note**                 |
|-------------|-----------|-----------------------------------|---------|--------------------------|
| did         | String    | 제공자 DID                      |    M    |                          |
| certVCRef   | String    | 가입증명서 URL              |    M    |                          |
| name        | String    | 제공자 이름                     |    M    |                          |
| description | String    | 제공자 설명              |    O    |                          |
| logo        | LogoImage | 로고 이미지                        |    O    | [LogoImage](#53-logoimage)          |
| ref         | String    | 참조 URL                     |    O    |                          |

<br>

## 5.5. ReqE2e

### Description

`E2E 요청 데이터`

### Declaration

```java
public class ReqE2e {
    String nonce;
    EllipticCurveType.ELLIPTIC_CURVE_TYPE curve;
    String publicKey;
    SymmetricCipherType.SYMMETRIC_CIPHER_TYPE cipher;
    SymmetricPaddingType.SYMMETRIC_PADDING_TYPE padding;
    Proof proof;
}
```

### Property

| Name      | Type      | Description                                   | **M/O** | **Note**                 |
|-----------|-----------|-----------------------------------------------|---------|--------------------------|
| nonce     | String               | 대칭키 생성용 nonce  |    M    | 멀티베이스 인코딩됨     |
| curve     | ELLIPTIC_CURVE_TYPE    | 타원곡선 종류                  |    M    | [ELLIPTIC_CURVE_TYPE](#18-elliptic_curve_type)   |
| publicKey | String               | 암호화용 서버 공개키 |    M    | 멀티베이스 인코딩됨     |
| cipher    | SYMMETRIC_CIPHER_TYPE  | 암호화 종류                        |    M    | [SYMMETRIC_CIPHER_TYPE](#14-symmetric_cipher_type)  |
| padding   | SYMMETRIC_PADDING_TYPE | 패딩 종류                       |    M    | [SYMMETRIC_PADDING_TYPE](#13-symmetric_padding_type)   |
| proof     | Proof                | Key aggreement proof               |    O    | [Proof](#4-proof)    |

<br>

## 6. VCSchema

### Description

`VC schema`

### Declaration

```java
public class VCSchema {
    String id;
    String schema;
    String title;
    String description;
    VCMetadata metadata;
    CredentialSubject credentialSubject;
}
```

### Property

| Name              | Type              | Description              | **M/O** | **Note**                 |
|-------------------|-------------------|--------------------------|---------|--------------------------|
| id                | String            | VC schema URL       |    M    |                          |
| schema            | String            | VC schema 포맷 URL |    M    |                          |
| title             | String            | VC schema 이름           |    M    |                          |
| description       | String            | VC schema 설명     |    M    |                          |
| metadata          | VCMetadata        | VC metadata              |    M    |  [VCMetadata](#61-vcmetadata)   |
| credentialSubject | CredentialSubject | Credential subject       |    M    |  [CredentialSubject](#62-credentialsubject)   |

<br>

## 6.1. VCMetadata

### Description

`VC Metadata`

### Declaration

```java
public class VCMetadata {
    String language;
    String formatVersion;
}
```

### Property

| Name          | Type   | Description         | **M/O** | **Note**                 |
|---------------|--------|---------------------|---------|--------------------------|
| language      | String | VC 기본 언어 |    M    |                          |
| formatVersion | String | VC 포맷 버전   |    M    |                          |

<br>

## 6.2. CredentialSubject

### Description

`Credential Subject`

### Declaration

```java
public class CredentialSubject {
    List<Claim> claims;
}
```

### Property

| Name   | Type    | Description          | **M/O** | **Note**                 |
|--------|---------|----------------------|---------|--------------------------|
| claims | List\<Claim> | Namespace 별 Claim    |    M    | [Claim](#621-claim)                         |

<br>

## 6.2.1. Claim

### Description

`Claim`

### Declaration

```java
public class Claim {
    Namespace namespace;
    List<ClaimDef> items;
}
```

### Property

| Name      | Type       | Description              | **M/O** | **Note**                 |
|-----------|------------|--------------------------|---------|--------------------------|
| namespace | Namespace  | Claim namespace          |    M    |  [Namespace](#6211-namespace)      |
| items     | List\<ClaimDef> | Claim 정의 목록           |    M    | [ClaimDef](#6212-claimdef)  |

<br>

## 6.2.1.1. Namespace

### Description

`Claim namespace`

### Declaration

```java
public class Namespace {
    String id;
    String name;
    String ref;
}
```

### Property

| Name | Type   | Description                   | **M/O** | **Note**                 |
|------|--------|-------------------------------|---------|--------------------------|
| id   | String | Claim namespace               |    M    |                          |
| name | String | Namespace 이름                |    M    |                          |
| ref  | String | Namespace 정보 URL             |    O    |                          |

<br>

## 6.2.1.2. ClaimDef

### Description

`Claim 정의`

### Declaration

```java
public class ClaimDef {
    String id;
    String caption;
    ClaimType.CLAIM_TYPE type;
    ClaimFormat.CLAIM_FORMAT format;
    boolean hideValue = false;
    Location.LOCATION location;
    boolean required = true;
    String description = "";
    Map<String, String> i18n;
}
```

### Property

| Name        | Type            | Description          | **M/O** | **Note**                 |
|-------------|-----------------|----------------------|---------|--------------------------|
| id          | String          | Claim ID             |    M    |                          |
| caption     | String          | Claim 이름            |    M    |                          |
| type        | CLAIM_TYPE       | Claim 종류            |    M    | [CLAIM_TYPE](#10-claim_type)         |
| format      | CLAIM_FORMAT     | Claim 포맷            |    M    |  [CLAIM_FORMAT](#11-claim_format)       |
| hideValue   | Bool            | 값 숨김                |    O    | Default(false)           |
| location    | LOCATION        | 값 위치                |    O    | Default(inline) <br> [LOCATION](#12-location)        |
| required    | Bool            | 필수여부               |    O    | Default(true)            |
| description | String          | Claim 설명             |    O    | Default("")              |
| i18n        | Map\<String, String> | 국제화                 |    O    |                          |

<br>

# ServiceVo

## 1. Protocol

## 1.1. BaseRequest

### Description

`각 요청 프로토콜 객체는 해당 추상 클래스를 상속하며, 프로토콜 메시지의 기본 클래스 역할을 한다.`

### Declaration

```java
public abstract class BaseRequestVo {
    String id;
    String txId;
}
```

### Property

| Name            | Type           | Description                   | **M/O** | **Note** |
|-----------------|----------------|--------------------------------|---------|----------|
| id            | String | 메시지 ID         |    M    | 
| txId  | String | 거래 ID         |    O    | 

<br>


## 1.1.1. P131RequestVo

### Description

`월렛 등록 프로토콜을 위한 요청 객체`

### Declaration

```java
public class P131RequestVo extends BaseRequestVo {
    AttestedDidDoc attestedDidDoc;
}
```

### Property

| Name            | Type           | Description                   | **M/O** | **Note** |
|-----------------|----------------|--------------------------------|---------|----------|
| attestedDidDoc  | AttestedDidDoc | 제공자 인증된 DID 문서          |    M    | [AttestedDidDoc](#42-attesteddiddoc) |

<br>

## 1.1.2. P132RequestVo

### Description

`사용자 등록 프로토콜을 위한 요청 객체`

### Declaration

```java
public class P132RequestVo extends BaseRequestVo {
    AttestedDidDoc attestedDIDDoc;
    ReqEcdh reqEcdh;
    ServerTokenSeed seed;
    SignedDidDoc signedDidDoc;
    String serverToken;
    String iv;
    String kycTxId;
}
```

### Property

| Name            | Type              | Description                   | **M/O** | **Note** |
|-----------------|-------------------|--------------------------------|---------|----------|
| attestedDIDDoc  | AttestedDidDoc     | 제공자의 인증된 DID 문서          |    M    | [AttestedDidDoc](#42-attesteddiddoc) |
| reqEcdh         | ReqEcdh            | ECDH 요청 데이터               |    M    | [ReqEcdh](#31-reqecdh) |
| seed            | ServerTokenSeed    | 서버 토큰 시드                 |    M    | [ServerTokenSeed](#21-servertokenseed) |
| signedDidDoc    | SignedDidDoc       | 서명된 DID 문서                |    M    | [SignedDidDoc](#43-signeddiddoc) |
| serverToken     | String             | 서버 토큰                      |    M    |          |
| iv              | String             | 초기화 벡터                    |    M    |          |
| kycTxId         | String             | KYC 거래 ID                |    M    |          |
<br>

## 1.1.3. P210RequestVo

### Description

`VC 발급 프로토콜을 위한 요청 객체`

### Declaration

```java
public class P210RequestVo extends BaseRequestVo {
    String vcPlanId;
    String issuer;
    String offerId;
    ReqEcdh reqEcdh;
    ServerTokenSeed seed;
    String serverToken;
    DIDAuth didAuth;
    AccE2e accE2e;
    String encReqVc;
    String vcId;
}
```

### Property

| Name         | Type             | Description                   | **M/O** | **Note** |
|--------------|------------------|--------------------------------|---------|----------|
| vcPlanId     | String            | VC Plan ID                 |    M    |          |
| issuer       | String            | 발급처 DID                     |    M    |          |
| offerId      | String            | Offer ID                        |    M    |          |
| reqEcdh      | ReqEcdh           | ECDH 요청 데이터               |    M    | [ReqEcdh](#31-reqecdh) |
| seed         | ServerTokenSeed   | 서버 토큰 시드                 |    M    | [ServerTokenSeed](#21-servertokenseed) |
| serverToken  | String            | 서버 토큰                      |    M    |          |
| didAuth      | DIDAuth           | DID Auth 데이터                |    M    | [DIDAuth](#35-didauth) |
| accE2e       | AccE2e            | E2E 암호화 데이터              |    M    | [AccE2e](#33-acce2e) |
| encReqVc     | String            | 암호화 VC 요청 데이터    |    M    |          |
| vcId         | String            | VC ID                      |    M    |          |

<br>

## 1.1.4. P310RequestVo

### Description

`VP 제출 프로토콜을 위한 요청 객체`

### Declaration

```java
public class P310RequestVo extends BaseRequestVo {
    String offerId;
    ReqEcdh reqEcdh;
    AccE2e accE2e;
    String encVp;
}
```

### Property

| Name     | Type             | Description                      | **M/O** | **Note** |
|----------|------------------|----------------------------------|---------|----------|
| offerId  | String            | Offer ID  |    M    |          |
| reqEcdh  | ReqEcdh           | ECDH 요청 데이터                 |    M    | [ReqEcdh](#31-reqecdh) |
| accE2e   | AccE2e            | E2E 수락 데이터       |    M    | [AccE2e](#33-acce2e) |
| encVp    | String            | 암호화된 VP |    M    |          |

<br>

## 1.2. BaseResponse

### Description

`각 응답 프로토콜 객체는 해당 추상 클래스를 상속하며, 프로토콜 메시지의 기본 클래스 역할을 한다.`

### Declaration

```java
public abstract class BaseResponseVo {
    String txId;
    Integer code;
    String message;
}
```

### Property

| Name            | Type           | Description                   | **M/O** | **Note** |
|-----------------|----------------|--------------------------------|---------|----------|
| txId            | String | 거래 ID         |    M    | 
| code  | Integer | 에러코드         |    M    | 
| message  | String | 에러메시지         |    M    | 

<br>

## 1.2.1. P131ResponseVo

### Description

`월렛 등록 프로토콜을 위한 응답 객체`

### Declaration

```java
public class P131ResponseVo extends BaseResponseVo {}
```

### Property
N/A

<br>


## 1.2.2. P132ResponseVo

### Description

`사용자 등록 프로토콜을 위한 응답 객체`

### Declaration

```java
public class P132ResponseVo extends BaseResponseVo {
    String iv;
    String encStd;
    AccEcdh accEcdh;
}
```

### Property

| Name     | Type     | Description                   | **M/O** | **Note** |
|----------|----------|-------------------------------|---------|----------|
| iv       | String   | 초기화 벡터                   |    M    |          |
| encStd   | String   | 암호화된 서버 토큰 데이터       |    M    |          |
| accEcdh  | AccEcdh  | ECDH 수락 데이터              |    M    | [AccEcdh](#32-accecdh) |

<br>

## 1.2.3. P210ResponseVo

### Description

`VC 발급 프로토콜을 위한 응답 객체`

### Declaration

```java
public class P210ResponseVo extends BaseResponseVo {
    String refId;
    AccEcdh accEcdh;
    String iv;
    String encStd;
    String authNonce;
    IssueProfile profile;
    E2e e2e;
}
```

### Property

| Name       | Type           | Description                      | **M/O** | **Note** |
|------------|----------------|----------------------------------|---------|----------|
| refId      | String          | 참조 ID                          |    M    |          |
| accEcdh    | AccEcdh         | ECDH 수락 데이터                 |    M    | [AccEcdh](#32-accecdh) |
| iv         | String          | 초기화 벡터                     |    M    |          |
| encStd     | String          | 암호화된 서버 토큰 데이터        |    M    |          |
| authNonce  | String          | 인증 nonce                       |    M    |          |
| profile    | IssueProfile    | 발급 프로파일                      |    M    |[IssueProfile](#51-issueprofile)          |
| e2e        | E2e             | E2E 암호화 데이터                |    M    | [E2e](#34-e2e) |

<br>

## 1.2.4. P310ResponseVo

### Description

`VP 제출 프로토콜을 위한 응답 객체`

### Declaration

```java
public class P310ResponseVo extends BaseResponseVo {
    VerifyProfile profile;
}
```

### Property

| Name    | Type           | Description                      | **M/O** | **Note** |
|---------|----------------|----------------------------------|---------|----------|
| profile | VerifyProfile   |검증 프로파일             |    M    | [VerifyProfile](#52-verifyprofile)         |

<br>

## 2. Token
## 2.1. ServerTokenSeed

### Description

`서버 토큰 시드`

### Declaration

```java
public class ServerTokenSeed {
    SERVER_TOKEN_PURPOSE purpose;
    SignedWalletInfo walletInfo;
    AttestedAppInfo caAppInfo;
}
```

### Property

| Name        | Type                                          | Description                   | **M/O** | **Note** |
|-------------|-----------------------------------------------|-------------------------------|---------|----------|
| purpose     | SERVER_TOKEN_PURPOSE       | 서버 토큰 목적        |    M    | [ServerTokenPurpose](#20-server_token_purpose) |
| walletInfo  | SignedWalletInfo                              | 서명된 월렛 정보        |    M    | [SignedWalletInfo](#212-signedwalletinfo) |
| caAppInfo   | AttestedAppInfo                               | 인증된 앱 정보          |    M    | [AttestedAppInfo](#211-attestedappinfo) |

<br>

## 2.1.1. AttestedAppInfo

### Description

`인증된 앱 정보`


### Declaration

```java
public class AttestedAppInfo {
    String appId;
    Provider provider;
    String nonce;
    Proof proof;
}
```

### Property

| Name     | Type     | Description                   | **M/O** | **Note**                  |
|----------|----------|-------------------------------|---------|---------------------------|
| appId    | String   | 인가앱 ID           |    M    |                            |
| provider | Provider | 인가앱 정보             |    M    | [Provider](#2111-provider)      |
| nonce    | String   | Nonce        |    M    |                            |
| proof    | Proof    | Proof              |    O    | [Proof](#4-proof)          |     

<br>

## 2.1.1.1. Provider

### Description

`제공자 정보`

### Declaration

```java
public class Provider {
    String did;
    String certVcRef;
}
```

### Property

| Name       | Type   | Description             | **M/O** | **Note** |
|------------|--------|-------------------------|---------|----------|
| did        | String | 제공자 DID    |    M    |          |
| certVcRef  | String | 가입증명서 VC URL |    M    |          |

<br>

## 2.1.2. SignedWalletInfo

### Description

`서명된 월렛 정보`

### Declaration

```java
public class SignedWalletInfo {
    Wallet wallet;
    String nonce;
    Proof proof;
    List<Proof> proofs;
}
```

### Property

| Name       | Type           | Description                | **M/O** | **Note** |
|------------|----------------|----------------------------|---------|----------|
| wallet     | Wallet          | 월렛 정보                 |    M    | [Wallet](#2121-wallet) |
| nonce      | String          | Nonce                      |    M    |          |
| proof      | Proof           | Proof                    |    O    | [Proof](#4-proof) |
| proofs     | List<Proof>     | Proof 목록               |    O    | [Proof](#4-proof) |

<br>

## 2.1.2.1. Wallet

### Description

`월렛 정보`

### Declaration

```java
public class Wallet {
    String id;
    String did;
}
```
### Property

| Name       | Type           | Description                | **M/O** | **Note** |
|------------|----------------|----------------------------|---------|----------|
| id         | String          | 월렛 ID  |    M    |          |
| did        | String          | 월렛 제공자 DID |    M    |          |

<br>

## 2.2. ServerTokenData

### Description

`서버 토큰 데이터`

### Declaration

```java
public class ServerTokenData {
    SERVER_TOKEN_PURPOSE purpose;
    String walletId;
    String appId;
    String validUntil;
    Provider provider;
    String nonce;
    Proof proof;
}
```

### Property

| Name       | Type                                         | Description                   | **M/O** | **Note** |
|------------|----------------------------------------------|-------------------------------|---------|----------|
| purpose    | SERVER_TOKEN_PURPOSE      | 서버토큰목적   |    M    | [ServerTokenPurpose](#20-server_token_purpose) |
| walletId   | String                                       | 월렛 ID |    M    |          |
| appId      | String                                       | 인가앱 ID    |    M    |          |
| validUntil | String                                       | 서버토큰 유효시간  |    M    |          |
| provider   | Provider                                     | 제공자 정보          |    M    | [Provider](#2111-provider) |
| nonce      | String                                       | Nonce                   |    M    |          |
| proof      | Proof                                        | Proof                  |    O    | [Proof](#4-proof) |

<br>

## 2.3. WalletTokenSeed

### Description

`월렛 토큰 시드`

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

| Name       | Type                                        | Description                       | **M/O** | **Note** |
|------------|---------------------------------------------|-----------------------------------|---------|----------|
| purpose    | WALLET_TOKEN_PURPOSE     | 월렛토큰목적     |    M    | [WalletTokenPurpose](#21-wallet_token_purpose) |
| pkgName    | String                                      | 인가앱 패키지명 |    M    |      |
| nonce      | String                                      | Nonce                        |    M    |          |
| validUntil | String                                      | 월렛토큰 유효시간      |    M    |          |
| userId     | String                                      | 사용자 ID |    M    |          |

<br>


## 2.4. WalletTokenData

### Description

`월렛 토큰 데이터`

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

| Name       | Type             | Description                   | **M/O** | **Note** |
|------------|------------------|-------------------------------|---------|----------|
| seed       | WalletTokenSeed   | 월렛 토큰 시드                 |    M    | [WalletTokenSeed](#23-wallettokenseed) |
| sha256_pii | String            | PII의 SHA-256 해시             |    M    |          |
| provider   | Provider          | 제공자 정보                    |    M    | [Provider](#2111-provider) |
| nonce      | String            | Nonce                    |    M    |          |
| proof      | Proof             | Proof                  |    O    | [Proof](#4-proof) |

<br>

## 3. SecurityChannel

## 3.1. ReqEcdh

### Description

`ECDH 요청 데이터`

### Declaration

```java
public class ReqEcdh implements ProofContainer {
    String client;
    String clientNonce;
    EllipticCurveType.ELLIPTIC_CURVE_TYPE curve;
    String publicKey;
    Ciphers candidate;
    Proof proof;
    List<Proof> proofs;

    public static class Ciphers {
        List<SymmetricCipherType.SYMMETRIC_CIPHER_TYPE> ciphers;
    }
}
```

### Property

| Name        | Type                             | Description                               | **M/O** | **Note** |
|-------------|----------------------------------|-------------------------------------------|---------|----------|
| client      | String                           | 클라이언트 DID                         |    M    |          |
| clientNonce | String                           | 클라이언트 Nonce              |    M    |          |
| curve       | ELLIPTIC_CURVE_TYPE | ECDH 커브타입                      |    M    |          |
| publicKey   | String                           | 공개키                     |    M    |          |
| candidate   | ReqEcdh.Ciphers                  |  대칭키 암호화 정보                         |    O    |          |
| proof       | Proof                            | Proof                   |    M    | [Proof](#4-proof) |
| proofs      | List<Proof>                      |  Proof 목록                            |    O    | [Proof](#4-proof) |

<br>

## 3.2. AccEcdh

### Description

`ECDH 수락 데이터`

### Declaration

```java
public class AccEcdh {
    String server;
    String serverNonce;
    String publicKey;
    SymmetricCipherType.SYMMETRIC_CIPHER_TYPE cipher;
    SymmetricPaddingType.SYMMETRIC_PADDING_TYPE padding;
    Proof proof;
}
```

### Property

| Name        | Type                                       | Description                        | **M/O** | **Note** |
|-------------|--------------------------------------------|------------------------------------|---------|----------|
| server      | String                                     | 서버 ID                  |    M    |          |
| serverNonce | String                                     | 서버 Nonce                       |    M    |          |
| publicKey   | String                                     | 공개키       |    M    |          |
| cipher      | SymmetricCipherType.SYMMETRIC_CIPHER_TYPE  | 암호화 종류         |    M    |          |
| padding     | SymmetricPaddingType.SYMMETRIC_PADDING_TYPE| 패딩 종류        |    M    |          |
| proof       | Proof                                      | Key agreement proof                |    O    | [Proof](#4-proof) |

<br>

## 3.3. AccE2e

### Description

`E2E 수락 데이터`

### Declaration

```java
public class AccE2e {
    String publicKey;
    String iv;
    Proof proof;
    List<Proof> proofs;
}
```

### Property

| Name       | Type               | Description                | **M/O** | **Note**               |
|------------|--------------------|----------------------------|---------|------------------------|
| publicKey  | String              | 공개키  |    M    | |
| iv         | String              | 초기화 벡터      |    M    | |
| proof      | Proof               | Key agreement proof        |    O    | [Proof](#4-proof)       |
| proofs     | List<Proof>         | proof 목록            |    O    | [Proof](#4-proof)       |

<br>

## 3.4. E2e

### Description

`E2E 암호화 정보`

### Declaration

```java
public class E2e {
    String iv;
    String encVc;
}
```

### Property

| Name  | Type   | Description                      | **M/O** | **Note** |
|-------|--------|----------------------------------|---------|----------|
| iv    | String | 초기화 벡터       |    M    |          |
| encVc | String | 암호화된 VC  |    M    |          |

<br>

## 3.5. DIDAuth

### Description

`DID Auth 데이터`

### Declaration

```java
public class DIDAuth {
    String did;
    String authNonce;
    Proof proof;
    List<Proof> proofs;
}
```

### Property

| Name       | Type       | Description                        | **M/O** | **Note**                  |
|------------|------------|------------------------------------|---------|---------------------------|
| did        | String     | 대상 DID     |    M    |                            |
| authNonce  | String     | Auth nonce      |    M    |                            |
| proof      | Proof      | 인증 proof               |    M    | [Proof](#4-proof)          |
| proofs     | List<Proof>| 인증 proof 목록      |    M    | [Proof](#4-proof)          |

<br>

## 4. DidDoc
## 4.1. DidDocVo

### Description

`인코딩된 DID 문서`

### Declaration

```java
public class DidDocVo {
    String didDoc;
}
```

### Property

| Name   | Type   | Description            | **M/O** | **Note** |
|--------|--------|------------------------|---------|----------|
| didDoc | String | 인코딩된 DID 문서 |    M    |          |

<br>

## 4.2. AttestedDidDoc

### Description

`제공자의 인증된 DID 문서 `

### Declaration

```java
public class AttestedDidDoc {
    String walletId;
    String ownerDidDoc;
    Provider provider;
    String nonce;
    Proof proof;
}
```

### Property

| Name       | Type     | Description                     | **M/O** | **Note**                  |
|------------|----------|---------------------------------|---------|---------------------------|
| walletId   | String   | 월렛 ID               |    M    |                            |
| ownerDidDoc| String   | 소유자의 DID 문서            |    M    |                            |
| provider   | Provider | 제공자 정보            |    M    | [Provider](#2111-provider)      |
| nonce      | String   | Nonce |    M    |                            |
| proof      | Proof    | Attestation proof               |    M    | [Proof](#4-proof)          |

<br>

## 4.3. SignedDidDoc

### Description

`서명된 DID 문서`

### Declaration

```java
public class SignedDidDoc {
    String ownerDidDoc;
    Wallet wallet;
    String nonce;
    Proof proof;
    List<Proof> proofs;
}
```

### Property

| Name       | Type           | Description                | **M/O** | **Note** |
|------------|----------------|----------------------------|---------|----------|
| ownerDidDoc| String          | 소유자의 DID document       |    M    |          |
| wallet     | Wallet          | 월렛 정보         |    M    | [Wallet](#2121-wallet) |
| nonce      | String          | Nonce                |    M    |          |
| proof      | Proof           | Proof               |    M    | [Proof](#4-proof) |
| proofs     | List<Proof>     | Proof 목록             |    M    | [Proof](#4-proof) |

<br>

## 5. Offer
## 5.1. IssueOfferPayload

### Description

`발급오퍼 페이로드`

### Declaration

```java
public class IssueOfferPayload {
    String offerId;
    String vcPlanId;
    String issuer;
    String validUntil;
}
```

### Property

| Name      | Type   | Description                   | **M/O** | **Note** |
|-----------|--------|-------------------------------|---------|----------|
| offerId   | String | Offer ID |    M    |          |
| vcPlanId  | String | VC Plan ID  |    M    |          |
| issuer    | String | 발급처 DID      |    M    |          |
| validUntil| String | 유효시간          |    M    |          |

<br>

## 5.2. VerifyOfferPayload

### Description

`제출오퍼 페이로드`

### Declaration

```java
public class VerifyOfferPayload {
    String offerId;
    OFFER_TYPE type;
    PRESENT_MODE mode;
    String device;
    String service;
    List<String> endpoints;
    String validUntil;
    boolean locked;

    public enum OFFER_TYPE {
        IssueOffer,
        VerifyOffer
    }

    public enum PRESENT_MODE {
        Direct,
        Indirect,
        Proxy
    }
}
```

### Property

| Name       | Type                  | Description                           | **M/O** | **Note** |
|------------|-----------------------|---------------------------------------|---------|----------|
| offerId    | String                 | Offer ID              |    M    |          |
| type       | OFFER_TYPE  | Offer 타입            |    M    |          |
| mode       | PRESENT_MODE | 제출모드           |    M    |          |
| device     | String                 | 응대장치 식별자      |    O    |          |
| service    | String                 | 서비스 식별자     |    O    |          |
| endpoints  | List<String>           | 프로파일 요청 API endpoint 목록             |    O    |          |
| validUntil | String                 | 유효시간        |    M    |          |
| locked     | boolean                | Offer 잠김 여부    |    O    |          |

<br>

## 6. VC
## 6.1. ReqVC

### Description

`VC 요청 객체`

### Declaration

```java
public class ReqVC {
    String refId;
    Profile profile;
}
```

### Property

| Name         | Type            | Description                         | **M/O** | **Note** |
|--------------|-----------------|-------------------------------------|---------|----------|
| refId        | String           | 참조 ID       |    M    |          |
| profile      | Profile    | 발급 요청 프로파일      |    M    |          |

<br>

## 6.1.1. Profile

### Description

`발급 요청 프로파일`

### Declaration

```java
public static class Profile {
    String id;
    String issuerNonce;
}
```

### Property

| Name         | Type            | Description                         | **M/O** | **Note** |
|--------------|-----------------|-------------------------------------|---------|----------|
| id        | String | 발급처 DID                        |    M    |                          |
| issuerNonce | String            | 발급처 nonce        |    M    |                                     |
<br>


## 6.2. VCPlanList

### Description

`VC Plan 목록`

### Declaration

```java
public class VCPlanList {
    int count;
    List<VCPlan> items;
}
```

### Property

| Name   | Type             | Description                      | **M/O** | **Note** |
|--------|------------------|----------------------------------|---------|----------|
| count  | int              | VC plan의 수               |    M    |          |
| items  | List<VCPlan>     | VC plan 목록              |    M    | [VCPlan](#621-vcplan) |

<br>

## 6.2.1. VCPlan

### Description

`VC plan 상세 객체`

### Declaration

```java
public class VCPlan {
    String vcPlanId;
    String name;
    String description;
    String ref;
    LogoImage logo;
    String validFrom;
    String validUntil;
    CredentialSchema credentialSchema;
    Option option;
    List<String> allowedIssuers;
    String manager;
}
```

### Property

| Name           | Type               | Description                          | **M/O** | **Note** |
|----------------|--------------------|--------------------------------------|---------|----------|
| vcPlanId       | String             | VC plan ID          |    M    |          |
| name           | String             | VC plan 이름                |    M    |          |
| description    | String             | VC plan 설명           |    M    |          |
| ref            | String             | 참조 ID          |    M    |          |
| logo           | LogoImage          | 로고이미지        |    O    | [LogoImage](#53-logoimage) |
| validFrom      | String             | 유효시작일      |    M    |          |
| validUntil     | String             | 유효만료일      |    M    |          |
| credentialSchema| CredentialSchema  | Credential schema            |    O    |          |
| option         | Option      | VC Plan 옵션                         |    O    |          |
| allowedIssuers | List<String>       | VC plan 사용이 허용된 발급 사업자 DID 목록 |    M    |          |
| manager        | String             | VC plan 관리 권한을 가진 엔티티     |    M    |          |

## 6.2.1.1. Option

### Description

`VC plan 옵션`

### Declaration
```java
public static class Option {
    boolean allowUserInit;
    boolean allowIssuerInit;
    boolean delegatedIssuance;
}
```

### Property

| Name         | Type            | Description                         | **M/O** | **Note** |
|--------------|-----------------|-------------------------------------|---------|----------|
| allowUserInit        | boolean | 사용자에 의한 발급 개시 허용 여부                        |    M    |                          |
| allowIssuerInit | boolean            | 이슈어에 의한 발급 개시 허용 여부        |    M    |                                     |
| delegatedIssuance | boolean            | 대표 발급자에 의한 위임발급 여부        |    M    |                                     |

<br>


# Enumerators

## 1. DID_KEY_TYPE

### Description

`DID 키 종류`

### Declaration
```java
public enum DID_KEY_TYPE {
    rsaVerificationKey2018("RsaVerificationKey2018"),
    secp256k1VerificationKey2018("Secp256k1VerificationKey2018"),
    secp256r1VerificationKey2018("Secp256r1VerificationKey2018");
}
```
<br>

## 2. DID_SERVICE_TYPE

### Description

`서비스 종류`

### Declaration
```java
public enum DID_SERVICE_TYPE {
    linkedDomains("LinkedDomains"),
    credentialRegistry("CredentialRegistry");
}
```

<br>

## 3. PROOF_PURPOSE

### Description

`Proof 목적`

### Declaration
```java
public enum PROOF_PURPOSE {
    assertionMethod("assertionMethod"),
    authentication("authentication"),
    keyAgreement("keyAgreement"),
    capabilityInvocation("capabilityInvocation"),
    capabilityDelegation("capabilityDelegation");
}
```

<br>

## 4. PROOF_TYPE

### Description

`Proof 종류`

### Declaration

```java
public enum PROOF_TYPE {
    rsaSignature2018("RsaSignature2018"),
    secp256k1Signature2018("Secp256k1Signature2018"),
    secp256r1Signature2018("Secp256r1Signature2018");
}
```

<br>

## 5. AUTH_TYPE

### Description

`키의 접근 방법을 가리킨다.`

### Declaration

```java
public enum AUTH_TYPE {
    FREE(1),
    PIN(2),
    BIO(4);
}
```

<br>

## 6. EVIDENCE_TYPE

### Description

`다중종류 배열을 위한 Evidence Enumerator`

### Declaration

```java
public enum EVIDENCE_TYPE {
    documentVerification("DocumentVerification");
}
```

<br>

## 7. PRESENCE

### Description

`표현 종류`

### Declaration

```java
public enum PRESENCE {
    physical("Physical"),
    digital("Digital");
}
```

<br>

## 8. PROFILE_TYPE

### Description

`프로파일 종류`

### Declaration

```java
public enum PROFILE_TYPE {
    issueProfile("IssueProfile"),
    verifyProfile("VerifyProfile");
}
```

<br>

## 9. LOGO_IMAGE_TYPE

### Description

`로고 이미지 종류`

### Declaration

```java
public enum LOGO_IMAGE_TYPE {
    jpg("jpg"),
    png("png");
}
```

<br>

## 10. CLAIM_TYPE

### Description

`Claim 종류`

### Declaration

```java
public enum CLAIM_TYPE {
    text("text"),
    image("image"),
    document("document");
}
```

<br>

## 11. CLAIM_FORMAT

### Description

`Claim 포맷`

### Declaration

```java
public enum CLAIM_FORMAT {
    plain("plain"),
    html("html"),
    xml("xml"),
    csv("csv"),
    png("png"),
    jpg("jpg"),
    gif("gif"),
    txt("txt"),
    pdf("pdf"),
    word("word");
}
```

<br>

## 12. LOCATION

### Description

`값 위치`

### Declaration

```java
public enum LOCATION {
    inline("inline"),
    remote("remote"),
    attach("attach");
}
```

<br>

## 13. SYMMETRIC_PADDING_TYPE

### Description

`패딩 옵션`

### Declaration

```java
public enum SYMMETRIC_PADDING_TYPE {
    NOPAD("NOPAD"),
    PKCS5("PKCS5");
}
```

<br>

## 14. SYMMETRIC_CIPHER_TYPE

### Description

`암호화 종류`

### Declaration

```java
public enum SYMMETRIC_CIPHER_TYPE {
    AES128CBC("AES-128-CBC"),
    AES128ECB("AES-128-ECB"),
    AES256CBC("AES-256-CBC"),
    AES256ECB("AES-256-ECB");
}
```

<br>

## 15. ALGORITHM_TYPE

### Description

`알고리즘 종류`

### Declaration

```java
public enum ALGORITHM_TYPE {
    RSA("Rsa"),
    SECP256K1("Secp256k1"),
    SECP256R1("Secp256r1");
}
```

<br>

## 16. CREDENTIAL_SCHEMA_TYPE

### Description

`Credential schema 종류`

### Declaration

```java
public enum CREDENTIAL_SCHEMA_TYPE {
    osdSchemaCredential("OsdSchemaCredential");
}
```

<br>

## 17. ELLIPTIC_CURVE_TYPE

### Description

`타원 곡선 종류`

### Declaration

```java
public enum ELLIPTIC_CURVE_TYPE {
    SECP256K1("Secp256k1"),
    SECP256R1("Secp256r1");
}
```

<br>

## 18. VERIFY_AUTH_TYPE

### Description

`키의 접근 방법과 제출 옵션을 가리킨다. AuthType과 유사하다.`

### Declaration

```java
public enum VERIFY_AUTH_TYPE {
    ANY(0x00000000),
    FREE(0x00000001),
    PIN(0x00000002),
    BIO(0x00000004),
    PIN_OR_BIO(0x00000006),
    PIN_AND_BIO(0x00008006);
}
```

<br>

## 19. ROLE_TYPE

### Description

`Role 타입 종류`

### Declaration

```java
public enum ROLE_TYPE {
    TAS("Tas"),
    WALLET("Wallet"),
    ISSUER("Issuer"),
    VERIFIER("Verifier"),
    WALLET_PROVIDER("WalletProvider"),
    APP_PROVIDER("AppProvider"),
    LIST_PROVIDER("ListProvider"),
    OP_PROVIDER("OpProvider"),
    KYC_PROVIDER("KycProvider"),
    NOTIFICATION_PROVIDER("NotificationProvider"),
    LOG_PROVIDER("LogProvider"),
    PORTAL_PROVIDER("PortalProvider"),
    DELEGATION_PROVIDER("DelegationProvider"),
    STORAGE_PROVIDER("StorageProvider"),
    BACKUP_PROVIDER("BackupProvider"),
    ETC("Etc");
}
```
<br>

## 20. SERVER_TOKEN_PURPOSE

### Description

`서버토큰목적`

### Declaration

```java
public enum SERVER_TOKEN_PURPOSE {
    CREATE_DID(5),
    UPDATE_DID(6),
    RESTORE_DID(7),
    ISSUE_VC(8),
    REMOVE_VC(9),
    PRESENT_VP(10),
    CREATE_DID_AND_ISSUE_VC(13);
}
```

<br>

## 21. WALLET_TOKEN_PURPOSE

### Description

`월렛토큰목적`

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




# APIs

## 1. deserialize

### Description

`데이터모델의 deserialize`

### Declaration

```java
public static <T> T deserialize(String jsonString, Class<T> clazz)
```

<br>

## 2. convertFrom

### Description

`AlgorithmType으로 변환 (ELLIPTIC_CURVE_TYPE / PROOF_TYPE / DID_KEY_TYPE)`

### Declaration

```java
public static ELLIPTIC_CURVE_TYPE convertFrom(AlgorithmType.ALGORITHM_TYPE type){
    switch (type) {
        case SECP256K1:
            return ELLIPTIC_CURVE_TYPE.SECP256K1;
        case SECP256R1:
            return ELLIPTIC_CURVE_TYPE.SECP256R1;
        default:
            throw new RuntimeException();
    }
}

public static PROOF_TYPE convertFrom(AlgorithmType.ALGORITHM_TYPE type){
    switch (type) {
        case RSA:
            return PROOF_TYPE.rsaSignature2018;
        case SECP256K1:
            return PROOF_TYPE.secp256k1Signature2018;
        case SECP256R1:
            return PROOF_TYPE.secp256r1Signature2018;
        default:
            throw new RuntimeException();
    }
}

public static DID_KEY_TYPE convertFrom(AlgorithmType.ALGORITHM_TYPE type){
    switch (type) {
        case RSA:
            return DID_KEY_TYPE.rsaVerificationKey2018;
        case SECP256K1:
            return DID_KEY_TYPE.secp256k1VerificationKey2018;
        case SECP256R1:
            return DID_KEY_TYPE.secp256r1VerificationKey2018;
        default:
            throw new RuntimeException();
    }
}
```

<br>

## 3. convertTo

### Description

`AlgorithmType에서 해당 값으로 변환 (ELLIPTIC_CURVE_TYPE / PROOF_TYPE / DID_KEY_TYPE)`

### Declaration

```java
public static AlgorithmType.ALGORITHM_TYPE convertTo(ELLIPTIC_CURVE_TYPE type){
    switch (type) {
        case SECP256K1:
            return AlgorithmType.ALGORITHM_TYPE.SECP256K1;
        case SECP256R1:
            return AlgorithmType.ALGORITHM_TYPE.SECP256R1;
        default:
            throw new RuntimeException();
    }
}

public static AlgorithmType.ALGORITHM_TYPE convertTo(PROOF_TYPE type){
    switch (type) {
        case rsaSignature2018:
            return AlgorithmType.ALGORITHM_TYPE.RSA;
        case secp256k1Signature2018:
            return AlgorithmType.ALGORITHM_TYPE.SECP256K1;
        case secp256r1Signature2018:
            return AlgorithmType.ALGORITHM_TYPE.SECP256R1;
        default:
            throw new RuntimeException();
    }
}

public static AlgorithmType.ALGORITHM_TYPE convertTo(DID_KEY_TYPE type){
    switch (type) {
        case rsaVerificationKey2018:
            return AlgorithmType.ALGORITHM_TYPE.RSA;
        case secp256k1VerificationKey2018:
            return AlgorithmType.ALGORITHM_TYPE.SECP256K1;
        case secp256r1VerificationKey2018:
            return AlgorithmType.ALGORITHM_TYPE.SECP256R1;
        default:
            throw new RuntimeException();
    }
}
```

<br>





