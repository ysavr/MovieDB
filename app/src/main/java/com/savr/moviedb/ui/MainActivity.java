package com.savr.moviedb.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.savr.moviedb.model.MovieResponse;
import com.savr.moviedb.model.ResultsItem;
import com.savr.moviedb.network.ApiCall;
import com.savr.moviedb.network.ApiService;
import com.savr.moviedb.R;
import com.savr.moviedb.viewHolder.NowPlayingContainerViewHolder;
import com.savr.moviedb.viewHolder.PopularContainerViewHolder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private BerandaAdapter adapter;
    private List<ResultsItem> movieListPopular = new ArrayList<>();
    private List<ResultsItem> movieListNowPlaying = new ArrayList<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        RecyclerView recycler_movie = findViewById(R.id.recycler_popular);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_movie.setLayoutManager(layoutManager);
        adapter = new BerandaAdapter(getApplicationContext(), getLayoutInflater());
        recycler_movie.setAdapter(adapter);
        loadData();

    }

    private void loadData() {
        ApiService apiService = ApiCall.getClient().create(ApiService.class);

//            Call<MovieResponse> call = apiService.getPopularMovie(ApiCall.API_KEY);
////            call.enqueue(new Callback<MovieResponse>() {
////                @Override
////                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
////                    List<ResultsItem> movieList = response.body().getResults();
////                    adapter = new PopularAdapter(movieList, getApplicationContext());
////                    recycler_popular.setAdapter(adapter);
////                }
////
////                @Override
////                public void onFailure(Call<MovieResponse> call, Throwable t) {
////
////                }
////            });

        // Menggunakan RxJava
        //Popular Movie
        compositeDisposable.add(apiService.getPopular(ApiCall.API_KEY)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<MovieResponse>() {
            @Override
            public void accept(MovieResponse movieResponse) throws Exception {
                if (movieResponse != null){
                    movieListPopular = movieResponse.getResults();
                    adapter.notifyItemChanged(BerandaAdapter.VIEWHOLDER_POPULAR);
                }else {
                    Log.e("Error","Response null");
                }
            }
        }));

        //Now Playing Movie
        compositeDisposable.add(apiService.getNowPlaying(ApiCall.API_KEY)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<MovieResponse>() {
            @Override
            public void accept(MovieResponse movieResponse) throws Exception {
                if (movieResponse != null){
                    movieListNowPlaying = movieResponse.getResults();
                    adapter.notifyItemChanged(BerandaAdapter.VIEWHOLDER_NOW_PAYING);
                }else {
                    Log.e("Error","Response null");
                }
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public class BerandaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int VIEWHOLDER_NOW_PAYING = 0;
        private static final int VIEWHOLDER_POPULAR = 1;

        private Context context;
        private LayoutInflater layoutInflater;

        private BerandaAdapter(Context context, LayoutInflater layoutInflater) {
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
                ((NowPlayingContainerViewHolder) holder).setView(movieListNowPlaying, MainActivity.this, context);
            }else if (holder instanceof PopularContainerViewHolder){
                ((PopularContainerViewHolder) holder).setView(movieListPopular, MainActivity.this, context);
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}
