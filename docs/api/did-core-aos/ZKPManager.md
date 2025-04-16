# Android ZKPManager Core SDK API (English Version)

- Module: ZKPManager
- Author: Dongjun Park
- Date: 2025-04-07
- Version: v1.0.0

| Version | Date       | Description     |
| ------- | ---------- | --------------- |
| v1.0.0  | 2025-04-07 | Initial release |

# Table of Contents

- [APIs](#api-list)
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

# API List

## 1. constructor

### Description
Java constructor for ZKPManager.

### Declaration
```java
ZKPManager(Context context);
ZKPManager(String fileName, Context context);
```

### Parameters
| Parameter | Type    | Description          | **M/O** | Notes           |
| --------- | ------- | -------------------- | ------- | --------------- |
| fileName  | String  | Name of storage file | O       | Optional naming |
| context   | Context | Android context      | M       |                 |

### Returns
| Type       | Description         | **M/O** |
| ---------- | ------------------- | ------- |
| ZKPManager | ZKPManager instance | M       |


## 2. addCredentials

### Description
Stores a given credential.

### Declaration
```java
void addCredentials(Credential credential, String credentialId)
```

### Parameters
| Parameter    | Type       | Description               |
| ------------ | ---------- | ------------------------- |
| credential   | Credential | Credential to be stored   |
| credentialId | String     | Identifier of the credential |

### Returns
void

## 3. getCredentials

### Description
Search and retrieve credentials by ID.

### Declaration
```java
List<CredentialInfo> getCredentials(List<String> identifiers)
```

### Parameters
| Parameter   | Type         | Description        |
| ----------- | ------------ | ------------------ |
| identifiers | List<String> | List of credential IDs |

### Returns
| Type                  | Description              |
| --------------------- | ------------------------ |
| List<CredentialInfo>  | List of stored credentials |

## 4. getAllCredentials

### Description
Retrieves all stored credentials.

### Declaration
```java
ArrayList<CredentialInfo> getAllCredentials()
```

### Returns
| Type                     | Description               |
| ------------------------ | ------------------------- |
| ArrayList<CredentialInfo> | All stored credentials   |

## 5. deleteCredentials

### Description
Deletes the credentials specified by the list of IDs.

### Declaration
```java
void deleteCredentials(List<String> identifiers)
```

### Parameters
| Parameter   | Type         | Description            |
| ----------- | ------------ | ---------------------- |
| identifiers | List<String> | List of credential IDs |

### Returns
void

## 6. deleteAllCredentials

### Description
Deletes all stored credentials.

### Declaration
```java
void deleteAllCredentials()
```

### Returns
void

## 7. createCredentialRequest

### Description
Creates a ZKP credential request.

### Declaration
```java
ZkpRequestCredentialBuilder createCredentialRequest(String proverDid,
                                                    CredentialPrimaryPublicKey credentialPublicKey,
                                                    CredentialOffer credOffer,
                                                    LinkedHashMap<String, String> credentialValueMap,
                                                    BigInteger proverNonce)
```

### Parameters
| Parameter           | Type                           | Description             |
| ------------------- | ------------------------------ | ----------------------- |
| proverDid           | String                         | Prover's DID            |
| credentialPublicKey | CredentialPrimaryPublicKey     | Credential Public Key   |
| credOffer           | CredentialOffer                | Credential Offer object |
| credentialValueMap  | LinkedHashMap<String, String> | Attribute-value mapping |
| proverNonce         | BigInteger                     | Prover's nonce          |

### Returns
| Type                        | Description                 |
| --------------------------- | --------------------------- |
| ZkpRequestCredentialBuilder | Credential Request builder  |

## 8. verifyAndStoreCredential

### Description
Verifies and stores the issued credential.

### Declaration
```java
boolean verifyAndStoreCredential(MasterSecretBlindingData masterSecretBlindingData,
                                  CredentialPrimaryPublicKey credentialPrimaryPublicKey,
                                  String credentialId,
                                  Credential credential,
                                  BigInteger proverNonce)
```

### Parameters
| Parameter                  | Type                       | Description                   |
| -------------------------- | -------------------------- | ----------------------------- |
| masterSecretBlindingData   | MasterSecretBlindingData   | Blinded master secret data    |
| credentialPrimaryPublicKey | CredentialPrimaryPublicKey | Credential Public Key         |
| credentialId               | String                     | Credential ID                 |
| credential                 | Credential                 | Credential object             |
| proverNonce                | BigInteger                 | Nonce provided by the prover  |

### Returns
| Type    | Description         |
| ------- | ------------------- |
| boolean | Verification result |

## 9. searchCredentials

### Description
Searches for credentials matching the proof request and generates a list of usable referents.

### Declaration
```java
ZkpSearchCredentialBuilder searchCredentials(ProofRequest proofRequest)
```

### Parameters
| Parameter    | Type         | Description           |
| ------------ | ------------ | --------------------- |
| proofRequest | ProofRequest | The ZKP proof request |

### Returns
| Type                       | Description                |
| -------------------------- | -------------------------- |
| ZkpSearchCredentialBuilder | Builder for credential search |

## 10. createReferent

### Description
Generates referents based on user-selected referent inputs for each credential.

### Declaration
```java
ZkpCreateReferentBuilder createReferent(List<UserReferent> customReferents)
```

### Parameters
| Parameter       | Type              | Description                 |
| --------------- | ----------------- | --------------------------- |
| customReferents | List<UserReferent> | User-defined referent inputs |

### Returns
| Type                     | Description        |
| ------------------------ | ------------------ |
| ZkpCreateReferentBuilder | Referent builder   |

## 11. createProof

### Description
Generates a ZKP proof based on the user's credentials and referents.

### Declaration
```java
ZkpCreateProofBuilder createProof(ProofRequest proofRequest,
                                  List<ProofParam> proofParams,
                                  Map<String, String> selfAttributes)
```

### Parameters
| Parameter      | Type                 | Description                   |
| -------------- | -------------------- | ----------------------------- |
| proofRequest   | ProofRequest         | The ZKP proof request         |
| proofParams    | List<ProofParam>     | Parameters mapping referents  |
| selfAttributes | Map<String, String> | Self-attested attributes      |

### Returns
| Type                  | Description    |
| --------------------- | -------------- |
| ZkpCreateProofBuilder | Proof builder  |
