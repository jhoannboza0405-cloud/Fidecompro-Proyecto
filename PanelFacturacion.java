import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PanelFacturacion extends JPanel {
    private Usuario usuarioActual;

    private JComboBox<Cliente> comboClientes;
    private JComboBox<ProductoBase> comboProductos;
    private JTextField txtCantidad;
    private JTextArea areaDetalle;

    private Factura facturaActual;

    public PanelFacturacion(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout());

        JPanel superior = new JPanel(new GridLayout(5, 2, 5, 5));
        superior.setBorder(new TitledBorder("Nueva factura"));

        comboClientes = new JComboBox<Cliente>();
        comboProductos = new JComboBox<ProductoBase>();
        txtCantidad = new JTextField();

        cargarClientes();
        cargarProductos();

        superior.add(new JLabel("Cliente:"));
        superior.add(comboClientes);
        superior.add(new JLabel("Producto:"));
        superior.add(comboProductos);
        superior.add(new JLabel("Cantidad:"));
        superior.add(txtCantidad);

        JButton btnCrear = new JButton("Crear factura");
        JButton btnAgregar = new JButton("Agregar producto");
        JButton btnActualizar = new JButton("Actualizar listas");
        btnActualizar.addActionListener(e -> actualizarCombos());


        superior.add(btnCrear);
        superior.add(btnAgregar);
        superior.add(btnActualizar);


        areaDetalle = new JTextArea();
        areaDetalle.setEditable(false);

        JPanel inferior = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton btnEmitir = new JButton("Emitir factura");
        JButton btnArchivo = new JButton("Generar archivo");


        inferior.add(btnEmitir);
        inferior.add(btnArchivo);

        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(areaDetalle), BorderLayout.CENTER);
        add(inferior, BorderLayout.SOUTH);

        btnCrear.addActionListener(e -> crearFactura());
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEmitir.addActionListener(e -> emitirFactura());
        btnArchivo.addActionListener(e -> generarArchivo());
 }

    private void actualizarCombos() {
    cargarClientes();
    cargarProductos();
}

    private void crearFactura() {
             try {
            validarPermisoFacturar();

            Cliente cliente = (Cliente) comboClientes.getSelectedItem();

            if (cliente == null || !cliente.isActivo()) {
                throw new DatosInvalidosException("Debe seleccionar un cliente activo.");
            }

            String numero = "FAC-" + BaseDatos.secFactura;
            facturaActual = new Factura(BaseDatos.secFactura++, numero, "13/03/2026", cliente);

            areaDetalle.setText("Factura creada: " + numero + "\nCliente: " + cliente.getNombre() + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void agregarProducto() {
        try {
            validarPermisoFacturar();

            if (facturaActual == null) {
                throw new DatosInvalidosException("Primero debe crear una factura.");
            }

            ProductoBase producto = (ProductoBase) comboProductos.getSelectedItem();
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            facturaActual.agregarLinea(producto, cantidad);
            mostrarDetalle();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void emitirFactura() {
        try {
            validarPermisoFacturar();

            if (facturaActual == null) {
                throw new DatosInvalidosException("No hay factura creada.");
            }

            facturaActual.emitir();
            BaseDatos.facturas.add(facturaActual);
            mostrarDetalle();
            cargarProductos();

            JOptionPane.showMessageDialog(this, "Factura emitida correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void generarArchivo() {
        try {
            if (facturaActual == null) {
                throw new DatosInvalidosException("No hay factura actual.");
            }

            areaDetalle.setText(facturaActual.generarArchivoTexto());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void mostrarDetalle() {
        String texto = "Factura: " + facturaActual.getNumeroFactura() + "\n";
        texto += "Cliente: " + facturaActual.getCliente().getNombre() + "\n";
        texto += "Estado: " + facturaActual.getEstado() + "\n";
        texto += "----------------------\n";

        for (int i = 0; i < facturaActual.getDetalles().size(); i++) {
            texto += facturaActual.getDetalles().get(i).toString() + "\n";
        }

        texto += "----------------------\n";
        texto += "Total: ₡" + facturaActual.getTotal() + "\n";

        areaDetalle.setText(texto);
    }

    private void cargarClientes() {
        comboClientes.removeAllItems();

        for (int i = 0; i < BaseDatos.clientes.size(); i++) {
            if (BaseDatos.clientes.get(i).isActivo()) {
                comboClientes.addItem(BaseDatos.clientes.get(i));
            }
        }
    }

    private void cargarProductos() {
        comboProductos.removeAllItems();

        for (int i = 0; i < BaseDatos.productos.size(); i++) {
            comboProductos.addItem(BaseDatos.productos.get(i));
        }
    }

    private void validarPermisoFacturar() throws DatosInvalidosException {
        if (!usuarioActual.tienePermiso("FACTURAR")) {
            throw new DatosInvalidosException("No tiene permisos para facturar.");
        }
    }
}
