package com.savr.moviedb;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.savr.moviedb.Model.MovieResponse;
import com.savr.moviedb.Model.ResultsItem;
import com.savr.moviedb.Network.ApiCall;
import com.savr.moviedb.Network.ApiService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler_popular;
    private PopularAdapter adapter;
    private ArrayList<ResultsItem> movieResponseArrayList;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recycler_popular = findViewById(R.id.recycler_popular);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_popular.setLayoutManager(layoutManager);

        loadData();

    }

    private void loadData() {
        if (ApiCall.API_KEY.isEmpty()) {
            Toast.makeText(this, "API KEY tidak terdaftar", Toast.LENGTH_SHORT).show();
        }else {
            ApiService apiService = ApiCall.getClient().create(ApiService.class);

//            Call<MovieResponse> call = apiService.getPopularMovie(ApiCall.API_KEY);
//            call.enqueue(new Callback<MovieResponse>() {
//                @Override
//                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                    List<ResultsItem> movieList = response.body().getResults();
//                    adapter = new PopularAdapter(movieList, getApplicationContext());
//                    recycler_popular.setAdapter(adapter);
//                }
//
//                @Override
//                public void onFailure(Call<MovieResponse> call, Throwable t) {
//
//                }
//            });

            // Menggunakan RxJava
            compositeDisposable.add(apiService.getPopular(ApiCall.API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<MovieResponse>() {
                @Override
                public void accept(MovieResponse movieResponse) throws Exception {
                    if (movieResponse != null){
                        List<ResultsItem> movieList = movieResponse.getResults();
                        adapter = new PopularAdapter(movieList, getApplicationContext());
                        recycler_popular.setAdapter(adapter);
                    }else {
                        Log.e("Error","Response null");
                    }
                }
            }));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private class PopularViewHolder extends RecyclerView.ViewHolder{

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setView(ResultsItem resultsItem) {
            ((TextView)itemView.findViewById(R.id.tittle)).setText(resultsItem.getTitle());
            ((TextView)itemView.findViewById(R.id.tvReleaseDate)).setText(resultsItem.getReleaseDate());
            ((TextView)itemView.findViewById(R.id.description)).setLines(5 );
            ((TextView)itemView.findViewById(R.id.description)).setText(resultsItem.getOverview());
            Picasso.with(getApplicationContext())
                    .load(ApiCall.IMAGE_URL+resultsItem.getBackdropPath())
                    .into((ImageView) itemView.findViewById(R.id.image_poster));
        }
    }

    private class PopularAdapter extends RecyclerView.Adapter<PopularViewHolder>{

        private List<ResultsItem> movieList;
        private Context context;

        public PopularAdapter(List<ResultsItem> movies, Context context) {
            this.movieList = movies;
            this.context = context;
        }

        @NonNull
        @Override
        public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.item_popular, viewGroup, false);
            return new PopularViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PopularViewHolder holder, int i) {
            holder.setView(movieList.get(i));
        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }
    }

}
