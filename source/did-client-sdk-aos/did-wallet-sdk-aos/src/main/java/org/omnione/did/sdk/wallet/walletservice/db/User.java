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

package org.omnione.did.sdk.wallet.walletservice.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.omnione.did.sdk.wallet.walletservice.util.WalletUtil;

@Entity(tableName = "tbl_user")
public class User {
    @PrimaryKey
    @NotNull
    public String idx;
    @ColumnInfo(name="pii")
    public String pii;
    @ColumnInfo(name="final_enc_key")
    public String fek;
    @ColumnInfo(name="create_date")
    public String createDate;
    @ColumnInfo(name="update_date")
    public String updateDate;

    public User(){
        this.idx = WalletUtil.genId();
    }

    public User(String pii){
        this.idx = WalletUtil.genId();
        this.pii = pii;
        this.createDate = WalletUtil.getDate();
    }
}
