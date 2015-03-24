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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author JulioLopez
 */
public class Cliente {
    static XStream xstream = new XStream();
    static String entradaConsulta = null;

    private static void consulta(String entrada) {
        URL url;
        HttpURLConnection conexion;
        String ruta = Constantes.URL_KEY;
        ObjetoRespuesta respuestaXML;
        try {
      
            url = new URL(ruta + entrada);
            System.out.println(ruta + entrada);
            conexion = (HttpURLConnection) url.openConnection();
            InputStream is = conexion.getInputStream();
            BufferedReader lector = new BufferedReader(new InputStreamReader(is));
            respuestaXML = (ObjetoRespuesta)  xstream.fromXML(lector);
            if (respuestaXML.getObjeto() instanceof Huesped){
                Huesped h = (Huesped) respuestaXML.getObjeto();
                System.out.println(h.toString());
            }
            else if (respuestaXML.getObjeto() instanceof Reserva){
            // HAZ COSAS TIPO RESERVA
            }
            // va a petar con listas de reserva
            else if (respuestaXML.getObjeto() instanceof ArrayList){
                ArrayList<Huesped> lista = (ArrayList) respuestaXML.getObjeto();
                for (Huesped h: lista){
                   System.out.println(h.toString());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static String encode(String dato){
        try {
            dato = URLEncoder.encode(dato,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dato;
    }

    private static void consultarHuesped(Scanner teclado) {
        System.out.println("Opción 1. Consultar huésped");
        String nif;
        do{
            System.out.println("  Introduzca el NIF del huésped que desea consultar");
            nif = teclado.nextLine();
        } while (nif.length()<=0);
        if (nif.length()>0){
        nif = encode(nif);
        entradaConsulta = ("ConsultaNIF&"+"NIF="+nif);
        consulta(entradaConsulta);
        }
    }

    private static void consultarHuespedApe(Scanner teclado) {
        System.out.println("Opción 2. Consultar huésped");
        System.out.println("  Introduzca los apellidos del huésped que desea consultar. Pulse intro sin introducir nada si desea buscar sólo por nombre");
        String apellidos = teclado.nextLine();
        apellidos = encode(apellidos);
        String nombre;
        if (apellidos.length()>0){
            System.out.println("  Ahora introduzca el nombre del huésped que desea consultar. Pulse intro para saltar.");
            nombre=teclado.nextLine();
        }
        else{
            do{
            System.out.println("  Introduzca el nombre del huésped que desea consultar. Este paso es obligatorio");
            nombre = teclado.nextLine();
            } while (nombre.length()<=0);               
        }
        nombre = encode(nombre);
        entradaConsulta  = ("ConsultaNombre&nombre="+nombre+"&apellidos="+apellidos);
        consulta(entradaConsulta);
    }

    private static void anadirHuesped(Scanner teclado) {
        System.out.println("Opción 3. Añadir huésped");
        String nombrehuesped,apellidoshuesped,nacHuesped,nifhuesped,dirhuesped,localhuesped,CPhuesped,provinciaHuesped;
        do{
            System.out.println("  Introduzca el nombre del nuevo huesped");
            nombrehuesped = teclado.nextLine();
        } while (nombrehuesped.length()<=0);
        do{
            System.out.println("  Introduzca los apellidos del nuevo huesped");
            apellidoshuesped = teclado.nextLine();
        } while (apellidoshuesped.length()<=0);
        do{
            System.out.println("  Introduzca la fecha de nacimiento del nuevo huesped (formato dd/mm/yyyy)");
            nacHuesped = teclado.nextLine();
        } while (nacHuesped.length()<=0);
        // convertir fechas
        do{
            System.out.println("  Introduzca el NIF del nuevo huesped");
            nifhuesped = teclado.nextLine();
        } while (nifhuesped.length()<=0);
        System.out.println("Domicilio del huesped");
        do{
            System.out.println("  1-dirección");
            dirhuesped = teclado.nextLine();
        } while (dirhuesped.length()<=0);
        do{
            System.out.println("  2-Localidad");
            localhuesped = teclado.nextLine();
        } while (localhuesped.length()<=0);
        do{
            System.out.println("  3-CP");
            CPhuesped = teclado.nextLine();
        } while (CPhuesped.length()<=0);
        do{
            System.out.println("  4-Provincia");
            provinciaHuesped = teclado.nextLine();
        } while (provinciaHuesped.length()<=0);
        System.out.println("Introduzca teléfono fijo (puede dejar vacío este campo)");
        String fijoHuesped = teclado.nextLine();
        System.out.println("Introduzca el número de móvil (puede dejar vacío este campo)");
        String movilHuesped = teclado.nextLine();
        System.out.println("Introduzca el correo electrónico (puede dejar vacío este campo)");
        String correoHuesped = teclado.nextLine();
        System.out.println("Gracias por introducir los datos del huésped");
    }

    private static void modHuesped(Scanner teclado) {
        System.out.println("Opción 4. Modificar datos del huésped");
        System.out.println("  Introduzca el NIF del huésped que desea modificar");
        String nif = teclado.nextLine();
        System.out.println(" PRUEBA NIF =" + nif);
    }

    private static void delHuesped(Scanner teclado) {
        System.out.println("Opción 5. Eliminar datos del huésped");
        System.out.println("  Introduzca el NIF del huésped que desea eliminar");
        String nif = teclado.nextLine();
        System.out.println(" PRUEBA NIF =" + nif);
    }

    private static void buscaReserva(Scanner teclado) {
        System.out.println("Opción 6. Buscar reservas por fecha");
        // FALTA INTRODUCIR FECHAS DE RESERVA
    }

    private static void modReserva(Scanner teclado) {
        System.out.println("Opción 7. Modificar reserva");
        System.out.println("  Introduzca el NIF del huésped cuya reserva desea modificar");
        String nif = teclado.nextLine();
        System.out.println(" PRUEBA NIF =" + nif);
        // FALTA INTRODUCIR FECHAS DE RESERVA
    }

    private static void delReserva(Scanner teclado) {
        System.out.println("Opción 8. Eliminar reserva");
        System.out.println("  Introduzca el NIF del huésped cuya reserva desea eliminar");
        String nif = teclado.nextLine();
        System.out.println(" PRUEBA NIF =" + nif);
        // FALTA INTRODUCIR FECHAS DE RESERVA
    }

    private static void anadirReserva(Scanner teclado) {
        System.out.println("Opción 9. Añadir reserva");
        System.out.println("  Introduzca el NIF del huésped (ya registrado) a cuyo nombre estará la reserva");
        String nif = teclado.nextLine();
        System.out.println(" PRUEBA NIF =" + nif);
    }

    public static void main(String[] args) {
        // TODO code application logic here
        xstream.alias("huesped", Huesped.class);
        xstream.alias("reserva", Reserva.class);
        xstream.alias("Respuesta", ObjetoRespuesta.class);
        Scanner teclado = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("MENÚ CUTRE DE JULIO");
            System.out.println("    1 - Consultar Huésped (NIF)");
            System.out.println("    2 - Consultar Huésped (Apellidos y nombre)");
            System.out.println("    3 - Añadir Huesped ");
            System.out.println("    4 - Modificar datos del Huésped");
            System.out.println("    5 - Eliminar datos del Huésped");
            System.out.println("    6 - Buscar reservas por fecha");
            System.out.println("    7 - Modificar reserva");
            System.out.println("    8 - Eliminar reserva");
            System.out.println("    9 - Añadir reserva");
            System.out.println("    10 - Salir");
            System.out.println();

            System.out.println("Seleccione el número de la opción deseada");
            opcion = Integer.parseInt(teclado.nextLine());
            switch (opcion) {
                case 1:
                    consultarHuesped(teclado);
                    break;
                case 2:
                    consultarHuespedApe(teclado);
                    break;
                case 3:
                    anadirHuesped(teclado);
                    break;
                case 4:
                    modHuesped(teclado);
                    break;
                case 5:
                    delHuesped(teclado);
                    break;
                case 6:
                    buscaReserva(teclado);
                    break;
                case 7:
                    modReserva(teclado);
                    break;
                case 8:
                    delReserva(teclado);
                    break;
                case 9:
                    anadirReserva(teclado);
                    break;
                case 10:
                    System.out.println("Opción 10. Salir");
                    System.out.println("    Gracias por usar el cutre menú julio para reservas de hotel.");
                    break;
                default:
                    System.out.println("Opción incorrecta. Por favor, seleccione una válida (1-10)");
            }
        } while (opcion != 10);
    }
}
