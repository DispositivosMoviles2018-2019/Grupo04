package uce.edu.ec.gcoello_ex_1h.vista;

import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
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

import uce.edu.ec.gcoello_ex_1h.R;
import uce.edu.ec.gcoello_ex_1h.controlador.VehiculoControlador;
import uce.edu.ec.gcoello_ex_1h.modelo.Vehiculo;

public class ListarActivity extends AppCompatActivity {

    //datos a mostrar
    List<Vehiculo> listavehiculos = new ArrayList<Vehiculo>();

    VehiculoControlador aAdpt;
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        initList();


        // We get the ListView component from the layout
        ListView lv = (ListView) findViewById(R.id.listView);

        aAdpt = new VehiculoControlador(listavehiculos, this);
        lv.setAdapter(aAdpt);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {
                // We know the View is a <extView so we can cast it
                //TextView clickedView = (TextView) view;

                Toast.makeText(ListarActivity.this, "Vehiculo N° [" + position + "] ", Toast.LENGTH_SHORT).show();
                //- Planet ["+clickedView.getText()+"]
            }
        });

        // we register for the contextmneu
        registerForContextMenu(lv);
    }

    //crear nuevo vehiculo
    public void crear(View view) {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog);
        d.setTitle("Ingresar nuevo vehiculo");
        d.setCancelable(true);
        // Recuperando lo que tiene en las cajas de texto
        final EditText plac = (EditText) d.findViewById(R.id.editTextPlaca);
        final EditText marc = (EditText) d.findViewById(R.id.editTextMarca);
        final EditText fech = (EditText) d.findViewById(R.id.editTextFecha);
        final EditText cost = (EditText) d.findViewById(R.id.editTextCosto);
        final CheckBox matri = (CheckBox) d.findViewById(R.id.editcheckBox);
        //final RadioGroup rg = (RadioGroup) d.findViewById(R.id.editRadiog);

        final RadioGroup colr = (RadioGroup) d.findViewById(R.id.radioG);
        final RadioButton colNegro = (RadioButton) d.findViewById(R.id.negro);
        final RadioButton colBlanco = (RadioButton) d.findViewById(R.id.blanco);
        final RadioButton colRojo = (RadioButton) d.findViewById(R.id.rojo);
        final RadioButton colOtro = (RadioButton) d.findViewById(R.id.otro);

        final Spinner spinner = (Spinner)d.findViewById(R.id.spinner);
        Button b = (Button) d.findViewById(R.id.button1);

        String[] color = {"Blanco","Negro","Rojo","Otro"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, color));

        b.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                Pattern restriccionplaca = Pattern.compile("^([A-Z&&[^A,U,Z,E,X,M]]{3}-\\d{4})$");

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

                            String colA = "";
                            if (colBlanco.isChecked()){
                                colA = "Blamco";
                            }
                            if (colNegro.isChecked()){
                                colA = "Negro";
                            }

                            if (colRojo.isChecked()){
                                colA = "Rojo";
                            }
                            if (colOtro.isChecked()){
                                colA = "Otro";
                            }

                       //     String scolor = spinner.getSelectedItem().toString();

                            // Mensaje
                            Toast.makeText(getBaseContext(), "Ok!, Vehiculo agregado", Toast.LENGTH_LONG).show();

                            //ListaActivity.this.listavehiculos.add(new Vehiculo(placa, marca, fecha, costo, matriculado, scolor));

                            Vehiculo vehiculo = new Vehiculo(placa, marca, fecha, costo, matriculado, colA);
                            ListarActivity.this.listavehiculos.add(vehiculo);
                            ListarActivity.this.aAdpt.notifyDataSetChanged(); // We notify the data model is changed

                            d.dismiss();
                        } catch (ParseException e) {
                            System.out.print("error guardando "+e);
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        d.show();

    }

    // We want to create a context Menu when the user long click on an item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        // We know that each row in the adapter is a Map
        Vehiculo vehiculo = aAdpt.getItem(aInfo.position);

        menu.setHeaderTitle("Opciones para el auto: " + vehiculo.getPlaca());
        menu.add(1, 1, 1, "Editar");
        menu.add(1, 2, 2, "Eliminar");
        menu.add(1, 3, 3, "Guardar lista vehiculos");


    }

    public void editarVehiculo(final AdapterView.AdapterContextMenuInfo aInfo,Object vehiculos) {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog);
        d.setTitle("Editar Vehiculo");
        d.setCancelable(true);

        //final Vehiculo vehiculo=aAdpt.getItem(aInfo.position);

        final Vehiculo vehiculo = (Vehiculo)buscarPorParametro(ListarActivity.this.listavehiculos, aInfo.position);

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

                            String scolor = spinner.getSelectedItem().toString();

                            // Mensaje
                            Toast.makeText(getBaseContext(), "vehiculo guardado" + "\n", Toast.LENGTH_LONG).show();


                            ListarActivity.this.listavehiculos.set(aInfo.position,new Vehiculo(placa,marca,fecha,costo,matriculado,scolor));
                            ListarActivity.this.aAdpt.notifyDataSetChanged(); // We notify the data model is changed

                            d.dismiss();

                        } catch (ParseException e) {
                            System.out.print("-Error guardar "+e);
                        }
                    }
                }
            }
        });

        d.show();
    }

    public String borrar(Object id) {
        listavehiculos.remove((int)id);
        aAdpt.notifyDataSetChanged();
        return "borrado";
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

            case 3:
                guardarJson(listavehiculos);
        }
        return true;
    }

    private String guardarJson(List<Vehiculo>listavehiculos){
        String archivojason="";
        try {
            JSONArray jsonArray=new JSONArray();
            for (Vehiculo puntuacion:listavehiculos){
                JSONObject objeto=new JSONObject();
                objeto.put("placa",puntuacion.getPlaca());
                objeto.put("marca",puntuacion.getMarca());
                objeto.put("fecfab",puntuacion.getFecFab());
                objeto.put("costo",puntuacion.getCosto());
                objeto.put("matriculado",puntuacion.getMatriculado());
                objeto.put("color",puntuacion.getColor());

                jsonArray.put(objeto);

            }
            archivojason=jsonArray.toString();
            guardarArchivo(archivojason,"json");
        } catch (JSONException e){
            e.printStackTrace();
        }
        return archivojason;
    }

    public Object buscarPorParametro(Collection lista, Object parametro) {
        Vehiculo v = aAdpt.getItem((int)parametro);
        return v;
    }

    public List<Vehiculo> cargarLista() throws ClassNotFoundException, IOException {

        File ruta_sd = new File(Environment.getExternalStorageDirectory(), "GRUPO4");
        if (!ruta_sd.exists()) {
            return null;
        }

        File dataFile = new File(ruta_sd.getAbsolutePath(), "GRUPO4");

        ObjectInputStream ois = null;
        List<Vehiculo> lista = new ArrayList<>();

        try{
            FileInputStream fis = new FileInputStream(dataFile);
            ois = new ObjectInputStream(fis);
            Object aux = ois.readObject();
            while(true){

                Vehiculo e = new Vehiculo();
                e = (Vehiculo) aux;

                System.out.println(" Vehiculo: "+e.toString());

                lista.add(e);
                // System.out.println("tamaño lista: "+lista.size());

                ObjectInputStream oin = new ObjectInputStream(fis);
                aux = oin.readObject();
            }

        } catch (IOException io){
            System.out.println("\n************FIN**************");
            //io.printStackTrace();
        } finally {
            ois.close();
            // System.out.println("tamaño lista Finally: "+lista.size());
        }

        if(lista.size()>0)
            return lista;
        else
            return null;
    }

    public void guardarArchivo (String dato,String tipoarchivo ){
        try {
            //File ruta_sd= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File ruta_sd = new File(Environment.getExternalStorageDirectory(), "GRUPO4");
            if (!ruta_sd.exists()) {
                ruta_sd.mkdirs();
            }

            SimpleDateFormat simpleD = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
            String nombre = simpleD.format(new Date());
            //File f=new File (ruta_sd.getAbsolutePath(),"vehiculos_grupo4_"+nombre+"."+tipoarchivo);
            File f=new File (ruta_sd.getAbsolutePath(),"vehiculos_grupo4."+tipoarchivo);
            //Toast.makeText(getBaseContext(),""+ruta_sd,Toast.LENGTH_SHORT).show();
            OutputStreamWriter archivo= new OutputStreamWriter(new FileOutputStream(f));

            archivo.write(dato);
            archivo.flush();
            archivo.close();
            Toast t=Toast.makeText(this,"Datos Guardados en "+ruta_sd,
                    Toast.LENGTH_LONG);
            t.show();
        } catch (IOException e){
            System.out.println("Ficheros Error al escribir a tarjeta SD");
        }
    }


    private void initList() {

        List<Vehiculo> lista;
        try {
            lista = cargarLista();
        } catch (Exception e) {
            System.out.println("error cargando lista "+e);
            lista=null;
        }
        try {
            if(lista==null) {
                listavehiculos.add(new Vehiculo("XTR-9784", "Audi", simpleDate.parse("2015-11-13"), 79990d, true, "Negro"));
                listavehiculos.add(new Vehiculo("CCD-0789", "Honda", simpleDate.parse("1998-03-05"), 15340.00, false, "Blanco"));
            }
            else
                listavehiculos.addAll(lista);
            //ordenamos la lista de vehiculos
            Collections.sort(listavehiculos, new ComparadorVehiculos());

        } catch (Exception e) {
            System.out.println("error cargando lista "+e);
        }
    }

    class ComparadorVehiculos implements Comparator<Vehiculo> {
        public int compare(Vehiculo a, Vehiculo b) {
            //comparar por placa
            return a.getPlaca().compareTo(b.getPlaca());
        }
    }
}
