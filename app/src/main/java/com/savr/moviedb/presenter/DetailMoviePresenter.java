package com.savr.moviedb.presenter;
import android.content.Context;
import android.util.Log;
import com.savr.moviedb.contract.DetailMovieContract;
import com.savr.moviedb.databases.Database;
import com.savr.moviedb.model.DetailMovieResponse;
import com.savr.moviedb.network.ApiCall;
import com.savr.moviedb.network.ApiService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailMoviePresenter implements DetailMovieContract.Presenter {
    
    private DetailMovieContract.View detailView;
    private ApiService apiService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int movie_id;
    private boolean isFavorite;
    private DetailMovieResponse response;
    private Database localDB;

    public DetailMoviePresenter(DetailMovieContract.View detailView, int movie_id, Context context) {
        this.detailView = detailView;
        this.movie_id = movie_id;
        localDB = new Database(context);

        detailView.setPresenter(this);
        start();
    }

    @Override
    public void loadMovieDetail() {
        detailView.showLoading(true);
        apiService = ApiCall.getClient().create(ApiService.class);
        compositeDisposable.add(apiService.getDetailMovie(movie_id, ApiCall.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DetailMovieResponse>() {
                    @Override
                    public void accept(DetailMovieResponse detailMovieResponse) throws Exception {
                        if (detailMovieResponse != null){
                            response = detailMovieResponse;
                            detailView.showMovieDetail(response);
                            showFavorite(movie_id);
                            detailView.showLoading(false);
                        }else {
                            detailView.showMessage("Response null");
                            detailView.showLoading(false);
                            Log.d("error","null");
                        }
                    }
                }));
    }

    @Override
    public void favorite(int id) {
        getFavorite(id);
    }

    private void addFavorite(int id) {
        localDB.addFavorites(String.valueOf(id));
        detailView.showFavorite(true);
    }

    private void removeFavorite(int id) {
        localDB.removeFavorites(String.valueOf(id));
        detailView.showFavorite(false);
    }

    private void getFavorite(int movie_id) {
        isFavorite = localDB.isFavorites(String.valueOf(movie_id));
        if (!isFavorite) {
            addFavorite(movie_id);
        }
        else {
            removeFavorite(movie_id);
        }
    }

    private void showFavorite(int id) {
        isFavorite = localDB.isFavorites(String.valueOf(movie_id));
        if (isFavorite){
            detailView.showFavorite(true);
        }
        else {
            detailView.showFavorite(false);
        }
    }

    @Override
    public void start() {
        loadMovieDetail();
    }
}
