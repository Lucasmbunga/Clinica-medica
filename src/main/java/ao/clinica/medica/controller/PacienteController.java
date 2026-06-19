package ao.clinica.medica.controller;

import ao.clinica.medica.dao.EnderecoDAOImpl;
import ao.clinica.medica.dao.PacienteDAOImpl;
import ao.clinica.medica.model.Endereco;
import ao.clinica.medica.model.Paciente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;

public class PacienteController {

    @FXML private TextField txtNome, txtCpf, txtRua, txtBairro, txtZona, txtNumeroCasa, txtPesquisa;
    @FXML private DatePicker dpNascimento;
    @FXML private TableView<Paciente> tablePacientes;
    @FXML private TableColumn<Paciente, Integer> colId;
    @FXML private TableColumn<Paciente, String> colNome, colCpf;
    @FXML private TableColumn<Paciente, LocalDate> colDataNasc;

    private final PacienteDAOImpl pacienteDAO = new PacienteDAOImpl();
    private final EnderecoDAOImpl enderecoDAO = new EnderecoDAOImpl();
    private ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();
    private Paciente pacienteSelecionado;

    @FXML
    public void initialize() {
        // Mapeia as colunas da TableView com os atributos do Model Paciente
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colDataNasc.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));

        tablePacientes.setPlaceholder(new Label("Nenhum paciente cadastrado no sistema."));

        carregarPacientes();
    }

    private void carregarPacientes() {
        try {
            listaPacientes.clear();
            listaPacientes.addAll(pacienteDAO.buscarTodos());
            tablePacientes.setItems(listaPacientes);
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Falha ao ler dados do banco de dados.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void handleSalvar(ActionEvent event) {
        if (txtNome.getText().isEmpty() || txtCpf.getText().isEmpty() || dpNascimento.getValue() == null) {
            mostrarAlerta("Validação", "Campos obrigatórios (*) não preenchidos.", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (pacienteSelecionado == null) {
                // MODO: NOVO CADASTRO
                Paciente novoPaciente = new Paciente();
                novoPaciente.setNome(txtNome.getText().trim());
                novoPaciente.setCpf(txtCpf.getText().trim());
                novoPaciente.setDataNascimento(dpNascimento.getValue());

                // Grava primeiro o Paciente para gerar o ID
                pacienteDAO.salvar(novoPaciente);

                // Grava o endereço associado ao ID gerado do paciente (Conforme o MER)
                Endereco novoEndereco = new Endereco(0, novoPaciente.getId(), txtRua.getText().trim(),
                        txtBairro.getText().trim(), txtZona.getText().trim(), txtNumeroCasa.getText().trim());
                enderecoDAO.salvar(novoEndereco);

            } else {
                // MODO: ATUALIZAÇÃO (EDIÇÃO)
                pacienteSelecionado.setNome(txtNome.getText().trim());
                pacienteSelecionado.setCpf(txtCpf.getText().trim());
                pacienteSelecionado.setDataNascimento(dpNascimento.getValue());
                pacienteDAO.atualizar(pacienteSelecionado);

                // Atualiza o endereço associado
                Endereco end = enderecoDAO.buscarPorId(pacienteSelecionado.getId());
                if (end != null) {
                    end.setRua(txtRua.getText().trim());
                    end.setBairro(txtBairro.getText().trim());
                    end.setZona(txtZona.getText().trim());
                    end.setNumeroCasa(txtNumeroCasa.getText().trim());
                    enderecoDAO.atualizar(end);
                }
            }

            carregarPacientes();
            handleLimpar(null);
            mostrarAlerta("Sucesso", "Operação concluída com êxito.", Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            mostrarAlerta("Erro Crítico", "Falha de persistência de dados.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void handleEditar(ActionEvent event) {
        pacienteSelecionado = tablePacientes.getSelectionModel().getSelectedItem();
        if (pacienteSelecionado == null) {
            mostrarAlerta("Seleção", "Selecione um paciente na tabela para editar.", Alert.AlertType.WARNING);
            return;
        }

        txtNome.setText(pacienteSelecionado.getNome());
        txtCpf.setText(pacienteSelecionado.getCpf());
        dpNascimento.setValue(pacienteSelecionado.getDataNascimento());

        try {
            Endereco end = enderecoDAO.buscarPorId(pacienteSelecionado.getId());
            if (end != null) {
                txtRua.setText(end.getRua());
                txtBairro.setText(end.getBairro());
                txtZona.setText(end.getZona());
                txtNumeroCasa.setText(end.getNumeroCasa());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleExcluir(ActionEvent event) {
        Paciente selecionado = tablePacientes.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Seleção", "Selecione um paciente para remover.", Alert.AlertType.WARNING);
            return;
        }

        try {
            pacienteDAO.excluir(selecionado.getId());
            carregarPacientes();
            mostrarAlerta("Sucesso", "Paciente removido com sucesso.", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Não foi possível remover o registro.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void handleLimpar(ActionEvent event) {
        txtNome.clear(); txtCpf.clear(); txtRua.clear();
        txtBairro.clear(); txtZona.clear(); txtNumeroCasa.clear();
        dpNascimento.setValue(null);
        pacienteSelecionado = null;
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }
}
