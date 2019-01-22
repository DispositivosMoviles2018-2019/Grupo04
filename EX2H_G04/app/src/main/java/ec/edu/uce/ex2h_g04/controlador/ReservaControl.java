package ec.edu.uce.ex2h_g04.controlador;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ec.edu.uce.ex2h_g04.modelo.Reserva;


//import ec.edu.uce.tarea_final_2h_clargo.modelo.Reserva;


public class ReservaControl implements Serializable {

    public ReservaControl(){
        super();
    }

    public String guardarJson(ArrayList<Reserva> lista){
        String arch="";
        String archivojason="";
        try {
            JSONArray jsonArray=new JSONArray();
            for (Reserva puntuacion:lista){
                JSONObject objeto=new JSONObject();
                objeto.put("numeroReserva",puntuacion.getNumeroReserva());
                objeto.put("email",puntuacion.getEmail());
                objeto.put("celular",puntuacion.getCelular());
                objeto.put("fecReserva",puntuacion.getFecReserva());
                objeto.put("fecEntrega",puntuacion.getFecEntrega());
                objeto.put("valor",puntuacion.getValor());
                objeto.put("user",puntuacion.getUser());
                objeto.put("placa",puntuacion.getPlaca());
                jsonArray.put(objeto);
            }
            archivojason=jsonArray.toString();
            arch = guardarArchivo(archivojason,"json");
        } catch (JSONException e){
            e.printStackTrace();
        }
        return arch;
    }

    private String guardarArchivo (String dato,String tipoarchivo ){
        File f=null;
        try {
            //File ruta_sd= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File ruta_sd = new File(Environment.getExternalStorageDirectory(), "MIS_DATOS_VEHICULOS");
            if (!ruta_sd.exists()) {
                ruta_sd.mkdirs();
            }

            SimpleDateFormat simpleD = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
            String nombre = simpleD.format(new Date()); //fecha actual
            f=new File (ruta_sd.getAbsolutePath(),"respaldo_reservas_"+nombre+"."+tipoarchivo);
            //Toast.makeText(getBaseContext(),""+ruta_sd,Toast.LENGTH_SHORT).show();
            OutputStreamWriter archivo= new OutputStreamWriter(new FileOutputStream(f));

            archivo.write(dato);
            archivo.flush();
            archivo.close();
            //Toast t=Toast.makeText(,"Datos Guardados en "+ruta_sd, Toast.LENGTH_SHORT).show();
        } catch (IOException e){
            Log.e("Ficheros","Error al escribir reservas (json) a tarjeta SD");
        }
        return f.getAbsolutePath();
    }
}

