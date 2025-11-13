package br.ulbra.DAO;

import static br.ulbra.DAO.AbstractDAO.getConnection;
import br.ulbra.model.Fornecedor;
import br.ulbra.model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO extends AbstractDAO implements CrudRepository<Produto> {

    @Override
    public void salvar(Produto p) throws SQLException {
        String sql = "INSERT INTO produto (nomeProduto, categoria, valorUnitario, quantEstoque, idFornecedor) VALUES (?, ?, ?,?,?)";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNomeProduto());
            ps.setString(2, p.getCategoria());
            ps.setDouble(3, p.getValorUnitario());
            ps.setInt(4, p.getQuantEstoque());
            ps.setInt(5, p.getIdFornecedor().getIdFornecedor());
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Falha ao inserir produto.");
            } else {

            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdProduto(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Produto buscarPorId(int id) throws SQLException {
        String sql = "SELECT p.idProduto, p.nomeProduto, p.categoria, p.valorUnitario, p.quantEstoque, p.idFornecedor, f.idFornecedor, f.razaoSocial, f.nomeFantasia, f.cnpj, f.email, f.telefone"
                + "FROM produto p INNER JOIN fornecedor f ON p.idproduto = f.idfornecedor WHERE p.idfornecedor = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Fornecedor f = new Fornecedor(
                            rs.getInt("idFornecedor"),
                            rs.getString("razaoSocial"),
                            rs.getString("nomeFantasia"),
                            rs.getString("cnpj"),
                            rs.getString("email"),
                            rs.getString("telefone")
                    );
                    return new Produto(
                            rs.getInt("idProduto"),
                            rs.getString("nomeProduto"),
                            rs.getString("categoria"),
                            rs.getDouble("valorUnitario"),
                            rs.getInt("quantEstoque"),
                            f
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Produto> listar() throws SQLException {
        String sql = "SELECT p.idProduto, p.nomeProduto, p.categoria, p.valorUnitario, p.quantEstoque, p.idFornecedor, f.idFornecedor, f.razaoSocial, f.nomeFantasia, f.cnpj, f.email, f.telefone"
                + " FROM produto p INNER JOIN fornecedor f ON p.idfornecedor = f.idfornecedor ORDER by p.idfornecedor";

        List<Produto> lista = new ArrayList<>();

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Fornecedor f = new Fornecedor(
                        rs.getInt("idFornecedor"),
                        rs.getString("nomeFantasia")
                );
                Produto p = new Produto(
                        rs.getInt("idProduto"),
                        rs.getString("nomeProduto"),
                        rs.getString("categoria"),
                        rs.getDouble("valorUnitario"),
                        rs.getInt("quantEstoque"),
                        f
                );
                lista.add(p);
            }
        }
        return lista;
    }

    @Override
    public void atualizar(Produto p) throws SQLException {
        String sql = "UPDATE produto SET nomeProduto = ?, categoria = ? , valorUnitario = ?, quantEstoque = ?, idFornecedor = ? WHERE idProduto = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNomeProduto());
            ps.setString(2, p.getCategoria());
            ps.setDouble(3, p.getValorUnitario());
            ps.setInt(4, p.getQuantEstoque());
            ps.setInt(5, p.getIdFornecedor().getIdFornecedor());
            ps.setInt(6, p.getIdProduto());
            ps.executeUpdate();
        }
    }
    
    public void atualizarQuantidade(Produto p) throws SQLException {
        String sql = "UPDATE produto SET quantEstoque = ? WHERE idProduto = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getQuantEstoque());
            ps.setInt(2, p.getIdProduto());
            ps.executeUpdate();
        }
    }
    
    @Override
    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM produto WHERE idProduto = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

}
