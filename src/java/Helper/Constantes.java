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
 *This class will have all the constants that we will need in the program
 * @author JulioLopez
 */
public class Constantes {


    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public final static SimpleDateFormat DATE_HTMLFORMAT = new SimpleDateFormat("yyyy-MM-dd");

    //Variables para llamar al Servidor
    //Huesped
    public final static String NAME_KEY = "nombre";
    public final static String SURNAME_KEY = "apellidos";
    public final static String NIF_KEY = "NIF";
    public final static String BIRTHDATE_KEY = "nacimiento";
    public final static String ADDRESS_KEY = "direccion";
    public final static String LOCALIDAD_KEY = "localidad";
    public final static String PROVINCIA_KEY = "provincia"; //Provincia
    public final static String CP_KEY = "CP";
    public final static String MOVIL_KEY = "movil";
    public final static String FIJO_KEY = "fijo";
    public final static String MAIL_KEY = "mail";

    //Constantes para clase Reserva
    public final static String HUESPED_KEY = "huesped";
    public final static String HABITACION_KEY = "habitacion";
    public final static String FENTRADA_KEY = "entrada";
    public final static String FSALIDA_KEY = "salida";
    
    //Constantes de uso general
    public final static String COMA = ", ";
    public final static String PUNTO= ".";        
    public final static String NEWLINE= "\n";
    

    //Constantes para la clase Cliente
    public final static String URL_KEY = "http://localhost:8080/Hotel_SD/hotelApp?operacion=";
    public final static String POST_KEY = "http://localhost:8080/Hotel_SD/hotelApp";
    
    
    //Cabecera XML
    public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    //Respuesta Web
    public static final String HTML_HEADER = "<html lang=\"es\">\n"
            + "    <head>\n"
            + "        <title>HotelSD</title>\n"
            + "        <meta charset=\"UTF-8\">\n"
            + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
            + "        <link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css\">\n"
            + "        <link href=\"css/footer.css\" rel=\"stylesheet\">\n"
            + "        <style type=\"text/css\"></style>\n"
            + "\n"
            + "        <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>\n"
            + "        <script src=\"http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.1.0/bootstrap.min.js\"></script>\n"
            + "\n"
            + "\n"
            + "    </head>\n"
            + "\n"
            + "    <body>\n"
            + "        <div class=\"container\">\n"
            + "            <div class=\"page-header\">\n"
            + "                <h1> Resultado </h1>\n"
            + "            </div>\n"
            + "<div class=\"col-sm-12\">";

    public static final String HTML_FOOTER = "</div>\n"
            + "        </div>\n"
            + "        <button class=\"btn-lg btn-success center-block\" onclick=\"location.href = 'http://localhost:8080/Hotel_SD/'\"> Volver</button>        \n"
            + "        <footer class=\"footer\">\n"
            + "            <div class=\"container\"> \n"
            + "                <br/>\n"
            + "                <p class=\"text-muted center-block\">Página web realizada por Manuel Gómez Pérez y Julio López González para Sistemas Distribuidos</p>\n"
            + "            </div>\n"
            + "        </footer>\n"
            + "\n"
            + "\n"
            + "    </body>\n"
            + "\n"
            + "\n"
            + "\n"
            + "\n"
            + "</html>\n"
            + "";

}
