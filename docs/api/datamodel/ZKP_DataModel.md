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

Android ZKP DataModel SDK
==

- Subject: ZKP-Based Data Model
- Author: Dongjun Park
- Date: 2025-04-16
- Version: v1.0.0

| Version | Date       | Change Description |
|---------|------------|--------------------|
| v1.0.0  | 2025-04-16 | Initial version    |

<div style="page-break-after: always;"></div>

# Table of Contents
- [1. CredentialRequest](#1-credentialrequest)
    - [1.1. BlindedCredentialSecrets](#11-blindedcredentialsecrets)
    - [1.2. BlindedCredentialSecretsCorrectnessProof](#12-blindedcredentialsecretscorrectnessproof)
- [2. Credential](#2-credential)
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

Credential issuance request object based on ZKP

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

| Name                      | Type                                     | Description                            | **M/O** | **Note** |
|---------------------------|------------------------------------------|----------------------------------------|---------|----------|
| proverDid                 | String                                   | Prover's DID                           | M       |          |
| credDefId                 | String                                   | Credential Definition ID               | M       |          |
| nonce                     | BigInteger                               | Nonce used for authentication          | M       |          |
| blindedMs                 | BlindedCredentialSecrets                 | Blinded Master Secret                  | M       |          |
| blindedMsCorrectnessProof| BlindedCredentialSecretsCorrectnessProof | Proof of correctness of blinding       | M       |          |
<br>

### 1.1. BlindedCredentialSecrets

#### Description

A value that hides specific attributes from the issuer when signing credentials using blind signatures

#### Declaration

```java
public class BlindedCredentialSecrets {
    String u;
    List<String> hiddenAttributes;
}
```

#### Property

| Name             | Type   | Description                     | **M/O** | **Note** |
|------------------|--------|---------------------------------|---------|----------|
| u                | String | Blinded Master Secret           | M       |          |
| hiddenAttributes | List   | List of hidden attribute names  | M       |          |
<br>

### 1.2. BlindedCredentialSecretsCorrectnessProof

#### Description

Proof of correctness for the BlindedCredentialSecrets

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

| Name     | Type                     | Description                         | **M/O** | **Note** |
|----------|--------------------------|-------------------------------------|---------|----------|
| c        | BigInteger               | Challenge value                     | M       |          |
| vDashCap | BigInteger               | Blinding verification value         | M       |          |
| mCaps    | Map<String, BigInteger> | Response values for m               | M       |          |
| rCaps    | Map<String, BigInteger> | Response values for r               | M       |          |
<br>

## 2. Credential

### Description

Credential object that has been issued

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

| Name                      | Type                          | Description                        | **M/O** | **Note** |
|---------------------------|-------------------------------|------------------------------------|---------|----------|
| credentialId              | String                        | Unique identifier for the credential| M      |          |
| schemaId                  | String                        | Identifier of the schema           | M       |          |
| credDefId                 | String                        | Credential Definition ID           | M       |          |
| values                    | Map<String, CredentialValue>  | Attribute name-value pairs         | M       |          |
| signature                 | CredentialSignature           | Signature information              | M       |          |
| signatureCorrectnessProof | SignatureCorrectnessProof     | Proof of correctness of the signature | M    |          |
<br>

## 3. ReferentInfo

### Description

Information about the referents selected by the user for proof generation

### Declaration

```java
public class ReferentInfo {
    Map<String, Referent> referents;
}
```

### Property

| Name      | Type                   | Description                                 | **M/O** | **Note** |
|-----------|------------------------|---------------------------------------------|---------|----------|
| referents | Map<String, Referent> | Reference information based on credentialId | M       |          |
<br>

## 4. AvailableReferent

### Description

Information on attributes in the holder's credentials that can be used for generating a proof

### Declaration

```java
public class AvailableReferent {
    Map<String, AttrReferent> selfAttrReferent;
    Map<String, AttrReferent> attrReferent;
    Map<String, PredicateReferent> predicateReferent;
}
```

### Property

| Name              | Type                            | Description                  | **M/O** | **Note** |
|-------------------|----------------------------------|------------------------------|---------|----------|
| selfAttrReferent  | Map<String, AttrReferent>       | Self-attested attributes     | O       |          |
| attrReferent      | Map<String, AttrReferent>       | General attributes to submit | O       |          |
| predicateReferent | Map<String, PredicateReferent>  | Predicate-based attributes   | O       |          |



### 4.1. AttrReferent

#### Description

An object representing attribute-related information used in the proof generation process. It allows selective disclosure of attributes to the verifier.

#### Declaration

```java
public class AttrReferent {
    String name;
    boolean checkRevealed;
    List<SubReferent> attrSubReferent;
}
```

#### Property

| Name              | Type                    | Description              | **M/O** | **Note** |
|-------------------|-------------------------|--------------------------|---------|----------|
| name              | String                  | Attribute name           | O       |          |
| checkRevealed     | boolean                 | Whether it is revealed   | O       |          |
| attrSubReferent   | List<SubReferent>       | SubReferent              | O       |          |


### 4.2. PredicateReferent

#### Description

Used to verify specific conditions on the attribute values.

#### Declaration

```java
public class PredicateReferent {
    String name;
    boolean checkRevealed;
    List<SubReferent> attrSubReferent;
}
```

#### Property

| Name              | Type                    | Description              | **M/O** | **Note** |
|-------------------|-------------------------|--------------------------|---------|----------|
| name              | String                  | Attribute name           | O       |          |
| checkRevealed     | boolean                 | Whether it is revealed   | O       |          |
| attrSubReferent   | List<SubReferent>       | SubReferent              | O       |          |


### 4.3. SubReferent

#### Description

Contains information related to a specific attribute.

#### Declaration

```java
public class SubReferent {
    String raw;
    String credentialId;
    String credentialDefId;
}
```

#### Property

| Name            | Type     | Description                                         | **M/O** | **Note** |
|------------------|----------|-----------------------------------------------------|---------|----------|
| raw              | String   | Raw value of the attribute                          | O       |          |
| credentialId     | String   | Credential ID that contains the attribute           | O       |          |
| credentialDefId  | String   | Definition of credential schema by the issuer      | O       |          |


## 5. Proof

### Description

Final proof object generated based on ZKP

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

| Name            | Type              | Description                        | **M/O** | **Note** |
|------------------|-------------------|------------------------------------|---------|----------|
| proofs          | List<SubProof>    | List of sub-proofs                 | M       |          |
| aggregatedProof | AggregatedProof   | Aggregated ZKP proof value         | M       |          |
| requestedProof  | RequestedProof    | Proof corresponding to requested attributes | M |     |
| identifiers     | List<Identifiers> | Credential identifiers used        | M       |          |

<br>

### 5.1. SubProof

#### Description

Contains basic proof information.

#### Declaration

```java
public class SubProof {
    PrimaryProof primaryProof;
}
```

#### Property

| Name         | Type          | Description     | **M/O** | **Note** |
|--------------|---------------|-----------------|---------|----------|
| primaryProof | PrimaryProof  | Primary proof   | O       |          |

<br>

### 5.2. AggregatedProof

#### Description

A structure that aggregates multiple proofs into a single ZKP

#### Declaration

```java
public class AggregatedProof {
    BigInteger cHash;
    List<String> cList;
}
```

#### Property

| Name  | Type       | Description                         | **M/O** | **Note** |
|-------|------------|-------------------------------------|---------|----------|
| cHash | BigInteger | Aggregated challenge value          | M       |          |
| cList | List       | Challenge values of sub-proofs      | M       |          |

<br>

### 5.3. RequestedProof

#### Description

Classifies the actual attributes submitted in response to the requested attributes.

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

| Name              | Type                             | Description                     | **M/O** | **Note** |
|-------------------|----------------------------------|---------------------------------|---------|----------|
| selfAttestedAttrs | Map<String, String>              | Self-attested attributes        | O       |          |
| revealedAttrs     | Map<String, Map<String,String>>  | Revealed attributes             | O       |          |
| unrevealedAttrs   | Map<String, Map<String,String>>  | Hidden attributes               | O       |          |
| predicates        | Map<String, Map<String,String>>  | Predicate-based attribute proof | O       |          |

<br>

### 5.4. Identifiers

#### Description

Contains the identifiers used to build the proof.

#### Declaration

```java
public class Identifiers {
    String schemaId;
    String credDefId;
}
```

#### Property

| Name      | Type   | Description                         | **M/O** | **Note** |
|-----------|--------|-------------------------------------|---------|----------|
| schemaId  | String | Referenced Credential Schema ID     | M       |          |
| credDefId | String | Referenced Credential Definition ID | M       |          |

<br>
