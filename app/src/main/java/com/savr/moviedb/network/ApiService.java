package com.savr.moviedb.network;

import com.savr.moviedb.model.DetailMovieResponse;
import com.savr.moviedb.model.MovieResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovie(@Query("api_key") String api_key);

    @GET("movie/popular")
    Observable<MovieResponse> getPopular(@Query("api_key") String api_key);

    @GET("movie/now_playing")
    Observable<MovieResponse> getNowPlaying(@Query("api_key") String api_key);

    @GET("movie/{movie_id}")
    Observable<DetailMovieResponse> getDetailMovie(@Path("movie_id") int movie_id, @Query("api_key") String api_key);
}
