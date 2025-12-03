package sistemavotacion;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;
import javafx.scene.control.Alert.AlertType;

public class GUI extends Application {
    private SistemaVotacion sistema;
    private Stage escenarioPrincipal;
    
    @Override
    public void start(Stage stage) {
        this.sistema = new SistemaVotacion();
        this.escenarioPrincipal = stage;
        
        stage.setTitle("Sistema de Votacion Electronica");
        
        mostrarPantallaLogin();
        
        stage.show();
    }
    
    private void mostrarPantallaLogin() {
        Label lbTitulo = new Label("Bienvenido al Sistema de Votacion");
        lbTitulo.setStyle("-fx-font-size: 18 px; -fx-font-weight: bold;");
        
        TextField txtUser = new TextField();
        txtUser.setPromptText("Usuario");
        
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Contrasena");
        
        Button btnIngresar = new Button("Iniciar Sesion");
        Label lbMensaje = new Label();
        
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(lbTitulo, txtUser, txtPass, btnIngresar, lbMensaje);
        
        btnIngresar.setOnAction(e -> {
            String user = txtUser.getText();
            String pass = txtPass.getText();
            
            if (sistema.getAdmin().autenticar(user, pass)) {
                lbMensaje.setText("Bienvenido Admin");
                mostrarPanelAdmin();
            }
            
            else{
                try{
                    Elector el = sistema.buscarElector(user, pass);
                    lbMensaje.setText("Bienvenido " + el.getNombre());
                    mostrarPanelElector(el);
                } catch (CredencialesInvalidasException ex) {
                    lbMensaje.setText("Error: " + ex.getMessage());
                    lbMensaje.setStyle("-fx-text-fill: red;");
                }
            }
        });
        
        Scene scene = new Scene(layout, 400, 300);
        escenarioPrincipal.setScene(scene);
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void mostrarPanelAdmin(){
        Label lbTitulo = new Label("Panel de Administrador");
        lbTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Button btnCargarCandidatos = new Button("Cargar Candidatos");
        Button btnCargarElectores = new Button("Cargar Electores");
        
        btnCargarCandidatos.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar archivo de Candidatos");
            File file = fileChooser.showOpenDialog(escenarioPrincipal);
            if (file != null) {
                String resultado = sistema.cargarCandidatos(file.getAbsolutePath());
                mostrarAlerta("Carga de Candidatos", resultado);
            }
        });
        
        btnCargarElectores.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar archivo de Electores");
            File file = fileChooser.showOpenDialog(escenarioPrincipal);
            if (file != null) {
                String resultado = sistema.cargarElectores(file.getAbsolutePath());
                mostrarAlerta("Carga de Electores", resultado);
            }
        });
        
        Button btnAbrir = new Button("Abrir Votacion");
        Button btnCerrar = new Button("Cerrar Votacion");
        Label lbEstado = new Label("Estado: Desconocido");
        
        btnAbrir.setOnAction(e -> {
            sistema.iniciarVotacion();
            lbEstado.setText("Estado: ABIERTA");
            mostrarAlerta("Votacion", "La votacion se ha abierto correctamente!");
        });
        
        btnCerrar.setOnAction(e -> {
            sistema.finalizarVotacion();
            lbEstado.setText("Estado: CERRADA");
            mostrarAlerta("Votacion", "La votacion se ha cerrado");
        });
        
        Button btnResultados = new Button("Ver Resultados Finales");
        btnResultados.setOnAction(e -> {
            String res = sistema.imprimirResultados();
            TextArea textArea = new TextArea(res);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Resultados");
            alert.setHeaderText("Resultados de la Votacion");
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();
        });
        
        Button btnVerListas = new Button("Ver Listas");
        btnVerListas.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("-- CANDIDATOS --\n");
            for(Candidato c : sistema.getCandidatos()){
                sb.append(c.getNombreCompleto()).append(" - ").append(c.getPartido()).append("\n");
            }
            sb.append("\n--- ELECTORES ---\n");
            for(Elector el : sistema.getElectores()) {
                sb.append(el.getNombre()).append(" (Voto: ").append(el.getHaVotado()).append(")\n");
            }
            
            TextArea textArea = new TextArea(sb.toString());
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Listas del Sistema");
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();
        });
        
        Button btnLogout = new Button("Cerrar Sesion");
        btnLogout.setStyle("-fx-background-color: #ffcccc;");
        btnLogout.setOnAction(e -> mostrarPantallaLogin());
        
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                lbTitulo,
                new Separator(),
                btnCargarCandidatos, btnCargarElectores, 
                new Separator(), 
                lbEstado, btnAbrir, btnCerrar, 
                new Separator(), 
                btnResultados, btnVerListas, 
                new Separator(), 
                btnLogout
        );
        
        escenarioPrincipal.setScene(new Scene(layout, 400, 550));
        
    }
    
    private void mostrarPanelElector(Elector e) {
        Label lbTitulo = new Label("Boleta Electoral");
        lbTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label lbUsuario = new Label("Elector: " + e.getNombre());
        
        VBox opLayout = new VBox(10);
        opLayout.setAlignment(Pos.CENTER_LEFT);
        opLayout.setPadding(new Insets(10));
        
        ToggleGroup grupoVoto = new ToggleGroup();
        List<Candidato> listaCandidatos = sistema.getCandidatos();
        
        if (listaCandidatos.isEmpty()) {
            opLayout.getChildren().add(new Label("No hay candidatos disponibles"));
        } else { 
            for (Candidato c : listaCandidatos) {
                RadioButton rb = new RadioButton(c.getNombreCompleto() + " (" + c.getPartido() + ")");
                rb.setUserData(c);
                rb.setToggleGroup(grupoVoto);
                opLayout.getChildren().add(rb);
            }
            
            RadioButton rbNulo = new RadioButton("ANULAR VOTO");
            rbNulo.setUserData(null);
            rbNulo.setToggleGroup(grupoVoto);
            opLayout.getChildren().add(rbNulo);
        }
        
        Button btnVotar = new Button("Emitir Voto");
        btnVotar.setStyle("-fx-font-size: 14px; -fx-background-color: #ccffcc;");
        
        btnVotar.setOnAction(ev -> {
            if (grupoVoto.getSelectedToggle() == null) {
                mostrarAlerta("Atencion", "Por favor seleccione una opcion.");
                return;
            }
            
            Object seleccion = grupoVoto.getSelectedToggle().getUserData();
            
            try{
                if (seleccion == null) {
                    sistema.registrarVotoNulo(e);
                    mostrarAlerta("Exito", "Su voto ha sido anulado correctamente");
                } else {
                    Candidato candidatoElegido = (Candidato) seleccion;
                    sistema.registrarVotos(e, candidatoElegido);
                    mostrarAlerta("Exito", "Voto registrado para: " + candidatoElegido.getNombreCompleto() + "!");
                }
                
                mostrarPantallaLogin();
            } catch (VotoInvalidoException ex) {
                mostrarAlerta("Error al votar", ex.getMessage());
            }
        });
        
        Button btnLogout = new Button("Salir sin votar");
        btnLogout.setOnAction(el -> mostrarPantallaLogin());
        
        VBox layout = new VBox(15, lbTitulo, lbUsuario, new Separator(), opLayout, new Separator(), btnVotar, btnLogout);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));
        
        escenarioPrincipal.setScene(new Scene(layout, 450, 500));
    }
    
    public static void main(String[] args) {
        launch();
    }
}
