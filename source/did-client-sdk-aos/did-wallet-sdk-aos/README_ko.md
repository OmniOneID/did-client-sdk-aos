# Android Wallet SDK Guide
본 문서는 OpenDID Wallet SDK 사용을 위한 가이드로, 
Open DID에 필요한 WalletToken, Lock/Unlock, Key, DID Document(DID 문서), Verifiable Credential(이하 VC) 정보를 생성 및 보관, 관리하는 기능을 제공한다.


## S/W 사양
| 구분 | 내용                |
|------|----------------------------|
| OS  | Android 13|
| Language  | Java 17|
| IDE  | Android Studio 4|
| Build System  | Gradle 8.2 |
| Compatibility | Android API level 34 or higher  |

<br>

## 빌드 방법
: 본 SDK 프로젝트의 build.gradle 파일에서 export JAR 태스크를 실행하여 JAR 파일을 생성한다.
1. 프로젝트의 `build.gradle` 파일을 열고, 아래와 같은 `export JAR` 태스크를 추가한다.
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
2. Android Studio에서 `Gradle` 창을 열고, 프로젝트의 `Tasks > other > exportJar` 태스크를 실행한다.
3. 실행이 완료되면 `release/` 폴더에 `did-wallet-sdk-aos-1.0.0.jar` 파일을 생성한다.

<br>

## SDK 적용 방법
1. 앱 프로젝트의 libs에 `did-core-sdk-aos-1.0.0.jar`, `did-utility-sdk-aos-1.0.0.jar`, `did-datamodel-sdk-aos-1.0.0.jar`, `did-wallet-sdk-aos-1.0.0.jar`, `did-communication-sdk-aos-1.0.0.jar` 파일을 복사한다.
2. 앱 프로젝트의 build gradle에 아래 의존성을 추가한다.

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
3. `Gradle`을 동기화하여 의존성이 제대로 추가되었는지 확인한다.

<br>

## API 규격서
| 구분 | API 문서 Link |
|------|----------------------------|
| WalletAPI  | [Wallet SDK API](../../../docs/api/did-wallet-sdk-aos/WalletAPI_ko.md) |
| ErrorCode      | [Error Code](../../../docs/api/did-wallet-sdk-aos/WalletError.md) |
