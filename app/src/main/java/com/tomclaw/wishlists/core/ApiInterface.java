package com.tomclaw.wishlists.core;

import com.tomclaw.wishlists.main.dto.WishItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by solkin on 26.02.17.
 */
public interface ApiInterface {

    @GET("wish_list.php")
    Call<List<WishItem>> getWishList(@Query("user") String user);
}
