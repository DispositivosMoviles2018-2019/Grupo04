package optativa.com.login2.vista;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import optativa.com.login2.R;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    EditText usuario;
    EditText password;
    Button boton, registro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        usuario = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.pass);
        boton = (Button) findViewById(R.id.btn);
        registro = (Button) findViewById(R.id.btnRegristro);
        boton.setOnClickListener(this);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regis();
            }
        });

    }

    public void regis() {
        Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean comparar(String a, String b) {

        boolean resul = false;

        String usr1 = "gustavo";
        String pass1 = "1234";

        String usr2 = "luis";
        String pass2 = "1234";

        boolean usrA = (usr1.equals(a)) && (pass1.equals(b));
        boolean usrB = (usr2.equals(a)) && (pass2.equals(b));

        if ((usrA == true) || (usrB == true)) {
            resul = true;
        }

        return resul;
    }

    @Override
    public void onClick(View v) {
        String usrComp = usuario.getText().toString();
        String passComp = password.getText().toString();
        boolean resultado = comparar(usrComp, passComp);

        if (resultado == true) {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Credenciales Incorrectas", Toast.LENGTH_SHORT).show();
        }
    }
}
