package com.example.paymentgateway;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.audiofx.DynamicsProcessing;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paymentgateway.config.config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config  = new PayPalConfiguration()
            .environment( PayPalConfiguration.ENVIRONMENT_SANDBOX )  //sandbox ko use krege coz it will be test//
            .clientId( com.example.paymentgateway.config.config.PAYPAL_CLIENT_ID);


    Button PAYMENT_BTN;
    EditText AMOUNT;
    String amount="";

    @Override
    protected void onDestroy() {
        stopService( new Intent( this,PayPalService.class ) );
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //Yaha se paypal service start hoga//
Intent intent = new Intent( this,PaymentActivity.class );
intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config );
startService( intent );

        PAYMENT_BTN= (Button)findViewById( R.id.btn_Payment );
        AMOUNT = (EditText)findViewById( R.id.Edit_Amount );


        PAYMENT_BTN.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessPayment();
            }
        } );
    }

    private void ProcessPayment() {
        amount = AMOUNT.getText().toString();
        PayPalPayment PayPalPayment = new PayPalPayment(new BigDecimal( String.valueOf( amount ) ),"USD",
                "Donate for Mobiloitte", com.paypal.android.sdk.payments.PayPalPayment.PAYMENT_INTENT_SALE );
        Intent intent = new Intent( this, PaymentActivity.class );
        intent.putExtra( PayPalService.EXTRA_PAYPAL_CONFIGURATION,config );
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,PayPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE  );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE)
        {
            if (requestCode == RESULT_OK)
            {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION );
                if (confirmation !=null)
                {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString( 4 );
                        startActivity( new Intent( this, PaymentDetails.class )
                                     .putExtra( "PaymentDetails",paymentDetails )
                                .putExtra("paymentAmount",amount)
                              );
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            }
            else if (requestCode == Activity.RESULT_CANCELED)
                Toast.makeText( this,"cancel",Toast.LENGTH_SHORT).show();
        }else
            if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID)
                Toast.makeText( this ,"invalid",Toast.LENGTH_SHORT).show();
    }
}
