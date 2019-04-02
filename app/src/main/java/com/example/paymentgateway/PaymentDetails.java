package com.example.paymentgateway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {

    TextView txtId,txtStatus,txtAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_payment_details );

        txtId = (TextView)findViewById( R.id.txtId);
        txtStatus = (TextView)findViewById( R.id.txtAmount );
        txtAmount = (TextView)findViewById( R.id.txtStatus );



        // Intent got krenge

        Intent intent = getIntent();
        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra( "PaymentDetails" ) );
            showDetails(jsonObject.getJSONObject( "responce" ),intent.getStringExtra( "paymentAmount" ));

        }catch (JSONException e){
            e.printStackTrace();

        }


    }

    private void showDetails(JSONObject responce, String payment_amount) {

            try {
                txtId.setText( responce.getString( "id" ) );
                txtAmount.setText( responce.getString( "state" ) );
                txtStatus.setText( responce.getString("$"+payment_amount ) );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

