/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ulbra.DAO;

import static br.ulbra.DAO.AbstractDAO.getConnection;
import br.ulbra.model.Cliente;
import br.ulbra.model.Usuario;
import br.ulbra.model.Venda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author devVHarthmann
 */
public class VendaDAO extends AbstractDAO implements CrudRepository<Venda> {

    @Override
    public void salvar(Venda v) throws SQLException {
        String sql = "INSERT INTO venda (dataVenda, valorTotal, idUsuario, idCliente) VALUES (?,?,?,?)";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, v.getDataVendaSql());
            ps.setDouble(2, v.getValorTotal());
            ps.setInt(3, v.getIdVendedor().getId_usuario());
            ps.setInt(4, v.getIdCliente().getIdCliente());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Falha ao gerar venda.");
            } else {

            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    v.setIdVenda(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Venda buscarPorId(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Venda> listar() throws SQLException {
        String sql = "SELECT "
                + "v.idVenda,"
                + "v.dataVenda,"
                + "v.valorTotal,"
                + "u.idUsuario,"
                + "u.nome,"
              
                + "c.idCliente,"
                + "c.cpf "
            
                + "FROM venda v "
                + "INNER JOIN usuario u ON v.idUsuario = u.idUsuario "
                + "INNER JOIN cliente c ON v.idCliente = c.idCliente "
                + "ORDER BY v.idVenda;";

        List<Venda> lista = new ArrayList<>();

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("idUsuario"),
                        rs.getString("nome")
                );
                Cliente c = new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("cpf")              
                );
                Venda v = new Venda(
                        rs.getInt("idVenda"),
                        rs.getDate("dataVenda").toLocalDate(),
                        rs.getDouble("valorTotal"),
                        u,
                        c
                );
                lista.add(v);
            }
        }
        return lista;
    }

    @Override
    public void atualizar(Venda obj) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remover(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
