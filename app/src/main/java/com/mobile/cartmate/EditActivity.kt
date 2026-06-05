package com.mobile.cartmate

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobile.cartmate.data.ItemDatabase
import com.mobile.cartmate.data.ItemEntity
import com.mobile.cartmate.databinding.ActivityEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class EditActivity : AppCompatActivity() {
    val TAG = "EditActivity"
    val editBinding by lazy { ActivityEditBinding.inflate(layoutInflater) }
    val itemDao by lazy {
        ItemDatabase.getDatabase(applicationContext).itemDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(editBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val item = intent.getSerializableExtra("item") as ItemEntity
        editBinding.ivEditItem.setImageResource(item.imageResId)
        editBinding.etEditProduct.setText(item.product)
        editBinding.etEditPrice.setText(item.price.toString())
        editBinding.etEditCount.setText(item.count.toString())
        image = item.imageResId

        editBinding.btnEditItem.setOnClickListener {
            val product = editBinding.etEditProduct.text.toString()
            val price = editBinding.etEditPrice.text.toString().toInt()
            val count = editBinding.etEditCount.text.toString().toInt()
            val updateItem = ItemEntity(item.id, product, image, price, count)

            if (product != item.product) {
                Log.i(TAG, "상품명 ${item.product}를 ${product}로 수정")
            }
              if (price != item.price) {
                Log.i(TAG, "가격 ${item.price}원을 ${price}원으로 수정")
            }
              if (count != item.count) {
                Log.i(TAG, "수량 ${item.count}개를 ${count}개로 수정")
            }

            CoroutineScope(Dispatchers.IO).launch {
                itemDao.updateItem(updateItem)
                finish()
            }
        }
        editBinding.btnCancel2.setOnClickListener {
            finish()
        }

        registerForContextMenu(editBinding.ivEditItem)
    }

    /*카테고리(이미지) 선택 Context Menu */
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        when(v?.id){
            R.id.ivEditItem -> {
                menuInflater.inflate(R.menu.menu_image, menu)
                when (image) {
                    R.drawable.food01 -> menu?.findItem(R.id.item01)?.isChecked = true
                    R.drawable.food02 -> menu?.findItem(R.id.item02)?.isChecked = true
                    R.drawable.food03 -> menu?.findItem(R.id.item03)?.isChecked = true
                    R.drawable.food04 -> menu?.findItem(R.id.item04)?.isChecked = true
                    R.drawable.food05 -> menu?.findItem(R.id.item05)?.isChecked = true
                    R.drawable.food06 -> menu?.findItem(R.id.item06)?.isChecked = true
                    R.drawable.food07 -> menu?.findItem(R.id.item07)?.isChecked = true
                }
            }
        }
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    var image: Int = R.drawable.food01
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item01 -> {
                image = R.drawable.food01
                editBinding.ivEditItem.setImageResource(image)
                true
            }

            R.id.item02 -> {
                image = R.drawable.food02
                editBinding.ivEditItem.setImageResource(image)
                true
            }

            R.id.item03 -> {
                image = R.drawable.food03
                editBinding.ivEditItem.setImageResource(image)
                true
            }

            R.id.item04 -> {
                image = R.drawable.food04
                editBinding.ivEditItem.setImageResource(image)
                true
            }

            R.id.item05 -> {
                image = R.drawable.food05
                editBinding.ivEditItem.setImageResource(image)
                true
            }

            R.id.item06 -> {
                image = R.drawable.food06
                editBinding.ivEditItem.setImageResource(image)
                true
            }

            R.id.item07 -> {
                image = R.drawable.food07
                editBinding.ivEditItem.setImageResource(image)
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }
}