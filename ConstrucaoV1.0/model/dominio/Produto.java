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
public class Produto 
{
    private long id;
    private String Nome;
    private String Descricao;
    private BigDecimal Preco;
    private Integer Quantidade;
    private Categoria categoria;
    private Usuario usuario;
    private LocalDateTime DataHoraCriacao;

    public Produto() 
    {
        
    }

    public Produto(long id, String Nome, String Descricao, BigDecimal Preco, Integer Quantidade, Categoria categoria, Usuario usuario, LocalDateTime DataHoraCriacao) {
        this.id = id;
        this.Nome = Nome;
        this.Descricao = Descricao;
        this.Preco = Preco;
        this.Quantidade = Quantidade;
        this.categoria = categoria;
        this.usuario = usuario;
        this.DataHoraCriacao = DataHoraCriacao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String Descricao) {
        this.Descricao = Descricao;
    }

    public BigDecimal getPreco() {
        return Preco;
    }

    public void setPreco(BigDecimal Preco) {
        this.Preco = Preco;
    }

    public Integer getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(Integer Quantidade) {
        this.Quantidade = Quantidade;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataHoraCriacao() {
        return DataHoraCriacao;
    }

    public void setDataHoraCriacao(LocalDateTime DataHoraCriacao) {
        this.DataHoraCriacao = DataHoraCriacao;
    }

    @Override
    public String toString() {
        return Nome;
    }
    
    
}
