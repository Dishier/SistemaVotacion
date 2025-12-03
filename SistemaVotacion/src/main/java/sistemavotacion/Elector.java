package sistemavotacion;

public class Elector extends Persona{
    private boolean haVotado;

    public Elector() {
    }

    public Elector(String nombre, Fecha nacimiento, String correo) {
        super(nombre, null, nacimiento, correo);
        this.haVotado = false;
        generarUsuario();
        generarContrasena();
    }

    public boolean getHaVotado() {
        return haVotado;
    }

    public void setHaVotado(boolean haVotado) {
        this.haVotado = haVotado;
    }
    
    public void generarUsuario(){
        String[] user = this.getCorreo().split("@");
        super.setUsuario(user[0]);
    }
    
    public void generarContrasena(){
        String[] nombres = this.nombreCompleto.trim().split(" ");
        int totalPartes = nombres.length;
        
        if(totalPartes < 3){
            this.contrasena = "ERROR_NOMBRE_COMPLETO";
            return;
        }
        
        String primerNombre = nombres[0];
        String apellidoP = nombres[totalPartes - 2];
        String apellidoM = nombres[totalPartes - 1];
        
        char letraN = primerNombre.charAt(0);
        char letraP = apellidoP.charAt(0);
        char letraM = apellidoM.charAt(0);
        
        String dia = this.fechaNacimiento.getDiaFormato();
        String mes = this.fechaNacimiento.getMesFormato();
        String anio = this.fechaNacimiento.getAnioFormato();
        
        this.contrasena = "" + letraN + dia + letraP + mes + letraM + anio;
        this.contrasena = this.contrasena.toUpperCase();
    }
    
    @Override
    public boolean autenticar(String user, String contra){
        return this.usuario.equals(user) && this.contrasena.equals(contra);
    }

    @Override
    public String toString() {
        return "Nombre: " + super.getNombre() +"\nDireccion de correo electronico: " + super.getCorreo() + "\nFecha de Nacimiento: " + super.getNacimiento();
    }
}
