package ao.clinica.medica.controller;

import ao.clinica.medica.dao.PacienteDAOImpl;
import ao.clinica.medica.dao.MedicoDAOImpl;
import ao.clinica.medica.model.Consulta;
import ao.clinica.medica.model.Paciente;
import ao.clinica.medica.model.Medico;
import ao.clinica.medica.model.StatusConsulta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

import java.sql.SQLException;
import java.time.LocalDate;

public class ConsultaController {

    @FXML private ComboBox<Paciente> cbPaciente;
    @FXML private ComboBox<Medico> cbMedico;
    @FXML private ComboBox<String> cbStatus;
    @FXML private DatePicker dpDataConsulta;
    @FXML private TextField txtHorario;

    @FXML private TableView<Consulta> tableConsultas;
    @FXML private TableColumn<Consulta, Integer> colId;
    @FXML private TableColumn<Consulta, String> colPaciente, colMedico, colEstado;
    @FXML private TableColumn<Consulta, LocalDate> colData;

    private final PacienteDAOImpl pacienteDAO = new PacienteDAOImpl();
    private final MedicoDAOImpl medicoDAO = new MedicoDAOImpl();
    // Substitua pelo seu ConsultaDAO real se já o tiver criado
    private ObservableList<Consulta> listaConsultas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Configurar Colunas da Tabela
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataConsulta"));

        colPaciente.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPaciente().getNome()));

        colMedico.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMedico().getUsuario().getNome()));

        colEstado.setCellValueFactory(new PropertyValueFactory<>("status"));

        // 2. Carregar os ComboBoxes com dados do Banco
        carregarComponentesIniciais();

        tableConsultas.setPlaceholder(new Label("Nenhuma consulta agendada no sistema."));
    }

    private void carregarComponentesIniciais() {
        try {
            // Carrega Pacientes
            ObservableList<Paciente> pacientes = FXCollections.observableArrayList(pacienteDAO.buscarTodos());
            cbPaciente.setItems(pacientes);

            // Carrega Médicos
            ObservableList<Medico> medicos = FXCollections.observableArrayList(medicoDAO.buscarTodos());
            cbMedico.setItems(medicos);

            // Popula os Estados padrão de atendimento clínico
            cbStatus.setItems(FXCollections.observableArrayList("Agendado", "Confirmado", "Cancelado", "Em Atendimento"));
            cbStatus.setValue("Agendado");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSalvar(ActionEvent event) {
        if (cbPaciente.getValue() == null || cbMedico.getValue() == null || dpDataConsulta.getValue() == null) {
            mostrarAlerta("Validação", "Preencha todos os campos obrigatórios.", Alert.AlertType.WARNING);
            return;
        }

        // Fluxo de salvamento via ConsultaDAO vai aqui...
        mostrarAlerta("Sucesso", "Consulta reservada com sucesso!", Alert.AlertType.INFORMATION);
        handleLimpar(null);
    }

    @FXML
    void handleConfirmar(ActionEvent event) {
        Consulta selecionada = tableConsultas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            selecionada.setStatus(StatusConsulta.CONFIRMADA);
            tableConsultas.refresh();
        }
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        Consulta selecionada = tableConsultas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            selecionada.setStatus(StatusConsulta.CANCELADA);
            tableConsultas.refresh();
        }
    }

    @FXML
    void handleLimpar(ActionEvent event) {
        cbPaciente.setValue(null);
        cbMedico.setValue(null);
        dpDataConsulta.setValue(null);
        txtHorario.clear();
        cbStatus.setValue("Agendado");
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }
}
