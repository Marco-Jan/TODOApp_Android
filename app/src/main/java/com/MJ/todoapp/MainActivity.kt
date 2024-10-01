package com.MJ.todoapp

import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MJ.todoapp.databinding.ActivityMainBinding
import com.MJ.todoapp.databinding.TodoListCardItemBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var todoListAdapter: TodoListAdapter

    private val todoLists = mutableListOf<TodoList>()

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    //View Binding
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    //Initialisiere den ADapter und se´tze ihn auf den RecyclerView
    todoListAdapter = TodoListAdapter(todoLists) {
        todoList -> //handle item Click
    }

    binding.recyclerViewList.apply {
        layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = todoListAdapter
    }

    //Beispiel-Daten

    val exampleList = TodoList("Einkaufsliste", mutableListOf(
        Item("Bananen", false),
        Item("Mehl", false),
        Item("Milch", false)
    ))
    val exampleList2 = TodoList("Garten", mutableListOf(
        Item("Rindenmulch", false)
    ))

        todoListAdapter.addTodoList(exampleList)
        todoListAdapter.addTodoList(exampleList2)


    }
}





























