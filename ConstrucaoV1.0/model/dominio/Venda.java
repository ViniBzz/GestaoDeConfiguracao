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
public class Venda 
{
    private long id;
    private Cliente cliente;
    private Usuario usuario;
    private BigDecimal TotalDavenda;
    private BigDecimal ValorPago;
    private BigDecimal Desconto;
    private BigDecimal Troco;
    private LocalDateTime DataHoraDaCriacao;
    private LocalDateTime UltimaAtualizacao;

    public Venda() 
    {
        
    }

    public Venda(long id, Cliente cliente, Usuario usuario, BigDecimal TotalDavenda, BigDecimal ValorPago, BigDecimal Desconto, BigDecimal Troco, LocalDateTime DataHoraDaCriacao, LocalDateTime UltimaAtualizacao) {
        this.id = id;
        this.cliente = cliente;
        this.usuario = usuario;
        this.TotalDavenda = TotalDavenda;
        this.ValorPago = ValorPago;
        this.Desconto = Desconto;
        this.Troco = Troco;
        this.DataHoraDaCriacao = DataHoraDaCriacao;
        this.UltimaAtualizacao = UltimaAtualizacao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getTotalDavenda() {
        return TotalDavenda;
    }

    public void setTotalDavenda(BigDecimal TotalDavenda) {
        this.TotalDavenda = TotalDavenda;
    }

    public BigDecimal getValorPago() {
        return ValorPago;
    }

    public void setValorPago(BigDecimal ValorPago) {
        this.ValorPago = ValorPago;
    }

    public BigDecimal getDesconto() {
        return Desconto;
    }

    public void setDesconto(BigDecimal Desconto) {
        this.Desconto = Desconto;
    }

    public BigDecimal getTroco() {
        return Troco;
    }

    public void setTroco(BigDecimal Troco) {
        this.Troco = Troco;
    }

    public LocalDateTime getDataHoraDaCriacao() {
        return DataHoraDaCriacao;
    }

    public void setDataHoraDaCriacao(LocalDateTime DataHoraDaCriacao) {
        this.DataHoraDaCriacao = DataHoraDaCriacao;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return UltimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime UltimaAtualizacao) {
        this.UltimaAtualizacao = UltimaAtualizacao;
    }
    
    
    
    
}
