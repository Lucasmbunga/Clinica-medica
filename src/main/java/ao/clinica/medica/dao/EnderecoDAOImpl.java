package ao.clinica.medica.dao;

import ao.clinica.medica.model.Endereco;
import ao.clinica.medica.model.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAOImpl implements GenericDAO<Endereco> {

    @Override
    public void salvar(Endereco endereco) throws SQLException {
        // SQL ajustado conforme as colunas reais do diagrama MER
        String sql = "INSERT INTO Enderecos (id_paciente, rua, bairro, zona, numero_casa) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, endereco.getIdPaciente());
            stmt.setString(2, endereco.getRua());
            stmt.setString(3, endereco.getBairro());
            stmt.setString(4, endereco.getZona());
            stmt.setString(5, endereco.getNumeroCasa());

            stmt.executeUpdate();

            // Recupera o ID gerado pelo AUTO_INCREMENT do MySQL
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    endereco.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void atualizar(Endereco endereco) throws SQLException {
        // SQL ajustado para refletir a nova semântica de atributos
        String sql = "UPDATE Enderecos SET id_paciente = ?, rua = ?, bairro = ?, zona = ?, numero_casa = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, endereco.getIdPaciente());
            stmt.setString(2, endereco.getRua());
            stmt.setString(3, endereco.getBairro());
            stmt.setString(4, endereco.getZona());
            stmt.setString(5, endereco.getNumeroCasa());
            stmt.setInt(6, endereco.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM Enderecos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Endereco buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Enderecos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Endereco(
                            rs.getInt("id"),
                            rs.getInt("id_paciente"),
                            rs.getString("rua"),
                            rs.getString("bairro"),
                            rs.getString("zona"),
                            rs.getString("numero_casa")
                    );
                }
            }
        }
        return null;
    }

    public Endereco buscarPorPaciente(Paciente paciente) throws SQLException {
        Endereco endereco = new Endereco();
        String sql = "SELECT * FROM Enderecos WHERE id_paciente = ?";
        try(Connection conn = ConnectionFactory.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
        ) {
            if (rs.next()) {
                endereco.setId(rs.getInt("id"));
                endereco.setIdPaciente(rs.getInt("id_paciente"));
                endereco.setBairro(rs.getString("bairro"));
                endereco.setRua(rs.getString("rua"));
                endereco.setNumeroCasa(rs.getString("numero_casa"));
                endereco.setZona(rs.getString("zona"));

            }

        }

        return endereco;
    }

    @Override
    public List<Endereco> buscarTodos() throws SQLException {
        List<Endereco> lista = new ArrayList<>();
        String sql = "SELECT * FROM Enderecos";
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Endereco(
                        rs.getInt("id"),
                        rs.getInt("id_paciente"),
                        rs.getString("rua"),
                        rs.getString("bairro"),
                        rs.getString("zona"),
                        rs.getString("numero_casa")
                ));
            }
        }
        return lista;
    }
}