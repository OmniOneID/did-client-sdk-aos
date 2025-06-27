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

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CaPkgDao {
    @Query("SELECT * FROM tbl_ca_pkg")
    List<CaPkg> getAll();

    @Query("SELECT * FROM tbl_ca_pkg WHERE idx IN (:id)")
    List<CaPkg> loadAllByIds(int[] id);

    @Insert
    void insertAll(CaPkg... caPkgs);

    @Delete
    void delete(CaPkg caPkg);

    @Query("DELETE FROM tbl_ca_pkg")
    void deleteAll();
}
