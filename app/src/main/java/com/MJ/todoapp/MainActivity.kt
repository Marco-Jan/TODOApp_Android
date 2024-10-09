package com.MJ.todoapp
import com.MJ.todoapp.TodoList
import com.MJ.todoapp.Item


import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MJ.todoapp.databinding.ActivityMainBinding
import com.MJ.todoapp.databinding.DialogAddListBinding
import com.MJ.todoapp.databinding.TodoListCardItemBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoListAdapter: TodoListAdapter
    private lateinit var listActivityLauncher: ActivityResultLauncher<Intent>

    private val todoLists = mutableListOf<TodoList>() // Liste aller To-Do-Listen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialisiere das View-Binding und setze das Layout für die Activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialisiere den Adapter und setze ihn auf den RecyclerView
        todoListAdapter = TodoListAdapter(todoLists,
            { todoList: TodoList ->
                // Startet die SingleListViewActivity mit der ausgewählten Liste
                val intent = Intent(this, SingleListViewActivity::class.java).apply {
                    putExtra("todoList", todoList) // Übertrage die ausgewählte Liste an die neue Activity
                }
                listActivityLauncher.launch(intent)
            },
            { todoList: TodoList, position ->
                // Zeigt einen Bestätigungsdialog zum Löschen der Liste an
                showDeleteConfirmationDialog(todoList, position)
            }
        )

        // Setze den LayoutManager und den Adapter für den RecyclerView
        binding.recyclerViewList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = todoListAdapter
        }

        // Beispiel-Daten für To-Do-Listen
        val exampleList = TodoList("Einkaufsliste", mutableListOf(
            Item("Bananen", false),
            Item("Mehl", false),
            Item("Milch", false)
        ))
        val exampleList2 = TodoList("Garten", mutableListOf(
            Item("Rindenmulch", false)
        ))

        // Füge die Beispiel-Listen zum Adapter hinzu
        todoListAdapter.addTodoList(exampleList)
        todoListAdapter.addTodoList(exampleList2)

        // Event-Handler zum Hinzufügen einer neuen Liste
        binding.addListButton.setOnClickListener {
            addNewList()
        }

        // Registriert einen Activity-Result-Launcher, um Ergebnisse von anderen Activities zu empfangen
        listActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            // Überprüfe, ob die Rückgabe erfolgreich war, und aktualisiere die Liste
            if (result.resultCode == RESULT_OK) {
                val updatedTodoList = result.data?.getParcelableExtraProvider<TodoList>("updatedTodoList")
                updatedTodoList?.let { updatedList ->
                    val index = todoLists.indexOfFirst { it.title == updatedList.title }
                    if (index != -1) {
                        todoLists[index] = updatedList
                        todoListAdapter.notifyItemChanged(index)
                    }
                }
            }
        }
    }

    // Öffnet einen Dialog, um eine neue Liste hinzuzufügen
    fun addNewList() {
        val inflater = layoutInflater
        val dialogBinding = DialogAddListBinding.inflate(inflater)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Neue Liste hinzufügen")
        builder.setView(dialogBinding.root)
        builder.setPositiveButton("Hinzufügen", null)
        builder.setNegativeButton("Abbrechen", null)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val listName = dialogBinding.listNameInput.text.toString().trim()
                if (listName.isNotEmpty()) {
                    // Füge die neue Liste hinzu und aktualisiere den Adapter
                    val newList = TodoList(listName, mutableListOf())
                    todoListAdapter.addTodoList(newList)
                    dialog.dismiss()
                } else {
                    dialogBinding.errorMessage.visibility = View.VISIBLE // Zeige Fehlermeldung an, wenn der Name leer ist
                }
            }
        }
        dialog.show()
    }

    // Zeigt einen Bestätigungsdialog an, um eine Liste zu löschen
    fun showDeleteConfirmationDialog(todoList: TodoList, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Liste löschen")
        builder.setMessage("Möchten Sie die Liste \"${todoList.title}\" wirklich löschen?")
        builder.setPositiveButton("Löschen") { _, _ ->
            // Löscht die Liste und benachrichtigt den Adapter
            val listName = todoLists[position].title
            todoListAdapter.removeTodoList(position)
            Toast.makeText(binding.root.context, "$listName gelöscht", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Abbrechen", null)
        builder.show()
    }
}





























