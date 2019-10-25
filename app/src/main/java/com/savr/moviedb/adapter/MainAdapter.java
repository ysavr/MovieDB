package com.savr.moviedb.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.savr.moviedb.R;
import com.savr.moviedb.model.ResultsItem;
import com.savr.moviedb.ui.MainActivity;
import com.savr.moviedb.viewHolder.NowPlayingContainerViewHolder;
import com.savr.moviedb.viewHolder.PopularContainerViewHolder;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEWHOLDER_NOW_PAYING = 0;
    private static final int VIEWHOLDER_POPULAR = 1;

    private Activity activity;
    private Context context;
    private List<ResultsItem> movieListNowPlayingList;
    private List<ResultsItem> movieListPopularList;
    private LayoutInflater layoutInflater;

    public MainAdapter(List<ResultsItem> movieListNowPlayingList, List<ResultsItem> movieListPopularList, Context context, Activity activity,LayoutInflater layoutInflater){
        this.activity = activity;
        this.movieListNowPlayingList = movieListNowPlayingList;
        this.movieListPopularList = movieListPopularList;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        switch (position){
            case VIEWHOLDER_NOW_PAYING:
                return new NowPlayingContainerViewHolder(layoutInflater.inflate(R.layout.item_container_now_playing,parent,false));
            case VIEWHOLDER_POPULAR:
                return new PopularContainerViewHolder(layoutInflater.inflate(R.layout.item_container_popular,parent,false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NowPlayingContainerViewHolder){
            ((NowPlayingContainerViewHolder) holder).setView(movieListNowPlayingList, activity, context);
        }else if (holder instanceof PopularContainerViewHolder){
            ((PopularContainerViewHolder) holder).setView(movieListPopularList, activity, context);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
