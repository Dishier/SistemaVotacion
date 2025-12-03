package sistemavotacion;

public class Admin extends Persona {

    public Admin() {
        super("Admin Default", "admin", new Fecha(1, 1, 2000), "admin@voto.com");
        this.contrasena = "12345";
    }
    
    public void generarUsuario(){
        super.setUsuario("Admin");
    }
    
    public void generarContrasena(){
        this.contrasena = "12345";
    }
    
    @Override
    public boolean autenticar(String user, String contra){
        return user.equals(this.usuario) && contra.equals(this.contrasena);
    }
    
}
