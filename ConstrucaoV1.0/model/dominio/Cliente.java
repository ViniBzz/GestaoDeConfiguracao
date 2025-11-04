/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.model.dominio;

/**
 *
 * @author vinicius
 */
public class Cliente 
{
    private long id;
    private String nome;
    private String telefone;
    private String email;
    private long CPF;
    private String CNPJ;
    private long CEP;

    public Cliente() 
    {
        
    }

    public Cliente(long id, String nome, String telefone, String email, long CPF, String CNPJ, long CEP) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.CPF = CPF;
        this.CNPJ = CNPJ;
        this.CEP = CEP;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getCPF() {
        return CPF;
    }

    public void setCPF(long CPF) {
        this.CPF = CPF;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public long getCEP() {
        return CEP;
    }

    public void setCEP(long CEP) {
        this.CEP = CEP;
    }

    @Override
    public String toString() {
        return nome;
    }
    
    
    
}
