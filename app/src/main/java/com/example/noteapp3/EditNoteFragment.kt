package com.example.noteapp3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.noteapp3.databinding.FragmentEditNoteBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase

class EditNoteFragment : Fragment() {

    private lateinit var editNoteTitle: EditText
    private lateinit var editNoteDesc: EditText
    private lateinit var saveButton: FloatingActionButton
    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEditNoteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_note, container, false
        )

        editNoteTitle = binding.editNoteTitle
        editNoteDesc = binding.editNoteDesc
        saveButton = binding.editNoteFab

        // Retrieve the note passed from HomeFragment
        note = arguments?.getParcelable("note")
        note?.let {
            editNoteTitle.setText(it.title)
            editNoteDesc.setText(it.description)
        }

        // Firebase Database reference
        val database = FirebaseDatabase.getInstance().getReference("notes")

        saveButton.setOnClickListener {
            val title = editNoteTitle.text.toString()
            val description = editNoteDesc.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                note?.let {
                    val updatedNote = it.copy(title = title, description = description)
                    database.child(it.id).setValue(updatedNote)
                }
                // Navigate back to HomeFragment
                findNavController().navigate(R.id.action_editNoteFragment_to_homeFragment)
            }
        }

        return binding.root
    }
}
