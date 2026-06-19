package ao.clinica.medica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // CORREÇÃO: Caminho atualizado para refletir a nova estrutura de pacotes ao.clinica.medica
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ao/clinica/medica/view/login.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("ClinicaMed - Autenticação");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false); // Mantém a proporção do Login Pane intacta
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}