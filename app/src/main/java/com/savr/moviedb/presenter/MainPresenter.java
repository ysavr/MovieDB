package com.savr.moviedb.presenter;

import android.util.Log;

import com.savr.moviedb.adapter.MainAdapter;
import com.savr.moviedb.contract.MainContract;
import com.savr.moviedb.model.MovieResponse;
import com.savr.moviedb.model.ResultsItem;
import com.savr.moviedb.network.ApiCall;
import com.savr.moviedb.network.ApiService;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mainView;
    private ApiService apiService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<ResultsItem> movieListPopular = new ArrayList<>();
    private List<ResultsItem> movieListNowPlaying = new ArrayList<>();

    public MainPresenter(MainContract.View mainView) {
        this.mainView = mainView;

        mainView.setPresenter(this);
        start();
    }

    @Override
    public void loadNowPlaying() {
        apiService = ApiCall.getClient().create(ApiService.class);
        compositeDisposable.add(apiService.getNowPlaying(ApiCall.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Exception {
                        if (movieResponse != null){
                            movieListNowPlaying = movieResponse.getResults();
                            mainView.showNowPlaying(movieListNowPlaying);
                            mainView.showLoading(false);
                            mainView.showList(true);
                        }else {
                            mainView.showMessage("Response null");
                            mainView.showLoading(false);
                            mainView.showList(true);
                        }
                    }
                }));

    }

    @Override
    public void loadPopular() {
        apiService = ApiCall.getClient().create(ApiService.class);
        compositeDisposable.add(apiService.getPopular(ApiCall.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Exception {
                        if (movieResponse != null){
                            movieListPopular = movieResponse.getResults();
                            mainView.showPopular(movieListPopular);
                            mainView.showLoading(false);
                            mainView.showList(true);
                        }else {
                            mainView.showMessage("Response null");
                            mainView.showLoading(false);
                            mainView.showList(true);
                        }
                    }
                }));
    }

    @Override
    public void start() {
        loadNowPlaying();
        loadPopular();
    }
}
