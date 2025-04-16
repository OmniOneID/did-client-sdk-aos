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
- Author: Sangjun Kim
- Date: 2024-08-12
- Version: v1.0.0

| Version          | Date       | Changes                  |
| ---------------- | ---------- | ------------------------ |
| v1.0.0  | 2024-08-12 | Initial version          |

<div style="page-break-after: always;"></div>

# Table of Contents

- [WalletCoreError](#walletcoreerror)
- [Error Code](#error-code)
  - [1. Common](#1-common)
  - [2. KeyManager](#2-keymanager)
  - [3. DIDManager](#3-didmanager)
  - [4. VCManager](#4-vcmanager)
  - [5. StorageManager](#5-storagemanager)
  - [6. Signable](#5-signable)
  - [7. Keystore](#7-keystore)


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
}
```

### Property

| Name               | Type       | Description                            | **M/O** | **Note**              |
|--------------------|------------|----------------------------------------|---------|-----------------------|
| code               | String     | Error code    |    M    |                       | 
| message            | String     | Error description                      |    M    |                       | 

<br>

# Error Code
## 1. Common

| Error Code   | Error Message                        | Description                       | Action Required                   |
|--------------|--------------------------------------|-----------------------------------|-----------------------------------|
| MSDKWLTxx000 | Invalid parameter : {name}           | Given parameter is invalid        | Check proper data type and length |
| MSDKWLTxx001 | Duplicate parameter : {name}         | Given parameters are duplicated   | Check the array value             |
| MSDKWLTxx002 | Fail to decode : {name}              | Given data couldn't be decoded    | Check the data is valid           |

<br>

## 2. KeyManager
| Error Code   | Error Message               | Description                       | Action Required                   |
|--------------|-----------------------------|-----------------------------------|-----------------------------------|
| MSDKWLT00100 | Given key id already exists | -                                 | Use not duplicated key id         |
| MSDKWLT00101 | Given keyGenRequest does not<br>conform to {Wallet/Secure}KeyGenRequest | - | Use WalletKeyGenRequest when generate wallet key <br>or SecureKeyGenRequest for secure key |
| MSDKWLT00200 | Given new pin is equal to old pin | -                                 | Use another new pin               |
| MSDKWLT00201 | Given key id is not pin auth type | -                                 | Use pin key id                    |
| MSDKWLT00300 | Error occurs while evaluate policy : {detail error} | -                                             | Depend on detail error cases                  |
| MSDKWLT00301 | Cannot get domain state                             | Cannot get domain state value from the system | Use another new pin                           |
| MSDKWLT00302 | User biometrics changed                             | -                                             | Current Secure key is expired.<br>Use new one |
| MSDKWLT00400 | Found no key by given keyType        | -                                 | Generate proper key or change keyType |
| MSDKWLT00401 | Insufficient result by given keyType | -                                 | Generate proper key or change keyType |
| MSDKWLT00900 | Given algorithm is unsupported    | -                                 | Use supported algorithm           |

<br>

## 3. DIDManager

| Error Code              | Error Message                                      | Description                                      | Action Required                                |
|-------------------------|----------------------------------------------------|--------------------------------------------------|------------------------------------------------|
| MSDKWLT01100            | Fail to generate random                            | Failed to generate a random value                | Ensure proper random generation procedure       |
| MSDKWLT01200            | Document is already exists                         | The DID document already exists                  | Avoid creating a duplicate DID document         |
| MSDKWLT01300            | Duplicate key ID exists in verification method     | A key ID is duplicated within a verification method | Ensure unique key IDs within verification method |
| MSDKWLT01301            | Not found key ID in verification method            | A specified key ID was not found in verification method | Verify the correct key ID is used              |
| MSDKWLT01302            | Duplicate service ID exists in service             | A service ID is duplicated within the service    | Ensure unique service IDs within the service    |
| MSDKWLT01303            | Not found service ID in service                    | A specified service ID was not found in the service | Verify the correct service ID is used         |
| MSDKWLT01900            | Don't call 'resetChanges' if no document saved     | The 'resetChanges' method was called without a saved document | Ensure the document is saved before calling resetChanges |
| MSDKWLT01901            | Unexpected condition occurred                      | An unexpected error occurred                      | Investigate the error cause and resolve it      |

<br>

## 4. VCManager

| Error Code              | Error Message                                     | Description                                      | Action Required                                |
|-------------------------|---------------------------------------------------|--------------------------------------------------|------------------------------------------------|
| MSDKWLT02100            | No claim code in credential(VC) for presentation  | No claim code found in the provided credential   | Ensure the credential contains the necessary claim code |

<br>

## 5. StorageManager

| Error Code              | Error Message                                         | Description                                          | Action Required                                    |
|-------------------------|-------------------------------------------------------|------------------------------------------------------|----------------------------------------------------|
| MSDKWLT10100            | Fail to save wallet                                   | Failed to save the wallet                            | Check the save procedure and ensure it's correct   |
| MSDKWLT10101            | Item duplicated with it in wallet                     | An item is duplicated within the wallet              | Ensure items are unique within the wallet          |
| MSDKWLT10200            | No item to update in wallet                           | There is no item available to update in the wallet   | Verify the item exists before attempting to update |
| MSDKWLT10300            | No items to remove in wallet                          | No items found to remove from the wallet             | Ensure there are items to remove before proceeding |
| MSDKWLT10301            | Fail to remove items from wallet                      | Failed to remove items from the wallet               | Investigate and resolve the issue causing the failure |
| MSDKWLT10400            | No items saved in wallet                              | No items have been saved in the wallet               | Save items to the wallet before accessing them     |
| MSDKWLT10401            | No items to find in wallet                            | No items found in the wallet                         | Verify that the wallet contains items before searching |
| MSDKWLT10402            | Fail to read wallet file                              | Failed to read the wallet file                       | Check the file path and permissions, and try again |
| MSDKWLT10500            | Malformed external wallet format                      | The external wallet format is incorrect              | Ensure the external wallet format is correct       |
| MSDKWLT10501            | Malformed wallet signature                            | The wallet signature is malformed                    | Verify and correct the wallet signature format     |
| MSDKWLT10502            | Malformed inner wallet format                         | The inner wallet format is incorrect                 | Ensure the inner wallet format is correct          |
| MSDKWLT10503            | Malformed item object type about item of inner wallet | The item object type within the inner wallet is incorrect | Verify and correct the item object type         |
| MSDKWLT10900            | Unexpected error occurred                             | An unexpected error occurred                         | Investigate the error cause and resolve it         |

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

  <br>

  ## 8. ZKP
  | Error Code   | Error Message                                             | Description                                               | Action Required                                     |
  |--------------|-----------------------------------------------------------|-----------------------------------------------------------|-----------------------------------------------------|
  | MSDKWLT13100 | Failed to validate parameter : {detail error}             | The input parameters are invalid or incorrectly formatted.| Verify and correct the input parameters.            |
  | MSDKWLT13101 | Failed to generate nonce                                  | Failed to generate a cryptographic nonce.                 |Retry the operation or check entropy sources.        |
  | MSDKWLT13102 | Failed to convert byte to big number                      | Conversion from byte array to big number failed.          |Ensure input bytes are valid and correctly formatted.|
  | MSDKWLT13103 | Failed to parse big number from json                      | Failed to parse big number from JSON format.              | Validate the JSON structure and value types.        |
  | MSDKWLT13104 | Failed to convert hexadecimal string to big number        | Hexadecimal string could not be converted to big number.  |Check if hex string is valid.                        |
  | MSDKWLT13105 | Failed to compare big numbers                             | Comparison of big number values failed.                   | Inspect input values for consistency.               |
  | MSDKWLT13106 | No data in wallet                                         | No corresponding data found in the wallet storage.        | Ensure data is saved before accessing.              |
  | MSDKWLT13107 | Failed to finalize hash computation                       | Final step of hashing process failed.                     | Check hashing input and algorithm compatibility.    |
  | MSDKWLT13108 | Failed to confirm big number is prime                     | The given big number is not a prime number.               | Provide a valid prime number as required.           |
  | MSDKWLT13109 | Failed to calculate TEQ proof                             | TEQ proof calculation failed.                             | Verify cryptographic parameters.                    |
  | MSDKWLT13110 | Failed to calculate TNE proof                             | TNE proof calculation failed.                             | Verify cryptographic parameters.                    |
  | MSDKWLT13111 | Error IO                                                  | An I/O error occurred during operation.                   | Check file paths and system permissions.            |
  | MSDKWLT13112 | No such algorithm                                         | Requested cryptographic algorithm is not available.       | Use a supported algorithm.                          |
  | MSDKWLT13113 | Data is null                                              | Required data is missing or null.                         | Ensure data is properly initialized.                |
  | MSDKWLT13114 | Duplicated key                                            | A key with the same identifier already exists.            | Use a unique key identifier.                        |
  | MSDKWLT13115 | Not supported type                                        | The data type is not supported by the operation.          | Use a valid supported data type.                    |
  | MSDKWLT13116 | Not supported predicate type                              | The predicate type is not supported.                      | Use one of the supported predicate types.           |
  | MSDKWLT13117 | Failed to generate master secret                          | Failed to generate the master secret.                     | Check randomness and retry.                         |
  | MSDKWLT13118 | Failed to verify credential key correctness proof         | Credential key correctness proof verification failed.     | Validate credential key and proof inputs.           |
  | MSDKWLT13119 | Failed to generate new blinded primary credential secrets factors  | Blinded secrets factor generation failed.                 | Check secret input values and retry.       |
  | MSDKWLT13120 | Failed to generate blinded credential secrets correctness proof    | Failed to generate correctness proof for blinded secrets. | Ensure correct secret inputs.              |
  | MSDKWLT13121 | Failed to encode raw data                                 | Failed to encode raw data.                                | Verify raw data format.                             |
  | MSDKWLT13122 | Failed to select master secret from wallet                | Master secret could not be retrieved from wallet.         |	Check if master secret exists in wallet.           |
  | MSDKWLT13123 | Failed to verify signature correctness proof              | Signature correctness proof validation failed.	           | Recheck input values and proof components.          |
  | MSDKWLT13124 | Failed to insert credential into wallet                   | Storing the credential in wallet failed.                  |	Check wallet state and retry.                      |
  | MSDKWLT13125 | Failed to select credential from wallet                   | Failed to retrieve credential from wallet.                |	Ensure credential exists and is accessible.        |
  | MSDKWLT13126 | Not found available request attribute                     | No matching attribute found for the request.              |	Check requested attribute names.                   |
  | MSDKWLT13127 | Not found available predicate attribute                   | No matching predicate attribute found.                    |	Verify predicate attributes in credential.         |
  | MSDKWLT13128 | Failed to build credential for proving                    | Failed to build credential structure for proving.         |	Check credential and referent mapping.             |
  | MSDKWLT13129 | Not found schema from list                                | Schema information not found in provided list.	           | Ensure correct schema ID is included.               |
  | MSDKWLT13130 | Not found credential definition from list                 | Credential definition missing from list.	                 | Verify definition ID exists in input.               |
  | MSDKWLT13131 | Failed to initialize primary equal proof                  | Initialization of primary equal proof failed.             |	Inspect equal predicate values.                    |
  | MSDKWLT13132 | Failed to initialize primary non-equal proof              | Initialization of primary non-equal proof failed.	       | Inspect non-equal predicate values.                 |
  | MSDKWLT13133 | Failed to initialize primary proof                        | Primary proof initialization failed.                      |	Review all related proof parameters.               |
  | MSDKWLT13134 | Failed to finalize primary equal proof                    | Finalization of primary equal proof failed.	             | Check final proof input structure.                  |
  | MSDKWLT13135 | Failed to finalize primary non-equal proof                | Finalization of primary non-equal proof failed.           |	Check final proof input structure.                 |
  | MSDKWLT13136 | Failed to convert hexadecimal string to big number        | Hexadecimal to big number conversion failed.              |	Ensure valid hexadecimal input.                    |

  <br>