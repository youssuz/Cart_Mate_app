package com.mobile.cartmate

import android.content.Intent
import android.icu.text.UnicodeSetSpanner
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobile.cartmate.data.ItemDatabase
import com.mobile.cartmate.data.ItemEntity
import com.mobile.cartmate.databinding.ActivityAddBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {
    val TAG = "AddActivity"
    val addBinding by lazy { ActivityAddBinding.inflate(layoutInflater) }
    val itemDao by lazy {
        ItemDatabase.getDatabase(applicationContext).itemDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(addBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        addBinding.btnAddItem.setOnClickListener {
            val product = addBinding.etAddProduct.text.toString()
            val price = addBinding.etAddPrice.text.toString()
            val count = addBinding.etAddCount.text.toString()
            if (product == "" || price == "" || count == "") {
                Toast.makeText(this, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                val newItem = ItemEntity(0, product, image, price.toInt(), count.toInt())
                Log.i(TAG, "${price}원 ${product} ${count}개")
                CoroutineScope(Dispatchers.IO).launch {
                    itemDao.insertItem(newItem)
                    finish()
                }
            }
        }
        addBinding.btnCancel.setOnClickListener {
            finish()
        }

        registerForContextMenu(addBinding.ivAddItem)
    }

    /*카테고리(이미지) 선택 Context Menu */
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        when(v?.id){
            R.id.ivAddItem -> {
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

    var image = R.drawable.food01
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item01 -> {
                image = R.drawable.food01
                addBinding.ivAddItem.setImageResource(image)
                true
            }

            R.id.item02 -> {
                image = R.drawable.food02
                addBinding.ivAddItem.setImageResource(image)
                true
            }

            R.id.item03 -> {
                image = R.drawable.food03
                addBinding.ivAddItem.setImageResource(image)
                true
            }

            R.id.item04 -> {
                image = R.drawable.food04
                addBinding.ivAddItem.setImageResource(image)
                true
            }

            R.id.item05 -> {
                image = R.drawable.food05
                addBinding.ivAddItem.setImageResource(image)
                true
            }

            R.id.item06 -> {
                image = R.drawable.food06
                addBinding.ivAddItem.setImageResource(image)
                true
            }

            R.id.item07 -> {
                image = R.drawable.food07
                addBinding.ivAddItem.setImageResource(image)
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }
}