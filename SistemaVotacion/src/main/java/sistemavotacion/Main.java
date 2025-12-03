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
                    System.out.println("✅ Acceso Concedido: ADMINISTRADOR");
                    menuAdmin(sistema, sc);
                } 
                // B) Verificar Elector
                else {
                    Elector elector = sistema.buscarElector(user, pass);
                    if (elector != null) {
                        System.out.println("✅ Acceso Concedido: " + elector.getNombre());
                        menuElector(sistema, sc, elector);
                    } else {
                        System.out.println("❌ Credenciales incorrectas. (Si es elector, verifique si el padrón ya fue cargado).");
                    }
                }

            } else if (opcion.equals("2")) {
                salir = true;
            } else {
                System.out.println("Opción no válida.");
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
            System.out.println("3. Abrir Votación");
            System.out.println("4. Cerrar Votación");
            System.out.println("5. Ver Resultados");
            System.out.println("6. Ver Lista Electores (Debug)");
            System.out.println("7. Cerrar Sesión");
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
                    System.out.println("🔔 Votación ABIERTA.");
                    break;
                case "4":
                    sistema.finalizarVotacion();
                    System.out.println("🔒 Votación CERRADA.");
                    break;
                case "5":
                    // Imprimimos el reporte que genera tu método con StringBuilder
                    System.out.println(sistema.imprimirResultados());
                    break;
                case "6":
                    System.out.println("--- LISTA DE ELECTORES Y CREDENCIALES ---");
                    for(Elector e : sistema.getElectores()){
                        System.out.println("User: " + e.getUsuario() + " | Pass: " + e.contrasena + " | Votó: " + e.getHaVotado());
                    }
                    break;
                case "7":
                    regresar = true;
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        }
    }

    // ---------------------------------------------------
    //               MENÚ ELECTOR
    // ---------------------------------------------------
    public static void menuElector(SistemaVotacion sistema, Scanner sc, Elector elector) {
        boolean regresar = false;
        while (!regresar) {
            System.out.println("\n[ PANEL ELECTOR ]");
            System.out.println("1. Emitir Voto");
            System.out.println("2. Cerrar Sesión");
            System.out.print("Opción: ");

            String op = sc.nextLine();

            if (op.equals("1")) {
                if (elector.getHaVotado()) {
                    System.out.println("⚠️ Usted ya votó.");
                } else {
                    realizarVoto(sistema, sc, elector);
                    regresar = true; // Sacar al usuario después de votar
                }
            } else if (op.equals("2")) {
                regresar = true;
            }
        }
    }

    // Lógica auxiliar para mostrar candidatos y capturar la elección
    private static void realizarVoto(SistemaVotacion sistema, Scanner sc, Elector elector) {
        ArrayList<Candidato> lista = sistema.getCandidatos();
        
        if (lista.isEmpty()) {
            System.out.println("❌ Error: No hay candidatos cargados en el sistema.");
            return;
        }

        System.out.println("\n--- BOLETA ELECTORAL ---");
        int i = 1;
        for (Candidato c : lista) {
            System.out.println(i + ". " + c.getNombreCompleto() + " (" + c.getPartido() + ")");
            i++;
        }
        System.out.println(i + ". ANULAR VOTO");

        System.out.print("Seleccione su opción: ");
        try {
            int seleccion = Integer.parseInt(sc.nextLine());

            // Opción: Votar por Candidato
            if (seleccion >= 1 && seleccion <= lista.size()) {
                Candidato elegido = lista.get(seleccion - 1);
                System.out.println("¿Confirma voto por " + elegido.getNombreCompleto() + "? (S/N)");
                if (sc.nextLine().equalsIgnoreCase("S")) {
                    
                    // LLAMADA A TU MÉTODO QUE LANZA EXCEPCIÓN
                    try {
                        sistema.registrarVotos(elector, elegido); 
                        System.out.println("✅ ¡Voto registrado con éxito!");
                    } catch (VotoInvalidoException e) {
                        System.out.println("❌ Error al votar: " + e.getMessage());
                    }
                }
            } 
            // Opción: Anular Voto
            else if (seleccion == lista.size() + 1) {
                System.out.println("¿Confirma ANULAR su voto? (S/N)");
                if (sc.nextLine().equalsIgnoreCase("S")) {
                    
                    // LLAMADA A TU MÉTODO DE VOTO NULO
                    try {
                        sistema.registrarVotoNulo(elector);
                        System.out.println("⚠️ Voto anulado registrado.");
                    } catch (VotoInvalidoException e) {
                        System.out.println("❌ Error al votar: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Opción inválida.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese solo números.");
        }
    }
}