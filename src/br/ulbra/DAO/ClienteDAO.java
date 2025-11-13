/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ulbra.DAO;

import static br.ulbra.DAO.AbstractDAO.getConnection;
import br.ulbra.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aluno.saolucas
 */
public class ClienteDAO extends AbstractDAO implements CrudRepository<Cliente>{

    @Override
    public void salvar(Cliente c) throws SQLException {
         String sql = "INSERT INTO cliente(nome,email,telefone,cpf)  VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getTelefone());
            ps.setString(4, c.getCpf());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Falha ao cadastrar cliente");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setIdCliente(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT idCliente, nome, email, telefone, cpf FROM cliente WHERE idCliente = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("idCliente"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("telefone"),
                            rs.getString("cpf")
                    );
                }
            }
        }
        return null;
    }
    
    public Cliente buscarPorCPF(String cpf) throws SQLException {
        String sql = "SELECT idCliente, nome, email, telefone, cpf FROM cliente WHERE cpf = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("idCliente"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("telefone"),
                            rs.getString("cpf")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Cliente> listar() throws SQLException {
        String sql = "SELECT idCliente, nome, email, telefone, cpf FROM cliente ORDER by idCliente";
        List<Cliente> lista = new ArrayList<>();
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente(
                            rs.getInt("idCliente"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("telefone"),
                            rs.getString("cpf")
                    );
                lista.add(c);
            }
        }
        return lista;
    }

    @Override
    public void atualizar(Cliente c) throws SQLException {
        String sql = "UPDATE cliente SET nome = ?, email = ? , telefone = ?, cpf = ? WHERE idCliente = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNome());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getTelefone());
            ps.setString(4, c.getCpf());
            ps.setInt(5, c.getIdCliente());
            ps.executeUpdate();
        }
    }

    @Override
    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE idCliente = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    
    
    
    
    
}
