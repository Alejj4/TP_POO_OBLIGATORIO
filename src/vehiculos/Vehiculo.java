package vehiculos;

import java.time.LocalDate;

public abstract class Vehiculo {
    private String codigo;
    private LocalDate fechaAlta;
    private double kmRecorridos;
    private Estado estado;

    public Vehiculo(String codigo, LocalDate fechaAlta) {
        this.codigo = codigo;
        this.fechaAlta = fechaAlta;
        this.kmRecorridos = 0.0;
        this.estado = Estado.DISPONIBLE;
    }

    public void registrarUso(int minutos) {
        cambiarEstado(Estado.DISPONIBLE);
    }

    public boolean estaDisponible() {
        return this.estado == Estado.DISPONIBLE;
    }

    public abstract double calcularCosto(int minutos);

    public String getCodigo() {
        return this.codigo;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public void cambiarEstado(Estado nuevoEstado) {
        this.estado = nuevoEstado;
    }
}
