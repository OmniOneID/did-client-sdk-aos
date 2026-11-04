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

Android WalletCoreError
==

- Topic: WalletCoreError
- Author: Eunjeong Kim
- Date: 2026-10-28
- Version: v3.0.0

| Version | Date       | Changes         |
| ------- | ---------- | --------------- |
| v3.0.0  | 2026-10-30 | Add OID4VC error; sync codes with WalletCoreErrorCode |
| v2.0.0  | 2025-04-30 | Add ZKP error   |
| v1.0.0  | 2024-08-12 | Initial version |


<div style="page-break-after: always;"></div>

# Table of Contents

- [Android WalletCoreError](#android-walletcoreerror)
- [Table of Contents](#table-of-contents)
  - [WalletCoreError](#walletcoreerror)
    - [Description](#description)
    - [Declaration](#declaration)
    - [Property](#property)
- [Error Code](#error-code)
  - [1. Common](#1-common)
  - [2. KeyManager](#2-keymanager)
  - [3. DIDManager](#3-didmanager)
  - [4. VCManager](#4-vcmanager)
  - [5. StorageManager](#5-storagemanager)
  - [6. Signable](#6-signable)
  - [7. Keystore](#7-keystore)
  - [8. ZKP](#8-zkp)
  - [9. OID4VC](#9-oid4vc)


## WalletCoreError

### Description
```
Error struct for Wallet Core. It has code and message pair.
Code starts with MSDKWLT.
```

### Declaration
```java
public enum WalletCoreErrorCode {
    private final String walletCoreCode = "MSDKWLT";
    private int code;
    private String msg;
    private String feature;
}
```

### Property

| Name               | Type       | Description                            | **M/O** | **Note**              |
|--------------------|------------|----------------------------------------|---------|-----------------------|
| code               | int        | Error code    |    M    |                       | 
| message            | String     | Error description                      |    M    |                       | 
| feature            | String     | Feature code                           |    O    | null for the common codes xx000–xx002 |

<br>

# Error Code
## 1. Common

| Error Code   | Error Message                        | Description                       | Action Required                   |
|--------------|--------------------------------------|-----------------------------------|-----------------------------------|
| MSDKWLTxx000 | Invalid parameter : {name}           | Given parameter is invalid        | Check proper data type and length |
| MSDKWLTxx001 | Duplicated parameter : {name}        | Given parameters are duplicated   | Check the array value             |
| MSDKWLTxx002 | Fail to decode : {name}              | Given data couldn't be decoded    | Check the data is valid           |

<br>

## 2. KeyManager
| Error Code   | Error Message               | Description                       | Action Required                   |
|--------------|-----------------------------|-----------------------------------|-----------------------------------|
| MSDKWLT00100 | Given key id already exists | -                                 | Use not duplicated key id         |
| MSDKWLT00101 | Given keyGenRequest does not<br>conform to {Wallet/Secure}KeyGenRequest | - | Use WalletKeyGenRequest when generate wallet key <br>or SecureKeyGenRequest for secure key |
| MSDKWLT00200 | Given new pin is equal to old pin | -                                 | Use another new pin               |
| MSDKWLT00201 | Given key id is not pin auth type | -                                 | Use pin key id                    |
| MSDKWLT00400 | Found no key by given keyType        | -                                 | Generate proper key or change keyType |
| MSDKWLT00401 | Insufficient result by given keyType | -                                 | Generate proper key or change keyType |
| MSDKWLT00900 | Given algorithm is unsupported    | -                                 | Use supported algorithm           |

<br>

## 3. DIDManager

| Error Code              | Error Message                                      | Description                                      | Action Required                                |
|-------------------------|----------------------------------------------------|--------------------------------------------------|------------------------------------------------|
| MSDKWLT01100            | Fail to generate random                            | Failed to generate a random value                | Ensure proper random generation procedure       |
| MSDKWLT01200            | The document already exists                        | The DID document already exists                  | Avoid creating a duplicate DID document         |
| MSDKWLT01300            | Duplicate key ID exists in verification method     | A key ID is duplicated within a verification method | Ensure unique key IDs within verification method |
| MSDKWLT01301            | Not found key ID in verification method            | A specified key ID was not found in verification method | Verify the correct key ID is used              |
| MSDKWLT01302            | Duplicate service ID exists in service             | A service ID is duplicated within the service    | Ensure unique service IDs within the service    |
| MSDKWLT01303            | Not found service ID in service                    | A specified service ID was not found in the service | Verify the correct service ID is used         |
| MSDKWLT01400            | The stored DID document was not found              | -                                                | Create and save the DID document first          |

<br>

## 4. VCManager

| Error Code              | Error Message                                     | Description                                      | Action Required                                |
|-------------------------|---------------------------------------------------|--------------------------------------------------|------------------------------------------------|
| MSDKWLT02100            | No claim code in credential(VC) for presentation  | No claim code found in the provided credential   | Ensure the credential contains the necessary claim code |

<br>

## 5. StorageManager

| Error Code              | Error Message                                         | Description                                          | Action Required                                    |
|-------------------------|-------------------------------------------------------|------------------------------------------------------|----------------------------------------------------|
| MSDKWLT10100            | Fail to save wallet : {detail error}                  | Failed to save the wallet                            | Check the save procedure and ensure it's correct   |
| MSDKWLT10101            | Item duplicated with it in wallet                     | An item is duplicated within the wallet              | Ensure items are unique within the wallet          |
| MSDKWLT10200            | No item to update in wallet                           | There is no item available to update in the wallet   | Verify the item exists before attempting to update |
| MSDKWLT10300            | No items to remove in wallet                          | No items found to remove from the wallet             | Ensure there are items to remove before proceeding |
| MSDKWLT10301            | Fail to remove items from wallet                      | Failed to remove items from the wallet               | Investigate and resolve the issue causing the failure |
| MSDKWLT10400            | No items saved in wallet                              | No items have been saved in the wallet               | Save items to the wallet before accessing them     |
| MSDKWLT10401            | No items to find in wallet                            | No items found in the wallet                         | Verify that the wallet contains items before searching |
| MSDKWLT10402            | Fail to read wallet file : {detail error}             | Failed to read the wallet file                       | Check the file path and permissions, and try again |
| MSDKWLT10500            | Malformed external wallet format : {detail error}     | The external wallet format is incorrect              | Ensure the external wallet format is correct       |
| MSDKWLT10501            | Malformed wallet signature                            | The wallet signature is malformed                    | Verify and correct the wallet signature format     |
| MSDKWLT10502            | Malformed inner wallet format : {detail error}        | The inner wallet format is incorrect                 | Ensure the inner wallet format is correct          |
| MSDKWLT10503            | Malformed item object type about item of inner wallet : {detail error} | The item object type within the inner wallet is incorrect | Verify and correct the item object type         |
| MSDKWLT10900            | Unexpected error occurred : {detail error}            | An unexpected error occurred                         | Investigate the error cause and resolve it         |

<br>

## 6. Signable

| Error Code   | Error Message                        | Description                       | Action Required                   |
|--------------|--------------------------------------|-----------------------------------|-----------------------------------|
| MSDKWLT11100 | Not proper public key format         | -                                 | -                                 |
| MSDKWLT11101 | Not proper private key format        | -                                 | -                                 |
| MSDKWLT11102 | Private and public keys are not pair | -                                 | -                                 |
| MSDKWLT11200 | Converting failed to compact representation | -                                 | -                                 |
| MSDKWLT11201 | Signing failed : {detail error}             | -                                 | Depend on detail error cases      |
| MSDKWLT11202 | Failed to verify signature : {detail error} | -                                 | Depend on detail error cases      |

<br>

## 7. Keystore

| Error Code   | Error Message                                            | Description                                        | Action Required                   |
|--------------|----------------------------------------------------------|----------------------------------------------------|-----------------------------------|
| MSDKWLT12100 | Failed to create secure key : {detail error}             | Error occurs via Keystore                          | Depend on detail error cases      |
| MSDKWLT12101 | Failed to copy public key                                | Cannot copy public key from SecKey                 | -                                 |
| MSDKWLT12102 | Failed to get public key representation : {detail error} | Cannot copy compressed public key data from SecKey | Depend on detail error cases      |
| MSDKWLT12103 | Cannot find secure key by given conditions               | -                                                  | Generate new key                  |
| MSDKWLT12104 | Failed to delete secure key                              | -                                                  | -                                 |
| MSDKWLT12200 | Signing failed : {detail error}                          | -                                                  | Depend on detail error cases      |
| MSDKWLT12300 | Cannot create encrypted data : {detail error}            | Error occurs via Keystore                          | Depend on detail error cases      |
| MSDKWLT12301 | Cannot create decrypted data : {detail error}            | Error occurs via Keystore                          | Depend on detail error cases      |
| MSDKWLT12400 | No registered bio authentication information             | -                                                  | Register bio key                  |

  <br>

## 8. ZKP
   

| Error Code   | Error Message                                          | Description                                      | Action Required |
| ------------ | ------------------------------------------------       | ------------------------------------------------ | --------------- |
| MSDKWLT04000 | Invalid parameter                                      | Invalid parameter                                | -               |
| MSDKWLT04001 | Duplicated parameter                                   | Duplicated parameter                             | -               |
| MSDKWLT04002 | Fail to decode                                         | Fail to decode                                   | -               |
| MSDKWLT04100 | Data is null                                           | Data is null                                     | -               |
| MSDKWLT04101 | Delta value must be positive                           | Delta value must be positive                     | -               |
| MSDKWLT04102 | Error IO                                               | Error IO                                         | -               |
| MSDKWLT04103 | Not supported type                                     | Not supported type                               | -               |
| MSDKWLT04200 | Master secret not found                                | Master secret not found                          | -               |
| MSDKWLT04201 | Failed to generate master secret                       | Failed to generate master secret                 | -               |
| MSDKWLT04300 | Calculated signature value is invalid. Possibly due to incorrect public key or credential values | Calculated signature value is invalid.           | -               |
| MSDKWLT04301 | Failed to verify credential key correctness proof      | Failed to verify credential key correctness proof  | -              |
| MSDKWLT04302 | Failed to verify credential signature correctness proof| Failed to verify credential signature correctness proof | -         |
| MSDKWLT04400 | Credential not found in storage                        | Credential not found in storage                  | -               |
| MSDKWLT04401 | Credential not found by identifiers in storage         | Credential not found by identifiers in storage   | -               |
| MSDKWLT04500 | Not found available request attribute                  | Not found available request attribute            | -               |
| MSDKWLT04501 | Not found available predicate attribute                | Not found available predicate attribute          | -               |
| MSDKWLT04600 | Failed to build credential for proving                 | Failed to build credential for proving           | -               |
| MSDKWLT04601 | Not found schema from list                             | Not found schema from list                       | -               |
| MSDKWLT04602 | Not found credential definition from list              | Not found credential definition from list        | -               |
| MSDKWLT04603 | Duplicated key                                         | Duplicated key                                   | -               |
| MSDKWLT04900 | Failed to calculate TEQ proof                          | Failed to calculate TEQ proof                    | -               |
| MSDKWLT04901 | Failed to calculate TNE proof                          | Failed to calculate TNE proof                    | -               |
  
  <br>

## 9. OID4VC

| Error Code   | Error Message                                          | Description | Action Required |
| ------------ | ------------------------------------------------------ | ----------- | --------------- |
| MSDKWLT06100 | Unsupported in : {detail error} | - | Depend on detail error cases |
| MSDKWLT06101 | Unsupported format : {detail error} | - | Depend on detail error cases |
| MSDKWLT06210 | Invalid JWE | - | - |
| MSDKWLT06211 | Unsupported algorithm for JWE | - | - |
| MSDKWLT06212 | Unsupported JWE key | - | - |
| MSDKWLT06213 | invalid SealedBox | - | - |
| MSDKWLT06214 | Authentication failed | - | - |
| MSDKWLT06215 | Failed to encrypt | - | - |
| MSDKWLT06216 | Key derivation failed | - | - |
| MSDKWLT06217 | Invalid KDF input | - | - |
| MSDKWLT06300 | Invalid credential response | - | - |
| MSDKWLT06400 | Not found kid for verify | - | - |
| MSDKWLT06401 | Failed to verify signature | - | - |
| MSDKWLT06500 | Presentation format is not supported : {detail error} | - | Depend on detail error cases |
| MSDKWLT06501 | Invalid DCQL query: {detail error} | - | Depend on detail error cases |
| MSDKWLT06502 | No credentials matched the request | - | - |
| MSDKWLT06503 | Required credential_sets not satisfied: {detail error} | - | Depend on detail error cases |
| MSDKWLT06504 | Matched credential not found | - | - |
| MSDKWLT06505 | Holder signing key not found | - | - |
| MSDKWLT06506 | No verifier encryption key found in client_metadata for direct_post.jwt | - | - |
| MSDKWLT06507 | Unsupported response encryption : {detail error} | - | Depend on detail error cases |
| MSDKWLT06508 | Invalid selected credentials: {detail error} | - | Depend on detail error cases |
| MSDKWLT06509 | Unsupported response_mode : {detail error} | - | Depend on detail error cases |
| MSDKWLT06600 | No stored document can answer any docRequest | - | - |
| MSDKWLT06610 | No document was selected | - | - |
| MSDKWLT06611 | Invalid selected mdoc documents: {detail error} | - | Depend on detail error cases |
| MSDKWLT06612 | A selected document carries no claim code | - | - |
| MSDKWLT06613 | The same document is selected twice for the same docRequest | - | - |
| MSDKWLT06614 | A selected document names the same claim code twice | - | - |
| MSDKWLT06620 | Invalid DeviceRequest: {detail error} | - | Depend on detail error cases |
| MSDKWLT06621 | Invalid SessionTranscript: {detail error} | - | Depend on detail error cases |

  <br>
