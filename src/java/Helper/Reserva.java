/*
 * Sistemas distribuidos - Practica Hotel
 * Universidad Rey Juan Carlos, Mostoles
 * Realizada por Julio Lopez González y Manuel Gómez Pérez
 * Doble Grado GII + ADE
 * https://github.com/giiade/hotel-distribuido
 */
package Helper;

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Contendra  el objeto Reserva y sus metodos.
 * Contenido:
 * Huesped
 * Habitacion
 * Fecha de entrada
 * Fecha de salidad
 */
public class Reserva {
    
    private final String LOG = getClass().getCanonicalName();
    
    private String NIF;
    private int habitacionId;
    private Date fEntrada;
    private Date fSalida;
    
    
     /**
     * Constructor de clase
     * Inicializa automaticamente el huesped a uno por defecto.
     */
    public Reserva(){
        this.NIF="12345678A";
        this.habitacionId=101;
        try {
            this.fEntrada = Const.DATE_FORMAT.parse("1/03/2015");
            this.fSalida = Const.DATE_FORMAT.parse("2/03/2015");
        } catch (ParseException ex) {
            Logger.getLogger(LOG).log(Level.SEVERE, "Error parseando Constructor Vacio \n{0}", ex.getMessage());
        }
 }

    /**
     * @return  NIF
     */
    public String getNIF() {
        return NIF;
    }

    /**
     * @param NIF El NIF a guardar
     */
    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    /**
     * @return El numero de habitacion
     */
    public int getHabitacionId() {
        return habitacionId;
    }

    /**
     * @param habitacionId habitacion a reservar
     */
    public void setHabitacionId(int habitacionId) {
        this.habitacionId = habitacionId;
    }

    /**
     * @return a fecha de entrada
     */
    public Date getfEntrada() {
        return fEntrada;
    }

    /**
     * @param fEntrada Guarda la fecha de entrada
     */
    public void setfEntrada(Date fEntrada) {
        this.fEntrada = fEntrada;
    }

    /**
     * @return la fecha de salida
     */
    public Date getfSalida() {
        return fSalida;
    }

    /**
     * @param fSalida la fecha de salida
     */
    public void setfSalida(Date fSalida) {
        this.fSalida = fSalida;
    }
    
}
