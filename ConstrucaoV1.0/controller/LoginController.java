/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.controller;

import com.mycompany.clamixgestao.model.dao.AutenticacaoDao;
import com.mycompany.clamixgestao.model.dominio.Usuario;
import com.mycompany.clamixgestao.view.formulario.Login;
import com.mycompany.clamixgestao.view.modelo.LoginDto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author vinicius
 */
public class LoginController implements ActionListener 
{
    private final Login login;
    private AutenticacaoDao autenticacaoDao;
    
    public LoginController(Login login)
    {
        this.login = login;
        this.autenticacaoDao = new AutenticacaoDao();
    }

    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        String acao = ae.getActionCommand().toLowerCase();
        
        switch(acao)
        {
            case "login": login(); break;
            case "cancelar": cancelar(); break;
        }
    }

    private void login() 
    {
        String usuario = this.login.getTxtLoginUsuario().getText();
        String senha = this.login.getTxtLoginSenha().getText();
        
        if(usuario.equals("") || senha.equals(""))
        {
            this.login.getLabelLoginMensagem().setText("Usuario e senha devem ser preenchidos");
        }
        
        LoginDto loginDto = new LoginDto(usuario, senha);
        
        Usuario usuarioTemp = this.autenticacaoDao.login(loginDto);
        
        if(usuarioTemp != null)
        {
            this.login.dispose();
            com.mycompany.clamixgestao.view.formulario.ClamixGestao clamixGestao = 
                new com.mycompany.clamixgestao.view.formulario.ClamixGestao(usuarioTemp);
            clamixGestao.setVisible(true);
        }else
        {
            this.login.getLabelLoginMensagem().setText("Usuario ou senha incorretos");
        }
        
    }

    private void cancelar() 
    {
        int confirmar = JOptionPane.showConfirmDialog(login, "Tem certeza que deseja sair?", "Sair do sistema", JOptionPane.YES_NO_OPTION);
        
        if(confirmar == JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
        
    }
    
    private void limparCampos()
    {
        this.login.getTxtLoginUsuario().setText("");
        this.login.getTxtLoginSenha().setText("");
        this.login.getLabelLoginMensagem().setText("");
    }
    
}
