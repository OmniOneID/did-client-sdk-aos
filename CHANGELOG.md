# Changelog

## v3.0.0 (2026-10-30)

### 🚀 New Features
- OID4VC Credential Issuance
    - Added DID-based SD-JWT VC (`dc+sd-jwt-did`) credential issuance.
    - Added DID-based mdoc (`mso_mdoc-did`) credential issuance.
    - Added `requestRevokeOID4VC` for OID4VC credential revocation.
- OID4VP Presentation
    - Added `opendid_vc` credential presentation.
    - Added DID-based SD-JWT VC (`dc+sd-jwt-did`) credential presentation.
    - Added DID-based mdoc (`mso_mdoc-did`) credential presentation.
    - Rejects an unsupported `response_mode` and revalidates the OID4VP selection.
- mdoc Proximity Presentation
    - Exposed the mdoc device response for proximity presentation.
    - Aligned the mdoc proximity API with the ISO/IEC 18013-5 draft.
- Wallet Token
    - Added `createLocalWalletToken` to issue a wallet token locally.
- Certificate VC Verification
    - `createEncVp` and `createEncZkpProof` verify the certificate VC before creating the presentation.
- Others
    - Added a `getDidDoc` overload with `versionId` in `WalletUtil`.
    - Added the `userId` field to `P210RequestVo` for proxy server integration.

### 🐞 Bug Fixes
- Treats the full claim code set as present-all in `makePresentation`.
- Uses the DID itself as the verification method controller.
- Keeps every ZKP requested item on its own referent key.

### 🔧 Build and Dependencies
- Upgraded the Android Gradle Plugin to 9.1.0 and moved dependencies to the version catalog.
- Removed unused dependencies.

## v2.0.1 (2025-11-29)

### 🚀 New Features
- Passcode Authentication
    - Added passcode authentication function.
    - Refactored terminology to use "passcode" instead of "pin" for consistency.
    - Updated documentation for authenticatePin
- Wallet and Key Management
    - Added deletekey API and key token support.
    - Implemented optional wallet deletion function and streamlined wallet deletion logic.
    - Added isAnyCredentialSaved API to check for saved credentials.
- DID and User Updates
    - Added support for DID Update and UPDATE_DID in key generation.
    - Integrated HTTP access token handling and user update parameters.
- Biometric and Security Enhancements
    - Modified biometric prompt and added connection access token.
    - Improved logic to delete existing keys and keystore aliases during biometric re-registration.
    - Added handling for HTTP 401 Unauthorized errors.


## v2.0.0 (2025-04-30)

### 🚀 New Features
- Unified SDK architecture
    - Merged Core SDK, Utility SDK, DataModel SDK, Communication SDK, and Wallet SDK into a single Wallet SDK module for streamlined usage and maintenance.
- Added ZKP (Zero-Knowledge Proof) core functionality
    - Integrated credential issuance and verification flows based on Indy AnonCreds ZKP.
    - Supports credential request generation, proof creation, and selective disclosure.
    - Added support for blinded credentials and proof validation via master secrets.


## v1.0.0 (2024-10-18)

### 🚀 New Features
- Core SDK
    - DID Document management(Generation, Retrieval, Deletion)
    - VerifiableCredential management(Storage, Retrieval, Deletion)
    - VerifiablePresentation generation
    - Key management for encryption, decryption and signing
- Utility SDK
    - Data encryption and decryption
    - Key generation using PBKDF
    - Shared Secrets Generation for ECDH
    - Multibase encoding and decoding
    - Hash algorithms
- DataModel SDK
    - Value object for Mobile Wallet (DID, VC, VP, Profile, etc)
- Communication SDK
    - Manages HTTP requests and responses, supporting GET and POST methods with JSON payloads.
- Wallet SDK
    - Token management to access wallet
    - Wallet lock/unlock management
    - Provides core and service functions
- Token management to access wallet
- Wallet lock/unlock management
- Provides core and service functions

