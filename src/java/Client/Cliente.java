/**
 * Práctica Hotel_SD para Sistemas distribuidos
 * @author Manuel Gómez Pérez, Julio López González
 * 
 */
package Client;

import Helper.Constantes;
import Helper.Huesped;
import Helper.Reserva;
import Servidor.ObjetoRespuesta;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import com.thoughtworks.xstream.XStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;



/**
 * Clase cliente, es la aplicación cliente que se comunicara con el servidor desde el ordenador
 * @author Julio Lopez, Manuel Gomez
 */
public class Cliente {
    // Variables globales
    static XStream xstream = new XStream();
    static String entradaConsulta = null;
    static int opcion;
    static Scanner teclado = new Scanner(System.in);
    static ObjetoRespuesta respuestaXML;
    static SimpleDateFormat formatoDeFecha = Constantes.DATE_FORMAT;
    static String entradaMod;
    /**
     *  Realiza consultas al servidor.
     * 
     * @param entrada (GET) 
     * @return devuelve valor cierto si la entrada a consultar se encuentra en el servidor,
     *      falso en caso contrario
     */
    public static boolean consulta(String entrada) {
        URL url;
        HttpURLConnection conexion;
        boolean consulta = false;

        try {
            String ruta = Constantes.URL_KEY;
            url = new URL(ruta + entrada);
            conexion = (HttpURLConnection) url.openConnection();
            InputStream is = conexion.getInputStream();
            BufferedReader lector = new BufferedReader(new InputStreamReader(is));
            respuestaXML = (ObjetoRespuesta) xstream.fromXML(lector);
            if (respuestaXML.getSuccess()) {
                if (respuestaXML.getObjeto() instanceof Huesped) {
                    Huesped h = (Huesped) respuestaXML.getObjeto();
                    System.out.println(h.toString());
                } else if (respuestaXML.getObjeto() instanceof Reserva) {
                    Reserva r = (Reserva) respuestaXML.getObjeto();
                    System.out.println(r.toString());
                } else if (respuestaXML.getObjeto() instanceof ArrayList) {
                    ArrayList listaAlgo = (ArrayList) respuestaXML.getObjeto();
                    if (listaAlgo.get(0) instanceof Reserva) {
                        ArrayList<Reserva> listaReserva = (ArrayList) respuestaXML.getObjeto();
                        for (Reserva r : listaReserva) {
                            System.out.println(r.toString());
                        }
                    } else if (listaAlgo.get(0) instanceof Huesped) {
                        ArrayList<Huesped> listaHuesped = (ArrayList) respuestaXML.getObjeto();
                        for (Huesped h : listaHuesped) {
                            System.out.println(h.toString());
                        }
                    }
                }
                consulta = true;
            } else {
                System.out.println(respuestaXML.getError());
                return false;
            }
        } catch (Exception ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return consulta;
        
    }
    /**
     * Realiza modificaciones(incluidas eliminaciones) a los datos
     * que se encuentran en el servidor
     * 
     * @param entrada (POST)
     * @throws IOException 
     */
    public static void modificacion(String entrada) throws IOException {
        URL url;
        HttpURLConnection conexion;
        String ruta = Constantes.POST_KEY;
        try {
            url = new URL(ruta);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conexion.getOutputStream());

            writer.write(entrada);
            writer.flush(); // Envía el cuerpo del mensaje
            InputStream is = conexion.getInputStream();
            BufferedReader lector = new BufferedReader(new InputStreamReader(is));
            respuestaXML = (ObjetoRespuesta) xstream.fromXML(lector);
            if (respuestaXML.getSuccess()) {
                if (respuestaXML.getObjeto() instanceof String) {
                    System.out.println(respuestaXML.getObjeto());
                }
            } else {
                System.out.println(respuestaXML.getError());
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Parar ejecucion y facilitar lectura de datos.
        try {
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /**
     * Codifica el texto introducido a UTF-8
     * 
     * @param dato 
     * @return string con 'dato' en UTF-8
     */
    private static String encode(String dato) {
        try {
            dato = URLEncoder.encode(dato, "ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dato;
    }
    /**
     * Comprueba que la fecha introducida tenga el formato (dd/MM/yyyy)
     * @return fecha con formato válido 
     */
    public static String comprobarFecha() {
        String fecha;
        do {
            System.out.println("    Introduce la fecha (formato dd/MM/yyyy)");
            fecha = teclado.nextLine();
            try {
                formatoDeFecha.parse(fecha);
                break;
            } catch (ParseException badFecha) {
                System.out.println("            Error al introducir fecha,inténtelo de nuevo");
            }
        } while (true);
        return fecha;
    }
    /**
     * Solicita un NIF para luego llamar al método de consulta de huésped con éste.
     * @return el resultado de la llamada a consultarHuesped(nif)
     * @throws IOException 
     */
    public static boolean consultarHuesped() throws IOException {
        System.out.println("Opción 1. Consultar huésped");
        String nif;
        boolean check;
        do {
            System.out.println("  Introduzca el NIF del huésped que desea consultar");
            nif = teclado.nextLine();
        } while (nif.length() <= 0);
        nif = encode(nif);
        check = consultarHuesped(nif);
        return check;
    }
    /**
     * Tras recibir un NIF, realiza una consulta del mismo al servidor, mostrando
     * resultado. Permite
     * modificarlo o eliminarlo si lo encuentra.
     * @param nif (a buscar en el servidor)
     * @return cierto si el NIF introducido coincide con el de un huésped registrado,
     *      falso en caso contrario
     * @throws IOException 
     */
    public static boolean consultarHuesped(String nif) throws IOException {

        boolean check = false;

        entradaConsulta = ("ConsultaNIF&" + "NIF=" + nif);
        check = consulta(entradaConsulta);
        if (check) {
            do {
                System.out.println("¿Qué desea hacer?");
                System.out.println("    1 - Modificar Huésped");
                System.out.println("    2 - Eliminar Huésped");
                System.out.println("    3 - Volver al menú anterior ");
                System.out.println();
                System.out.println("Seleccione el número de la opción deseada");
                opcion = Integer.parseInt(teclado.nextLine());
                switch (opcion) {
                    case 1:
                        modHuesped(nif);
                        break;
                    case 2:
                        delHuesped(nif);
                        opcion=3;
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Opción incorrecta. Por favor, seleccione una válida (1-3)");
                }
            } while (opcion != 3);
            opcion = 0;
        }
        return check;
    }
    /**
     * Recibe un nombre y apellidos. Comprueba si coiciden con el de algún huesped
     * (mediante una llamada a consulta(entrada).Muestra una lista con las coincidencias.
     * Permite, tras seleccionar el huésped,
     * eliminarlo o modificarlo
     * @throws IOException 
     */
    public static void consultarHuespedApe() throws IOException {
        boolean check = false;
        System.out.println("Opción 2. Consultar huésped");
        System.out.println("  Introduzca los apellidos del huésped que desea consultar. Pulse intro sin introducir nada si desea buscar sólo por nombre");
        String apellidos = teclado.nextLine();
        apellidos = encode(apellidos);
        String nombre, nif;
        if (apellidos.length() > 0) {
            System.out.println("  Ahora introduzca el nombre del huésped que desea consultar. Pulse intro para saltar.");
            nombre = teclado.nextLine();
        } else {
            do {
                System.out.println("  Introduzca el nombre del huésped que desea consultar. Este paso es obligatorio");
                nombre = teclado.nextLine();
            } while (nombre.length() <= 0);
        }
        nombre = encode(nombre);
        entradaConsulta = ("ConsultaNombre&nombre=" + nombre + "&apellidos=" + apellidos);
        check = consulta(entradaConsulta);
        if (check) {
            do {
                System.out.println("¿Qué desea hacer?");
                System.out.println("    1 - Modificar Huésped");
                System.out.println("    2 - Eliminar Huésped");
                System.out.println("    3 - Volver al menú anterior ");
                System.out.println();
                System.out.println("Seleccione el número de la opción deseada");
                opcion = Integer.parseInt(teclado.nextLine());
                switch (opcion) {
                    case 1:
                        System.out.println("Debido a que la búsqueda anterior puede tener varios resultados, introduzca el NIF del huésped que desea modificar.");
                        System.out.println("Si no desea modificar un huésped. Pulse intro sin introducir el NIF");
                        nif = teclado.nextLine();
                        if (nif.length() > 0) {
                            modHuesped(nif);
                        } else {
                            System.out.println("Operación de modificación cancelada");
                        }
                        break;
                    case 2:
                        System.out.println("Debido a que la búsqueda anterior puede tener varios resultados, introduzca el NIF del huésped que desea eliminar.");
                        System.out.println("Si no desea eliminar un huésped. Pulse intro sin introducir el NIF");
                        nif = teclado.nextLine();
                        if (nif.length() > 0) {
                            delHuesped(nif);
                            opcion=3;
                        } else {
                            System.out.println("Operación de eliminación cancelada");
                        }
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Opción incorrecta. Por favor, seleccione una válida (1-3)");
                }
            } while (opcion != 3);
            opcion = 0;
        }
    }
    /**
     * Solicita la introducción de un nif y realiza una llamada a anadirHuesped con
     * este como parámetro de entrada. Realiza una llamada a consulta(entrada)
     * con estos datos
     * @throws IOException 
     */
    public static void anadirHuesped() throws IOException {
        System.out.println("Opción 3. Añadir huésped");
        String nifhuesped;
        do {
            System.out.println("  Introduzca el NIF del nuevo huesped");
            nifhuesped = teclado.nextLine();
        } while (nifhuesped.length() <= 0);
        nifhuesped = encode(nifhuesped);
        anadirHuesped(nifhuesped);
    }
    /**
     * Añade un nuevo huesped, tras introducir todos sus datos obligatorios.
     * Permite dejar en blanco los opcionales.
     * @param nifhuesped
     * @throws IOException 
     */
    public static void anadirHuesped(String nifhuesped) throws IOException {

        String nombrehuesped, apellidoshuesped, nacHuesped, dirhuesped, localhuesped, CPhuesped, provinciaHuesped;
        do {
            System.out.println("  Introduzca el nombre del nuevo huesped");
            nombrehuesped = teclado.nextLine();
        } while (nombrehuesped.length() <= 0);
        nombrehuesped = encode(nombrehuesped);
        do {
            System.out.println("  Introduzca los apellidos del nuevo huesped");
            apellidoshuesped = teclado.nextLine();
        } while (apellidoshuesped.length() <= 0);
        apellidoshuesped = encode(apellidoshuesped);
        System.out.println("  Fecha de nacimiento:");
        nacHuesped = comprobarFecha();
        System.out.println("Domicilio del huesped");
        do {
            System.out.println("  1-dirección");
            dirhuesped = teclado.nextLine();
        } while (dirhuesped.length() <= 0);
        dirhuesped = encode(dirhuesped);
        do {
            System.out.println("  2-Localidad");
            localhuesped = teclado.nextLine();
        } while (localhuesped.length() <= 0);
        do {
            System.out.println("  3-CP");
            CPhuesped = teclado.nextLine();
        } while (CPhuesped.length() <= 0);
        CPhuesped = encode(CPhuesped);
        do {
            System.out.println("  4-Provincia");
            provinciaHuesped = teclado.nextLine();
        } while (provinciaHuesped.length() <= 0);
        provinciaHuesped = encode(provinciaHuesped);
        System.out.println("Introduzca teléfono fijo (puede dejar vacío este campo)");
        String fijoHuesped = teclado.nextLine();
        System.out.println("Introduzca el número de móvil (puede dejar vacío este campo)");
        String movilHuesped = teclado.nextLine();
        System.out.println("Introduzca el correo electrónico (puede dejar vacío este campo)");
        String correoHuesped = teclado.nextLine();
        entradaMod = "operacion=AddHuesped&NIF=" + nifhuesped + "&nombre=" + nombrehuesped + "&apellidos=" + apellidoshuesped + "&direccion=" + dirhuesped + "&localidad=" + localhuesped + "&provincia=" + provinciaHuesped + "&CP=" + CPhuesped + "&nacimiento=" + nacHuesped + "&movil=" + movilHuesped + "&fijo=" + fijoHuesped + "&mail=" + correoHuesped;
        modificacion(entradaMod);
    }
    /**
     * Solicita la introducción de un NIF. Realiza una llamada a modHuesped con éste como
     * parámetro de entrada. Realiza una llamada a modificacion(entrada) con estos datos
     * @throws IOException 
     */
    public static void modHuesped() throws IOException {
        System.out.println("  Introduzca el NIF del huésped que desea modificar");
        String nif = teclado.nextLine();
        modHuesped(nif);
    }
    /**
     * Solicita la introducción de todos los datos del huésped. Aquellos que no se deseen modificar
     * tendrán que ser introducidos tal cual. Realiza varias llamadas a modificacion(entrada)
     * con éstos datos
     * @param nif
     * @throws IOException 
     */
    public static void modHuesped(String nif) throws IOException {
        System.out.println("     Modificar datos del huésped");
        System.out.println("Introduzca todos los datos. Cambie aquellos que desea modificar");
        String nombrehuesped, apellidoshuesped, nacHuesped, dirhuesped, localhuesped, CPhuesped, provinciaHuesped;
        do {
            System.out.println("  Introduzca el nombre del huesped");
            nombrehuesped = teclado.nextLine();
        } while (nombrehuesped.length() <= 0);
        nombrehuesped = encode(nombrehuesped);
        do {
            System.out.println("  Introduzca los apellidos del huesped");
            apellidoshuesped = teclado.nextLine();
        } while (apellidoshuesped.length() <= 0);
        apellidoshuesped = encode(apellidoshuesped);
        System.out.println("  Fecha de nacimiento:");
        nacHuesped = comprobarFecha();
        System.out.println("Domicilio del huesped");
        do {
            System.out.println("  1-dirección");
            dirhuesped = teclado.nextLine();
        } while (dirhuesped.length() <= 0);
        dirhuesped = encode(dirhuesped);
        do {
            System.out.println("  2-Localidad");
            localhuesped = teclado.nextLine();
        } while (localhuesped.length() <= 0);
        do {
            System.out.println("  3-CP");
            CPhuesped = teclado.nextLine();
        } while (CPhuesped.length() <= 0);
        CPhuesped = encode(CPhuesped);
        do {
            System.out.println("  4-Provincia");
            provinciaHuesped = teclado.nextLine();
        } while (provinciaHuesped.length() <= 0);
        provinciaHuesped = encode(provinciaHuesped);
        System.out.println("Introduzca teléfono fijo");
        String fijoHuesped = teclado.nextLine();
        System.out.println("Introduzca el número de móvil)");
        String movilHuesped = teclado.nextLine();
        System.out.println("Introduzca el correo electrónico");
        String correoHuesped = teclado.nextLine();
        if (nombrehuesped.length() > 0 || apellidoshuesped.length() > 0) {
            entradaMod = "operacion=ModificarHuesped/CambiarNombre&nombre=" + nombrehuesped + "&apellidos=" + apellidoshuesped+"&NIF="+nif;
            modificacion(entradaMod);
        }
        if(dirhuesped.length() > 0 || CPhuesped.length() > 0 || localhuesped.length() > 0 || provinciaHuesped.length() > 0) {
            entradaMod = "operacion=ModificarHuesped/CambiarDomicilio&direccion=" + dirhuesped + "&localidad=" + localhuesped + "&provincia=" + provinciaHuesped + "&CP=" + CPhuesped+"&NIF="+nif;
            modificacion(entradaMod);
        }
        if (nacHuesped.length() > 0) {
            entradaMod = "operacion=ModificarHuesped/CambiarNacimiento&nacimiento=" + nacHuesped+"&NIF="+nif;
            modificacion(entradaMod);
        }
        if (movilHuesped.length() > 0 || fijoHuesped.length() > 0 || correoHuesped.length() > 0) {
            entradaMod = "operacion=ModificarHuesped/CambiarOpcional&movil=" + movilHuesped + "&fijo=" + fijoHuesped + "&mail=" + correoHuesped+"&NIF="+nif;
            modificacion(entradaMod);
        }
    }
    /**
     * Solicita un NIF al usuario, el cual será usado para llamar a delHuesped(nif)
     * @throws IOException 
     */
    public static void delHuesped() throws IOException {
        System.out.println("  Introduzca el NIF del huésped que desea eliminar");
        String nif = teclado.nextLine();
        delHuesped(nif);
    }
    /**
     * Recibe un nif. Envía una petición de eliminación del huésped con dicho NIF
     * @param nif
     * @throws IOException 
     */
    public static void delHuesped(String nif) throws IOException {
        System.out.println("    Eliminar datos del huésped");
        entradaMod = "operacion=ModificarHuesped/Eliminar&NIF=" + nif;
        modificacion(entradaMod);
    }
    /**
     * Realiza una consulta al servidor con una fecha introducida por el usuario.
     * Muestra la lista de reservas que tienen dicha fecha como fecha de entrada.
     */
    public static void buscaReserva() {
        System.out.println("Opción 6. Buscar reservas por fecha de entrada");
        String fecha = comprobarFecha();
        entradaConsulta = ("ConsultarReservas&entrada=" + fecha);
        consulta(entradaConsulta);
    }
    /**
     * Solicita la introducción del NIF del huésped que ha hecho la reserva así como la fecha de entrada.
     * Permite modificar la fecha de entrada y de salida de la reserva. Realiza una llamada a
     * modificacion(entrada)
     * @throws IOException 
     */
    public static void modReserva() throws IOException {
        System.out.println("Opción 7. Modificar reserva");
        System.out.println("  Introduzca el NIF del huésped cuya reserva desea modificar");
        String nif = teclado.nextLine();
        if (nif.length() > 0) {
            System.out.println("  Introduzca la fecha de entrada original");
            String fechaEntradaOrig = teclado.nextLine();
            System.out.println("  Introduzca sólo los datos que desea modificar.Para aquellos que no desee modificar pulse intro sin introducir texto.");
            System.out.println("  Introduzca la fecha de entrada modificada");
            String fechaEntrada = teclado.nextLine();
            System.out.println("  Introduzca la fecha de salida modificada");
            String fechaSalida = teclado.nextLine();
            
            if (fechaSalida.length() > 0) {
                entradaMod = "operacion=ModificarReservas/cambiarFsalida&salida=" + fechaSalida +"&" + Constantes.FOENTRADA_KEY+ "=" +fechaEntradaOrig+ "&NIF=" + nif;
                modificacion(entradaMod);
            }
            if (fechaEntrada.length() > 0) {
                entradaMod = "operacion=ModificarReservas/cambiarFentrada&" + Constantes.FOENTRADA_KEY + "=" + fechaEntradaOrig + "&NIF=" + nif+"&" + Constantes.FENTRADA_KEY + "="+fechaEntrada;
                modificacion(entradaMod);
            } 
        }
    }
    /**
     * Solicita el NIF del huésped que ha hecho la reserva así como la fecha de entrada.
     * Realiza una llamada a modificacion(entrada) solicitando la eliminación de la reserva
     * con esos datos.
     * @throws IOException 
     */
    public static void delReserva() throws IOException {
        System.out.println("Opción 8. Eliminar reserva");
        System.out.println("  Introduzca el NIF del huésped cuya reserva desea eliminar");
        String nif = teclado.nextLine();
        if (nif.length() > 0) {
            System.out.println("  Fecha de entrada de la reserva a eliminar");
            String fechaEntrada = comprobarFecha();
            entradaMod = "operacion=ModificarReservas/EliminarReserva&entrada=" + fechaEntrada + "&NIF=" + nif;
            modificacion(entradaMod);

        }
    }
    /**
     * Permite añadir una reserva mediante la introducción de un NIF, una fecha de entrada y una de salida.
     * Si el NIF no se encuentra registrado, permite añadir un nuevo huésped con una llamada a anadirHuesped(nif);
     * @throws IOException 
     */
    public static void anadirReserva() throws IOException {
        boolean check = false;
        System.out.println("Opción 9. Añadir reserva");
        System.out.println("  Introduzca el NIF del huésped a cuyo nombre estará la reserva");
        String nif = teclado.nextLine();
        nif = encode(nif);
        System.out.println("  Fecha de entrada:");
        String fEntrada = comprobarFecha();
        System.out.println("  Fecha de Salida");
        String fSalida = comprobarFecha();
        System.out.println("Se comprobará si el huésped se encuentra en la base de datos");
        check = consultarHuesped(nif);
        if (!check) {
            do {
                System.out.println("El huésped no se encuentra en la base de datos. ¿Qué desea?");
                System.out.println("    1- Añadir huésped y crear reserva");
                System.out.println("    2-Volver al menu anterior");
                System.out.println("Seleccione la opción deseada");
                opcion = Integer.parseInt(teclado.nextLine());
                switch (opcion) {
                    case 1:
                        anadirHuesped(nif);
                        System.out.println("Se procederá a crear la reserva con el huésped añadido");
                        entradaMod = "operacion=AddReserva&entrada=" + fEntrada + "&NIF=" + nif + "&salida=" + fSalida;
                        modificacion(entradaMod);
                        opcion=2;
                        break;
                    case 2:
                        break;
                    default:
                        System.out.println("Opción incorrecta. Por favor, seleccione una válida (1-2)");
                }
            } while (opcion != 2);
        }
        else{
            System.out.println("Se procederá a crear la reserva.");
            entradaMod = "operacion=AddReserva&entrada=" + fEntrada + "&NIF=" + nif + "&salida=" + fSalida;
            modificacion(entradaMod);
        }
    }

    /**
     * Menú principal de la aplicación con las distintas llamadas a los módulos que
     * permiten consultar,añadir,modificar o eliminar huéspedes o reservas.
     * @param args
     * @throws IOException 
     */    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        xstream.alias("huesped", Huesped.class);
        xstream.alias("reserva", Reserva.class);
        xstream.alias("Respuesta", ObjetoRespuesta.class);

        do {
            System.out.println("MENÚ CLIENTE");
            System.out.println("    1 - Consultar Huésped (NIF)");
            System.out.println("    2 - Consultar Huésped (Apellidos y nombre)");
            System.out.println("    3 - Añadir Huesped ");
            System.out.println("    4 - Modificar datos del Huésped");
            System.out.println("    5 - Eliminar datos del Huésped");
            System.out.println("    6 - Buscar reservas por fecha de entrada");
            System.out.println("    7 - Modificar reserva");
            System.out.println("    8 - Eliminar reserva");
            System.out.println("    9 - Añadir reserva");
            System.out.println("    10 - Salir");
            System.out.println();

            System.out.println("Seleccione el número de la opción deseada");
            opcion = Integer.parseInt(teclado.nextLine());
            switch (opcion) {
                case 1:
                    consultarHuesped();
                    break;
                case 2:
                    consultarHuespedApe();
                    break;
                case 3:
                    anadirHuesped();
                    break;
                case 4:
                    System.out.println("Opción 4. Modificar datos del huésped");
                    modHuesped();
                    break;
                case 5:
                    System.out.println("Opción 5. Eliminar datos del huésped");
                    delHuesped();
                    break;
                case 6:
                    buscaReserva();
                    break;
                case 7:
                    modReserva();
                    break;
                case 8:
                    delReserva();
                    break;
                case 9:
                    anadirReserva();
                    break;
                case 10:
                    System.out.println("Opción 10. Salir");
                    System.out.println("    Gracias por usar el nuestro menú para reservas de hotel.");
                    break;
                default:
                    System.out.println("Opción incorrecta. Por favor, seleccione una válida (1-10)");
                    
            }
        } while (opcion!= 10);
    }
}
