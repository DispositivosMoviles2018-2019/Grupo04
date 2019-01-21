package ec.edu.uce.final_2h_g04.vista;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ec.edu.uce.final_2h_g04.R;
import ec.edu.uce.final_2h_g04.modelo.Globales;
import ec.edu.uce.final_2h_g04.modelo.MyDbHelper;
import ec.edu.uce.final_2h_g04.modelo.Usuario;

import static android.Manifest.permission.READ_CONTACTS;

public class Registro extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private Usuario usuario;
    MyDbHelper mHelper;
    SQLiteDatabase mDb;

    ArrayList<Usuario> listaUsuario;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "admin:admin"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView regres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usuario = new Usuario();

        mHelper = new MyDbHelper(this);

        listaUsuario = new ArrayList<Usuario>();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        regres = (TextView) findViewById(R.id.regresarL);
        regres.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                vLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        //cargar los usuarios
        mDb = mHelper.getReadableDatabase();

        String query = "SELECT _id, usuario, clave FROM " + MyDbHelper.EsquemaUsuario.ColumnasUsuario.TABLE_NAME;
        Log.e("QUERY", query);
        Cursor mCursor = mDb.rawQuery(query, null);
        Usuario user;

        if (mCursor.getCount() > 0) {
            for (int i = 0; i < mCursor.getCount(); i++) {
                mCursor.moveToPosition(i);
                Integer numeroReserva = mCursor.getInt(0);
                String usuario = mCursor.getString(1);
                String clave = mCursor.getString(2);

                user = new Usuario(numeroReserva, usuario, clave);
                listaUsuario.add(user);
            }
            mCursor.close();
            mDb.close();
        }
    }

  /*  public void listarUsuarios(View view) {

        Usuario usuario;
        String data="";
        MostrarDatos mostrarDatos = new MostrarDatos();

        for (int i=0; i<listaUsuario.size(); i++) {
            usuario = listaUsuario.get(i);
            data += usuario.getId() +" "+usuario.getUsuario()+"\n";
        }


        //lanzamos la actividad de mostrar datos
        Intent intent = new Intent(getApplicationContext(), MostrarDatos.class);
        intent.putExtra("titulo", "LISTA DE USUARIOS REGISTRADOS");
        intent.putExtra("data", data);
        startActivity(intent);
    }*/

/*    public void respaldo(View view) {
        UsuarioControl userC = new UsuarioControl();
        String name = userC.guardarJson(listaUsuario);
        Toast.makeText(getApplicationContext(), "Ok, respaldo usuarios\n"+name, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("ResourceType")
    public void configuracion(View view) {
        //Load the preference defaults
        PreferenceManager.setDefaultValues(this, R.layout.preferencias, false);
        Intent i = new Intent(this, Preferencias.class);
        startActivity(i);
    }*/

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError("La clave es muy corta la longitud minima es 3 caracteres");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        //return email.contains("henry")|| email.contains("tipantuna");
        return true;
    }

    private boolean isPasswordValid(String password) {
        Pattern restriccion = Pattern.compile("(?:.*[a-zA-Z0-9])");
        Matcher validar = restriccion.matcher(password);

        if (!validar.matches())
            return false;

        if (password.length() < 3)
            return false;
        //return password.length() > 4;
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(Registro.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        public boolean verificarExiste() {
            MyDbHelper mHelper = new MyDbHelper(getApplicationContext());
            SQLiteDatabase mDb = mHelper.getReadableDatabase();

            String columns[] = new String[]{"_id"};
            String selection = MyDbHelper.EsquemaUsuario.ColumnasUsuario.NAME + " =? AND " + MyDbHelper.EsquemaUsuario.ColumnasUsuario.PASS + "=?";
            String selectionArgs[] = new String[]{mEmail.toString(), mPasswordView.getText().toString()};

            Cursor c = mDb.query(
                    MyDbHelper.EsquemaUsuario.ColumnasUsuario.TABLE_NAME,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            //si encontro un registro entonces si existe el usuario y clave ingresados
            if (c.getCount() > 0) {

                //guardamos los datos del usuario que se ha logueado exitosamente
                c.moveToFirst();
                usuario.setId(c.getInt(0));
                usuario.setUsuario(mEmail.toString());
                usuario.setClave(mPasswordView.getText().toString());

                Globales g = Globales.getInstance();
                g.setUser(usuario);

                c.close();
                mDb.close();
                return true;
            } else
                c.close();
            mDb.close();
            return false;

        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                if (verificarExiste()) {
                    Toast.makeText(getApplicationContext(), "Usuario Registrado", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Intenta con otro usuario", Toast.LENGTH_SHORT).show();
                } else {

                    //Toast.makeText(getApplicationContext(), "Usuario NO Registrado", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Registro.this);
                    dialogo1.setMessage("¿Desea registrarse con los datos que ha ingresado?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                            MyDbHelper mHelper = new MyDbHelper(getApplicationContext());
                            SQLiteDatabase mDb;

                            mDb = mHelper.getWritableDatabase();

                            // Contenedor de valores
                            ContentValues values = new ContentValues();

                            // Pares clave-valor
                            values.put(MyDbHelper.EsquemaUsuario.ColumnasUsuario.NAME, mEmail.toString());
                            values.put(MyDbHelper.EsquemaUsuario.ColumnasUsuario.PASS, mPasswordView.getText().toString());
                            // Insertar...
                            mDb.insert(MyDbHelper.EsquemaUsuario.ColumnasUsuario.TABLE_NAME, null, values);

                            mHelper.close();
                            mDb.close();
                            Toast.makeText(getApplicationContext(), "Ok!, nuevo usuario agregado", Toast.LENGTH_LONG).show();

                            //recuperamos el id del usuario guardado
                            mHelper = new MyDbHelper(getApplicationContext());
                            mDb = mHelper.getReadableDatabase();

                            String columns[] = new String[]{"_id"};
                            String selection = MyDbHelper.EsquemaUsuario.ColumnasUsuario.NAME + " =? AND " + MyDbHelper.EsquemaUsuario.ColumnasUsuario.PASS + "=?";
                            String selectionArgs[] = new String[]{mEmail.toString(), mPasswordView.getText().toString()};

                            Cursor c = mDb.query(
                                    MyDbHelper.EsquemaUsuario.ColumnasUsuario.TABLE_NAME,
                                    columns,
                                    selection,
                                    selectionArgs,
                                    null,
                                    null,
                                    null
                            );

                            //si encontro un registro entonces si existe el usuario y clave ingresados
                            if (c.getCount() > 0) {
                                //guardamos los datos del usuario que se ha logueado exitosamente
                                c.moveToFirst();
                                usuario.setId(c.getInt(0));
                                usuario.setUsuario(mEmail.toString());
                                usuario.setClave(mPasswordView.getText().toString());

                                Globales g = Globales.getInstance();
                                g.setUser(usuario);
                            }
                            c.close();
                            mDb.close();
                        }
                    });
                    dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo1.show();

                }


            } else {
                Toast.makeText(getApplicationContext(), "Usuario Registrado", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Intenta con otro usuario", Toast.LENGTH_SHORT).show();
            }

        }
        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);


        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public void vLogin() {
        Intent intent = new Intent(Registro.this, LoginActivity.class);
        startActivity(intent);

    }
}

