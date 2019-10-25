package com.savr.moviedb.view.viewHolder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.savr.moviedb.R;

public class GenreViewHolder extends RecyclerView.ViewHolder{
    public TextView genreText;
    public GenreViewHolder(@NonNull View itemView) {
        super(itemView);
        genreText = itemView.findViewById(R.id.genre);
    }

    public void setView(String genre) {
        genreText.setText(genre);
    }
}
