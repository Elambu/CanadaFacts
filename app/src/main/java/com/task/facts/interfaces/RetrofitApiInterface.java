package com.task.facts.interfaces;

import com.task.facts.model.FactsResponse;

import retrofit2.Call;
import retrofit2.http.GET;



public interface RetrofitApiInterface {

    @GET("facts.json")
    public Call<FactsResponse> getFactResponse();
}
