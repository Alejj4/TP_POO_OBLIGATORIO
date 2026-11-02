package excepciones;

public class VehiculoNoDisponibleException extends Exception {

    public VehiculoNoDisponibleException(String message) {
        super("Vehículo no disponible.");
    }
}
