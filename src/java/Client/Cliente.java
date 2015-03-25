/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author JulioLopez
 */
public class Cliente {

    static XStream xstream = new XStream();
    static String entradaConsulta = null;
    static int opcion;
    static Scanner teclado = new Scanner(System.in);
    static ObjetoRespuesta respuestaXML;
    static SimpleDateFormat formatoDeFecha = Constantes.DATE_FORMAT;
    static String entradaMod;

    private static boolean consulta(String entrada) {
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
        return consulta;
    }

    private static void modificacion(String entrada) throws IOException {
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

    }

    private static String encode(String dato) {
        try {
            dato = URLEncoder.encode(dato, "ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dato;
    }

    private static String comprobarFecha() {
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

    private static boolean consultarHuesped() throws IOException {
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

    private static boolean consultarHuesped(String nif) throws IOException {

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

    private static void consultarHuespedApe() throws IOException {
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

    private static void anadirHuesped() throws IOException {
        System.out.println("Opción 3. Añadir huésped");
        String nifhuesped;
        do {
            System.out.println("  Introduzca el NIF del nuevo huesped");
            nifhuesped = teclado.nextLine();
        } while (nifhuesped.length() <= 0);
        nifhuesped = encode(nifhuesped);
        anadirHuesped(nifhuesped);

    }

    private static void anadirHuesped(String nifhuesped) throws IOException {

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

    private static void modHuesped() throws IOException {
        System.out.println("  Introduzca el NIF del huésped que desea modificar");
        String nif = teclado.nextLine();
        modHuesped(nif);
    }

    private static void modHuesped(String nif) throws IOException {
        System.out.println("     Modificar datos del huésped");
        System.out.println("Introduzca todos los datos. Cambie aquellos que desea modificar");
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

    private static void delHuesped() throws IOException {
        System.out.println("  Introduzca el NIF del huésped que desea eliminar");
        String nif = teclado.nextLine();
        delHuesped(nif);
    }

    private static void delHuesped(String nif) throws IOException {
        System.out.println("    Eliminar datos del huésped");
        entradaMod = "operacion=ModificarHuesped/Eliminar&NIF=" + nif;
        modificacion(entradaMod);

    }

    private static void buscaReserva() {
        System.out.println("Opción 6. Buscar reservas por fecha de entrada");
        String fecha = comprobarFecha();
        entradaConsulta = ("ConsultarReservas&entrada=" + fecha);
        consulta(entradaConsulta);
    }

    private static void modReserva() throws IOException {
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

    private static void delReserva() throws IOException {
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

    private static void anadirReserva() throws IOException {
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
