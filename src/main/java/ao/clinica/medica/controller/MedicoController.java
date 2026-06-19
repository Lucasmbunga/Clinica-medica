package ao.clinica.medica.controller;

import ao.clinica.medica.dao.MedicoDAOImpl;
import ao.clinica.medica.model.Medico;
import ao.clinica.medica.model.Usuario;
import ao.clinica.medica.model.PerfilUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class MedicoController {

    @FXML private TextField txtNome, txtCrm, txtEspecialidade, txtLogin, txtPesquisa;
    @FXML private PasswordField txtSenha;
    @FXML private TableView<Medico> tableMedicos;
    @FXML private TableColumn<Medico, Integer> colId;
    @FXML private TableColumn<Medico, String> colNome, colCrm, colEspecialidade;

    private final MedicoDAOImpl medicoDAO = new MedicoDAOImpl();
    private ObservableList<Medico> listaMedicos = FXCollections.observableArrayList();
    private Medico medicoSelecionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Como o nome está dentro do objeto Usuario (Composição),
        // usaremos uma propriedade customizada ou lidaremos na exibição.
        colNome.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsuario().getNome()));

        colCrm.setCellValueFactory(new PropertyValueFactory<>("crm"));
        colEspecialidade.setCellValueFactory(new PropertyValueFactory<>("especialidade"));

        tableMedicos.setPlaceholder(new Label("Nenhum médico cadastrado."));
        carregarMedicos();
    }

    private void carregarMedicos() {
        try {
            listaMedicos.clear();
            listaMedicos.addAll(medicoDAO.buscarTodos());
            tableMedicos.setItems(listaMedicos);
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Falha ao sincronizar o corpo clínico.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void handleSalvar(ActionEvent event) {
        if (txtNome.getText().isEmpty() || txtCrm.getText().isEmpty() || txtLogin.getText().isEmpty()) {
            mostrarAlerta("Validação", "Campos com asterisco (*) são obrigatórios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (medicoSelecionado == null) {
                // Instancia o Usuário de acesso concomitante
                Usuario user = new Usuario(0, txtNome.getText().trim(), txtLogin.getText().trim(), txtSenha.getText().trim(), PerfilUsuario.MEDICO);

                Medico novoMedico = new Medico();
                novoMedico.setCrm(txtCrm.getText().trim());
                novoMedico.setEspecialidade(txtEspecialidade.getText().trim());
                novoMedico.setUsuario(user);

                medicoDAO.salvar(novoMedico);
            } else {
                // Atualização
                Usuario user = medicoSelecionado.getUsuario();
                user.setNome(txtNome.getText().trim());
                user.setLogin(txtLogin.getText().trim());
                if (!txtSenha.getText().isEmpty()) user.setSenha(txtSenha.getText().trim());

                medicoSelecionado.setCrm(txtCrm.getText().trim());
                medicoSelecionado.setEspecialidade(txtEspecialidade.getText().trim());

                medicoDAO.atualizar(medicoSelecionado);
            }

            carregarMedicos();
            handleLimpar(null);
            mostrarAlerta("Sucesso", "Médico registado com êxito.", Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            mostrarAlerta("Erro Crítico", "Erro na persistência do profissional.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void handleEditar(ActionEvent event) {
        medicoSelecionado = tableMedicos.getSelectionModel().getSelectedItem();
        if (medicoSelecionado == null) {
            mostrarAlerta("Seleção", "Selecione um médico para editar.", Alert.AlertType.WARNING);
            return;
        }

        txtNome.setText(medicoSelecionado.getUsuario().getNome());
        txtCrm.setText(medicoSelecionado.getCrm());
        txtEspecialidade.setText(medicoSelecionado.getEspecialidade());
        txtLogin.setText(medicoSelecionado.getUsuario().getLogin());
        txtSenha.clear(); // Por motivos de segurança, não preenchemos a senha em texto limpo
    }

    @FXML
    void handleExcluir(ActionEvent event) {
        Medico selecionado = tableMedicos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Seleção", "Selecione o profissional a ser removido.", Alert.AlertType.WARNING);
            return;
        }

        try {
            medicoDAO.excluir(selecionado.getId());
            carregarMedicos();
            mostrarAlerta("Sucesso", "Médico removido do sistema.", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao excluir o registo.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void handleLimpar(ActionEvent event) {
        txtNome.clear(); txtCrm.clear(); txtEspecialidade.clear();
        txtLogin.clear(); txtSenha.clear();
        medicoSelecionado = null;
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }
}