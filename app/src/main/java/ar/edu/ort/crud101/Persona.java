package ar.edu.ort.crud101;

/**
 * Created by leandro on 5/18/17.
 */

public class Persona {
    int Id;
    String Nombre;
    String FechaNac;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getFechaNac() {
        return FechaNac;
    }

    public void setFechaNac(String fechaNac) {
        FechaNac = fechaNac;
    }
}
