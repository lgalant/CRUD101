package ar.edu.ort.crud101;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by leandro on 6/6/17.
 */

class PersonasAdapter  extends BaseAdapter {

    ArrayList<Persona> personas;
    Context context;

    PersonasAdapter(Context context, ArrayList<Persona> personas) {
        this.personas = personas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return personas.size();
    }

    @Override
    public Object getItem(int position) {
        return personas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setPersonas(Persona[] personas) {
        this.personas = new ArrayList<>(Arrays.asList(personas)); // Convierte array en ArrayList
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
        }

        TextView personID = (TextView) view.findViewById(R.id.personID);
        TextView nombre = (TextView) view.findViewById(R.id.nombre);
        TextView fechanac = (TextView) view.findViewById(R.id.fechanac);

        Persona p = personas.get(position);
        personID.setText(String.valueOf(p.getId()));
        nombre.setText(p.getNombre());
        fechanac.setText(p.getFechaNac());
        return view;

    }
}