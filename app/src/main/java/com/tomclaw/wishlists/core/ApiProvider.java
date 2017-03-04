package com.tomclaw.wishlists.core;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by solkin on 04.03.17.
 */
@EBean(scope = EBean.Scope.Singleton)
public class ApiProvider {

    private ApiInterface api;

    @AfterInject
    void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://zibuhoker.ru/wish/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiInterface.class);
    }

    public ApiInterface getApi() {
        return api;
    }
}
