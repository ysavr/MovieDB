package com.savr.moviedb.contract;
import com.savr.moviedb.model.DetailMovieResponse;
import com.savr.moviedb.utils.BasePresenter;
import com.savr.moviedb.utils.BaseView;

public interface DetailMovieContract {

    interface View extends BaseView<DetailMovieContract.Presenter> {

        void showLoading(boolean show);

        void showMovieDetail(DetailMovieResponse detailMovieResponse);

        void showMessage(String message);
    }

    interface Presenter extends BasePresenter {

        void loadMovieDetail();

    }
}
