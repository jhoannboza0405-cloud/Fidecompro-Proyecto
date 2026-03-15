import javax.swing.SwingUtilities;

public class FidecomproApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BaseDatos.cargarDatosIniciales();
                new VentanaLogin();
            }
        });
    }
}
