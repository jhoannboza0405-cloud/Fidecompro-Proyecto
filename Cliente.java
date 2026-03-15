public class Cliente extends Persona {
    private int idCliente;
    private String identificacion;
    private String telefono;
    private String correo;
    private String direccion;
    private boolean activo;

    public Cliente(int idCliente, String nombre, String identificacion, String telefono, String correo, String direccion) {
        super(nombre);
        this.idCliente = idCliente;
        this.identificacion = identificacion;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.activo = true;}

    public void actualizarDatos(String telefono, String correo, String direccion) {
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;}

    public void desactivar() {
        activo = false;}

    public int getIdCliente() {
        return idCliente; }

    public String getIdentificacion() {
        return identificacion; }

    public String getTelefono() {
        return telefono; }

    public String getCorreo() {
        return correo; }

    public String getDireccion() {
        return direccion;  }

    public boolean isActivo() {
        return activo; }

    @Override
    public String getResumen() {
        return "Cliente: " + nombre + " | ID: " + identificacion + " | Tel: " + telefono; }

    @Override
    public String toString() {
        if (activo) {
            return nombre + " - " + identificacion;
        } else {
            return nombre + " - " + identificacion + " (Inactivo)";
        }
    }
}
