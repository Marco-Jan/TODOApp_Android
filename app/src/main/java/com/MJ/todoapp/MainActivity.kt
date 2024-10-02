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

    private val todoLists = mutableListOf<TodoList>()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    //View Binding
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    //Initialisiere den ADapter und se´tze ihn auf den RecyclerView
        todoListAdapter = TodoListAdapter(todoLists,
            { todoList: TodoList ->
                val intent = Intent(this, SingleListViewActivity::class.java).apply{
                    putExtra("todoList", todoList) //Übertrage die TodoList über den intent
                }
                startActivity(intent)

            },
            { todoList: TodoList, position ->
                showDeleteConfirmationDialog(todoList, position)
            }
        )

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

        binding.addListButton.setOnClickListener{
            addNewList();
        }

        listActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {result ->
            if(result.resultCode == RESULT_OK) {
                val updatedTodoList = result.data?.getParcelableExtraProvider<TodoList>("updatedTodoList")
                updatedTodoList?.let { updatedList ->
                    val index = todoLists.indexOfFirst { it.title == updatedList.title }
                    if(index != -1) {
                        todoLists[index] = updatedList
                        todoListAdapter.notifyItemChanged(index)
                    }
                }
            }
        }
    }

    fun addNewList() {
       val inflater = layoutInflater
       val dialogBinding = DialogAddListBinding.inflate(inflater)
       val builder = AlertDialog.Builder(this)
       builder.setTitle("Neue Liste hinzufügen")
       builder.setView(dialogBinding.root)
       builder.setPositiveButton("Hinzufügen", null)
       builder.setNegativeButton("Abbrechen", null)

       val dialog = builder.create()
       dialog.setOnShowListener{
           val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
           val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

           positiveButton.setOnClickListener{
               val listName = dialogBinding.listNameInput.text.toString().trim()
               if(listName.isNotEmpty()){
                   val newList =TodoList(listName, mutableListOf())
                   todoListAdapter.addTodoList(newList)
               dialog.dismiss()
               } else {
                   dialogBinding.errorMessage.visibility = View.VISIBLE
                   dialogBinding.listNameInput.requestFocus()
               }
           }
           negativeButton.setOnClickListener{
               dialog.dismiss()
           }
       }
        dialog.show()
    }

    fun showDeleteConfirmationDialog(todoList: TodoList, position: Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Liste löschen")
        builder.setMessage("Möchten Sie die Liste \"${todoList.title}\" wirklich löschen?")
        builder.setPositiveButton("Löschen") {_, _ ->
            val listName = todoLists[position].title
            todoListAdapter.removeTodoList(position)
            Toast.makeText(
                binding.root.context,
                "$listName gelöscht",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("Abbrechen", null)
        builder.show()
    }
}





























