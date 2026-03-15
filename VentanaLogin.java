import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;

    public VentanaLogin() {
        setTitle("Fidecompro - Login");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        add(txtUsuario);

        add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        add(txtContrasena);

        add(new JLabel(""));
        add(new JLabel(""));

        btnIngresar = new JButton("Iniciar sesión");
        add(new JLabel(""));
        add(btnIngresar);

        btnIngresar.addActionListener(e -> ingresar());

        setVisible(true);
    }

    private void ingresar() {
        String user = txtUsuario.getText();
        String pass = new String(txtContrasena.getPassword());

        try {
            Usuario u = BaseDatos.login(user, pass);
            JOptionPane.showMessageDialog(this, "Bienvenido " + u.getNombre());
            dispose();
            new VentanaPrincipal(u);
        } catch (AutenticacionException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
