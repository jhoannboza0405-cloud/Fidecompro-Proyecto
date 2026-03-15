import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PanelClientes extends JPanel {
    private Usuario usuarioActual;
    private DefaultListModel<Cliente> modeloClientes;
    private JList<Cliente> listaClientes;

    private JTextField txtNombre;
    private JTextField txtId;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtDireccion;

    public PanelClientes(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout());

        modeloClientes = new DefaultListModel<Cliente>();
        listaClientes = new JList<Cliente>(modeloClientes);
        refrescarLista();

        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panelForm.setBorder(new TitledBorder("Datos del cliente"));

        txtNombre = new JTextField();
        txtId = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtDireccion = new JTextField();

        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Identificación:"));
        panelForm.add(txtId);
        panelForm.add(new JLabel("Teléfono:"));
        panelForm.add(txtTelefono);
        panelForm.add(new JLabel("Correo:"));
        panelForm.add(txtCorreo);
        panelForm.add(new JLabel("Dirección:"));
        panelForm.add(txtDireccion);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnDesactivar = new JButton("Desactivar");

        panelForm.add(btnRegistrar);
        panelForm.add(btnActualizar);

        add(panelForm, BorderLayout.NORTH);
        add(new JScrollPane(listaClientes), BorderLayout.CENTER);
        add(btnDesactivar, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrarCliente());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnDesactivar.addActionListener(e -> desactivarCliente());

        listaClientes.addListSelectionListener(e -> cargarSeleccionado()); }

    private void registrarCliente() {
        try {
            validarPermisoAdmin();

            if (txtNombre.getText().trim().isEmpty() || txtId.getText().trim().isEmpty()) {
                throw new DatosInvalidosException("Nombre e identificación son obligatorios.");
            }

            Cliente c = new Cliente(
                    BaseDatos.secCliente++,
                    txtNombre.getText(),
                    txtId.getText(),
                    txtTelefono.getText(),
                    txtCorreo.getText(),
                    txtDireccion.getText()
            );

            BaseDatos.clientes.add(c);
            refrescarLista();
            limpiar();
            JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void actualizarCliente() {
        try {
            validarPermisoAdmin();

            Cliente c = listaClientes.getSelectedValue();
            if (c == null) {
                throw new DatosInvalidosException("Seleccione un cliente.");
            }

            c.actualizarDatos(txtTelefono.getText(), txtCorreo.getText(), txtDireccion.getText());
            refrescarLista();
            JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void desactivarCliente() {
        try {
            validarPermisoAdmin();

            Cliente c = listaClientes.getSelectedValue();
            if (c == null) {
                throw new DatosInvalidosException("Seleccione un cliente.");
            }

            c.desactivar();
            refrescarLista();
            JOptionPane.showMessageDialog(this, "Cliente desactivado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void cargarSeleccionado() {
        Cliente c = listaClientes.getSelectedValue();

        if (c != null) {
            txtNombre.setText(c.getNombre());
            txtId.setText(c.getIdentificacion());
            txtTelefono.setText(c.getTelefono());
            txtCorreo.setText(c.getCorreo());
            txtDireccion.setText(c.getDireccion());
        }
    }

    private void refrescarLista() {
        modeloClientes.clear();

        for (int i = 0; i < BaseDatos.clientes.size(); i++) {
            modeloClientes.addElement(BaseDatos.clientes.get(i));
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtId.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText(""); }

    private void validarPermisoAdmin() throws DatosInvalidosException {
        if (!usuarioActual.tienePermiso("ADMIN_CLIENTES")) {
            throw new DatosInvalidosException("No tiene permisos para gestionar clientes.");
        }
    }
}
