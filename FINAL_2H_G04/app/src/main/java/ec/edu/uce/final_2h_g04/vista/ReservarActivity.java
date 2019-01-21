package ec.edu.uce.final_2h_g04.vista;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ec.edu.uce.final_2h_g04.R;
import ec.edu.uce.final_2h_g04.controlador.ReservaControl;
import ec.edu.uce.final_2h_g04.modelo.Globales;
import ec.edu.uce.final_2h_g04.modelo.InterfazCRUD;
import ec.edu.uce.final_2h_g04.modelo.MailJob;
import ec.edu.uce.final_2h_g04.modelo.MyDbHelper;
import ec.edu.uce.final_2h_g04.modelo.Reserva;
import ec.edu.uce.final_2h_g04.modelo.Usuario;

public class ReservarActivity extends AppCompatActivity implements InterfazCRUD {

    private final double VALOR_DIA = 80.0;


    ArrayList<Reserva> listaReserva;

    Spinner placas;
    TextView numeroR, valor;
    EditText mail, fechaP, fechaE, celu;

    Button btn_ok, btn_cancel;

    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);

        listaReserva = new ArrayList<Reserva>();

        placas = (Spinner) findViewById(R.id.spinPlacas);
        //numeroR = (TextView)findViewById(R.id.txtReserva);
        mail = (EditText) findViewById(R.id.txtMail);
        celu = (EditText) findViewById(R.id.txtCelular);
        fechaP = (EditText) findViewById(R.id.txtFechaP);
        fechaE = (EditText) findViewById(R.id.txtFechaE);
        valor = (TextView) findViewById(R.id.txtValor);

        btn_ok = (Button) findViewById(R.id.btnOk);
        btn_cancel = (Button) findViewById(R.id.btnCancel);


        fechaP.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    double val = restarFechas(simpleDate.parse(fechaE.getText().toString()), simpleDate.parse(fechaP.getText().toString())) * VALOR_DIA;
                    valor.setText("Valor a pagar: $" + val);
                } catch (Exception e) {
                    valor.setText("Valor a pagar: $0");
                    Log.e("Error de fechas ", e.toString());
                }
            }
        });

        fechaE.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    double val = restarFechas(simpleDate.parse(fechaE.getText().toString()), simpleDate.parse(fechaP.getText().toString())) * VALOR_DIA;
                    valor.setText("Valor a pagar: $" + val);
                } catch (Exception e) {
                    valor.setText("Valor a pagar: $0");
                    Log.e("Error de fechas ", e.toString());
                }
            }
        });


        MyDbHelper mHelper = new MyDbHelper(this);
        SQLiteDatabase mDb = mHelper.getReadableDatabase();

        mHelper = new MyDbHelper(this);
        mDb = mHelper.getReadableDatabase();
        Cursor mCursor;
        /*Cursor c = db.query(
                LawyerEntry.TABLE_NAME,  // Nombre de la tabla
                null,  // Lista de Columnas a consultar
                null,  // Columnas para la cláusula WHERE
                null,  // Valores a comparar con las columnas del WHERE
                null,  // Agrupar con GROUP BY
                null,  // Condición HAVING para GROUP BY
                null  // Cláusula ORDER BY
        );*/
        //String[] columns = new String[] {MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.ID, MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.PLACA, MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.MARCA, MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.FECFAB, MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.COSTO, MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.MATRICULADO, MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.COLOR, MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.FOTO, MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.ESTADO};
        //mCursor = mDb.query(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.TABLE_NAME, columns, null, null, null, null, null);

        //cargar las reservas de la base a la 'listaReservas'
        String query = "SELECT _id, email, celular, fecRes, fecEnt, valor, user, placa FROM " + MyDbHelper.EsquemaReserva.ColumnasReserva.TABLE_NAME;
        Log.e("QUERY", query);
        mCursor = mDb.rawQuery(query, null);
        Reserva reserva;

        Date fecReserva = new Date();
        Date fecEntrega = new Date();
        if (mCursor.getCount() > 0) {
            for (int i = 0; i < mCursor.getCount(); i++) {
                mCursor.moveToPosition(i);
                Integer numeroReserva = mCursor.getInt(0);
                String email = mCursor.getString(1);
                String celular = mCursor.getString(2);
                try {
                    fecReserva = simpleDate.parse(mCursor.getString(3));
                    fecEntrega = simpleDate.parse(mCursor.getString(4));
                } catch (Exception e) {
                    Log.e("error", "leyendo fecha");
                }

                Double valor = mCursor.getDouble(5);
                int user = mCursor.getInt(6);
                String placa = mCursor.getString(7);

                reserva = new Reserva(numeroReserva, email, celular, fecReserva, fecEntrega, valor, user, placa);
                listaReserva.add(reserva);
            }
        }

        mCursor.close();

        //seleccionamos las placas de los autos no reservados, osea 'estado' igual a 0
        query = "SELECT " + MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.PLACA + " FROM " + MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.TABLE_NAME + " WHERE " + MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.ESTADO + "=?";
        Log.e("QUERY", query);
        mCursor = mDb.rawQuery(query, new String[]{"0"});

        if (mCursor.getCount() <= 0) {
            mCursor.close();
            mDb.close();

            Toast.makeText(getApplicationContext(), "NO HAY AUTOS DISPONIBLES EN ESTE MOMENTO!", Toast.LENGTH_LONG).show();
            return;
        }
        //llenar las placas
        ArrayList<String> lista = new ArrayList<String>();
        for (int i = 0; i < mCursor.getCount(); i++) {
            mCursor.moveToPosition(i);
            lista.add(mCursor.getString(0)); //PLACA
        }
        placas.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista));

        mCursor.close();
        mDb.close();
    }


    public void listarReservas(View view) {

        Reserva reserva;
        String data = "";
        MostrarDatos mostrarDatos = new MostrarDatos();

        for (int i = 0; i < listaReserva.size(); i++) {
            reserva = listaReserva.get(i);
            data += reserva.toString() + "\n\n";
        }


        //lanzamos la actividad de mostrar datos
        Intent intent = new Intent(getApplicationContext(), MostrarDatos.class);
        intent.putExtra("titulo", "AUTOS RESERVADOS");
        intent.putExtra("data", data);
        startActivity(intent);
    }


    @Override
    public String crear(Object obj) {
        listaReserva.add((Reserva) obj);
        return "ok";
    }

    @Override
    public String actualizar(Object id) {
        return null;
    }

    @Override
    public String borrar(Object id) {
        return null;
    }

    @Override
    public Object buscarPorParametro(Collection lista, Object parametro) {
        return null;
    }

    @Override
    public Collection listar() {
        return null;
    }

    public void botonOK(View view) {

        //VALIDACIONES

        if (placas.getCount() <= 0) {
            Toast.makeText(getApplicationContext(), "NO EXISTEN AUTOS DISPONIBLES EN ESTE MOMENTO!", Toast.LENGTH_LONG).show();
            return;
        }

        Pattern restriccioncorreo = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher validarcorreo = restriccioncorreo.matcher(mail.getText());
        if (!validarcorreo.matches() || mail.getText().equals("")) {
            Toast.makeText(getBaseContext(), "Direccion de correo no valida" + "\n", Toast.LENGTH_LONG).show();
            return;
        }

        Pattern restriccioncelu = Pattern.compile("09\\d{8}");
        Matcher validarcelu = restriccioncelu.matcher(celu.getText());
        if (!validarcelu.matches() || fechaP.getText().equals("")) {
            Toast.makeText(getBaseContext(), "Numero de celular no valido" + "\n", Toast.LENGTH_LONG).show();
            return;
        }

        Pattern restriccionfecha = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");
        Matcher validarfecha = restriccionfecha.matcher(fechaP.getText());
        if (!validarfecha.matches() || fechaP.getText().equals("")) {
            Toast.makeText(getBaseContext(), "Fecha de Prestamo no valida" + "\n", Toast.LENGTH_LONG).show();
            return;
        }

        validarfecha = restriccionfecha.matcher(fechaE.getText());
        if (!validarfecha.matches() || fechaP.getText().equals("")) {
            Toast.makeText(getBaseContext(), "Fecha de Entrega no valida" + "\n", Toast.LENGTH_LONG).show();
            return;
        }

        int numUltimo = 0;
        double val = 0;

        MyDbHelper mHelper = new MyDbHelper(this);
        SQLiteDatabase mDb = mHelper.getReadableDatabase();

        String columns[] = new String[]{MyDbHelper.EsquemaReserva.ColumnasReserva.ID};

        Cursor c = mDb.query(
                MyDbHelper.EsquemaReserva.ColumnasReserva.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if (c != null && c.getCount() > 0) {
            c.moveToLast();
            numUltimo = c.getInt(0);
        }
        c.close();
        mDb.close();

        try {
            val = restarFechas(simpleDate.parse(fechaE.getText().toString()), simpleDate.parse(fechaP.getText().toString())) * VALOR_DIA;
            valor.setText("Valor a pagar: $" + val);
        } catch (Exception e) {
            Log.e("Error de fechas ", e.toString());
        }

        Date fP = null, fE = null;

        try {
            fP = simpleDate.parse(fechaP.getText().toString());
            fE = simpleDate.parse(fechaE.getText().toString());
        } catch (Exception e) {
            System.out.println("Error formato fechas " + e);
        }

        Globales g = Globales.getInstance();
        Usuario user = g.getUser();

        Reserva reserva = new Reserva(numUltimo, mail.getText().toString(), celu.getText().toString(), fP, fE, val, user.getId(), placas.getSelectedItem().toString());

        crear(reserva);

        //actualizar estado del vehiculo a reservado
        // Valores
        ContentValues values = new ContentValues();

        // Valores nuevos del nombre y teléfono
        values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.ESTADO, 1);

        // WHERE
        String selection = MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.PLACA + " = ?";
        String[] selectionArgs = {placas.getSelectedItem().toString()};

        // Actualizar
        mDb = mHelper.getWritableDatabase();
        mDb.update(
                MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        c.close();
        mDb.close();


        //grabar nuevo registro de reserva a la base de datos
        // CREATE TABLE reserva (_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, celular TEXT, fecRes DATE, fecEnt DATE, valor REAL, user INTEGER, placa TEXT
        values = new ContentValues();

        values.put(MyDbHelper.EsquemaReserva.ColumnasReserva.EMAIL, reserva.getEmail());
        values.put(MyDbHelper.EsquemaReserva.ColumnasReserva.CELULAR, reserva.getCelular());
        values.put(MyDbHelper.EsquemaReserva.ColumnasReserva.FECRES, simpleDate.format(reserva.getFecReserva()));
        values.put(MyDbHelper.EsquemaReserva.ColumnasReserva.FECENT, simpleDate.format(reserva.getFecEntrega()));
        values.put(MyDbHelper.EsquemaReserva.ColumnasReserva.VALOR, reserva.getValor());
        values.put(MyDbHelper.EsquemaReserva.ColumnasReserva.USER, reserva.getCelular());
        values.put(MyDbHelper.EsquemaReserva.ColumnasReserva.PLACA, reserva.getPlaca());

        mDb = mHelper.getWritableDatabase();
        mDb.insert(
                MyDbHelper.EsquemaReserva.ColumnasReserva.TABLE_NAME,
                null,
                values);
        mHelper.close();
        mDb.close();

        Toast.makeText(getApplicationContext(), "OK! se ha creado la reservacion\n" + "Auto: " + placas.getSelectedItem().toString() + " Total: " + val, Toast.LENGTH_LONG).show();

        //mandar SMS
        /*Intent intent = new Intent(getApplicationContext(), SMSActivity.class);
        intent.putExtra("cel", reserva.getCelular());
        intent.putExtra("data", "Auto reservado: " + reserva.getPlaca() + " valor:" + reserva.getValor());
        startActivity(intent);*/

        //MAndar correo
        new MailJob("epoka.asuru@gmail.com", "narkolgmail1").execute(
                new MailJob.Mail("epoka.asuru@gmail.com", "nb.realmagic@gmail.com", "subjeto", "contenido")
        );
        finish();
    }

    protected void sendEmail() {
        String[] TO = {"contacto@seogalicia.es"}; //aquí pon tu correo
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);

        // Esto podrás modificarlo si quieres, el asunto y el cuerpo del mensaje
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Escribe aquí tu mensaje");

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
        }
    }


    public void respaldo(View view) {
        ReservaControl reserva = new ReservaControl();
        String name = reserva.guardarJson(listaReserva);
        Toast.makeText(getApplicationContext(), "Ok, respaldo\n" + name, Toast.LENGTH_LONG).show();
    }

    public void botonCancelar(View view) {
        finish();
    }

    public int restarFechas(Date hoy, Date fecha) {
        final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día
        long diferencia = (hoy.getTime() - fecha.getTime()) / MILLSECS_PER_DAY;
        Log.e("dif-Dias", "Diferencia de dias " + diferencia);
        return (int) diferencia;
    }
}
