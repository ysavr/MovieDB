package com.savr.moviedb.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.savr.moviedb.R;
import com.savr.moviedb.model.DetailMovieResponse;
import com.savr.moviedb.model.GenresItem;
import com.savr.moviedb.network.ApiCall;
import com.savr.moviedb.network.ApiService;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailMovieActivity extends AppCompatActivity {

    private String TAG = "DetailMovieActivity";
    private int movie_id;
    private TextView tittle, deskripsi, releaseDate, rating, durasi, language;
    private ImageView poster;
    private RecyclerView recyclerGenre;
    private ProgressBar progress;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private GenreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        movie_id = Integer.parseInt(getIntent().getStringExtra("movie_id"));
        progress = findViewById(R.id.progress);
        tittle = findViewById(R.id.detail_movie_tittle);
        deskripsi = findViewById(R.id.detail_movie_deskripsi);
        poster = findViewById(R.id.detail_movie_image);
        releaseDate = findViewById(R.id.detail_movie_releaseDate);
        rating = findViewById(R.id.rating);
        durasi = findViewById(R.id.detail_movie_durasi);
        language = findViewById(R.id.detail_movie_language);
        recyclerGenre = findViewById(R.id.recyler_genre);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerGenre.setLayoutManager(layoutManager);

        loadData();
    }

    private void loadData() {
        ApiService apiService = ApiCall.getClient().create(ApiService.class);

        compositeDisposable.add(apiService.getDetailMovie(movie_id,ApiCall.API_KEY)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<DetailMovieResponse>() {
            @Override
            public void accept(DetailMovieResponse detailMovieResponse) throws Exception {
                if (detailMovieResponse != null){
                    tittle.setText(detailMovieResponse.getTitle());
                    deskripsi.setText(detailMovieResponse.getOverview());
                    releaseDate.setText(detailMovieResponse.getReleaseDate());
                    rating.setText(String.valueOf(detailMovieResponse.getVoteAverage()));
                    durasi.setText(String.valueOf(detailMovieResponse.getRuntime())+" menit");
                    language.setText(detailMovieResponse.getOriginalLanguage());

                    Picasso.with(getApplicationContext())
                            .load(ApiCall.IMAGE_URL+detailMovieResponse.getBackdropPath())
                            .into(poster);

                    adapter = new GenreAdapter(detailMovieResponse.getGenres(),getApplicationContext());
                    recyclerGenre.setAdapter(adapter);
                    recyclerGenre.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }else {
                    Log.e("Error","Response null");
                }
            }
        }));
    }

    private class GenreViewHolder extends RecyclerView.ViewHolder{
        private TextView genre;
        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            genre = itemView.findViewById(R.id.genre);
        }
    }

    private class GenreAdapter extends RecyclerView.Adapter<GenreViewHolder>{

        private List<GenresItem> genresItemList;
        private Context context;

        public GenreAdapter(List<GenresItem> genresItemList, Context context) {
            this.genresItemList = genresItemList;
            this.context = context;
        }

        @NonNull
        @Override
        public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new GenreViewHolder(getLayoutInflater().inflate(R.layout.item_genre, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull GenreViewHolder holder, int i) {
            holder.genre.setText(genresItemList.get(i).getName());
        }

        @Override
        public int getItemCount() {
            return genresItemList.size();
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
