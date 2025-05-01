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
│   └── README.md
│   └── api
│       ├── private
│       │   ├── DIDManager_ko.md
│       │   ├── DIDManager.md
│       │   ├── KeyManager_ko.md
│       │   ├── KeyManager.md
│       │   ├── SecureEncryptor_ko.md
│       │   ├── SecureEncryptor.md
│       │   ├── VCManager_ko.md
│       │   ├── VCManager.md
│       │   ├── ZKPManager_ko.md
│       │   └── ZKPManager.md
│       └── public
│           ├── Communication_ko.md
│           ├── Communication.md
│           ├── CommunicationError.md
│           ├── DataModel_ko.md
│           ├── DataModel.md
│           ├── Utility_ko.md
│           ├── Utility.md
│           ├── UtilityError.md
│           ├── WalletAPI_ko.md
│           ├── WalletAPI.md
│           ├── WalletCoreError.md
│           ├── WalletError.md
│           ├── ZKP_DataModel_ko.md
│           └── ZKP_DataModel.md
└── source
    └── release
    │   └── did-wallet-sdk-aos-2.0.0.jar
    └── did-wallet-sdk-aos
        ├── build.gradle
        ├── gradle
        ├── gradle.properties
        ├── gradlew
        ├── local.properties
        ├── README_ko.md
        ├── README.md
        ├── settings.gradle
        └── src
```

| Name                    | Description                                     |
| ----------------------- | ----------------------------------------------- |
| source                  | SDK source code project                         |
| docs                    | Documentation                                   |
| ┖ api                   | API guide documentation                         |
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

Libraries can be found in the [releases folder](./source/release).



### Wallet SDK

1. Copy the `did-wallet-sdk-aos-2.0.0.jar` file to the libs of the app project.
2. Add the following dependencies to the build.gradle of the app project.

```groovy
    implementation files('libs/did-wallet-sdk-aos-2.0.0.jar')
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.navigation:navigation-fragment:2.7.7'

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'

    implementation 'com.google.firebase:firebase-messaging:20.0.0'
    implementation 'com.google.android.gms:play-services-vision:20.1.3'

    implementation "androidx.navigation:navigation-fragment-ktx:2.7.7"
    implementation "androidx.navigation:navigation-ui-ktx:2.7.7"

    implementation 'com.google.code.gson:gson:2.10.1'
    implementation 'androidx.biometric:biometric:1.1.0'
    implementation 'org.bitcoinj:bitcoinj-core:0.15.7'

    implementation 'com.madgag.spongycastle:core:1.54.0.0'
    implementation 'com.madgag.spongycastle:prov:1.54.0.0'
    implementation 'com.madgag.spongycastle:pkix:1.54.0.0'
    implementation 'com.madgag.spongycastle:pg:1.54.0.0'

    api "androidx.room:room-runtime:2.6.1"
    annotationProcessor "androidx.room:room-compiler:2.6.1"
```
3. Sync `Gradle` to ensure the dependencies are properly added.




## API Reference

API Reference can be found : 
<br>
- [Wallet SDK](source/did-wallet-sdk-aos/README.md)  

## Change Log

ChangeLog can be found : [here](./CHANGELOG.md)  
<br>



## OpenDID Demonstration Videos <br>
To watch our demonstration videos of the OpenDID system in action, please visit our [Demo Repository](https://github.com/OmniOneID/did-demo-server). <br>

These videos showcase key features including user registration, VC issuance, and VP submission processes.

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) and [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for details on our code of conduct, and the process for submitting pull requests to us.


## License
[Apache 2.0](LICENSE)
