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

@Entity(tableName = "tbl_ca_pkg")
public class CaPkg {
    @PrimaryKey
    @NotNull
    public String idx;
    @ColumnInfo(name="pkg_name")
    public String pkgName;
    @ColumnInfo(name="create_date")
    public String createDate;

    public CaPkg(){
        this.idx = WalletUtil.genId();
    }

    public CaPkg(String pkgName){
        this.idx = WalletUtil.genId();
        this.pkgName = pkgName;
        this.createDate = WalletUtil.getDate();
    }
}
