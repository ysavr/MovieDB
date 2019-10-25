package com.savr.moviedb.presenter;
import android.util.Log;
import com.savr.moviedb.contract.DetailMovieContract;
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
    private DetailMovieResponse response;

    public DetailMoviePresenter(DetailMovieContract.View detailView, int movie_id) {
        this.detailView = detailView;
        this.movie_id = movie_id;

        detailView.setPresenter(this);
        start();
    }

    @Override
    public void loadMovieDetail() {
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
    public void start() {
        loadMovieDetail();
    }
}
