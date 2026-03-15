public class DetalleFactura {
    private ProductoBase producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotalLinea;

    public DetalleFactura(ProductoBase producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecioUnitario();
        this.subtotalLinea = calcularSubtotal(); }

    public double calcularSubtotal() {
        return cantidad * precioUnitario; }

    public ProductoBase getProducto() {
        return producto; }

    public int getCantidad() {
        return cantidad; }

    public double getSubtotalLinea() {
        return subtotalLinea;}

    @Override
    public String toString() {
        return producto.getNombre() + " | Cant: " + cantidad + " | Subtotal: ₡" + subtotalLinea; }
}
