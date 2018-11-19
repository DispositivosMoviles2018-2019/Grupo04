package ec.edu.uce.proyecto_final_1h_g04.controlador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ec.edu.uce.proyecto_final_1h_g04.R;
import ec.edu.uce.proyecto_final_1h_g04.modelo.Vehiculo;

public class VehiculoControlador extends ArrayAdapter<Vehiculo> {

    private List<Vehiculo> listavehiculos;
    private Context ctx;

    public VehiculoControlador(List<Vehiculo> listavehiculos, Context ctx) {
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


            holder.placa=placav;
            holder.marca=marcav;
            holder.fecha=fechav;
            holder.costo=costov;
            holder.matriculado=matriculadov;
            holder.color=colorv;

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

        return v;
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
        public TextView color;
    }


}
