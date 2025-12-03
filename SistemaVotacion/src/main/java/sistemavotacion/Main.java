package sistemavotacion;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // 1. Instanciar el sistema y scanner
        SistemaVotacion sistema = new SistemaVotacion();
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        // 2. Bucle Principal
        while (!salir) {
            System.out.println("\n========================================");
            System.out.println("   SISTEMA DE VOTACION ELECTRONICA");
            System.out.println("========================================");
            System.out.println("1. Iniciar Sesion");
            System.out.println("2. Salir");
            System.out.print(">> ");

            String opcion = sc.nextLine();

            if (opcion.equals("1")) {
                // --- LOGIN ---
                System.out.print("Usuario: ");
                String user = sc.nextLine();
                System.out.print("Contraseña: ");
                String pass = sc.nextLine();

                // A) Verificar Admin
                if (sistema.getAdmin().autenticar(user, pass)) {
                    System.out.println("Acceso Concedido: ADMINISTRADOR");
                    menuAdmin(sistema, sc);
                } 
                // B) Verificar Elector
                else {
                    try {
                        Elector elector = sistema.buscarElector(user, pass);
                    
                        System.out.println("Acceso concedido: " + elector.getNombre());
                        menuElector(sistema, sc, elector);
                    } catch (CredencialesInvalidasException e) {
                        System.out.println("Error de acceso: " + e.getMessage());
                        System.out.println("  (Verifique sus datos o si el padron ya fue cargado).");
                    }
                }
            } else if (opcion.equals("2")) {
                salir = true;
            } else {
                System.out.println("Opcion no valida.");
            }
        }
        System.out.println("Programa finalizado.");
        sc.close();
    }

    // ---------------------------------------------------
    //               MENÚ ADMINISTRADOR
    // ---------------------------------------------------
    public static void menuAdmin(SistemaVotacion sistema, Scanner sc) {
        boolean regresar = false;
        while (!regresar) {
            System.out.println("\n[ PANEL ADMINISTRADOR ]");
            System.out.println("1. Cargar Candidatos (txt)");
            System.out.println("2. Cargar Electores (txt)");
            System.out.println("3. Abrir Votacion");
            System.out.println("4. Cerrar Votacion");
            System.out.println("5. Ver Resultados");
            System.out.println("6. Ver Lista Electores");
            System.out.println("7. Ver Lista Candidatos");
            System.out.println("8. Cerrar Sesion");
            System.out.print("Opción: ");
            
            String op = sc.nextLine();

            switch (op) {
                case "1":
                    System.out.print("Nombre archivo candidatos (ej. candidatos.txt): ");
                    // Imprimimos el String que retorna tu método
                    System.out.println(sistema.cargarCandidatos(sc.nextLine()));
                    break;
                case "2":
                    System.out.print("Nombre archivo electores (ej. electores.txt): ");
                    // Imprimimos el String que retorna tu método
                    System.out.println(sistema.cargarElectores(sc.nextLine()));
                    break;
                case "3":
                    sistema.iniciarVotacion();
                    System.out.println("Votacion ABIERTA.");
                    break;
                case "4":
                    sistema.finalizarVotacion();
                    System.out.println("Votacion CERRADA.");
                    break;
                case "5":
                    System.out.println(sistema.imprimirResultados());
                    break;
                case "6":
                    System.out.println("--- LISTA DE ELECTORES Y CREDENCIALES ---");
                    for(Elector e : sistema.getElectores()){
                        System.out.println("User: " + e.getUsuario() + " | Pass: " + e.contrasena + " | Votó: " + e.getHaVotado());
                    }
                    break;
                case "7":
                    System.out.println("--- LISTA DE CANDIDATOS ---");
                    for(Candidato c : sistema.getCandidatos()){
                        System.out.println("Nombre: " + c.getNombreCompleto() + " | Partido: " + c.getPartido() + " | Votos Recibidos: " + c.getVotosRecibidos());
                    }
                    break;
                case "8":
                    regresar = true;
                    break;
                default:
                    System.out.println("Opcion incorrecta.");
            }
        }
    }
    
    public static void menuElector(SistemaVotacion sistema, Scanner sc, Elector elector) {
        boolean regresar = false;
        while (!regresar) {
            System.out.println("\n[ PANEL ELECTOR ]");
            System.out.println("1. Emitir Voto");
            System.out.println("2. Cerrar Sesion");
            System.out.print("Opcion: ");

            String op = sc.nextLine();

            if (op.equals("1")) {
                if (elector.getHaVotado()) {
                    System.out.println("Usted ya voto.");
                } else {
                    realizarVoto(sistema, sc, elector);
                    regresar = true;
                }
            } else if (op.equals("2")) {
                regresar = true;
            }
        }
    }

    private static void realizarVoto(SistemaVotacion sistema, Scanner sc, Elector elector) {
        ArrayList<Candidato> lista = sistema.getCandidatos();
        
        if (lista.isEmpty()) {
            System.out.println("Error: No hay candidatos cargados en el sistema.");
            return;
        }

        System.out.println("\n--- BOLETA ELECTORAL ---");
        int i = 1;
        for (Candidato c : lista) {
            System.out.println(i + ". " + c.getNombreCompleto() + " (" + c.getPartido() + ")");
            i++;
        }
        System.out.println(i + ". ANULAR VOTO");

        System.out.print("Seleccione su opcion: ");
        try {
            int seleccion = Integer.parseInt(sc.nextLine());

            // Opción: Votar por Candidato
            if (seleccion >= 1 && seleccion <= lista.size()) {
                Candidato elegido = lista.get(seleccion - 1);
                System.out.println("¿Confirma voto por " + elegido.getNombreCompleto() + "? (S/N)");
                if (sc.nextLine().equalsIgnoreCase("S")) {

                    try {
                        sistema.registrarVotos(elector, elegido); 
                        System.out.println("¡Voto registrado con exito!");
                    } catch (VotoInvalidoException e) {
                        System.out.println("Error al votar: " + e.getMessage());
                    }
                }
            } 
            else if (seleccion == lista.size() + 1) {
                System.out.println("¿Confirma ANULAR su voto? (S/N)");
                if (sc.nextLine().equalsIgnoreCase("S")) {
                    
                    // LLAMADA A TU MÉTODO DE VOTO NULO
                    try {
                        sistema.registrarVotoNulo(elector);
                        System.out.println("Voto anulado registrado.");
                    } catch (VotoInvalidoException e) {
                        System.out.println("Error al votar: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Opcion invalida.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese solo numeros.");
        }
    }
}