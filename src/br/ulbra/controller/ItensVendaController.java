/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ulbra.controller;

import br.ulbra.DAO.ItensVendaDAO;
import br.ulbra.model.ItensVenda;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author aluno.saolucas
 */
public class ItensVendaController {
    private ItensVendaDAO dao = new ItensVendaDAO();

    public void salvar(ItensVenda i) throws SQLException {
        dao.salvar(i);
    }

    public ItensVenda buscar(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public List<ItensVenda> listar() throws SQLException {
        return dao.listar();
    }
    
     public List<ItensVenda> listarItens(int id) throws SQLException {
        return dao.listarItens(id);
    }
    
    public void atualizar(ItensVenda i) throws SQLException {
        dao.atualizar(i);
    }

    public void remover(int id) throws SQLException {
        dao.remover(id);
    }
}
