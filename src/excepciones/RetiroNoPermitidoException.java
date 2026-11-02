package excepciones;

public class RetiroNoPermitidoException extends Exception {

    public RetiroNoPermitidoException(String message) {
        super("No se puede retirar el vehículo.");
    }
}
