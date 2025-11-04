/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.model.dominio;

/**
 *
 * @author vinicius
 */
public class Categoria 
{
    private long id;
    private String Nome;
    private String Descricao;

    public Categoria() {
    }

    public Categoria(long id, String Nome, String Descricao) {
        this.id = id;
        this.Nome = Nome;
        this.Descricao = Descricao;
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

    @Override
    public String toString() {
        return Nome;
    }
    
    
}
