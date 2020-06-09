package com.savr.moviedb.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.savr.moviedb.R;
import com.savr.moviedb.adapter.GenreAdapter;
import com.savr.moviedb.contract.DetailMovieContract;
import com.savr.moviedb.model.DetailMovieResponse;
import com.savr.moviedb.network.ApiCall;
import com.savr.moviedb.pagination.PaginationActivity;
import com.savr.moviedb.presenter.DetailMoviePresenter;
import com.squareup.picasso.Picasso;

public class DetailMovieActivity extends AppCompatActivity implements DetailMovieContract.View {

    private String TAG = "DetailMovieActivity";
    private int movie_id;
    private TextView tittle, deskripsi, releaseDate, rating, durasi, language;
    private ImageView poster;
    private FloatingActionButton buttonFavorite;
    private RecyclerView recyclerGenre;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CoordinatorLayout parenLayout;
    private GenreAdapter adapter;
    private DetailMovieContract.Presenter presenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        movie_id = Integer.parseInt(getIntent().getStringExtra("movie_id"));
        parenLayout = findViewById(R.id.parent_layout);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        tittle = findViewById(R.id.detail_movie_tittle);
        deskripsi = findViewById(R.id.detail_movie_deskripsi);
        poster = findViewById(R.id.detail_movie_image);
        buttonFavorite = findViewById(R.id.btn_favorite);
        releaseDate = findViewById(R.id.detail_movie_releaseDate);
        rating = findViewById(R.id.rating);
        durasi = findViewById(R.id.detail_movie_durasi);
        language = findViewById(R.id.detail_movie_language);
        recyclerGenre = findViewById(R.id.recyler_genre);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerGenre.setLayoutManager(layoutManager);
        progressDialog = new ProgressDialog(DetailMovieActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        handleClick();

        new DetailMoviePresenter(this, movie_id, getApplicationContext());
    }

    private void handleClick() {
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.favorite(movie_id);
            }
        });
    }

    @Override
    public void showLoading(boolean show) {
        if (show) {
            progressDialog.show();
            parenLayout.setVisibility(View.INVISIBLE);
        }
        else {
            progressDialog.dismiss();
            parenLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMovieDetail(DetailMovieResponse detailMovieResponse) {
        tittle.setText(detailMovieResponse.getTagline());
        deskripsi.setText(detailMovieResponse.getOverview());
        releaseDate.setText(detailMovieResponse.getReleaseDate());
        rating.setText(String.valueOf(detailMovieResponse.getVoteAverage()));
        durasi.setText(detailMovieResponse.getRuntime() +" menit");
        language.setText(detailMovieResponse.getOriginalLanguage());
        Picasso.with(getApplicationContext())
                .load(ApiCall.IMAGE_URL+detailMovieResponse.getBackdropPath())
                .into(poster);
        collapsingToolbarLayout.setTitle(detailMovieResponse.getTitle());
        adapter = new GenreAdapter(detailMovieResponse.getGenres(), getLayoutInflater());
        recyclerGenre.setAdapter(adapter);
        recyclerGenre.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFavorite(boolean isFavorite) {
        if (isFavorite) buttonFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        else buttonFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
    }

    @Override
    public void showMessage(String message) {
        Log.d(TAG, message);
    }

    @Override
    public void setPresenter(DetailMovieContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
