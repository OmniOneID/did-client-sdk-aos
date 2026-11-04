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

package org.omnione.did.sdk.core.oid4vc.format.mdoc;

import com.upokecenter.cbor.CBORObject;
import com.upokecenter.cbor.CBORType;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

public final class MdocTdate {
    private MdocTdate() {}

    public static Long toEpochSeconds(CBORObject value) {
        if (value == null || value.isNull()) {
            return null;
        }
        CBORObject untagged = value.isTagged() ? value.Untag() : value;
        if (untagged.getType() == CBORType.Integer) {
            try {
                return untagged.AsInt64Value();
            } catch (ArithmeticException outOfRange) {
                return null;
            }
        }
        if (untagged.getType() == CBORType.FloatingPoint) {
            return (long) untagged.AsDoubleValue();
        }
        if (untagged.getType() != CBORType.TextString) {
            return null;
        }
        return parseRfc3339(untagged.AsString());
    }

    public static Long parseRfc3339(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            return Instant.parse(text).getEpochSecond();
        } catch (DateTimeParseException notInstant) {
            try {
                return OffsetDateTime.parse(text).toInstant().getEpochSecond();
            } catch (DateTimeParseException notOffsetDateTime) {
                return null;
            }
        }
    }
}
