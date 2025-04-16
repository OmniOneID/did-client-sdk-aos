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

Android Communication SDK API
==

- 주제: Communication SDK
- 작성: Sangjun Kim
- 일자: 2024-09-04
- 버전: v1.0.0

| 버전   | 일자       | 변경 내용                 |
| ------ | ---------- | -------------------------|
| v1.0.0 | 2024-09-04 | 초기 작성                 |


<div style="page-break-after: always;"></div>

# 목차
- [APIs](#api-목록)
  - [1. makeHttpRequest](#1-makehttprequest)

<br>

## API 목록
### 1. makeHttpRequest

#### Description
`Http 요청 및 응답 기능 제공`

#### Declaration

```java
public String makeHttpRequest(String urlString, String method, String payload)
```


#### Parameters

| Parameter | Type   | Description                | **M/O** | **비고** |
|-----------|--------|----------------------------|---------|---------|
| urlString    | String    | 서버 URL |M| |
| method    | String    | HTTP 메서드 |M| |
| payload    | String    | 요청데이터 |M| |

#### Returns

| Type | Description                |**M/O** | **비고** |
|------|----------------------------|---------|---------|
| String  | 응답데이터 |M| |


#### Usage
```java
HttpUrlConnectionTask httpFunc = new HttpUrlConnectionTask();
String response = httpFunc.makeHttpRequest("http://opendid/api/v1", "POST", requestData);
```

<br>
