package com.MJ.todoapp
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.MJ.todoapp.databinding.ListItemBinding


class ItemAdapter(private val items: MutableList<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    // ViewHolder-Klasse hält die Referenzen zu den Views eines Listenelements (Items)
    inner class ItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        // Bindet die Daten eines Items an die View
        fun bind(item: Item) {
            binding.itemName.text = item.name // Setzt den Namen des Items in der TextView
            if (item.isDone) {
                // Wenn das Item erledigt ist, wird der Text durchgestrichen und die Farbe geändert
                binding.itemName.paintFlags = binding.itemName.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                binding.itemName.setTextColor(binding.root.context.getColor(R.color.gray))
            } else {
                // Wenn das Item nicht erledigt ist, entferne den Durchstreichungseffekt
                binding.itemName.paintFlags = binding.itemName.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.itemName.setTextColor(binding.root.context.getColor(android.R.color.black))
            }

            // Wenn das Item angeklickt wird, ändert sich sein Status (erledigt/nicht erledigt)
            binding.itemName.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    item.isDone = !item.isDone // Ändere den Status (isDone)
                    notifyItemChanged(position) // Benachrichtige den Adapter, dass sich das Item geändert hat
                }
            }

            // Entfernt das Item, wenn auf das Löschsymbol geklickt wird
            binding.deleteIcon.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    removeItem(position) // Entfernt das Item an der aktuellen Position
                }
            }
        }
    }

    // Erstellt ein neues ViewHolder-Objekt und bindet das ListItem-Layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    // Bindet die Daten eines Items an das ViewHolder-Objekt
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    // Gibt die Anzahl der Items zurück
    override fun getItemCount(): Int = items.size

    // Fügt ein neues Item zur Liste hinzu
    fun addItem(item: Item) {
        items.add(item) // Füge das neue Item zur Liste hinzu
        notifyItemInserted(items.size - 1) // Benachrichtige den Adapter, dass ein neues Item hinzugefügt wurde
    }

    // Entfernt ein Item aus der Liste
    fun removeItem(position: Int) {
        items.removeAt(position) // Entferne das Item an der angegebenen Position
        notifyItemRemoved(position) // Benachrichtige den Adapter, dass das Item entfernt wurde
    }
}
