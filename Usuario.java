public class Usuario extends Persona {
    private int idUsuario;
    private String usuario;
    private String contrasena;
    private String rol;

    public Usuario(int idUsuario, String usuario, String contrasena, String nombre, String rol) {
        super(nombre);
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public boolean validarCredenciales(String user, String pass) {
        return usuario.equals(user) && contrasena.equals(pass);
    }

    public void cambiarContrasena(String actual, String nueva) throws DatosInvalidosException {
        if (!contrasena.equals(actual)) {
            throw new DatosInvalidosException("La contraseña actual no coincide.");
        }

        if (nueva == null || nueva.trim().isEmpty()) {
            throw new DatosInvalidosException("La nueva contraseña no puede estar vacía.");
        }

        contrasena = nueva;
    }

    public boolean tienePermiso(String accion) {
        if (rol.equalsIgnoreCase("admin")) {
            return true;
        }

        if (rol.equalsIgnoreCase("cajero")) {
            return accion.equals("FACTURAR") || accion.equals("VER_CLIENTES");
        }

        return false;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getRol() {
        return rol;
    }

    @Override
    public String getResumen() {
        return "Usuario: " + nombre + " | Rol: " + rol;
    }
}
