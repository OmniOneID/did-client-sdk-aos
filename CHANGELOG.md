# Changelog

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

