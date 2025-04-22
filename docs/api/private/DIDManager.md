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

Android DIDManager Core SDK API
==

- Topic: DIDManager
- Author: Sangjun Kim
- Date: 2024-07-10
- Version: v1.0.0

| Version | Date       | Changes                  |
| ------- | ---------- | ------------------------ |
| v1.0.0  | 2024-07-10 | Initial version          |


<div style="page-break-after: always;"></div>

# Table of Contents
- [APIs](#api-list)
    - [1. constructor](#1-constructor)
    - [2. genDID](#2-isanykeysaved)
    - [3. isSaved](#3-issaved)
    - [4. createDocument](#4-createdocument)
    - [5. getDocument](#5-getdocument)
    - [6. replaceDocument](#6-replacedocument)
    - [7. saveDocument](#7-savedocument)
    - [8. deleteDocument](#8-deletedocument)
    - [9. addVerificationMethod](#9-addverificationmethod)
    - [10. removeVerificationMethod](#10-removeverificationmethod)
    - [11. addService](#11-addservice)
    - [12. removeService](#12-removeservice)
    - [13. resetChanges](#13-resetchanges)
- [Enumerators](#enumerators)
    - [1. DID_METHOD_TYPE](#1-did_method_type)
    - [2. DID_KEY_TYPE](#2-did_key_type)
- [Value Object](#value-object)
    - [1. DIDDocument](#1-diddocument)
    - [2. DIDKeyInfo](#2-didkeyinfo)
    - [3. KeyInfo](#3-keyinfo)
    - [4. Service](#4-service)

# API List
## 1. Constructor

### Description
`DIDManager constructor`

### Declaration

```java
DIDManager(String fileName, Context context);
```

### Parameters

| Parameter | Type   | Description                | **M/O** | **Notes** |
|-----------|--------|----------------------------|---------|-----------|
| fileName  | string | File name                  | M       | Name of the wallet file to be saved in DIDManager |
| context   | Context| Context                    | M       |           |

### Returns

| Type        | Description      | **M/O** | **Notes** |
|-------------|------------------|---------|-----------|
| DIDManager  | DIDManager object| M       |           |

### Usage
```java
DIDManager<DIDDocument> didManager = new DIDManager<>("MyWallet", this);
```

<br>


## 2. genDID

### Description
`Returns a random DID string including the methodName.`

### Declaration

```java
static String genDID(String methodName)
```

### Parameters

| Name       | Type   | Description          | **M/O** | **Note** |
|------------|--------|----------------------|---------|----------|
| methodName | String | Method name to use   | M       |          |

### Returns

| Type   | Description  | **M/O** | **Notes**                                                         |
|--------|--------------|---------|-------------------------------------------------------------------|
| String | DID string   | M       | Returns a string in the following format: <br>`did:omn:3kH8HncYkmRTkLxxipTP9PB3jSXB` |

### Usage
```java
String did = DIDManager.genDID("MyDID");
```

<br>

## 3. isSaved

### Description
`Returns whether a DID document is saved in the wallet.`

### Declaration

```java
boolean isSaved()
```

### Parameters

n/a

### Returns

| Type    | Description                 | **M/O** | **Notes** |
|---------|-----------------------------|---------|-----------|
| boolean | Indicates if a DID document is saved | M       |           |

### Usage
```java
DIDManager<DIDDocument> didManager = new DIDManager<>("MyWallet", this);

if (didManager.isSaved()) {
  DIDDocument didDocument = didManager.getDocument();
}
```

<br>

## 4. createDocument

### Description
`Creates a "temporary DIDDocument object" and manages it as an internal variable.`

### Declaration

```java
void createDocument(String did, List<DIDKeyInfo> keyInfos, String controller, List<Service> services) throws Exception
```

### Parameters
| Parameter   | Type                             | Description                                                  | **M/O** | **Notes**                                          |
|-------------|----------------------------------|--------------------------------------------------------------|---------|----------------------------------------------------|
| did         | String                           | User DID                                                     | M       | Refer to [genDID](#2-gendid) for DID generation     |
| keyInfos    | List<DIDKeyInfo>                 | Array of public key information objects to register in DID document | M       | Refer to [DIDKeyInfo](#2-didkeyinfo)               |
| controller  | String                           | DID to register as controller in the DID document.<br>If null, the `did` parameter is used. | M       |                                                    |
| services    | List<Service>                    | Service information objects to specify in the DID document   | M       | Refer to [Service](#4-service)                     |

### Returns
void

### Usage
```java
DIDManager<DIDDocument> didManager = new DIDManager<>("MyWallet", this);

didManager.createDocument(did, didKeyInfos, did, null);
```

<br>

## 5. getDocument

### Description
`Returns the "temporary DIDDocument object". If null, returns the saved DID document.`

### Declaration

```java
DIDDocument getDocument() throws Exception
```

### Parameters

n/a

### Returns

| Type        | Description | **M/O** | **Notes**                           |
|-------------|--------------|---------|-------------------------------------|
| DIDDocument | DID document | M       | Refer to [DIDDocument](#1-diddocument) |

### Usage
```java
DIDManager<DIDDocument> didManager = new DIDManager<>("MyWallet", this);

if (didManager.isSaved()) {
  DIDDocument didDocument = didManager.getDocument();
}
```

<br>

## 6. replaceDocument

### Description
`Replaces the "temporary DIDDocument object" with the provided object.`

### Declaration

```java
void replaceDocument(DIDDocument didDocument, boolean needUpdate)
```

### Parameters

| Parameter   | Type        | Description                                                                                             | **M/O** | **Notes**                                                                                                                                                               |
|-------------|-------------|---------------------------------------------------------------------------------------------------------|---------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| didDocument | DIDDocument | The DID document object to replace                                                                      | M       | Refer to [DIDDocument](#1-diddocument)                                                                                                                                  |
| needUpdate  | boolean     | Whether to update the `updated` attribute of the DID document object to the current time                | M       | If the DID document object has no proof added, you may use `true` as needed. <br>However, if proof is added, use `false` to preserve the original signed document. |

### Returns
void

### Usage
```java
DIDManager<DIDDocument> didManager = new DIDManager<>("MyWallet", this);

DIDDocument newDIDDocument = new DIDDocument();
didManager.replaceDocument(newDIDDocument, false);
```

<br>

## 7. saveDocument

### Description
`Saves the "temporary DIDDocument object" to the wallet file and then initializes it.
If called when there are no changes to the already saved file, i.e., the "temporary DIDDocument object" is null, it does nothing.`

### Declaration

```java
void saveDocument() throws Exception
```

### Parameters

n/a

### Returns

void

### Usage
```java
DIDManager<DIDDocument> didManager = new DIDManager<>("MyWallet", this);

DIDDocument newDIDDocument = new DIDDocument();
didManager.replaceDocument(newDIDDocument, false);

didManager.saveDocument();
```

<br>

## 8. deleteDocument

### Description
`Deletes the saved wallet file. After deleting the file, initializes the "temporary DIDDocument object" to null.`

### Declaration

```java
void deleteDocument() throws Exception
```

### Parameters

n/a

### Returns

void

### Usage
```java
DIDManager<DIDDocument> didManager = new DIDManager<>("MyWallet", this);

didManager.deleteDocument();
```

<br>

## 9. addVerificationMethod

### Description
`Adds public key information to the "temporary DIDDocument object".
It is mainly used to add new public key information to the saved DID document.`

### Declaration

```java
void addVerificationMethod(DIDKeyInfo keyInfo) throws Exception
```

### Parameters

| Parameter | Type        | Description      | **M/O** | **Notes**                   |
|-----------|-------------|------------------|---------|-----------------------------|
| keyInfo   | DIDKeyInfo  | Key ID list      | M       | Refer to [DIDKeyInfo](#2-didkeyinfo) |

### Returns

void

### Usage
```java
// KeyManager to generate key pair to add
KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("MyWallet", this);

WalletKeyGenRequest keyGenInfo = new WalletKeyGenRequest(
    "PIN",
    AlgorithmType.ALGORITHM_TYPE.SECP256R1,
    StorageOption.STORAGE_OPTION.WALLET,
    new KeyGenWalletMethodType("password".getBytes())
);
keyManager.generateKey(keyGenInfo);
List<KeyInfo> keyInfos = keyManager.getKeyInfos(List.of("PIN"));

DIDManager<DIDDocument> didManager = new DIDManager<>("MyWallet", this);

DIDKeyInfo didKeyInfo = new DIDKeyInfo(
    keyInfos.get(0),
    DIDMethodType.DID_METHOD_TYPE.assertionMethod,
    did
);
didManager.addVerificationMethod(didKeyInfo);
```

<br>

## 10. removeVerificationMethod

### Description
`Removes public key information from the "temporary DIDDocument object".
It is mainly used to remove registered public key information from the saved DID document.`

### Declaration

```java
void removeVerificationMethod(String keyId) throws Exception
```

### Parameters

| Parameter | Type   | Description                             | **M/O** | **Notes** |
|-----------|--------|-----------------------------------------|---------|-----------|
| keyId     | string | ID of the public key information to remove from the DID document | M       |           |

### Returns

void

### Usage
```java
DIDManager<DIDDocument> didManager = new DIDManager<>("MyWallet", this);

didManager.removeVerificationMethod("PIN");
```

<br>

## 11. addService

### Description
`Adds service information to the "temporary DIDDocument object".
It is mainly used to add registered service information to the saved DID document.`

### Declaration

```java
void addService(Service service) throws Exception
```

### Parameters

| Parameter | Type    | Description                      | **M/O** | **Notes**              |
|-----------|---------|----------------------------------|---------|------------------------|
| service   | Service | Service information object to specify in the DID document | M       | Refer to [Service](#4-service) |

### Returns

void

### Usage
```java
DIDManager<DIDDocument> didManager = new DIDManager<>("MyWallet", this);

Service service = new Service("serviceId", DIDServiceType.DID_SERVICE_TYPE.credentialRegistry, List.of("http://serviceEndpoint"));
didManager.addService(service);
```

<br>

## 12. removeService

### Description
`Removes service information from the "temporary DIDDocument object".
It is mainly used to remove registered service information from the saved DID document.`

### Declaration

```java
void removeService(String serviceId) throws Exception
```

### Parameters

| Parameter | Type   | Description                  | **M/O** | **Notes** |
|-----------|--------|------------------------------|---------|-----------|
| serviceId | String | ID of the service to remove from the DID document | M       |           |

### Returns

void

### Usage
```java
DIDManager<DIDDocument> didManager = new DIDManager<>("MyWallet", this);

didManager.removeService("serviceId");
```

<br>

## 13. resetChanges

### Description
`Resets the "temporary DIDDocument object" to null to discard changes.
Throws an error if there is no saved DID document file. This can only be used when a saved DID document file exists.`

### Declaration

```java
void resetChanges() throws Exception
```

### Parameters
n/a

### Returns

void

### Usage
```java
DIDManager<DIDDocument> didManager = new DIDManager<>("MyWallet", this);

didManager.resetChanges();
```

<br>

# Enumerators
## 1. DID_METHOD_TYPE

### Description

`Specifies the purpose of the key registered in the DID document.`

### Declaration

```java
public enum DID_METHOD_TYPE {
    assertionMethod(1),
    authentication(1<<1),
    keyAgreement(1<<2),
    capabilityInvocation(1<<3),
    capabilityDelegation(1<<4);
}
```

<br>

## 2. DID_KEY_TYPE

### Description

`Signature key type`

### Declaration

```java
public enum DID_KEY_TYPE {
    rsaVerificationKey2018("RsaVerificationKey2018"),
    secp256k1VerificationKey2018("Secp256k1VerificationKey2018"),
    secp256r1VerificationKey2018("Secp256r1VerificationKey2018");
}
```
<br>

# Value Object

## 1. DIDDocument

### Description

`DID document object (provided by DataModel SDK)`
[Link]

### Declaration

```java
public class DIDDocument {

    @SerializedName("@context")
    List<String> context;
    String id;
    String controller;
    List<VerificationMethod> verificationMethod;
    List<String> assertionMethod;
    List<String> authentication;
    List<String> keyAgreement;
    List<String> capabilityInvocation;
    List<String> capabilityDelegation;
    List<Service> service;
    String created; //utcDateTime
    String updated; //utcDateTime
    String versionId; //DIDVersionId
    boolean deactivated;
    Proof proof;
    List<Proof> proofs;
}
```

<br>

## 2. DIDKeyInfo

### Description

`Public key information object to register in the DID document.`

### Declaration

```java
public class DIDKeyInfo {
    KeyInfo keyInfo;
    DIDMethodType.DID_METHOD_TYPE methodType;
    String controller;
}
```

### Property

| Name        | Type                 | Description                                                        | **M/O** | **Note**                                                |
|-------------|----------------------|--------------------------------------------------------------------|---------|---------------------------------------------------------|
| keyInfo     | KeyInfo              | Public key information object returned by KeyManager               | M       |                                                         |
| methodType  | DID_METHOD_TYPE      | Type specifying the purpose of the public key registered in the DID document | M       |                                                         |
| controller  | String               | DID to register as the controller of the public key in the DID document. If null, the ID of the DID document is used as the controller. | M       |                                                         |

<br>

## 3. KeyInfo

### Description

`Key information object retrieved from KeyManager.`[Link]

### Declaration

```java
public class KeyInfo extends Meta {
    AuthType.AUTH_TYPE authType;
    AlgorithmType.ALGORITHM_TYPE algorithmType;
    String publicKey;
    KeyAccessMethod.KEY_ACCESS_METHOD accessMethod;
}
```

<br>

## 4. Service

### Description

`Service object in the DID document (provided by DataModel SDK).` [Link]

### Declaration

```java
public class Service {
    String id;
    DIDServiceType.DID_SERVICE_TYPE type;
    List<String> serviceEndpoint;
}
```