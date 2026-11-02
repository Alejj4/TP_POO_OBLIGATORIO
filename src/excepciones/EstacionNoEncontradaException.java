package excepciones;

public class EstacionNoEncontradaException extends Exception {

    public EstacionNoEncontradaException(String message) {
        super("Estación no encontrada.");
    }
}
