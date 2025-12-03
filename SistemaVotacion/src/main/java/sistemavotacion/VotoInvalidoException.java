package sistemavotacion;

public class VotoInvalidoException extends Exception {
    public VotoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
