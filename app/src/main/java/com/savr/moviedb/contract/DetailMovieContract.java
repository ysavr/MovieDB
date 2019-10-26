package com.savr.moviedb.contract;
import com.savr.moviedb.model.DetailMovieResponse;
import com.savr.moviedb.utils.BasePresenter;
import com.savr.moviedb.utils.BaseView;

public interface DetailMovieContract {

    interface View extends BaseView<DetailMovieContract.Presenter> {

        void showLoading(boolean show);

        void showMovieDetail(DetailMovieResponse detailMovieResponse);

        void showFavorite(boolean isFavorite);

        void showMessage(String message);
    }

    interface Presenter extends BasePresenter {

        void loadMovieDetail();

        void favorite(int id);

    }
}
