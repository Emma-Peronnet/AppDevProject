package com.example.mybankaccounts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedFile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListAccountsActivity extends AppCompatActivity {
    //Variables
    private Button button2;
    private TextView textViewAccounts;
    private MockApiAccounts mockApiAccounts;

    private static final String FILE_NAME = "data_app.txt";

    private boolean isConnected = true; //If on Nougat or higher, set this to false
    ConnectivityManager connectivityManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_accounts);

        textViewAccounts = findViewById(R.id.text_view_accounts);

        //TLS
        OkHttpClient client = new OkHttpClient();
        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(new TLSSocketFactory())
                    .build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("https://60102f166c21e10017050128.mockapi.io/labbbank/")
                .baseUrl(BuildConfig.Base_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mockApiAccounts = retrofit.create(MockApiAccounts.class);

        //Functions
        getAccounts();

        //Button to go the UpdateAccount activity
        this.button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), UpdateAccount.class);
                startActivity(otherActivity);
                finish();
            }
        });

    }

    private void getAccounts(){
        //Get the accounts from the API if the internet connexion is on (only works for Nougat and higher)
        if(isConnected){
            Toast.makeText(this,"Online", Toast.LENGTH_LONG).show();

            Call<List<Account>> call = mockApiAccounts.getAccounts();
            call.enqueue(new Callback<List<Account>>() {
                @Override
                public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                    if(!response.isSuccessful()){
                        textViewAccounts.setText("Code : "+ response.code());
                        return;
                    }

                    List<Account> accounts = response.body();
                    for(Account account : accounts){
                        String content="";
                        content+= "ID: " + account.getId() + "\n";
                        content+= "Account Name : " + account.getAccountName() + "\n";
                        content+= "Amount : " + account.getAmount() + "\n";
                        content+= "Iban : " + account.getIban() + "\n";
                        content+= "Currency : " + account.getCurrency() + "\n \n";

                        textViewAccounts.append(content);
                    }
                }

                @Override
                public void onFailure(Call<List<Account>> call, Throwable t) {
                    textViewAccounts.setText(t.getMessage());
                }
            });

        }else{
            Toast.makeText(this,"Offline", Toast.LENGTH_LONG).show();
        }
    }

   public void load(View v){
       FileInputStream fis = null;

       try {
           fis = openFileInput(FILE_NAME);
           InputStreamReader isr = new InputStreamReader(fis);
           BufferedReader br = new BufferedReader(isr);
           StringBuilder sb = new StringBuilder();
           String text;

           while((text = br.readLine()) != null){
                sb.append(text).append("\n");
               textViewAccounts.append(text);
           }

       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           if(fis!= null){
               try {
                   fis.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
   }

    private void registerNetworkCallback(){
        try {
            connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){
                @Override
                public void onAvailable(@NonNull Network network) {
                    isConnected = true;
                }

                @Override
                public void onLost(@NonNull Network network) {
                    isConnected = false;
                }
            });
        }catch (Exception e){
            isConnected = false;
        }
    }


    private void unregisterNetworkCallback(){
        connectivityManager.unregisterNetworkCallback(new ConnectivityManager.NetworkCallback());
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkCallback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterNetworkCallback();
    }
}