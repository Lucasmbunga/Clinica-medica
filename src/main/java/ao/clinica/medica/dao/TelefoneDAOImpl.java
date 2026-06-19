package ao.clinica.medica.dao;


import ao.clinica.medica.model.Telefone;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TelefoneDAOImpl implements GenericDAO<Telefone> {

    @Override
    public void salvar(Telefone telefone) throws SQLException {
        String sql = "INSERT INTO telefones (id_paciente, id_medico, contacto) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (telefone.getIdPaciente() > 0) stmt.setInt(1, telefone.getIdPaciente());
            else stmt.setNull(1, Types.INTEGER);

            if (telefone.getIdMedico() > 0) stmt.setInt(2, telefone.getIdMedico());
            else stmt.setNull(2, Types.INTEGER);

            stmt.setString(3, telefone.getNumero());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    telefone.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Telefone> buscarPorPaciente(int idPaciente) throws SQLException {
        List<Telefone> lista = new ArrayList<>();
        String sql = "SELECT * FROM telefones WHERE id_paciente = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Telefone(rs.getInt("id"), rs.getInt("id_paciente"), rs.getInt("id_medico"), rs.getString("contacto")));
                }
            }
        }
        return lista;
    }

    @Override public void atualizar(Telefone entidade) throws SQLException {
        String sql = "UPDATE telefones SET contacto = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entidade.getNumero());
            stmt.setInt(2, entidade.getId());
            stmt.executeUpdate();
        }
    }
    @Override public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM telefones WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    @Override public Telefone buscarPorId(int id) throws SQLException { return null; }
    @Override public List<Telefone> buscarTodos() throws SQLException { return new ArrayList<>(); }
}