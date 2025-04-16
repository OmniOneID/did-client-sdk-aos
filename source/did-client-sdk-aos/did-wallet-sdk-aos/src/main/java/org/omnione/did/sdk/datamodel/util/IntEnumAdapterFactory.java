/*
 * Copyright 2024 OmniOne.
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

package org.omnione.did.sdk.datamodel.util;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class IntEnumAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (!IntEnum.class.isAssignableFrom(rawType) || !Enum.class.isAssignableFrom(rawType)) {
            return null; // Only handle IntEnum enums
        }

        return (TypeAdapter<T>) new TypeAdapter<IntEnum>() {
            @Override
            public void write(JsonWriter out, IntEnum value) throws IOException {
                out.value(value.getValue());
            }

            @Override
            public IntEnum read(JsonReader in) throws IOException {
                int value = in.nextInt();
                Method fromValueMethod;
                try {
                    fromValueMethod = rawType.getMethod("fromValue", int.class);
                    return (IntEnum) fromValueMethod.invoke(null, value);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new IOException("Failed to deserialize enum", e);
                }
            }
        };
    }
}