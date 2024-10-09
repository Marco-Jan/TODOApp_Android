package com.MJ.todoapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.MJ.todoapp.databinding.ActivitySingleListViewBinding
import com.MJ.todoapp.databinding.DialogAddListBinding
import com.MJ.todoapp.databinding.DialogAddItemBinding


class SingleListViewActivity : AppCompatActivity() {
    private lateinit var todoList: TodoList // Die ausgewählte To-Do-Liste
    private lateinit var itemAdapter: ItemAdapter // Adapter für die Items der Liste

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setze das View-Binding und das Layout für diese Activity
        var binding = ActivitySingleListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Erhalte die übertragene TodoList aus dem Intent
        todoList = intent.getParcelableExtraProvider("todoList") ?: return

        // Initialisiere den Adapter für die Items der Liste
        itemAdapter = ItemAdapter(todoList.items)
        binding.recyclerViewItemList.apply {
            layoutManager = LinearLayoutManager(this@SingleListViewActivity) // Setze den LayoutManager
            adapter = itemAdapter // Setze den Adapter für die Items
        }

        binding.listName.text = todoList.title // Setze den Listennamen in der UI

        // Zurück-Button: Übertrage die aktualisierte Liste zurück an die MainActivity
        binding.backButton.setOnClickListener {
            val intent = Intent().apply {
                putExtra("updatedTodoList", todoList)
            }
            setResult(RESULT_OK, intent)
            finish() // Beende die Activity
        }

        // Event-Handler für das Hinzufügen eines neuen Items
        binding.addItemButton.setOnClickListener {
            addNewItem()
        }

        // Event-Handler zum Löschen erledigter Items
        binding.deleteCompletedItemsButton.setOnClickListener {
            deleteCompletedItems()  // Rufe die Funktion auf, um den Löschvorgang zu starten
        }
    }

    // Öffnet einen Dialog, um ein neues Item hinzuzufügen
    fun addNewItem() {
        val inflater = layoutInflater
        val dialogBinding = DialogAddItemBinding.inflate(inflater) // Verwende das richtige Binding-Objekt
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Neues Item hinzufügen")
        builder.setView(dialogBinding.root)
        builder.setPositiveButton("Hinzufügen", null)
        builder.setNegativeButton("Abbrechen", null)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val itemName = dialogBinding.itemNameInput.text.toString().trim()
                if (itemName.isNotEmpty()) {
                    val newItem = Item(itemName, false) // Erstelle ein neues Item mit dem Namen
                    itemAdapter.addItem(newItem)  // Füge das Item dem Adapter hinzu
                    dialog.dismiss()
                } else {
                    dialogBinding.errorMessage.visibility = View.VISIBLE // Zeige Fehlermeldung an, wenn der Name leer ist
                }
            }
        }
        dialog.show()
    }

    // Löscht alle erledigten Items nach Bestätigung
    fun deleteCompletedItems() {
        if (todoList.items.any { it.isDone }) {
            // Zeigt einen Bestätigungsdialog zum Löschen erledigter Items an
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Erledigte Items löschen")
            builder.setMessage("Möchten Sie wirklich alle erledigten Items löschen?")
            builder.setPositiveButton("Löschen") { dialog, _ ->
                val itemsToRemove = todoList.items.filter { it.isDone } // Filtere erledigte Items
                todoList.items.removeAll(itemsToRemove) // Entferne erledigte Items
                itemAdapter.notifyDataSetChanged()  // Aktualisiere den RecyclerView
                Toast.makeText(this, "Erledigte Items gelöscht", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            builder.setNegativeButton("Abbrechen") { dialog, _ ->
                dialog.dismiss()  // Dialog schließen, wenn Abbrechen gewählt wird
            }
            builder.show()
        } else {
            Toast.makeText(this, "Keine erledigten Items zum Löschen vorhanden", Toast.LENGTH_SHORT).show()
        }
    }
}
