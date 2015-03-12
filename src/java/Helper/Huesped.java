/*
 * Sistemas distribuidos - Practica Hotel
 * Universidad Rey Juan Carlos, Mostoles
 * Realizada por Julio Lopez González y Manuel Gómez Pérez
 * Doble Grado GII + ADE
 * https://github.com/giiade/hotel-distribuido
 */
package Helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Esta clase es la que representa el objeto Huesped que estará formado por lo 
 * siguiente:
 * Obligatorios
 *      Nombre, Apellido, nif, nacimiento, 
 *      domicilio(direccion, cp, localidad, provincia)
 * Opcionales:
 *      Fijo, movil, correo
 * 
 * 
 */
public class Huesped {
    
    //CONSTANTES --> Luego se meterán en una clase de constantes.
    final static String DIR_KEY = "direccion";
    final static String LOC_KEY = "localidad";
    final static String MUN_KEY = "municipio";
    final static String CP_KEY = "cp";
    
    //Variables
    private String nombre;
    private String apellidos;
    private String nif;
    private Date nacimiento;
    ArrayList<HashMap<String,String>> domicilio;
    
    //Opcionales
    private String fijo;
    private String movil;
    private String correo;

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
    public void setNacimiento(Date nacimiento) {
        this.nacimiento = nacimiento;
    }

    /**
     * @return fijo (codigo de localidad-7 cifras)
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
     * @return  movil
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
    
    public void setDomicilio(String direccion, String cp, String municipio, String localidad){
        HashMap<String,String> hashmap = new HashMap<>();
        hashmap.put(DIR_KEY, direccion);
        domicilio.add(hashmap);
        hashmap.put(CP_KEY, cp);
        domicilio.add(hashmap);
        hashmap.put(MUN_KEY, municipio);
        domicilio.add(hashmap);
        hashmap.put(LOC_KEY, localidad);
        domicilio.add(hashmap);
    }
    
    public ArrayList<HashMap<String,String>> getDomicilio(){
        return domicilio;
    }
    
    
    
    
}
