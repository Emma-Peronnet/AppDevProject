package com.example.mybankaccounts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateAccount extends AppCompatActivity {
    //Variables
    private Button button3;
    private TextInputLayout textInputId;
    private TextInputLayout textInputAccountName;
    private TextInputLayout textInputAmount;
    private TextInputLayout textInputIban;
    private TextInputLayout textInputCurrency;

    private static final String FILE_NAME = "data_app.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        textInputId = findViewById(R.id.text_input_id);
        textInputAccountName = findViewById(R.id.text_input_account_name);
        textInputAmount = findViewById(R.id.text_input_amount);
        textInputIban = findViewById(R.id.text_input_iban);
        textInputCurrency = findViewById(R.id.text_input_currency);

        //Button to go to ListAccounts activity
        this.button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), ListAccountsActivity.class);
                startActivity(otherActivity);
                finish();
            }
        });
    }

    private boolean validateId(){
        String idInput = textInputId.getEditText().getText().toString().trim();
        if(idInput.isEmpty()){
            textInputId.setError("Field can't be empty");
            return false;
        }else{
            textInputId.setError(null);
            return true;
        }
    }

    private boolean validateAccountName(){
        String accountNameInput = textInputAccountName.getEditText().getText().toString().trim();
        if(accountNameInput.isEmpty()){
            textInputAccountName.setError("Field can't be empty");
            return false;
        }else{
            textInputAccountName.setError(null);
            return true;
        }
    }

    private boolean validateAmount(){
        String amountInput = textInputAmount.getEditText().getText().toString().trim();
        if(amountInput.isEmpty()){
            textInputAmount.setError("Field can't be empty");
            return false;
        }else{
            textInputAmount.setError(null);
            return true;
        }
    }

    private boolean validateIban(){
        String ibanInput = textInputIban.getEditText().getText().toString().trim();
        if(ibanInput.isEmpty()){
            textInputIban.setError("Field can't be empty");
            return false;
        }else{
            textInputIban.setError(null);
            return true;
        }
    }

    private boolean validateCurrency(){
        String currencyInput = textInputCurrency.getEditText().getText().toString().trim();
        if(currencyInput.isEmpty()){
            textInputCurrency.setError("Field can't be empty");
            return false;
        }else{
            textInputCurrency.setError(null);
            return true;
        }
    }

    public void confirmInput(View v){
        if(!validateId() | !validateAccountName() | !validateAmount() | !validateIban() | !validateCurrency()){
            return;
        }else{
            String id = textInputId.getEditText().getText().toString().trim();
            String accountname = textInputAccountName.getEditText().getText().toString().trim();
            String amount = textInputAmount.getEditText().getText().toString().trim();
            String iban = textInputIban.getEditText().getText().toString().trim();
            String currency = textInputCurrency.getEditText().getText().toString().trim();

            Account account = new Account(id, accountname, amount, iban, currency);

            save(v, account);
        }
    }

    public void save(View v, Account account){
        String text="";
        text+= "ID: " + account.getId() + "\n";
        text+= "Account Name : " + account.getAccountName() + "\n";
        text+= "Amount : " + account.getAmount() + "\n";
        text+= "Iban : " + account.getIban() + "\n";
        text+= "Currency : " + account.getCurrency() + "\n \n";

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}