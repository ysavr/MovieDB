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

public class PopularContainerViewHolder extends RecyclerView.ViewHolder {

    private RecyclerView recycler_movie;
    private TextView empty_message;

    public PopularContainerViewHolder(@NonNull View itemView) {
        super(itemView);
        recycler_movie = itemView.findViewById(R.id.popular_recyclerview);
        empty_message = itemView.findViewById(R.id.popular_empty_message);
    }

    public void setView(List<ResultsItem> movieListPopular, Activity activity, Context context) {
        if (movieListPopular.isEmpty()){
            empty_message.setText("Tidak ada film yang ditampilkan ");
            empty_message.setVisibility(View.VISIBLE);
        }else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            recycler_movie.setLayoutManager(layoutManager);
            PopularAdapter adapter = new PopularAdapter(movieListPopular, activity, context);
            recycler_movie.setAdapter(adapter);
        }
    }

    private class PopularViewHolder extends RecyclerView.ViewHolder{

        public PopularViewHolder(@NonNull View itemView) {
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
                    Log.d("Popular", "movie_id "+resultsItem.getId());
                }
            });
        }
    }

    private class PopularAdapter extends RecyclerView.Adapter<PopularViewHolder>{

        private List<ResultsItem> movieListPopular;
        private Activity activity;
        private Context context;

        public PopularAdapter(List<ResultsItem> movieListPopular, Activity activity, Context context) {
            this.movieListPopular = movieListPopular;
            this.activity = activity;
            this.context = context;
        }

        @NonNull
        @Override
        public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new PopularViewHolder(activity.getLayoutInflater().inflate(R.layout.item_movie_banner, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PopularViewHolder holder, int i) {
            holder.setView(movieListPopular.get(i),context);
        }

        @Override
        public int getItemCount() {
            return movieListPopular.size();
        }
    }
}
