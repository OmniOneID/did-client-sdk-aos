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
import androidx.room.Update;

import java.util.List;

@Dao
public interface TokenDao {
    @Query("SELECT * FROM tbl_token")
    List<Token> getAll();

    @Query("SELECT * FROM tbl_token WHERE idx IN (:id)")
    List<Token> loadAllByIds(String[] id);

    @Insert
    void insertAll(Token... tokens);

    @Update
    void update(Token token);

    @Delete
    void delete(Token token);

    @Query("DELETE FROM tbl_token WHERE idx = :idx")
    int deleteByIdx(String idx);

    @Query("DELETE FROM tbl_token")
    void deleteAll();
}
