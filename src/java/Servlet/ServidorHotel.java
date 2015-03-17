/*
 * Sistemas distribuidos - Practica Hotel
 * Universidad Rey Juan Carlos, Mostoles
 * Realizada por Julio Lopez González y Manuel Gómez Pérez
 * Doble Grado GII + ADE
 * https://github.com/giiade/hotel-distribuido
 */
package Servlet;

import Helper.Huesped;
import Helper.Reserva;
import Helper.Constantes;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.thoughtworks.xstream.XStream;

/**
 *
 * @author JulioLopez
 */
@WebServlet(name = "ServidorHotel", urlPatterns = {"/"}, loadOnStartup = 1)
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

        String webPath = request.getServletPath();
        ArrayList<String> resultados = new ArrayList<>();
        ObjetoRespuesta respuesta = new ObjetoRespuesta();

        //System.out.println("Peticion recibida -->" +  webPath);
        int nParametros = 0;
        String xml = Constantes.XML_HEADER + "\n";

        switch (webPath) {
            case "/ConsultaNIF":
                nParametros = request.getParameterMap().size();
                if (nParametros > 0) {
                    //String a consultar con el servidor
                    String consulta = request.getParameter("NIF");

                    //Consultamos con nuestro ArrayList 
                    Huesped h = getHuesped(consulta); //Si hay un huesped con ese dni lo recupera
                    //y sino devuelve null
                    if (h != null) {

                        respuesta = new ObjetoRespuesta(true, h.getClass().getSimpleName(), h, "");

                    } else {
                        respuesta = new ObjetoRespuesta(false, h.getClass().getSimpleName(), null, "No se ha encontrado ningún huesped");
                    }

                } else {
                    respuesta = new ObjetoRespuesta(false, Huesped.class.getSimpleName(), null, "URL Malformada");
                }

                break;
            case "/ConsultaNombre":
                //Parametros --> Recibe Nombre y apellido. los dos.
                nParametros = request.getParameterMap().size();
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

                        if (aux.isEmpty()) {
                            xml = xstream.toXML("No se ha encontrado ningun elemento relacionado con la busqueda");
                        } else {
                            xml = xstream.toXML(aux);
                        }
                    }
                }
                break;
            case "/AddHuesped":
                nParametros = request.getParameterMap().size();
                boolean success = true;
                if (nParametros >= 1) {

                    // HashMap<String,String> datosHuesped =
                    String nif = request.getParameter(Constantes.NIF_KEY);

                    if (compruebaNif(nif)) {

                        Huesped huesped;
                        String name = request.getParameter(Constantes.NAME_KEY);
                        String apellidos = request.getParameter(Constantes.SURNAME_KEY);
//                        String dir = request.getParameter(Constantes.ADDRESS_KEY);
//                        String localidad = request.getParameter(Constantes.LOCALIDAD_KEY);
//                        String provincia = request.getParameter(Constantes.PROVINCIA_KEY);
//                        String cp = request.getParameter(Constantes.CP_KEY);
                        String nacimiento = request.getParameter(Constantes.BIRTHDATE_KEY);
//                        String movil = request.getParameter(Constantes.MOVIL_KEY);
//                        String fijo = request.getParameter(Constantes.FIJO_KEY);
//                        String mail = request.getParameter(Constantes.MAIL_KEY);
                        //Terminar de rellenar

                        huesped = new Huesped(name, apellidos, nif, nacimiento);

                        huespedes.add(huesped); //Añadimos a la lista el huesped creado.
                        //Enviar mensaje XML

                        xml = xstream.toXML(success);
                    } else {
                        success = false;
                        xml = xstream.toXML(success);
                    }

                } else {
                    success = false;
                    xml = xstream.toXML(success);
                }
                break;

            //Recibimos el NIF para eliminar
            case "/ModificarHuesped/Eliminar":
                nParametros = request.getParameterMap().size();
                if (nParametros >= 0) {
                    String nif = request.getParameter(Constantes.NIF_KEY);
                    Huesped h = getHuesped(nif);
                    if (h != null) {
                        huespedes.remove(h);
                    } else {
                        //Mensaje de no se encuentra
                    }
                }
                break;
            case "/ModificarHuesped/CambiarNombre":
                nParametros = request.getParameterMap().size();
                if (nParametros >= 2) {
                    String nif = request.getParameter(Constantes.NIF_KEY);
                    String name = request.getParameter(Constantes.NAME_KEY);
                    String apellidos = request.getParameter(Constantes.SURNAME_KEY);

                    Huesped h = getHuesped(nif);
                    if (h != null) {
                        h.setNombre(name);
                        h.setApellidos(apellidos);
                    } else {
                        //No se encuentra
                    }
                } else {
                    //URL MAL FORMADA
                }
                break;
            case "/ModificarHuesped/CambiarDomicilio":
                if (nParametros >= 4) {
                    String nif = request.getParameter(Constantes.NIF_KEY);
                    String dir = request.getParameter(Constantes.ADDRESS_KEY);
                    String localidad = request.getParameter(Constantes.LOCALIDAD_KEY);
                    String provincia = request.getParameter(Constantes.PROVINCIA_KEY);
                    String cp = request.getParameter(Constantes.CP_KEY);

                    Huesped h = getHuesped(nif);
                    if (h != null) {
                        h.setDomicilio(dir, cp, provincia, localidad);
                    } else {
                        //No se encuentra
                    }
                } else {
                    //URL MAL FORMADA
                }
                break;
            case "/ModificarHuesped/CambiarNacimiento":
                if (nParametros >= 1) {
                    String nif = request.getParameter(Constantes.NIF_KEY);
                    String nacimiento = request.getParameter(Constantes.BIRTHDATE_KEY);

                    Huesped h = getHuesped(nif);
                    if (h != null) {
                        h.setNacimiento(nacimiento);
                    } else {
                        //No se encuentra
                    }
                } else {
                    //URL MAL FORMADA
                }
                break;
            case "/ModificarHuesped/CambiarOpcional":
                Boolean eliminar = false;
                nParametros = request.getParameterMap().size();
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
                    } else {
                        //No se encuentra
                    }
                } else {
                    //URL MAL FORMADA
                }
                break;

            case "/ConsultarReservas":
                nParametros = request.getParameterMap().size();
                if (nParametros > 1) {
                    //Buscamos por dni y fecha
                    String id = request.getParameter(Constantes.NIF_KEY);
                    //Recogemos NIF
                    String fecha = request.getParameter(Constantes.FENTRADA_KEY);
                    Date date = null;
                    try {
                        date = Constantes.DATE_FORMAT.parse(fecha);
                    } catch (ParseException ex) {
                        Logger.getLogger(ServidorHotel.class.getSimpleName()).log(Level.SEVERE, "EROOR PARSING", ex);
                    }

                    for (Reserva r : reservasCliente.get(id)) {
                        //Recorremos todas las reservas realizadas por un DNI
                        if (r.getfEntrada().equals(date)) {
                            resultados.add(r.toString());
                        }
                    }

                } else {
                    resultados.add("NADA DE NADA");
                }
                break;
            case "/ModificarReservas/cambiarFentrada":
                //Por hacer. Pensar si hacer cada uno por separado o no
                //Afectaria a modificarhuesped tambien. 
                break;
            case "/ModificarReservas/cambiarFsalida":
                break;
            case "/ModificarReservas/EliminarHabitacion":
                break;
            case "/AñadirReserva":
                //Recibimos NIF y Fechas de entrada y salida.

                String nif = request.getParameter(Constantes.NIF_KEY);
                String fEntrada = request.getParameter(Constantes.FENTRADA_KEY);
                String fSalida = request.getParameter(Constantes.FSALIDA_KEY);

                //Comprobamos que el dni del supuesto Huesped esté en la lista
                if (compruebaNif(nif)) {
                    //Si tenemos un huesped en la lista con ese dni creamos un
                    //objeto Reserva
                    Reserva r = null;
                    //Generamos un numero de habitación aleatorio entre 100 y 599
                    int nHabitacion = (int) new Random().nextDouble() * 600 + 100;
                    r = new Reserva(nif, nHabitacion, fEntrada, fSalida);
                    //Añadimos la habitacion a la lista.
                    if (reservasCliente.containsKey(nif)) {
                        reservasCliente.get(nif).add(r);
                    } else {
                        reservas = new ArrayList<>();
                        reservas.add(r);
                        reservasCliente.put(nif, reservas);
                    }
                    //Success = true; -> Codigo HTTP 202, o algo así.
                }
                break;
            default:
                //Mensaje que enviamos si no entra por ninguno de estos.
                break;

        }

        if (resultados.isEmpty()) {
            resultados.add("No se ha encontrado nada");
        }

        response.setContentType(
                "application/xml;charset=UTF-8"); //Devolvemos un XML
        try (PrintWriter out = response.getWriter()) {
            out.println(Constantes.XML_HEADER);
            out.println(xstream.toXML(respuesta));

        }

//        ObjetoRespuesta repuesta1 = (ObjetoRespuesta) xstream.fromXML(xstream.toXML(respuesta));
//        if (repuesta1.getType().equals("Huesped")) {
//            Huesped h = (Huesped) repuesta1.getObjeto();
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

}
