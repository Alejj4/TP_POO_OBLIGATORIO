package vehiculos;

import java.time.LocalDate;

public class BicicletaComun extends Vehiculo {
    private int tamanioRodado;

    public BicicletaComun(String codigo, LocalDate fechaAlta, int rodado, Estado estado) {
        super(codigo, fechaAlta);
        this.tamanioRodado = rodado;
        this.cambiarEstado(estado);
    }

    @Override
    public double calcularCosto(int minutos) {
        if (minutos <= 2) {
            return 0;
        }
        if (minutos > 1440) {
            return 50000;
        }
        if (minutos <= 120) {
            return 200 + (20 * minutos);
        }
        int minutosExcedentes = minutos - 120;
        return 200 + (20 * 120) + (20 * 1.5 * minutosExcedentes);
    }

    public int getTamanioRodado() {
        return tamanioRodado;
    }
}
