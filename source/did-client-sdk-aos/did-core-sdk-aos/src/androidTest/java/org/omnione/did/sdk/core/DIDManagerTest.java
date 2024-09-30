/*
 * Copyright 2024 Raonsecure
 */

package org.omnione.did.sdk.core;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.omnione.did.sdk.datamodel.common.Proof;
import org.omnione.did.sdk.datamodel.common.enums.AlgorithmType;
import org.omnione.did.sdk.datamodel.common.enums.ProofPurpose;
import org.omnione.did.sdk.datamodel.common.enums.ProofType;
import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.did.DIDServiceType;
import org.omnione.did.sdk.datamodel.did.Service;
import org.omnione.did.sdk.utility.DataModels.MultibaseType;
import org.omnione.did.sdk.utility.MultibaseUtils;
import org.omnione.did.sdk.core.didmanager.DIDManager;
import org.omnione.did.sdk.core.didmanager.datamodel.DIDKeyInfo;
import org.omnione.did.sdk.core.didmanager.datamodel.DIDMethodType;
import org.omnione.did.sdk.core.keymanager.KeyManager;
import org.omnione.did.sdk.core.keymanager.datamodel.DetailKeyInfo;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyGenWalletMethodType;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyInfo;
import org.omnione.did.sdk.core.keymanager.datamodel.StorageOption;
import org.omnione.did.sdk.core.keymanager.datamodel.WalletKeyGenRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DIDManagerTest {
    @Test
    public void didManager() throws Exception {
//        assertArrayEquals(a,b) : 배열 a와b가 일치함을 확인
//        assertEquals(a,b) : 객체 a와b의 값이 같은지 확인
//        assertSame(a,b) : 객체 a와b가 같은 객체임을 확인
//        assertTrue(a) : a가 참인지 확인
//        assertNotNull(a) : a객체가 null이 아님을 확인

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        assertEquals("com.opendid.walletcore.test", appContext.getPackageName());

        KeyManager<DetailKeyInfo> keyManager = new KeyManager<>("didTest", appContext);
        if(keyManager.isAnyKeySaved())
            keyManager.deleteAllKeys();
        // NONE KEY 생성
        WalletKeyGenRequest keyGenInfo = new WalletKeyGenRequest(
                "FREE",
                AlgorithmType.ALGORITHM_TYPE.SECP256R1,
                StorageOption.STORAGE_OPTION.WALLET,
                new KeyGenWalletMethodType()
        );
        Log.d("DIDManagerTest", "KeyGenInfo (FREE) : " + keyGenInfo.toJson());
        if(!keyManager.isKeySaved("FREE"))
            keyManager.generateKey(keyGenInfo);

        // pin Key 생성
        keyGenInfo = new WalletKeyGenRequest(
                "PIN",
                AlgorithmType.ALGORITHM_TYPE.SECP256R1,
                StorageOption.STORAGE_OPTION.WALLET,
                new KeyGenWalletMethodType(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, "111111".getBytes()))
        );
        Log.d("DIDManagerTest", "KeyGenInfo (PIN) : " + keyGenInfo.toJson());
        if(!keyManager.isKeySaved("PIN"))
            keyManager.generateKey(keyGenInfo);

        DIDManager<DIDDocument> didManager = new DIDManager<>("didTest", appContext);
        if(didManager.isSaved())
            didManager.deleteDocument();
        // 1.genDID
        String did = DIDManager.genDID("opendid");
        Log.d("DIDManagerTest", "1. genDID : " + did);
        //3.createDocument

        List<KeyInfo> keyInfos = keyManager.getKeyInfos(List.of("FREE","PIN"));
        List<DIDKeyInfo> didKeyInfos = new ArrayList<>();
        for(KeyInfo keyInfo : keyInfos){
            DIDKeyInfo didKeyInfo = new DIDKeyInfo(keyInfo, List.of(DIDMethodType.DID_METHOD_TYPE.assertionMethod), did);
            didKeyInfos.add(didKeyInfo);
        }

        didManager.createDocument(did, didKeyInfos, did, null);
        //6. saveDocument
        didManager.saveDocument();
        //4.getDocument
        DIDDocument didDocument = didManager.getDocument();
        Log.d("DIDManagerTest","2. getDocument : " + didDocument.toJson());

        //5.replaceDocument
        List<Proof> proofs = new ArrayList<>();
        Proof proof = new Proof(
                "2024-08-15T12:20:36Z",
                ProofPurpose.PROOF_PURPOSE.assertionMethod,
                "did:omn:tas?version=1.0#assert",
                ProofType.PROOF_TYPE.secp256r1Signature2018
                );

        proofs.add(proof);
        didDocument.setProof(proof);
        didManager.replaceDocument(didDocument, false);
        Log.d("DIDManagerTest","5. replaceDocument getDocument : " + didDocument.toJson());
        didManager.saveDocument();
        Log.d("DIDManagerTest","5. replaceDocument save getDocument : " + didDocument.toJson());

        //8.addVerificationMethod
        keyGenInfo = new WalletKeyGenRequest(
                "PIN2",
                AlgorithmType.ALGORITHM_TYPE.SECP256R1,
                StorageOption.STORAGE_OPTION.WALLET,
                new KeyGenWalletMethodType(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, "password".getBytes()))
        );
        Log.d("DIDManagerTest","PIN2 keygenInfo : " + keyGenInfo.toJson());
        keyManager.generateKey(keyGenInfo);
        List<KeyInfo> keyInfos2 = keyManager.getKeyInfos(List.of("PIN2"));
        Log.d("DIDManagerTest","PIN2 keygenInfo : " + keyInfos2.get(0).toJson());
        DIDKeyInfo didKeyInfo = new DIDKeyInfo(keyInfos2.get(0), List.of(DIDMethodType.DID_METHOD_TYPE.assertionMethod, DIDMethodType.DID_METHOD_TYPE.authentication), did);
        didManager.addVerificationMethod(didKeyInfo);
        Log.d("DIDManagerTest","addVerificationMethod getDocument : " + didManager.getDocument().toJson());

        didManager.saveDocument();
        Log.d("DIDManagerTest","addVerificationMethod getDocument2 : " + didManager.getDocument().toJson());

        //9.addVerificationMethod
        didManager.removeVerificationMethod("PIN");
        didManager.saveDocument();
        Log.d("DIDManagerTest","removeVerificationMethod getDocument : " + didManager.getDocument().toJson());

        //10.addService
        Service service = new Service("id1", DIDServiceType.DID_SERVICE_TYPE.credentialRegistry, List.of("end1"));
        didManager.addService(service);
        service = new Service("id2", DIDServiceType.DID_SERVICE_TYPE.credentialRegistry, List.of("end2"));
        didManager.addService(service);
        didManager.saveDocument();
        Log.d("DIDManagerTest","addService getDocument : " + didManager.getDocument().toJson());

        //11.removeService
        didManager.removeService("id1");
        Log.d("DIDManagerTest","removeService id1 getDocument : " + didManager.getDocument().toJson());
        didManager.removeService("id2");
        Log.d("DIDManagerTest","removeService id2 getDocument : " + didManager.getDocument().toJson());
        didManager.saveDocument();
        Log.d("DIDManagerTest","removeService getDocument : " + didManager.getDocument().toJson());

        //7. deleteDocument
        didManager.deleteDocument();
        Log.d("DIDManagerTest","isSaved : " + didManager.isSaved());

        keyManager.deleteAllKeys();

    }
}