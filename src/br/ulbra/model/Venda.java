package br.ulbra.model;

import java.sql.Date;
import java.time.LocalDate;

public class Venda {
    private int idVenda;
    private LocalDate dataVenda;
    private Date dataVendaSql;
    private double valorTotal;
    private Usuario idVendedor;
    private Cliente idCliente;

    public Venda() {
    }

    public java.sql.Date getDataVendaSql() {
        return dataVendaSql;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }
    
    public Venda(int idVenda, LocalDate dataVenda, double valorTotal, Usuario idVendedor, Cliente idCliente) {
        this.idVenda = idVenda;
        this.dataVenda = dataVenda;
        this.dataVendaSql = Date.valueOf(dataVenda);
        this.valorTotal = valorTotal;
        this.idVendedor = idVendedor;
        this.idCliente = idCliente;
    }

    public Venda(int idVenda, double valorTotal, Usuario idVendedor, Cliente idCliente) {
        this.idVenda = idVenda;
        this.valorTotal = valorTotal;
        this.idVendedor = idVendedor;
        this.idCliente = idCliente;
    }

    public Venda(int idVenda) {
        this.idVenda = idVenda;
    }
    
    
    public int getIdVenda() {
        return idVenda;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public Usuario getIdVendedor() {
        return idVendedor;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setIdVendedor(Usuario idVendedor) {
        this.idVendedor = idVendedor;
    }
}
