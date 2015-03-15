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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JulioLopez
 */
@WebServlet(name = "ServidorHotel", urlPatterns = {"/consultarHuesped"}, loadOnStartup = -1)
public class ServidorHotel extends HttpServlet {
    
    ArrayList<Huesped> huespedes = new ArrayList<>();
    HashMap<String,ArrayList<Reserva>> reservasCliente = new HashMap<>();
    ArrayList<Reserva> reservas = new ArrayList<>();

    
    
    
    
    

    /**
     * Initilizes the servlet with config file and saved data if it exist
     * @param config -> Configuration of server
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        //Añadiriamos aquí un archivo file para cargar al inciar el servlet.
        //Crearemos los ejemplos en el caso de que no encuentre un archivo.    
        
        super.init(config); 
        
        
        
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
        
        Huesped huesped = new Huesped(); // Constructor vacio, datos ejemplo
        Reserva reserva = new Reserva(); // Constructor vacio, datos ejemplo
        System.out.println("Iniciando el server....");
        
        huespedes.add(huesped);
        
        reservas.add(reserva);      
        reservasCliente.put(reserva.getNIF(), reservas);
        
        
        String webPath = request.getServletPath();
        ArrayList<String> resultados = new ArrayList<>();
        
        System.out.println("Peticion recibida -->" +  webPath);
        
        switch(webPath){
            case "/consultarHuesped":
                int nParametros = request.getParameterMap().size();
                if(nParametros > 0){
                    //String a consultar con el servidor
                     String consulta = request.getParameter("NIF");
                     
                     
                     //Consultamos con nuestro ArrayList de servidores.
                     
                     for(Huesped h:huespedes){
                         if (h.getNif().equals(consulta))
                             resultados.add(h.toString());
                     }
                }
                
        }
        
        if (resultados.isEmpty()){
            resultados.add("No se ha encontrado nada");
        }
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            for (String i: resultados){
                out.println(i);
            }
            
        }
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

}
