/*
 * Sistemas distribuidos - Practica Hotel
 * Universidad Rey Juan Carlos, Mostoles
 * Realizada por Julio Lopez González y Manuel Gómez Pérez
 * Doble Grado GII + ADE
 * https://github.com/giiade/hotel-distribuido
 */
package Servidor;

/**
 * Esta clase contiene el objeto respuesta que mandaremos desde el servidor.
 * @author JulioLopez
 */
public class ObjetoRespuesta {
    
    //Peticion correcta o no
    private Boolean success;
    
    //Que tipo de objeto vamos a devolver en el XML Interno
    //private String Type;
    
    //XML Interno con todo lo que tenemos que meter
    private Object objeto ;
            
    private String error;

    /**
     * Constructor vacio de ObjetoRespuesta
     */
    public ObjetoRespuesta() {
    }

    /**
     *
     * @param success
     * @param o 
     * @param error
     */
    public ObjetoRespuesta(Boolean success, Object o, String error) {
        this.success = success;
        //this.Type = Type;
        this.objeto = o;
        this.error = error;
    }

    /**
     * @return the success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

//    /**
//     * @return the Type
//     */
//    public String getType() {
//        return Type;
//    }
//
//    /**
//     * @param Type the Type to set
//     */
//    public void setType(String Type) {
//        this.Type = Type;
//    }

 

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return the objeto
     */
    public Object getObjeto() {
        return objeto;
    }

    /**
     * @param objeto the objeto to set
     */
    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }
    
    
    
   
    
    
    
}
