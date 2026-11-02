package vehiculos;

import java.time.LocalDate;

public class BicicletaElectrica extends Vehiculo {
    private int nivelBateria;
    private double autonomia;

    public BicicletaElectrica(String codigo, LocalDate fechaAlta, double autonomia) {
        super(codigo, fechaAlta);
        this.autonomia = autonomia;
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
            return 100000;
        }
        if (minutos <= 120) {
            return 300 + (45 * minutos);
        }
        int minutosExcedentes = minutos - 120;
        return 300 + (45 * 120) + (45 * 1.5 * minutosExcedentes);
  }

    public int getbateria(){
        return this.nivelBateria;
    }

    public void setbateria(int bateria){
        this.nivelBateria=bateria;
    }
}
