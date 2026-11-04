package vistas;

import menu.MenuAdmin;
import menu.MenuUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Swing {
    private JPanel Main;
    private JButton botonSalir;
    private JButton botonUsuario;
    private JButton BotonAdmin;
    private JPanel menuPrincipal;

    private JPanel panelUsuario;
    private JButton botonRegistrarUsuario;
    private JButton botonAgregarSaldo;
    private JButton botonRetirarVehiculo;
    private JButton botonDevolverVehiculo;
    private JButton botonPlanMensual;
    private JButton botonVolverUsuario;

    private JPanel panelAdmin;
    private JButton botonBuscarUsuario;
    private JButton botonAgregarEstacion;
    private JButton botonVolverAdmin;
    private JButton reporteEstacionesButton;
    private JButton reporteViajesButton;
    private JButton reporteUsuariosButton;
    private JButton botonReporteViajes;
    private JButton botonReporteEstacion;

    private MenuUsuario menuUsuario;
    private MenuAdmin menuAdmin;

    public Swing(MenuUsuario menuUsuario, MenuAdmin menuAdmin) {
        this.menuUsuario = menuUsuario;
        this.menuAdmin = menuAdmin;

        botonUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) Main.getLayout();
                cl.show(Main, "vistaUsuario");
            }
        });
        botonVolverUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) Main.getLayout();
                cl.show(Main, "Card1");
            }
        });
        botonRegistrarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuUsuario.registrarUsuario();
            }
        });
        botonAgregarSaldo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuUsuario.agregarSaldo();
            }
        });
        botonRetirarVehiculo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuUsuario.retirarVehiculo();
            }
        });
        botonDevolverVehiculo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuUsuario.devolverVehiculo();
            }
        });
        botonPlanMensual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuUsuario.suscribirPlanMensual();
            }
        });

        BotonAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) Main.getLayout();
                cl.show(Main, "vistaAdmin");
            }
        });
        botonVolverAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) Main.getLayout();
                cl.show(Main, "Card1");
            }
        });
        botonBuscarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuAdmin.buscarUsuario();
            }
        });
        botonAgregarEstacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuAdmin.agregarEstacion();
            }
        });

        botonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Hasta luego");
                System.exit(0);
            }
        });

        reporteEstacionesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuAdmin.verReporteUsoPorEstacion();
            }
        });
        reporteViajesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuAdmin.verReporteViajesPorUsuario();
            }
        });
    }

    public void iniciar() {
        Font fuente = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("OptionPane.messageFont", fuente);
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("TextField.font", fuente);
        UIManager.put("Label.font", fuente);

        JFrame frame = new JFrame("Schipani Express");
        java.net.URL urlLogo = getClass().getResource("/vistas/logo.png");
        if (urlLogo != null) {
            frame.setIconImage(new ImageIcon(urlLogo).getImage());
        }
        frame.setContentPane(Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(540, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
