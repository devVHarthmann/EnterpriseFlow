/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template cile, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ulbra.controller;

import br.ulbra.DAO.ClienteDAO;
import br.ulbra.model.Cliente;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author aluno.saolucas
 */
public class ClienteController {
    private ClienteDAO dao = new ClienteDAO();

    public void salvar(Cliente c) throws SQLException {
        dao.salvar(c);
    }

    public Cliente buscar(int id) throws SQLException {
        return dao.buscarPorId(id);
    }
    
    public Cliente buscarCpf(String cpf) throws SQLException {
        return dao.buscarPorCPF(cpf);
    }
    
    public List<Cliente> listar() throws SQLException {
        return dao.listar();
    }

    public void atualizar(Cliente c) throws SQLException {
        dao.atualizar(c);
    }

    public void remover(int id) throws SQLException {
        dao.remover(id);
    }
}
