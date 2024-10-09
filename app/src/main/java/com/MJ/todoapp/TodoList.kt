package com.MJ.todoapp

import android.os.Parcel
import android.os.Parcelable

data class TodoList(
    val title: String, // Der Titel der To-Do-Liste
    val items: MutableList<Item> // Die Liste der Items, die zur To-Do-Liste gehören
) : Parcelable {

    // Konstruktor zum Erstellen einer TodoList aus einem Parcel
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "", // Lese den Titel der Liste aus dem Parcel
        parcel.createTypedArrayList(Item.CREATOR) ?: mutableListOf() // Lese die Items aus dem Parcel
    )

    // Schreibt die To-Do-Liste in ein Parcel, um sie zu übertragen
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title) // Schreibe den Titel in das Parcel
        parcel.writeTypedList(items) // Schreibe die Items in das Parcel
    }

    // Beschreibt den Inhalt
    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<TodoList> {
        override fun createFromParcel(parcel: Parcel): TodoList = TodoList(parcel) // Erstellt eine TodoList aus einem Parcel
        override fun newArray(size: Int): Array<TodoList?> = arrayOfNulls(size) // Erstellt ein Array von TodoLists
    }
}

