Android Client SDK
==

Welcome to the Client SDK Repository. <br>
This repository provides an SDK for developing an Android mobile wallet.

## Folder Structure
```
did-client-sdk-aos
├── CLA.md
├── CODE_OF_CONDUCT.md
├── CONTRIBUTING.md
├── LICENSE
├── dependencies-license.md
├── MAINTAINERS.md
├── README.md
├── README_ko.md
├── RELEASE-PROCESS.md
├── SECURITY.md
├── docs
│   └── api
│       ├── did-communication-sdk-aos
│       │   ├── Communication.md
│       │   ├── CommunicationError.md
│       │   └── Communication_ko.md
│       ├── did-core-sdk-aos
│       │   ├── DIDManager.md
│       │   ├── DIDManager_ko.md
│       │   ├── KeyManager.md
│       │   ├── KeyManager_ko.md
│       │   ├── SecureEncryptor.md
│       │   ├── SecureEncryptor_ko.md
│       │   ├── VCManager.md
│       │   ├── VCManager_ko.md
│       │   └── WalletCoreError.md
│       ├── did-datamodel-sdk-aos
│       │   ├── DataModel.md
│       │   └── DataModel_ko.md
│       ├── did-utility-sdk-aos
│       │   ├── Utility.md
│       │   ├── UtilityError.md
│       │   └── Utility_ko.md
│       └── did-wallet-sdk-aos
│           ├── WalletAPI.md
│           ├── WalletAPI_ko.md
│           └── WalletError.md
└── source
    └── did-client-sdk-aos
        ├── build.gradle
        ├── did-communication-sdk-aos
        │   ├── CHANGELOG.md
        │   ├── dependencies-license.md
        │   ├── README.md
        │   ├── README_ko.md
        │   ├── SECURITY.md
        │   ├── build.gradle
        │   └── src
        ├── did-core-sdk-aos
        │   ├── CHANGELOG.md
        │   ├── dependencies-license.md
        │   ├── README.md
        │   ├── README_ko.md
        │   ├── SECURITY.md
        │   ├── build.gradle
        │   └── src
        ├── did-datamodel-sdk-aos
        │   ├── CHANGELOG.md
        │   ├── dependencies-license.md
        │   ├── README.md
        │   ├── README_ko.md
        │   ├── SECURITY.md
        │   ├── build.gradle
        │   └── src
        ├── did-utility-sdk-aos
        │   ├── CHANGELOG.md
        │   ├── dependencies-license.md
        │   ├── README.md
        │   ├── README_ko.md
        │   ├── SECURITY.md
        │   ├── build.gradle
        │   └── src
        ├── did-wallet-sdk-aos
        │   ├── CHANGELOG.md
        │   ├── dependencies-license.md
        │   ├── README.md
        │   ├── README_ko.md
        │   ├── SECURITY.md
        │   ├── build.gradle
        │   └── src
        ├── release
        │   ├── did-communication-sdk-aos-1.0.0.jar
        │   ├── did-core-sdk-aos-1.0.0.jar
        │   ├── did-datamodel-sdk-aos-1.0.0.jar
        │   ├── did-utility-sdk-aos-1.0.0.jar
        │   └── did-wallet-sdk-aos-1.0.0.jar
        └── settings.gradle
```

| Name                    | Description                                     |
| ----------------------- | ----------------------------------------------- |
| source                  | SDK source code project                         |
| docs                    | Documentation                                   |
| ┖ api                   | API guide documentation                         |
| ┖ design                | Design documentation                            |
| sample                  | Samples and data                                |
| README.md               | Overview and description of the project         |
| CLA.md                  | Contributor License Agreement                   |
| CHANGELOG.md            | Version-specific changes in the project         |
| CODE_OF_CONDUCT.md      | Code of conduct for contributors                |
| CONTRIBUTING.md         | Contribution guidelines and procedures          |
| LICENSE                 | Apache 2.0                                      |
| dependencies-license.md | Licenses for the project’s dependency libraries |
| MAINTAINERS.md          | General guidelines for maintaining              |
| RELEASE-PROCESS.md      | Release process                                 |
| SECURITY.md             | Security policies and vulnerability reporting   |

## Libraries

Libraries can be found in the [releases folder](source/did-client-sdk-aos/release).

### Core SDK
1. Copy the `did-core-sdk-aos-1.0.0.jar`, `did-utility-sdk-aos-1.0.0.jar`, `did-datamodel-sdk-aos-1.0.0.jar` file to the libs of the app project.
2. Add the following dependencies to the build.gradle of the app project.

```groovy
    implementation files('libs/did-core-sdk-aos-1.0.0.jar')
    implementation files('libs/did-utility-sdk-aos-1.0.0.jar')
    implementation files('libs/did-datamodel-sdk-aos-1.0.0.jar')
    implementation 'com.google.code.gson:gson:2.10.1'
    implementation 'androidx.biometric:biometric:1.1.0'

    implementation 'com.madgag.spongycastle:core:1.54.0.0'
    implementation 'com.madgag.spongycastle:prov:1.54.0.0'
    implementation 'com.madgag.spongycastle:pkix:1.54.0.0'
    implementation 'com.madgag.spongycastle:pg:1.54.0.0'
```
3. Sync `Gradle` to ensure the dependencies are properly added.


### Utility SDK

1. Copy the `opendid-utility-sdk-aos-1.0.0.jar` file to the `libs` directory of your app project.
2. Add the following dependencies to the build.gradle file of your app project.

```groovy
    implementation files('libs/did-utility-sdk-aos-1.0.0.jar')
    implementation 'com.google.code.gson:gson:2.10.1'
    implementation 'org.bitcoinj:bitcoinj-core:0.15.7'

    implementation 'com.madgag.spongycastle:core:1.54.0.0'
    implementation 'com.madgag.spongycastle:prov:1.54.0.0'
    implementation 'com.madgag.spongycastle:pkix:1.54.0.0'
    implementation 'com.madgag.spongycastle:pg:1.54.0.0'
```

3. Synchronize `Gradle` to ensure that dependencies have been added correctly.

### DataModel
1. Copy the `opendid-datamodel-sdk-aos-1.0.0.jar` file to the libs directory of the app project.
2. Add the following dependencies to the build.gradle of the app project.

```groovy
    implementation files('libs/did-datamodel-sdk-aos-1.0.0.jar')
    implementation 'com.google.code.gson:gson:2.10.1'
```
3. Sync `Gradle` to ensure the dependencies are properly added.


### Wallet SDK

1. Copy the `did-core-sdk-aos-1.0.0.jar`, `did-utility-sdk-aos-1.0.0.jar`, `did-datamodel-sdk-aos-1.0.0.jar`, `did-wallet-sdk-aos-1.0.0.jar`, `did-communication-sdk-aos-1.0.0.jar` file to the libs of the app project.
2. Add the following dependencies to the build.gradle of the app project.

```groovy
    implementation files('libs/did-wallet-sdk-aos-1.0.0.jar')
    implementation files('libs/did-core-sdk-aos-1.0.0.jar')
    implementation files('libs/did-utility-sdk-aos-1.0.0.jar')
    implementation files('libs/did-datamodel-sdk-aos-1.0.0.jar')
    implementation files('libs/did-communication-sdk-aos-1.0.0.jar')
    api "androidx.room:room-runtime:2.6.1"
    annotationProcessor "androidx.room:room-compiler:2.6.1"
    implementation 'androidx.biometric:biometric:1.1.0'
```
3. Sync `Gradle` to ensure the dependencies are properly added.


### Communication SDK

1. Copy the `did-communication-sdk-aos-1.0.0.jar` file to the libs of the app project.
2. Add the following dependencies to the build.gradle of the app project.

```groovy
    implementation files('libs/did-communication-sdk-aos-1.0.0.jar')
```
3. Sync `Gradle` to ensure the dependencies are properly added.


## API Reference

API Reference can be found : 
<br>
- [Core SDK](source/did-client-sdk-aos/did-core-sdk-aos/README.md)  
- [Utility SDK](source/did-client-sdk-aos/did-utility-sdk-aos/README.md)  
- [DataModel SDK](source/did-client-sdk-aos/did-datamodel-sdk-aos/README.md)  
- [Wallet SDK](source/did-client-sdk-aos/did-wallet-sdk-aos/README.md)  
- [Communication SDK](source/did-client-sdk-aos/did-communication-sdk-aos/README.md)  

## Change Log

ChangeLog can be found : 
<br>
- [Core SDK](source/did-client-sdk-aos/did-core-sdk-aos/CHANGELOG.md)  
- [Utility SDK](source/did-client-sdk-aos/did-utility-sdk-aos/CHANGELOG.md)  
- [DataModel SDK](source/did-client-sdk-aos/did-datamodel-sdk-aos/CHANGELOG.md)  
- [Wallet SDK](source/did-client-sdk-aos/did-wallet-sdk-aos/CHANGELOG.md)  
- [Communication SDK](source/did-client-sdk-aos/did-communication-sdk-aos/CHANGELOG.md)  

## OpenDID Demonstration Videos <br>
To watch our demonstration videos of the OpenDID system in action, please visit our [Demo Repository](https://github.com/OmniOneID/did-demo-server). <br>

These videos showcase key features including user registration, VC issuance, and VP submission processes.

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) and [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for details on our code of conduct, and the process for submitting pull requests to us.


## License
[Apache 2.0](LICENSE)
