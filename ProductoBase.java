public abstract class ProductoBase {
    protected int idProducto;
    protected String codigo;
    protected String nombre;
    protected String tipo;
    protected double precioUnitario;
    protected int inventario;
    protected int minimoInventario;
    protected boolean activo;

    public ProductoBase(int idProducto, String codigo, String nombre, String tipo, double precioUnitario, int inventario, int minimoInventario) {
        this.idProducto = idProducto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precioUnitario = precioUnitario;
        this.inventario = inventario;
        this.minimoInventario = minimoInventario;
        this.activo = true;
    }

    public abstract String descripcionTipo();

    public void aumentarInventario(int cantidad) throws DatosInvalidosException {
        if (cantidad <= 0) {
            throw new DatosInvalidosException("La cantidad debe ser mayor a 0.");
        }
        inventario += cantidad;
    }

    public void disminuirInventario(int cantidad) throws DatosInvalidosException, InventarioInsuficienteException {
        if (cantidad <= 0) {
            throw new DatosInvalidosException("La cantidad debe ser mayor a 0.");
        }

        if (cantidad > inventario) {
            throw new InventarioInsuficienteException("No hay inventario suficiente.");
        }

        inventario -= cantidad;
    }

    public void actualizarPrecio(double nuevoPrecio) throws DatosInvalidosException {
        if (nuevoPrecio < 0) {
            throw new DatosInvalidosException("El precio no puede ser negativo.");
        }
        precioUnitario = nuevoPrecio;
    }

    public boolean validacionInventario(int cantidad) {
        return inventario >= cantidad;
    }

    public boolean alertaStock() {
        return inventario < minimoInventario;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getInventario() {
        return inventario;
    }

    public int getMinimoInventario() {
        return minimoInventario;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " | ₡" + precioUnitario + " | Stock: " + inventario;
    }
}
