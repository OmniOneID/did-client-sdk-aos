# Android Wallet SDK Guide
This document is a guide for using the OpenDID Wallet SDK, and provides functions for creating, storing, and managing the WalletToken, Lock/Unlock, Key, DID Document (DID Document), and Verifiable Credential (hereinafter referred to as VC) information required for Open DID.

## S/W Specifications
| Category | Details                |
|------|----------------------------|
| OS  | Android 13|
| Language  | Java 17|
| IDE  | Android Studio 4|
| Build System  | Gradle 8.2 |
| Compatibility | Android API level 34 or higher  |

<br>


## Build Method
: Execute the export JAR task in the build.gradle file of this SDK project to generate a JAR file.
1. Open the project's `build.gradle` file and add the following `export JAR` task.
```groovy
ext {
    version = "1.0.0"
}

task exportJar(type: Copy){
    from('build/intermediates/aar_main_jar/release/')
    into('../release/')
    include('classes.jar')
    rename('classes.jar', 'did-wallet-sdk-aos-${version}.jar')
}
```
2. Open the `Gradle` window in Android Studio, and run the `Tasks > other > exportJar` task of the project.
3. Once the execution is complete, the `did-wallet-sdk-aos-1.0.0.jar` file will be generated in the `release/` folder.

<br>

## SDK Application Method
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

<br>

## API Specification
| Category | API Document Link |
|------|----------------------------|
| WalletAPI  | [Wallet SDK API](../../../docs/api/did-wallet-sdk-aos/WalletAPI.md) |
| ErrorCode      | [Error Code](../../../docs/api/did-wallet-sdk-aos/WalletError.md) |
