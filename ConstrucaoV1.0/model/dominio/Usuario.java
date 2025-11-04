/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.model.dominio;

import java.time.LocalDateTime;

/**
 *
 * @author vinicius
 */
public class Usuario 
{
    private long id;
    private String Nome;
    private String Senha;
    private String Usuario;
    private Perfil Perfil;
    private boolean Estado;
    private LocalDateTime DataHoraCriacao;
    private LocalDateTime UltimoLogin;
    
    public Usuario()
    {
        this.Estado = true;
    }

    public Usuario(long id, String Nome, String Senha, String Usuario, Perfil Perfil, LocalDateTime DataHoraCriacao, LocalDateTime UltimoLogin) {
        this.id = id;
        this.Nome = Nome;
        this.Senha = Senha;
        this.Usuario = Usuario;
        this.Perfil = Perfil;
        this.DataHoraCriacao = DataHoraCriacao;
        this.UltimoLogin = UltimoLogin;
        this.Estado = true;
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

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String Senha) {
        this.Senha = Senha;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public Perfil getPerfil() {
        return Perfil;
    }

    public void setPerfil(Perfil Perfil) {
        this.Perfil = Perfil;
    }

    public boolean isEstado() {
        return Estado;
    }

    public void setEstado(boolean Estado) {
        this.Estado = Estado;
    }

    public LocalDateTime getDataHoraCriacao() {
        return DataHoraCriacao;
    }

    public void setDataHoraCriacao(LocalDateTime DataHoraCriacao) {
        this.DataHoraCriacao = DataHoraCriacao;
    }

    public LocalDateTime getUltimoLogin() {
        return UltimoLogin;
    }

    public void setUltimoLogin(LocalDateTime UltimoLogin) {
        this.UltimoLogin = UltimoLogin;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        return this.id == other.id;
    }
     
    public void reset()
    {
        this.Estado = true;
    }
    public void mudarEstado()
    {
        this.Estado = !this.Estado;
    }
}
