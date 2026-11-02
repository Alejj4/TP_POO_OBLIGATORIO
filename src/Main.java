import controladores.ControladorCuenta;
import controladores.ControladorEstacion;
import controladores.ControladorReporte;
import controladores.ControladorViajes;
import menu.MenuAdmin;
import menu.MenuUsuario;
import repositorio.Repositorio;
import vistas.Swing;

public class Main {
    public static void main(String[] args) {
        Repositorio repositorio = new Repositorio();
        repositorio.cargarUsuarios();
        repositorio.cargarEstaciones();
        repositorio.cargarVehiculos();

        ControladorCuenta controladorCuenta = new ControladorCuenta(repositorio);
        ControladorViajes controladorViajes = new ControladorViajes(repositorio);
        ControladorEstacion controladorEstacion = new ControladorEstacion(repositorio);
        ControladorReporte controladorReporte = new ControladorReporte(repositorio);

        MenuUsuario menuUsuario = new MenuUsuario(controladorCuenta, controladorViajes);
        MenuAdmin menuAdmin = new MenuAdmin(controladorCuenta, controladorEstacion, controladorReporte);

        new Swing(menuUsuario, menuAdmin).iniciar();
    }
}
