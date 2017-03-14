/**
 * Clase para controlar la excpecion de que el sistema se resetee
 * @author marina
 */
public class SystemResetException extends Exception{
    public SystemResetException(String message){
        super(message);
    }
}
