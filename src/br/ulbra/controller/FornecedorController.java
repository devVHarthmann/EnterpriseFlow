package br.ulbra.controller;

import br.ulbra.DAO.FornecedorDAO;
import br.ulbra.model.Fornecedor;
import java.sql.SQLException;
import java.util.List;

public class FornecedorController {

    private FornecedorDAO dao = new FornecedorDAO();

    public void salvar(Fornecedor f) throws SQLException {
        dao.salvar(f);
    }

    public Fornecedor buscar(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public List<Fornecedor> listar() throws SQLException {
        return dao.listar();
    }

    public void atualizar(Fornecedor f) throws SQLException {
        dao.atualizar(f);
    }

    public void remover(int id) throws SQLException {
        dao.remover(id);
    }
}
