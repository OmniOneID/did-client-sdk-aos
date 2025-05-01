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

Android CommunicationError
==

- Topic: CommunicationError
- Author: Sangjun Kim
- Date: 2024-09-04
- Version: v1.0.0

| Version          | Date       | Changes                  |
| ---------------- | ---------- | ------------------------ |
| v1.0.0  | 2024-09-04 | Initial version          |

<div style="page-break-after: always;"></div>

# Table of Contents

- [CommunicationError](#Communicationerror)
  - [Error Code](#error-code)


## CommunicationError

### Description
Error struct for Communication operations. It has code and message pair.
Code starts with MSDKCMM.

### Declaration
```java
public enum CommunicationErrorCode {
    private final String CommunicationCode = "MSDKCMM";
    private int code;
    private String msg;
}
```

### Property

| Name    | Type   | Description             | **M/O** | **Note** |
|---------|--------|-------------------------|---------|----------|
| code    | String | Error code              | M       |          |
| message | String | Error description       | M       |          |

<br>

# Error Code

| Error Code      | Error Message            | Description      | Action Required  |
|-----------------|--------------------------|------------------|------------------|
| MSDKCMM00000    | unkown                   |                  | 
| MSDKCMM00001    | Invalid parameter                   |                  |                  |
| MSDKCMM00002    | Incorrect url connection |                  |                  |
| MSDKCMM00003    | server error message     |                  |                  |


<br>
