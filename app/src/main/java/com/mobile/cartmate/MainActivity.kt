package com.mobile.cartmate

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.cartmate.data.ItemDatabase
import com.mobile.cartmate.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val itemDao by lazy {
        ItemDatabase.getDatabase(applicationContext).itemDao()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = ItemListAdapter()
        // EditActivity
        adapter.clickListener = { pos ->
            val item = adapter.currentList[pos]
            val intent : Intent = Intent(this, EditActivity::class.java)
            intent.putExtra("item", item)
            Log.i(TAG, "${item.product} 수정")

            startActivity(intent)
        }
        //delete item
        adapter.longClickListener = { pos ->
            val item = adapter.currentList[pos]
            val builder: AlertDialog.Builder = AlertDialog.Builder(this).apply {
                setTitle("장바구니 아이템 삭제")
                setMessage("${item.product}을/를 장바구니에서 삭제하시겠습니까?")
                setPositiveButton("삭제") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.i(TAG, "${item.product} 삭제")
                        itemDao.deleteItem(item)
                    }
                }
                setNegativeButton("취소", null)
                setCancelable(false)
            }
            builder.show()
            true
        }

        /*Flow*/
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val listFlow = itemDao.getAllItems()
                listFlow.distinctUntilChanged().collect { list ->
                    adapter.submitList(list)

                    val totalPrice = list.sumOf { item ->
                        item.totalPrice()
                    }
                    binding.tvTotal.text = "총 ${totalPrice}원"
                }
            }
        }


        binding.rvItems.adapter = adapter
        binding.rvItems.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.itemAdd -> { // 장바구니 추가 AddActivity
                val intent : Intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.itemIntroduce -> { // 개발자 소개
                val builder: AlertDialog.Builder = AlertDialog.Builder(this).apply {
                    setTitle("개발자 소개")
                    setMessage("컴퓨터학과 윤수지")
                    setPositiveButton("확인", null)
                    setCancelable(false)
                }
                builder.show()
                true
            }
            R.id.itemfinish -> { // 앱 종료
                val builder: AlertDialog.Builder = AlertDialog.Builder(this).apply {
                    setTitle("앱 종료")
                    setMessage("앱을 종료하시겠습니까?")
                    setNegativeButton("취소", null)
                    setPositiveButton("종료") { di, _ ->
                        Toast.makeText(this@MainActivity, "종료!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    setCancelable(false)
                }
                builder.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
