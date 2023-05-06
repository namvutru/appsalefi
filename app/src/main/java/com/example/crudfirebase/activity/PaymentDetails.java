package com.example.crudfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.crudfirebase.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {
    TextView textId,textAmount,textStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        textId= findViewById(R.id.textId);
        textAmount = findViewById(R.id.textAmount);
        textStatus= findViewById(R.id.textStatus);

        Intent intent = getIntent();

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void showDetails(JSONObject response,String paymentamount){
        try {
            textId.setText(response.getString("id"));
            textStatus.setText(response.getString("state"));
            textAmount.setText(response.getString("$"+paymentamount));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}