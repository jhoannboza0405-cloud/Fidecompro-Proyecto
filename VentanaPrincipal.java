import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private Usuario usuarioActual;

    public VentanaPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;

        setTitle("Fidecompro - Menú Principal");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Clientes", new PanelClientes(usuarioActual));
        tabs.add("Productos", new PanelProductos(usuarioActual));
        tabs.add("Facturación", new PanelFacturacion(usuarioActual));

        add(tabs, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar sesión");
        btnCerrar.addActionListener(e -> {
            dispose();
            new VentanaLogin();
        });

        JLabel lblUsuario = new JLabel("Sesión activa: " + usuarioActual.getResumen());

        JPanel sur = new JPanel(new BorderLayout());
        sur.add(lblUsuario, BorderLayout.CENTER);
        sur.add(btnCerrar, BorderLayout.EAST);

        add(sur, BorderLayout.SOUTH);

        setVisible(true);
    }
}
