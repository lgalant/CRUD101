package ar.edu.ort.crud101;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        id_vw = (EditText) v.findViewById(R.id.id);
        nombre_vw = (EditText) v.findViewById(R.id.nombre);
        fechanac_vw = (EditText) v.findViewById(R.id.fechanac);

        get.setOnClickListener(this);
        post.setOnClickListener(this);
        put.setOnClickListener(this);
        delete.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        String urlDeApi = "http://templateapiort.azurewebsites.net/api/persona/";
        switch (v.getId()) {
            case R.id.btnGet:
                String urlGet = urlDeApi+ id_vw.getText().toString();
                Log.d("Get", urlGet);
                new ConectarAPITask().execute("GET", urlGet);
                break;
            case R.id.btnPost:
                Persona p = new Persona();
                p.setId(id_vw.getText().toString());
                p.setNombre(nombre_vw.getText().toString());
                p.setFechaNac(fechanac_vw.getText().toString());
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                System.out.println(gson.toJson(p));

                new ConectarAPITask().execute("POST",urlDeApi, gson.toJson(p));
                Log.d("Push", "post");
                break;
            case R.id.btnPut:
                Log.d("Push", "put");
                break;
            case R.id.btnDelete:
                Log.d("Push", "delete");
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

            if (method.equals("GET"))
                getPersona(urlApi);

            if (method.equals("POST")) {
                String json = params[2];
                postPersona(urlApi, json);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Persona persona) {
            super.onPostExecute(persona);

            if (persona != null) {
                nombre_vw.setText(persona.getNombre());
                fechanac_vw.setText(persona.getFechaNac());
            }

        }


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
                Persona resultado = parsearResultado(response.body().string());
                return resultado;
            }
            catch (IOException |JSONException e){
                Log.d("Error :", e.getMessage());
                return null;

            }
        }


        private Persona parsearResultado(String respuesta)  throws JSONException {
            Log.d("Respuesta:", respuesta);
            Gson gson = new Gson();
            Persona p = gson.fromJson(respuesta,Persona.class);
            return p;
        }
    }
}
