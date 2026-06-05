package com.mobile.cartmate.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity ("cart_items")
data class ItemEntity(
    @PrimaryKey (autoGenerate = true) val id: Int,
    val product: String,
    val imageResId: Int,
    val price: Int,
    val count: Int
): Serializable {
    fun totalPrice(): Int {
        return price * count
    }
}
