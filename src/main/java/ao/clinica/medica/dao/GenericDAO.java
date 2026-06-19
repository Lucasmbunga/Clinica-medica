package ao.clinica.medica.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {
    void salvar(T entidade) throws SQLException;
    void atualizar(T entidade) throws SQLException;
    void excluir(int id) throws SQLException;
    T buscarPorId(int id) throws SQLException;
    List<T> buscarTodos() throws SQLException;
}
