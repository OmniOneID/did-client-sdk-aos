### New Structure:

docs/ 
├── did-wallet-sdk-aos/ 
├── did-communication-sdk-aos/ 
├── did-core-sdk-aos/ 
├── did-utility-sdk-aos/ 
└── did-datamodel-sdk-aos/

All documents from the above folders are now moved to either:

- `public/` — if they describe public APIs, interfaces, or expected usage patterns
- `private/` — if they describe internal modules, helper utilities, or low-level SDK logic not meant for public consumption

 Benefits of this Structure

- Improves clarity for internal vs. external API boundaries
- Reduces accidental exposure of internal implementation details


## 📂 Folder Descriptions

### `public/`

This directory contains documentation for APIs and components that are:

- Exposed to external developers or service integrators
- Considered part of the **official public SDK**
- Safe to rely on for integration and long-term support

✅ Examples:
- Wallet Interface APIs  
- Credential issuance and verification workflows  
- Public encryption/decryption utility wrappers

### `private/`

This directory contains documentation for:

- Internal helper classes and base components
- Logic not intended for external access
- Modules still under development or subject to change without notice