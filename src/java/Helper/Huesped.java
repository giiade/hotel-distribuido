/*
 * Sistemas distribuidos - Practica Hotel
 * Universidad Rey Juan Carlos, Mostoles
 * Realizada por Julio Lopez González y Manuel Gómez Pérez
 * Doble Grado GII + ADE
 * https://github.com/giiade/hotel-distribuido
 */
package Helper;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase es la que representa el objeto Huesped que estará formado por lo
 * siguiente: Obligatorios Nombre, Apellido, nif, nacimiento,
 * domicilio(direccion, cp, localidad, provincia) Opcionales: Fijo, movil,
 * correo
 *
 *
 */
public class Huesped {
    
    @XStreamOmitField
    final private String LOG = this.getClass().getSimpleName();
    //Variables
    private String nombre;
    private String apellidos;
    private String nif;
    private Date nacimiento;
//    @XStreamImplicit(keyFieldName = "dirección", itemFieldName = "dirección")
    HashMap<String, String> domicilio;

    //Opcionales
    private String fijo;
    private String movil;
    private String correo;

    public Huesped(String nombre, String apellidos, String nif, String nacimiento, String direccion, String cp, String localidad,
            String municipio, String fijo, String movil, String correo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nif = nif;
        try {
            this.nacimiento = Constantes.DATE_FORMAT.parse(nacimiento);
        } catch (ParseException ex) {
            Logger.getLogger(Huesped.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setDomicilio(direccion, cp, localidad, municipio);
        this.fijo = fijo;
        this.movil = movil;
        this.correo = correo;
    }

    public Huesped(String nombre, String apellidos, String nif, String nacimiento) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nif = nif;
        try {
            this.nacimiento = Constantes.DATE_FORMAT.parse(nacimiento);
        } catch (ParseException ex) {
            Logger.getLogger(Huesped.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Huesped() {
        this.nombre = "Jose";
        this.apellidos = "Perez Perez";
        this.nif = "12345678A";
        try {
            this.nacimiento = Constantes.DATE_FORMAT.parse("15/01/1990");
        } catch (ParseException ex) {
            Logger.getLogger(LOG).log(Level.SEVERE, "Error parseando Constructor Vacio \n{0}", ex.getMessage());
        }
        this.movil = "673214356";
        this.setDomicilio("Rio Manzano", "27890", "Pozuelo", "Madrid");
    }

    /**
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre el nombre a guardar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return apellidos
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * @param apellidos apellidos a guardar
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * @return nif
     */
    public String getNif() {
        return nif;
    }

    /**
     * @param nif el nif a guardar
     */
    public void setNif(String nif) {
        this.nif = nif;
    }

    /**
     * @return nacimiento(dd/MM/YYYY)
     */
    public Date getNacimiento() {
        return nacimiento;
    }

    /**
     * @param nacimiento a guardar(dd/MM/YYYY)
     */
    public void setNacimiento(String nacimiento) {
        try {
            this.nacimiento = Constantes.DATE_FORMAT.parse(nacimiento);
        } catch (ParseException ex) {
            Logger.getLogger(LOG).log(Level.SEVERE, "Error parseando Constructor Vacio \n{0}", ex.getMessage());
        }
    }

    /**
     * @return fijo código de localidad-7 cifras)
     */
    public String getFijo() {
        return fijo;
    }

    /**
     * @param fijo a guardar
     */
    public void setFijo(String fijo) {
        this.fijo = fijo;
    }

    /**
     * @return movil
     */
    public String getMovil() {
        return movil;
    }

    /**
     * @param movil a guardar
     */
    public void setMovil(String movil) {
        this.movil = movil;
    }

    /**
     * @return correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param correo a guardar
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Guarda la dirección
     *
     * @param direccion
     * @param cp
     * @param municipio
     * @param localidad
     */
    public void setDomicilio(String direccion, String cp, String municipio, String localidad) {
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put(Constantes.ADDRESS_KEY, direccion);
        hashmap.put(Constantes.CP_KEY, cp);
        hashmap.put(Constantes.PROVINCIA_KEY, municipio);
        hashmap.put(Constantes.LOCALIDAD_KEY, localidad);
        domicilio = hashmap;
    }

    public HashMap<String, String> getDomicilio() {
        return domicilio;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Huesped)) return false;
        Huesped aux = (Huesped) obj;
        return this.getNif().equals(aux.getNif()); 
        //return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder();
        String coma = ", ";
        String punto= ".";
        String newline = "\n";
        resultado.append("Datos del Huesped: ").append(newline);
        resultado.append("  Nombre: " ).append(this.nombre).append(punto).append(newline);
        resultado.append("  Apellidos: " ).append(this.apellidos).append(punto).append(newline);
        resultado.append("  Fecha de nacimiento: ").append(this.nacimiento).append(punto).append(newline);
        resultado.append("  NIF: ").append(this.nif).append(punto).append(newline);
        resultado.append("  Dirección: ").append(this.domicilio.get("direccion")).append(coma).append(this.domicilio.get("municipio")).append(coma).append(this.domicilio.get("cp")).append(coma).append(this.domicilio.get("localidad")).append(punto).append(newline);
        if (this.fijo != null) {
            resultado.append("  Fijo: ").append(this.fijo).append(punto).append(newline);
        } else {
            resultado.append("  No hay número de fijo registrado").append(newline);
        }
        if (this.movil !=null) {
            resultado.append("  Móvil: ").append(this.movil).append(punto).append(newline);
        } else {
            resultado.append("  No hay número de fijo registrado").append(punto).append(newline);
        }
        if (this.correo != null) {
            resultado.append("  Correo: ").append(this.correo).append(punto).append(newline);
        } else {
            resultado.append("  No hay dirección de correo electrónico registrada").append(punto).append(newline);
        }
        

        return resultado.toString();

    }

}
