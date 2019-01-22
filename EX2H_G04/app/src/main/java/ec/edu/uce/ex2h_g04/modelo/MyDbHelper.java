package ec.edu.uce.ex2h_g04.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.BaseColumns;
import android.support.v4.content.ContextCompat;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import ec.edu.uce.ex2h_g04.R;


/**

 */

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "BASE-TOTAL-SQL.db";
    private static final int DB_VERSION = 1;

    private static final String STRING_CREATE_USER =
            "CREATE TABLE usuario (_id INTEGER PRIMARY KEY AUTOINCREMENT, usuario TEXT, clave TEXT);";
    private static final String STRING_CREATE_VEHICULO =
            "CREATE TABLE vehiculo (_id INTEGER PRIMARY KEY AUTOINCREMENT, placa TEXT, marca TEXT, fecFab DATE, costo REAL, matriculado INTEGER, color TEXT, foto TEXT, estado INTEGER);";
    private static final String STRING_CREATE_RESERVA =
            "CREATE TABLE reserva (_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, celular TEXT, fecRes DATE, fecEnt DATE, valor REAL, user INTEGER, placa TEXT);";

    Context contxt;
    public MyDbHelper(Context context) {
        super(context, Environment.getExternalStorageDirectory().getAbsolutePath() + "/MIS_DATOS_VEHICULOS/" +DB_NAME, null, DB_VERSION);
        contxt = context;
    }

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creamos las tablas
        db.execSQL(STRING_CREATE_USER);
        db.execSQL(STRING_CREATE_VEHICULO);
        db.execSQL(STRING_CREATE_RESERVA);

        String insert;

        //VEHICULO
        Drawable drawable = ContextCompat.getDrawable(contxt, R.drawable.auto2);

        //grabar nuevo registro a la base de datos
        // Valores
        ContentValues values = new ContentValues();
        values.put(EsquemaVehiculo.ColumnasVehiculo.PLACA, "QWE-6784");
        values.put(EsquemaVehiculo.ColumnasVehiculo.MARCA, "Nissan");
        values.put(EsquemaVehiculo.ColumnasVehiculo.FECFAB, "2015-01-01");
        values.put(EsquemaVehiculo.ColumnasVehiculo.COSTO, 30000.0);
        values.put(EsquemaVehiculo.ColumnasVehiculo.MATRICULADO, 0);
        values.put(EsquemaVehiculo.ColumnasVehiculo.COLOR, "Negro");
        values.put(EsquemaVehiculo.ColumnasVehiculo.FOTO, getStringFromBitmap(((BitmapDrawable)drawable).getBitmap()));
        values.put(EsquemaVehiculo.ColumnasVehiculo.ESTADO, 0);

        db.insert(
                EsquemaVehiculo.ColumnasVehiculo.TABLE_NAME,
                null,
                values);

        drawable = ContextCompat.getDrawable(contxt, R.drawable.auto2);
        values = new ContentValues();
        values.put(EsquemaVehiculo.ColumnasVehiculo.PLACA, "ASD-9999");
        values.put(EsquemaVehiculo.ColumnasVehiculo.MARCA, "Lada");
        values.put(EsquemaVehiculo.ColumnasVehiculo.FECFAB, "2014-01-01");
        values.put(EsquemaVehiculo.ColumnasVehiculo.COSTO, 12200.0);
        values.put(EsquemaVehiculo.ColumnasVehiculo.MATRICULADO, 1);
        values.put(EsquemaVehiculo.ColumnasVehiculo.COLOR, "Otro");
        values.put(EsquemaVehiculo.ColumnasVehiculo.FOTO, getStringFromBitmap(((BitmapDrawable)drawable).getBitmap()));
        values.put(EsquemaVehiculo.ColumnasVehiculo.ESTADO, 0);

        db.insert(
                EsquemaVehiculo.ColumnasVehiculo.TABLE_NAME,
                null,
                values);

        drawable = ContextCompat.getDrawable(contxt, R.drawable.icon);
        values = new ContentValues();
        values.put(EsquemaVehiculo.ColumnasVehiculo.PLACA, "FGH-5555");
        values.put(EsquemaVehiculo.ColumnasVehiculo.MARCA, "Chevrolet");
        values.put(EsquemaVehiculo.ColumnasVehiculo.FECFAB, "2014-01-01");
        values.put(EsquemaVehiculo.ColumnasVehiculo.COSTO, 12000.0);
        values.put(EsquemaVehiculo.ColumnasVehiculo.MATRICULADO, 1);
        values.put(EsquemaVehiculo.ColumnasVehiculo.COLOR, "Otro");
        values.put(EsquemaVehiculo.ColumnasVehiculo.FOTO, getStringFromBitmap(((BitmapDrawable)drawable).getBitmap()));
        values.put(EsquemaVehiculo.ColumnasVehiculo.ESTADO, 1);

        db.insert(
                EsquemaVehiculo.ColumnasVehiculo.TABLE_NAME,
                null,
                values);

        drawable = ContextCompat.getDrawable(contxt, R.drawable.auto2);
        values = new ContentValues();
        values.put(EsquemaVehiculo.ColumnasVehiculo.PLACA, "UIO-1111");
        values.put(EsquemaVehiculo.ColumnasVehiculo.MARCA, "Honda");
        values.put(EsquemaVehiculo.ColumnasVehiculo.FECFAB, "2017-01-01");
        values.put(EsquemaVehiculo.ColumnasVehiculo.COSTO, 19000.0);
        values.put(EsquemaVehiculo.ColumnasVehiculo.MATRICULADO, 1);
        values.put(EsquemaVehiculo.ColumnasVehiculo.COLOR, "Blanco");
        values.put(EsquemaVehiculo.ColumnasVehiculo.FOTO, getStringFromBitmap(((BitmapDrawable)drawable).getBitmap()));
        values.put(EsquemaVehiculo.ColumnasVehiculo.ESTADO, 0);

        db.insert(
                EsquemaVehiculo.ColumnasVehiculo.TABLE_NAME,
                null,
                values);

        drawable = ContextCompat.getDrawable(contxt, R.drawable.auto2);
        values = new ContentValues();
        values.put(EsquemaVehiculo.ColumnasVehiculo.PLACA, "BNM-3333");
        values.put(EsquemaVehiculo.ColumnasVehiculo.MARCA, "Ford");
        values.put(EsquemaVehiculo.ColumnasVehiculo.FECFAB, "2017-01-01");
        values.put(EsquemaVehiculo.ColumnasVehiculo.COSTO, 22000.0);
        values.put(EsquemaVehiculo.ColumnasVehiculo.MATRICULADO, 1);
        values.put(EsquemaVehiculo.ColumnasVehiculo.COLOR, "Otro");
        values.put(EsquemaVehiculo.ColumnasVehiculo.FOTO, getStringFromBitmap(((BitmapDrawable)drawable).getBitmap()));
        values.put(EsquemaVehiculo.ColumnasVehiculo.ESTADO, 0);

        db.insert(
                EsquemaVehiculo.ColumnasVehiculo.TABLE_NAME,
                null,
                values);

        drawable = ContextCompat.getDrawable(contxt, R.drawable.icon);
        values = new ContentValues();
        values.put(EsquemaVehiculo.ColumnasVehiculo.PLACA, "WWE-6666");
        values.put(EsquemaVehiculo.ColumnasVehiculo.MARCA, "Chevrolet");
        values.put(EsquemaVehiculo.ColumnasVehiculo.FECFAB, "2017-01-01");
        values.put(EsquemaVehiculo.ColumnasVehiculo.COSTO, 17000.0);
        values.put(EsquemaVehiculo.ColumnasVehiculo.MATRICULADO, 0);
        values.put(EsquemaVehiculo.ColumnasVehiculo.COLOR, "Negro");
        values.put(EsquemaVehiculo.ColumnasVehiculo.FOTO, getStringFromBitmap(((BitmapDrawable)drawable).getBitmap()));
        values.put(EsquemaVehiculo.ColumnasVehiculo.ESTADO, 1);

        db.insert(
                EsquemaVehiculo.ColumnasVehiculo.TABLE_NAME,
                null,
                values);



        //USUARIO
        insert = "INSERT INTO "+ EsquemaUsuario.ColumnasUsuario.TABLE_NAME+" ("+ EsquemaUsuario.ColumnasUsuario.NAME+", "+ EsquemaUsuario.ColumnasUsuario.PASS+") VALUES (\"admin\",\"admin\");";
        db.execSQL(insert);
        insert = "INSERT INTO "+ EsquemaUsuario.ColumnasUsuario.TABLE_NAME+" ("+ EsquemaUsuario.ColumnasUsuario.NAME+", "+ EsquemaUsuario.ColumnasUsuario.PASS+") VALUES (\"Carlos Largo\",\"carlos\");";
        db.execSQL(insert);
        insert = "INSERT INTO "+ EsquemaUsuario.ColumnasUsuario.TABLE_NAME+" ("+ EsquemaUsuario.ColumnasUsuario.NAME+", "+ EsquemaUsuario.ColumnasUsuario.PASS+") VALUES (\"Bryan Catucuamba\",\"bryan\");";
        db.execSQL(insert);
        insert = "INSERT INTO "+ EsquemaUsuario.ColumnasUsuario.TABLE_NAME+" ("+ EsquemaUsuario.ColumnasUsuario.NAME+", "+ EsquemaUsuario.ColumnasUsuario.PASS+") VALUES (\"Diego Navarrete\",\"diego\");";
        db.execSQL(insert);
        insert = "INSERT INTO "+ EsquemaUsuario.ColumnasUsuario.TABLE_NAME+" ("+ EsquemaUsuario.ColumnasUsuario.NAME+", "+ EsquemaUsuario.ColumnasUsuario.PASS+") VALUES (\"Juan Perez\",\"juan\");";
        db.execSQL(insert);

        //RESERVAS
        insert = "INSERT INTO "+ EsquemaReserva.ColumnasReserva.TABLE_NAME+" ("+ EsquemaReserva.ColumnasReserva.EMAIL+", "+ EsquemaReserva.ColumnasReserva.CELULAR+", "+ EsquemaReserva.ColumnasReserva.FECRES+", "+ EsquemaReserva.ColumnasReserva.FECENT+", "+ EsquemaReserva.ColumnasReserva.VALOR+", "+ EsquemaReserva.ColumnasReserva.USER+", "+ EsquemaReserva.ColumnasReserva.PLACA+") VALUES (\"user@hotmail.com\",\"0983444123\",\"2018-02-06\",\"2018-02-07\",80.0, 2,\"FGH-5555\");";
        db.execSQL(insert);
        insert = "INSERT INTO "+ EsquemaReserva.ColumnasReserva.TABLE_NAME+" ("+ EsquemaReserva.ColumnasReserva.EMAIL+", "+ EsquemaReserva.ColumnasReserva.CELULAR+", "+ EsquemaReserva.ColumnasReserva.FECRES+", "+ EsquemaReserva.ColumnasReserva.FECENT+", "+ EsquemaReserva.ColumnasReserva.VALOR+", "+ EsquemaReserva.ColumnasReserva.USER+", "+ EsquemaReserva.ColumnasReserva.PLACA+") VALUES (\"user2@hotmail.com\",\"0983444144\",\"2018-02-06\",\"2018-02-07\",80.0, 3,\"WWE-6666\");";
        db.execSQL(insert);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public String getDbName() { return DB_NAME;}

    /**
     * Esquema de la tabla
     */
    public class EsquemaUsuario {

        public abstract class ColumnasUsuario implements BaseColumns {
            public static final String TABLE_NAME ="usuario";

            public static final String ID = "_id";
            public static final String NAME = "usuario";
            public static final String PASS = "clave";
        }
    }

    /**
     * Esquema de la tabla
     */
    public class EsquemaVehiculo {

        public abstract class ColumnasVehiculo implements BaseColumns {
            public static final String TABLE_NAME ="vehiculo";

            public static final String ID = "_id";
            public static final String PLACA = "placa";
            public static final String MARCA = "marca";
            public static final String FECFAB = "fecFab";
            public static final String COSTO = "costo";
            public static final String MATRICULADO = "matriculado";
            public static final String COLOR = "color";
            public static final String FOTO = "foto";
            public static final String ESTADO = "estado";
        }
    }

    /**
     * Esquema de la tabla
     */
    public class EsquemaReserva {

        public abstract class ColumnasReserva implements BaseColumns {
            public static final String TABLE_NAME ="reserva";

            public static final String ID = "_id";
            public static final String EMAIL = "email";
            public static final String CELULAR = "celular";
            public static final String FECRES = "fecRes";
            public static final String FECENT = "fecEnt";
            public static final String VALOR = "valor";
            public static final String USER = "user";
            public static final String PLACA = "placa";
        }
    }
}