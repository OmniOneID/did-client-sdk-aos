/* 
 * Copyright 2024 Raonsecure
 */

package org.omnione.did.sdk.core;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.omnione.did.sdk.datamodel.common.enums.AlgorithmType;
import org.omnione.did.sdk.datamodel.common.enums.VerifyAuthType;
import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.DataModels.MultibaseType;
import org.omnione.did.sdk.utility.DigestUtils;
import org.omnione.did.sdk.utility.MultibaseUtils;
import org.omnione.did.sdk.core.common.KeystoreManager;
import org.omnione.did.sdk.core.keymanager.KeyManager;
import org.omnione.did.sdk.core.keymanager.datamodel.DetailKeyInfo;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyGenWalletMethodType;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyInfo;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyStoreAccessMethod;
import org.omnione.did.sdk.core.keymanager.datamodel.SecureKeyGenRequest;
import org.omnione.did.sdk.core.keymanager.datamodel.StorageOption;
import org.omnione.did.sdk.core.keymanager.datamodel.WalletKeyGenRequest;
import org.omnione.did.sdk.core.keymanager.supportalgorithm.Secp256R1Manager;
import org.omnione.did.sdk.core.storagemanager.StorageManager;
import org.omnione.did.sdk.core.storagemanager.datamodel.FileExtension;
import org.omnione.did.sdk.core.storagemanager.datamodel.UsableInnerWalletItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class KeyManagerTest {
    @Test
    public void keyManager() throws Exception {
//        assertArrayEquals(a,b) : 배열 a와b가 일치함을 확인
//        assertEquals(a,b) : 객체 a와b의 값이 같은지 확인
//        assertSame(a,b) : 객체 a와b가 같은 객체임을 확인
//        assertTrue(a) : a가 참인지 확인
//        assertNotNull(a) : a객체가 null이 아님을 확인

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        //assertEquals("com.opendid.walletcore.test", appContext.getPackageName());

        for(String alias : KeystoreManager.getKeystoreAliasList()){
            Log.d("KeyManagerTest","Bio key 생성 전 alias : " + alias);
        }
        // KeyManager 생성자
        KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("walletTest", appContext);
        if(keyManager.isAnyKeySaved())
            keyManager.deleteAllKeys();
        // NONE KEY 생성
        WalletKeyGenRequest keyGenInfo = new WalletKeyGenRequest();
        keyGenInfo.setId("FREE");
        keyGenInfo.setAlgorithmType(AlgorithmType.ALGORITHM_TYPE.SECP256R1);
        keyGenInfo.setStorage(StorageOption.STORAGE_OPTION.WALLET);
        KeyGenWalletMethodType keyGenWalletMethodType = new KeyGenWalletMethodType();
        keyGenInfo.setWalletMethodType(keyGenWalletMethodType);
        Log.d("KeyManagerTest", "KeyGenInfo (FREE) : " + keyGenInfo.toJson());
        keyManager.generateKey(keyGenInfo);
        // pin Key 생성
        keyGenInfo = new WalletKeyGenRequest();
        keyGenInfo.setId("PIN");
        keyGenInfo.setAlgorithmType(AlgorithmType.ALGORITHM_TYPE.SECP256R1);
        keyGenInfo.setStorage(StorageOption.STORAGE_OPTION.WALLET);
        keyGenWalletMethodType = new KeyGenWalletMethodType(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, "111111".getBytes()));
        keyGenInfo.setWalletMethodType(keyGenWalletMethodType);
        Log.d("KeyManagerTest", "KeyGenInfo (PIN) : " + keyGenInfo.toJson());
        keyManager.generateKey(keyGenInfo);

        Secp256R1Manager secp256R1Manager = new Secp256R1Manager();
        byte[] privateKey = MultibaseUtils.decode("f9e075a5e7a8ad6148a27ee2159a6a140c163084da7bf5e956fac2f5f36d81aa1");
        byte[] publicKey = MultibaseUtils.decode("f023888e60a7bf9672b09fad449436939a1b9dff53f18bd9a2383a7d18e3e15c861");

        byte[] privateKey2 = MultibaseUtils.decode("fbdf8c9e39d301233b5a262dd156f6602bc29f0cfcb346f7c5cf525c4f247207b");
        byte[] publicKey2 = MultibaseUtils.decode("f035406dba5e8a29dc2d05b42c08f925b95d972786d95f91e86e7d2c0f51c6cef9b");

        Log.d("KeyManagerTest", "checkKeyPairMatch priv1 / pub1");
        secp256R1Manager.checkKeyPairMatch(privateKey, publicKey);
        //Log.d("KeyManagerTest", "checkKeyPairMatch priv1 / pub2");
        //secp256R1Manager.checkKeyPairMatch(privateKey, publicKey2);
        //Log.d("KeyManagerTest", "checkKeyPairMatch priv2 / pub1");
        //secp256R1Manager.checkKeyPairMatch(privateKey2, publicKey);
        Log.d("KeyManagerTest", "checkKeyPairMatch priv2 / pub2");
        secp256R1Manager.checkKeyPairMatch(privateKey2, publicKey2);

        //지문키 등록
//        try {
//            walletApi.setBioPromptListener(new BioPromptHelper.BioPromptInterface() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.d("KeyManagerTest", "MAIN registerBioKey onSuccess");
//                    Log.d("KeyManagerTest","result : " + result);
//                    // 지문키 생성 후 처리
//                }
//
//                @Override
//                public void onFail(String result) {
//                    Log.d("KeyManagerTest", "MAIN registerBioKey onFail");
//                    Log.d("KeyManagerTest","result : " + result);
//                }
//            });
//            walletApi.registerBioKey(this);
//            KeyManager<BaseObject> keyManager = new KeyManager<>("KeyManagerTest", context);
//        SecureKeyGenRequest keyGenInfo2 = new SecureKeyGenRequest();
//        keyGenInfo2.setId("BIO");
//        keyGenInfo2.setAlgoType(AlgorithmType.ALGORITHM_TYPE.SECP256R1);
//        keyGenInfo2.setStorageOption(StorageOption.STORAGE_OPTION.KEYSTORE);
//        keyGenInfo2.setAccessMethod(KeyStoreAccessMethod.KEYSTORE_ACCESS_METHOD.BIOMETRY);

        SecureKeyGenRequest bioKeyRequest = new SecureKeyGenRequest(
                "BIO",
                AlgorithmType.ALGORITHM_TYPE.SECP256R1,
                StorageOption.STORAGE_OPTION.KEYSTORE,
                KeyStoreAccessMethod.KEYSTORE_ACCESS_METHOD.BIOMETRY
        );

        keyManager.generateKey(bioKeyRequest);

        // key wallet 확인
        StorageManager<KeyInfo, DetailKeyInfo> storageManager = new StorageManager<>("walletTest", FileExtension.FILE_EXTENSION.KEY, false, appContext, DetailKeyInfo.class, KeyInfo.class);
        for (UsableInnerWalletItem<KeyInfo, DetailKeyInfo> walletItem : storageManager.getAllItems()) {
            Log.i("KeyManagerTest", "Wallet Meta : " + walletItem.getMeta().toJson());
            Log.i("KeyManagerTest", "Wallet Item : " + walletItem.getItem().toJson());
        }

        for(String alias : KeystoreManager.getKeystoreAliasList()){
            Log.d("KeyManagerTest","Bio key 생성 후 alias : " + alias);
        }

        keyManager.changePin("PIN", "111111".getBytes(), "222222".getBytes());

        keyManager.changePin("PIN", "222222".getBytes(), "111111".getBytes());

        // id에 대한 키인포
        List<String> ids = new ArrayList<>();
        ids.add("PIN");
        ids.add("BIO");
        //List<String> ids = new ArrayList<>(List.of(("FREE")));
        for(KeyInfo keyInfo : keyManager.getKeyInfos(ids)){
            Log.d("KeyManagerTest", "getKeyInfos(ids) : " + keyInfo.toJson());
        }

        // verify auth type 대한 키인포

        for(KeyInfo keyInfo : keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.ANY)){
            Log.i("KeyManagerTest", "getKeyInfos(verifyAuthType) ANY: " + keyInfo.toJson());
        }
        for(KeyInfo keyInfo : keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.FREE)){
            Log.i("KeyManagerTest", "getKeyInfos(verifyAuthType) FREE: " + keyInfo.toJson());
        }
        for(KeyInfo keyInfo : keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.PIN)){
            Log.i("KeyManagerTest", "getKeyInfos(verifyAuthType) PIN: " + keyInfo.toJson());
        }
        for(KeyInfo keyInfo : keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.BIO)){
            Log.i("KeyManagerTest", "getKeyInfos(verifyAuthType) BIO: " + keyInfo.toJson());
        }
        for(KeyInfo keyInfo : keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.PIN_OR_BIO)){
            Log.i("KeyManagerTest", "getKeyInfos(verifyAuthType) PIN_OR_BIO: " + keyInfo.toJson());
        }
        for(KeyInfo keyInfo : keyManager.getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE.PIN_AND_BIO)){
            Log.i("KeyManagerTest", "getKeyInfos(verifyAuthType) PIN_AND_BIO: " + keyInfo.toJson());
        }



        /////////////// 서명, 검증/////////////////
        // passcode 맞는것

        byte[] plainData = "KeyManagerTest".getBytes();
        byte[] digest = DigestUtils.getDigest(plainData, DigestEnum.DIGEST_ENUM.SHA_256);
        byte[] signature = keyManager.sign("PIN", "111111".getBytes(), digest);
        Log.d("KeyManagerTest","PIN 111111 sign Value : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_16, signature));
        ids = new ArrayList<>();
        ids.add("PIN");
        String pubKey = "";
        for(KeyInfo keyInfo : keyManager.getKeyInfos(ids)){
            pubKey = keyInfo.getPublicKey();
            Log.d("KeyManagerTest", "PIN 111111 sign pubKey: " + pubKey);
        }

        keyManager.verify(AlgorithmType.ALGORITHM_TYPE.SECP256R1, MultibaseUtils.decode(pubKey), digest, signature);

        for(String alias : KeystoreManager.getKeystoreAliasList()){
            Log.d("KeyManagerTest","Bio key 서명 전 alias : " + alias);
        }
        // 지문키
        signature = keyManager.sign("BIO", null, "KeyManagerTest".getBytes());
        Log.d("KeyManagerTest","BIO sign Value : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_16, signature));
        ids = new ArrayList<>();
        ids.add("BIO");
        pubKey = "";
        for(KeyInfo keyInfo : keyManager.getKeyInfos(ids)){
            pubKey = keyInfo.getPublicKey();
            Log.d("KeyManagerTest", "BIO sign pubKey: " + pubKey);
        }
        keyManager.verify(AlgorithmType.ALGORITHM_TYPE.SECP256R1, MultibaseUtils.decode(pubKey), "KeyManagerTest".getBytes(), signature);

        //PIN 값 삭제
        ids = new ArrayList<>();
        ids.add("BIO");
        keyManager.deleteKeys(ids);

        //PIN, BIO로 가져오기
        ids = new ArrayList<>();
        ids.add("PIN");
        ids.add("BIO");
        for(KeyInfo keyInfo : keyManager.getKeyInfos(ids)){
            Log.d("KeyManagerTest", "deleteKeys(ids) 후 getKeyInfos(ids) : " + keyInfo.toJson());
        }

        for(String alias : KeystoreManager.getKeystoreAliasList()){
            Log.d("KeyManagerTest","BIO 삭제 후 alias : " + alias);
        }

        keyManager.deleteAllKeys();

        for(String alias : KeystoreManager.getKeystoreAliasList()){
            Log.d("KeyManagerTest","전부삭제 후 alias : " + alias);
        }
    }
}