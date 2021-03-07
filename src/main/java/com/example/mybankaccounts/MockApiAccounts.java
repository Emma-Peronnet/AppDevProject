package com.example.mybankaccounts;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MockApiAccounts {
    @GET("accounts")
    Call<List<Account>> getAccounts();
}
