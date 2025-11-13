package br.ulbra.controller;

import br.ulbra.DAO.ProdutoDAO;
import br.ulbra.model.Produto;
import java.sql.SQLException;
import java.util.List;

public class ProdutoController {

    private ProdutoDAO dao = new ProdutoDAO();

    public void salvar(Produto p) throws SQLException {
        dao.salvar(p);
    }

    public Produto buscar(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public List<Produto> listar() throws SQLException {
        return dao.listar();
    }

    public void atualizar(Produto p) throws SQLException {
        dao.atualizar(p);
    }

    public void atualizarQuantidade(Produto p) throws SQLException {
        dao.atualizarQuantidade(p);
    }

    public void remover(int id) throws SQLException {
        dao.remover(id);
    }
}
