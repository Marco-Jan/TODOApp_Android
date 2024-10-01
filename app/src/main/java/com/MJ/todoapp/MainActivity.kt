package com.MJ.todoapp

import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.MJ.todoapp.databinding.ActivityMainBinding
import com.MJ.todoapp.databinding.TodoListCardItemBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    //View Binding
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    }
}

data class TodoList(
    val title: String,
    val items: MutableList<Item>
)

data class Item(
    val name: String,
    val isDOne: Boolean = false,
)

class TodoListAdapter(
    private val todoLists: MutableList<TodoList>,
    private val onItemClicked: (TodoList) -> Unit

) : RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {
    //Viewholder: Hält die Referenzen zu den Views der Listenelemente
    inner class TodoListViewHolder(val binding: TodoListCardItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(todoList: TodoList) {
                    //setzt den Titel auf die Anzahl der Items
                    binding.todoListTitle.text = todoList.title
                    binding.todoListItemCount.text = "${todoList.items.size} Items"

                    //Click-Event, wenn ein TodoList-Element angeklickt wird
                    binding.root.setOnClicklistener {
                        onItemClicked(todoList)
                    }
                }
            }
    //Erstellt ein neues View für ein Listenelement (Viewholder)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TodoListCardItemBinding.inflate(inflater, parent, false)
        return TodoListViewHolder(binding)
    }
    //bindet die Daten an die Views des Viewholder
    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        val todoList = todoLists[position]
        holder.bind(todoList)
    }
    //Gibt die Anzahl der Todo-Listen zurück

    override fun getItemCount(): Int = todoLists.size
}























