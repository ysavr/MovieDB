package com.savr.moviedb.Network;

import com.savr.moviedb.Model.MovieResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovie(@Query("api_key") String api_key);

    @GET("movie/popular")
    Observable<MovieResponse> getPopular(@Query("api_key") String api_key);

}
