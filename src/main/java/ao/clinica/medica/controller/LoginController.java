package ao.clinica.medica.controller;


import ao.clinica.medica.dao.UsuarioDAOImpl;
import ao.clinica.medica.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {


    @FXML
    private HBox rootPane;
    @FXML
    private TextField txtLogin;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblErro;

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    // Atributo estático para manter a sessão do utilizador ativa na aplicação
    private static Usuario usuarioLogado;

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String login = txtLogin.getText().trim();
        String senha = txtSenha.getText().trim();

        // 1. Validação de Defesa de UI (Anti-Empty Pattern)
        if (login.isEmpty() || senha.isEmpty()) {
            lblErro.setText("Por favor, preencha todos os campos.");
            return;
        }

        try {
            // 2. Autenticação na Camada de Persistência (Segura contra SQL Injection)
            Usuario usuario = usuarioDAO.autenticar(login, senha);

            if (usuario != null) {
                usuarioLogado = usuario;
                lblErro.setText(""); // Limpa mensagens de erro anteriores

                // 3. Transição de Tela Dinâmica e Elegante
                carregarDashboard(event);
            } else {
                lblErro.setText("Utilizador ou palavra-passe incorretos.");
            }

        } catch (SQLException e) {
            lblErro.setText("Erro crítico de ligação ao Banco de Dados.");
            e.printStackTrace();
        }
    }

    private void carregarDashboard(ActionEvent event) {
        try {
            // Localiza o ficheiro FXML seguindo a convenção profissional de Resources
// CORREÇÃO: Caminho absoluto exato para o novo pacote estruturado
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ao/clinica/medica/view/dashboard.fxml"));
            Parent dashboardRoot = loader.load();

            // Configura a nova Scene
            Scene dashboardScene = new Scene(dashboardRoot);

            // Recupera o Stage atual através do evento do botão
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(dashboardScene);
            window.setTitle("ClinicaMed - Painel de Gestão Operacional");
            window.centerOnScreen(); // Garante o alinhamento perfeito no monitor
            window.show();

        } catch (IOException e) {
            lblErro.setText("Falha grave ao carregar a interface principal.");
            e.printStackTrace();
        }
    }
}