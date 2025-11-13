package br.ulbra.DAO;

import br.ulbra.model.Fornecedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO extends AbstractDAO implements CrudRepository<Fornecedor> {

    @Override
    public void salvar(Fornecedor f) throws SQLException {
        String sql = "INSERT INTO fornecedor (razaoSocial, nomefantasia, cnpj, email, telefone ) VALUES (?, ?, ?,?, ?)";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, f.getRazaoSocial());
            ps.setString(2, f.getNomeFantasia());
            ps.setString(3, f.getCnpj());
            ps.setString(4, f.getEmail());
            ps.setString(5, f.getTelefone());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Falha ao inserir fornecedor");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    f.setIdFornecedor(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Fornecedor buscarPorId(int id) throws SQLException {
        String sql = "SELECT idFornecedor, razaoSocial, nomefantasia, cnpj, email, telefone FROM fornecedor WHERE idFornecedor = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Fornecedor(
                            rs.getInt("idFornecedor"),
                            rs.getString("razaoSocial"),
                            rs.getString("nomeFantasia"),
                            rs.getString("cnpj"),
                            rs.getString("email"),
                            rs.getString("telefone")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Fornecedor> listar() throws SQLException {
        String sql = "SELECT idFornecedor, razaoSocial, nomefantasia, cnpj, email, telefone FROM fornecedor ORDER by idFornecedor";
        List<Fornecedor> lista = new ArrayList<>();
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Fornecedor f = new Fornecedor(
                        rs.getInt("idFornecedor"),
                        rs.getString("razaoSocial"),
                        rs.getString("nomeFantasia"),
                        rs.getString("cnpj"),
                        rs.getString("email"),
                        rs.getString("telefone")
                );
                lista.add(f);
            }
        }
        return lista;
    }

    @Override
    public void atualizar(Fornecedor f) throws SQLException {
      String sql = "UPDATE fornecedor set razaoSocial=?, nomeFantasia=?, cnpj=?, email=?, telefone=?  where idFornecedor=?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
           ps.setString(1, f.getRazaoSocial());
            ps.setString(2, f.getNomeFantasia());
            ps.setString(3, f.getCnpj());
            ps.setString(4, f.getEmail());
            ps.setString(5, f.getTelefone());
            ps.setInt(6, f.getIdFornecedor());
            ps.executeUpdate();
        }
    }

    @Override
    public void remover(int id) throws SQLException {
          String sql = "DELETE FROM fornecedor WHERE idFornecedor = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

}
