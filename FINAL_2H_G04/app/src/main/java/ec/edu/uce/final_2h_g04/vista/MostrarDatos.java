package ec.edu.uce.final_2h_g04.vista;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ec.edu.uce.final_2h_g04.R;

public class MostrarDatos extends AppCompatActivity {

    TextView titulo;
    Button boton;
    EditText data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_datos);

        titulo = (TextView) findViewById(R.id.txt_titulo);
        boton = (Button) findViewById(R.id.btn_ok_data);
        data = (EditText) findViewById(R.id.txt_data);

        String text_titulo=getIntent().getStringExtra("titulo");
        String text_data=getIntent().getStringExtra("data");

        titulo.setText(text_titulo);
        data.setText(text_data);
    }

    public void botonOK(View view) {
        finish();
    }
}
