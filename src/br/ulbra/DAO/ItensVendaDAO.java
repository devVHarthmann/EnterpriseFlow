/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ulbra.DAO;

import static br.ulbra.DAO.AbstractDAO.getConnection;
import br.ulbra.model.ItensVenda;
import br.ulbra.model.Produto;
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
public class ItensVendaDAO extends AbstractDAO implements CrudRepository<ItensVenda> {

    @Override
    public void salvar(ItensVenda i) throws SQLException {
        String sql = "INSERT INTO itensvenda (idVenda, idProduto, quantidadeProd, precoUnit) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i.getIdVenda().getIdVenda());
            ps.setInt(2, i.getIdProduto().getIdProduto());
            ps.setInt(3, i.getQuantiProduto());
            ps.setDouble(4, i.getPrecoUnit());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Falha ao inserir itens da venda.");
            } else {

            }

        }

    }

    @Override
    public ItensVenda buscarPorId(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ItensVenda> listar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");

    }

    public List<ItensVenda> listarItens(int id) throws SQLException {
        String sql = "SELECT\n"
                + "    v.idVenda,\n"
                + "    p.idProduto, p.nomeProduto,\n"
                + "    iv.quantidadeProd,\n"
                + "    iv.precoUnit\n"
                + "FROM venda v\n"
                + "INNER JOIN itensvenda iv ON v.idVenda = iv.idVenda\n"
                + "INNER JOIN produto p ON iv.idProduto = p.idProduto\n"
                + "WHERE v.idVenda = " + id;

        List<ItensVenda> lista = new ArrayList<>();

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Venda v = new Venda(
                        rs.getInt("idVenda")
                );
                Produto p = new Produto(
                        rs.getInt("idProduto"),
                        rs.getString("nomeProduto")
                );
                ItensVenda iv = new ItensVenda(
                        v,
                        p,
                        rs.getInt("quantidadeProd"),
                        rs.getDouble("precoUnit")
                );
                lista.add(iv);
            }

        }
        return lista;
    }

    @Override
    public void atualizar(ItensVenda obj) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remover(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
