package com.savr.moviedb.viewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.savr.moviedb.ui.DetailMovieActivity;
import com.savr.moviedb.model.ResultsItem;
import com.savr.moviedb.network.ApiCall;
import com.savr.moviedb.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NowPlayingContainerViewHolder extends RecyclerView.ViewHolder {

    private RecyclerView recycler_movie;
    private TextView empty_message;

    public NowPlayingContainerViewHolder(View itemView) {
        super(itemView);
        recycler_movie = itemView.findViewById(R.id.now_playing_recyclerview);
        empty_message = itemView.findViewById(R.id.now_playing_empty_message);
    }

    public void setView(List<ResultsItem> movieListNowPlaying, Activity activity, Context context) {
        if (movieListNowPlaying.isEmpty()){
            empty_message.setText("Tidak ada film yang ditampilkan");
            empty_message.setVisibility(View.VISIBLE);
        }else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);
            recycler_movie.setLayoutManager(layoutManager);
            NowPlayingAdapter adapter = new NowPlayingAdapter(movieListNowPlaying, activity, context);
            recycler_movie.setAdapter(adapter);
        }
    }

    private class NowPlayingViewHolder extends RecyclerView.ViewHolder{

        public NowPlayingViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setView(final ResultsItem resultsItem, final Context context) {
            ((TextView)itemView.findViewById(R.id.tittle)).setText(resultsItem.getTitle());
            ((TextView)itemView.findViewById(R.id.tvReleaseDate)).setText(resultsItem.getReleaseDate());
            ((TextView)itemView.findViewById(R.id.description)).setLines(5 );
            ((TextView)itemView.findViewById(R.id.description)).setText(resultsItem.getOverview());
            Picasso.with(context)
                    .load(ApiCall.IMAGE_URL+resultsItem.getBackdropPath())
                    .into((ImageView) itemView.findViewById(R.id.image_poster));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailMovieActivity.class);
                    intent.putExtra("movie_id", String.valueOf(resultsItem.getId()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    private class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingViewHolder>{
        private List<ResultsItem> movieListNowPlaying;
        private Activity activity;
        private Context context;

        public NowPlayingAdapter(List<ResultsItem> movieListNowPlaying, Activity activity, Context context) {
            this.movieListNowPlaying = movieListNowPlaying;
            this.activity = activity;
            this.context = context;
        }

        @NonNull
        @Override
        public NowPlayingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new NowPlayingViewHolder(activity.getLayoutInflater().inflate(R.layout.item_movie_banner, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull NowPlayingViewHolder holder, int i) {
            holder.setView(movieListNowPlaying.get(i), context);
        }

        @Override
        public int getItemCount() {
            return movieListNowPlaying.size();
        }
    }
}
