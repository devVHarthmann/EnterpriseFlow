/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ulbra.DAO;

import static br.ulbra.DAO.AbstractDAO.getConnection;
import br.ulbra.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author vdasi
 */
public class UsuarioDAO extends AbstractDAO implements CrudRepository<Usuario> {

    private String criptografarSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }
    
    @Override
    public void salvar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, cargo, senha) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getCargo());
            ps.setString(4, criptografarSenha(u.getSenha()));

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Falha ao inserir usuario");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setId_usuario(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT idusuario, nome, email ,cargo, senha FROM usuario WHERE idusuario = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("idusuario"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("cargo"),
                            rs.getString("senha")
                    );
                }
            }
        }
        return null;
    }
    
    
    
    
    
    
    
    @Override
    public List<Usuario> listar() throws SQLException {
        String sql = "Select idusuario,nome,email,cargo FROM usuario ORDER by idusuario";
        List<Usuario> lista = new ArrayList<>();
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("idusuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("cargo"));

                lista.add(u);

            }
        }
        return lista;
    }

    @Override
    public void atualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuario set nome=?, email=?, cargo=? where idusuario=?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getCargo());
            ps.setInt(4, u.getId_usuario());

            ps.executeUpdate();
        }

    }

    @Override
    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE idusuario = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Usuario autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT idusuario, nome, email, cargo, senha FROM usuario WHERE email = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("senha");
                    if (BCrypt.checkpw(senha, hash)) {
                        return new Usuario(
                                rs.getInt("idusuario"),
                                rs.getString("nome"),
                               
                                rs.getString("email"),
                                rs.getString("cargo"),
                                hash
                        );
                    }
                }
            }
        }
        return null;
    }

}
