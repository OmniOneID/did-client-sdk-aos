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

- Topic: VCManager
- Author: Sangjun Kim
- Date: 2024-07-10
- Version: v1.0.0

| Version | Date       | Changes                   |
| ------- | ---------- | ------------------------- |
| v1.0.0  | 2024-07-10 | Initial version           |


<div style="page-break-after: always;"></div>

# Table of Contents
- [APIs](#api-list)
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

# APIs
## 1. Constructor

### Description
`VCManager constructor`

### Declaration

```java
VCManager(String fileName, Context context);
```

### Parameters

| Parameter | Type    | Description            | **M/O** | **Remarks**                       |
|-----------|---------|------------------------|---------|-----------------------------------|
| fileName  | string  | File name              | M       | The file name to save the wallet in VCManager |
| context   | Context | Context                | M       |                                   |

### Returns

| Type      | Description           | **M/O** | **Remarks** |
|-----------|-----------------------|---------|-------------|
| VCManager | VCManager object      | M       |             |

### Usage
```java
VCManager<VerifiableCredential> vcManager = new VCManager<>("MyWallet", this);
```

<br>

## 2. isAnyCredentialsSaved

### Description
`Checks if there is at least one saved VC. (Verifies the existence of the VC wallet)`

### Declaration

```java
boolean isAnyCredentialsSaved()
```

### Parameters

n/a

### Returns

| Type    | Description          | **M/O** | **Remarks** |
|---------|----------------------|---------|-------------|
| boolean | Presence of saved VC | M       |             |

### Usage
```java
VCManager<VerifiableCredential> vcManager = new VCManager<>("MyWallet", this);

if(vcManager.isAnyCredentialsSaved()) {
    vcManager.deleteAllCredentials();
}
```

<br>

## 3. addCredential

### Description
`Stores the issued VC.`

### Declaration

```java
void addCredentials(VerifiableCredential verifiableCredential) throws Exceptionvoid addCredentials(VerifiableCredential verifiableCredential) throws Exception
```

### Parameters

| Parameter            | Type                  | Description      | **M/O** | **Remarks**                                  |
|----------------------|-----------------------|------------------|---------|----------------------------------------------|
| verifiableCredential | VerifiableCredential  | Issued VC object | M       |[VerifiableCredential](#1-verifiablecredential)|

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
`Returns all VCs that match the given IDs.`

### Declaration

```java
List<VerifiableCredential> getCredentials(List<String> identifiers) throws Exception
```

### Parameters

| Parameter    | Type                  | Description                 | **M/O** | **Remarks** |
|--------------|-----------------------|-----------------------------|---------|-------------|
| identifiers  | List&lt;String&gt;    | List of VC IDs to retrieve  | M       |             |

### Returns

| Type                                | Description                     | **M/O** | **Remarks**                                  |
|-------------------------------------|---------------------------------|---------|----------------------------------------------|
| List&lt;VerifiableCredential&gt;    | List of VCs that match the IDs  | M       | [VerifiableCredential](#1-verifiablecredential) |

### Usage
```java
VCManager<VerifiableCredential> vcManager = new VCManager<>("MyWallet", this);

List<VerifiableCredential> vcList = vcManager.getCredentials(List.of("credentialId", "credentialId2"));
```

<br>

## 5. getAllCredentials

### Description
`Returns all stored VCs.`

### Declaration

```java
List<VerifiableCredential> getAllCredentials() throws Exception
```

### Parameters

n/a

### Returns

| Type                                | Description             | **M/O** | **Remarks**                                  |
|-------------------------------------|-------------------------|---------|----------------------------------------------|
| List&lt;VerifiableCredential&gt;    | List of all stored VCs  | M       | [Link](#1-verifiablecredential)|

### Usage
```java
VCManager<VerifiableCredential> vcManager = new VCManager<>("MyWallet", this);

List<VerifiableCredential> vcList = vcManager.getAllCredentials();
```

<br>

## 6. deleteCredentials

### Description
`Deletes all VCs that match the given IDs. If no VCs remain after deletion, the file is deleted.`

### Declaration

```java
void deleteCredentials(List<String> identifiers) throws Exception
```

### Parameters

| Parameter    | Type                  | Description                 | **M/O** | **Remarks** |
|--------------|-----------------------|-----------------------------|---------|-------------|
| identifiers  | List&lt;String&gt;    | List of VC IDs to delete    | M       |             |

### Returns

void

### Usage
```java
VCManager<VerifiableCredential> vcManager = new VCManager<>("MyWallet", this);

vcManager.deleteCredentials(List.of("credentialId", "credentialId2"));
```

<br>

## 7. deleteAllCredentials

### Description
`Deletes all stored VCs. (Deletes the wallet file)`

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
`Returns a VP object without proof.`

### Declaration

```java
VerifiablePresentation makePresentation(List<ClaimInfo> claimInfos, PresentationInfo presentationInfo) throws Exception
```

### Parameters

| Parameter         | Type                       | Description                         | **M/O** | **Remarks** |
|-------------------|----------------------------|-------------------------------------|---------|-------------|
| claimInfos        | List&lt;ClaimInfo&gt;      | List of VC information for VP creation | M       |             |
| presentationInfo  | PresentationInfo           | Information about the VC itself for VP creation | M       |             |

### Returns

| Type                    | Description                    | **M/O** | **Remarks**                                  |
|-------------------------|--------------------------------|---------|----------------------------------------------|
| VerifiablePresentation  | VP object without proof        | M       | [VerifiablePresentation](#4-verifiablepresentation) |

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

`VC object (provided by DataModel SDK)`
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

`VC information used for VP creation. Retrieves the VC corresponding to the credentialId from the wallet and uses only the claim information specified in claimCodes to create the VP.`

### Declaration

```java
public class ClaimInfo {
    String credentialId;
    List<String> claimCodes;
}
```

### Property

| Name          | Type                  | Description     | **M/O** | **Note** |
|---------------|-----------------------|-----------------|---------|----------|
| credentialId  | String                | ID of the VC    | M       |          |
| claimCodes    | List&lt;String&gt;    | List of claim codes | M   |          |

<br>


## 3. PresentationInfo

### Description

`Information about the VP itself used for VP creation`

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

| Name          | Type                      | Description            | **M/O** | **Note** |
|---------------|---------------------------|------------------------|---------|----------|
| holder        | String                    | DID of the VC holder   | M       |          |
| validFrom     | String                    | VC validity start date | M       |          |
| validUntil    | String                    | VC validity end date   | M       |          |
| verifierNonce | String                    | Verifier nonce         | M       |          |

<br>


## 4. VerifiablePresentation

### Description

`VP object (provided by DataModel SDK)`
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