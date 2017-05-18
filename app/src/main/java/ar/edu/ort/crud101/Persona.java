package ar.edu.ort.crud101;

/**
 * Created by leandro on 5/18/17.
 */

public class Persona {
    private String Id;
    private String Nombre;
    private String FechaNac;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
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
