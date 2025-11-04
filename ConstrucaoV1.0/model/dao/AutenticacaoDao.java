/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.model.dao;

import com.mycompany.clamixgestao.model.dominio.Perfil;
import com.mycompany.clamixgestao.model.dominio.Usuario;
import com.mycompany.clamixgestao.model.exception.NegocioException;
import com.mycompany.clamixgestao.view.modelo.LoginDto;
import javax.swing.JOptionPane;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author vinicius
 */
public class AutenticacaoDao 
{
    private final UsuarioDao usuarioDao;
    
    public AutenticacaoDao()
    {
        this.usuarioDao = new UsuarioDao();
    }
    
    public boolean temPermissao(Usuario usuario)
    {
        try
        {
            permissao(usuario);
            return true;
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Usuario sem permissao", 0);
            return false;  
        }
    }
    
    private void permissao(Usuario usuario)
    {
        if (!Perfil.Admin.equals(usuario.getPerfil()))
        {
            throw new NegocioException("Sem permissao para relaziar a acao.");
        }
    }
    
    public Usuario login(LoginDto login)
    {
        Usuario usuario = usuarioDao.buscarPeloUsuario(login.getUsuario());
        
        if(usuario == null || !usuario.isEstado())
        {
            return null;
        }
        if(usuario.isEstado() && validarSenha(usuario.getSenha(), login.getSenha()))
        {
            return usuario;
        }
        
        return null;
    }

    // Para o uso sem o spring security crypt
   // private boolean validarSenhaSemSeguraca(String senhaUsuario, String senhaLogin) 
   // {
  //      return senhaUsuario.equals(senhaLogin);
  //  }
    
    private boolean validarSenha(String senhaUsuario, String senhaLogin) 
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(senhaLogin, senhaUsuario);
    }
    
}
