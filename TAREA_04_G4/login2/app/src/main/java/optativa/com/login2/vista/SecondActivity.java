package optativa.com.login2.vista;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import optativa.com.login2.R;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        boton = (Button) findViewById(R.id.btn);
        boton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
