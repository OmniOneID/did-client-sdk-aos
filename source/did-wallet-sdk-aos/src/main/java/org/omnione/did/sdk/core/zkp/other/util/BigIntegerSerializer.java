/*
 * Copyright 2025 OmniOne.
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

package org.omnione.did.sdk.core.zkp.other.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigInteger;

public class BigIntegerSerializer implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {

        TypeAdapter defaultAdapter = gson.getAdapter(typeToken);
        return new Adapter(defaultAdapter);
    }

    private static class Adapter<T extends BigInteger> extends TypeAdapter<T> {
        private final TypeAdapter<T> defaultAdapter;

        Adapter(TypeAdapter<T> defaultAdapter) {
            this.defaultAdapter = defaultAdapter;
        }

        public void write(JsonWriter out, T value) throws IOException {
            out.value(value.toString());
        }

        public T read(JsonReader in) throws IOException {
            return defaultAdapter.read(in);
        }
    }
}