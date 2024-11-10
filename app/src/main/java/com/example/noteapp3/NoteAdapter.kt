package com.example.noteapp3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp3.databinding.NoteLayoutBinding

class NoteAdapter(private val notesList: MutableList<Note>) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val originalNotesList: List<Note> = notesList.toList() // Lưu bản sao gốc của danh sách

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.binding.noteTitle.text = note.title
        holder.binding.noteDesc.text = note.description
    }

    override fun getItemCount(): Int = notesList.size

    class NoteViewHolder(val binding: NoteLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    // Hàm filter để lọc ghi chú
    fun filterNotes(query: String?) {
        notesList.clear()
        if (query.isNullOrEmpty()) {
            notesList.addAll(originalNotesList)  // Nếu không có từ khóa tìm kiếm, hiển thị tất cả
        } else {
            val filteredList = originalNotesList.filter {
                it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
            }
            notesList.addAll(filteredList)
        }
        notifyDataSetChanged()
    }
}
