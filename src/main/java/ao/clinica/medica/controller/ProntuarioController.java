package ao.clinica.medica.controller;

import ao.clinica.medica.dao.ConsultaDAOImpl;
import ao.clinica.medica.dao.ProntuarioDAOImpl;
import ao.clinica.medica.model.Consulta;
import ao.clinica.medica.model.Prontuario;
import ao.clinica.medica.model.StatusConsulta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProntuarioController {

    @FXML private ListView<Consulta> listConsultasAtivas;
    @FXML private Label lblPacienteSelecionado, lblDataHoje;
    @FXML private TextArea txtDiagnostico, txtPrescricao, txtObservacoes;
    @FXML private Button btnIniciarAtendimento, btnFinalizar;

    private final ConsultaDAOImpl consultaDAO = new ConsultaDAOImpl();
    private final ProntuarioDAOImpl prontuarioDAO = new ProntuarioDAOImpl();
    private Consulta consultaEmFoco;

    @FXML
    public void initialize() {
        lblDataHoje.setText("Hoje: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Customização da exibição da lista de consultas
        listConsultasAtivas.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Consulta item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getPaciente().getNome() + " [" + item.getHora() + "]");
                }
            }
        });

        carregarConsultasDoDia();
    }

    private void carregarConsultasDoDia() {
        try {
            // No mundo real, filtraríamos apenas consultas com status 'Confirmado' do dia de hoje
            ObservableList<Consulta> consultas = FXCollections.observableArrayList(consultaDAO.buscarTodos());
            listConsultasAtivas.setItems(consultas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAbrirFicha() {
        consultaEmFoco = listConsultasAtivas.getSelectionModel().getSelectedItem();
        if (consultaEmFoco != null) {
            lblPacienteSelecionado.setText("Paciente: " + consultaEmFoco.getPaciente().getNome());
            txtDiagnostico.clear();
            txtPrescricao.clear();
            txtObservacoes.clear();
        } else {
            mostrarAlerta("Aviso", "Selecione um atendimento na lista lateral.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void handleFinalizarAtendimento() {
        if (consultaEmFoco == null) {
            mostrarAlerta("Erro", "Não há nenhum atendimento ativo para gravar.", Alert.AlertType.ERROR);
            return;
        }

        if (txtDiagnostico.getText().trim().isEmpty()) {
            mostrarAlerta("Validação", "O diagnóstico é obrigatório para finalizar o prontuário.", Alert.AlertType.WARNING);
            return;
        }

        try {
            Prontuario p = new Prontuario();
            p.setConsulta(consultaEmFoco);
            p.setDiagnostico(txtDiagnostico.getText().trim());
            p.setPrescricao(txtPrescricao.getText().trim());
            p.setObservacoes(txtObservacoes.getText().trim());
            p.setDataRegistro(LocalDateTime.now());

            prontuarioDAO.salvar(p);

            // Atualizar status da consulta para 'REALIZADA'
            consultaEmFoco.setStatus(StatusConsulta.REALIZADA);
            consultaDAO.atualizar(consultaEmFoco);

            mostrarAlerta("Sucesso", "Prontuário guardado e atendimento finalizado com sucesso.", Alert.AlertType.INFORMATION);
            limparFormulario();
            carregarConsultasDoDia();

        } catch (SQLException e) {
            mostrarAlerta("Erro Crítico", "Falha ao gravar os dados clínicos no banco de dados.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void limparFormulario() {
        lblPacienteSelecionado.setText("Selecione um paciente para iniciar");
        txtDiagnostico.clear();
        txtPrescricao.clear();
        txtObservacoes.clear();
        consultaEmFoco = null;
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }
}
