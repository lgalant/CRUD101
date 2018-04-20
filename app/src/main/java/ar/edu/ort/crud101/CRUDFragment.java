package ar.edu.ort.crud101;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.net.ResponseCache;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class CRUDFragment extends Fragment implements View.OnClickListener{

    DialogFragment df;
    EditText id_vw,nombre_vw,fechanac_vw;
    public CRUDFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crud, container, false);

        Button get = (Button) v.findViewById(R.id.btnGet);
        Button post = (Button) v.findViewById(R.id.btnPost);
        Button put = (Button) v.findViewById(R.id.btnPut);
        Button delete = (Button) v.findViewById(R.id.btnDelete);
        Button viewAll = (Button) v.findViewById(R.id.viewAll);


        id_vw = (EditText) v.findViewById(R.id.id);
        nombre_vw = (EditText) v.findViewById(R.id.nombre);
        fechanac_vw = (EditText) v.findViewById(R.id.fechanac);

        get.setOnClickListener(this);
        post.setOnClickListener(this);
        put.setOnClickListener(this);
        delete.setOnClickListener(this);
        viewAll.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {;
        GsonBuilder builder;
        Gson gson;
        Persona p;
        String urlDeApi = "http://templateapiort.azurewebsites.net/api/persona/";
        switch (v.getId()) {
            case R.id.btnGet:
                String urlGet = urlDeApi+ id_vw.getText().toString();
                Log.d("Get", urlGet);
                new ConectarAPITask().execute("GET", urlGet);
                break;
            case R.id.btnPost:
                p = new Persona();
                p.setId(Integer.valueOf(id_vw.getText().toString()));
                p.setNombre(nombre_vw.getText().toString());
                p.setFechaNac(fechanac_vw.getText().toString());
                builder = new GsonBuilder();
                gson = builder.create();
                System.out.println(gson.toJson(p));

                new ConectarAPITask().execute("POST",urlDeApi, gson.toJson(p));
                break;
            case R.id.btnPut:
                p = new Persona();
                p.setId(Integer.valueOf(id_vw.getText().toString()));
                p.setNombre(nombre_vw.getText().toString());
                p.setFechaNac(fechanac_vw.getText().toString());
                builder = new GsonBuilder();
                gson = builder.create();
                System.out.println(gson.toJson(p));

                new ConectarAPITask().execute("PUT",urlDeApi, gson.toJson(p));
                break;
            case R.id.btnDelete:
                Log.d("Push", "delete");
                break;
            case R.id.viewAll:
                Log.d("Get All", urlDeApi);
                FragmentManager fm = getFragmentManager();
                ViewAllFragment viewAllFragment = new ViewAllFragment();
                viewAllFragment.show(fm,"View all people");
                break;

        }
    }


    private class ConectarAPITask extends AsyncTask<String, Void,  Persona> {
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected Persona doInBackground(String... params) {

            String method = params[0];
            String urlApi = params[1];
            String resultado;

            if (method.equals("GET")) {
                return  getPersona(urlApi);
            }


            if (method.equals("PUT")) {
                String json = params[2];
                putPersona(urlApi, json);
            }

            if (method.equals("POST")) {
                String json = params[2];
                postPersona(urlApi, json);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Persona persona) {
            super.onPostExecute(persona);
            //Log.d("ope :",persona.getNombre());
            if (persona != null) {
                nombre_vw.setText(persona.getNombre());
                fechanac_vw.setText(persona.getFechaNac());
            }

        }


        // PUT implies putting a resource - completely replacing whatever is available at the given URL with a different thing.
        // By definition, a PUT is idempotent. Do it as many times as you like, and the result is the same. x=5 is idempotent.
        // You can PUT a resource whether it previously exists, or not (eg, to Create, or to Update)!
        // UPDATE
        private void putPersona(String urlApi, String json) {

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(urlApi)
                    .put(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                Log.d("response:" , response.toString());
                return;
            } catch (Exception e) {
                Log.d("Error :", e.getMessage());
                return;

            }

        }

        // POST updates a resource, adds a subsidiary resource, or causes a change.
        // A POST is not idempotent, in the way that x++ is not idempotent.
        // INSERT
        private void postPersona(String urlApi, String json) {

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(urlApi)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return;
            } catch (IOException e) {
                Log.d("Error :", e.getMessage());
                return;

            }
        }

        private Persona getPersona(String urlApi) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urlApi)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                Persona persona = parsearResultado(response.body().string());
                return persona;
            }
            catch (IOException e){
                Log.d("Error :", e.getMessage());
                return null;

            }
        }


        private Persona parsearResultado(String respuesta)   {
            if (respuesta == null || respuesta.length()==0)
                return null;

            Log.d("Respuesta:", respuesta);

            try {
                Gson gson = new Gson();
                Persona p = gson.fromJson(respuesta, Persona.class);
                Log.d("Persona nombre:", p.getNombre());
                return p;

            }
            catch (Exception e) {
                Log.d("Error :", e.getMessage());
                return null;
            }

        }

    }
}
