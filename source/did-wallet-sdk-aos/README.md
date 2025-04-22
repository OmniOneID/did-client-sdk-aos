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
    version = "2.0.0"
}

task exportJar(type: Copy){
    from('build/intermediates/aar_main_jar/release/')
    into('../release/')
    include('classes.jar')
    rename('classes.jar', 'did-wallet-sdk-aos-${version}.jar')
}
```
2. Open the `Gradle` window in Android Studio, and run the `Tasks > other > exportJar` task of the project.
3. Once the execution is complete, the `did-wallet-sdk-aos-2.0.0.jar` file will be generated in the `release/` folder.

<br>

## SDK Application Method
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

<br>

## API Specification
| Category | API Document Link |
|------|----------------------------|
| WalletAPI  | [Wallet SDK API](../../docs/api/wallet/WalletAPI.md) |
| ErrorCode      | [Error Code](../../docs/api/wallet/WalletError.md) |
