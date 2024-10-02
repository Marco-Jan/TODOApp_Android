package com.MJ.todoapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.MJ.todoapp.databinding.ActivitySingleListViewBinding

class SingleListViewActivity : AppCompatActivity() {
    private lateinit var todoList: TodoList
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivitySingleListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoList = intent.getParcelableExtraProvider("todoList") ?: return

        itemAdapter = ItemAdapter(todoList.items)
        binding.recyclerViewItemList.apply {
            layoutManager = LinearLayoutManager(this@SingleListViewActivity)
            adapter = itemAdapter
        }

        binding.listName.text = todoList.title

        binding.backButton.setOnClickListener{
            val intent = Intent().apply {
                putExtra("updatedTodoList", todoList)
        }

            setResult(RESULT_OK, intent)
            finish()
        }
    }
}