package com.mobile.cartmate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert
    suspend fun insertItem(item: ItemEntity)
    @Update
    suspend fun updateItem(item: ItemEntity)
    @Delete
    suspend fun deleteItem(item: ItemEntity)
    @Query ("SELECT * FROM cart_items")
    fun getAllItems(): Flow<List<ItemEntity>>
}