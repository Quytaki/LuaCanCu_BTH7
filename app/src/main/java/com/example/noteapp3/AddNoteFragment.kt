package com.example.noteapp3

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.noteapp3.databinding.FragmentAddNoteBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNoteFragment : Fragment() {

    private lateinit var addNoteTitle: EditText
    private lateinit var addNoteDesc: EditText
    private lateinit var saveFab: FloatingActionButton  // Khai báo FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddNoteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_note, container, false
        )

        addNoteTitle = binding.addNoteTitle
        addNoteDesc = binding.addNoteDesc
        saveFab = binding.saveFab // Gán FloatingActionButton vào biến saveFab

        // Firebase Database reference
        val database = FirebaseDatabase.getInstance().getReference("notes")

        saveFab.setOnClickListener {
            val title = addNoteTitle.text.toString()
            val description = addNoteDesc.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val noteId = database.push().key ?: return@setOnClickListener
                val note = Note(noteId, title, description)
                database.child(noteId).setValue(note)

                // Navigate back to HomeFragment
                findNavController().navigate(R.id.action_addNoteFragment_to_homeFragment)
            }
        }

        return binding.root
    }
}

