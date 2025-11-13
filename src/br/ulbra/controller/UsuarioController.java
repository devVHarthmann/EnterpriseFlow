/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ulbra.controller;

import br.ulbra.DAO.UsuarioDAO;
import br.ulbra.model.Usuario;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author vdasi
 */
public class UsuarioController {
    private UsuarioDAO dao = new UsuarioDAO();
    
    public void salvar(Usuario u) throws SQLException{
        dao.salvar(u);
    }
    public Usuario buscar (int id) throws SQLException{
        return dao.buscarPorId(id);
    }
    public List<Usuario> listar() throws SQLException{
        return dao.listar();
    }
    public void atualizar(Usuario u) throws SQLException{
        dao.atualizar(u);
    }
    public void remover(int id) throws SQLException{
        dao.remover(id);
    }
}
