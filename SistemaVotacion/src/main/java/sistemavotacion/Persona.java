package sistemavotacion;

public abstract class Persona {
    protected String nombreCompleto;
    protected String usuario;
    protected String contrasena;
    protected Fecha fechaNacimiento;
    private String correo;

    public Persona() {
    }

    public Persona(String nombre, String usuario, Fecha nacimiento, String correo) {
        this.nombreCompleto = nombre;
        this.usuario = usuario;
        this.fechaNacimiento = nacimiento;
        this.correo = correo;
    }
    
    public String getCorreo(){
        return correo;
    }
    
    public void setCorreo(String correo){
        this.correo = correo;
    }
    
    public String getNombre() {
        return nombreCompleto;
    }

    public void setNombre(String nombre) {
        this.nombreCompleto = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Fecha getNacimiento() {
        return fechaNacimiento;
    }

    public void setNacimiento(int d, int m, int a) {
        fechaNacimiento = new Fecha(d,m,a);
    }
    
    public abstract boolean autenticar(String user, String contra);
}
