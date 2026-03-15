import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PanelProductos extends JPanel {
    private Usuario usuarioActual;
    private DefaultListModel<ProductoBase> modeloProductos;
    private JList<ProductoBase> listaProductos;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtTipo;
    private JTextField txtPrecio;
    private JTextField txtInventario;
    private JTextField txtMinimo;
    private JTextField txtAjuste;

    public PanelProductos(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout());

        modeloProductos = new DefaultListModel<ProductoBase>();
        listaProductos = new JList<ProductoBase>(modeloProductos);
        refrescarLista();

        JPanel form = new JPanel(new GridLayout(7, 2, 5, 5));
        form.setBorder(new TitledBorder("Datos del producto"));

        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtTipo = new JTextField();
        txtPrecio = new JTextField();
        txtInventario = new JTextField();
        txtMinimo = new JTextField();
        txtAjuste = new JTextField();

        form.add(new JLabel("Código:"));
        form.add(txtCodigo);
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Tipo:"));
        form.add(txtTipo);
        form.add(new JLabel("Precio:"));
        form.add(txtPrecio);
        form.add(new JLabel("Inventario:"));
        form.add(txtInventario);
        form.add(new JLabel("Mínimo inventario:"));
        form.add(txtMinimo);
        form.add(new JLabel("Cantidad ajuste:"));
        form.add(txtAjuste);

        JPanel botones = new JPanel(new GridLayout(1, 4, 5, 5));
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnPrecio = new JButton("Modificar precio");
        JButton btnSumar = new JButton("Reponer");
        JButton btnRestar = new JButton("Disminuir");

        botones.add(btnRegistrar);
        botones.add(btnPrecio);
        botones.add(btnSumar);
        botones.add(btnRestar);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(listaProductos), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrarProducto());
        btnPrecio.addActionListener(e -> modificarPrecio());
        btnSumar.addActionListener(e -> reponer());
        btnRestar.addActionListener(e -> disminuir());

        listaProductos.addListSelectionListener(e -> cargarSeleccionado());
    }

    private void registrarProducto() {
        try {
            validarPermisoAdmin();

            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            String tipo = txtTipo.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int inv = Integer.parseInt(txtInventario.getText().trim());
            int min = Integer.parseInt(txtMinimo.getText().trim());

            if (codigo.isEmpty() || nombre.isEmpty()) {
                throw new DatosInvalidosException("Código y nombre son obligatorios.");
            }

            if (precio < 0) {
                throw new DatosInvalidosException("El precio no puede ser menor a 0.");
            }

            if (inv < 0) {
                throw new DatosInvalidosException("El inventario inicial no puede ser negativo.");
            }

            ProductoBase p = new ProductoFisico(BaseDatos.secProducto++, codigo, nombre, tipo, precio, inv, min);
            BaseDatos.productos.add(p);

            refrescarLista();
            limpiar();
            JOptionPane.showMessageDialog(this, "Producto registrado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void modificarPrecio() {
        try {
            validarPermisoAdmin();

            ProductoBase p = listaProductos.getSelectedValue();
            if (p == null) {
                throw new DatosInvalidosException("Seleccione un producto.");
            }

            double nuevoPrecio = Double.parseDouble(txtPrecio.getText().trim());
            p.actualizarPrecio(nuevoPrecio);
            refrescarLista();
            JOptionPane.showMessageDialog(this, "Precio actualizado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void reponer() {
        try {
            validarPermisoAdmin();

            ProductoBase p = listaProductos.getSelectedValue();
            if (p == null) {
                throw new DatosInvalidosException("Seleccione un producto.");
            }

            int cant = Integer.parseInt(txtAjuste.getText().trim());
            p.aumentarInventario(cant);
            refrescarLista();
            mostrarAlertaStock(p);
            JOptionPane.showMessageDialog(this, "Inventario aumentado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void disminuir() {
        try {
            validarPermisoAdmin();

            ProductoBase p = listaProductos.getSelectedValue();
            if (p == null) {
                throw new DatosInvalidosException("Seleccione un producto.");
            }

            int cant = Integer.parseInt(txtAjuste.getText().trim());
            p.disminuirInventario(cant);
            refrescarLista();
            mostrarAlertaStock(p);
            JOptionPane.showMessageDialog(this, "Inventario disminuido.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void mostrarAlertaStock(ProductoBase p) {
        if (p.alertaStock()) {
            JOptionPane.showMessageDialog(this, "Alerta: stock bajo de " + p.getNombre());
        }
    }

    private void cargarSeleccionado() {
        ProductoBase p = listaProductos.getSelectedValue();

        if (p != null) {
            txtCodigo.setText(p.getCodigo());
            txtNombre.setText(p.getNombre());
            txtTipo.setText(p.getTipo());
            txtPrecio.setText(String.valueOf(p.getPrecioUnitario()));
            txtInventario.setText(String.valueOf(p.getInventario()));
            txtMinimo.setText(String.valueOf(p.getMinimoInventario()));
        }
    }

    private void refrescarLista() {
        modeloProductos.clear();

        for (int i = 0; i < BaseDatos.productos.size(); i++) {
            modeloProductos.addElement(BaseDatos.productos.get(i));
        }
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtTipo.setText("");
        txtPrecio.setText("");
        txtInventario.setText("");
        txtMinimo.setText("");
        txtAjuste.setText("");
    }

    private void validarPermisoAdmin() throws DatosInvalidosException {
        if (!usuarioActual.tienePermiso("ADMIN_PRODUCTOS")) {
            throw new DatosInvalidosException("No tiene permisos para gestionar productos.");
        }
    }
}
