package ao.clinica.medica.dao;


import ao.clinica.medica.model.Consulta;
import ao.clinica.medica.model.StatusConsulta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAOImpl implements GenericDAO<Consulta> {

    private final PacienteDAOImpl pacienteDAO = new PacienteDAOImpl();
    private final MedicoDAOImpl medicoDAO = new MedicoDAOImpl();

    @Override
    public void salvar(Consulta consulta) throws SQLException {
        String sql = "INSERT INTO consultas (id_paciente, id_medico, data_consulta, hora_consulta, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, consulta.getPaciente().getId());
            stmt.setInt(2, consulta.getMedico().getId());
            stmt.setDate(3, Date.valueOf(consulta.getData()));
            stmt.setTime(4, Time.valueOf(consulta.getHora()));
            stmt.setString(5, consulta.getStatus().name());

            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    consulta.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void atualizar(Consulta consulta) throws SQLException {
        String sql = "UPDATE consultas SET id_paciente = ?, id_medico = ?, data_consulta = ?, hora_consulta = ?, status = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, consulta.getPaciente().getId());
            stmt.setInt(2, consulta.getMedico().getId());
            stmt.setDate(3, Date.valueOf(consulta.getData()));
            stmt.setTime(4, Time.valueOf(consulta.getHora()));
            stmt.setString(5, consulta.getStatus().name());
            stmt.setInt(6, consulta.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM consultas WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Consulta buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM consultas WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return MapearConsulta(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Consulta> buscarTodos() throws SQLException {
        List<Consulta> lista = new ArrayList<>();
        String sql = "SELECT * FROM consultas";
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(MapearConsulta(rs));
            }
        }
        return lista;
    }

    private Consulta MapearConsulta(ResultSet rs) throws SQLException {
        Consulta c = new Consulta();
        c.setId(rs.getInt("id"));
        c.setPaciente(pacienteDAO.buscarPorId(rs.getInt("id_paciente")));
        c.setMedico(medicoDAO.buscarPorId(rs.getInt("id_medico")));
        c.setData(rs.getDate("data_consulta").toLocalDate());
        c.setHora(rs.getTime("hora_consulta").toLocalTime());
        c.setStatus(StatusConsulta.valueOf(rs.getString("status")));
        return c;
    }
}
