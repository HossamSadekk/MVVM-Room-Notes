package com.example.notesapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.Room.Note;

public class NoteAdapter extends ListAdapter<Note,NoteAdapter.NoteHolder> {
//private List<Note> notes = new ArrayList<>();
private onItemClickListener listener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
        
    }
    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            //return true if the items the same
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                     oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.title.setText(currentNote.getTitle());
        holder.priority.setText(currentNote.getPriority()+"");
        holder.description.setText(currentNote.getDescription());
    }

   /* public void setNotes(List<Note> notes)
    {
        this.notes = notes;
        notifyDataSetChanged();
    }*/

    // returns note at specific position
    public Note getNoteAt(int Position)
    {
        return getItem(Position);
    }

  /*  @Override
    public int getItemCount() {
        return notes.size();
    }*/

    class NoteHolder extends RecyclerView.ViewHolder
    {
        private TextView title;
        private TextView priority;
        private TextView description;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            priority = itemView.findViewById(R.id.tv_priority);
            description = itemView.findViewById(R.id.tv_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position!=RecyclerView.NO_POSITION )
                    {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener
    {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(onItemClickListener listener)
    {
        this.listener = listener;
    }
}
