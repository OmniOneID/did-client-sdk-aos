Android Client SDK
==

Client SDK Repository에 오신 것을 환영합니다. <br> 이 Repository는 안드로이드 모바일 월렛을 개발하기 위한 SDK를 제공합니다.

## 폴더 구조
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
        │   ├── LICENSE-dependencies.md
        │   ├── README.md
        │   ├── README_ko.md
        │   ├── SECURITY.md
        │   ├── build.gradle
        │   └── src
        ├── did-core-sdk-aos
        │   ├── CHANGELOG.md
        │   ├── LICENSE-dependencies.md
        │   ├── README.md
        │   ├── README_ko.md
        │   ├── SECURITY.md
        │   ├── build.gradle
        │   └── src
        ├── did-datamodel-sdk-aos
        │   ├── CHANGELOG.md
        │   ├── LICENSE-dependencies.md
        │   ├── README.md
        │   ├── README_ko.md
        │   ├── SECURITY.md
        │   ├── build.gradle
        │   └── src
        ├── did-utility-sdk-aos
        │   ├── CHANGELOG.md
        │   ├── LICENSE-dependencies.md
        │   ├── README.md
        │   ├── README_ko.md
        │   ├── SECURITY.md
        │   ├── build.gradle
        │   └── src
        ├── did-wallet-sdk-aos
        │   ├── CHANGELOG.md
        │   ├── LICENSE-dependencies.md
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

|  이름 |         역할                    |
| ------- | ------------------------------------ |
| source  |  SDK 소스코드 프로젝트             |
| docs  |   문서            |
| ┖ api  |  API 가이드 문서          |
| ┖ design |  설계 문서            |
| sample  |  샘플 및 데이터            |
| README.md  |  프로젝트의 전체적인 개요 설명            |
| CLA.md             | Contributor License Agreement                |
| CHANGELOG.md| 프로젝트 버전별 변경사항           |
| CODE_OF_CONDUCT.md| 기여자의 행동강령            |
| CONTRIBUTING.md| 기여 절차 및 방법           |
| LICENSE                 | Apache 2.0                                      |
| dependencies-license.md| 프로젝트 의존성 라이브러리에 대한 라이선스            |
| MAINTAINERS.md          | 유지관리 가이드              |
| RELEASE-PROCESS.md      | 릴리즈 절차                                |
| SECURITY.md| 보안취약점 보고 및 보안정책            | 

## 라이브러리

라이브러리는 [release 폴더](source/did-client-sdk-aos/release)에서 찾을 수 있습니다.

### Core SDK

1. 앱 프로젝트의 libs에 `did-core-sdk-aos-1.0.0.jar`, `did-utility-sdk-aos-1.0.0.jar`, `did-datamodel-sdk-aos-1.0.0.jar` 파일을 복사한다.
2. 앱 프로젝트의 build gradle에 아래 의존성을 추가한다.

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
3. `Gradle`을 동기화하여 의존성이 제대로 추가되었는지 확인한다.

### Utility SDK

1. 앱 프로젝트의 libs에 `did-utility-sdk-aos-1.0.0.jar` 파일을 복사한다.
2. 앱 프로젝트의 build gradle에 아래 의존성을 추가한다.

```groovy
    implementation files('libs/did-utility-sdk-aos-1.0.0.jar')
    implementation 'com.google.code.gson:gson:2.10.1'
    implementation 'org.bitcoinj:bitcoinj-core:0.15.7'

    implementation 'com.madgag.spongycastle:core:1.54.0.0'
    implementation 'com.madgag.spongycastle:prov:1.54.0.0'
    implementation 'com.madgag.spongycastle:pkix:1.54.0.0'
    implementation 'com.madgag.spongycastle:pg:1.54.0.0'
```
3. `Gradle`을 동기화하여 의존성이 제대로 추가되었는지 확인한다.


### DataModel

1. 앱 프로젝트의 libs에 `did-datamodel-sdk-aos-1.0.0.jar` 파일을 복사한다.
2. 앱 프로젝트의 build gradle에 아래 의존성을 추가한다.

```groovy
    implementation files('libs/did-datamodel-sdk-aos-1.0.0.jar')
    implementation 'com.google.code.gson:gson:2.10.1'
```
3. `Gradle`을 동기화하여 의존성이 제대로 추가되었는지 확인한다.

### Wallet SDK

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


### Communication SDK

1. 앱 프로젝트의 libs에 `did-communication-sdk-aos-1.0.0.jar` 파일을 복사한다.
2. 앱 프로젝트의 build gradle에 아래 의존성을 추가한다.

```groovy
    implementation files('libs/did-communication-sdk-aos-1.0.0.jar')
```
3. `Gradle`을 동기화하여 의존성이 제대로 추가되었는지 확인한다.

## API 참조

API 참조는 아래에서 확인할 수 있습니다.
<br>
- [Core SDK](source/did-client-sdk-aos/did-core-sdk-aos/README_ko.md)  
- [Utility SDK](source/did-client-sdk-aos/did-utility-sdk-aos/README_ko.md)  
- [DataModel SDK](source/did-client-sdk-aos/did-datamodel-sdk-aos/README_ko.md)  
- [Wallet SDK](source/did-client-sdk-aos/did-wallet-sdk-aos/README_ko.md)  
- [Communication SDK](source/did-client-sdk-aos/did-communication-sdk-aos/README_ko.md)  

## 수정내역

ChangeLog는 아래에서 확인할 수 있습니다.
<br>
- [Core SDK](source/did-client-sdk-aos/did-core-sdk-aos/CHANGELOG.md)  
- [Utility SDK](source/did-client-sdk-aos/did-utility-sdk-aos/CHANGELOG.md)  
- [DataModel SDK](source/did-client-sdk-aos/did-datamodel-sdk-aos/CHANGELOG.md)  
- [Wallet SDK](source/did-client-sdk-aos/did-wallet-sdk-aos/CHANGELOG.md)  
- [Communication SDK](source/did-client-sdk-aos/did-communication-sdk-aos/CHANGELOG.md)  

## 기여

Contributing 및 pull request 제출 절차에 대한 자세한 내용은 [CONTRIBUTING.md](CONTRIBUTING.md)와 [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) 를 참조하세요.

## 라이선스
[Apache 2.0](LICENSE)

