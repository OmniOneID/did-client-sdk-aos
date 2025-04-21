/*
 * Copyright 2024-2025 OmniOne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.omnione.did.sdk.core.api;

public class WalletTestData {
    public static String WALLET_TOKEN_DATA_PERSONALIZE = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":1,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";

    public static String WALLET_TOKEN_DATA_DEPERSONALIZE = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":2,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";

    public static String WALLET_TOKEN_DATA_PERSONALIZE_AND_CONFIGLOCK = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":3,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";

    public static String WALLET_TOKEN_DATA_CONFIGLOCK = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":4,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";

    public static String WALLET_TOKEN_DATA_CREATE_DID = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":5,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";

    public static String WALLET_TOKEN_DATA_UPDATE_DID = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":6,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";

    public static String WALLET_TOKEN_DATA_RESTORE_DID = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":7,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";

    public static String WALLET_TOKEN_DATA_ISSUE_VC = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":8,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";

    public static String WALLET_TOKEN_DATA_REMOVE_VC = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":9,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";

    public static String WALLET_TOKEN_DATA_PRESENT_VP = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":10,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";

    public static String WALLET_TOKEN_DATA_LIST_VC = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":11,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";

    public static String WALLET_TOKEN_DATA_DETAIL_VC = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":12,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";
    public static String WALLET_TOKEN_DATA_CREATE_DID_AND_ISSUE_VC = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":13,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";
    public static String WALLET_TOKEN_DATA_LIST_VC_AND_PRESENT_VP = "{\"nonce\":\"mHaq6qqiUKLt8N6raGc+/5g\",\"proof\":{\"created\":\"2024-08-23T11:40:03.566877Z\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mH6qvGRLx8qpMPhQwJMeEQsz0eGYhZQI1y9yu1xl4b+qIQM2tfFueWHpuffAvdioqSrR0K4Qb0AgbzcdtQO4ds7A\",\"type\":\"Secp256r1Signature2018\",\"verificationMethod\":\"did:omn:cas?versionId=1#assert\"},\"provider\":{\"certVcRef\":\"http://192.168.3.130:8094/cas/api/v1/certificate-vc\",\"did\":\"did:omn:cas\"},\"seed\":{\"nonce\":\"F6438ED0B74B348E3635C89DA164E8FFE\",\"pkgName\":\"org.omnione.did.ca\",\"purpose\":14,\"userId\":\"7156af3d\",\"validUntil\":\"2050-08-23T12:10:02Z\"},\"sha256_pii\":\"zCWYkiLKjXUHu5xCRcJoJ9nfiyKzhiYW3YiWwdDwfh2vx\"}";

    public static String[] hWalletToken = {
            "b1fa33d7e1d6de3ef5d26ddac500f7cd772a84ac1265a68a7e7a26d088898178",
            "b58d39273d5dcd217d81f9e8be32fbedc06c3a47c07f246757738f09f3c4642e",
            "4d3099ed8c756720be266501befd96d05a59da43bbb6c7a69563437278917a2e",
            "14e54fe537897257c7c20e8fc2a95f8d3a3eac72989ccb728b248b802282dc8f",
            "faac3d6a5e47227d69cc40117d6cc3b46e02976e83b3a2e0d5a1d34d015772a0",
            "13c21914000973473af82d89e0960d6ed5e6f6492d9db921052a4bb58d748db1",
            "24ce59f41d35ef91117b6f2462ab7d76658c7b2ed1304b930714e1a57908bae9",
            "6480686ee2b148949220ca4f4d923e082a877aba0209932167519b9fba58b84b",
            "4d2fb9764498322c2948a88e5b509ffc6c44e86311e56f0bba9c8c0af6ac1583",
            "0b5cf2697ec41aee9007e3bd0f4ba14fb47b89caeb977f79d667ef9abd385082",
            "6f44e4617d75b3d3bfd7135078c193a5995c56e24a566c40e2ae757786f30ed0",
            "5799678b95649aaea7c2f08a44515b949a1b5c4725d8ae400d16542810f4790e",
            "4a72b96d613c161535ad27dbe816fbb7c451d689f1b9d05b8637057796f6e49d",
            "15e67a17d1759faf62b1e33e8a40335a5804df36961deed5c94722d53124c036"
    };
    public static String validUntil = "2050-08-23T12:10:02Z";
    // create wallet (device key)
    public static String TEST_ATTESTED_DID_DOC = "{\"walletId\":\"WID202408HrhNC4ysSOu\",\"nonce\":\"f92c90b0eb247b7d14ed33413a3bc94d\",\"ownerDidDoc\":\"ueyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvbnMvZGlkL3YxIl0sImFzc2VydGlvbk1ldGhvZCI6WyJhc3NlcnQiXSwiYXV0aGVudGljYXRpb24iOlsiYXV0aCJdLCJjb250cm9sbGVyIjoiZGlkOm9tbjp0YXMiLCJjcmVhdGVkIjoiMjAyNC0wOC0yNlQwOToyMjoxMVoiLCJkZWFjdGl2YXRlZCI6ZmFsc2UsImlkIjoiZGlkOm9tbjo0Q2Z4ZHFUcUM2WmFmanRBVmlQcGVTVW9SYVhMIiwia2V5QWdyZWVtZW50IjpbImtleWFncmVlIl0sInByb29mcyI6W3siY3JlYXRlZCI6IjIwMjQtMDgtMjZUMDk6MjI6MTFaIiwicHJvb2ZQdXJwb3NlIjoiYXNzZXJ0aW9uTWV0aG9kIiwicHJvb2ZWYWx1ZSI6Im1JRTJoTGJFVDcvYUM5WVhKbFlnT1ZyMDl2TmpUZU1VL0xBdVRLbnhNK0paVkV3RlBab2JLSXdxejU5Y0k2U0p6YWNVRkl0WE9vcDBCZy9Nc3Q4YThVbkU9IiwidHlwZSI6IlNlY3AyNTZyMVNpZ25hdHVyZTIwMTgiLCJ2ZXJpZmljYXRpb25NZXRob2QiOiJkaWQ6b21uOjRDZnhkcVRxQzZaYWZqdEFWaVBwZVNVb1JhWEw_dmVyc2lvbklkPTEjYXNzZXJ0In0seyJjcmVhdGVkIjoiMjAyNC0wOC0yNlQwOToyMjoxMVoiLCJwcm9vZlB1cnBvc2UiOiJhdXRoZW50aWNhdGlvbiIsInByb29mVmFsdWUiOiJtSDRRaTcvbHJucTF6aFFKV0Vwd1RHY1drZmFTY3Z5ekhVaFdaTlRiK0loazNBMU5raHNBR0NhNnFFSjJTQnZRZDJrakJwSXpFd3pmbkQ0ZSsxV2R4U0ZFPSIsInR5cGUiOiJTZWNwMjU2cjFTaWduYXR1cmUyMDE4IiwidmVyaWZpY2F0aW9uTWV0aG9kIjoiZGlkOm9tbjo0Q2Z4ZHFUcUM2WmFmanRBVmlQcGVTVW9SYVhMP3ZlcnNpb25JZD0xI2F1dGgifV0sInVwZGF0ZWQiOiIyMDI0LTA4LTI2VDA5OjIyOjExWiIsInZlcmlmaWNhdGlvbk1ldGhvZCI6W3siYXV0aFR5cGUiOjEsImNvbnRyb2xsZXIiOiJkaWQ6b21uOnRhcyIsImlkIjoia2V5YWdyZWUiLCJwdWJsaWNLZXlNdWx0aWJhc2UiOiJtQTJEV2RiU1Z0Z0tPUjFoTE8vZ1MyZy8wejdRYXJDaXRTRlh2NXFvN2tUeWMiLCJ0eXBlIjoiU2VjcDI1NnIxVmVyaWZpY2F0aW9uS2V5MjAxOCJ9LHsiYXV0aFR5cGUiOjEsImNvbnRyb2xsZXIiOiJkaWQ6b21uOnRhcyIsImlkIjoiYXV0aCIsInB1YmxpY0tleU11bHRpYmFzZSI6Im1BcGtwOERjUWU1TEpwMmtaQ2hadHdOT2lTQmM3Z0N3L01QWmhLTFA2aHVKWiIsInR5cGUiOiJTZWNwMjU2cjFWZXJpZmljYXRpb25LZXkyMDE4In0seyJhdXRoVHlwZSI6MSwiY29udHJvbGxlciI6ImRpZDpvbW46dGFzIiwiaWQiOiJhc3NlcnQiLCJwdWJsaWNLZXlNdWx0aWJhc2UiOiJtQTAzbmg2OUY1aDBqRHY5QnJuU0pteVpkNWV1SG5NbkFwenpSQXljNUpFMnIiLCJ0eXBlIjoiU2VjcDI1NnIxVmVyaWZpY2F0aW9uS2V5MjAxOCJ9XSwidmVyc2lvbklkIjoiMSJ9\",\"provider\":{\"did\":\"did:omn:wallet\",\"certVcRef\":\"http://192.168.3.130:8095/wallet/api/v1/certificate-vc\"},\"did\":\"did:omn:wallet\",\"proof\":{\"type\":\"Secp256r1Signature2018\",\"created\":\"2024-08-26T09:22:13.368740Z\",\"verificationMethod\":\"did:omn:wallet?versionId\\u003d1#assert\",\"proofPurpose\":\"assertionMethod\",\"proofValue\":\"mIGfv8qiuKlX9nahD/QkOCCUmg+nUnvFDXhh1KpVvCGw+YNZkfntjOq+Ee4X5uH+l7IuyWLYt/sBhHmxbVvNWRCo\"}}";
    public static String TEST_PASSCODE = "123456";
    public static String TEST_TX_ID = "c91a76c4-8e01-4847-8f59-74ab3590af63";
    public static String TEST_SERVER_TOKEN = "zDv3FyFqRm8LyKySLevEQVLAzR76VzMoVhGNS7jVnKazW";
    public static String TEST_REF_ID = "EZ8VPzvJa0IGTUakE";
    public static String TEST_AUTH_NONCE = "mRt0ghfNAAjXkIm4ToaR2Zg";
    public static String TEST_ISSUE_PROFILE = "{\"profile\":{\n" +
            "         \"issuer\":{\n" +
            "            \"name\":\"issuer\",\n" +
            "            \"did\":\"did:omn:issuer\",\n" +
            "            \"certVcRef\":\"http://192.168.3.130:8091/issuer/api/v1/certificate-vc\"\n" +
            "         },\n" +
            "         \"credentialSchema\":{\n" +
            "            \"id\":\"http://192.168.3.130:8091/issuer/api/v1/vc/vcschema?name\\u003dnational_id\",\n" +
            "            \"type\":\"OsdSchemaCredential\"\n" +
            "         },\n" +
            "         \"process\":{\n" +
            "            \"issuerNonce\":\"mBcgy9h50XljNuXYHdILz/Q\",\n" +
            "            \"endpoints\":[\n" +
            "               \"http://192.168.3.130:8091/issuer\"\n" +
            "            ],\n" +
            "            \"reqE2e\":{\n" +
            "               \"nonce\":\"mBcgy9h50XljNuXYHdILz/Q\",\n" +
            "               \"curve\":\"Secp256r1\",\n" +
            "               \"publicKey\":\"mAlpIX2LtKHvlcuhWxb327ltRFQwahoFvb3neuMyG/458\",\n" +
            "               \"cipher\":\"AES-256-CBC\",\n" +
            "               \"padding\":\"PKCS5\"\n" +
            "            }\n" +
            "         }\n" +
            "      },\n" +
            "      \"proof\":{\n" +
            "         \"type\":\"Secp256r1Signature2018\",\n" +
            "         \"created\":\"2024-09-02T11:58:20.853374Z\",\n" +
            "         \"verificationMethod\":\"did:omn:issuer#assert\",\n" +
            "         \"proofPurpose\":\"assertionMethod\",\n" +
            "         \"proofValue\":\"mIImWmZNnl+mPxEBRUqh8GrNSIqRl0X5Aws//k3gEmzQseiRhB31tdtUQtHqcynPsFOlA4iHIAJ9pBja+7VGQ4Hw\"\n" +
            "      },\n" +
            "      \"id\":\"ee998bbc-3d05-4bec-a227-8e07cbb019ab\",\n" +
            "      \"type\":\"IssueProfile\",\n" +
            "      \"title\":\"National ID\",\n" +
            "      \"description\":\"National ID\",\n" +
            "      \"encoding\":\"UTF-8\",\n" +
            "      \"language\":\"ko\"\n" +
            "   }";
    public static String TEST_ENC_VC = "mRK5CJoYbFzmvGm8VVvyiaGLUHlZLftApRPazLGMFP1rR+NOU64tbux+00dfZpzJjWO8Le0FcMQnOO5az4e99TrwhGCPWzOq1BPoGP008PeAJHqEtE0BVAyxDAOYYJTE4lDZc6IT1xq5QrF/dc4Uvn1CH2ezZcu4GLTzJ7fa+sJXp1XvnXPgwBYzik5dlwMeC8UpFIK6mvjGpIHKftfhFt3GdD/JucxmCML84EinWo0nQmNSPS/3vQNem7aGdxuD4wVSVWPcri3c11yZpIUpTaYjmVvXV+uNuc+iFPFM9PdpdrUtkgBeD/PYAbU/bD0nf/pBQ3SEuFvn7QKQd9VO0IlQQc/eytfCUF2rVsAWRMEJxSQ2owpzlqzbMa0bXWEMQtq0MALILEH1Mis+zM/ifKcb2v5JdPRs0U8RyUFLP6vQIqwblUVr2JA44CvZGi6c+bcc1vdOZF6Hs1He9Tww34dMppvrGmYgjwDDDVXpPD82vcwUca1BKPdTEbY9mjpXYQDTCPwlamO77nZ1995PUHuH32f745wSHEE/qN24ldp0TNRUgVzdy2bs4L7/EKWagWsS/e7E3bQq9G3D232xIj5zY3iDFeo2kEMxAbEvz8ApCBp/B44vhWr3jL6XpigfvXQ7oTYNvQ/Prnk2pjKOw2J+QSPLSGnlF+3xDCxs4e0L5xVo3KY8IPn/exEpinsHGabpSjJZ2GazyG7o2QTQaHimcF/lx1lXkaEeZhJVqRBTcPmKA0PqKdfkoczFYlmNH2S7e9IPfYqqC/tPT5vNdvfalmDYmcyAp9IyarvhSUUzSczOvxzQ1S4xYv3GmeGwo9yhkD7VuY5hodWxu2ikFCLCDK/XbKvGtc124Xemrvg/4C7MvZFsSHQFzC4Dt1SAHvgjpYM31d6MajAGSzQBw1fLHjZJaUOtvjt1FWlEXqfBqOhlTmkBKfkEmgsrrnoMqUIoZXCtZGTxq9dHxuajUqQYREkqwSV67LcZeKxCjvKg+KaJrVgAc2lJTGOCAUkrS70v8HzNvdYFOEgx0q9u5j14dH8UUEcUBRoW2I7jOKBljqJxJzZMG9dT5mynPQ+sWgqRVcIWeKpVaGUuqUTJIx53DppM7fLTVXumT8XXU0LvwEfA4U3YUCv2eYf0VPWcJIESvGoiIYfDOrIvFQoHE0Ecv3Q/9E905fczUwZ0yQ7XOUo4KvibPUlKcONU8Jf39yw2vL7fhSDehlQdU88Vd4WKQxX/r5z/Dd5gHPf0TZgDwhj6bn/WUg2q94khKMcAv3kynHFsUuXurup2zSVpWEqBbRLAGsGmKnA+cAQzUZdP3dTipwYK4xCXcX7LlnpV0Vl9x8uACyYRcDDSZHhKN5T8JMmq5dDNowYa9k6djwYAetAoskrMUbfzBIE5YnSEXtexbMUI5kQ3ncXRqtUhfREpFlW1SXosuMZvJKME5SJrYoWI5Vnrb4j7L9pRYgq7v1B3d/qvNu3PXJfOVAh7CX3UJEfWYqafvP9HCgk92NpkMt8vNwdbPB7hZbsMw0J8ccpEr6hp/l4lx9ztO/4Gy/p1XobbE4RC7c3X0zkqUEDpDnWHwD3fHpDB2zm6l/pYe5n4Lu3qChxtKk+fXfI5PAw8GWywFdidzrvUBNV3EaCBKL314MrHo2hnE9GWPIAnQlGk1irLn8WRmssdXZS9EJrd3ZIZ0jdpaRvZMCKsxXxSxqZXaHVjaFuNRDGNm7VxkaA0EKo1xyRZbrby6J52X8AL0OFCXE2z1wo0A2K7JKzHLLIrhK/8+x8olepfkR7beDRqqQUzcWsRTvaP8dtGplOz85cS4lbUm3e2iWU6JL9V2kYhwpc/CFxBc6x2yrgXUHsDLbnZXCjzrbBHpKE71hFShDZefag/+b5Ulg622b+Ws5yKFNZtNMNJlaldFoxN8G2Kr3dBn3DULQGqKuxT4WtlHXH6HwH1rxxlVa2/e/9z0PzJsOvcaHSHRkuZOxHJEK+yaMrYqGRAX+HBJ7g2D8aiS88xlF7yYtItTjbcWkZd6PY5DXb021oeqnTsK1+NrKc5ezzt364fqxQsjUYQqtoCRxtTUw7VPb4wg4T5IPbeZM5WNMuHcme8NeOfhURVNb7C/oqfcWZ5l7Tdl8uV8Mtqkp0DeDOdOmD1mN62P5bBFdN8aAcH7gtuq8QmdI71Mqg/zw0y6z/wVSlCV/vRdvgJ6OIgHmMBXKgYjlB19Zm27sdodFKLCYGn066L2wNO3sdyLUYieSNTl5PzXk1WbmjZSv7wNWP8PrrwmRp72CxluPg23J57fKYuQPcLuST7cEanmgCdged200iqgST9A9wc5RjTArZzjudK7hlk4AaLQ6R42V0QQZVbMXLMMsqh8aMkowCsutk/hulTmxGFmLUJzqFkx5j+MzSD86q5ZDJ7G/+b+zOUdLCyDmjpowmU8SmmgvxhWyrRIkvgCEKPdKP38wgB6kvALxOqMyy6l2e4/I5ZVazeWs8wT2iE3nGsvicil10BvNgyTQ92RiF6LIdrJmDT6LSIte3SNgLfwa9jaMPB7jX5W/dk4he9CJfuGHL6HWWfGwMg4iy4utukGfGFfm3Um7IyA5PppLGjJlhhF6w3aT96nVBxYGnOpefes+yGR9CxYi4vYi483w73e3d/lAMpCmK0bq0hN8qfySFktb+Wj8Akj9V8UdSDcwGVExRGaf+uUEtEAKYk76lNCWl1j1ssFA7LVK/2YIYlVDG6aNkJRAwDIPveKIT9yc4bA2GJX/DwlZlj0YxGzVE/I9s3Mx4KtoJNQ4Z+9+uzJFvzjpL4/1s5lMddcUVPjgEvr10/IlJyHyF5AtYHX59D/vEC+27PBUDnPWuwRjbz6dBBZcaoybB/L2LuHBVaE3uwF4GzYp7kLpF0lhGp9TaGHPz8qq4wr0DYjkOUN/boPLEQFT7qzIHirFnDKS6ctIZIeM85bfGm146EjK3+2XRMnCD3nb7qQBk6RaRDGflioZ2ldYpZDRfH66ZZ3VJ5iTdNfnVvylcKeX8bBmHQ7X3+JqhZESr94qmDwGztRGYBde0U";
    public static String TEST_VERIFY_PROFILE = "{\n" +
            "   \"description\":\"11번가 로그인을 위해 제출이 필요한 VP에 대한 프로파일 입니다.\",\n" +
            "   \"encoding\":\"UTF-8\",\n" +
            "   \"id\":\"affdb518-34f7-4b85-aaba-d4f9262bd3a7\",\n" +
            "   \"language\":\"ko\",\n" +
            "   \"profile\":{\n" +
            "      \"filter\":{\n" +
            "         \"credentialSchemas\":[\n" +
            "            {\n" +
            "               \"allowedIssuers\":[\n" +
            "                  \"did:omn:issuer\"\n" +
            "               ],\n" +
            "               \"displayClaims\":[\n" +
            "                  \"testId.aa\"\n" +
            "               ],\n" +
            "               \"id\":\"http://192.168.3.130:8091/issuer/api/v1/vc/vcschema?name=national_id\",\n" +
            "               \"requiredClaims\":[\n" +
            "                  \"org.opendid.v1.national_id.family_name\",\n" +
            "                  \"org.opendid.v1.national_id.given_name\",\n" +
            "                  \"org.opendid.v1.national_id.birth_date\"\n" +
            "               ],\n" +
            "               \"type\":\"OsdSchemaCredential\",\n" +
            "               \"value\":\"VerifiableProfile\"\n" +
            "            }\n" +
            "         ]\n" +
            "      },\n" +
            "      \"process\":{\n" +
            "         \"authType\":2,\n" +
            "         \"endpoints\":[\n" +
            "            \"http://192.168.3.130:8092/verifier\"\n" +
            "         ],\n" +
            "         \"reqE2e\":{\n" +
            "            \"cipher\":\"AES-256-CBC\",\n" +
            "            \"curve\":\"Secp256r1\",\n" +
            "            \"nonce\":\"mMMg3gVgwEsbpxORRNp1SRA\",\n" +
            "            \"padding\":\"PKCS5\",\n" +
            "            \"publicKey\":\"zw2oVJVmfEqKxjXjE9wc3LBX33WHmusGA91HADm5PRr7D\"\n" +
            "         },\n" +
            "         \"verifierNonce\":\"mMMg3gVgwEsbpxORRNp1SRA\"\n" +
            "      },\n" +
            "      \"verifier\":{\n" +
            "         \"certVcRef\":\"http://192.168.3.130:8092/verifier/api/v1/certificate-vc\",\n" +
            "         \"description\":\"verifier\",\n" +
            "         \"did\":\"did:omn:verifier\",\n" +
            "         \"name\":\"verifier\",\n" +
            "         \"ref\":\"http://192.168.3.130:8092/verifier/api/v1/certificate-vc\"\n" +
            "      }\n" +
            "   },\n" +
            "   \"proof\":{\n" +
            "      \"created\":\"2024-08-29T18:20:39.666665Z\",\n" +
            "      \"proofPurpose\":\"assertionMethod\",\n" +
            "      \"proofValue\":\"z3oJhN6sQ7d71doPZAkgumZVyZxSXJw4jL9g8hkPkxaVyU4vKCDXpMf4vS6dh7jUFkKtrb2hSjEyU1mNv7kZXUWUmP\",\n" +
            "      \"type\":\"Secp256r1Signature2018\",\n" +
            "      \"verificationMethod\":\"did:omn:verifier?versionId=1#assert\"\n" +
            "   },\n" +
            "   \"title\":\"11번가 로그인 VP 프로파일\",\n" +
            "   \"type\":\"VerifyProfile\"\n" +
            "}";
}
