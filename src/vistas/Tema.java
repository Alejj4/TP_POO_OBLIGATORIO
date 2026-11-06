package vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Sistema de diseño y componentes visuales personalizados para Schipani Express.
 * Dirección visual: "Señalética Porteña Contemporánea" (Marfil Cálido, Pizarra Subte y Verde Ciclovía).
 * Compatible con Java 17 estándar sin librerías externas.
 */
public final class Tema {

    private Tema() {}

    // =========================================================================
    // 1. PALETA DE COLORES (Contrastes verificados WCAG AA)
    // =========================================================================
    public static final Color FONDO_LIENZO       = new Color(0xF6, 0xF5, 0xF0); // #F6F5F0 Marfil Urbano
    public static final Color SUPERFICIE_TARJETA = new Color(0xFF, 0xFF, 0xFF); // #FFFFFF Blanco Puro
    public static final Color SUPERFICIE_SUTIL   = new Color(0xEF, 0xEC, 0xE4); // #EFECE4 Piedra Clara
    public static final Color SIDEBAR_FONDO      = new Color(0x11, 0x18, 0x22); // #111822 Pizarra Nocturno
    public static final Color SIDEBAR_TARJETA    = new Color(0x1E, 0x29, 0x3B); // #1E293B Pizarra Elevada
    public static final Color SIDEBAR_BORDE      = new Color(0x33, 0x41, 0x55); // #334155 Borde Pizarra

    public static final Color TEXTO_PRINCIPAL    = new Color(0x11, 0x18, 0x22); // #111822 Tinta Principal (16.4:1)
    public static final Color TEXTO_SECUNDARIO   = new Color(0x52, 0x5E, 0x6E); // #525E6E Gris Editorial (6.3:1)
    public static final Color TEXTO_TERCIARIO    = new Color(0x64, 0x74, 0x8B); // #64748B Etiquetas (4.7:1)
    public static final Color TEXTO_SOBRE_OSCURO = new Color(0xF8, 0xFA, 0xFC); // #F8FAFC Blanco Cálido
    public static final Color TEXTO_SIDEBAR_MUTE = new Color(0x94, 0xA3, 0xB8); // #94A3B8 Gris Azulado

    public static final Color ACCION_PRIMARIA    = new Color(0x0D, 0x7A, 0x5F); // #0D7A5F Verde Ciclovía (5.4:1)
    public static final Color ACCION_HOVER       = new Color(0x09, 0x5E, 0x49); // #095E49 Verde Profundo
    public static final Color ACCION_PRESIONADO  = new Color(0x06, 0x47, 0x37); // #064737 Verde Presionado
    public static final Color ACCION_TINTE       = new Color(0xE6, 0xF4, 0xF0); // #E6F4F0 Selección Suave

    public static final Color ADMIN_ACENTO       = new Color(0xD9, 0x77, 0x06); // #D97706 Ámbar Subte
    public static final Color ADMIN_HOVER        = new Color(0xB4, 0x53, 0x09); // #B45309 Ámbar Oscuro
    public static final Color ADMIN_TINTE        = new Color(0xFE, 0xF3, 0xC7); // #FEF3C7 Fondo Ámbar Suave

    public static final Color BORDE_SUTIL        = new Color(0xE2, 0xE0, 0xD8); // #E2E0D8 Borde de Tarjeta
    public static final Color BORDE_CAMPO        = new Color(0xD5, 0xD2, 0xC8); // #D5D2C8 Borde de Input
    public static final Color SOMBRA_SUAVE       = new Color(17, 24, 34, 12);   // Sombra de elevación

    // Estados semánticos (Éxito, Advertencia, Error, Información)
    public static final Color EXITO_FONDO        = new Color(0xEC, 0xFD, 0xF5); // #ECFDF5
    public static final Color EXITO_BORDE        = new Color(0x6E, 0xE7, 0xB7); // #6EE7B7
    public static final Color EXITO_TEXTO        = new Color(0x06, 0x5F, 0x46); // #065F46
    public static final Color EXITO_ICONO        = new Color(0x10, 0xB9, 0x81); // #10B981

    public static final Color ALERTA_FONDO       = new Color(0xFF, 0xFB, 0xEB); // #FFFBEB
    public static final Color ALERTA_BORDE       = new Color(0xFC, 0xD3, 0x4D); // #FCD34D
    public static final Color ALERTA_TEXTO       = new Color(0x92, 0x40, 0x0E); // #92400E

    public static final Color ERROR_FONDO        = new Color(0xFE, 0xF2, 0xF2); // #FEF2F2
    public static final Color ERROR_BORDE        = new Color(0xFC, 0xA5, 0xA5); // #FCA5A5
    public static final Color ERROR_TEXTO        = new Color(0x99, 0x1B, 0x1B); // #991B1B

    public static final Color INFO_FONDO         = new Color(0xEF, 0xF6, 0xFF); // #EFF6FF
    public static final Color INFO_BORDE         = new Color(0x93, 0xC5, 0xFD); // #93C5FD
    public static final Color INFO_TEXTO         = new Color(0x1E, 0x40, 0xAF); // #1E40AF

    // =========================================================================
    // 2. TIPOGRAFÍA (Familias nativas de Windows con fallback multiplataforma)
    // =========================================================================
    public static final Font FUENTE_HERO         = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FUENTE_TITULO_1     = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FUENTE_TITULO_2     = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FUENTE_SUBTITULO    = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FUENTE_CUERPO       = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_CUERPO_BOLD  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FUENTE_ETIQUETA     = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FUENTE_BOTON        = new Font("Segoe UI", Font.BOLD, 13);

    public static final Font FUENTE_MONO_GRANDE  = new Font("Consolas", Font.BOLD, 22);
    public static final Font FUENTE_MONO_MEDIA   = new Font("Consolas", Font.BOLD, 14);
    public static final Font FUENTE_MONO_CHICA   = new Font("Consolas", Font.BOLD, 12);

    // =========================================================================
    // 3. DIMENSIONES Y ESCALA ESPACIAL
    // =========================================================================
    public static final int VENTANA_ANCHO        = 1180;
    public static final int VENTANA_ALTO         = 760;
    public static final int SIDEBAR_ANCHO        = 265;
    public static final int RADIO_TARJETA        = 16;
    public static final int RADIO_CONTROL        = 10;
    public static final int ALTO_CAMPO           = 40;
    public static final int ALTO_BOTON           = 42;

    private static final DecimalFormat FORMATO_MONEDA;
    static {
        DecimalFormatSymbols sym = new DecimalFormatSymbols(new Locale("es", "AR"));
        sym.setGroupingSeparator('.');
        sym.setDecimalSeparator(',');
        FORMATO_MONEDA = new DecimalFormat("#,##0.00", sym);
    }

    public static String formatearPesos(double monto) {
        return "$" + FORMATO_MONEDA.format(monto);
    }

    public static void activarAntialiasing(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    // =========================================================================
    // 4. COMPONENTE: TarjetaPanel (Bordes redondeados + sombra suave + franja superior opcional)
    // =========================================================================
    public static class TarjetaPanel extends JPanel {
        private Color colorFondo = SUPERFICIE_TARJETA;
        private Color colorBorde = BORDE_SUTIL;
        private Color franjaSuperior = null;
        private int radio = RADIO_TARJETA;
        private boolean conSombra = true;

        public TarjetaPanel(LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }

        public TarjetaPanel() {
            this(new BorderLayout());
        }

        public TarjetaPanel configurarEstilo(Color fondo, Color borde, Color franjaTop, boolean sombra) {
            this.colorFondo = fondo;
            this.colorBorde = borde;
            this.franjaSuperior = franjaTop;
            this.conSombra = sombra;
            repaint();
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            activarAntialiasing(g2);

            int offsetSombra = conSombra ? 3 : 0;
            int w = getWidth() - 1;
            int h = getHeight() - 1 - offsetSombra;

            if (conSombra) {
                g2.setColor(SOMBRA_SUAVE);
                g2.fill(new RoundRectangle2D.Double(1, 3, w - 1, h, radio, radio));
            }

            g2.setColor(colorFondo);
            g2.fill(new RoundRectangle2D.Double(0, 0, w, h, radio, radio));

            if (franjaSuperior != null) {
                Shape clipOriginal = g2.getClip();
                g2.setClip(new RoundRectangle2D.Double(0, 0, w, h, radio, radio));
                g2.setColor(franjaSuperior);
                g2.fillRect(0, 0, w + 1, 5);
                g2.setClip(clipOriginal);
            }

            if (colorBorde != null) {
                g2.setColor(colorBorde);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, radio, radio));
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =========================================================================
    // 5. COMPONENTE: BotonPrimario / BotonSecundario (Hover, Presionado, Foco)
    // =========================================================================
    public static class BotonModerna extends JButton {
        private final Color colorBase;
        private final Color colorHover;
        private final Color colorPresionado;
        private final Color colorTexto;
        private final Color colorBorde;
        private boolean mouseEncima = false;
        private boolean mousePresionado = false;

        public BotonModerna(String texto, Color base, Color hover, Color presionado, Color textoColor, Color borde) {
            super(texto);
            this.colorBase = base;
            this.colorHover = hover;
            this.colorPresionado = presionado;
            this.colorTexto = textoColor;
            this.colorBorde = borde;

            setFont(FUENTE_BOTON);
            setForeground(textoColor);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(getPreferredSize().width + 32, ALTO_BOTON));
            setBorder(new EmptyBorder(0, 18, 0, 18));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    mouseEncima = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    mouseEncima = false;
                    mousePresionado = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (isEnabled()) {
                        mousePresionado = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    mousePresionado = false;
                    repaint();
                }
            });
        }

        public static BotonModerna primarioVerde(String texto) {
            return new BotonModerna(texto, ACCION_PRIMARIA, ACCION_HOVER, ACCION_PRESIONADO, Color.WHITE, null);
        }

        public static BotonModerna primarioPizarra(String texto) {
            return new BotonModerna(texto, SIDEBAR_FONDO, SIDEBAR_TARJETA, Color.BLACK, Color.WHITE, null);
        }

        public static BotonModerna primarioAmbar(String texto) {
            return new BotonModerna(texto, ADMIN_ACENTO, ADMIN_HOVER, new Color(0x92, 0x40, 0x0E), Color.WHITE, null);
        }

        public static BotonModerna secundario(String texto) {
            return new BotonModerna(texto, SUPERFICIE_SUTIL, new Color(0xE2, 0xDF, 0xD5), new Color(0xD5, 0xD2, 0xC8), TEXTO_PRINCIPAL, BORDE_CAMPO);
        }

        public static BotonModerna peligroSuave(String texto) {
            return new BotonModerna(texto, ERROR_FONDO, new Color(0xFE, 0xE2, 0xE2), new Color(0xFE, 0xCA, 0xCA), ERROR_TEXTO, ERROR_BORDE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            activarAntialiasing(g2);

            int w = getWidth();
            int h = getHeight();

            if (!isEnabled()) {
                g2.setColor(new Color(0xE2, 0xE0, 0xD8));
                g2.fill(new RoundRectangle2D.Double(0, 0, w, h, RADIO_CONTROL, RADIO_CONTROL));
                setForeground(TEXTO_TERCIARIO);
            } else {
                setForeground(colorTexto);
                Color actual = mousePresionado ? colorPresionado : (mouseEncima ? colorHover : colorBase);
                g2.setColor(actual);
                g2.fill(new RoundRectangle2D.Double(0, 0, w, h, RADIO_CONTROL, RADIO_CONTROL));

                if (colorBorde != null) {
                    g2.setColor(colorBorde);
                    g2.setStroke(new BasicStroke(1f));
                    g2.draw(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, RADIO_CONTROL, RADIO_CONTROL));
                }
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =========================================================================
    // 6. COMPONENTE: BotonNavegacion (Items de la barra lateral con indicador activo)
    // =========================================================================
    public static class BotonNavegacion extends JButton {
        private final String numeroOrden;
        private final Color colorActivo;
        private boolean activo = false;
        private boolean hover = false;

        public BotonNavegacion(String numeroOrden, String texto, Color colorActivo) {
            super(texto);
            this.numeroOrden = numeroOrden;
            this.colorActivo = colorActivo;

            setFont(FUENTE_CUERPO_BOLD);
            setHorizontalAlignment(SwingConstants.LEFT);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(225, 42));
            setMaximumSize(new Dimension(240, 42));
            setBorder(new EmptyBorder(0, 38, 0, 12));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        public void setActivo(boolean activo) {
            this.activo = activo;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            activarAntialiasing(g2);

            int w = getWidth();
            int h = getHeight();

            if (activo) {
                g2.setColor(colorActivo);
                g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 10, 10));
                setForeground(Color.WHITE);
            } else if (hover) {
                g2.setColor(SIDEBAR_TARJETA);
                g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 10, 10));
                setForeground(TEXTO_SOBRE_OSCURO);
            } else {
                setForeground(TEXTO_SIDEBAR_MUTE);
            }

            // Número monoespaciado a la izquierda
            g2.setFont(FUENTE_MONO_CHICA);
            g2.setColor(activo ? new Color(255, 255, 255, 210) : new Color(148, 163, 184, 170));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(numeroOrden, 13, (h + fm.getAscent() - fm.getDescent()) / 2);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =========================================================================
    // 7. COMPONENTE: CampoTexto (Placeholder, foco esmeralda y estado de error)
    // =========================================================================
    public static class CampoTexto extends JTextField {
        private final String placeholder;
        private boolean enFoco = false;
        private boolean conError = false;

        public CampoTexto(String placeholder, boolean monoespaciado) {
            super();
            this.placeholder = placeholder;
            setOpaque(false);
            setFont(monoespaciado ? FUENTE_MONO_MEDIA : FUENTE_CUERPO);
            setForeground(TEXTO_PRINCIPAL);
            setCaretColor(ACCION_PRIMARIA);
            setBorder(new EmptyBorder(0, 14, 0, 14));
            setPreferredSize(new Dimension(220, ALTO_CAMPO));

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    enFoco = true;
                    conError = false;
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    enFoco = false;
                    repaint();
                }
            });
        }

        public void marcarError(boolean error) {
            this.conError = error;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            activarAntialiasing(g2);

            int w = getWidth() - 1;
            int h = getHeight() - 1;

            g2.setColor(isEnabled() ? Color.WHITE : SUPERFICIE_SUTIL);
            g2.fill(new RoundRectangle2D.Double(0, 0, w, h, RADIO_CONTROL, RADIO_CONTROL));

            if (conError) {
                g2.setColor(new Color(239, 68, 68, 35));
                g2.setStroke(new BasicStroke(3f));
                g2.draw(new RoundRectangle2D.Double(1, 1, w - 2, h - 2, RADIO_CONTROL, RADIO_CONTROL));
                g2.setColor(new Color(0xDC, 0x26, 0x26));
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, RADIO_CONTROL, RADIO_CONTROL));
            } else if (enFoco) {
                g2.setColor(new Color(13, 122, 95, 36));
                g2.setStroke(new BasicStroke(3f));
                g2.draw(new RoundRectangle2D.Double(1, 1, w - 2, h - 2, RADIO_CONTROL, RADIO_CONTROL));
                g2.setColor(ACCION_PRIMARIA);
                g2.setStroke(new BasicStroke(1.6f));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, RADIO_CONTROL, RADIO_CONTROL));
            } else {
                g2.setColor(BORDE_CAMPO);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, RADIO_CONTROL, RADIO_CONTROL));
            }

            super.paintComponent(g);

            if (getText().isEmpty() && placeholder != null && !placeholder.isEmpty()) {
                g2.setFont(FUENTE_CUERPO);
                g2.setColor(TEXTO_TERCIARIO);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(placeholder, 14, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }

            g2.dispose();
        }
    }

    // =========================================================================
    // 8. COMPONENTE: SelectorCombo (JComboBox estilizado sin bordes grises clásicos)
    // =========================================================================
    public static <T> JComboBox<T> crearCombo(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        estilizarCombo(combo);
        return combo;
    }

    public static <T> void estilizarCombo(JComboBox<T> combo) {
        combo.setFont(FUENTE_CUERPO_BOLD);
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXTO_PRINCIPAL);
        combo.setPreferredSize(new Dimension(220, ALTO_CAMPO));
        combo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_CAMPO, 1, true),
                new EmptyBorder(4, 10, 4, 10)
        ));
        combo.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton("▾");
                btn.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 8));
                btn.setContentAreaFilled(false);
                btn.setFocusPainted(false);
                btn.setForeground(TEXTO_SECUNDARIO);
                btn.setFont(FUENTE_CUERPO_BOLD);
                return btn;
            }
        });
    }

    // =========================================================================
    // 9. COMPONENTE: BannerEstado (Mensajes de error, advertencia, info y éxito in-app)
    // =========================================================================
    public static class BannerEstado extends JPanel {
        public enum Tipo { EXITO, ERROR, ADVERTENCIA, INFO }

        private Tipo tipo = Tipo.INFO;
        private final JLabel lblTitulo = new JLabel();
        private final JLabel lblDetalle = new JLabel();
        private final JButton btnAccion = new JButton();
        private Runnable accionCallback;

        public BannerEstado() {
            super(new BorderLayout(14, 0));
            setOpaque(false);
            setBorder(new EmptyBorder(12, 16, 12, 16));

            JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
            textos.setOpaque(false);
            lblTitulo.setFont(FUENTE_CUERPO_BOLD);
            lblDetalle.setFont(FUENTE_CUERPO);
            textos.add(lblTitulo);
            textos.add(lblDetalle);

            btnAccion.setFont(FUENTE_ETIQUETA);
            btnAccion.setFocusPainted(false);
            btnAccion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnAccion.setOpaque(false);
            btnAccion.setContentAreaFilled(false);
            btnAccion.setVisible(false);
            btnAccion.addActionListener(e -> {
                if (accionCallback != null) accionCallback.run();
            });

            add(textos, BorderLayout.CENTER);
            add(btnAccion, BorderLayout.EAST);
            setVisible(false);
        }

        public void mostrar(Tipo tipo, String titulo, String detalle, String textoBoton, Runnable callback) {
            this.tipo = tipo;
            this.accionCallback = callback;

            String prefijo = switch (tipo) {
                case EXITO -> "✓  ";
                case ERROR -> "✕  ";
                case ADVERTENCIA -> "⚠  ";
                case INFO -> "ℹ  ";
            };
            Color colorTexto = switch (tipo) {
                case EXITO -> EXITO_TEXTO;
                case ERROR -> ERROR_TEXTO;
                case ADVERTENCIA -> ALERTA_TEXTO;
                case INFO -> INFO_TEXTO;
            };

            lblTitulo.setText(prefijo + titulo);
            lblTitulo.setForeground(colorTexto);
            lblDetalle.setText(detalle);
            lblDetalle.setForeground(colorTexto);

            if (textoBoton != null && callback != null) {
                btnAccion.setText(textoBoton + " →");
                btnAccion.setForeground(colorTexto);
                btnAccion.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(colorTexto, 1, true),
                        new EmptyBorder(5, 10, 5, 10)
                ));
                btnAccion.setVisible(true);
            } else {
                btnAccion.setVisible(false);
            }

            setVisible(true);
            revalidate();
            repaint();
        }

        public void ocultar() {
            setVisible(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            activarAntialiasing(g2);

            Color fondo = switch (tipo) {
                case EXITO -> EXITO_FONDO;
                case ERROR -> ERROR_FONDO;
                case ADVERTENCIA -> ALERTA_FONDO;
                case INFO -> INFO_FONDO;
            };
            Color borde = switch (tipo) {
                case EXITO -> EXITO_BORDE;
                case ERROR -> ERROR_BORDE;
                case ADVERTENCIA -> ALERTA_BORDE;
                case INFO -> INFO_BORDE;
            };

            int w = getWidth() - 1;
            int h = getHeight() - 1;
            g2.setColor(fondo);
            g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 12, 12));
            g2.setColor(borde);
            g2.setStroke(new BasicStroke(1.3f));
            g2.draw(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, 12, 12));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =========================================================================
    // 10. COMPONENTE: BarraBateria (Indicador visual de carga 0% - 100%)
    // =========================================================================
    public static class BarraBateria extends JComponent {
        private int porcentaje;

        public BarraBateria(int porcentaje) {
            this.porcentaje = Math.max(0, Math.min(100, porcentaje));
            setPreferredSize(new Dimension(120, 8));
        }

        public void setPorcentaje(int porcentaje) {
            this.porcentaje = Math.max(0, Math.min(100, porcentaje));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            activarAntialiasing(g2);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(BORDE_SUTIL);
            g2.fill(new RoundRectangle2D.Double(0, 0, w, h, h, h));

            Color colorBarra;
            if (porcentaje < 15) {
                colorBarra = new Color(0xEF, 0x44, 0x44); // Rojo batería baja (<15%)
            } else if (porcentaje < 40) {
                colorBarra = ADMIN_ACENTO;                // Ámbar batería media
            } else {
                colorBarra = EXITO_ICONO;                 // Verde esmeralda
            }

            int anchoCarga = (int) Math.round((w * porcentaje) / 100.0);
            if (anchoCarga > 0) {
                g2.setColor(colorBarra);
                g2.fill(new RoundRectangle2D.Double(0, 0, Math.max(h, anchoCarga), h, h, h));
            }

            g2.dispose();
        }
    }

    // =========================================================================
    // 11. COMPONENTE: TablaModerna (JTable con selección esmeralda y filas cebradas)
    // =========================================================================
    public static JTable crearTablaModerna(TableModel model) {
        JTable tabla = new JTable(model);
        tabla.setFont(FUENTE_CUERPO);
        tabla.setForeground(TEXTO_PRINCIPAL);
        tabla.setBackground(Color.WHITE);
        tabla.setRowHeight(38);
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setGridColor(BORDE_SUTIL);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setSelectionBackground(ACCION_TINTE);
        tabla.setSelectionForeground(TEXTO_PRINCIPAL);
        tabla.setFillsViewportHeight(true);

        JTableHeader header = tabla.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setFont(FUENTE_ETIQUETA);
                lbl.setForeground(TEXTO_TERCIARIO);
                lbl.setBackground(FONDO_LIENZO);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE_SUTIL),
                        new EmptyBorder(0, 14, 0, 14)
                ));
                return lbl;
            }
        });

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBorder(new EmptyBorder(0, 14, 0, 14));
                if (isSelected) {
                    lbl.setBackground(ACCION_TINTE);
                    lbl.setForeground(TEXTO_PRINCIPAL);
                    lbl.setFont(FUENTE_CUERPO_BOLD);
                } else {
                    lbl.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xFA, 0xF9, 0xF5));
                    lbl.setForeground(TEXTO_PRINCIPAL);
                    lbl.setFont(FUENTE_CUERPO);
                }
                return lbl;
            }
        };

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        return tabla;
    }

    public static JLabel crearEtiquetaCampo(String texto) {
        JLabel lbl = new JLabel(texto.toUpperCase());
        lbl.setFont(FUENTE_ETIQUETA);
        lbl.setForeground(TEXTO_SECUNDARIO);
        return lbl;
    }

    public static JLabel crearPastilla(String texto, Color fondo, Color colorTexto) {
        JLabel lbl = new JLabel(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                activarAntialiasing(g2);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setOpaque(false);
        lbl.setBackground(fondo);
        lbl.setForeground(colorTexto);
        lbl.setFont(FUENTE_ETIQUETA);
        lbl.setBorder(new EmptyBorder(4, 10, 4, 10));
        return lbl;
    }
}
