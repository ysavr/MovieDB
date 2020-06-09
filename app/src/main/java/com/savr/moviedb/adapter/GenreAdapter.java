package com.savr.moviedb.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.savr.moviedb.R;
import com.savr.moviedb.model.GenresItem;
import com.savr.moviedb.view.viewHolder.GenreViewHolder;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreViewHolder>{

    private List<GenresItem> genresItemList;
    private LayoutInflater layoutInflater;

    public GenreAdapter(List<GenresItem> genresItemList, LayoutInflater layoutInflater) {
        this.genresItemList = genresItemList;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GenreViewHolder(layoutInflater.inflate(R.layout.item_genre, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        holder.setView(genresItemList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genresItemList.size();
    }
}
