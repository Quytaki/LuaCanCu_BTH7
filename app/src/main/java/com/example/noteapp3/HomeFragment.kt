package com.example.noteapp3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp3.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private val notesList = mutableListOf<Note>()
    private lateinit var emptyNotesImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        // Initialize RecyclerView and Adapter
        recyclerView = binding.homeRecyclerView
        noteAdapter = NoteAdapter(notesList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = noteAdapter

        // ImageView for empty notes background
        emptyNotesImage = binding.emptyNotesImage

        // Firebase Realtime Database
        val database = FirebaseDatabase.getInstance().getReference("notes")

        // Listen for changes in the database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notesList.clear() // Clear old data
                for (noteSnapshot in snapshot.children) {
                    val note = noteSnapshot.getValue(Note::class.java)
                    note?.let { notesList.add(it) }
                }

                // Notify the adapter that data has changed
                noteAdapter.notifyDataSetChanged()

                // Show or hide the empty state
                if (notesList.isEmpty()) {
                    emptyNotesImage.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    emptyNotesImage.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "loadPost:onCancelled", error.toException())
            }
        })

        // Set up FloatingActionButton to navigate to AddNoteFragment
        binding.addNoteFab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }

        return binding.root
    }
}
