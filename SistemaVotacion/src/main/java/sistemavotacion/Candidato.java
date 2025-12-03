package sistemavotacion;

public class Candidato implements Comparable<Candidato>{
    private String partido;
    private String nombreCompleto;
    private int votosRecibidos;

    public Candidato(String partido, String nombreCompleto) {
        this.partido = partido;
        this.nombreCompleto = nombreCompleto;
        this.votosRecibidos = 0;
    }

    public String getPartido() {
        return partido;
    }

    public void setPartido(String partido) {
        this.partido = partido;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getVotosRecibidos() {
        return votosRecibidos;
    }

    public void setVotosRecibidos(int votosRecibidos) {
        this.votosRecibidos = votosRecibidos;
    }
    
    public void incrementarVoto(){
        votosRecibidos ++;
    }
    
    @Override
    public int compareTo(Candidato otroCandidato){
        return Integer.compare(otroCandidato.getVotosRecibidos(), this.votosRecibidos);
    }

    @Override
    public String toString() {
        return "Candidato: " + nombreCompleto + "\nPartido: " + partido + "\nVotos Recibidos: " + votosRecibidos;
    }
}
