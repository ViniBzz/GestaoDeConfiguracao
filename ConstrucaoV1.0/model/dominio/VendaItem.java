/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.model.dominio;

import java.math.BigDecimal;

import java.time.LocalDateTime;
/**
 *
 * @author vinicius
 */
public class VendaItem 
{
    private Venda vendal;
    private Produto produto;
    private Integer Quantidade;
    private BigDecimal total;
    private BigDecimal Desconto;

    public VendaItem() 
    {
        
    }

    public VendaItem(Venda vendal, Produto produto, Integer Quantidade, BigDecimal total, BigDecimal Desconto) {
        this.vendal = vendal;
        this.produto = produto;
        this.Quantidade = Quantidade;
        this.total = total;
        this.Desconto = Desconto;
    }

    public Venda getVendal() {
        return vendal;
    }

    public void setVendal(Venda vendal) {
        this.vendal = vendal;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(Integer Quantidade) {
        this.Quantidade = Quantidade;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDesconto() {
        return Desconto;
    }

    public void setDesconto(BigDecimal Desconto) {
        this.Desconto = Desconto;
    }
    
                                                                                                                                
}
                                                                      