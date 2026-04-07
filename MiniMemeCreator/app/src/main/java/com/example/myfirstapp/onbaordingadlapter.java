package com.example.myfirstapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class onbaordingadlapter extends RecyclerView.Adapter<onbaordingadlapter.ViewHolder> {

    String[] emojis  = {"🎭", "🖼️", "⬆️"};
    String[] titles  = {"Create Memes", "Default Memes", "Upload & Share"};
    String[] descs   = {
            "Create hilarious memes right from your phone!\nReady in just a few seconds.",
            "Use our built-in default meme templates.\nBest trending memes already available!",
            "Upload your own photo, add text\nand save directly to your gallery!"
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemonboarding, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.emoji.setText(emojis[position]);
        holder.title.setText(titles[position]);
        holder.desc.setText(descs[position]);
    }

    @Override
    public int getItemCount() { return 3; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView emoji, title, desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emoji = itemView.findViewById(R.id.txtEmoji);
            title = itemView.findViewById(R.id.txtTitle);
            desc  = itemView.findViewById(R.id.txtDesc);
        }
    }
}