package ao.clinica.medica.dao;

import ao.clinica.medica.model.Prontuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProntuarioDAOImpl implements GenericDAO<Prontuario> {

    private final ConsultaDAOImpl consultaDAO = new ConsultaDAOImpl();

    @Override
    public void salvar(Prontuario prontuario) throws SQLException {
        String sql = "INSERT INTO prontuarios (id_consulta, diagnostico, prescricao, observacoes) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, prontuario.getConsulta().getId());
            stmt.setString(2, prontuario.getDiagnostico());
            stmt.setString(3, prontuario.getPrescricao());
            stmt.setString(4, prontuario.getObservacoes());

            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    prontuario.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void atualizar(Prontuario prontuario) throws SQLException {
        String sql = "UPDATE prontuarios SET diagnostico = ?, prescricao = ?, observacoes = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prontuario.getDiagnostico());
            stmt.setString(2, prontuario.getPrescricao());
            stmt.setString(3, prontuario.getObservacoes());
            stmt.setInt(4, prontuario.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM prontuarios WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Prontuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM prontuarios WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return MapearProntuario(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Prontuario> buscarTodos() throws SQLException {
        List<Prontuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM prontuarios";
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(MapearProntuario(rs));
            }
        }
        return lista;
    }

    private Prontuario MapearProntuario(ResultSet rs) throws SQLException {
        Prontuario p = new Prontuario();
        p.setId(rs.getInt("id"));
        p.setConsulta(consultaDAO.buscarPorId(rs.getInt("id_consulta")));
        p.setDiagnostico(rs.getString("diagnostico"));
        p.setPrescricao(rs.getString("prescricao"));
        p.setObservacoes(rs.getString("observacoes"));
        p.setDataRegistro(rs.getTimestamp("data_registro").toLocalDateTime());
        return p;
    }
}
