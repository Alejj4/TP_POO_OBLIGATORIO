package viajes;

import java.time.Duration;

public class PlanMensual implements Promocion{
    private String nombre = "Plan mensual";
    public static final int TOPE_VIAJES = 20;
    private int minutosMaximos = 30;

    @Override
    public double calcularDescuento(Viaje viaje, double costo) {
        if (!viaje.getUsuario().getTienePlanMensual() || viaje.getUsuario().getViajesConPlanUsados() >= TOPE_VIAJES) {
            return 0.0;
        } else {
            long duracion = Duration.between(viaje.getFechaHoraInicio(), viaje.getFechaHoraFin()).toMinutes();
            if (duracion <= minutosMaximos) {
                return costo;
            } else
                return viaje.getVehiculo().calcularCosto(minutosMaximos);
        }
    }

    @Override
    public String getNombre() {
        return nombre;
    }
}
