package com.savr.moviedb.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.savr.moviedb.adapter.MainAdapter;
import com.savr.moviedb.contract.MainContract;
import com.savr.moviedb.model.MovieResponse;
import com.savr.moviedb.model.ResultsItem;
import com.savr.moviedb.network.ApiCall;
import com.savr.moviedb.network.ApiService;
import com.savr.moviedb.R;
import com.savr.moviedb.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainAdapter adapter;
    private MainContract.Presenter presenter;
    private List<ResultsItem> movieListPopular = new ArrayList<>();
    private List<ResultsItem> movieListNowPlaying = new ArrayList<>();
    private RecyclerView recycler_movie;
    private ProgressBar progressBar;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        recycler_movie = findViewById(R.id.recycler_popular);
        progressBar = findViewById(R.id.progress);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_movie.setLayoutManager(layoutManager);
        recycler_movie.setVisibility(View.GONE);
        adapter = new MainAdapter(movieListPopular, movieListNowPlaying, getApplicationContext(), MainActivity.this, getLayoutInflater());
        recycler_movie.setAdapter(adapter);

        new MainPresenter(this);

    }

    @Override
    public void showLoading(boolean show) {
        if (show) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showList(boolean show) {
        if (show) recycler_movie.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNowPlaying(List<ResultsItem> movieListNowPlayingList) {
        this.movieListNowPlaying = movieListNowPlayingList;
        adapter = new MainAdapter(movieListPopular, movieListNowPlaying, getApplicationContext(), MainActivity.this, getLayoutInflater());
        recycler_movie.setAdapter(adapter);
        adapter.notifyItemChanged(adapter.getItemViewType(0));
    }

    @Override
    public void showPopular(List<ResultsItem> movieListPopularList) {
        this.movieListPopular = movieListPopularList;
        adapter = new MainAdapter(movieListPopular, movieListNowPlaying, getApplicationContext(), MainActivity.this, getLayoutInflater());
        recycler_movie.setAdapter(adapter);
        adapter.notifyItemChanged(adapter.getItemViewType(1));
    }

    @Override
    public void showMessage(String message) {
        Log.d("message", message);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
