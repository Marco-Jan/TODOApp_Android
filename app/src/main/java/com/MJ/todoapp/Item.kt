package com.MJ.todoapp

import android.os.Parcel
import android.os.Parcelable

data class Item (
    val name: String,
    var isDone: Boolean = false
) : Parcelable {

    // Konstruktor zum Erstellen eines Items aus einem Parcel (zum Wiederherstellen von Parcelable-Daten)
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "", // Lese den Namen des Items aus dem Parcel
        parcel.readByte() != 0.toByte() // Lese den Status (isDone) des Items aus dem Parcel
    )

    // Schreibt das Item in ein Parcel, um es über Intents zu versenden
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name) // Schreibe den Namen in das Parcel
        parcel.writeByte(if (isDone) 1 else 0) // Schreibe den Status (true/false) als Byte (1 oder 0)
    }

    // Beschreibt den Inhalt (meistens 0)
    override fun describeContents(): Int = 0

    // Companion-Objekt zur Erstellung eines Items aus einem Parcel und zur Bereitstellung eines Arrays
    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item = Item(parcel) // Erstellt ein Item aus einem Parcel
        override fun newArray(size: Int): Array<Item?> = arrayOfNulls(size) // Erstellt ein Array von Items
    }
}
