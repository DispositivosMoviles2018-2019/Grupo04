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
import java.util.Date;
import java.util.List;

import ec.edu.uce.ex2h_g04.modelo.Usuario;


//import ec.edu.uce.tarea_final_2h_clargo.modelo.Usuario;

/**
  */

public class UsuarioControl implements Serializable {

    public UsuarioControl(){
        super();
    }

    public String guardarJson(List<Usuario> lista){
        String arch="";
        String archivojason="";
        try {
            JSONArray jsonArray=new JSONArray();
            for (Usuario puntuacion:lista){
                JSONObject objeto=new JSONObject();
                objeto.put("id",puntuacion.getId());
                objeto.put("usuario",puntuacion.getUsuario());
                objeto.put("clave",puntuacion.getClave());
                jsonArray.put(objeto);
            }
            archivojason=jsonArray.toString();
            arch = guardarArchivo(archivojason,"json");
        } catch (JSONException e){
            e.printStackTrace();
        }
        return arch;
    }

    public String guardarArchivo (String dato,String tipoarchivo ){
        File f=null;
        try {
            //File ruta_sd= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File ruta_sd = new File(Environment.getExternalStorageDirectory(), "MIS_DATOS_VEHICULOS");
            if (!ruta_sd.exists()) {
                ruta_sd.mkdirs();
            }

            SimpleDateFormat simpleD = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
            String nombre = simpleD.format(new Date());
            f=new File (ruta_sd.getAbsolutePath(),"respaldo_usuarios"+nombre+"."+tipoarchivo);
            //Toast.makeText(getBaseContext(),""+ruta_sd,Toast.LENGTH_SHORT).show();
            OutputStreamWriter archivo= new OutputStreamWriter(new FileOutputStream(f));

            archivo.write(dato);
            archivo.flush();
            archivo.close();
        } catch (IOException e){
            Log.e("Ficheros","Error al escribir respaldo_usuarios a tarjeta SD");
        }
        return f.getAbsolutePath();
    }
}
