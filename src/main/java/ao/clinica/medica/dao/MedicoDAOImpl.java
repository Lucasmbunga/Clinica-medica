package ao.clinica.medica.dao;

import ao.clinica.medica.model.Medico;
import ao.clinica.medica.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAOImpl implements GenericDAO<Medico> {

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    @Override
    public void salvar(Medico medico) throws SQLException {
        // 1. Garante que o usuário vinculado está guardado e tem o ID gerado
        if (medico.getUsuario() != null) {
            usuarioDAO.salvar(medico.getUsuario());
        } else {
            throw new SQLException("Não é possível cadastrar um médico sem um utilizador de acesso associado.");
        }

        String sql = "INSERT INTO medicos (id_usuario, numero_crm, especialidade, telefone) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, medico.getUsuario().getId());
            stmt.setString(2, medico.getCrm());
            stmt.setString(3, medico.getEspecialidade());
            stmt.setString(4, medico.getTelefone());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    medico.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void atualizar(Medico medico) throws SQLException {
        // 1. Atualiza primeiro os dados de login/nome na tabela de utilizadores
        if (medico.getUsuario() != null) {
            usuarioDAO.atualizar(medico.getUsuario());
        }

        // 2. Atualiza os dados específicos do médico
        String sql = "UPDATE medicos SET numero_crm = ?, especialidade = ?, telefone = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medico.getCrm());
            stmt.setString(2, medico.getEspecialidade());
            stmt.setString(3, medico.getTelefone());
            stmt.setInt(4, medico.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws SQLException {
        // Devido ao ON DELETE CASCADE no banco de dados, apagar o utilizador
        // associado vai remover o médico do sistema automaticamente.
        Medico medico = buscarPorId(id);
        if (medico != null && medico.getUsuario() != null) {
            usuarioDAO.excluir(medico.getUsuario().getId());
        }
    }

    @Override
    public Medico buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM medicos WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int usuarioId = rs.getInt("id_usuario");
                    Usuario usuario = usuarioDAO.buscarPorId(usuarioId);

                    Medico medico = new Medico();
                    medico.setId(rs.getInt("id"));
                    medico.setUsuario(usuario);
                    medico.setCrm(rs.getString("numero_crm"));
                    medico.setEspecialidade(rs.getString("especialidade"));
                    medico.setTelefone(rs.getString("telefone"));
                    return medico;
                }
            }
        }
        return null;
    }

    @Override
    public List<Medico> buscarTodos() throws SQLException {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM medicos";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int usuarioId = rs.getInt("id_usuario");
                Usuario usuario = usuarioDAO.buscarPorId(usuarioId);

                Medico medico = new Medico();
                medico.setId(rs.getInt("id"));
                medico.setUsuario(usuario);
                medico.setCrm(rs.getString("munero_crm"));
                medico.setEspecialidade(rs.getString("especialidade"));
                medico.setTelefone(rs.getString("telefone"));

                lista.add(medico);
            }
        }
        return lista;
    }
}