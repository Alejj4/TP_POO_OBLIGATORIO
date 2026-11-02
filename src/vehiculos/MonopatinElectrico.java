package vehiculos;

import java.time.LocalDate;

public class MonopatinElectrico extends Vehiculo {
    private int nivelBateria;
    private double autonomia;
    private double velocidadMaxima;

    public MonopatinElectrico(String codigo, LocalDate fechaAlta, double velocidadMax) {
        super(codigo, fechaAlta);
        this.velocidadMaxima = velocidadMax;
        this.nivelBateria = 100;
    }

    @Override
    public boolean estaDisponible() {
        return super.estaDisponible() && this.nivelBateria >= 15;
    }

    @Override
    public double calcularCosto(int minutos) {
        if (minutos <= 2) {
            return 0;
        }
        if (minutos > 1440) {
            return 150000;
        }
        if (minutos <= 120) {
            return 300 + (50 * minutos);
        }
        int minutosExcedentes = minutos - 120;
        return 300 + (50 * 120) + (50 * 1.5 * minutosExcedentes);
    }

    @Override
    public void registrarUso(int minutos) {
        nivelBateria -= (int) (0.5 * minutos);
        if (nivelBateria < 0) {
            nivelBateria = 0;
        }

        if (nivelBateria < 15) {
            cambiarEstado(Estado.BATERIA_BAJA);
        } else {
            cambiarEstado(Estado.DISPONIBLE);
        }
    }

    public int getbateria(){
        return this.nivelBateria;
    }

    public void setbateria(int bateria){
         this.nivelBateria=bateria;
    }


}
