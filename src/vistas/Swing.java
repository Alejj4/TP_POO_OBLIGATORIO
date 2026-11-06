package vistas;

import controladores.ControladorCuenta;
import controladores.ControladorEstacion;
import controladores.ControladorReporte;
import controladores.ControladorViajes;
import excepciones.*;
import menu.MenuAdmin;
import menu.MenuUsuario;
import repositorio.Repositorio;
import usuarios.Usuario;
import vehiculos.*;
import viajes.PlanMensual;
import viajes.Viaje;
import vistas.Tema.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interfaz gráfica principal de Schipani Express.
 * Ventana de escritorio (1180 x 760 px) construida íntegramente con Java Swing estándar,
 * sin diálogos modales JOptionPane y con feedback contextual en cada pantalla.
 */
public class Swing {

    private final MenuUsuario menuUsuario;
    private final MenuAdmin menuAdmin;
    private final ControladorCuenta ctrlCuenta;
    private final ControladorViajes ctrlViajes;
    private final ControladorEstacion ctrlEstacion;
    private final ControladorReporte ctrlReporte;
    private final Repositorio repositorio;

    private JFrame frame;
    private final CardLayout rootLayout = new CardLayout();
    private final JPanel rootPanel = new JPanel(rootLayout);

    // Navegación interna de Usuario y Administrador
    private final CardLayout userContentLayout = new CardLayout();
    private final JPanel userContentPanel = new JPanel(userContentLayout);
    private final Map<String, BotonNavegacion> userNavButtons = new HashMap<>();
    private final Map<String, JButton> userDefaultButtons = new HashMap<>();

    private final CardLayout adminContentLayout = new CardLayout();
    private final JPanel adminContentPanel = new JPanel(adminContentLayout);
    private final Map<String, BotonNavegacion> adminNavButtons = new HashMap<>();
    private final Map<String, JButton> adminDefaultButtons = new HashMap<>();

    // Estado compartido en barra lateral de Usuario
    private String dniActivo = "12345678";
    private JLabel lblSidebarNombre;
    private JLabel lblSidebarDni;
    private JLabel lblSidebarEmail;
    private JLabel lblSidebarSaldo;
    private JLabel lblSidebarPlan;
    private JLabel lblHomeRedBadge;

    // Controles dinámicos para refrescar vistas al cambiar de pestaña
    private JComboBox<String> comboEstacionRetiro;
    private JComboBox<String> comboEstacionDevolucion;
    private CampoTexto txtDniRetiro;
    private CampoTexto txtCodigoVehiculoRetiro;
    private JLabel lblTarifaPreview;
    private JLabel lblMinimoPreview;
    private JLabel lblEstacionTitulo;
    private JLabel lblEstacionSubtitulo;
    private JLabel lblEstacionOcupacion;
    private JPanel gridVehiculosEstacion;
    private BannerEstado bannerRetiro;

    private CampoTexto txtDniDevolucion;
    private JLabel lblViajeActivoInfo;
    private JPanel contenedorComprobanteViaje;
    private BannerEstado bannerDevolucion;

    private CampoTexto txtDniSaldo;
    private CampoTexto txtImporteSaldo;
    private JComboBox<String> comboMedioPago;
    private JLabel lblSaldoAnterior;
    private JLabel lblSaldoNuevo;
    private JLabel lblMedioAsociado;
    private BannerEstado bannerSaldo;

    private CampoTexto txtDniPlan;
    private JLabel lblEstadoPlanBadge;
    private JLabel lblProgresoPlanTexto;
    private BarraBateria barraProgresoPlan;
    private JLabel lblSaldoPostPlan;
    private BannerEstado bannerPlan;

    private CampoTexto txtNombreRegistro;
    private CampoTexto txtDniRegistro;
    private CampoTexto txtEmailRegistro;
    private BannerEstado bannerRegistro;

    // Controles dinámicos de Administrador
    private CampoTexto txtDniBuscarAdmin;
    private JPanel panelResultadoUsuarioAdmin;
    private BannerEstado bannerBuscarAdmin;

    private CampoTexto txtCodigoEstacionNueva;
    private CampoTexto txtDireccionEstacionNueva;
    private CampoTexto txtCoordEstacionNueva;
    private CampoTexto txtCapacidadEstacionNueva;
    private JPanel listaEstacionesAdmin;
    private BannerEstado bannerEstacionAdmin;

    private CampoTexto txtDniReporteUsuario;
    private DefaultTableModel modeloTablaViajesUsuario;
    private JLabel lblSubtituloReporteUsuario;
    private JPanel contenedorTablaViajesUsuario;
    private BannerEstado bannerReporteUsuario;

    private JPanel selectorTarjetasEstacionReporte;
    private DefaultTableModel modeloTablaUsoEstacion;
    private JLabel lblSubtituloUsoEstacion;
    private JPanel contenedorTablaUsoEstacion;
    private BannerEstado bannerReporteEstacion;
    private String codigoEstacionReporteSeleccionada = "E01";

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Swing(MenuUsuario menuUsuario, MenuAdmin menuAdmin) {
        this.menuUsuario = menuUsuario;
        this.menuAdmin = menuAdmin;
        this.ctrlCuenta = menuUsuario.getControladorCuenta();
        this.ctrlViajes = menuUsuario.getControladorViajes();
        this.ctrlEstacion = menuAdmin.getControladorEstacion();
        this.ctrlReporte = menuAdmin.getControladorReporte();
        this.repositorio = ctrlEstacion.getRepositorio();
    }

    public void iniciar() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        frame = new JFrame("Schipani Express — Sistema de Movilidad Compartida CABA");
        java.net.URL urlLogo = getClass().getResource("/vistas/logo.png");
        if (urlLogo != null) {
            frame.setIconImage(new ImageIcon(urlLogo).getImage());
        }

        rootPanel.setBackground(Tema.FONDO_LIENZO);
        rootPanel.add(crearPantallaHome(), "HOME");
        rootPanel.add(crearShellUsuario(), "USUARIO");
        rootPanel.add(crearShellAdmin(), "ADMIN");

        configurarAtajosGlobales();

        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Tema.VENTANA_ANCHO, Tema.VENTANA_ALTO);
        frame.setMinimumSize(new Dimension(1100, 720));
        frame.setLocationRelativeTo(null);

        mostrarHome();
        frame.setVisible(true);
    }

    private void configurarAtajosGlobales() {
        InputMap im = rootPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = rootPanel.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "volverMenuPrincipal");
        am.put("volverMenuPrincipal", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarHome();
            }
        });
    }

    // =========================================================================
    // PANTALLA 1: MENÚ PRINCIPAL ("HOME")
    // =========================================================================
    private JPanel crearPantallaHome() {
        JPanel home = new JPanel(new BorderLayout(0, 24));
        home.setBackground(Tema.FONDO_LIENZO);
        home.setBorder(new EmptyBorder(32, 44, 32, 44));

        // 1. Cabecera editorial
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDE_SUTIL),
                new EmptyBorder(0, 0, 20, 0)
        ));

        JPanel marcaIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        marcaIzq.setOpaque(false);

        JLabel insignia = new JLabel("SE", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Tema.activarAntialiasing(g2);
                g2.setColor(Tema.ACCION_PRIMARIA);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        insignia.setPreferredSize(new Dimension(48, 48));
        insignia.setForeground(Color.WHITE);
        insignia.setFont(Tema.FUENTE_TITULO_1);

        JPanel titulosMarca = new JPanel(new GridLayout(2, 1, 0, 2));
        titulosMarca.setOpaque(false);

        JPanel filaTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filaTitulo.setOpaque(false);
        JLabel lblNombreApp = new JLabel("SCHIPANI EXPRESS");
        lblNombreApp.setFont(Tema.FUENTE_TITULO_1);
        lblNombreApp.setForeground(Tema.TEXTO_PRINCIPAL);
        filaTitulo.add(lblNombreApp);
        filaTitulo.add(Tema.crearPastilla("RED CABA ACTIVA", Tema.ACCION_TINTE, Tema.ACCION_PRIMARIA));

        JLabel lblBajada = new JLabel("Bicicletas comunes, bicicletas eléctricas y monopatines compartidos en estaciones de Buenos Aires");
        lblBajada.setFont(Tema.FUENTE_SUBTITULO);
        lblBajada.setForeground(Tema.TEXTO_SECUNDARIO);

        titulosMarca.add(filaTitulo);
        titulosMarca.add(lblBajada);

        marcaIzq.add(insignia);
        marcaIzq.add(titulosMarca);

        JPanel accionesDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 4));
        accionesDer.setOpaque(false);
        lblHomeRedBadge = new JLabel();
        lblHomeRedBadge.setFont(Tema.FUENTE_CUERPO_BOLD);
        lblHomeRedBadge.setForeground(Tema.TEXTO_SECUNDARIO);

        BotonModerna btnSalir = BotonModerna.peligroSuave("Salir del sistema");
        btnSalir.addActionListener(e -> System.exit(0));

        accionesDer.add(lblHomeRedBadge);
        accionesDer.add(btnSalir);

        header.add(marcaIzq, BorderLayout.WEST);
        header.add(accionesDer, BorderLayout.EAST);

        // 2. Cuerpo central: Título y 2 grandes tarjetas de Rol
        JPanel centro = new JPanel(new BorderLayout(0, 20));
        centro.setOpaque(false);

        JPanel encabezadoSeccion = new JPanel(new GridLayout(2, 1, 0, 4));
        encabezadoSeccion.setOpaque(false);
        JLabel lblTag = new JLabel("SELECCIÓN DE PERFIL OPERATIVO");
        lblTag.setFont(Tema.FUENTE_ETIQUETA);
        lblTag.setForeground(Tema.ACCION_PRIMARIA);
        JLabel lblPregunta = new JLabel("¿Cómo querés ingresar a Schipani Express?");
        lblPregunta.setFont(Tema.FUENTE_HERO);
        lblPregunta.setForeground(Tema.TEXTO_PRINCIPAL);
        encabezadoSeccion.add(lblTag);
        encabezadoSeccion.add(lblPregunta);

        JPanel grillaRoles = new JPanel(new GridLayout(1, 2, 28, 0));
        grillaRoles.setOpaque(false);

        // Tarjeta Usuario
        TarjetaPanel cardUsuario = new TarjetaPanel(new BorderLayout(0, 18))
                .configurarEstilo(Color.WHITE, Tema.BORDE_SUTIL, Tema.ACCION_PRIMARIA, true);
        cardUsuario.setBorder(new EmptyBorder(28, 30, 28, 30));

        JPanel infoUsuario = new JPanel();
        infoUsuario.setLayout(new BoxLayout(infoUsuario, BoxLayout.Y_AXIS));
        infoUsuario.setOpaque(false);

        JPanel topPillsUser = new JPanel(new BorderLayout());
        topPillsUser.setOpaque(false);
        topPillsUser.add(Tema.crearPastilla("MODO VIAJERO · USUARIO", Tema.ACCION_TINTE, Tema.ACCION_PRIMARIA), BorderLayout.WEST);
        JLabel lblOpsUser = new JLabel("5 OPERACIONES");
        lblOpsUser.setFont(Tema.FUENTE_MONO_CHICA);
        lblOpsUser.setForeground(Tema.TEXTO_TERCIARIO);
        topPillsUser.add(lblOpsUser, BorderLayout.EAST);

        JLabel tituloUser = new JLabel("Terminal de Usuario");
        tituloUser.setFont(Tema.FUENTE_TITULO_1);
        tituloUser.setForeground(Tema.TEXTO_PRINCIPAL);
        tituloUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descUser = new JLabel("<html>Registrate con tu DNI, acreditá saldo con tu medio de pago preferido, consultá en tiempo real los vehículos y niveles de batería en E01, E02 y E03 para retirarlos, devolvelos al finalizar o suscribite al Plan Mensual.</html>");
        descUser.setFont(Tema.FUENTE_SUBTITULO);
        descUser.setForeground(Tema.TEXTO_SECUNDARIO);
        descUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        TarjetaPanel cajaPuntosUser = new TarjetaPanel(new GridLayout(2, 2, 12, 8))
                .configurarEstilo(Tema.FONDO_LIENZO, Tema.BORDE_SUTIL, null, false);
        cajaPuntosUser.setBorder(new EmptyBorder(14, 16, 14, 16));
        cajaPuntosUser.add(crearItemChecklist("✓ Alta rápida por DNI y email"));
        cajaPuntosUser.add(crearItemChecklist("✓ Carga de saldo y medio de pago"));
        cajaPuntosUser.add(crearItemChecklist("✓ Retiro con vista de batería"));
        cajaPuntosUser.add(crearItemChecklist("✓ Plan Mensual $15.000 (20 viajes)"));

        topPillsUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        cajaPuntosUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoUsuario.add(topPillsUser);
        infoUsuario.add(Box.createVerticalStrut(14));
        infoUsuario.add(tituloUser);
        infoUsuario.add(Box.createVerticalStrut(8));
        infoUsuario.add(descUser);
        infoUsuario.add(Box.createVerticalStrut(18));
        infoUsuario.add(cajaPuntosUser);

        BotonModerna btnEntrarUsuario = BotonModerna.primarioVerde("Ingresar como Usuario  →");
        btnEntrarUsuario.addActionListener(e -> abrirModoUsuario("RETIRAR"));

        cardUsuario.add(infoUsuario, BorderLayout.CENTER);
        cardUsuario.add(btnEntrarUsuario, BorderLayout.SOUTH);

        // Tarjeta Administrador
        TarjetaPanel cardAdmin = new TarjetaPanel(new BorderLayout(0, 18))
                .configurarEstilo(Color.WHITE, Tema.BORDE_SUTIL, Tema.SIDEBAR_FONDO, true);
        cardAdmin.setBorder(new EmptyBorder(28, 30, 28, 30));

        JPanel infoAdmin = new JPanel();
        infoAdmin.setLayout(new BoxLayout(infoAdmin, BoxLayout.Y_AXIS));
        infoAdmin.setOpaque(false);

        JPanel topPillsAdmin = new JPanel(new BorderLayout());
        topPillsAdmin.setOpaque(false);
        topPillsAdmin.add(Tema.crearPastilla("GESTIÓN Y AUDITORÍA", Tema.ADMIN_TINTE, Tema.ADMIN_HOVER), BorderLayout.WEST);
        JLabel lblOpsAdmin = new JLabel("4 OPERACIONES");
        lblOpsAdmin.setFont(Tema.FUENTE_MONO_CHICA);
        lblOpsAdmin.setForeground(Tema.TEXTO_TERCIARIO);
        topPillsAdmin.add(lblOpsAdmin, BorderLayout.EAST);

        JLabel tituloAdmin = new JLabel("Consola de Administración");
        tituloAdmin.setFont(Tema.FUENTE_TITULO_1);
        tituloAdmin.setForeground(Tema.TEXTO_PRINCIPAL);
        tituloAdmin.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descAdmin = new JLabel("<html>Consultá fichas completas de usuarios por documento (saldo, estado del plan y viajes), habilitá nuevas estaciones en la red porteña y auditá los reportes históricos de viajes por persona o por estación.</html>");
        descAdmin.setFont(Tema.FUENTE_SUBTITULO);
        descAdmin.setForeground(Tema.TEXTO_SECUNDARIO);
        descAdmin.setAlignmentX(Component.LEFT_ALIGNMENT);

        TarjetaPanel cajaPuntosAdmin = new TarjetaPanel(new GridLayout(2, 2, 12, 8))
                .configurarEstilo(Tema.FONDO_LIENZO, Tema.BORDE_SUTIL, null, false);
        cajaPuntosAdmin.setBorder(new EmptyBorder(14, 16, 14, 16));
        cajaPuntosAdmin.add(crearItemChecklist("✓ Búsqueda integral por DNI"));
        cajaPuntosAdmin.add(crearItemChecklist("✓ Alta de estaciones y capacidad"));
        cajaPuntosAdmin.add(crearItemChecklist("✓ Reporte de viajes por usuario"));
        cajaPuntosAdmin.add(crearItemChecklist("✓ Reporte de uso por estación"));

        topPillsAdmin.setAlignmentX(Component.LEFT_ALIGNMENT);
        cajaPuntosAdmin.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoAdmin.add(topPillsAdmin);
        infoAdmin.add(Box.createVerticalStrut(14));
        infoAdmin.add(tituloAdmin);
        infoAdmin.add(Box.createVerticalStrut(8));
        infoAdmin.add(descAdmin);
        infoAdmin.add(Box.createVerticalStrut(18));
        infoAdmin.add(cajaPuntosAdmin);

        BotonModerna btnEntrarAdmin = BotonModerna.primarioPizarra("Ingresar como Administrador  →");
        btnEntrarAdmin.addActionListener(e -> abrirModoAdmin("BUSCAR"));

        cardAdmin.add(infoAdmin, BorderLayout.CENTER);
        cardAdmin.add(btnEntrarAdmin, BorderLayout.SOUTH);

        grillaRoles.add(cardUsuario);
        grillaRoles.add(cardAdmin);

        centro.add(encabezadoSeccion, BorderLayout.NORTH);
        centro.add(grillaRoles, BorderLayout.CENTER);

        // 3. Pie de Tarifario Oficial
        TarjetaPanel footerTarifas = new TarjetaPanel(new BorderLayout())
                .configurarEstilo(Color.WHITE, Tema.BORDE_SUTIL, null, false);
        footerTarifas.setBorder(new EmptyBorder(14, 22, 14, 22));

        JPanel tarifasIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        tarifasIzq.setOpaque(false);
        JLabel lblTarifasTag = new JLabel("TARIFAS VIGENTES:");
        lblTarifasTag.setFont(Tema.FUENTE_ETIQUETA);
        lblTarifasTag.setForeground(Tema.TEXTO_TERCIARIO);
        tarifasIzq.add(lblTarifasTag);
        tarifasIzq.add(crearEtiquetaTarifa("Bicicleta común:", "$200 + $20/min"));
        tarifasIzq.add(crearEtiquetaTarifa("Bicicleta eléctrica:", "$300 + $45/min"));
        tarifasIzq.add(crearEtiquetaTarifa("Monopatín eléctrico:", "$300 + $50/min"));

        JPanel promosDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        promosDer.setOpaque(false);
        promosDer.add(Tema.crearPastilla("Hasta 2 min: Gratis", Tema.ACCION_TINTE, Tema.ACCION_PRIMARIA));
        promosDer.add(Tema.crearPastilla("Primer viaje: 15 min gratis", Tema.ADMIN_TINTE, Tema.ADMIN_HOVER));

        footerTarifas.add(tarifasIzq, BorderLayout.WEST);
        footerTarifas.add(promosDer, BorderLayout.EAST);

        home.add(header, BorderLayout.NORTH);
        home.add(centro, BorderLayout.CENTER);
        home.add(footerTarifas, BorderLayout.SOUTH);

        return home;
    }

    private JLabel crearItemChecklist(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(Tema.FUENTE_CUERPO_BOLD);
        lbl.setForeground(Tema.TEXTO_PRINCIPAL);
        return lbl;
    }

    private JPanel crearEtiquetaTarifa(String vehiculo, String precio) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setOpaque(false);
        JLabel l1 = new JLabel(vehiculo);
        l1.setFont(Tema.FUENTE_CUERPO_BOLD);
        l1.setForeground(Tema.TEXTO_PRINCIPAL);
        JLabel l2 = new JLabel(precio);
        l2.setFont(Tema.FUENTE_MONO_CHICA);
        l2.setForeground(Tema.ACCION_PRIMARIA);
        p.add(l1);
        p.add(l2);
        return p;
    }

    // =========================================================================
    // SHELL DE USUARIO (BARRA LATERAL + 5 PANTALLAS)
    // =========================================================================
    private JPanel crearShellUsuario() {
        JPanel shell = new JPanel(new BorderLayout());
        shell.setBackground(Tema.FONDO_LIENZO);

        // Barra lateral izquierda (265 px)
        JPanel sidebar = new JPanel(new BorderLayout(0, 16));
        sidebar.setBackground(Tema.SIDEBAR_FONDO);
        sidebar.setPreferredSize(new Dimension(Tema.SIDEBAR_ANCHO, Tema.VENTANA_ALTO));
        sidebar.setBorder(new EmptyBorder(22, 18, 22, 18));

        JPanel topSidebar = new JPanel();
        topSidebar.setLayout(new BoxLayout(topSidebar, BoxLayout.Y_AXIS));
        topSidebar.setOpaque(false);

        // Marca en sidebar
        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandRow.setOpaque(false);
        brandRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel badge = new JLabel("SE", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Tema.activarAntialiasing(g2);
                g2.setColor(Tema.ACCION_PRIMARIA);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setPreferredSize(new Dimension(36, 36));
        badge.setForeground(Color.WHITE);
        badge.setFont(Tema.FUENTE_TITULO_2);

        JPanel brandTexts = new JPanel(new GridLayout(2, 1));
        brandTexts.setOpaque(false);
        JLabel lbTitle = new JLabel("SCHIPANI EXPRESS");
        lbTitle.setFont(Tema.FUENTE_CUERPO_BOLD);
        lbTitle.setForeground(Color.WHITE);
        JLabel lbSub = new JLabel("Modo Viajero · CABA");
        lbSub.setFont(Tema.FUENTE_ETIQUETA);
        lbSub.setForeground(Tema.EXITO_ICONO);
        brandTexts.add(lbTitle);
        brandTexts.add(lbSub);
        brandRow.add(badge);
        brandRow.add(brandTexts);

        // Tarjeta de usuario activo en la barra lateral
        TarjetaPanel userCard = new TarjetaPanel(new GridLayout(4, 1, 0, 3))
                .configurarEstilo(Tema.SIDEBAR_TARJETA, Tema.SIDEBAR_BORDE, null, false);
        userCard.setBorder(new EmptyBorder(12, 14, 12, 14));
        userCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        userCard.setMaximumSize(new Dimension(240, 108));

        lblSidebarDni = new JLabel("USUARIO · DNI 12345678");
        lblSidebarDni.setFont(Tema.FUENTE_MONO_CHICA);
        lblSidebarDni.setForeground(Tema.EXITO_ICONO);

        lblSidebarNombre = new JLabel("Juan Pérez");
        lblSidebarNombre.setFont(Tema.FUENTE_TITULO_2);
        lblSidebarNombre.setForeground(Color.WHITE);

        lblSidebarEmail = new JLabel("juan@mail.com");
        lblSidebarEmail.setFont(Tema.FUENTE_CUERPO);
        lblSidebarEmail.setForeground(Tema.TEXTO_SIDEBAR_MUTE);

        JPanel filaSaldoPlan = new JPanel(new BorderLayout());
        filaSaldoPlan.setOpaque(false);
        lblSidebarSaldo = new JLabel("$20.000,00");
        lblSidebarSaldo.setFont(Tema.FUENTE_MONO_MEDIA);
        lblSidebarSaldo.setForeground(Color.WHITE);
        lblSidebarPlan = new JLabel("Sin plan");
        lblSidebarPlan.setFont(Tema.FUENTE_ETIQUETA);
        lblSidebarPlan.setForeground(Tema.TEXTO_SIDEBAR_MUTE);
        filaSaldoPlan.add(lblSidebarSaldo, BorderLayout.WEST);
        filaSaldoPlan.add(lblSidebarPlan, BorderLayout.EAST);

        userCard.add(lblSidebarDni);
        userCard.add(lblSidebarNombre);
        userCard.add(lblSidebarEmail);
        userCard.add(filaSaldoPlan);

        // Botones de navegación de Usuario
        JPanel navList = new JPanel();
        navList.setLayout(new BoxLayout(navList, BoxLayout.Y_AXIS));
        navList.setOpaque(false);
        navList.setAlignmentX(Component.LEFT_ALIGNMENT);

        navList.add(crearTituloSeccionSidebar("OPERACIONES DE VIAJE"));
        navList.add(Box.createVerticalStrut(6));
        navList.add(crearItemNavUsuario("RETIRAR", "01", "Retirar vehículo"));
        navList.add(Box.createVerticalStrut(4));
        navList.add(crearItemNavUsuario("DEVOLVER", "02", "Devolver vehículo"));
        navList.add(Box.createVerticalStrut(14));
        navList.add(crearTituloSeccionSidebar("MI CUENTA Y PLANES"));
        navList.add(Box.createVerticalStrut(6));
        navList.add(crearItemNavUsuario("SALDO", "03", "Agregar saldo"));
        navList.add(Box.createVerticalStrut(4));
        navList.add(crearItemNavUsuario("PLAN", "04", "Plan mensual"));
        navList.add(Box.createVerticalStrut(4));
        navList.add(crearItemNavUsuario("REGISTRAR", "05", "Registrar usuario"));

        topSidebar.add(brandRow);
        topSidebar.add(Box.createVerticalStrut(16));
        topSidebar.add(userCard);
        topSidebar.add(Box.createVerticalStrut(18));
        topSidebar.add(navList);

        BotonModerna btnVolverHome = new BotonModerna(
                "← Cambiar de rol (Esc)",
                Tema.SIDEBAR_TARJETA,
                Tema.SIDEBAR_BORDE,
                Tema.SIDEBAR_FONDO,
                Color.WHITE,
                Tema.SIDEBAR_BORDE
        );
        btnVolverHome.addActionListener(e -> mostrarHome());

        sidebar.add(topSidebar, BorderLayout.NORTH);
        sidebar.add(btnVolverHome, BorderLayout.SOUTH);

        // Contenedor de vistas derechas
        userContentPanel.setOpaque(false);
        userContentPanel.setBorder(new EmptyBorder(26, 32, 26, 32));
        userContentPanel.add(crearVistaRetirarVehiculo(), "RETIRAR");
        userContentPanel.add(crearVistaDevolverVehiculo(), "DEVOLVER");
        userContentPanel.add(crearVistaAgregarSaldo(), "SALDO");
        userContentPanel.add(crearVistaPlanMensual(), "PLAN");
        userContentPanel.add(crearVistaRegistrarUsuario(), "REGISTRAR");

        shell.add(sidebar, BorderLayout.WEST);
        shell.add(userContentPanel, BorderLayout.CENTER);
        return shell;
    }

    private JLabel crearTituloSeccionSidebar(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(Tema.FUENTE_ETIQUETA);
        lbl.setForeground(Tema.TEXTO_TERCIARIO);
        lbl.setBorder(new EmptyBorder(0, 8, 0, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private BotonNavegacion crearItemNavUsuario(String clave, String orden, String texto) {
        BotonNavegacion btn = new BotonNavegacion(orden, texto, Tema.ACCION_PRIMARIA);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> abrirModoUsuario(clave));
        userNavButtons.put(clave, btn);
        return btn;
    }

    private BotonNavegacion crearItemNavAdmin(String clave, String orden, String texto) {
        BotonNavegacion btn = new BotonNavegacion(orden, texto, Tema.ADMIN_ACENTO);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> abrirModoAdmin(clave));
        adminNavButtons.put(clave, btn);
        return btn;
    }

    // =========================================================================
    // VISTA USUARIO 1: RETIRAR VEHÍCULO + ESTACIÓN CON BATERÍA Y TIPO
    // =========================================================================
    private JPanel crearVistaRetirarVehiculo() {
        JPanel vista = new JPanel(new BorderLayout(0, 16));
        vista.setOpaque(false);

        JPanel cabecera = crearCabeceraVista(
                "OPERACIÓN DE VIAJE · PASO 1",
                "Retirar vehículo en estación",
                "Elegí la estación donde estás, hacé clic en una unidad disponible y confirmá el retiro con Enter.",
                Tema.ACCION_PRIMARIA
        );

        JPanel cuerpo = new JPanel(new GridBagLayout());
        cuerpo.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Columna izquierda: Formulario (38% ancho)
        gbc.gridx = 0;
        gbc.weightx = 0.38;
        gbc.insets = new Insets(0, 0, 0, 18);

        TarjetaPanel cardForm = new TarjetaPanel(new BorderLayout(0, 14));
        cardForm.setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel campos = new JPanel();
        campos.setLayout(new BoxLayout(campos, BoxLayout.Y_AXIS));
        campos.setOpaque(false);

        JLabel subForm = new JLabel("Datos para el desbloqueo");
        subForm.setFont(Tema.FUENTE_TITULO_2);
        subForm.setForeground(Tema.TEXTO_PRINCIPAL);
        subForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtDniRetiro = new CampoTexto("Ej: 12345678", true);
        txtDniRetiro.setText(dniActivo);
        txtDniRetiro.addActionListener(e -> actualizarSidebarUsuario(txtDniRetiro.getText().trim()));

        comboEstacionRetiro = Tema.crearCombo(new String[]{});
        comboEstacionRetiro.addActionListener(e -> {
            String item = (String) comboEstacionRetiro.getSelectedItem();
            if (item != null && item.contains(" — ")) {
                String cod = item.split(" — ")[0].trim();
                refrescarGrillaVehiculosEstacion(cod);
            }
        });

        txtCodigoVehiculoRetiro = new CampoTexto("Ej: BC01, BE01, ME01", true);
        txtCodigoVehiculoRetiro.setText("BE01");

        // Caja de resumen de tarifa previa al retiro
        TarjetaPanel cajaTarifa = new TarjetaPanel(new GridLayout(3, 1, 0, 4))
                .configurarEstilo(Tema.FONDO_LIENZO, Tema.BORDE_SUTIL, null, false);
        cajaTarifa.setBorder(new EmptyBorder(12, 14, 12, 14));
        lblTarifaPreview = new JLabel("Tarifa seleccionada: $300 + $45/min");
        lblTarifaPreview.setFont(Tema.FUENTE_CUERPO_BOLD);
        lblTarifaPreview.setForeground(Tema.TEXTO_PRINCIPAL);
        lblMinimoPreview = new JLabel("Saldo mínimo requerido (30 min): $1.650,00");
        lblMinimoPreview.setFont(Tema.FUENTE_CUERPO);
        lblMinimoPreview.setForeground(Tema.TEXTO_SECUNDARIO);
        JLabel lblReglaCortesia = new JLabel("★ Hasta 2 min sin cargo · 1.er viaje: 15 min gratis");
        lblReglaCortesia.setFont(Tema.FUENTE_ETIQUETA);
        lblReglaCortesia.setForeground(Tema.ACCION_PRIMARIA);
        cajaTarifa.add(lblTarifaPreview);
        cajaTarifa.add(lblMinimoPreview);
        cajaTarifa.add(lblReglaCortesia);

        agregarCampoAlBox(campos, "Documento del usuario (DNI)", txtDniRetiro);
        agregarCampoAlBox(campos, "Estación de origen", comboEstacionRetiro);
        agregarCampoAlBox(campos, "Código de vehículo (clic en una tarjeta a la derecha)", txtCodigoVehiculoRetiro);
        cajaTarifa.setAlignmentX(Component.LEFT_ALIGNMENT);
        campos.add(Box.createVerticalStrut(6));
        campos.add(cajaTarifa);

        BotonModerna btnConfirmarRetiro = BotonModerna.primarioVerde("Destrabar y retirar vehículo (Enter)");
        btnConfirmarRetiro.addActionListener(e -> ejecutarRetiroVehiculo());
        userDefaultButtons.put("RETIRAR", btnConfirmarRetiro);

        cardForm.add(subForm, BorderLayout.NORTH);
        cardForm.add(campos, BorderLayout.CENTER);
        cardForm.add(btnConfirmarRetiro, BorderLayout.SOUTH);

        cuerpo.add(cardForm, gbc);

        // Columna derecha: Estación y sus vehículos con tipo y batería (62% ancho)
        gbc.gridx = 1;
        gbc.weightx = 0.62;
        gbc.insets = new Insets(0, 0, 0, 0);

        TarjetaPanel cardEstacion = new TarjetaPanel(new BorderLayout(0, 14));
        cardEstacion.setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel headerEstacion = new JPanel(new BorderLayout());
        headerEstacion.setOpaque(false);
        headerEstacion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDE_SUTIL),
                new EmptyBorder(0, 0, 12, 0)
        ));

        JPanel titulosEst = new JPanel(new GridLayout(2, 1, 0, 2));
        titulosEst.setOpaque(false);
        lblEstacionTitulo = new JLabel("Estación E01 — Av. Corrientes 1200");
        lblEstacionTitulo.setFont(Tema.FUENTE_TITULO_2);
        lblEstacionTitulo.setForeground(Tema.TEXTO_PRINCIPAL);
        lblEstacionSubtitulo = new JLabel("Hacé clic sobre un vehículo para seleccionarlo automáticamente");
        lblEstacionSubtitulo.setFont(Tema.FUENTE_CUERPO);
        lblEstacionSubtitulo.setForeground(Tema.TEXTO_SECUNDARIO);
        titulosEst.add(lblEstacionTitulo);
        titulosEst.add(lblEstacionSubtitulo);

        lblEstacionOcupacion = Tema.crearPastilla("4 / 10 disponibles", Tema.ACCION_TINTE, Tema.ACCION_PRIMARIA);
        headerEstacion.add(titulosEst, BorderLayout.WEST);
        headerEstacion.add(lblEstacionOcupacion, BorderLayout.EAST);

        gridVehiculosEstacion = new JPanel(new GridLayout(0, 2, 12, 12));
        gridVehiculosEstacion.setOpaque(false);

        JPanel wrapperGrid = new JPanel(new BorderLayout());
        wrapperGrid.setOpaque(false);
        wrapperGrid.add(gridVehiculosEstacion, BorderLayout.NORTH);

        JScrollPane scrollVehiculos = new JScrollPane(wrapperGrid);
        scrollVehiculos.setBorder(null);
        scrollVehiculos.setOpaque(false);
        scrollVehiculos.getViewport().setOpaque(false);
        scrollVehiculos.getVerticalScrollBar().setUnitIncrement(16);

        cardEstacion.add(headerEstacion, BorderLayout.NORTH);
        cardEstacion.add(scrollVehiculos, BorderLayout.CENTER);

        cuerpo.add(cardEstacion, gbc);

        bannerRetiro = new BannerEstado();

        vista.add(cabecera, BorderLayout.NORTH);
        vista.add(cuerpo, BorderLayout.CENTER);
        vista.add(bannerRetiro, BorderLayout.SOUTH);
        return vista;
    }

    private void refrescarGrillaVehiculosEstacion(String codigoEstacion) {
        Estacion est = repositorio.buscarEstacion(codigoEstacion);
        gridVehiculosEstacion.removeAll();

        if (est == null) {
            gridVehiculosEstacion.revalidate();
            gridVehiculosEstacion.repaint();
            return;
        }

        List<Vehiculo> vehiculos = est.getVehiculosEstacionados();
        lblEstacionTitulo.setText("Estación " + est.getCodigo() + " · " + est.getDireccion());
        lblEstacionSubtitulo.setText("Capacidad máxima: " + est.getCapacidadMaxima() + " anclajes · Coordenadas: " + est.getCoordenadas());
        lblEstacionOcupacion.setText(vehiculos.size() + " / " + est.getCapacidadMaxima() + " en dársena");

        if (vehiculos.isEmpty()) {
            gridVehiculosEstacion.setLayout(new BorderLayout());
            TarjetaPanel vacio = new TarjetaPanel(new GridLayout(3, 1, 0, 6))
                    .configurarEstilo(Tema.FONDO_LIENZO, Tema.BORDE_CAMPO, null, false);
            vacio.setBorder(new EmptyBorder(36, 24, 36, 24));
            JLabel t1 = new JLabel("No hay vehículos estacionados en " + est.getCodigo() + " en este momento", SwingConstants.CENTER);
            t1.setFont(Tema.FUENTE_TITULO_2);
            t1.setForeground(Tema.TEXTO_PRINCIPAL);
            JLabel t2 = new JLabel("Todas las unidades de esta cabecera fueron retiradas. Probá seleccionando otra estación en el desplegable.", SwingConstants.CENTER);
            t2.setFont(Tema.FUENTE_CUERPO);
            t2.setForeground(Tema.TEXTO_SECUNDARIO);
            vacio.add(t1);
            vacio.add(t2);
            gridVehiculosEstacion.add(vacio, BorderLayout.CENTER);
        } else {
            gridVehiculosEstacion.setLayout(new GridLayout(0, 2, 12, 12));
            String seleccionadoActual = txtCodigoVehiculoRetiro.getText().trim();
            boolean algunoCoincide = vehiculos.stream().anyMatch(v -> v.getCodigo().equalsIgnoreCase(seleccionadoActual));
            if (!algunoCoincide) {
                Vehiculo primero = vehiculos.get(0);
                txtCodigoVehiculoRetiro.setText(primero.getCodigo());
                actualizarPreviewTarifaVehiculo(primero);
            }

            for (Vehiculo v : vehiculos) {
                gridVehiculosEstacion.add(crearTarjetaVehiculoEstacion(v));
            }
        }

        gridVehiculosEstacion.revalidate();
        gridVehiculosEstacion.repaint();
    }

    private JPanel crearTarjetaVehiculoEstacion(Vehiculo v) {
        boolean seleccionado = v.getCodigo().equalsIgnoreCase(txtCodigoVehiculoRetiro.getText().trim());
        boolean disponible = v.estaDisponible();

        String tipoNombre;
        String detalleTecnico;
        String tarifaTexto;
        Integer bateria = null;

        if (v instanceof BicicletaComun bc) {
            tipoNombre = "Bicicleta común";
            detalleTecnico = "Rodado " + bc.getTamanioRodado() + " · Tracción mecánica";
            tarifaTexto = "$200 + $20/min";
        } else if (v instanceof BicicletaElectrica be) {
            tipoNombre = "Bicicleta eléctrica";
            bateria = be.getbateria();
            detalleTecnico = "Asistencia eléctrica · " + bateria + "% carga";
            tarifaTexto = "$300 + $45/min";
        } else if (v instanceof MonopatinElectrico me) {
            tipoNombre = "Monopatín eléctrico";
            bateria = me.getbateria();
            detalleTecnico = "Vel. máx. " + (int) me.getVelocidadMaxima() + " km/h · " + bateria + "% carga";
            tarifaTexto = "$300 + $50/min";
        } else {
            tipoNombre = "Vehículo urbano";
            detalleTecnico = "Estándar";
            tarifaTexto = "-";
        }

        Color fondo = seleccionado ? Tema.ACCION_TINTE : Color.WHITE;
        Color borde = seleccionado ? Tema.ACCION_PRIMARIA : Tema.BORDE_SUTIL;

        TarjetaPanel card = new TarjetaPanel(new BorderLayout(0, 8))
                .configurarEstilo(fondo, borde, seleccionado ? Tema.ACCION_PRIMARIA : null, false);
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Fila 1: Código + Estado
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lblCod = Tema.crearPastilla(
                v.getCodigo(),
                seleccionado ? Tema.ACCION_PRIMARIA : Tema.SIDEBAR_FONDO,
                Color.WHITE
        );
        lblCod.setFont(Tema.FUENTE_MONO_CHICA);

        JLabel lblEstado = new JLabel(disponible ? "● Disponible" : "⚠ Batería baja (<15%)");
        lblEstado.setFont(Tema.FUENTE_ETIQUETA);
        lblEstado.setForeground(disponible ? Tema.ACCION_PRIMARIA : Tema.ERROR_TEXTO);

        top.add(lblCod, BorderLayout.WEST);
        top.add(lblEstado, BorderLayout.EAST);

        // Fila 2: Tipo, detalle y barra de batería
        JPanel mid = new JPanel(new GridLayout(3, 1, 0, 3));
        mid.setOpaque(false);
        JLabel lblTipo = new JLabel(tipoNombre);
        lblTipo.setFont(Tema.FUENTE_CUERPO_BOLD);
        lblTipo.setForeground(Tema.TEXTO_PRINCIPAL);

        JLabel lblDet = new JLabel(detalleTecnico);
        lblDet.setFont(Tema.FUENTE_CUERPO);
        lblDet.setForeground(Tema.TEXTO_SECUNDARIO);

        mid.add(lblTipo);
        mid.add(lblDet);

        if (bateria != null) {
            BarraBateria barra = new BarraBateria(bateria);
            mid.add(barra);
        } else {
            JLabel lblMec = new JLabel("Sin batería · Lista para rodar");
            lblMec.setFont(Tema.FUENTE_ETIQUETA);
            lblMec.setForeground(Tema.ACCION_PRIMARIA);
            mid.add(lblMec);
        }

        // Fila 3: Tarifa
        JPanel bot = new JPanel(new BorderLayout());
        bot.setOpaque(false);
        bot.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDE_SUTIL));
        JLabel lblTarifaTag = new JLabel("Tarifa:");
        lblTarifaTag.setFont(Tema.FUENTE_ETIQUETA);
        lblTarifaTag.setForeground(Tema.TEXTO_TERCIARIO);
        JLabel lblTarifaVal = new JLabel(tarifaTexto);
        lblTarifaVal.setFont(Tema.FUENTE_MONO_CHICA);
        lblTarifaVal.setForeground(Tema.TEXTO_PRINCIPAL);
        bot.add(lblTarifaTag, BorderLayout.WEST);
        bot.add(lblTarifaVal, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);
        card.add(mid, BorderLayout.CENTER);
        card.add(bot, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtCodigoVehiculoRetiro.setText(v.getCodigo());
                actualizarPreviewTarifaVehiculo(v);
                String item = (String) comboEstacionRetiro.getSelectedItem();
                if (item != null && item.contains(" — ")) {
                    refrescarGrillaVehiculosEstacion(item.split(" — ")[0].trim());
                }
            }
        });

        return card;
    }

    private void actualizarPreviewTarifaVehiculo(Vehiculo v) {
        if (v instanceof BicicletaComun) {
            lblTarifaPreview.setText("Tarifa seleccionada (" + v.getCodigo() + "): $200 + $20/min");
        } else if (v instanceof BicicletaElectrica) {
            lblTarifaPreview.setText("Tarifa seleccionada (" + v.getCodigo() + "): $300 + $45/min");
        } else if (v instanceof MonopatinElectrico) {
            lblTarifaPreview.setText("Tarifa seleccionada (" + v.getCodigo() + "): $300 + $50/min");
        }
        lblMinimoPreview.setText("Saldo mínimo requerido (30 min): " + Tema.formatearPesos(v.calcularCosto(30)));
    }

    private void ejecutarRetiroVehiculo() {
        String dni = txtDniRetiro.getText().trim();
        String itemEst = (String) comboEstacionRetiro.getSelectedItem();
        String codEstacion = (itemEst != null && itemEst.contains(" — ")) ? itemEst.split(" — ")[0].trim() : "";
        String codVehiculo = txtCodigoVehiculoRetiro.getText().trim().toUpperCase();

        txtDniRetiro.marcarError(false);
        txtCodigoVehiculoRetiro.marcarError(false);

        try {
            Viaje viaje = ctrlViajes.retirarVehiculo(dni, codVehiculo, codEstacion);
            dniActivo = dni;
            actualizarSidebarUsuario(dni);
            poblarCombosEstaciones();
            refrescarGrillaVehiculosEstacion(codEstacion);

            bannerRetiro.mostrar(
                    BannerEstado.Tipo.EXITO,
                    "¡Retiro confirmado! Ya podés empezar a pedalear o rodar.",
                    "Retiraste el vehículo " + viaje.getVehiculo().getCodigo() + " en la estación " + codEstacion +
                            ". Recordá que hasta los 2 minutos la devolución es sin cargo.",
                    "Ir a Devolver vehículo",
                    () -> abrirModoUsuario("DEVOLVER")
            );
        } catch (UsuarioNoEncontradoException ex) {
            txtDniRetiro.marcarError(true);
            bannerRetiro.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "No encontramos ningún usuario con el DNI '" + dni + "'.",
                    "Revisá que el número esté bien escrito o registrá una cuenta nueva en menos de un minuto.",
                    "Registrar usuario",
                    () -> {
                        txtDniRegistro.setText(dni);
                        abrirModoUsuario("REGISTRAR");
                    }
            );
        } catch (EstacionNoEncontradaException ex) {
            bannerRetiro.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "La estación seleccionada no está disponible en la red.",
                    "Elegí una de las estaciones activas del listado desplegable (por ejemplo E01, E02 o E03).",
                    null,
                    null
            );
        } catch (VehiculoNoDisponibleException ex) {
            txtCodigoVehiculoRetiro.marcarError(true);
            bannerRetiro.mostrar(
                    BannerEstado.Tipo.ADVERTENCIA,
                    "El vehículo '" + codVehiculo + "' no está disponible para retiro en " + codEstacion + ".",
                    "Puede que tenga batería menor al 15% o no se encuentre en esta estación. Hacé clic en cualquiera de las tarjetas con indicador verde.",
                    null,
                    null
            );
        } catch (RetiroNoPermitidoException ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "";
            if (msg.toLowerCase().contains("saldo")) {
                bannerRetiro.mostrar(
                        BannerEstado.Tipo.ERROR,
                        "No te alcanza el saldo para retirar este vehículo.",
                        msg + " Acreditá saldo en tu cuenta para destrabarlo en el acto.",
                        "Agregar saldo ahora",
                        () -> {
                            txtDniSaldo.setText(dni);
                            abrirModoUsuario("SALDO");
                        }
                );
            } else {
                bannerRetiro.mostrar(
                        BannerEstado.Tipo.ADVERTENCIA,
                        "Ya tenés un viaje en curso pendiente de devolución.",
                        "Solo podés usar un vehículo a la vez. Devolvé primero tu unidad actual en cualquier estación para habilitar otro retiro.",
                        "Ir a Devolver vehículo",
                        () -> {
                            txtDniDevolucion.setText(dni);
                            abrirModoUsuario("DEVOLVER");
                        }
                );
            }
        }
    }

    // =========================================================================
    // VISTA USUARIO 2: DEVOLVER VEHÍCULO + RESUMEN DETALLADO DEL VIAJE
    // =========================================================================
    private JPanel crearVistaDevolverVehiculo() {
        JPanel vista = new JPanel(new BorderLayout(0, 16));
        vista.setOpaque(false);

        JPanel cabecera = crearCabeceraVista(
                "OPERACIÓN DE VIAJE · PASO 2",
                "Devolver vehículo y comprobante de viaje",
                "Ingresá tu DNI y la estación de destino. Al devolverlo verás la duración, promoción aplicada, costo cobrado y tu saldo restante.",
                Tema.ACCION_PRIMARIA
        );

        JPanel cuerpo = new JPanel(new GridBagLayout());
        cuerpo.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Izquierda: Formulario de devolución
        gbc.gridx = 0;
        gbc.weightx = 0.40;
        gbc.insets = new Insets(0, 0, 0, 18);

        TarjetaPanel cardForm = new TarjetaPanel(new BorderLayout(0, 16));
        cardForm.setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel campos = new JPanel();
        campos.setLayout(new BoxLayout(campos, BoxLayout.Y_AXIS));
        campos.setOpaque(false);

        JLabel subForm = new JLabel("Anclar y finalizar recorrido");
        subForm.setFont(Tema.FUENTE_TITULO_2);
        subForm.setForeground(Tema.TEXTO_PRINCIPAL);

        txtDniDevolucion = new CampoTexto("Ej: 12345678", true);
        txtDniDevolucion.setText(dniActivo);
        txtDniDevolucion.addActionListener(e -> refrescarEstadoViajeActivoDevolucion());

        comboEstacionDevolucion = Tema.crearCombo(new String[]{});

        TarjetaPanel cajaViajeEnCurso = new TarjetaPanel(new BorderLayout())
                .configurarEstilo(Tema.FONDO_LIENZO, Tema.BORDE_SUTIL, null, false);
        cajaViajeEnCurso.setBorder(new EmptyBorder(14, 16, 14, 16));
        lblViajeActivoInfo = new JLabel("<html><b>Estado actual:</b> Verificando viaje en curso...</html>");
        lblViajeActivoInfo.setFont(Tema.FUENTE_CUERPO);
        lblViajeActivoInfo.setForeground(Tema.TEXTO_SECUNDARIO);
        cajaViajeEnCurso.add(lblViajeActivoInfo, BorderLayout.CENTER);

        agregarCampoAlBox(campos, "Documento del usuario (DNI)", txtDniDevolucion);
        agregarCampoAlBox(campos, "Estación de devolución (Destino)", comboEstacionDevolucion);
        cajaViajeEnCurso.setAlignmentX(Component.LEFT_ALIGNMENT);
        campos.add(Box.createVerticalStrut(8));
        campos.add(cajaViajeEnCurso);

        BotonModerna btnDevolver = BotonModerna.primarioVerde("Finalizar viaje y devolver (Enter)");
        btnDevolver.addActionListener(e -> ejecutarDevolucionVehiculo());
        userDefaultButtons.put("DEVOLVER", btnDevolver);

        cardForm.add(subForm, BorderLayout.NORTH);
        cardForm.add(campos, BorderLayout.CENTER);
        cardForm.add(btnDevolver, BorderLayout.SOUTH);

        cuerpo.add(cardForm, gbc);

        // Derecha: Comprobante de Viaje Finalizado
        gbc.gridx = 1;
        gbc.weightx = 0.60;
        gbc.insets = new Insets(0, 0, 0, 0);

        contenedorComprobanteViaje = new JPanel(new BorderLayout());
        contenedorComprobanteViaje.setOpaque(false);
        mostrarEstadoInicialComprobante();

        cuerpo.add(contenedorComprobanteViaje, gbc);

        bannerDevolucion = new BannerEstado();

        vista.add(cabecera, BorderLayout.NORTH);
        vista.add(cuerpo, BorderLayout.CENTER);
        vista.add(bannerDevolucion, BorderLayout.SOUTH);
        return vista;
    }

    private void refrescarEstadoViajeActivoDevolucion() {
        String dni = txtDniDevolucion.getText().trim();
        Usuario u = ctrlCuenta.buscarUsuario(dni);
        if (u == null) {
            lblViajeActivoInfo.setText("<html><b>DNI no registrado:</b> Ingresá un documento válido.</html>");
            return;
        }
        Viaje abierto = u.getViajeAbierto();
        if (abierto == null) {
            lblViajeActivoInfo.setText("<html><b>" + u.getNombre() + ":</b> No tiene ningún viaje abierto en este momento.</html>");
        } else {
            lblViajeActivoInfo.setText("<html><b>Viaje en curso detectado:</b><br/>Vehículo <b>"
                    + abierto.getVehiculo().getCodigo() + "</b> (" + nombreTipoVehiculo(abierto.getVehiculo())
                    + ") retirado en <b>" + abierto.getEstacionOrigen().getCodigo() + " — "
                    + abierto.getEstacionOrigen().getDireccion() + "</b>.</html>");
        }
    }

    private void mostrarEstadoInicialComprobante() {
        contenedorComprobanteViaje.removeAll();
        TarjetaPanel cardVacia = new TarjetaPanel(new GridLayout(3, 1, 0, 8))
                .configurarEstilo(Color.WHITE, Tema.BORDE_SUTIL, Tema.SIDEBAR_FONDO, true);
        cardVacia.setBorder(new EmptyBorder(48, 32, 48, 32));

        JLabel l1 = new JLabel("Comprobante de viaje listo para emitirse", SwingConstants.CENTER);
        l1.setFont(Tema.FUENTE_TITULO_2);
        l1.setForeground(Tema.TEXTO_PRINCIPAL);

        JLabel l2 = new JLabel("<html><center>Al confirmar la devolución del vehículo en la estación elegida, verás aquí el desglose completo:<br/><b>duración en minutos, tipo de vehículo, promoción aplicada, costo cobrado y nuevo saldo restante.</b></center></html>", SwingConstants.CENTER);
        l2.setFont(Tema.FUENTE_CUERPO);
        l2.setForeground(Tema.TEXTO_SECUNDARIO);

        cardVacia.add(l1);
        cardVacia.add(l2);
        contenedorComprobanteViaje.add(cardVacia, BorderLayout.CENTER);
        contenedorComprobanteViaje.revalidate();
        contenedorComprobanteViaje.repaint();
    }

    private void renderizarComprobanteViaje(Usuario usuario, Viaje viajeCerrado, String promocionTexto, double costoCobrado, double saldoRestante) {
        contenedorComprobanteViaje.removeAll();

        TarjetaPanel ticket = new TarjetaPanel(new BorderLayout(0, 14))
                .configurarEstilo(Color.WHITE, Tema.BORDE_SUTIL, Tema.ACCION_PRIMARIA, true);
        ticket.setBorder(new EmptyBorder(22, 26, 22, 26));

        // Encabezado del ticket
        JPanel topTicket = new JPanel(new BorderLayout());
        topTicket.setOpaque(false);
        topTicket.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDE_SUTIL),
                new EmptyBorder(0, 0, 12, 0)
        ));

        JPanel izqTop = new JPanel(new GridLayout(2, 1, 0, 4));
        izqTop.setOpaque(false);
        izqTop.add(Tema.crearPastilla("✓ DEVOLUCIÓN REGISTRADA", Tema.EXITO_FONDO, Tema.EXITO_TEXTO));
        JLabel lblTit = new JLabel("Resumen de Viaje · " + usuario.getNombre());
        lblTit.setFont(Tema.FUENTE_TITULO_2);
        lblTit.setForeground(Tema.TEXTO_PRINCIPAL);
        izqTop.add(lblTit);

        JLabel lblDni = new JLabel("DNI " + usuario.getDocumento());
        lblDni.setFont(Tema.FUENTE_MONO_MEDIA);
        lblDni.setForeground(Tema.TEXTO_SECUNDARIO);

        topTicket.add(izqTop, BorderLayout.WEST);
        topTicket.add(lblDni, BorderLayout.EAST);

        // Cuerpo de detalles del viaje
        JPanel detalle = new JPanel(new GridLayout(5, 1, 0, 8));
        detalle.setOpaque(false);

        String origenTxt = viajeCerrado.getEstacionOrigen().getCodigo() + " (" + viajeCerrado.getEstacionOrigen().getDireccion() + ")";
        String destinoTxt = viajeCerrado.getEstacionDestino() != null
                ? viajeCerrado.getEstacionDestino().getCodigo() + " (" + viajeCerrado.getEstacionDestino().getDireccion() + ")"
                : "-";

        detalle.add(crearFilaComprobante("Trayecto realizado:", origenTxt + "  →  " + destinoTxt, false));
        detalle.add(crearFilaComprobante("Vehículo y tipo:", viajeCerrado.getVehiculo().getCodigo() + " · " + nombreTipoVehiculo(viajeCerrado.getVehiculo()), false));
        detalle.add(crearFilaComprobante("Duración del viaje:", viajeCerrado.getDuracionMinutos() + " minutos", true));
        detalle.add(crearFilaComprobante("Promoción aplicada:", promocionTexto, false));
        detalle.add(crearFilaComprobante("Fecha y hora de cierre:", viajeCerrado.getFechaHoraFin().format(FORMATO_FECHA), true));

        // Pie de totales (Costo cobrado y Saldo restante)
        TarjetaPanel cajaTotales = new TarjetaPanel(new GridLayout(1, 2, 16, 0))
                .configurarEstilo(Tema.FONDO_LIENZO, Tema.BORDE_SUTIL, null, false);
        cajaTotales.setBorder(new EmptyBorder(14, 18, 14, 18));

        JPanel colCosto = new JPanel(new GridLayout(2, 1, 0, 2));
        colCosto.setOpaque(false);
        JLabel tCosto = new JLabel("COSTO COBRADO");
        tCosto.setFont(Tema.FUENTE_ETIQUETA);
        tCosto.setForeground(Tema.TEXTO_SECUNDARIO);
        JLabel vCosto = new JLabel(Tema.formatearPesos(costoCobrado));
        vCosto.setFont(Tema.FUENTE_MONO_GRANDE);
        vCosto.setForeground(Tema.TEXTO_PRINCIPAL);
        colCosto.add(tCosto);
        colCosto.add(vCosto);

        JPanel colSaldo = new JPanel(new GridLayout(2, 1, 0, 2));
        colSaldo.setOpaque(false);
        JLabel tSaldo = new JLabel("SALDO RESTANTE EN CUENTA", SwingConstants.RIGHT);
        tSaldo.setFont(Tema.FUENTE_ETIQUETA);
        tSaldo.setForeground(Tema.ACCION_PRIMARIA);
        JLabel vSaldo = new JLabel(Tema.formatearPesos(saldoRestante), SwingConstants.RIGHT);
        vSaldo.setFont(Tema.FUENTE_MONO_GRANDE);
        vSaldo.setForeground(Tema.ACCION_PRIMARIA);
        colSaldo.add(tSaldo);
        colSaldo.add(vSaldo);

        cajaTotales.add(colCosto);
        cajaTotales.add(colSaldo);

        ticket.add(topTicket, BorderLayout.NORTH);
        ticket.add(detalle, BorderLayout.CENTER);
        ticket.add(cajaTotales, BorderLayout.SOUTH);

        contenedorComprobanteViaje.add(ticket, BorderLayout.CENTER);
        contenedorComprobanteViaje.revalidate();
        contenedorComprobanteViaje.repaint();
    }

    private JPanel crearFilaComprobante(String clave, String valor, boolean mono) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDE_SUTIL));
        JLabel l1 = new JLabel(clave);
        l1.setFont(Tema.FUENTE_CUERPO);
        l1.setForeground(Tema.TEXTO_SECUNDARIO);
        JLabel l2 = new JLabel(valor);
        l2.setFont(mono ? Tema.FUENTE_MONO_MEDIA : Tema.FUENTE_CUERPO_BOLD);
        l2.setForeground(Tema.TEXTO_PRINCIPAL);
        fila.add(l1, BorderLayout.WEST);
        fila.add(l2, BorderLayout.EAST);
        return fila;
    }

    private void ejecutarDevolucionVehiculo() {
        String dni = txtDniDevolucion.getText().trim();
        String itemEst = (String) comboEstacionDevolucion.getSelectedItem();
        String codEstacion = (itemEst != null && itemEst.contains(" — ")) ? itemEst.split(" — ")[0].trim() : "";

        txtDniDevolucion.marcarError(false);

        Usuario u = ctrlCuenta.buscarUsuario(dni);
        Viaje viajeAntesDeCerrar = (u != null) ? u.getViajeAbierto() : null;
        boolean teniaPlanAntes = (u != null) && u.getTienePlanMensual();
        int viajesPreviosAntes = (u != null && viajeAntesDeCerrar != null) ? u.cantidadViajesPrevios(viajeAntesDeCerrar) : 0;

        try {
            double costoCobrado = ctrlViajes.devolverVehiculo(dni, codEstacion);
            dniActivo = dni;
            actualizarSidebarUsuario(dni);
            poblarCombosEstaciones();
            refrescarEstadoViajeActivoDevolucion();

            if (u != null && viajeAntesDeCerrar != null) {
                String promoAplicada;
                if (viajeAntesDeCerrar.fueCancelado()) {
                    promoAplicada = "Cortesía por viaje menor a 2 min (100% bonificado)";
                } else if (teniaPlanAntes) {
                    promoAplicada = "Plan Mensual Activo (hasta 30 min gratis)";
                } else if (viajesPreviosAntes == 0) {
                    promoAplicada = "Promoción Primer Viaje (15 min gratis)";
                } else {
                    promoAplicada = "Sin promoción (Tarifa estándar)";
                }

                renderizarComprobanteViaje(u, viajeAntesDeCerrar, promoAplicada, costoCobrado, u.getCuenta().getSaldo());
            }

            bannerDevolucion.mostrar(
                    BannerEstado.Tipo.EXITO,
                    "¡Vehículo devuelto correctamente en " + codEstacion + "!",
                    "Se cerró tu viaje. Costo cobrado: " + Tema.formatearPesos(costoCobrado) +
                            " · Nuevo saldo disponible: " + (u != null ? Tema.formatearPesos(u.getCuenta().getSaldo()) : ""),
                    null,
                    null
            );
        } catch (UsuarioNoEncontradoException ex) {
            txtDniDevolucion.marcarError(true);
            bannerDevolucion.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "No encontramos ningún usuario con el DNI '" + dni + "'.",
                    "Verificá que el número de documento sea correcto.",
                    null,
                    null
            );
        } catch (EstacionNoEncontradaException ex) {
            bannerDevolucion.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "La estación de devolución no existe.",
                    "Seleccioná una estación válida en el desplegable.",
                    null,
                    null
            );
        } catch (NoTieneViajeAbiertoException ex) {
            bannerDevolucion.mostrar(
                    BannerEstado.Tipo.ADVERTENCIA,
                    "No tenés ningún viaje abierto para devolver.",
                    "Para registrar una devolución primero tenés que haber retirado un vehículo en alguna estación.",
                    "Ir a Retirar vehículo",
                    () -> {
                        txtDniRetiro.setText(dni);
                        abrirModoUsuario("RETIRAR");
                    }
            );
        }
    }

    // =========================================================================
    // VISTA USUARIO 3: AGREGAR SALDO
    // =========================================================================
    private JPanel crearVistaAgregarSaldo() {
        JPanel vista = new JPanel(new BorderLayout(0, 16));
        vista.setOpaque(false);

        JPanel cabecera = crearCabeceraVista(
                "BILLETERA VIRTUAL Y MEDIOS DE PAGO",
                "Agregar saldo a tu cuenta",
                "Acreditá saldo con tu DNI. En tu primera recarga se asocia automáticamente el medio de pago elegido.",
                Tema.ACCION_PRIMARIA
        );

        JPanel cuerpo = new JPanel(new GridLayout(1, 2, 22, 0));
        cuerpo.setOpaque(false);

        // Formulario izquierdo
        TarjetaPanel cardForm = new TarjetaPanel(new BorderLayout(0, 16));
        cardForm.setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel campos = new JPanel();
        campos.setLayout(new BoxLayout(campos, BoxLayout.Y_AXIS));
        campos.setOpaque(false);

        txtDniSaldo = new CampoTexto("Ej: 12345678", true);
        txtDniSaldo.setText(dniActivo);
        txtDniSaldo.addActionListener(e -> refrescarTarjetaSaldoUsuario());

        txtImporteSaldo = new CampoTexto("Ej: 5000", true);
        txtImporteSaldo.setText("5000");

        // Botones de carga rápida
        JPanel chipsMonto = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chipsMonto.setOpaque(false);
        chipsMonto.setAlignmentX(Component.LEFT_ALIGNMENT);
        BotonModerna b2k = BotonModerna.secundario("+$2.000");
        b2k.addActionListener(e -> txtImporteSaldo.setText("2000"));
        BotonModerna b5k = BotonModerna.secundario("+$5.000");
        b5k.addActionListener(e -> txtImporteSaldo.setText("5000"));
        BotonModerna b15k = BotonModerna.secundario("+$15.000 (Plan Mensual)");
        b15k.addActionListener(e -> txtImporteSaldo.setText("15000"));
        chipsMonto.add(b2k);
        chipsMonto.add(b5k);
        chipsMonto.add(b15k);

        comboMedioPago = Tema.crearCombo(new String[]{
                "Tarjeta de Débito",
                "Tarjeta de Crédito",
                "Mercado Pago / QR",
                "Transferencia Bancaria"
        });

        agregarCampoAlBox(campos, "Documento del titular (DNI)", txtDniSaldo);
        agregarCampoAlBox(campos, "Importe a acreditar ($ ARS)", txtImporteSaldo);
        campos.add(chipsMonto);
        campos.add(Box.createVerticalStrut(12));
        agregarCampoAlBox(campos, "Medio de pago (se asocia en tu primera carga)", comboMedioPago);

        BotonModerna btnAcreditar = BotonModerna.primarioVerde("Acreditar saldo ahora (Enter)");
        btnAcreditar.addActionListener(e -> ejecutarAgregarSaldo());
        userDefaultButtons.put("SALDO", btnAcreditar);

        cardForm.add(campos, BorderLayout.CENTER);
        cardForm.add(btnAcreditar, BorderLayout.SOUTH);

        // Tarjeta derecha de estado de saldo
        TarjetaPanel cardEstado = new TarjetaPanel(new BorderLayout(0, 16))
                .configurarEstilo(Tema.SIDEBAR_FONDO, Tema.SIDEBAR_BORDE, Tema.ACCION_PRIMARIA, true);
        cardEstado.setBorder(new EmptyBorder(26, 28, 26, 28));

        JPanel infoSaldo = new JPanel(new GridLayout(4, 1, 0, 8));
        infoSaldo.setOpaque(false);

        JLabel tagCuenta = new JLabel("ESTADO DE BILLETERA · SCHIPANI EXPRESS");
        tagCuenta.setFont(Tema.FUENTE_ETIQUETA);
        tagCuenta.setForeground(Tema.EXITO_ICONO);

        lblSaldoAnterior = new JLabel("Saldo anterior: $20.000,00");
        lblSaldoAnterior.setFont(Tema.FUENTE_SUBTITULO);
        lblSaldoAnterior.setForeground(Tema.TEXTO_SIDEBAR_MUTE);

        lblSaldoNuevo = new JLabel("$20.000,00");
        lblSaldoNuevo.setFont(new Font("Consolas", Font.BOLD, 34));
        lblSaldoNuevo.setForeground(Color.WHITE);

        lblMedioAsociado = new JLabel("Medio de pago asociado: Se registrará en la primera carga");
        lblMedioAsociado.setFont(Tema.FUENTE_CUERPO);
        lblMedioAsociado.setForeground(Tema.TEXTO_SIDEBAR_MUTE);

        infoSaldo.add(tagCuenta);
        infoSaldo.add(lblSaldoAnterior);
        infoSaldo.add(lblSaldoNuevo);
        infoSaldo.add(lblMedioAsociado);

        cardEstado.add(infoSaldo, BorderLayout.NORTH);

        cuerpo.add(cardForm);
        cuerpo.add(cardEstado);

        bannerSaldo = new BannerEstado();

        vista.add(cabecera, BorderLayout.NORTH);
        vista.add(cuerpo, BorderLayout.CENTER);
        vista.add(bannerSaldo, BorderLayout.SOUTH);
        return vista;
    }

    private void refrescarTarjetaSaldoUsuario() {
        Usuario u = ctrlCuenta.buscarUsuario(txtDniSaldo.getText().trim());
        if (u != null) {
            lblSaldoAnterior.setText("Titular: " + u.getNombre() + " (DNI " + u.getDocumento() + ")");
            lblSaldoNuevo.setText(Tema.formatearPesos(u.getCuenta().getSaldo()));
            String medio = u.getCuenta().getMedioPagoAsociado();
            lblMedioAsociado.setText("Medio de pago asociado: " + (medio != null ? medio : "Se asociará en la 1.ª recarga"));
        }
    }

    private void ejecutarAgregarSaldo() {
        String dni = txtDniSaldo.getText().trim();
        String montoStr = txtImporteSaldo.getText().trim().replace(",", ".");
        String medio = (String) comboMedioPago.getSelectedItem();

        txtDniSaldo.marcarError(false);
        txtImporteSaldo.marcarError(false);

        double importe;
        try {
            importe = Double.parseDouble(montoStr);
        } catch (NumberFormatException ex) {
            txtImporteSaldo.marcarError(true);
            bannerSaldo.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "El importe ingresado no es un número válido.",
                    "Escribí únicamente números positivos (por ejemplo: 2000 o 5000).",
                    null,
                    null
            );
            return;
        }

        Usuario uAntes = ctrlCuenta.buscarUsuario(dni);
        double saldoPrevio = (uAntes != null) ? uAntes.getCuenta().getSaldo() : 0.0;

        try {
            double nuevoSaldo = ctrlCuenta.agregarSaldo(dni, importe, medio);
            dniActivo = dni;
            actualizarSidebarUsuario(dni);

            Usuario uDespues = ctrlCuenta.buscarUsuario(dni);
            lblSaldoAnterior.setText("Saldo previo: " + Tema.formatearPesos(saldoPrevio));
            lblSaldoNuevo.setText(Tema.formatearPesos(nuevoSaldo));
            if (uDespues != null && uDespues.getCuenta().getMedioPagoAsociado() != null) {
                lblMedioAsociado.setText("Medio de pago asociado: " + uDespues.getCuenta().getMedioPagoAsociado());
            }

            bannerSaldo.mostrar(
                    BannerEstado.Tipo.EXITO,
                    "¡Saldo acreditado en el acto!",
                    "Sumaste " + Tema.formatearPesos(importe) + " a tu cuenta. Tu nuevo saldo disponible es " + Tema.formatearPesos(nuevoSaldo) + ".",
                    "Ir a Retirar vehículo",
                    () -> abrirModoUsuario("RETIRAR")
            );
        } catch (ImporteInvalidoException ex) {
            txtImporteSaldo.marcarError(true);
            bannerSaldo.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "El importe a recargar debe ser mayor a $0.",
                    "Ingresá un monto positivo o elegí uno de los botones rápidos (+$2.000, +$5.000 o +$15.000).",
                    null,
                    null
            );
        } catch (UsuarioNoEncontradoException ex) {
            txtDniSaldo.marcarError(true);
            bannerSaldo.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "No encontramos ningún usuario con el DNI '" + dni + "'.",
                    "Verificá el documento ingresado o creá el usuario antes de acreditarle saldo.",
                    "Registrar usuario",
                    () -> {
                        txtDniRegistro.setText(dni);
                        abrirModoUsuario("REGISTRAR");
                    }
            );
        }
    }

    // =========================================================================
    // VISTA USUARIO 4: PLAN MENSUAL ($15.000)
    // =========================================================================
    private JPanel crearVistaPlanMensual() {
        JPanel vista = new JPanel(new BorderLayout(0, 16));
        vista.setOpaque(false);

        JPanel cabecera = crearCabeceraVista(
                "ABONO URBANO INTELIGENTE",
                "Plan Mensual Schipani Express ($15.000)",
                "Suscribite por $15.000 y obtené hasta 30 minutos gratis por viaje con un tope de 20 viajes.",
                Tema.ACCION_PRIMARIA
        );

        JPanel cuerpo = new JPanel(new GridBagLayout());
        cuerpo.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Izquierda: Beneficios y formulario de suscripción (58%)
        gbc.gridx = 0;
        gbc.weightx = 0.58;
        gbc.insets = new Insets(0, 0, 0, 18);

        TarjetaPanel cardBeneficios = new TarjetaPanel(new BorderLayout(0, 16))
                .configurarEstilo(Color.WHITE, Tema.BORDE_SUTIL, Tema.ACCION_PRIMARIA, true);
        cardBeneficios.setBorder(new EmptyBorder(24, 26, 24, 26));

        JPanel topBen = new JPanel(new BorderLayout());
        topBen.setOpaque(false);
        topBen.add(Tema.crearPastilla("PACK 20 VIAJES BONIFICADOS", Tema.ACCION_TINTE, Tema.ACCION_PRIMARIA), BorderLayout.WEST);
        JLabel lblPrecioPlan = new JLabel("$15.000,00");
        lblPrecioPlan.setFont(Tema.FUENTE_MONO_GRANDE);
        lblPrecioPlan.setForeground(Tema.TEXTO_PRINCIPAL);
        topBen.add(lblPrecioPlan, BorderLayout.EAST);

        JPanel listaBeneficios = new JPanel(new GridLayout(3, 1, 0, 10));
        listaBeneficios.setOpaque(false);
        listaBeneficios.add(crearItemChecklist("1. Hasta 30 minutos gratis en cada viaje (bicicletas comunes, eléctricas y monopatines)."));
        listaBeneficios.add(crearItemChecklist("2. Tope de 20 viajes: se desactiva automáticamente al completar los 20 recorridos."));
        listaBeneficios.add(crearItemChecklist("3. Podés volver a suscribirte inmediatamente una vez consumidos los 20 viajes."));

        JPanel formSuscribir = new JPanel(new BorderLayout(12, 8));
        formSuscribir.setOpaque(false);
        formSuscribir.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDE_SUTIL),
                new EmptyBorder(14, 0, 0, 0)
        ));

        txtDniPlan = new CampoTexto("DNI del usuario (Ej: 12345678)", true);
        txtDniPlan.setText(dniActivo);
        txtDniPlan.addActionListener(e -> refrescarEstadoPlanVista());

        BotonModerna btnSuscribir = BotonModerna.primarioVerde("Suscribirme por $15.000 (Enter)");
        btnSuscribir.addActionListener(e -> ejecutarSuscribirPlan());
        userDefaultButtons.put("PLAN", btnSuscribir);

        formSuscribir.add(Tema.crearEtiquetaCampo("Documento del usuario a suscribir"), BorderLayout.NORTH);
        formSuscribir.add(txtDniPlan, BorderLayout.CENTER);
        formSuscribir.add(btnSuscribir, BorderLayout.EAST);

        cardBeneficios.add(topBen, BorderLayout.NORTH);
        cardBeneficios.add(listaBeneficios, BorderLayout.CENTER);
        cardBeneficios.add(formSuscribir, BorderLayout.SOUTH);

        cuerpo.add(cardBeneficios, gbc);

        // Derecha: Estado actual del plan del usuario (42%)
        gbc.gridx = 1;
        gbc.weightx = 0.42;
        gbc.insets = new Insets(0, 0, 0, 0);

        TarjetaPanel cardEstadoPlan = new TarjetaPanel(new BorderLayout(0, 14));
        cardEstadoPlan.setBorder(new EmptyBorder(24, 26, 24, 26));

        JPanel boxEstado = new JPanel(new GridLayout(5, 1, 0, 8));
        boxEstado.setOpaque(false);

        JLabel lblTagEst = new JLabel("ESTADO DE TU SUSCRIPCIÓN");
        lblTagEst.setFont(Tema.FUENTE_ETIQUETA);
        lblTagEst.setForeground(Tema.TEXTO_TERCIARIO);

        lblEstadoPlanBadge = new JLabel("Consultando estado...");
        lblEstadoPlanBadge.setFont(Tema.FUENTE_TITULO_2);
        lblEstadoPlanBadge.setForeground(Tema.TEXTO_PRINCIPAL);

        lblProgresoPlanTexto = new JLabel("Viajes utilizados: 0 / 20");
        lblProgresoPlanTexto.setFont(Tema.FUENTE_CUERPO_BOLD);
        lblProgresoPlanTexto.setForeground(Tema.TEXTO_SECUNDARIO);

        barraProgresoPlan = new BarraBateria(100);

        lblSaldoPostPlan = new JLabel("Saldo disponible: $20.000,00");
        lblSaldoPostPlan.setFont(Tema.FUENTE_MONO_MEDIA);
        lblSaldoPostPlan.setForeground(Tema.ACCION_PRIMARIA);

        boxEstado.add(lblTagEst);
        boxEstado.add(lblEstadoPlanBadge);
        boxEstado.add(lblProgresoPlanTexto);
        boxEstado.add(barraProgresoPlan);
        boxEstado.add(lblSaldoPostPlan);

        cardEstadoPlan.add(boxEstado, BorderLayout.NORTH);
        cuerpo.add(cardEstadoPlan, gbc);

        bannerPlan = new BannerEstado();

        vista.add(cabecera, BorderLayout.NORTH);
        vista.add(cuerpo, BorderLayout.CENTER);
        vista.add(bannerPlan, BorderLayout.SOUTH);
        return vista;
    }

    private void refrescarEstadoPlanVista() {
        Usuario u = ctrlCuenta.buscarUsuario(txtDniPlan.getText().trim());
        if (u == null) {
            lblEstadoPlanBadge.setText("Usuario no encontrado");
            lblProgresoPlanTexto.setText("Ingresá un DNI registrado para ver su plan.");
            barraProgresoPlan.setPorcentaje(0);
            lblSaldoPostPlan.setText("Saldo disponible: -");
            return;
        }
        int usados = u.getViajesConPlanUsados();
        int restantes = Math.max(0, PlanMensual.TOPE_VIAJES - usados);
        if (u.getTienePlanMensual()) {
            lblEstadoPlanBadge.setText("● Plan Mensual ACTIVO (" + u.getNombre() + ")");
            lblEstadoPlanBadge.setForeground(Tema.ACCION_PRIMARIA);
            lblProgresoPlanTexto.setText("Viajes usados: " + usados + " de " + PlanMensual.TOPE_VIAJES + " (" + restantes + " disponibles)");
            barraProgresoPlan.setPorcentaje((int) ((restantes * 100.0) / PlanMensual.TOPE_VIAJES));
        } else {
            lblEstadoPlanBadge.setText("○ Sin Plan Mensual activo (" + u.getNombre() + ")");
            lblEstadoPlanBadge.setForeground(Tema.TEXTO_SECUNDARIO);
            lblProgresoPlanTexto.setText("Suscribite por $15.000 para activar 20 viajes de hasta 30 min gratis.");
            barraProgresoPlan.setPorcentaje(0);
        }
        lblSaldoPostPlan.setText("Saldo en cuenta: " + Tema.formatearPesos(u.getCuenta().getSaldo()));
    }

    private void ejecutarSuscribirPlan() {
        String dni = txtDniPlan.getText().trim();
        txtDniPlan.marcarError(false);

        try {
            ctrlCuenta.suscribirPlanMensual(dni);
            dniActivo = dni;
            actualizarSidebarUsuario(dni);
            refrescarEstadoPlanVista();

            Usuario u = ctrlCuenta.buscarUsuario(dni);
            bannerPlan.mostrar(
                    BannerEstado.Tipo.EXITO,
                    "¡Ya estás suscripto al Plan Mensual!",
                    "Se debitaron $15.000,00 de tu cuenta. Tenés 20 viajes de hasta 30 minutos gratis cada uno. Saldo restante: "
                            + (u != null ? Tema.formatearPesos(u.getCuenta().getSaldo()) : ""),
                    "Ir a Retirar vehículo",
                    () -> abrirModoUsuario("RETIRAR")
            );
        } catch (PlanMensualActivoException ex) {
            refrescarEstadoPlanVista();
            Usuario u = ctrlCuenta.buscarUsuario(dni);
            int restantes = (u != null) ? (PlanMensual.TOPE_VIAJES - u.getViajesConPlanUsados()) : 20;
            bannerPlan.mostrar(
                    BannerEstado.Tipo.INFO,
                    "Ya tenés un Plan Mensual activo en tu cuenta.",
                    "Todavía contás con " + restantes + " de 20 viajes bonificados. Cuando uses los 20 viajes se desactivará solo y podrás volver a suscribirte.",
                    null,
                    null
            );
        } catch (SaldoInsuficienteException ex) {
            Usuario u = ctrlCuenta.buscarUsuario(dni);
            double actual = (u != null) ? u.getCuenta().getSaldo() : 0;
            double faltante = Math.max(0, 15000 - actual);
            bannerPlan.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "No te alcanza el saldo para activar el Plan Mensual ($15.000,00).",
                    "Tu saldo actual es " + Tema.formatearPesos(actual) + ". Necesitás cargar al menos " + Tema.formatearPesos(faltante) + " más.",
                    "Agregar saldo ahora",
                    () -> {
                        txtDniSaldo.setText(dni);
                        txtImporteSaldo.setText(String.valueOf((int) Math.ceil(faltante)));
                        abrirModoUsuario("SALDO");
                    }
            );
        } catch (UsuarioNoEncontradoException ex) {
            txtDniPlan.marcarError(true);
            bannerPlan.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "No encontramos ningún usuario con el DNI '" + dni + "'.",
                    "Revisá los números o registrá al usuario antes de suscribirlo al plan.",
                    "Registrar usuario",
                    () -> {
                        txtDniRegistro.setText(dni);
                        abrirModoUsuario("REGISTRAR");
                    }
            );
        } catch (ImporteInvalidoException ex) {
            bannerPlan.mostrar(BannerEstado.Tipo.ERROR, "No se pudo procesar el débito del plan.", ex.getMessage(), null, null);
        }
    }

    // =========================================================================
    // VISTA USUARIO 5: REGISTRAR USUARIO
    // =========================================================================
    private JPanel crearVistaRegistrarUsuario() {
        JPanel vista = new JPanel(new BorderLayout(0, 16));
        vista.setOpaque(false);

        JPanel cabecera = crearCabeceraVista(
                "ALTA DE NUEVO VIAJERO",
                "Registrar usuario en Schipani Express",
                "Completá tu nombre, DNI y correo electrónico. Tu primer viaje incluye 15 minutos sin cargo.",
                Tema.ACCION_PRIMARIA
        );

        JPanel cuerpo = new JPanel(new GridLayout(1, 2, 22, 0));
        cuerpo.setOpaque(false);

        TarjetaPanel cardForm = new TarjetaPanel(new BorderLayout(0, 16));
        cardForm.setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel campos = new JPanel();
        campos.setLayout(new BoxLayout(campos, BoxLayout.Y_AXIS));
        campos.setOpaque(false);

        txtNombreRegistro = new CampoTexto("Ej: Juan Pérez", false);
        txtDniRegistro = new CampoTexto("Ej: 12345678", true);
        txtEmailRegistro = new CampoTexto("Ej: juan@mail.com", false);

        agregarCampoAlBox(campos, "Nombre y apellido", txtNombreRegistro);
        agregarCampoAlBox(campos, "Documento (DNI)", txtDniRegistro);
        agregarCampoAlBox(campos, "Correo electrónico", txtEmailRegistro);

        BotonModerna btnRegistrar = BotonModerna.primarioVerde("Crear cuenta de usuario (Enter)");
        btnRegistrar.addActionListener(e -> ejecutarRegistrarUsuario());
        userDefaultButtons.put("REGISTRAR", btnRegistrar);

        cardForm.add(campos, BorderLayout.CENTER);
        cardForm.add(btnRegistrar, BorderLayout.SOUTH);

        TarjetaPanel cardPromo = new TarjetaPanel(new BorderLayout(0, 12))
                .configurarEstilo(Color.WHITE, Tema.BORDE_SUTIL, Tema.ACCION_PRIMARIA, true);
        cardPromo.setBorder(new EmptyBorder(24, 26, 24, 26));

        JPanel textoPromo = new JPanel(new GridLayout(4, 1, 0, 8));
        textoPromo.setOpaque(false);
        textoPromo.add(Tema.crearPastilla("PROMOCIÓN DE BIENVENIDA", Tema.ACCION_TINTE, Tema.ACCION_PRIMARIA));
        JLabel t1 = new JLabel("Tu primer viaje tiene 15 minutos gratis");
        t1.setFont(Tema.FUENTE_TITULO_2);
        t1.setForeground(Tema.TEXTO_PRINCIPAL);
        JLabel t2 = new JLabel("<html>Al darte de alta, el sistema aplica automáticamente la promoción <b>PrimerViaje</b> en tu primer recorrido finalizado.</html>");
        t2.setFont(Tema.FUENTE_CUERPO);
        t2.setForeground(Tema.TEXTO_SECUNDARIO);
        JLabel t3 = new JLabel("Usuario de ejemplo precargado: Juan Pérez · DNI 12345678 · Saldo $20.000");
        t3.setFont(Tema.FUENTE_MONO_CHICA);
        t3.setForeground(Tema.ACCION_PRIMARIA);

        textoPromo.add(t1);
        textoPromo.add(t2);
        textoPromo.add(t3);

        cardPromo.add(textoPromo, BorderLayout.NORTH);

        cuerpo.add(cardForm);
        cuerpo.add(cardPromo);

        bannerRegistro = new BannerEstado();

        vista.add(cabecera, BorderLayout.NORTH);
        vista.add(cuerpo, BorderLayout.CENTER);
        vista.add(bannerRegistro, BorderLayout.SOUTH);
        return vista;
    }

    private void ejecutarRegistrarUsuario() {
        String nombre = txtNombreRegistro.getText().trim();
        String dni = txtDniRegistro.getText().trim();
        String email = txtEmailRegistro.getText().trim();

        txtNombreRegistro.marcarError(false);
        txtDniRegistro.marcarError(false);
        txtEmailRegistro.marcarError(false);

        try {
            Usuario nuevo = ctrlCuenta.registrarUsuario(nombre, dni, email);
            dniActivo = nuevo.getDocumento();
            actualizarSidebarUsuario(dniActivo);
            sincronizarCamposDniUsuario(dniActivo);

            txtNombreRegistro.setText("");
            txtDniRegistro.setText("");
            txtEmailRegistro.setText("");

            bannerRegistro.mostrar(
                    BannerEstado.Tipo.EXITO,
                    "¡Bienvenido/a a Schipani Express, " + nuevo.getNombre() + "!",
                    "Tu cuenta con DNI " + nuevo.getDocumento() + " (" + nuevo.getEmail() + ") ya está activa. Cargá saldo para realizar tu primer viaje con 15 minutos gratis.",
                    "Cargar saldo inicial",
                    () -> abrirModoUsuario("SALDO")
            );
        } catch (IllegalArgumentException | IllegalStateException ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "Datos inválidos";
            if (msg.toLowerCase().contains("nombre")) txtNombreRegistro.marcarError(true);
            if (msg.toLowerCase().contains("documento")) txtDniRegistro.marcarError(true);
            if (msg.toLowerCase().contains("email")) txtEmailRegistro.marcarError(true);

            bannerRegistro.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "No pudimos completar el registro: " + msg,
                    "Revisá los campos marcados en rojo e intentá nuevamente.",
                    null,
                    null
            );
        }
    }

    // =========================================================================
    // SHELL DE ADMINISTRADOR (BARRA LATERAL + 4 PANTALLAS)
    // =========================================================================
    private JPanel crearShellAdmin() {
        JPanel shell = new JPanel(new BorderLayout());
        shell.setBackground(Tema.FONDO_LIENZO);

        JPanel sidebar = new JPanel(new BorderLayout(0, 16));
        sidebar.setBackground(Tema.SIDEBAR_FONDO);
        sidebar.setPreferredSize(new Dimension(Tema.SIDEBAR_ANCHO, Tema.VENTANA_ALTO));
        sidebar.setBorder(new EmptyBorder(22, 18, 22, 18));

        JPanel topSidebar = new JPanel();
        topSidebar.setLayout(new BoxLayout(topSidebar, BoxLayout.Y_AXIS));
        topSidebar.setOpaque(false);

        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandRow.setOpaque(false);
        brandRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel badge = new JLabel("SE", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Tema.activarAntialiasing(g2);
                g2.setColor(Tema.ADMIN_ACENTO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setPreferredSize(new Dimension(36, 36));
        badge.setForeground(Color.WHITE);
        badge.setFont(Tema.FUENTE_TITULO_2);

        JPanel brandTexts = new JPanel(new GridLayout(2, 1));
        brandTexts.setOpaque(false);
        JLabel lbTitle = new JLabel("SCHIPANI EXPRESS");
        lbTitle.setFont(Tema.FUENTE_CUERPO_BOLD);
        lbTitle.setForeground(Color.WHITE);
        JLabel lbSub = new JLabel("Consola Administrador");
        lbSub.setFont(Tema.FUENTE_ETIQUETA);
        lbSub.setForeground(Tema.ADMIN_ACENTO);
        brandTexts.add(lbTitle);
        brandTexts.add(lbSub);
        brandRow.add(badge);
        brandRow.add(brandTexts);

        JPanel navList = new JPanel();
        navList.setLayout(new BoxLayout(navList, BoxLayout.Y_AXIS));
        navList.setOpaque(false);
        navList.setAlignmentX(Component.LEFT_ALIGNMENT);

        navList.add(crearTituloSeccionSidebar("GESTIÓN Y CONSULTA"));
        navList.add(Box.createVerticalStrut(6));
        navList.add(crearItemNavAdmin("BUSCAR", "01", "Buscar usuario"));
        navList.add(Box.createVerticalStrut(4));
        navList.add(crearItemNavAdmin("ESTACION", "02", "Registrar estación"));
        navList.add(Box.createVerticalStrut(14));
        navList.add(crearTituloSeccionSidebar("REPORTES DE AUDITORÍA"));
        navList.add(Box.createVerticalStrut(6));
        navList.add(crearItemNavAdmin("REP_USUARIO", "03", "Viajes de un usuario"));
        navList.add(Box.createVerticalStrut(4));
        navList.add(crearItemNavAdmin("REP_ESTACION", "04", "Uso de una estación"));

        topSidebar.add(brandRow);
        topSidebar.add(Box.createVerticalStrut(22));
        topSidebar.add(navList);

        BotonModerna btnVolverHome = new BotonModerna(
                "← Cambiar de rol (Esc)",
                Tema.SIDEBAR_TARJETA,
                Tema.SIDEBAR_BORDE,
                Tema.SIDEBAR_FONDO,
                Color.WHITE,
                Tema.SIDEBAR_BORDE
        );
        btnVolverHome.addActionListener(e -> mostrarHome());

        sidebar.add(topSidebar, BorderLayout.NORTH);
        sidebar.add(btnVolverHome, BorderLayout.SOUTH);

        adminContentPanel.setOpaque(false);
        adminContentPanel.setBorder(new EmptyBorder(26, 32, 26, 32));
        adminContentPanel.add(crearVistaAdminBuscarUsuario(), "BUSCAR");
        adminContentPanel.add(crearVistaAdminRegistrarEstacion(), "ESTACION");
        adminContentPanel.add(crearVistaAdminReporteUsuario(), "REP_USUARIO");
        adminContentPanel.add(crearVistaAdminReporteEstacion(), "REP_ESTACION");

        shell.add(sidebar, BorderLayout.WEST);
        shell.add(adminContentPanel, BorderLayout.CENTER);
        return shell;
    }

    // =========================================================================
    // VISTA ADMIN 1: BUSCAR USUARIO POR DOCUMENTO
    // =========================================================================
    private JPanel crearVistaAdminBuscarUsuario() {
        JPanel vista = new JPanel(new BorderLayout(0, 16));
        vista.setOpaque(false);

        JPanel cabecera = crearCabeceraVista(
                "CONSOLA DE ADMINISTRACIÓN · AUDITORÍA DE PADRONES",
                "Buscar usuario por documento",
                "Consultá la ficha completa de un viajero: datos personales, saldo actual, estado del Plan Mensual e historial de viajes.",
                Tema.ADMIN_ACENTO
        );

        JPanel centro = new JPanel(new BorderLayout(0, 16));
        centro.setOpaque(false);

        // Barra de búsqueda
        TarjetaPanel barraBusqueda = new TarjetaPanel(new BorderLayout(14, 0));
        barraBusqueda.setBorder(new EmptyBorder(14, 18, 14, 18));

        txtDniBuscarAdmin = new CampoTexto("Ingresá el DNI del usuario (Ej: 12345678)", true);
        txtDniBuscarAdmin.setText("12345678");

        BotonModerna btnBuscar = BotonModerna.primarioPizarra("Buscar ficha de usuario (Enter)");
        btnBuscar.addActionListener(e -> ejecutarBuscarUsuarioAdmin());
        adminDefaultButtons.put("BUSCAR", btnBuscar);

        barraBusqueda.add(Tema.crearEtiquetaCampo("DNI a inspeccionar:"), BorderLayout.WEST);
        barraBusqueda.add(txtDniBuscarAdmin, BorderLayout.CENTER);
        barraBusqueda.add(btnBuscar, BorderLayout.EAST);

        panelResultadoUsuarioAdmin = new JPanel(new BorderLayout());
        panelResultadoUsuarioAdmin.setOpaque(false);

        centro.add(barraBusqueda, BorderLayout.NORTH);
        centro.add(panelResultadoUsuarioAdmin, BorderLayout.CENTER);

        bannerBuscarAdmin = new BannerEstado();

        vista.add(cabecera, BorderLayout.NORTH);
        vista.add(centro, BorderLayout.CENTER);
        vista.add(bannerBuscarAdmin, BorderLayout.SOUTH);
        return vista;
    }

    private void ejecutarBuscarUsuarioAdmin() {
        String dni = txtDniBuscarAdmin.getText().trim();
        txtDniBuscarAdmin.marcarError(false);
        panelResultadoUsuarioAdmin.removeAll();

        Usuario u = ctrlCuenta.buscarUsuario(dni);
        if (u == null) {
            txtDniBuscarAdmin.marcarError(true);
            bannerBuscarAdmin.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "No encontramos ningún usuario registrado con el DNI '" + dni + "'.",
                    "Verificá el número de documento (por ejemplo: 12345678 para Juan Pérez).",
                    null,
                    null
            );
            panelResultadoUsuarioAdmin.revalidate();
            panelResultadoUsuarioAdmin.repaint();
            return;
        }

        bannerBuscarAdmin.ocultar();

        JPanel grilla = new JPanel(new GridBagLayout());
        grilla.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Ficha izquierda (36%)
        gbc.gridx = 0;
        gbc.weightx = 0.36;
        gbc.insets = new Insets(0, 0, 0, 16);

        TarjetaPanel cardFicha = new TarjetaPanel(new GridLayout(6, 1, 0, 6))
                .configurarEstilo(Color.WHITE, Tema.BORDE_SUTIL, Tema.SIDEBAR_FONDO, true);
        cardFicha.setBorder(new EmptyBorder(20, 22, 20, 22));

        cardFicha.add(Tema.crearPastilla("DNI " + u.getDocumento(), Tema.FONDO_LIENZO, Tema.TEXTO_PRINCIPAL));
        JLabel lblNom = new JLabel(u.getNombre());
        lblNom.setFont(Tema.FUENTE_TITULO_1);
        lblNom.setForeground(Tema.TEXTO_PRINCIPAL);
        cardFicha.add(lblNom);

        JLabel lblMail = new JLabel("Email: " + u.getEmail());
        lblMail.setFont(Tema.FUENTE_CUERPO);
        lblMail.setForeground(Tema.TEXTO_SECUNDARIO);
        cardFicha.add(lblMail);

        JLabel lblSal = new JLabel("Saldo actual: " + Tema.formatearPesos(u.getCuenta().getSaldo()));
        lblSal.setFont(Tema.FUENTE_MONO_MEDIA);
        lblSal.setForeground(Tema.ACCION_PRIMARIA);
        cardFicha.add(lblSal);

        String planTxt = u.getTienePlanMensual()
                ? "Plan Mensual: ACTIVO (" + u.getViajesConPlanUsados() + "/20 usados)"
                : "Plan Mensual: Inactivo";
        JLabel lblPlan = new JLabel(planTxt);
        lblPlan.setFont(Tema.FUENTE_CUERPO_BOLD);
        lblPlan.setForeground(Tema.TEXTO_PRINCIPAL);
        cardFicha.add(lblPlan);

        JLabel lblCantViajes = new JLabel("Total de viajes realizados: " + u.getViajes().size());
        lblCantViajes.setFont(Tema.FUENTE_CUERPO);
        lblCantViajes.setForeground(Tema.TEXTO_SECUNDARIO);
        cardFicha.add(lblCantViajes);

        grilla.add(cardFicha, gbc);

        // Tabla derecha de viajes del usuario (64%)
        gbc.gridx = 1;
        gbc.weightx = 0.64;
        gbc.insets = new Insets(0, 0, 0, 0);

        TarjetaPanel cardViajes = new TarjetaPanel(new BorderLayout(0, 10));
        cardViajes.setBorder(new EmptyBorder(18, 20, 18, 20));
        JLabel titTabla = new JLabel("Viajes asociados a " + u.getNombre());
        titTabla.setFont(Tema.FUENTE_TITULO_2);
        titTabla.setForeground(Tema.TEXTO_PRINCIPAL);
        cardViajes.add(titTabla, BorderLayout.NORTH);

        if (u.getViajes().isEmpty()) {
            JLabel sinViajes = new JLabel("Este usuario todavía no registra viajes en la red.", SwingConstants.CENTER);
            sinViajes.setFont(Tema.FUENTE_CUERPO);
            sinViajes.setForeground(Tema.TEXTO_SECUNDARIO);
            cardViajes.add(sinViajes, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Inicio", "Vehículo", "Origen", "Destino", "Duración", "Estado"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            try {
                List<Viaje> ordenados = ctrlReporte.reporteViajesPorUsuario(dni);
                for (Viaje v : ordenados) {
                    model.addRow(filaViajeParaTabla(v));
                }
            } catch (UsuarioNoEncontradoException ignored) {}

            JTable tabla = Tema.crearTablaModerna(model);
            JScrollPane sp = new JScrollPane(tabla);
            sp.setBorder(BorderFactory.createLineBorder(Tema.BORDE_SUTIL));
            cardViajes.add(sp, BorderLayout.CENTER);
        }

        grilla.add(cardViajes, gbc);

        panelResultadoUsuarioAdmin.add(grilla, BorderLayout.CENTER);
        panelResultadoUsuarioAdmin.revalidate();
        panelResultadoUsuarioAdmin.repaint();
    }

    // =========================================================================
    // VISTA ADMIN 2: REGISTRAR ESTACIÓN
    // =========================================================================
    private JPanel crearVistaAdminRegistrarEstacion() {
        JPanel vista = new JPanel(new BorderLayout(0, 16));
        vista.setOpaque(false);

        JPanel cabecera = crearCabeceraVista(
                "INFRAESTRUCTURA URBANA",
                "Registrar nueva estación en la red",
                "Dá de alta nuevas cabeceras indicando código (E04...), dirección, coordenadas y capacidad máxima de anclajes.",
                Tema.ADMIN_ACENTO
        );

        JPanel cuerpo = new JPanel(new GridBagLayout());
        cuerpo.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Formulario izquierdo (42%)
        gbc.gridx = 0;
        gbc.weightx = 0.42;
        gbc.insets = new Insets(0, 0, 0, 18);

        TarjetaPanel cardForm = new TarjetaPanel(new BorderLayout(0, 14));
        cardForm.setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel campos = new JPanel();
        campos.setLayout(new BoxLayout(campos, BoxLayout.Y_AXIS));
        campos.setOpaque(false);

        txtCodigoEstacionNueva = new CampoTexto("Ej: E04", true);
        txtDireccionEstacionNueva = new CampoTexto("Ej: Av. Santa Fe 3200", false);
        txtCoordEstacionNueva = new CampoTexto("Ej: -34.5889,-58.4102", true);
        txtCoordEstacionNueva.setText("-34.5889,-58.4102");
        txtCapacidadEstacionNueva = new CampoTexto("Ej: 12", true);

        agregarCampoAlBox(campos, "Código identificador", txtCodigoEstacionNueva);
        agregarCampoAlBox(campos, "Dirección en Buenos Aires", txtDireccionEstacionNueva);
        agregarCampoAlBox(campos, "Coordenadas geográficas", txtCoordEstacionNueva);
        agregarCampoAlBox(campos, "Capacidad máxima de vehículos", txtCapacidadEstacionNueva);

        BotonModerna btnGuardarEst = BotonModerna.primarioPizarra("Registrar estación (Enter)");
        btnGuardarEst.addActionListener(e -> ejecutarRegistrarEstacionAdmin());
        adminDefaultButtons.put("ESTACION", btnGuardarEst);

        cardForm.add(campos, BorderLayout.CENTER);
        cardForm.add(btnGuardarEst, BorderLayout.SOUTH);

        cuerpo.add(cardForm, gbc);

        // Lista derecha de estaciones activas (58%)
        gbc.gridx = 1;
        gbc.weightx = 0.58;
        gbc.insets = new Insets(0, 0, 0, 0);

        TarjetaPanel cardLista = new TarjetaPanel(new BorderLayout(0, 12));
        cardLista.setBorder(new EmptyBorder(22, 24, 22, 24));

        JLabel titLista = new JLabel("Estaciones operativas (ordenadas de mayor a menor capacidad)");
        titLista.setFont(Tema.FUENTE_TITULO_2);
        titLista.setForeground(Tema.TEXTO_PRINCIPAL);

        listaEstacionesAdmin = new JPanel();
        listaEstacionesAdmin.setLayout(new BoxLayout(listaEstacionesAdmin, BoxLayout.Y_AXIS));
        listaEstacionesAdmin.setOpaque(false);

        JScrollPane spEst = new JScrollPane(listaEstacionesAdmin);
        spEst.setBorder(null);
        spEst.setOpaque(false);
        spEst.getViewport().setOpaque(false);

        cardLista.add(titLista, BorderLayout.NORTH);
        cardLista.add(spEst, BorderLayout.CENTER);

        cuerpo.add(cardLista, gbc);

        bannerEstacionAdmin = new BannerEstado();

        vista.add(cabecera, BorderLayout.NORTH);
        vista.add(cuerpo, BorderLayout.CENTER);
        vista.add(bannerEstacionAdmin, BorderLayout.SOUTH);
        return vista;
    }

    private void refrescarListaEstacionesAdmin() {
        listaEstacionesAdmin.removeAll();
        List<Estacion> estaciones = ctrlEstacion.obtenerEstacionesOrdenadasPorCapacidad();
        for (Estacion e : estaciones) {
            TarjetaPanel item = new TarjetaPanel(new BorderLayout(12, 0))
                    .configurarEstilo(Tema.FONDO_LIENZO, Tema.BORDE_SUTIL, null, false);
            item.setBorder(new EmptyBorder(12, 16, 12, 16));
            item.setMaximumSize(new Dimension(1000, 68));

            JLabel tit = new JLabel(e.getCodigo() + " — " + e.getDireccion());
            tit.setFont(Tema.FUENTE_CUERPO_BOLD);
            tit.setForeground(Tema.TEXTO_PRINCIPAL);

            int ocupados = e.getVehiculosEstacionados().size();
            JLabel sub = new JLabel("Vehículos anclados: " + ocupados + " / " + e.getCapacidadMaxima()
                    + (e.requiereRedistribucion() ? "  ·  ⚠ Requiere redistribución" : "  ·  ✓ Nivel óptimo"));
            sub.setFont(Tema.FUENTE_CUERPO);
            sub.setForeground(e.requiereRedistribucion() ? Tema.ADMIN_HOVER : Tema.TEXTO_SECUNDARIO);

            JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
            textos.setOpaque(false);
            textos.add(tit);
            textos.add(sub);

            JLabel capPill = Tema.crearPastilla("Capacidad: " + e.getCapacidadMaxima(), Color.WHITE, Tema.TEXTO_PRINCIPAL);
            capPill.setFont(Tema.FUENTE_MONO_CHICA);

            item.add(textos, BorderLayout.CENTER);
            item.add(capPill, BorderLayout.EAST);

            listaEstacionesAdmin.add(item);
            listaEstacionesAdmin.add(Box.createVerticalStrut(8));
        }
        listaEstacionesAdmin.revalidate();
        listaEstacionesAdmin.repaint();
    }

    private void ejecutarRegistrarEstacionAdmin() {
        String codigo = txtCodigoEstacionNueva.getText().trim().toUpperCase();
        String direccion = txtDireccionEstacionNueva.getText().trim();
        String coord = txtCoordEstacionNueva.getText().trim().isEmpty() ? "-34.6037,-58.3816" : txtCoordEstacionNueva.getText().trim();
        String capStr = txtCapacidadEstacionNueva.getText().trim();

        txtCodigoEstacionNueva.marcarError(false);
        txtDireccionEstacionNueva.marcarError(false);
        txtCapacidadEstacionNueva.marcarError(false);

        int capacidad;
        try {
            capacidad = Integer.parseInt(capStr);
        } catch (NumberFormatException ex) {
            txtCapacidadEstacionNueva.marcarError(true);
            bannerEstacionAdmin.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "La capacidad máxima debe ser un número entero mayor a 0.",
                    "Por ejemplo: 8, 10 o 12 anclajes.",
                    null,
                    null
            );
            return;
        }

        try {
            Estacion nueva = ctrlEstacion.registrarEstacion(codigo, direccion, coord, capacidad);
            poblarCombosEstaciones();
            refrescarListaEstacionesAdmin();

            txtCodigoEstacionNueva.setText("");
            txtDireccionEstacionNueva.setText("");
            txtCapacidadEstacionNueva.setText("");

            bannerEstacionAdmin.mostrar(
                    BannerEstado.Tipo.EXITO,
                    "¡Estación " + nueva.getCodigo() + " incorporada a la red!",
                    nueva.getDireccion() + " ya figura activa con capacidad para " + nueva.getCapacidadMaxima() + " vehículos.",
                    null,
                    null
            );
        } catch (IllegalArgumentException | IllegalStateException ex) {
            bannerEstacionAdmin.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "No se pudo registrar la estación: " + ex.getMessage(),
                    "Verificá que el código no esté repetido y que todos los campos estén completos.",
                    null,
                    null
            );
        }
    }

    // =========================================================================
    // VISTA ADMIN 3: REPORTE DE VIAJES DE UN USUARIO (CON TABLA)
    // =========================================================================
    private JPanel crearVistaAdminReporteUsuario() {
        JPanel vista = new JPanel(new BorderLayout(0, 16));
        vista.setOpaque(false);

        JPanel cabecera = crearCabeceraVista(
                "REPORTES DE AUDITORÍA · ORDENADO DEL MÁS RECIENTE AL MÁS ANTIGUO",
                "Reporte de viajes de un usuario",
                "Ingresá el DNI del usuario para listar todos sus viajes ordenados cronológicamente del más reciente al más antiguo.",
                Tema.ADMIN_ACENTO
        );

        JPanel centro = new JPanel(new BorderLayout(0, 14));
        centro.setOpaque(false);

        TarjetaPanel barraFiltro = new TarjetaPanel(new BorderLayout(14, 0));
        barraFiltro.setBorder(new EmptyBorder(12, 18, 12, 18));

        txtDniReporteUsuario = new CampoTexto("DNI del usuario (Ej: 12345678)", true);
        txtDniReporteUsuario.setText("12345678");

        BotonModerna btnGenerar = BotonModerna.primarioPizarra("Generar reporte (Enter)");
        btnGenerar.addActionListener(e -> ejecutarReporteViajesUsuario());
        adminDefaultButtons.put("REP_USUARIO", btnGenerar);

        barraFiltro.add(Tema.crearEtiquetaCampo("Documento del usuario:"), BorderLayout.WEST);
        barraFiltro.add(txtDniReporteUsuario, BorderLayout.CENTER);
        barraFiltro.add(btnGenerar, BorderLayout.EAST);

        TarjetaPanel cardTabla = new TarjetaPanel(new BorderLayout(0, 10));
        cardTabla.setBorder(new EmptyBorder(18, 20, 18, 20));

        lblSubtituloReporteUsuario = new JLabel("Historial de viajes");
        lblSubtituloReporteUsuario.setFont(Tema.FUENTE_TITULO_2);
        lblSubtituloReporteUsuario.setForeground(Tema.TEXTO_PRINCIPAL);

        modeloTablaViajesUsuario = new DefaultTableModel(
                new String[]{"Fecha y Hora Inicio", "Vehículo", "Tipo de Unidad", "Estación Origen", "Estación Destino", "Duración", "Estado"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        contenedorTablaViajesUsuario = new JPanel(new BorderLayout());
        contenedorTablaViajesUsuario.setOpaque(false);

        cardTabla.add(lblSubtituloReporteUsuario, BorderLayout.NORTH);
        cardTabla.add(contenedorTablaViajesUsuario, BorderLayout.CENTER);

        centro.add(barraFiltro, BorderLayout.NORTH);
        centro.add(cardTabla, BorderLayout.CENTER);

        bannerReporteUsuario = new BannerEstado();

        vista.add(cabecera, BorderLayout.NORTH);
        vista.add(centro, BorderLayout.CENTER);
        vista.add(bannerReporteUsuario, BorderLayout.SOUTH);
        return vista;
    }

    private void ejecutarReporteViajesUsuario() {
        String dni = txtDniReporteUsuario.getText().trim();
        txtDniReporteUsuario.marcarError(false);
        contenedorTablaViajesUsuario.removeAll();
        modeloTablaViajesUsuario.setRowCount(0);

        try {
            List<Viaje> viajes = ctrlReporte.reporteViajesPorUsuario(dni);
            Usuario u = ctrlCuenta.buscarUsuario(dni);
            String nombre = u != null ? u.getNombre() : dni;
            lblSubtituloReporteUsuario.setText("Viajes de " + nombre + " (DNI " + dni + ") · " + viajes.size() + " registros");
            bannerReporteUsuario.ocultar();

            if (viajes.isEmpty()) {
                JLabel vacio = new JLabel("El usuario " + nombre + " (DNI " + dni + ") todavía no tiene viajes registrados.", SwingConstants.CENTER);
                vacio.setFont(Tema.FUENTE_CUERPO);
                vacio.setForeground(Tema.TEXTO_SECUNDARIO);
                contenedorTablaViajesUsuario.add(vacio, BorderLayout.CENTER);
            } else {
                for (Viaje v : viajes) {
                    modeloTablaViajesUsuario.addRow(new Object[]{
                            v.getFechaHoraInicio().format(FORMATO_FECHA),
                            v.getVehiculo().getCodigo(),
                            nombreTipoVehiculo(v.getVehiculo()),
                            v.getEstacionOrigen().getCodigo() + " — " + v.getEstacionOrigen().getDireccion(),
                            v.getEstacionDestino() != null ? (v.getEstacionDestino().getCodigo() + " — " + v.getEstacionDestino().getDireccion()) : "En curso",
                            v.estaFinalizado() ? (v.getDuracionMinutos() + " min") : "En curso",
                            estadoViajeLegible(v)
                    });
                }
                JTable tabla = Tema.crearTablaModerna(modeloTablaViajesUsuario);
                JScrollPane sp = new JScrollPane(tabla);
                sp.setBorder(BorderFactory.createLineBorder(Tema.BORDE_SUTIL));
                contenedorTablaViajesUsuario.add(sp, BorderLayout.CENTER);
            }
        } catch (UsuarioNoEncontradoException ex) {
            txtDniReporteUsuario.marcarError(true);
            lblSubtituloReporteUsuario.setText("Usuario no encontrado");
            bannerReporteUsuario.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "No existe ningún usuario con el DNI '" + dni + "'.",
                    "Revisá el número ingresado (por ejemplo: 12345678).",
                    null,
                    null
            );
        }

        contenedorTablaViajesUsuario.revalidate();
        contenedorTablaViajesUsuario.repaint();
    }

    // =========================================================================
    // VISTA ADMIN 4: REPORTE DE USO DE UNA ESTACIÓN (CON TABLA + TARJETAS)
    // =========================================================================
    private JPanel crearVistaAdminReporteEstacion() {
        JPanel vista = new JPanel(new BorderLayout(0, 16));
        vista.setOpaque(false);

        JPanel cabecera = crearCabeceraVista(
                "REPORTES DE AUDITORÍA · TRÁFICO POR CABECERA",
                "Reporte de uso de una estación",
                "Seleccioná una estación (ordenadas por capacidad máxima) para auditar todos los viajes que salieron o llegaron a ella.",
                Tema.ADMIN_ACENTO
        );

        JPanel centro = new JPanel(new BorderLayout(0, 14));
        centro.setOpaque(false);

        selectorTarjetasEstacionReporte = new JPanel(new GridLayout(1, 0, 12, 0));
        selectorTarjetasEstacionReporte.setOpaque(false);

        TarjetaPanel cardTabla = new TarjetaPanel(new BorderLayout(0, 10));
        cardTabla.setBorder(new EmptyBorder(18, 20, 18, 20));

        lblSubtituloUsoEstacion = new JLabel("Movimientos registrados en la estación");
        lblSubtituloUsoEstacion.setFont(Tema.FUENTE_TITULO_2);
        lblSubtituloUsoEstacion.setForeground(Tema.TEXTO_PRINCIPAL);

        modeloTablaUsoEstacion = new DefaultTableModel(
                new String[]{"Fecha y Hora Inicio", "Usuario (DNI)", "Vehículo", "Operación en Estación", "Trayecto (Origen → Destino)", "Duración"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        contenedorTablaUsoEstacion = new JPanel(new BorderLayout());
        contenedorTablaUsoEstacion.setOpaque(false);

        cardTabla.add(lblSubtituloUsoEstacion, BorderLayout.NORTH);
        cardTabla.add(contenedorTablaUsoEstacion, BorderLayout.CENTER);

        centro.add(selectorTarjetasEstacionReporte, BorderLayout.NORTH);
        centro.add(cardTabla, BorderLayout.CENTER);

        bannerReporteEstacion = new BannerEstado();

        vista.add(cabecera, BorderLayout.NORTH);
        vista.add(centro, BorderLayout.CENTER);
        vista.add(bannerReporteEstacion, BorderLayout.SOUTH);
        return vista;
    }

    private void refrescarReporteUsoEstacion(String codigoEstacion) {
        this.codigoEstacionReporteSeleccionada = codigoEstacion;
        selectorTarjetasEstacionReporte.removeAll();

        List<Estacion> ordenadas = ctrlEstacion.obtenerEstacionesOrdenadasPorCapacidad();
        for (Estacion est : ordenadas) {
            boolean activa = est.getCodigo().equalsIgnoreCase(codigoEstacion);
            TarjetaPanel card = new TarjetaPanel(new GridLayout(3, 1, 0, 2))
                    .configurarEstilo(
                            activa ? Tema.ACCION_TINTE : Color.WHITE,
                            activa ? Tema.ACCION_PRIMARIA : Tema.BORDE_SUTIL,
                            activa ? Tema.ACCION_PRIMARIA : null,
                            false
                    );
            card.setBorder(new EmptyBorder(12, 16, 12, 16));
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JLabel l1 = new JLabel(est.getCodigo() + " · Cap. " + est.getCapacidadMaxima() + " anclajes");
            l1.setFont(Tema.FUENTE_MONO_CHICA);
            l1.setForeground(activa ? Tema.ACCION_PRIMARIA : Tema.TEXTO_TERCIARIO);

            JLabel l2 = new JLabel(est.getDireccion());
            l2.setFont(Tema.FUENTE_CUERPO_BOLD);
            l2.setForeground(Tema.TEXTO_PRINCIPAL);

            JLabel l3 = new JLabel(est.getVehiculosEstacionados().size() + " vehículos estacionados hoy");
            l3.setFont(Tema.FUENTE_CUERPO);
            l3.setForeground(Tema.TEXTO_SECUNDARIO);

            card.add(l1);
            card.add(l2);
            card.add(l3);

            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    refrescarReporteUsoEstacion(est.getCodigo());
                }
            });

            selectorTarjetasEstacionReporte.add(card);
        }

        contenedorTablaUsoEstacion.removeAll();
        modeloTablaUsoEstacion.setRowCount(0);

        try {
            List<Viaje> viajes = ctrlReporte.reporteUsoPorEstacion(codigoEstacion);
            Estacion est = repositorio.buscarEstacion(codigoEstacion);
            String dir = est != null ? est.getDireccion() : "";
            lblSubtituloUsoEstacion.setText("Uso de la estación " + codigoEstacion + " — " + dir + " (" + viajes.size() + " viajes)");
            bannerReporteEstacion.ocultar();

            if (viajes.isEmpty()) {
                JLabel vacio = new JLabel("No hay viajes registrados que hayan salido o llegado a la estación " + codigoEstacion + " todavía.", SwingConstants.CENTER);
                vacio.setFont(Tema.FUENTE_CUERPO);
                vacio.setForeground(Tema.TEXTO_SECUNDARIO);
                contenedorTablaUsoEstacion.add(vacio, BorderLayout.CENTER);
            } else {
                for (Viaje v : viajes) {
                    boolean fueOrigen = v.getEstacionOrigen() != null && v.getEstacionOrigen().getCodigo().equalsIgnoreCase(codigoEstacion);
                    boolean fueDestino = v.getEstacionDestino() != null && v.getEstacionDestino().getCodigo().equalsIgnoreCase(codigoEstacion);
                    String rolEstacion = (fueOrigen && fueDestino) ? "Salida y Devolución" : (fueOrigen ? "Retiro (Origen)" : "Devolución (Destino)");
                    String destCode = v.getEstacionDestino() != null ? v.getEstacionDestino().getCodigo() : "En curso";

                    modeloTablaUsoEstacion.addRow(new Object[]{
                            v.getFechaHoraInicio().format(FORMATO_FECHA),
                            v.getUsuario().getNombre() + " (" + v.getUsuario().getDocumento() + ")",
                            v.getVehiculo().getCodigo() + " · " + nombreTipoVehiculo(v.getVehiculo()),
                            rolEstacion,
                            v.getEstacionOrigen().getCodigo() + " → " + destCode,
                            v.estaFinalizado() ? (v.getDuracionMinutos() + " min") : "En curso"
                    });
                }
                JTable tabla = Tema.crearTablaModerna(modeloTablaUsoEstacion);
                JScrollPane sp = new JScrollPane(tabla);
                sp.setBorder(BorderFactory.createLineBorder(Tema.BORDE_SUTIL));
                contenedorTablaUsoEstacion.add(sp, BorderLayout.CENTER);
            }
        } catch (EstacionNoEncontradaException ex) {
            bannerReporteEstacion.mostrar(
                    BannerEstado.Tipo.ERROR,
                    "No se encontró la estación '" + codigoEstacion + "'.",
                    "Seleccioná una de las tarjetas de estación disponibles arriba.",
                    null,
                    null
            );
        }

        selectorTarjetasEstacionReporte.revalidate();
        selectorTarjetasEstacionReporte.repaint();
        contenedorTablaUsoEstacion.revalidate();
        contenedorTablaUsoEstacion.repaint();
    }

    // =========================================================================
    // MÉTODOS AUXILIARES Y NAVEGACIÓN ENTRE VISTAS
    // =========================================================================
    private void mostrarHome() {
        int cantEst = repositorio.getEstaciones().size();
        int cantVeh = 0;
        for (Estacion e : repositorio.getEstaciones()) {
            cantVeh += e.getVehiculosEstacionados().size();
        }
        if (lblHomeRedBadge != null) {
            lblHomeRedBadge.setText(cantEst + " estaciones operativas · " + cantVeh + " vehículos en dársena");
        }
        rootLayout.show(rootPanel, "HOME");
        if (frame != null) {
            frame.getRootPane().setDefaultButton(null);
        }
    }

    private void abrirModoUsuario(String subvista) {
        poblarCombosEstaciones();
        actualizarSidebarUsuario(dniActivo);
        sincronizarCamposDniUsuario(dniActivo);

        for (Map.Entry<String, BotonNavegacion> entry : userNavButtons.entrySet()) {
            entry.getValue().setActivo(entry.getKey().equals(subvista));
        }

        if ("RETIRAR".equals(subvista)) {
            String item = (String) comboEstacionRetiro.getSelectedItem();
            if (item != null && item.contains(" — ")) {
                refrescarGrillaVehiculosEstacion(item.split(" — ")[0].trim());
            }
        } else if ("DEVOLVER".equals(subvista)) {
            refrescarEstadoViajeActivoDevolucion();
        } else if ("SALDO".equals(subvista)) {
            refrescarTarjetaSaldoUsuario();
        } else if ("PLAN".equals(subvista)) {
            refrescarEstadoPlanVista();
        }

        rootLayout.show(rootPanel, "USUARIO");
        userContentLayout.show(userContentPanel, subvista);

        if (frame != null) {
            frame.getRootPane().setDefaultButton(userDefaultButtons.get(subvista));
        }
    }

    private void abrirModoAdmin(String subvista) {
        for (Map.Entry<String, BotonNavegacion> entry : adminNavButtons.entrySet()) {
            entry.getValue().setActivo(entry.getKey().equals(subvista));
        }

        if ("BUSCAR".equals(subvista)) {
            ejecutarBuscarUsuarioAdmin();
        } else if ("ESTACION".equals(subvista)) {
            refrescarListaEstacionesAdmin();
        } else if ("REP_USUARIO".equals(subvista)) {
            ejecutarReporteViajesUsuario();
        } else if ("REP_ESTACION".equals(subvista)) {
            refrescarReporteUsoEstacion(codigoEstacionReporteSeleccionada);
        }

        rootLayout.show(rootPanel, "ADMIN");
        adminContentLayout.show(adminContentPanel, subvista);

        if (frame != null) {
            frame.getRootPane().setDefaultButton(adminDefaultButtons.get(subvista));
        }
    }

    private void poblarCombosEstaciones() {
        List<Estacion> estaciones = repositorio.getEstaciones();
        String selRetiro = (String) comboEstacionRetiro.getSelectedItem();
        String selDevol = (String) comboEstacionDevolucion.getSelectedItem();

        comboEstacionRetiro.removeAllItems();
        comboEstacionDevolucion.removeAllItems();

        for (Estacion e : estaciones) {
            String etiqueta = e.getCodigo() + " — " + e.getDireccion() + " (" + e.getVehiculosEstacionados().size() + "/" + e.getCapacidadMaxima() + ")";
            comboEstacionRetiro.addItem(etiqueta);
            comboEstacionDevolucion.addItem(etiqueta);
        }

        restaurarSeleccionCombo(comboEstacionRetiro, selRetiro);
        restaurarSeleccionCombo(comboEstacionDevolucion, selDevol);
    }

    private void restaurarSeleccionCombo(JComboBox<String> combo, String seleccionPrevia) {
        if (seleccionPrevia == null || !seleccionPrevia.contains(" — ")) return;
        String codPrevio = seleccionPrevia.split(" — ")[0].trim();
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).startsWith(codPrevio + " — ")) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void actualizarSidebarUsuario(String dni) {
        Usuario u = ctrlCuenta.buscarUsuario(dni);
        if (u == null && !repositorio.getUsuarios().isEmpty()) {
            u = repositorio.getUsuarios().get(0);
        }
        if (u != null) {
            dniActivo = u.getDocumento();
            lblSidebarDni.setText("USUARIO · DNI " + u.getDocumento());
            lblSidebarNombre.setText(u.getNombre());
            lblSidebarEmail.setText(u.getEmail());
            lblSidebarSaldo.setText(Tema.formatearPesos(u.getCuenta().getSaldo()));
            lblSidebarPlan.setText(u.getTienePlanMensual() ? "Plan Activo" : "Sin plan");
            lblSidebarPlan.setForeground(u.getTienePlanMensual() ? Tema.EXITO_ICONO : Tema.TEXTO_SIDEBAR_MUTE);
        }
    }

    private void sincronizarCamposDniUsuario(String dni) {
        if (txtDniRetiro != null) txtDniRetiro.setText(dni);
        if (txtDniDevolucion != null) txtDniDevolucion.setText(dni);
        if (txtDniSaldo != null) txtDniSaldo.setText(dni);
        if (txtDniPlan != null) txtDniPlan.setText(dni);
    }

    private JPanel crearCabeceraVista(String overline, String titulo, String subtitulo, Color colorOverline) {
        JPanel p = new JPanel(new GridLayout(3, 1, 0, 2));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDE_SUTIL),
                new EmptyBorder(0, 0, 12, 0)
        ));

        JLabel lOver = new JLabel(overline);
        lOver.setFont(Tema.FUENTE_ETIQUETA);
        lOver.setForeground(colorOverline);

        JLabel lTit = new JLabel(titulo);
        lTit.setFont(Tema.FUENTE_TITULO_1);
        lTit.setForeground(Tema.TEXTO_PRINCIPAL);

        JLabel lSub = new JLabel(subtitulo);
        lSub.setFont(Tema.FUENTE_SUBTITULO);
        lSub.setForeground(Tema.TEXTO_SECUNDARIO);

        p.add(lOver);
        p.add(lTit);
        p.add(lSub);
        return p;
    }

    private void agregarCampoAlBox(JPanel contenedor, String etiqueta, JComponent campo) {
        JLabel lbl = Tema.crearEtiquetaCampo(etiqueta);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, Tema.ALTO_CAMPO));
        contenedor.add(lbl);
        contenedor.add(Box.createVerticalStrut(5));
        contenedor.add(campo);
        contenedor.add(Box.createVerticalStrut(12));
    }

    private String nombreTipoVehiculo(Vehiculo v) {
        if (v instanceof BicicletaComun) return "Bicicleta común";
        if (v instanceof BicicletaElectrica) return "Bicicleta eléctrica";
        if (v instanceof MonopatinElectrico) return "Monopatín eléctrico";
        return "Vehículo";
    }

    private String estadoViajeLegible(Viaje v) {
        if (!v.estaFinalizado()) return "En curso";
        if (v.fueCancelado()) return "Cancelado (≤2 min)";
        return "Finalizado";
    }

    private Object[] filaViajeParaTabla(Viaje v) {
        return new Object[]{
                v.getFechaHoraInicio().format(FORMATO_FECHA),
                v.getVehiculo().getCodigo() + " (" + nombreTipoVehiculo(v.getVehiculo()) + ")",
                v.getEstacionOrigen().getCodigo(),
                v.getEstacionDestino() != null ? v.getEstacionDestino().getCodigo() : "-",
                v.estaFinalizado() ? (v.getDuracionMinutos() + " min") : "En curso",
                estadoViajeLegible(v)
        };
    }
}
