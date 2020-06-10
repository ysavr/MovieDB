package com.savr.moviedb.view.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.savr.moviedb.adapter.MainAdapter;
import com.savr.moviedb.contract.MainContract;
import com.savr.moviedb.model.ResultsItem;
import com.savr.moviedb.R;
import com.savr.moviedb.pagination.PaginationActivity;
import com.savr.moviedb.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_others:
                startActivity(new Intent(this, PaginationActivity.class));
                break;
            case R.id.menu_room:
                startActivity(new Intent(this, RoomActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
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
