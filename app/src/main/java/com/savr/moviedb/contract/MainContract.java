package com.savr.moviedb.contract;
import com.savr.moviedb.model.ResultsItem;
import com.savr.moviedb.utils.BasePresenter;
import com.savr.moviedb.utils.BaseView;
import java.util.List;

public interface MainContract {
    interface View extends BaseView<Presenter>{

        void showLoading(boolean show);

        void showList(boolean show);

        void showNowPlaying(List<ResultsItem> movieListNowPlayingList);

        void showPopular(List<ResultsItem> movieListPopularList);

        void showMessage(String message);
    }

    interface Presenter extends BasePresenter {

        void loadNowPlaying();

        void loadPopular();

    }
}
