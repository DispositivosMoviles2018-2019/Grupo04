package ec.edu.uce.ex2h_g04.modelo;



public class Globales {
    private static Globales instance;

    // Global variable
    private Usuario user;
    private Vehiculo vehiculo;

    // Restrict the constructor from being instantiated
    private Globales(){}

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public static synchronized Globales getInstance(){
        if(instance==null){
            instance=new Globales();
        }
        return instance;
    }
}
