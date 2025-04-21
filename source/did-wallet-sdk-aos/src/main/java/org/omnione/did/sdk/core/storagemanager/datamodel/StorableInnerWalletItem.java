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

package org.omnione.did.sdk.core.storagemanager.datamodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.omnione.did.sdk.datamodel.util.IntEnumAdapterFactory;
import org.omnione.did.sdk.datamodel.util.JsonSortUtil;
import org.omnione.did.sdk.datamodel.util.StringEnumAdapterFactory;

public class StorableInnerWalletItem<M extends Meta> {
    M meta;
    String item;

    public M getMeta() {
        return meta;
    }

    public void setMeta(M meta) {
        this.meta = meta;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public <T> void fromJson(String val, Class<T> clazz) {
        Gson gson = new Gson();
        TypeToken<StorableInnerWalletItem<M>> typeToken = new TypeToken<StorableInnerWalletItem<M>>() {};
        StorableInnerWalletItem<M> obj = gson.fromJson(val, typeToken.getType());
        this.meta = obj.getMeta();
        this.item = obj.getItem();
    }
    public String toJson() {
        //GsonWrapper gson = new GsonWrapper();
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new IntEnumAdapterFactory())
                .registerTypeAdapterFactory(new StringEnumAdapterFactory())
                .disableHtmlEscaping()
                .create();
        String json = gson.toJson(this);
        return JsonSortUtil.sortJsonString(gson, json);
    }
}
