import java.util.ArrayList;

public class BaseDatos {
    public static ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
    public static ArrayList<Cliente> clientes = new ArrayList<Cliente>();
    public static ArrayList<ProductoBase> productos = new ArrayList<ProductoBase>();
    public static ArrayList<Factura> facturas = new ArrayList<Factura>();

    public static int secCliente = 1;
    public static int secProducto = 1;
    public static int secFactura = 1;

    public static void cargarDatosIniciales() {
        usuarios.add(new Usuario(1, "admin", "123", "Administrador", "admin"));
        usuarios.add(new Usuario(2, "cajero", "123", "Cajero General", "cajero"));

        clientes.add(new Cliente(secCliente++, "Universidad Fidelitas", "101010101010", "8888-8888", "Ufide@correo.com", "San José"));

        productos.add(new ProductoFisico(secProducto++, "P001", "Arroz", "Abarrotes", 1500, 20, 5));
        productos.add(new ProductoFisico(secProducto++, "P002", "Frijoles", "Abarrotes", 1800, 15, 5));
        productos.add(new ProductoFisico(secProducto++, "P003", "Leche", "Lácteos", 1200, 10, 3));
        productos.add(new ProductoFisico(secProducto++, "P004", "Atún", "Abarrotes", 1000, 25, 3));
        productos.add(new ProductoFisico(secProducto++, "P005", "Crema corporal", "Cuidado personal", 5000, 9, 3));
        productos.add(new ProductoFisico(secProducto++, "P006", "Espejo", "Hogar", 15000, 5, 2)); }

    public static Usuario login(String user, String pass) throws AutenticacionException {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).validarCredenciales(user, pass)) {
                return usuarios.get(i); } }

        throw new AutenticacionException("Usuario o contraseña incorrectos.");}
}
