package ec.edu.uce.final_2h_g04.controlador;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ec.edu.uce.final_2h_g04.R;
import ec.edu.uce.final_2h_g04.modelo.MyDbHelper;
import ec.edu.uce.final_2h_g04.modelo.Vehiculo;

//import ec.edu.uce.tarea_final_2h_clargo.modelo.MyDbHelper;
//import ec.edu.uce.tarea_final_2h_clargo.modelo.Vehiculo;
//import ec.edu.uce.tarea_final_2h_clargo.R;

/**

 */

public class VehiculoAdapter extends ArrayAdapter<Vehiculo> {
    private List<Vehiculo> listavehiculos;
    private Context ctx;

    public VehiculoAdapter(List<Vehiculo> listavehiculos, Context ctx) {
        super(ctx, R.layout.vista, listavehiculos);
        this.listavehiculos = listavehiculos;
        this.ctx = ctx;
    }

    public int getCount() {
        return listavehiculos.size();
    }

    public Vehiculo getItem(int position) {
        return listavehiculos.get(position);
    }

    public long getItemId(int position) {
        return listavehiculos.get(position).hashCode();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        PlanetHolder holder = new PlanetHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.vista, null);
            // Now we can fill the layout with the right values
            TextView placav = (TextView) v.findViewById(R.id.placa);
            TextView marcav = (TextView) v.findViewById(R.id.marca);
            TextView fechav = (TextView) v.findViewById(R.id.fecha);
            TextView costov = (TextView) v.findViewById(R.id.costo);
            TextView matriculadov = (TextView) v.findViewById(R.id.matriculado);
            TextView colorv = (TextView) v.findViewById(R.id.color);
            TextView reservado = (TextView) v.findViewById(R.id.reservado);

            ImageView fot = (ImageView) v.findViewById(R.id.img);

            holder.placa=placav;
            holder.marca=marcav;
            holder.fecha=fechav;
            holder.costo=costov;
            holder.matriculado=matriculadov;
            holder.color=colorv;
            holder.reservado=reservado;
            holder.foto = fot;

            v.setTag(holder);
        }
        else
            holder = (PlanetHolder) v.getTag();

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        Vehiculo p = listavehiculos.get(position);
        holder.placa.setText("Placa: "+p.getPlaca());
        holder.marca.setText("Marca: " + p.getMarca());
        holder.fecha.setText("Fecha: "+simpleDate.format(p.getFecFab()));
        holder.costo.setText("Costo: "+p.getCosto());
        holder.matriculado.setText("Matriculado: " + (p.getMatriculado()?"si":"no"));
        holder.color.setText("Color: "+p.getColor());
        holder.reservado.setText("Reservado: "+ (p.getEstado()?"si":"no"));

        MyDbHelper mHelper = new MyDbHelper(getContext());
        SQLiteDatabase mDb = mHelper.getReadableDatabase();

        Cursor cursor = mDb.rawQuery("SELECT foto FROM vehiculo WHERE placa = ?", new String[]{p.getPlaca()});

        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            holder.foto.setImageBitmap(getBitmapFromString(cursor.getString(0)));
        }
        cursor.close();
        mDb.close();

        return v;
    }


    private Bitmap getBitmapFromString(String jsonString) {
        // String a Bitmap
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }


	/* *********************************
	 * We use the holder pattern
	 * It makes the view faster and avoid finding the component
	 * **********************************/

    private static class PlanetHolder {
        public TextView placa;
        public TextView marca;
        public TextView fecha;
        public TextView costo;
        public TextView matriculado;
        public TextView reservado;
        public TextView color;
        public ImageView foto;
    }

}
