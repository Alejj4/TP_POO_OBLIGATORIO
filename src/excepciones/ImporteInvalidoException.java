package excepciones;

public class ImporteInvalidoException extends Exception{

    public ImporteInvalidoException(String message) {
        super("Importe ingresado inválido.");
    }
}
