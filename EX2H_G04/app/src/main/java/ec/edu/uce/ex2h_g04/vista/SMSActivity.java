package ec.edu.uce.ex2h_g04.vista;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ec.edu.uce.ex2h_g04.R;

public class SMSActivity extends AppCompatActivity {

    private String RECIPIENT_ADDRESS = "";
    private static final String ACTION_SENT =
            "uce.edu.ec.tarea_final_2h_clargo.SENT";
    private static final String ACTION_DELIVERED =
            "uce.edu.ec.tarea_final_2h_clargo.DELIVERED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);


        EditText numero = (EditText) findViewById(R.id.txt_numero);
        String num = getIntent().getStringExtra("cel");
        numero.setText(num);


        Button sendButton = (Button) findViewById(R.id.btn_enviar);
        //Button sendButton = new Button(this);
        //sendButton.setText("Enviar mensaje SMS");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkSMSStatePermission();
                    String data = getIntent().getStringExtra("data");
                    sendSMS(data);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Servicio de mensajeria no disponible, verifique permisos de SMS!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSMSStatePermission();
        //Monitor status of the operations
        registerReceiver(sent, new IntentFilter(ACTION_SENT));
        registerReceiver(delivered, new IntentFilter(ACTION_DELIVERED));
    }

    private void sendSMS(String message) {
        PendingIntent sIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(ACTION_SENT), 0);
        PendingIntent dIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(ACTION_DELIVERED), 0);
        //Send the message
        SmsManager manager = SmsManager.getDefault();


        EditText numero = (EditText) findViewById(R.id.txt_numero);
        RECIPIENT_ADDRESS = numero.getText().toString();
        Log.e("SMS", RECIPIENT_ADDRESS);

        manager.sendTextMessage(RECIPIENT_ADDRESS, null, message, sIntent, dIntent);
    }

    private void checkSMSStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "No se tiene permiso para enviar SMS.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para enviar SMS!");
            Toast.makeText(getApplicationContext(), "Si tiene permiso para enviar SMS.", Toast.LENGTH_LONG).show();
        }
    }


    private BroadcastReceiver sent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    //Handle sent success
                    Toast.makeText(getApplicationContext(), "Envio correcto", Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(getApplicationContext(), "Error enviando. RESULT_ERROR_GENERIC_FAILURE", Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(getApplicationContext(), "Error enviando. RESULT_ERROR_NO_SERVICE", Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(getApplicationContext(), "Error enviando. RESULT_ERROR_NULL_PDU", Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    //Handle sent error
                    Toast.makeText(getApplicationContext(), "Error enviando. RESULT_ERROR_RADIO_OFF", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private BroadcastReceiver delivered = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    //Handle delivery success
                    Toast.makeText(getApplicationContext(), "Mensaje entregado OK!", Toast.LENGTH_LONG).show();
                    break;
                case Activity.RESULT_CANCELED:
                    //Handle delivery failure
                    Toast.makeText(getApplicationContext(), "Error mensaje no entregado!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
