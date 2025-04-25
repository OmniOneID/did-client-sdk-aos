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
        └── did-wallet-sdk-aos-2.0.0.jar
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

|  이름 |         역할                    |
| ------- | ------------------------------------ |
| source  |  SDK 소스코드 프로젝트             |
| docs  |   문서            |
| ┖ api  |  API 가이드 문서          |
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

라이브러리는 [release 폴더](source/did-wallet-sdk-aos/release)에서 찾을 수 있습니다.

### Wallet SDK

1. 앱 프로젝트의 libs에 `did-wallet-sdk-aos-2.0.0.jar` 파일을 복사한다.
2. 앱 프로젝트의 build gradle에 아래 의존성을 추가한다.

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
3. `Gradle`을 동기화하여 의존성이 제대로 추가되었는지 확인한다.


## API 참조

API 참조는 아래에서 확인할 수 있습니다.
<br>
- [Wallet SDK](source/did-wallet-sdk-aos/README_ko.md)  

## 수정내역

ChangeLog는 아래에서 확인할 수 있습니다.
<br>
- [Here](./CHANGELOG.md)  


## 데모 영상 <br>
OpenDID 시스템의 실제 동작을 보여주는 데모 영상은 [Demo Repository](https://github.com/OmniOneID/did-demo-server)에서 확인하실 수 있습니다. <br>
사용자 등록, VC 발급, VP 제출 등 주요 기능들을 영상으로 확인하실 수 있습니다.

## 기여

Contributing 및 pull request 제출 절차에 대한 자세한 내용은 [CONTRIBUTING.md](CONTRIBUTING.md)와 [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) 를 참조하세요.

## 라이선스
[Apache 2.0](LICENSE)

