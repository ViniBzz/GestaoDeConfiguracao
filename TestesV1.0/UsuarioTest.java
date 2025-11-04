/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao;

import com.mycompany.clamixgestao.model.dao.UsuarioDao;
import com.mycompany.clamixgestao.model.dominio.Perfil;
import com.mycompany.clamixgestao.model.dominio.Usuario;
import java.time.LocalDateTime;

/**
 *
 * @author vinicius
 */
public class UsuarioTest 
{
    public static void main(String[] args)
    {
        Usuario usuario = new Usuario(0L, "Vinicius", "12345", "Vinicius", Perfil.Admin, null, null);
        
        UsuarioDao usuarioDao = new UsuarioDao();
        String mensagem = usuarioDao.salvar(usuario);
        System.out.println(mensagem);
    }
}
