/*
 * Sistemas distribuidos - Practica Hotel
 * Universidad Rey Juan Carlos, Mostoles
 * Realizada por Julio Lopez González y Manuel Gómez Pérez
 * Doble Grado GII + ADE
 * https://github.com/giiade/hotel-distribuido
 */
package Helper;

import java.text.SimpleDateFormat;

/**
 *This class will have all the constast that we will need in the program
 * @author JulioLopez
 */
public class Constantes {
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    
    //Variables para llamar al Servidor
    
    //Huesped
    public final static String     NAME_KEY = "nombre";
    public final static String     SURNAME_KEY= "apellidos";
    public final static String     NIF_KEY = "NIF";
    public final static String     BIRTHDATE_KEY = "nacimiento";
    public final static String     ADDRESS_KEY = "dirección";
    public final static String     LOCALIDAD_KEY = "localidad";
    public final static String     PROVINCIA_KEY = "provincia"; //Provincia
    public final static String     CP_KEY = "CP";
    public final static String     MOVIL_KEY = "movil";
    public final static String     FIJO_KEY = "fijo";
    public final static String     MAIL_KEY = "mail";
    
    //Constantes para clase Reserva

    public final static String  HUESPED_KEY = "huesped";
    public final static String  HABITACION_KEY = "habitacion";
    public final static String  FENTRADA_KEY = "entrada";
    public final static String  FSALIDA_KEY = "salida";
    
    
    //Cabecera XML
    public static final String XML_HEADER = "<?xml version\"1.0\" encoding=\"UTF-8\"?>";
    

}
