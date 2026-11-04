package controladores;
import repositorio.Repositorio;
import viajes.*;
import usuarios.Usuario;
import excepciones.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import vehiculos.Estacion;

public class ControladorReporte {

    private Repositorio repositorio;

    public ControladorReporte(Repositorio repositorio) {
        this.repositorio = repositorio; }

    public List<Viaje> reporteViajesPorUsuario(String documento) throws UsuarioNoEncontradoException {
        Usuario usuario = repositorio.buscarUsuario(documento);
        if (usuario == null){
            throw new UsuarioNoEncontradoException("No existe el usuario " + documento);
        }
        List<Viaje> viajes = new ArrayList<>(usuario.getViajes());
        Collections.sort(viajes);
        return viajes;

    }

    public List<Viaje> reporteUsoPorEstacion(String codigo) throws EstacionNoEncontradaException {
        Estacion estacion = repositorio.buscarEstacion(codigo);
        if (estacion == null) {
            throw new EstacionNoEncontradaException("No existe la estación " + codigo);
        }
        List<Viaje> resultado = repositorio.viajesDeEstacion(estacion);
        Collections.sort(resultado);
        return resultado;
    }
}

