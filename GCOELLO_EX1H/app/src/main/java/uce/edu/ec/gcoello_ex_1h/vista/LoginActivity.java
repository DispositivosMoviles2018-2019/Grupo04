package uce.edu.ec.gcoello_ex_1h.vista;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import uce.edu.ec.gcoello_ex_1h.R;
import uce.edu.ec.gcoello_ex_1h.modelo.Usuario;

public class LoginActivity extends AppCompatActivity {

    private Usuario usuario1, usuario2, usuario3, usuario4;
    ArrayList<Usuario> listaUsuariosRegistrados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //USUARIOS REGISTRADOS
        usuario1 = new Usuario("luis", "luis");
        usuario2 = new Usuario("ana", "ana");
        usuario3 = new Usuario("juan", "juan");
        usuario4 = new Usuario("admin", "admin");
        usuario4 = new Usuario("gustavo","gustavo");


        listaUsuariosRegistrados = new ArrayList<Usuario>();

        listaUsuariosRegistrados.add(usuario1);
        listaUsuariosRegistrados.add(usuario2);
        listaUsuariosRegistrados.add(usuario3);
        listaUsuariosRegistrados.add(usuario4);
    }

    public void Botonlogin(View view) {
        TextView user, pass;
        boolean userValido = false;
        boolean claveValido = false;

        user = findViewById(R.id.txtUser);
        pass = findViewById(R.id.txtPass);

        for (Usuario usuario:listaUsuariosRegistrados) {
            if(usuario.getUsuario().equals(user.getText().toString())) {
                //Toast.makeText(getBaseContext(), "Usuario no registrado! " + user.getText().toString(), Toast.LENGTH_SHORT).show();
                userValido = true;
            }
            if(usuario.getClave().equals(pass.getText().toString())) {
                //Toast.makeText(getBaseContext(), "Error, clave incorrecta!", Toast.LENGTH_SHORT).show();
                if (userValido)
                    claveValido = true;
                else
                    claveValido = false;
            }
        }

        if(userValido && claveValido) {
            Intent intent = new Intent(getApplicationContext(), ListarActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getBaseContext(), "Error, Usuario no registrado!", Toast.LENGTH_SHORT).show();
        }

    }
}
