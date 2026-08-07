/*
 * Copyright 2026 OmniOne.
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

package org.omnione.did.sdk.core.oid4vc.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class TxCodeSpec {

    public enum InputMode { NUMERIC, TEXT }

    private final int length;
    @NonNull private final InputMode inputMode;
    @Nullable private final String description;

    public TxCodeSpec(int length, @NonNull InputMode inputMode, @Nullable String description) {
        this.length = length;
        this.inputMode = inputMode;
        this.description = description;
    }

    public int getLength() { return length; }
    @NonNull public InputMode getInputMode() { return inputMode; }
    @Nullable public String getDescription() { return description; }

    public static InputMode parseInputMode(@Nullable String raw) {
        if (raw == null) return InputMode.NUMERIC;
        return "text".equalsIgnoreCase(raw) ? InputMode.TEXT : InputMode.NUMERIC;
    }
}
