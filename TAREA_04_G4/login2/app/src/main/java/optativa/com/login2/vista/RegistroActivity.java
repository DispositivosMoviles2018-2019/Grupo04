package optativa.com.login2.vista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import optativa.com.login2.R;

import java.io.*;

import android.content.Context;

import optativa.com.login2.entities.Personas;

public class RegistroActivity extends AppCompatActivity {

    private EditText usr;
    private EditText pass;
    private EditText nombre;
    private EditText apellido;
    private EditText email;
    private EditText cel;
    private RadioGroup genero;
    private RadioButton masculino;
    private RadioButton femenino;
    private DatePicker fecha;
    private CheckBox optativa;
    private CheckBox progra;
    private CheckBox redes;
    private CheckBox analisis;
    private CheckBox protocolos;
    private Switch beca;
    private Button registro;
    public String user = "";
    List<Personas> users = new ArrayList<>();

    private final String FILENAME = "usuario.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usr = (EditText) findViewById(R.id.txtUsuario);
        pass = (EditText) findViewById(R.id.txtPassword);
        nombre = (EditText) findViewById(R.id.txtNombre);
        apellido = (EditText) findViewById(R.id.txtApellido);
        cel = (EditText) findViewById(R.id.txtTelefono);
        genero = (RadioGroup) findViewById(R.id.opcionesSexo);
        masculino = (RadioButton) findViewById(R.id.gHombre);
        femenino = (RadioButton) findViewById(R.id.gMujer);
        fecha = (DatePicker) findViewById(R.id.fecha);
        optativa = (CheckBox) findViewById(R.id.optativa);
        progra = (CheckBox) findViewById(R.id.distribuida);
        redes = (CheckBox) findViewById(R.id.redes);
        analisis = (CheckBox) findViewById(R.id.analisis);
        protocolos = (CheckBox) findViewById(R.id.protocolos);
        beca = (Switch) findViewById(R.id.beca);
        registro = (Button) findViewById(R.id.btnRegistrarDatos);

    }


    private void guardar() {
        String generoF;
        String becaF;
        List<String> asignaturas = new ArrayList<>();

        if (masculino.isChecked()) {
            generoF = "Masculino";
        } else {
            generoF = "Femenino";
        }

        if (beca.isChecked()) {
            becaF = "Becado";
        } else {
            becaF = "Sin Beca";
        }


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fecha.getYear()).append(fecha.getMonth()).append(fecha.getDayOfMonth());

        if (optativa.isChecked()) {
            asignaturas.add("Dispositivos Moviles");
        }
        if (progra.isChecked()) {
            asignaturas.add("Programacion Distribuida");
        }
        if (redes.isChecked()) {
            asignaturas.add("Redes de Comunicacion");
        }
        if (analisis.isChecked()) {
            asignaturas.add("Analisis Matematico");
        }
        if (protocolos.isChecked()) {
            asignaturas.add("Protocoles de Comunicacion");
        }


        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(FILENAME, Context.MODE_APPEND));
            outputStreamWriter.write(usr.getText().toString() + stringBuilder.toString());
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
