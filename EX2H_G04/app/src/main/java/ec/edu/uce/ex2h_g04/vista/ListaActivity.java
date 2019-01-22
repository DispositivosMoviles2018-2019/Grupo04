package ec.edu.uce.ex2h_g04.vista;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ec.edu.uce.ex2h_g04.R;
import ec.edu.uce.ex2h_g04.controlador.VehiculoAdapter;
import ec.edu.uce.ex2h_g04.modelo.BackupTask;
import ec.edu.uce.ex2h_g04.modelo.InterfazCRUD;
import ec.edu.uce.ex2h_g04.modelo.MyDbHelper;
import ec.edu.uce.ex2h_g04.modelo.Vehiculo;

public class ListaActivity extends Activity implements InterfazCRUD, BackupTask.CompletionListener {

    private static final int CAMERA_RESULT = 100;

    //datos a mostrar
    List<Vehiculo> listavehiculos = new ArrayList<Vehiculo>();

    VehiculoAdapter aAdpt;
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
    ListView lv=null;

    Dialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        // We get the ListView component from the layout
        lv = (ListView) findViewById(R.id.listView);

        aAdpt = new VehiculoAdapter(listavehiculos, this);
        lv.setAdapter(aAdpt);

        listar();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {
                // We know the View is a <extView so we can cast it
                //TextView clickedView = (TextView) view;

                Toast.makeText(ListaActivity.this, "Auto: "+aAdpt.getItem(position).getPlaca(), Toast.LENGTH_SHORT).show();
                //- Planet ["+clickedView.getText()+"]
            }
        });

        // we register for the contextmneu
        registerForContextMenu(lv);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addVehiculo(view);

            }
        });
    }

    // We want to create a context Menu when the user long click on an item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        // We know that each row in the adapter is a Map
        Vehiculo vehiculo = aAdpt.getItem(aInfo.position);

        menu.setHeaderTitle("Auto seleccionado: " + vehiculo.getPlaca());
        menu.add(1, 1, 1, "Editar");
        menu.add(1, 2, 2, "Eliminar");
        //menu.add(1, 3, 3, "Guardar JSON");
    }

    // This method is called when user selects an Item in the Context menu
    @Override

    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int opcion=item.getItemId();
        switch (opcion){
            case 1:
                editarVehiculo(aInfo,listavehiculos);
                break;

            case 2:
                borrar(aInfo.position);
                break;

            //case 3:
            //guardarJson(listavehiculos);
            //String cadena="";
            //cadena=guardarJson(listavehiculos);
            //Toast.makeText(getBaseContext(),"Dato Guardado"+cadena,Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void respaldo(View view) {
        guardarJson(listavehiculos);
        respaldoBase(view);
    }

    public void  restaurarBase(View view) {

        if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ) {
            BackupTask task = new BackupTask(this);
            task.setCompletionListener(this);
            task.execute(BackupTask.COMMAND_RESTORE);
            initList();
        }

    }

    public void respaldoBase(View view) {

        if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ) {
            BackupTask task = new BackupTask(this);
            task.setCompletionListener(this);
            task.execute(BackupTask.COMMAND_BACKUP);
        }
    }

    private void initList() {

        try {
            MyDbHelper mHelper = new MyDbHelper(this);
            SQLiteDatabase mDb = mHelper.getReadableDatabase();

            String query = "SELECT _id, placa, marca, fecFab, costo, matriculado, color, foto, estado FROM "+MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.TABLE_NAME;
            Log.e("QUERY", query);
            Cursor mCursor = mDb.rawQuery(query, null);
            Vehiculo vehiculo;

            Date fecFab=new Date();

            listavehiculos.clear();
            aAdpt.setNotifyOnChange(true);

            if(mCursor.getCount()>0) {
                for (int i = 0; i < mCursor.getCount(); i++) {
                    mCursor.moveToPosition(i);
                    int id = mCursor.getInt(0);
                    String placa = mCursor.getString(1);
                    String marca = mCursor.getString(2);
                    try {
                        fecFab = simpleDate.parse(mCursor.getString(3));
                    } catch (Exception e) {
                        Log.e("error", "leyendo fecha");
                    }
                    Double costo = mCursor.getDouble(4);
                    int matriculado= mCursor.getInt(5);
                    String color = mCursor.getString(6);
                    String foto = mCursor.getString(7);
                    int estado= mCursor.getInt(8);

                    Drawable draw = new BitmapDrawable(getBitmapFromString(foto));

                    vehiculo = new Vehiculo(id, placa, marca, fecFab, costo, matriculado==1?true:false, color, estado==1?true:false, draw);
                    listavehiculos.add(vehiculo);

                }
            }

            mCursor.close();
            mDb.close();

            //ordenamos la lista de vehiculos por placa segun las preferencias guardadas
            Collections.sort(listavehiculos, new ComparadorVehiculos());
            aAdpt.setNotifyOnChange(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addVehiculo(View view) {
        crear(view);
    }

    public void editarVehiculo(final AdapterView.AdapterContextMenuInfo aInfo,Object vehiculos) {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog);
        d.setTitle("Editar Vehiculo");
        d.setCancelable(true);

        //final Vehiculo vehiculo=aAdpt.getItem(aInfo.position);

        final Vehiculo vehiculo = (Vehiculo)buscarPorParametro(ListaActivity.this.listavehiculos, aInfo.position);

        // Recuperando lo que tiene en las cajas de texto
        final EditText plac = (EditText) d.findViewById(R.id.editTextPlaca);
        plac.setText(vehiculo.getPlaca().toString());
        plac.setEnabled(false);
        final EditText marc = (EditText) d.findViewById(R.id.editTextMarca);
        marc.setText(vehiculo.getMarca().toString());
        final EditText fech = (EditText) d.findViewById(R.id.editTextFecha);
        fech.setText(simpleDate.format(vehiculo.getFecFab()));
        final EditText cost = (EditText) d.findViewById(R.id.editTextCosto);
        cost.setText(vehiculo.getCosto().toString());
        final CheckBox matri = (CheckBox) d.findViewById(R.id.editcheckBox);
        matri.setChecked(vehiculo.getMatriculado());

        final CheckBox estado = (CheckBox) d.findViewById(R.id.reservado);
        estado.setChecked(vehiculo.getEstado());

        //final RadioGroup rg = (RadioGroup) d.findViewById(R.id.editRadiog);
        final Spinner spinner = (Spinner)d.findViewById(R.id.spinner);

        Button b = (Button) d.findViewById(R.id.button1);

        String[] color = {"Blanco","Negro","Rojo","Otro"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, color));
        String col = vehiculo.getColor();
        if(col.equals("Blanco"))
            spinner.setSelection(0);
        else if(col.equals("Negro"))
            spinner.setSelection(1);
        else if(col.equals("Rojo"))
            spinner.setSelection(2);
        else
            spinner.setSelection(3);

        final ImageView fot = d.findViewById(R.id.fotoDialogo);
        fot.setImageDrawable(vehiculo.getFoto());

        b.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                Pattern restriccionplaca = Pattern.compile("^([A-Z]{3}-\\d{4})$");
                Matcher validarplaca = restriccionplaca.matcher(plac.getText());
                if (!validarplaca.matches() || plac.getText().equals("")) {
                    Toast.makeText(getBaseContext(), "Placa no valida" + "\n", Toast.LENGTH_LONG).show();
                } else {
                    Pattern restriccionfecha = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");
                    Matcher validarfecha = restriccionfecha.matcher(fech.getText());
                    if (!validarfecha.matches() || fech.getText().equals("")){
                        Toast.makeText(getBaseContext(), "Fecha no valida" + "\n", Toast.LENGTH_LONG).show();
                    } else {
                        System.out.print("---------"+validarfecha.matches());
                        try {

                            String placa = plac.getText().toString();
                            String marca = marc.getText().toString();
                            String dateInString = fech.getText().toString();
                            Date fecha = simpleDate.parse(dateInString);
                            Double costo = Double.parseDouble(cost.getText().toString());
                            Boolean matriculado = matri.isChecked();
                            Boolean est = estado.isChecked();

                            String scolor = spinner.getSelectedItem().toString();

                            int id = listavehiculos.get(aInfo.position).getId();

                            MyDbHelper mHelper = new MyDbHelper(getApplicationContext());
                            SQLiteDatabase mDb = mHelper.getWritableDatabase();

                            //grabar nuevo registro a la base de datos
                            // Valores
                            ContentValues values = new ContentValues();

                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.PLACA, placa);
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.MARCA, marca);
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.FECFAB, dateInString);
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.COSTO, costo);
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.MATRICULADO, matriculado?1:0);
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.COLOR, scolor);
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.FOTO, ConvertDrawableToByteArray(fot.getDrawable()));
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.ESTADO, est?1:0);

                            // WHERE
                            String selection = MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.PLACA + " = ?";
                            String[] selectionArgs = {placa};

                            // Actualizar
                            mDb.update(
                                    MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.TABLE_NAME,
                                    values,
                                    selection,
                                    selectionArgs);

                            //actualizamos los datos del vehiculo
                            ListaActivity.this.listavehiculos.set(aInfo.position,new Vehiculo(id, placa,marca,fecha,costo,matriculado,scolor,est,fot.getDrawable()));
                            ListaActivity.this.aAdpt.notifyDataSetChanged(); // We notify the data model is changed

                            Toast.makeText(getBaseContext(), "Vehiculo actualizado!", Toast.LENGTH_LONG).show();
                            d.dismiss();
                        } catch (ParseException e) {
                            System.out.print("---------Error xxx bbb");
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        d.show();
    }

    ////////////////////////////////////////////////////////////////////////////////////////7
    //MANEJO DE IMAGEN A JSON
    private String getStringFromBitmap(Bitmap bitmapPicture) {
        /*
         * This functions converts Bitmap picture to a string which can be
         * JSONified.
         * */
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }


    // llamar asi a esta funcion
    // String jsonString = jsonObj.getString("image");
    private Bitmap getBitmapFromString(String jsonString) {
        // This Function converts the String back to Bitmap
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
////////////////////////////////////////////////////////////////////////////////////////7

    private String guardarJson(List<Vehiculo>listavehiculos){
        String archivojason="";
        try {
            JSONArray jsonArray=new JSONArray();
            for (Vehiculo puntuacion:listavehiculos){
                JSONObject objeto=new JSONObject();
                objeto.put("id",puntuacion.getId());
                objeto.put("placa",puntuacion.getPlaca());
                objeto.put("marca",puntuacion.getMarca());
                objeto.put("fecfab",puntuacion.getFecFab());
                objeto.put("costo",puntuacion.getCosto());
                objeto.put("matriculado",puntuacion.getMatriculado());
                objeto.put("color",puntuacion.getColor());
                objeto.put("estado",puntuacion.getEstado());

                String encodedImage = getStringFromBitmap(((BitmapDrawable)puntuacion.getFoto()).getBitmap());

                objeto.put("foto",encodedImage);

                jsonArray.put(objeto);
            }
            archivojason=jsonArray.toString();
            guardarArchivo(archivojason,"json");
        } catch (JSONException e){
            e.printStackTrace();
        }
        return archivojason;
    }

    public void guardarArchivo (String dato,String tipoarchivo ){
        try {
            //File ruta_sd= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File ruta_sd = new File(Environment.getExternalStorageDirectory(), "MIS_DATOS_VEHICULOS");
            if (!ruta_sd.exists()) {
                ruta_sd.mkdirs();
            }

            SimpleDateFormat simpleD = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
            String nombre = simpleD.format(new Date());
            File f=new File (ruta_sd.getAbsolutePath(),"respaldo_vehiculos_"+nombre+"."+tipoarchivo);
            //Toast.makeText(getBaseContext(),""+ruta_sd,Toast.LENGTH_SHORT).show();
            OutputStreamWriter archivo= new OutputStreamWriter(new FileOutputStream(f));

            archivo.write(dato);
            archivo.flush();
            archivo.close();
            Toast t=Toast.makeText(this,"Ok! respaldo vehiculos\n"+ruta_sd, Toast.LENGTH_SHORT);
            t.show();
        } catch (IOException e){
            Log.e("Ficheros","Error al escribir respaldo_vehiculos a tarjeta SD");
        }
    }

    public static byte[] ConvertDrawableToByteArray(Drawable drawableResource) {

        Bitmap imageBitmap = ((BitmapDrawable) drawableResource).getBitmap();
        ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, imageByteStream);
        byte[] imageByteData = imageByteStream.toByteArray();
        return imageByteData;
    }

    @Override
    public void onResume() {
        super.onResume();
        //ordenamos la lista de vehiculos por placa segun las preferencias guardadas
        Collections.sort(listavehiculos, new ComparadorVehiculos());
        ListaActivity.this.aAdpt.notifyDataSetChanged(); // We notify the data model is changed
    }


    @Override
    public String crear(Object obj) {
        d = new Dialog(this);
        d.setContentView(R.layout.dialog);
        d.setTitle("Ingresar nuevo vehiculo");
        d.setCancelable(true);

        // Recuperando lo que tiene en las cajas de texto
        final EditText plac = (EditText) d.findViewById(R.id.editTextPlaca);
        final EditText marc = (EditText) d.findViewById(R.id.editTextMarca);
        final EditText fech = (EditText) d.findViewById(R.id.editTextFecha);
        final EditText cost = (EditText) d.findViewById(R.id.editTextCosto);
        final CheckBox matri = (CheckBox) d.findViewById(R.id.editcheckBox);

        final Spinner spinner = (Spinner)d.findViewById(R.id.spinner);
        Button b = (Button) d.findViewById(R.id.button1);

        Button botonImg = (Button)d.findViewById(R.id.btn_imagen);

        botonImg.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(it, CAMERA_RESULT);
            }
        });

        final ImageView fot = (ImageView)d.findViewById(R.id.fotoDialogo);

        //final BitmapDrawable drawable = (BitmapDrawable)fot.getDrawable();
        final Drawable drawable = ContextCompat.getDrawable(this, R.drawable.icon);
        fot.setImageDrawable(drawable);
        //Log.e("DRAWABLE ", drawable.toString());
        //Bitmap bitmap = drawable.getBitmap();

        final CheckBox estado = (CheckBox) d.findViewById(R.id.reservado);

        String[] color = {"Blanco","Negro","Rojo","Otro"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, color));

        b.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                Pattern restriccionplaca = Pattern.compile("^([A-Z]{3}-\\d{4})$");
                Matcher validarplaca = restriccionplaca.matcher(plac.getText());
                if (!validarplaca.matches() || plac.getText().equals("")) {
                    Toast.makeText(getBaseContext(), "Placa no valida" + "\n", Toast.LENGTH_LONG).show();
                } else {
                    Pattern restriccionfecha = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");
                    Matcher validarfecha = restriccionfecha.matcher(fech.getText());
                    if (!validarfecha.matches() || fech.getText().equals("")){
                        Toast.makeText(getBaseContext(), "Fecha no valida" + "\n", Toast.LENGTH_LONG).show();
                    } else {
                        System.out.print("---------"+validarfecha.matches());
                        try {
                            String placa = plac.getText().toString();
                            String marca = marc.getText().toString();
                            String dateInString = fech.getText().toString();
                            Date fecha = simpleDate.parse(dateInString);
                            Double costo = Double.parseDouble(cost.getText().toString());
                            Boolean matriculado = matri.isChecked();

                            Boolean est = estado.isChecked();

                            String scolor = spinner.getSelectedItem().toString();

                            MyDbHelper mHelper = new MyDbHelper(getApplicationContext());
                            SQLiteDatabase mDb;

                            //grabar nuevo registro a la base de datos
                            // Valores
                            ContentValues values = new ContentValues();

                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.PLACA, placa);
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.MARCA, marca);
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.FECFAB, dateInString);
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.COSTO, costo);
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.MATRICULADO, matriculado?1:0);
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.COLOR, scolor);
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.FOTO, getStringFromBitmap(((BitmapDrawable)drawable).getBitmap()));
                            values.put(MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.ESTADO, est?1:0);

                            mDb = mHelper.getWritableDatabase();
                            mDb.insert(
                                    MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.TABLE_NAME,
                                    null,
                                    values);
                            mHelper.close();
                            mDb.close();

                            //consultamos el id del vehiculo que acabamos de ingresar
                            mDb = mHelper.getReadableDatabase();
                            String query = "SELECT "+MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.ID+" FROM "+MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.TABLE_NAME+" WHERE "+MyDbHelper.EsquemaVehiculo.ColumnasVehiculo.PLACA+"=?";
                            Log.e("QUERY", query);
                            Cursor mCursor = mDb.rawQuery(query, new String[]{placa});

                            int id=0;

                            if(mCursor.getCount()>0) {
                                mCursor.moveToFirst();
                                id = mCursor.getInt(0);
                            }

                            Vehiculo vehiculo = new Vehiculo(id, placa, marca, fecha, costo, matriculado, scolor, est, drawable);
                            ListaActivity.this.listavehiculos.add(vehiculo);
                            //ordenamos la lista de vehiculos por placa segun las preferencias guardadas
                            Collections.sort(listavehiculos, new ComparadorVehiculos());
                            ListaActivity.this.aAdpt.notifyDataSetChanged(); // We notify the data model is changed

                            mCursor.close();
                            mDb.close();

                            // Mensaje
                            Toast.makeText(getBaseContext(), "Ok!, Vehiculo agregado", Toast.LENGTH_LONG).show();

                            d.dismiss(); // cerramos el dialogo
                        } catch (ParseException e) {
                            System.out.print("error guardando "+e);
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        d.show();

        return "ok";
    }

    //resultado de la camara

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode && requestCode == CAMERA_RESULT) {
            // Get Extra from the intent
            Bundle extras = data.getExtras();

            Bitmap bitmap = (Bitmap) extras.get("data");

            int byteSize = bitmap.getRowBytes() * bitmap.getHeight();
            ByteBuffer byteBuffer = ByteBuffer.allocate(byteSize);
            bitmap.copyPixelsToBuffer(byteBuffer);

            // Get the byteArray.
            byte[] byteArray = byteBuffer.array();

            // Get the ByteArrayInputStream.
            ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10; //Downsample by 10x
            Bitmap userImage = BitmapFactory.decodeStream(bs, null, options);


            ImageView iv = (ImageView)d.findViewById(R.id.fotoDialogo);
            if(iv!=null)
                iv.setImageBitmap(userImage);
            Toast.makeText(getApplicationContext(), "imagen "+lv==null?"nulo":"no nulo", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public String actualizar(Object id) {
        ListaActivity.this.listavehiculos.set((int)id, new Vehiculo());
        aAdpt.notifyDataSetChanged();
        return "ok";
    }

    @Override
    public String borrar(Object id) {
        MyDbHelper mHelper = new MyDbHelper(this);
        SQLiteDatabase mDb = mHelper.getReadableDatabase();

        mDb = mHelper.getWritableDatabase();
        //Eliminar un registro
        mDb.execSQL("DELETE FROM vehiculo WHERE placa='"+listavehiculos.get((int)id).getPlaca()+"'");

        listavehiculos.remove((int)id);
        aAdpt.notifyDataSetChanged();

        return "borrado";
    }

    @Override
    public Object buscarPorParametro(Collection lista, Object parametro) {
        Vehiculo v = aAdpt.getItem((int)parametro);
        return v;
    }

    @Override
    public Collection listar() {
        initList();
        return ListaActivity.this.listavehiculos;
    }

    //Clase para comparar vehiculos
    class ComparadorVehiculos implements Comparator<Vehiculo> {
        public int compare(Vehiculo a, Vehiculo b) {

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            boolean ascendente = settings.getBoolean("asc", true); //verificamos si esta activo el checkbox ascendente

            //ordenar por placa segun las preferencias
            int x = a.getPlaca().compareTo(b.getPlaca());
            if(ascendente) {
                return  x;
            }
            else {
                x = b.getPlaca().compareTo(a.getPlaca());
                return x;
            }

            //ordenar por costo
            /*if(a.getCosto()<b.getCosto())
                return -1;
            else if(a.getCosto()>b.getCosto())
                return 1;
            else
                return 0;n//son iguales
                */
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //FUNCIONES DE CONTROL DE RESPALDO Y RESTAURACION DE BASE DE DATOS
    @Override
    public void onBackupComplete() {
        Toast.makeText(this, "Respaldo satisfactorio!", Toast.LENGTH_LONG).show();
        listar();
        aAdpt = new VehiculoAdapter(listavehiculos, this);
        lv.setAdapter(aAdpt);
    }

    @Override
    public void onError(int errorCode) {
        if(errorCode == BackupTask.RESTORE_NOFILEERROR) {
            Toast.makeText(this, "No se ha encontrado respaldo para restaurar",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Error durante la operación: "+errorCode,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRestoreComplete() {
        Toast.makeText(this, "Restauración satisfactoria!", Toast.LENGTH_LONG).show();
        listar();
        aAdpt = new VehiculoAdapter(listavehiculos, this);
        lv.setAdapter(aAdpt);
    }
}
