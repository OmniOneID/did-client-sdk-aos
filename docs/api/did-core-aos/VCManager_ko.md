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

Android VCManager Core SDK API
==

- 주제: VCManager
- 작성: Sangjun Kim
- 일자: 2024-07-10
- 버전: v1.0.0

| 버전   | 일자       | 변경 내용                 |
| ------ | ---------- | -------------------------|
| v1.0.0 | 2024-07-10 | 초기 작성                 |


<div style="page-break-after: always;"></div>

# 목차
- [APIs](#api-목록)
    - [1. constructor](#1-constructor)
    - [2. isAnyCredentialsSaved](#2-isanykeysaved)
    - [3. addCredential](#3-addcredential)
    - [4. getCredentials](#4-getcredentials)
    - [5. getAllCredentials](#5-getallcredentials)
    - [6. deleteCredentials](#6-deletecredentials)
    - [7. deleteAllCredentials](#7-deleteallcredentials)
    - [8. makePresentation](#8-makepresentation)
- [Value Object](#value-object)
    - [1. VerifiableCredential](#1-verifiablecredential)
    - [2. ClaimInfo](#2-claiminfo)
    - [3. PresentationInfo](#3-presentationinfo)
    - [4. VerifiablePresentation](#4-verifiablepresentation)

# API 목록
## 1. Constructor

### Description
`VCManager costructor`

### Declaration

```java
VCManager(String fileName, Context context);
```


### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| fileName    | string    | 파일명 |M|VCManager에서 저장할 월렛의 파일명|
| context    | Context | context |M|  |

### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| VCManager  | VCManager 객체 |M| |


### Usage
```java
VCManager<VerifiableCredential> vcManager = new VCManager<>("MyWallet", this);
```

<br>

## 2. isAnyCredentialsSaved

### Description
`저장된 VC가 1개 이상 있는지 확인한다. (VC 월렛이 존재하는지 확인)` 

### Declaration

```java
boolean isAnyCredentialsSaved()
```

### Parameters

n/a

### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| boolean  | 저장된 VC 유무 |M| |



### Usage
```java
VCManager<VerifiableCredential> vcManager = new VCManager<>("MyWallet", this);

if(vcManager.isAnyCredentialsSaved()) {
    vcManager.deleteAllCredentials()
}
```

<br>

## 3. addCredential

### Description
`발급받은 VC를 저장한다.`

### Declaration

```java
void addCredentials(VerifiableCredential verifiableCredential) throws Exceptionvoid addCredentials(VerifiableCredential verifiableCredential) throws Exception
```

### Parameters
| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| verifiableCredential| VerifiableCredential| 발급받은 VC 객체 |M| [VerifiableCredential](#1-verifiablecredential)|



### Returns

void

### Usage
```java
VCManager<VerifiableCredential> vcManager = new VCManager<>("MyWallet", this);

vcManager.addCredentials(verifiableCredential);
```

<br>

## 4. getCredentials

### Description
`ID와 일치하는 VC를 모두 반환한다.`

### Declaration

```java
List<VerifiableCredential> getCredentials(List<String> identifiers) throws Exception
```

### Parameters
| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| identifiers    | List&lt;String&gt; | 반환받을 VC ID 리스트 |M| |


### Returns
| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| List&lt;VerifiableCredential&gt;  | ID와 일치하는 VC 리스트 |M| [VerifiableCredential](#1-verifiablecredential)|



### Usage
```java
VCManager<VerifiableCredential> vcManager = new VCManager<>("MyWallet", this);

List<VerifiableCredential> vcList = vcManager.getCredentials(List.of("credentialId","credentialId2"));    
```

<br>

## 5. getAllCredentials

### Description
`저장된 VC를 모두 반환한다. `

### Declaration

```java
List<VerifiableCredential> getAllCredentials() throws Exception
```

### Parameters

n/a

### Returns


| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| List&lt;VerifiableCredential&gt;  | 저장된 모든 VC 리스트  |M| [Link](#1-verifiablecredential)|



### Usage
```java
VCManager<VerifiableCredential> vcManager = new VCManager<>("MyWallet", this);

List<VerifiableCredential> vcList = vcManager.getAllCredentials();    
```

<br>

## 6. deleteCredentials

### Description
`ID가 일치하는 VC를 모두 삭제한다. VC 삭제 후, 남은 VC가 없으면 파일을 삭제한다.`

### Declaration

```java
void deleteCredentials(List<String> identifiers) throws Exception
```

### Parameters
| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| identifiers    | List&lt;String&gt; | 삭제할 VC ID 리스트 |M| |


### Returns
void

### Usage
```java
VCManager<VerifiableCredential> vcManager = new VCManager<>("MyWallet", this);

vcManager.deleteCredentials(List.of("credentialId","credentialId2"));
                
```

<br>

## 7. deleteAllCredentials

### Description
`저장된 VC를 모두 삭제한다. (월렛 파일 삭제)`

### Declaration

```java
void deleteAllCredentials() throws Exception
```

### Parameters

n/a


### Returns

void


### Usage
```java
VCManager<VerifiableCredential> vcManager = new VCManager<>("MyWallet", this);

vcManager.deleteAllCredentials();
```

<br>

## 8. makePresentation

### Description
`Proof가 없는 VP 객체를 리턴한다.`

### Declaration

```java
VerifiablePresentation makePresentation(List<ClaimInfo> claimInfos, PresentationInfo presentationInfo) throws Exception
```

### Parameters
| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| claimInfos    | List&lt;ClaimInfo&gt; | VP 생성에 사용될 VC 정보 리스트 |M| |
| presentationInfo    | PresentationInfo | VP 생성에 사용될 VC 자체 정보 |M| |


### Returns
| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| VerifiablePresentation | Proof가 없는 VP 객체 |M| [VerifiablePresentation](#4-verifiablepresentation)|


### Usage
```java
VCManager<VerifiableCredential> vcManager = new VCManager<>("MyWallet", this);

List<ClaimInfo> claimInfos = new ArrayList<>();
ClaimInfo claimInfo = new ClaimInfo(
                        "credentialId",
                        List.of("org.iso.18013.5.family_name", "org.iso.18013.5.birth_date")
                        );
claimInfos.add(claimInfo);
PresentationInfo presentationInfo = new PresentationInfo(
                        "Holder DID",
                        "valid from date",
                        "valid until date",
                        "nonce"
                        );

VerifiablePresentation vp = vcManager.makePresentation(claimInfos, presentationInfo);
```

<br>


# Value Object

## 1. VerifiableCredential

### Description

`VC 객체 (DataModel SDK 제공)`
[Link]

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
```


<br>

## 2. ClaimInfo

### Description

`VP 생성에 사용될 VC 정보. 월렛에서 credentialId에 해당하는 VC를 꺼내 claimCodes에 명시된 클레임 정보만 이용해 VP를 생성하는데 이용한다.`

### Declaration

```java
public class ClaimInfo {
    String credentialId;
    List<String> claimCodes;
}
```

### Property

| Name          | Type               | Description                      | **M/O** | **Note**   
|-----------|--------|----------------------------|---------|---------|
| credentialId    | String | VC의 ID |M| |
| claimCodes    | List&lt;String&gt; | 클레임 코드 리스트 |M| |

<br>

## 3. PresentationInfo

### Description

`VP 생성에 사용될 VP 자체 정보`

### Declaration

```java
public class PresentationInfo extends Meta {
    String holder;
    String validFrom;
    String validUntil;
    String verifierNonce;
}
```

### Property

| Name          | Type                      | Description                     | **M/O** | **Note**                           |
|---------------|---------------------------|---------------------------------|---------|------------------------------------|
| holder  | String | VC 소유자의 DID |    M    | |
| validFrom  | String    | VC 유효기간 시작 일시  |    M    |   |
| validUntil  | String    | VC 유효기간 종료 일시  |    M    |   |
| verifierNonce  | String    | verifier nonce  |    M    |   |

<br>

## 4. VerifiablePresentation

### Description

`VP 객체 (DataModel SDK 제공)`
[Link]

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
