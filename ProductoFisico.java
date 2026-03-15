public class ProductoFisico extends ProductoBase {

    public ProductoFisico(int idProducto, String codigo, String nombre, String tipo, double precioUnitario, int inventario, int minimoInventario) {
        super(idProducto, codigo, nombre, tipo, precioUnitario, inventario, minimoInventario);
    }

    @Override
    public String descripcionTipo() {
        return "Producto físico para venta";
    }
}
