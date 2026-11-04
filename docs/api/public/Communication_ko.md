---
puppeteer:
    pdf:
        format: A4
        displayHeaderFooter: true
        landscape: false
        scale: 0.8
        margin:
            top: 1.2cm
            right: 1cm
            bottom: 1cm
            left: 1cm
    image:
        quality: 100
        fullPage: false
---

Android Communication API
==

- 주제: Communication
- 작성: 김은정
- 일자: 2026-10-28
- 버전: v3.0.0

| 버전   | 일자       | 변경 내용                 |
| ------ | ---------- | -------------------------|
| v3.0.0 | 2026-10-30 | accessToken, contentType 파라미터 추가 |
| v1.0.0 | 2024-09-04 | 초기 작성                 |


<div style="page-break-after: always;"></div>

# 목차
- [APIs](#api-목록)
  - [1. makeHttpRequest](#1-makehttprequest)

<br>

# API 목록
### 1. makeHttpRequest

#### Description
`Http 요청 및 응답 기능 제공`

#### Declaration

```java
public String makeHttpRequest(String urlString, String method, String payload, String accessToken) throws CommunicationException
public String makeHttpRequest(String urlString, String method, String payload, String accessToken, String contentType) throws CommunicationException
```


#### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| urlString    | String    | 서버 URL |M| |
| method    | String    | HTTP 메서드 |M| |
| payload    | String    | 요청데이터 |M| |
| accessToken    | String    | `Authorization: Bearer` 헤더로 전송. null 이면 보내지 않음 |O| |
| contentType    | String    | `Content-Type` 헤더 값 |O| 4-파라미터 오버로드는 `application/json` |

#### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| String  | 응답데이터 |M| |


#### Usage
```java
HttpUrlConnectionTask httpFunc = new HttpUrlConnectionTask();
String response = httpFunc.makeHttpRequest("http://opendid/api/v1", "POST", requestData, null);
```

<br>
