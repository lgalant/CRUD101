package ar.edu.ort.crud101;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewAllFragment extends DialogFragment implements View.OnClickListener{

    PersonasAdapter personasAdapter;

    public ViewAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_view_all, container, false);

        ListView lv = (ListView) v.findViewById(R.id.personList);
        personasAdapter = new PersonasAdapter(getContext(),new ArrayList<Persona>());
        lv.setAdapter(personasAdapter);

        Button salir = (Button) v.findViewById(R.id.salir);
        salir.setOnClickListener(this);


        String urlDeApi = "http://templateapiort.azurewebsites.net/api/persona/";
        new ConectarAPITask().execute(urlDeApi);
        return v;
    }


    private class ConectarAPITask extends AsyncTask<String, Void,  Persona[]> {
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected Persona[] doInBackground(String... params) {

            String urlApi = params[0];
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urlApi)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                Persona[] personas = parsearResultado(response.body().string());
                return personas;
            }
            catch (IOException e){
                Log.d("Error :", e.getMessage());
                return null;

            }

        }

        @Override
        protected void onPostExecute(Persona[] personas) {
            super.onPostExecute(personas);
            personasAdapter.setPersonas(personas);
            personasAdapter.notifyDataSetChanged();

        }



        private Persona[] parsearResultado(String respuesta)   {
            if (respuesta == null || respuesta.length()==0)
                return null;

            Log.d("Respuesta:", respuesta);

            try {
                Gson gson = new Gson();
                Persona[] p = gson.fromJson(respuesta, Persona[].class);
                //Log.d("Persona nombre:", p.getNombre());
                return p;

            }
            catch (Exception e) {
                Log.d("Error :", e.getMessage());
                return null;
            }

        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.salir)
            dismiss();
    }
}
