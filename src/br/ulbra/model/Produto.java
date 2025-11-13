package br.ulbra.model;

public class Produto {
    private int idProduto;
    private String nomeProduto;
    private String categoria;
    private double valorUnitario;
    private int quantEstoque;
    private Fornecedor idFornecedor;
    
    
    public Produto() {
    }

    public Produto(int idProduto, String nomeProduto, String categoria, double valorUnitario, int quantEstoque, Fornecedor idFornecedor) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.categoria = categoria;
        this.valorUnitario = valorUnitario;
        this.quantEstoque = quantEstoque;
        this.idFornecedor = idFornecedor;
    }

    public Produto(int idProduto, String nomeProduto, int quantEstoque) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.quantEstoque = quantEstoque;
    }
    
    public Produto(int idProduto, String nomeProduto, String categoria, double valorUnitario, int quantEstoque) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.categoria = categoria;
        this.valorUnitario = valorUnitario;
        this.quantEstoque = quantEstoque;
    }

    public Produto(int idProduto, String nomeProduto, String categoria) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.categoria = categoria;
    }

    public Produto(int idProduto, String nomeProduto) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
    }

    
    
    

    public int getIdProduto() {
        return idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public int getQuantEstoque() {
        return quantEstoque;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public void setQuantEstoque(int quantEstoque) {
        this.quantEstoque = quantEstoque;
    }

    public Fornecedor getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Fornecedor idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    @Override
    public String toString() {
        return "Produto{" + "idProduto=" + idProduto + ", nomeProduto=" + nomeProduto + ", categoria=" + categoria + ", valorUnitario=" + valorUnitario + ", quantEstoque=" + quantEstoque + ", idFornecedor=" + idFornecedor + '}';
    }
    
}
