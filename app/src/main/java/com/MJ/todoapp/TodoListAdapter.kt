package com.MJ.todoapp
import com.MJ.todoapp.TodoList
import com.MJ.todoapp.Item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.MJ.todoapp.databinding.TodoListCardItemBinding

class TodoListAdapter(
    private val todoLists: MutableList<TodoList>, // Liste der To-Do-Listen
    private val onItemClicked: (TodoList) -> Unit, // Callback für Klicks auf To-Do-Listen
    private val onItemLongClicked: (TodoList, Int) -> Unit // Callback für Lang-Klicks auf To-Do-Listen
) : RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    // ViewHolder-Klasse hält die Referenzen zu den Views der Listenelemente
    inner class TodoListViewHolder(val binding: TodoListCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bindet die To-Do-Liste an die UI
        fun bind(todoList: TodoList) {
            binding.todoListTitle.text = todoList.title // Setze den Titel der Liste
            binding.todoListItemCount.text = binding.root.context.resources.getQuantityString(
                R.plurals.todo_list_item_count, // Setze die Anzahl der Items
                todoList.items.size,
                todoList.items.size
            )

            // Click-Event, wenn eine To-Do-Liste angeklickt wird
            binding.root.setOnClickListener {
                onItemClicked(todoList) // Rufe das Klick-Callback auf
            }

            // Long-Click-Event, um eine To-Do-Liste zu löschen
            binding.root.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLongClicked(todoList, position) // Rufe das Lang-Klick-Callback auf
                }
                true
            }
        }
    }

    // Erstellt ein neues ViewHolder-Objekt und bindet das Layout der To-Do-Liste
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TodoListCardItemBinding.inflate(inflater, parent, false)
        return TodoListViewHolder(binding)
    }

    // Bindet die Daten einer To-Do-Liste an den ViewHolder
    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        val todoList = todoLists[position]
        holder.bind(todoList)
    }

    // Gibt die Anzahl der To-Do-Listen zurück
    override fun getItemCount(): Int = todoLists.size

    // Fügt eine neue To-Do-Liste hinzu und benachrichtigt den Adapter
    fun addTodoList(todoList: TodoList) {
        todoLists.add(todoList) // Füge die neue Liste hinzu
        notifyItemInserted(todoLists.size - 1) // Benachrichtige den Adapter über das Hinzufügen
    }

    // Entfernt eine To-Do-Liste und benachrichtigt den Adapter
    fun removeTodoList(position: Int) {
        todoLists.removeAt(position) // Entferne die Liste an der angegebenen Position
        notifyItemRemoved(position) // Benachrichtige den Adapter über das Entfernen
    }
}
