package com.example.noteapp3

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepository {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun addNote(note: Note, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val noteId = database.child("notes").push().key
        note.id = noteId ?: ""
        database.child("notes").child(note.id).setValue(note)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun updateNote(note: Note, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        database.child("notes").child(note.id).setValue(note)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun deleteNote(noteId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        database.child("notes").child(noteId).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getNotes(onDataReceived: (List<Note>) -> Unit, onFailure: (Exception) -> Unit) {
        database.child("notes").get().addOnSuccessListener { snapshot ->
            val notes = snapshot.children.mapNotNull { it.getValue(Note::class.java) }
            onDataReceived(notes)
        }.addOnFailureListener { onFailure(it) }
    }
}
