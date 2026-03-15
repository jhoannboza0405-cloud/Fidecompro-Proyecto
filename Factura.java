import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class Factura {
    private int idFactura;
    private String numeroFactura;
    private String fecha;
    private double subtotal;
    private double impuesto;
    private double total;
    private String estado;
    private Cliente cliente;
    private ArrayList<DetalleFactura> detalles;

    public Factura(int idFactura, String numeroFactura, String fecha, Cliente cliente) {
        this.idFactura = idFactura;
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.estado = "BORRADOR";
        this.detalles = new ArrayList<DetalleFactura>(); }

    public void agregarLinea(ProductoBase producto, int cantidad) throws DatosInvalidosException, InventarioInsuficienteException {
        if (cantidad <= 0) {
            throw new DatosInvalidosException("La cantidad debe ser mayor a 0.");
        }

         if (!producto.validacionInventario(cantidad)) {
            throw new InventarioInsuficienteException(
            "Inventario insuficiente para: " + producto.getNombre()
            );
            }

        detalles.add(new DetalleFactura(producto, cantidad));
        calcularTotales(); }

    public void calcularTotales() {
        subtotal = 0;

        for (int i = 0; i < detalles.size(); i++) {
            subtotal += detalles.get(i).getSubtotalLinea();
        }

        impuesto = subtotal * 0.13;
        total = subtotal + impuesto;  }

    public void emitir() throws DatosInvalidosException, InventarioInsuficienteException {
        if (detalles.size() == 0) {
            throw new DatosInvalidosException("La factura debe tener al menos un producto.");
        }

        for (int i = 0; i < detalles.size(); i++) {
            DetalleFactura d = detalles.get(i);
            if (!d.getProducto().validacionInventario(d.getCantidad())) {
                throw new InventarioInsuficienteException("Inventario insuficiente para: " + d.getProducto().getNombre());
            }
        }

        for (int i = 0; i < detalles.size(); i++) {
            DetalleFactura d = detalles.get(i);
            d.getProducto().disminuirInventario(d.getCantidad());
        }

        estado = "EMITIDA"; }

    public String generarArchivoTexto() throws DatosInvalidosException, IOException {
        if (!estado.equals("EMITIDA")) {
            throw new DatosInvalidosException("Solo se puede generar archivo si la factura está emitida.");
        }

        String texto = "========== FACTURA ==========\n";
        texto += "Número: " + numeroFactura + "\n";
        texto += "Fecha: " + fecha + "\n";
        texto += "Cliente: " + cliente.getNombre() + "\n";
        texto += "-----------------------------\n";

        for (int i = 0; i < detalles.size(); i++) {
            texto += detalles.get(i).toString() + "\n";
        }

        texto += "-----------------------------\n";
        texto += "Subtotal: ₡" + subtotal + "\n";
        texto += "Impuesto: ₡" + impuesto + "\n";
        texto += "Total: ₡" + total + "\n";
        texto += "Estado: " + estado + "\n";
        texto += "=============================";

        FileWriter writer = new FileWriter("factura_" + numeroFactura + ".txt");
        writer.write(texto);
        writer.close();

        return texto; }

    public int getIdFactura() {
        return idFactura;  }

    public String getNumeroFactura() {
        return numeroFactura; }

    public String getFecha() {
        return fecha; }

    public double getSubtotal() {
        return subtotal; }

    public double getImpuesto() {
        return impuesto; }

    public double getTotal() {
        return total;}

    public String getEstado() {
        return estado; }

    public Cliente getCliente() {
        return cliente; }

    public ArrayList<DetalleFactura> getDetalles() {
        return detalles; }
}
