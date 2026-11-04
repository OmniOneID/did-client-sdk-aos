# Android Wallet SDK Guide
This document is a guide for using the OpenDID Wallet SDK, and provides functions for creating, storing, and managing the WalletToken, Lock/Unlock, Key, DID Document (DID Document), and Verifiable Credential (hereinafter referred to as VC) information required for Open DID. It also provides DID-based OID4VC (SD-JWT VC, mdoc) credential issuance, presentation and revocation, and mdoc proximity presentation.

## S/W Specifications
| Category | Details                |
|------|----------------------------|
| OS  | Android 14|
| Language  | Java 21|
| IDE  | Android Studio 4|
| Build System  | Gradle 9.4.1 (Android Gradle Plugin 9.1.0) |
| Compatibility | compileSdk 36.1, minSdk 26|

<br>


## Build Method
: Execute the export JAR task in the build.gradle file of this SDK project to generate a JAR file.
1. The project's `build.gradle` file defines the following `export JAR` task.
```groovy
ext {
    customVersionMajorMinorBuild = "3.0.0"
}

task exportJar(type: Copy){
    from('build/intermediates/aar_main_jar/release/syncReleaseLibJars/')
    into('../release/')
    include('classes.jar')
    rename('classes.jar', "${project.name}-${customVersionMajorMinorBuild}.jar")
}
```
2. Open the `Gradle` window in Android Studio, and run the `Tasks > other > exportJar` task of the project.
3. Once the execution is complete, the `did-wallet-sdk-aos-3.0.0.jar` file will be generated in the `release/` folder.

<br>

## SDK Application Method
1. Copy the `did-wallet-sdk-aos-3.0.0.jar` file to the libs of the app project.
2. Add the following dependencies to the build.gradle of the app project.

```groovy
    implementation files('libs/did-wallet-sdk-aos-3.0.0.jar')
    implementation 'com.google.android.material:material:1.13.0'

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'

    implementation 'com.google.code.gson:gson:2.13.2'
    implementation 'androidx.biometric:biometric:1.1.0'
    implementation 'org.bitcoinj:bitcoinj-core:0.15.7'

    implementation 'com.madgag.spongycastle:core:1.54.0.0'
    implementation 'com.madgag.spongycastle:prov:1.54.0.0'

    implementation 'com.nimbusds:nimbus-jose-jwt:9.40'
    implementation 'com.upokecenter:cbor:4.5.6'

    api "androidx.room:room-runtime:2.8.4"
    annotationProcessor "androidx.room:room-compiler:2.8.4"
```
3. Sync `Gradle` to ensure the dependencies are properly added.

<br>

## API Specification
| Category | API Document Link |
|------|----------------------------|
| WalletAPI  | [Wallet SDK API](../../docs/api/public/WalletAPI.md) |
| ErrorCode      | [Error Code](../../docs/api/public/WalletError.md) |
