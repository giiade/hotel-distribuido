/*
 * Sistemas distribuidos - Practica Hotel
 * Universidad Rey Juan Carlos, Mostoles
 * Realizada por Julio Lopez González y Manuel Gómez Pérez
 * Doble Grado GII + ADE
 * https://github.com/giiade/hotel-distribuido
 */
package Servidor;

import Helper.Constantes;
import Helper.Huesped;
import Helper.Reserva;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JulioLopez
 */
@WebServlet(name = "ServidorHotel", urlPatterns = {"/hotelApp"}, loadOnStartup = 1)
public class ServidorHotel extends HttpServlet {

    ArrayList<Huesped> huespedes = new ArrayList<>();
    HashMap<String, ArrayList<Reserva>> reservasCliente = new HashMap<>();
    ArrayList<Reserva> reservas = new ArrayList<>();
    XStream xstream = new XStream();

    /**
     * Initilizes the servlet with config file and saved data if it exist
     *
     * @param config -> Configuration of server
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        //Añadiriamos aquí un archivo file para cargar al inciar el servlet.
        //Crearemos los ejemplos en el caso de que no encuentre un archivo.    
        super.init(config);
        Huesped huesped = new Huesped(); // Constructor vacio, datos ejemplo
        Reserva reserva = new Reserva(); // Constructor vacio, datos ejemplo
        huespedes.add(huesped);
        reservas.add(reserva);
        reservasCliente.put(reserva.getNIF(), reservas);

        xstream.alias("huesped", Huesped.class);
        xstream.alias("reserva", Reserva.class);
        xstream.alias("Respuesta", ObjetoRespuesta.class);
        xstream.autodetectAnnotations(true);

        //Obtenemos la dirección relativa para guardar un archivo persistente de 
        //datos.
        ServletContext sc = getServletContext();
        String Hola = sc.getRealPath("files");

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //String webPath = request.getServletPath();
        ArrayList<String> resultados = new ArrayList<>();
        ObjetoRespuesta respuesta = new ObjetoRespuesta();
        if (request.getParameterMap().size()>0) {
            String webPath = request.getParameter("operacion");

            //System.out.println("Peticion recibida -->" +  webPath);
            int nParametros = request.getParameterMap().size() - 1;
            String xml = Constantes.XML_HEADER + "\n";

            switch (webPath) {
                case "ConsultaNIF":        
                    if (nParametros > 0) {
                        //String a consultar con el servidor
                        String consulta = request.getParameter("NIF");

                        //Consultamos con nuestro ArrayList 
                        Huesped h = getHuesped(consulta); //Si hay un huesped con ese dni lo recupera
                        //y sino devuelve null
                        if (h != null) {

                            respuesta = new ObjetoRespuesta(true, h, "");

                        } else {
                            //Error
                            respuesta = new ObjetoRespuesta(false, null, "No se ha encontrado ningún huesped");
                        }

                    } else {
                        respuesta = new ObjetoRespuesta(false, null, "URL Malformada");
                    }

                    break;
                case "ConsultaNombre":
                    //Parametros --> Recibe Nombre y apellido. los dos.
                    if (nParametros > 1) {
                        //String a consultar con el servidor
                        String name = URLDecoder.decode(request.getParameter(Constantes.NAME_KEY), "ISO-8859-1");
                        String surname = URLDecoder.decode(request.getParameter(Constantes.SURNAME_KEY), "UTF-8");
                        //Consultamos con nuestro ArrayList de servidores, primero el nombre
                        ArrayList<Huesped> aux = new ArrayList<>();
                        for (Huesped h : huespedes) {
                            if (!name.equals("") && (!surname.equals(""))) {
                                if (h.getNombre().equals(name) && h.getApellidos().equals(surname)) {
                                    aux.add(h);
                                }
                            } else if (!name.equals("")) {
                                if (h.getNombre().equals(name)) {
                                    aux.add(h);
                                }
                            } else {
                                if (h.getApellidos().equals(surname)) {
                                    aux.add(h);
                                }
                            }
                        }

                        if (aux.isEmpty()) {
                            //Error
                            respuesta = new ObjetoRespuesta(false, aux, "No se ha encontrado ningun dato");
                        } else {
                            //Success
                            respuesta = new ObjetoRespuesta(true, aux, "");
                        }

                    } else {
                        //error
                        respuesta = new ObjetoRespuesta(false, null, "URL malformada");
                    }
                    break;
                case "AddHuesped":
                    if (nParametros >= 5) {

                        String nif = request.getParameter(Constantes.NIF_KEY);

                        if (compruebaNif(nif)) {

                            Huesped huesped;
                            String name = request.getParameter(Constantes.NAME_KEY);
                            String apellidos = request.getParameter(Constantes.SURNAME_KEY);
                            String dir = request.getParameter(Constantes.ADDRESS_KEY);
                            String localidad = request.getParameter(Constantes.LOCALIDAD_KEY);
                            String provincia = request.getParameter(Constantes.PROVINCIA_KEY);
                            String cp = request.getParameter(Constantes.CP_KEY);
                            String nacimiento = request.getParameter(Constantes.BIRTHDATE_KEY);
                            String movil = request.getParameter(Constantes.MOVIL_KEY);
                            String fijo = request.getParameter(Constantes.FIJO_KEY);
                            String mail = request.getParameter(Constantes.MAIL_KEY);
                            //Terminar de rellenar

                            huesped = new Huesped(name, apellidos, nif, nacimiento, dir, cp, localidad, provincia, fijo, movil, mail);

                            huespedes.add(huesped); //Añadimos a la lista el huesped creado.
                            //Enviar mensaje XML

                            respuesta = new ObjetoRespuesta(true, "Cliente añadido", "");
                        } else {
                            respuesta = new ObjetoRespuesta(false, null, "El dni ya está registrado");
                        }

                    } else {
                        respuesta = new ObjetoRespuesta(false, null, "URL malformada");
                    }
                    break;

                //Recibimos el NIF para eliminar
                case "ModificarHuesped/Eliminar":
                    if (nParametros >= 0) {
                        String nif = request.getParameter(Constantes.NIF_KEY);
                        Huesped h = getHuesped(nif);
                        if (h != null) {
                            huespedes.remove(h);
                            respuesta = new ObjetoRespuesta(true, "Cliente Eliminado", "");

                        } else {
                            respuesta = new ObjetoRespuesta(false, null, "El dni " + nif + " ya está registrado");
                        }
                    } else {
                        respuesta = new ObjetoRespuesta(false, null, "URL malformada");
                    }
                    break;
                case "ModificarHuesped/CambiarNombre":
                    if (nParametros >= 2) {
                        String nif = request.getParameter(Constantes.NIF_KEY);
                        String name = request.getParameter(Constantes.NAME_KEY);
                        String apellidos = request.getParameter(Constantes.SURNAME_KEY);

                        Huesped h = getHuesped(nif);
                        if (h != null) {
                            h.setNombre(name);
                            h.setApellidos(apellidos);
                            respuesta = new ObjetoRespuesta(true, "Nombre Cambiado con exito", "");

                        } else {
                            respuesta = new ObjetoRespuesta(true, null, "No se encuentra el DNI");
                        }
                    } else {
                        respuesta = new ObjetoRespuesta(true, null, "URL mal formada");
                    }
                    break;
                case "ModificarHuesped/CambiarDomicilio":
                    if (nParametros >= 4) {
                        String nif = request.getParameter(Constantes.NIF_KEY);
                        String dir = request.getParameter(Constantes.ADDRESS_KEY);
                        String localidad = request.getParameter(Constantes.LOCALIDAD_KEY);
                        String provincia = request.getParameter(Constantes.PROVINCIA_KEY);
                        String cp = request.getParameter(Constantes.CP_KEY);

                        Huesped h = getHuesped(nif);
                        if (h != null) {
                            h.setDomicilio(dir, cp, provincia, localidad);
                            respuesta = new ObjetoRespuesta(true, "Domicilio cambiado con exito", "");
                        } else {
                            respuesta = new ObjetoRespuesta(false, null, "No se encuentra el DNI");
                        }
                    } else {
                        respuesta = new ObjetoRespuesta(true, null, "URL mal formada");
                    }
                    break;
                case "ModificarHuesped/CambiarNacimiento":
                    if (nParametros >= 1) {
                        String nif = request.getParameter(Constantes.NIF_KEY);
                        String nacimiento = request.getParameter(Constantes.BIRTHDATE_KEY);

                        Huesped h = getHuesped(nif);
                        if (h != null) {
                            h.setNacimiento(nacimiento);
                            respuesta = new ObjetoRespuesta(true, "Fecha de nacimiento cambiada con exito", "");
                        } else {
                            respuesta = new ObjetoRespuesta(true, null, "No se encuentra el DNI");
                        }
                    } else {
                        respuesta = new ObjetoRespuesta(true, null, "URL mal formada");
                    }
                    break;
                case "ModificarHuesped/CambiarOpcional":
                    if (nParametros >= 3) {
                        String nif = request.getParameter(Constantes.NIF_KEY);
                        String movil = request.getParameter(Constantes.MOVIL_KEY);
                        String fijo = request.getParameter(Constantes.FIJO_KEY);
                        String mail = request.getParameter(Constantes.MAIL_KEY);

                        Huesped h = getHuesped(nif);
                        if (h != null) {
                            h.setMovil(movil);
                            h.setCorreo(mail);
                            h.setFijo(fijo);
                            respuesta = new ObjetoRespuesta(true, "Parametros opcionales actualizados con exito", "");
                        } else {
                            respuesta = new ObjetoRespuesta(true, null, "No se encuentra el DNI");
                        }
                    } else {
                        respuesta = new ObjetoRespuesta(true, null, "URL mal formada");
                    }
                    break;

                case "ConsultarReservas":
                    if (nParametros > 0) {
                    //Buscamos por dni y fecha
                        //String id = request.getParameter(Constantes.NIF_KEY);
                        //Recogemos NIF
                        String fecha = request.getParameter(Constantes.FENTRADA_KEY);
                        Date date;
                        try {
                            date = Constantes.DATE_FORMAT.parse(fecha);
                        } catch (ParseException ex) {
                            respuesta = new ObjetoRespuesta(false, null, "Error al convertir fecha \"dd/MM/YYY\"");
                            break;
                            //Logger.getLogger(ServidorHotel.class.getSimpleName()).log(Level.SEVERE, "ERROR PARSING", ex);
                        }
                        reservas = new ArrayList<>();
                        for (String key : reservasCliente.keySet()) {
                            for (Reserva r : reservasCliente.get(key)) {
                                //Recorremos todas las reservas realizadas por un DNI
                                if (r.getfEntrada().equals(date)) {
                                    reservas.add(r);
                                }
                            }
                        }
                        if (!reservas.isEmpty()) {
                            respuesta = new ObjetoRespuesta(true, reservas, "");
                        } else {
                            respuesta = new ObjetoRespuesta(false, null, "No hay ninguna coincidencia");
                        }
                    } else {
                        respuesta = new ObjetoRespuesta(true, null, "URL mal formada");
                    }
                    break;
                case "ModificarReservas/cambiarFentrada":
                    nParametros = request.getParameterMap().size();
                    if (nParametros >= 2) {
                        String nif = request.getParameter(Constantes.NIF_KEY);
                        String fEntrada = request.getParameter(Constantes.FENTRADA_KEY);
                        Date date;
                        try {
                            date = Constantes.DATE_FORMAT.parse(fEntrada);
                        } catch (ParseException ex) {
                            respuesta = new ObjetoRespuesta(false, null, "Error al convertir fecha \"dd/MM/YYY\"");
                            break;
                            //salimos para enviar el mensaje de fallo.
                        }

                        Reserva r = getReserva(nif, date);
                        if (r != null) {
                            r.setfEntrada(date);
                            respuesta = new ObjetoRespuesta(true, "Fecha de entrada cambiada con exito", "");

                        } else {
                            respuesta = new ObjetoRespuesta(true, null, "No se encuentra el NIF o la fecha de entrada");
                        }
                    } else {
                        respuesta = new ObjetoRespuesta(true, null, "URL mal formada");
                    }
                    break;
                case "ModificarReservas/cambiarFsalida":
                    nParametros = request.getParameterMap().size();
                    if (nParametros >= 2) {
                        String nif = request.getParameter(Constantes.NIF_KEY);
                        String fEntrada = request.getParameter(Constantes.FENTRADA_KEY);
                        String fSalida = request.getParameter(Constantes.FSALIDA_KEY);
                        Date dateE, dateS;
                        try {
                            dateE = Constantes.DATE_FORMAT.parse(fEntrada);
                            dateS = Constantes.DATE_FORMAT.parse(fSalida);
                        } catch (ParseException ex) {
                            respuesta = new ObjetoRespuesta(false, null, "Error al convertir fecha \"dd/MM/YYY\"");
                            break;
                            //Salimos para enviar el mensaje de fallo.
                        }

                        Reserva r = getReserva(nif, dateE);
                        if (r != null) {
                            r.setfSalida(dateS);
                            respuesta = new ObjetoRespuesta(true, "Fecha de salida cambiada con exito", "");

                        } else {
                            respuesta = new ObjetoRespuesta(true, null, "No se encuentra el NIF o la fecha de entrada");
                        }
                    } else {
                        respuesta = new ObjetoRespuesta(true, null, "URL mal formada");
                    }
                    break;
                case "ModificarReservas/EliminarReserva":
                    nParametros = request.getParameterMap().size();
                    if (nParametros >= 2) {
                        String nif = request.getParameter(Constantes.NIF_KEY);
                        String fEntrada = request.getParameter(Constantes.FENTRADA_KEY);
                        Date date;
                        try {
                            date = Constantes.DATE_FORMAT.parse(fEntrada);
                        } catch (ParseException ex) {
                            respuesta = new ObjetoRespuesta(false, null, "Error al convertir fecha \"dd/MM/YYY\"");
                            break;
                            //salimos para enviar el mensaje de fallo.
                        }

                        Reserva r = getReserva(nif, date);
                        if (r != null) {
                            reservasCliente.get(nif).remove(r);
                            respuesta = new ObjetoRespuesta(true, "Reserva eliminada", "");

                        } else {
                            respuesta = new ObjetoRespuesta(true, null, "No se encuentra el NIF o la fecha de entrada");
                        }
                    } else {
                        respuesta = new ObjetoRespuesta(true, null, "URL mal formada");
                    }
                    break;
                case "AddReserva":

                    if (nParametros > 1) {
                        //Recibimos NIF y Fechas de entrada y salida.
                        String nif = request.getParameter(Constantes.NIF_KEY);
                        String fEntrada = request.getParameter(Constantes.FENTRADA_KEY);
                        String fSalida = request.getParameter(Constantes.FSALIDA_KEY);

                        //Comprobamos que el dni del supuesto Huesped esté en la lista
                        if (!compruebaNif(nif)) {
                        //Si tenemos un huesped en la lista con ese dni creamos un
                            //objeto Reserva
                            Reserva r = null;
                            //Generamos un numero de habitación aleatorio entre 100 y 599
                            int nHabitacion = (int) (new Random().nextDouble() * 500) + 100;
                            try {
                                r = new Reserva(nif, nHabitacion, fEntrada, fSalida);
                            } catch (ParseException ex) {
                                respuesta = new ObjetoRespuesta(false, null, "Error al convertir fecha \"dd/MM/YYY\"");
                                break;
                            }
                            //Añadimos la habitacion a la lista.
                            if (reservasCliente.containsKey(nif)) {
                                //Comprobamos si la clave reservas ya existe
                                if (!ComprobarFecha(nif, r.getfEntrada())) {
                                    reservasCliente.get(nif).add(r);
                                } else {
                                    respuesta = new ObjetoRespuesta(false, null, "Ya exixste reserva para ese día");
                                    break;
                                }
                            } else {
                                reservas = new ArrayList<>();
                                reservas.add(r);
                                reservasCliente.put(nif, reservas);
                            }
                            respuesta = new ObjetoRespuesta(true, "Reserva creada", "");
                        } else {
                            respuesta = new ObjetoRespuesta(false, null, "No existe un huesped con ese NIF");
                        }
                    } else {
                        respuesta = new ObjetoRespuesta(true, null, "URL mal formada");
                    }

                    break;
                default:
                    //Mensaje que enviamos si no entra por ninguno de estos.
                    break;

            }
        }else{
            respuesta = new ObjetoRespuesta(false, null, "URL Malformada");
        }

        response.setContentType(
                "application/xml;charset=UTF-8"); //Devolvemos un XML
        try (PrintWriter out = response.getWriter()) {
            out.println(Constantes.XML_HEADER);
            out.println(xstream.toXML(respuesta));

        }

//        ObjetoRespuesta repuesta1 = (ObjetoRespuesta) xstream.fromXML(xstream.toXML(respuesta));
//        if (repuesta1.getObjeto() instanceof ArrayList){
//            ArrayList h = (ArrayList) repuesta1.getObjeto();
//            if(h.get(0) instanceof Reserva){
//                Reserva r = (Reserva) h.get(0);
//            }
//        }
        //Ejemplo recogida datos
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response
    )
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response
    )
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * Comprueba si existe el NIF en el servidor.
     *
     * @param nif NIF a comprobar
     * @return devuelve el valor True si el NIF no está en el servidor, False en
     * caso contrario.
     */
    private boolean compruebaNif(String nif) {
        for (Huesped h : huespedes) {
            if (h.getNif().equals(nif)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Recibe un parametro String nif y busca si hay algún objeto asociado a el.
     *
     * @param nif nif a buscar
     * @return Huesped en caso de encontrarlo, null en caso de que no lo
     * encuentre.
     */
    private Huesped getHuesped(String nif) {
        Huesped huesped = null;
        for (Huesped h : huespedes) {
            if (h.getNif().equals(nif)) {
                huesped = h;
            }
        }
        return huesped;
    }

    /**
     * Comprueba si la fecha dada esta repetida
     *
     * @param date la fecha en formato "dd/MM/YYYY"
     * @return true si encuentra la fecha, false en caso contrario.
     */
    private Boolean ComprobarFecha(String id, Date date) {
        for (Reserva r : reservasCliente.get(id)) {
            //Recorremos todas las reservas realizadas por un DNI
            if (r.getfEntrada().equals(date)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Recibe un NIF y una fecha de entrada y nos devuelve la reserva asociada
     * si la hay
     *
     * @param id Nif del Huesped
     * @param fEntrada Fecha de entrada en formato "dd/MM/YYYY"
     * @return Objeto reserva con la reserva o null.
     */
    private Reserva getReserva(String id, Date fEntrada) {
        Reserva reserva = null;
        for (Reserva r : reservasCliente.get(id)) {
            //Recorremos todas las reservas realizadas por un DNI
            if (r.getfEntrada().equals(fEntrada)) {
                return r;
            }
        }
        return reserva;
    }

}
