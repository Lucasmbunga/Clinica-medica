package ao.clinica.medica.dao;

import ao.clinica.medica.model.Endereco;
import ao.clinica.medica.model.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAOImpl implements GenericDAO<Paciente> {

    private final EnderecoDAOImpl enderecoDAO = new EnderecoDAOImpl();
    @Override
    public void salvar(Paciente paciente) throws SQLException {
        // 1. Salva o endereço primeiro para conseguir o ID gerado
        /*Endereco endereco = new Endereco();
        if (paciente.getEndereco() != null) {
            enderecoDAO.salvar(paciente.getEndereco());
        } else {
            throw new SQLException("Não é possível salvar um paciente sem endereço completo.");
        }
*/
        // 2. Insere o paciente usando o ID do endereço como FK
        String sql = "INSERT INTO pacientes ( nome, cpf, data_nascimento) VALUES ( ?,  ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, Date.valueOf(paciente.getDataNascimento())); // Converte LocalDate para java.sql.Date
            //stmt.setString(4, paciente.getTelefone());
            //stmt.setString(4, paciente.getHistoricoMedico());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    paciente.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void atualizar(Paciente paciente) throws SQLException {
        // 1. Atualiza os dados do endereço primeiro
        Endereco endereco=enderecoDAO.buscarPorPaciente(paciente);
        if (endereco != null) {
            enderecoDAO.atualizar(endereco);
        }

        // 2. Atualiza os dados do paciente
        String sql = "UPDATE pacientes SET nome = ?, cpf = ?, data_nascimento = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, Date.valueOf(paciente.getDataNascimento()));
            stmt.setString(4, paciente.getTelefone());
            stmt.setInt(5, paciente.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws SQLException {
        // Devido ao ON DELETE CASCADE configurado no banco, apagar o endereço
        // associado vai remover o paciente automaticamente de forma limpa.
        Paciente paciente = buscarPorId(id);
        Endereco   endereco=enderecoDAO.buscarPorPaciente(paciente);
        if (paciente != null && endereco != null) {
            enderecoDAO.excluir(endereco.getId());
        }
    }

    @Override
    public Paciente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    return new Paciente(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getDate("data_nascimento").toLocalDate(), // O driver moderno converte direto para LocalDate
                            rs.getString("telefone")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Paciente> buscarTodos() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                lista.add(new Paciente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getDate("data_nascimento").toLocalDate(),
                        rs.getString("telefone")
                ));
            }
        }
        return lista;
    }
}