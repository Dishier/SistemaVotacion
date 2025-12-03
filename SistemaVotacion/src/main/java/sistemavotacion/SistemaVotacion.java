package sistemavotacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SistemaVotacion {
    private Admin admin;
    
    private ArrayList<Elector> electores;
    private ArrayList<Candidato> candidatos;
    
    private boolean votacionAbierta;
    private int votosNulos;
    private int totalVotosEmitidos;
    
    public SistemaVotacion(){
        this.admin = new Admin();
        this.electores = new ArrayList<>();
        this.candidatos = new ArrayList<>();
        this.votacionAbierta = false;
        this.votosNulos = 0;
        this.totalVotosEmitidos = 0;
    }
    
    public Admin getAdmin(){
        return admin;
    }
    
    public ArrayList<Elector> getElectores(){
        return electores;
    }
    
    public ArrayList<Candidato> getCandidatos(){
        return candidatos;
    }
    
    public void iniciarVotacion(){
        this.votacionAbierta = true;
    }
    
    public void finalizarVotacion(){
        this.votacionAbierta = false;
    }
    
    public Elector buscarElector(String user, String contra) throws CredencialesInvalidasException {
    for (Elector e : this.electores) {
        if (e.autenticar(user, contra)) {
            return e;
        }
    }
    throw new CredencialesInvalidasException("Usuario o contrasena incorrectos."); 
}
    
    public String cargarCandidatos(String archivo){
        if (this.candidatos == null){
            this.candidatos = new ArrayList<>();
        } else {
            this.candidatos.clear();
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String lineaN;
            int contador = 0;
            
            while((lineaN = br.readLine()) != null){
                String lineaP = br.readLine();
                if (lineaP == null) break;
                
                Candidato c = new Candidato(lineaP, lineaN);
                this.candidatos.add(c);
                contador++;
                }
            return "Se cargaron " + contador + " candidatos correctamente.";
            }catch (IOException e){
                    return "Error al leer archivo: " + e.getMessage();
        }
    }
    
    public String cargarElectores(String archivo){
        if (this.electores == null) {
            this.electores = new ArrayList<>();
        } else {
            this.electores.clear();
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String n;
            int cont = 0;
            
            while ((n = br.readLine()) != null) {
                String correo = br.readLine();
                String fecha = br.readLine();
                
                if (correo == null || fecha == null) break;
                
                String[] partesFecha = fecha.split("/");
                int d = Integer.parseInt(partesFecha[0].trim());
                int m = Integer.parseInt(partesFecha[1].trim());
                int a = Integer.parseInt(partesFecha[2].trim());
                Fecha fechaN = new Fecha(d,m,a);
                
                Elector e = new Elector(n, fechaN ,correo);
                
                this.electores.add(e);
                cont++;
            }
            return "Se cargaron " + cont + " electores exitosamente.";
        } catch (IOException e){
            return "Error leyendo archivo: " + e.getMessage();
        } catch (NumberFormatException e){
            return "Error de formato en la fecha.";
        }
    }
    
    public void registrarVotos(Elector e, Candidato c) throws VotoInvalidoException{
        if (!this.votacionAbierta) {
            throw new VotoInvalidoException("La votacion esta CERRADA.");
        }
        if (e.getHaVotado()) {
            throw new VotoInvalidoException("El elector " + e.getNombre() + " ya ha votado");
        }
        
        c.incrementarVoto();
        e.setHaVotado(true);
        this.totalVotosEmitidos++;
    }
    
    public void registrarVotoNulo(Elector e) throws VotoInvalidoException{
        if (!this.votacionAbierta) {
            throw new VotoInvalidoException("La votacion esta CERRADA.");
        }
        if (e.getHaVotado()) {
            throw new VotoInvalidoException("El elector " + e.getNombre() + " ya ha votado.");
        }
        
        this.votosNulos++;
        e.setHaVotado(true);
        this.totalVotosEmitidos++;
    }
    
    public String imprimirResultados(){
        if (this.votacionAbierta) {
            return "ERROR: La votacion se encuentra abierta. Necesita cerrarla para ver los resultados.";
        }

        if (this.totalVotosEmitidos == 0) {
            return "No hay votos registrados";
        }
        
        Collections.sort(this.candidatos);
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n--- RESULTADOS DE LA VOTACIÓN ---\n");
        sb.append(String.format("%-20s %-20s %-10s %-10s%n", "Candidato", "Partido", "Votos", "%"));
        sb.append("----------------------------------------------------------------\n");
        
        for(Candidato c : this.candidatos){
            double porcentaje = (c.getVotosRecibidos() * 100.0) / this.totalVotosEmitidos;
            
            sb.append(String.format("%-20s %-20s %-10d %.2f%%%n", 
                    c.getNombreCompleto(), c.getPartido(), c.getVotosRecibidos(), porcentaje));
        }
        
        double porcNulos = (this.votosNulos * 100.0) / this.totalVotosEmitidos;
        sb.append("----------------------------------------------------------------\n");
        sb.append(String.format("%-41s %-10d %.2f%%%n", "VOTOS NULOS", this.votosNulos, porcNulos));
        sb.append("----------------------------------------------------------------\n");
        sb.append("TOTAL VOTOS: ").append(this.totalVotosEmitidos).append("\n");
        
        sb.append("\nGANADOR: ").append(this.candidatos.get(0).getNombreCompleto());
        
        return sb.toString();
        }
    
}
