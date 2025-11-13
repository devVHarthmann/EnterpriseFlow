
package br.ulbra.model;

public class ItensVenda {
    
    private Venda idVenda;
    private Produto idProduto;
    private int quantiProduto;
    private double precoUnit;

    public ItensVenda() {
    }

    public ItensVenda(Venda idVenda, Produto idProduto, int quantiProduto, double precoUnit) {
        this.idVenda = idVenda;
        this.idProduto = idProduto;
        this.quantiProduto = quantiProduto;
        this.precoUnit = precoUnit;
    }

    public Venda getIdVenda() {
        return idVenda;
    }

    public Produto getIdProduto() {
        return idProduto;
    }

    public int getQuantiProduto() {
        return quantiProduto;
    }

    public double getPrecoUnit() {
        return precoUnit;
    }

    public void setIdVenda(Venda idVenda) {
        this.idVenda = idVenda;
    }

    public void setIdProduto(Produto idProduto) {
        this.idProduto = idProduto;
    }

    public void setQuantiProduto(int quantiProduto) {
        this.quantiProduto = quantiProduto;
    }

    public void setPrecoUnit(double precoUnit) {
        this.precoUnit = precoUnit;
    }

    @Override
    public String toString() {
        return "itensVenda{" + "idVenda=" + idVenda + ", idProduto=" + idProduto + ", quantiProduto=" + quantiProduto + ", precoUnit=" + precoUnit + '}';
    }
    
    
}
