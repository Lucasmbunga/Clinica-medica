package ao.clinica.medica.controller;

import ao.clinica.medica.model.Usuario;
import ao.clinica.medica.model.PerfilUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DashboardController {

    // Componentes injetados do dashboard.fxml
    @FXML private Label lblNomeUsuario;
    @FXML private Label lblPerfilUsuario;
    @FXML private Button btnPacientes;
    @FXML private Button btnMedicos;
    @FXML private Button btnConsultas;
    @FXML private Button btnProntuarios;
    @FXML private TableView<?> tableConsultas; // O tipo genérico será mapeado nas próximas semanas

    /**
     * O método initialize() é chamado automaticamente pelo FXMLLoader
     * logo após a renderização da árvore de componentes visuais.
     */
    @FXML
    public void initialize() {
        // Recupera o utilizador que realizou a autenticação na sessão ativa
        Usuario usuarioLogado = LoginController.getUsuarioLogado();

        if (usuarioLogado != null) {
            // Atualiza os elementos da UI com os dados reais da sessão
            lblNomeUsuario.setText(usuarioLogado.getNome());
            lblPerfilUsuario.setText("Perfil: " + usuarioLogado.getPerfil().name());

            // Executa a política de segurança de UI (RBAC) conforme as regras de negócio
            aplicarRestricoesDePerfil(usuarioLogado.getPerfil());
        }

        // Inicializar configurações da TableView padrão (Será expandido na Semana 3)
        configurarTabelaAtendimentos();
    }

    /**
     * Aplica o algoritmo de restrição visual com base no nível de privilégio do utilizador.
     */
    private void aplicarRestricoesDePerfil(PerfilUsuario perfil) {
        switch (perfil) {
            case RECEPCIONISTA:
                // Recepcionistas gerem a operação de balcão (Pacientes e Agendamentos),
                // mas não possuem autorização para abrir ou editar Prontuários Médicos.
                btnProntuarios.setDisable(true);
                btnProntuarios.setStyle("-fx-opacity: 0.4; -fx-cursor: not-allowed;");
                break;

            case MEDICO:
                // Médicos visualizam a agenda e editam o histórico clínico,
                // mas não realizam cadastros de infraestrutura ou do corpo clínico.
                btnMedicos.setDisable(true);
                btnMedicos.setStyle("-fx-opacity: 0.4; -fx-cursor: not-allowed;");
                break;

            case ADMINISTRADOR:
                // Acesso total irrestrito aos módulos do sistema.
                break;
        }
    }

    private void configurarTabelaAtendimentos() {
        // Define uma mensagem profissional para quando a tabela de triagem estiver vazia
        tableConsultas.setPlaceholder(new Label("Nenhum atendimento agendado para o turno atual."));
    }

    // --- MÉTODOS DE NAVEGAÇÃO (Stubs para roteamento FXML nas próximas etapas) ---

    @FXML
    public void navegarPacientes(ActionEvent event) {
        try {
            // Carrega o FXML de pacientes
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ao/clinica/medica/view/pacientes.fxml"));
            VBox pacientesPane = loader.load();

            // Captura o contentor dinâmico central do BorderPane do Dashboard e injeta a tela
            BorderPane root = (BorderPane) btnPacientes.getScene().getRoot();
            root.setCenter(pacientesPane);

        } catch (java.io.IOException e) {
            System.err.println("Erro ao carregar a sub-view de Pacientes.");
            e.printStackTrace();
        }
    }

    @FXML
    public void navegarMedicos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ao/clinica/medica/view/medicos.fxml"));
            VBox medicosPane = loader.load();

            BorderPane root = (BorderPane) btnMedicos.getScene().getRoot();
            root.setCenter(medicosPane);

        } catch (java.io.IOException e) {
            System.err.println("Erro ao carregar a sub-view do Corpo Clínico.");
            e.printStackTrace();
        }
    }
    @FXML
    public void navegarConsultas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ao/clinica/medica/view/consultas.fxml"));
            VBox consultasPane = loader.load();

            BorderPane root = (BorderPane) btnConsultas.getScene().getRoot();
            root.setCenter(consultasPane);

        } catch (java.io.IOException e) {
            System.err.println("Erro ao carregar a sub-view de Consultas.");
            e.printStackTrace();
        }
    }

    @FXML
    public void navegarProntuarios(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ao/clinica/medica/view/prontuarios.fxml"));
            VBox prontuariosPane = loader.load();

            BorderPane root = (BorderPane) btnProntuarios.getScene().getRoot();
            root.setCenter(prontuariosPane);

        } catch (java.io.IOException e) {
            System.err.println("Erro ao carregar o módulo de Prontuários.");
            e.printStackTrace();
        }
    }
}
