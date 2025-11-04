/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.model.dao;


import com.mycompany.clamixgestao.model.conection.Conection;
import com.mycompany.clamixgestao.model.conection.ConexaoSQLMariaDB;
import com.mycompany.clamixgestao.model.dominio.Perfil;
import com.mycompany.clamixgestao.model.dominio.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author vinicius
 */
public class UsuarioDao 
{
    private final Conection conexao;
    
    public UsuarioDao()
    {
        this.conexao = new ConexaoSQLMariaDB();
    }
    
    
    public String salvar(Usuario usuario)
    {
        return usuario.getId() == 0L ? adicionar(usuario) : editar(usuario);
    }

    private String adicionar(Usuario usuario) 
    {
        String sql = "INSERT INTO usuario(nome, senha, estado, perfil) VALUES(?, ?, ?, ?)";
        
        Usuario usuarioTemp = buscarPeloUsuario(usuario.getUsuario());
        
        if (usuarioTemp != null )
        {
            return String.format("Erro: username %s ja existe no banco de dados", usuario.getUsuario());
        }
        
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            
            preencheValorPreparedStatement(preparedStatement, usuario);
            
            int resultado = preparedStatement.executeUpdate();
            
            return resultado == 1 ? "Usuario adicionado com sucesso!" : "Nao foi possivel adicionar usuario";
        }catch (SQLException e)
        {
            return String.format("Error: %s", e.getMessage());
        }
    }

    private String editar(Usuario usuario) 
    {
        String sql = "UPDATE usuario SET nome = ?, senha = ?, estado = ?, perfil = ? WHERE id = ?";
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            
            preencheValorPreparedStatement(preparedStatement, usuario);
            preparedStatement.setLong(5, usuario.getId());
            
            int resultado = preparedStatement.executeUpdate();
            
            return resultado == 1 ? "Usuario editado com sucesso!" : "Nao foi possivel editar usuario ";
        }catch (SQLException e)
        {
            return String.format("Error: %s", e.getMessage());
        }
    }

    private void preencheValorPreparedStatement(PreparedStatement preparedStatement, Usuario usuario) throws SQLException
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        String SenhaCrypt = passwordEncoder.encode(usuario.getSenha());
        
        preparedStatement.setString(1, usuario.getNome());
        preparedStatement.setString(2, SenhaCrypt);
        preparedStatement.setBoolean(3, usuario.isEstado());
        preparedStatement.setString(4, usuario.getPerfil().name());
    }
    
    public List<Usuario> buscarTodosUsuarios()
    {
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();
        try
        {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
                    
             while(result.next())
             {
                 usuarios.add(getUsuario(result));
             }
        }catch (SQLException e)
        {
           System.out.println(String.format("Error: ", e.getMessage()));
        }
        return usuarios;
    }
    
    private Usuario getUsuario(ResultSet result) throws SQLException
    {
        Usuario usuario = new Usuario();
        
        usuario.setId(result.getLong("id"));
        usuario.setNome(result.getString("nome"));
        usuario.setUsuario(result.getString("nome")); // Usa nome como usuário para login
        usuario.setSenha(result.getString("senha"));
        usuario.setPerfil(Perfil.valueOf(result.getString("perfil")));
        usuario.setEstado(result.getBoolean("estado"));
        usuario.setDataHoraCriacao(result.getObject("data_hora_criado", LocalDateTime.class));
        usuario.setUltimoLogin(result.getObject("ultimo_login", LocalDateTime.class));
        
        return usuario;
    }
    
    public Usuario buscarPeloUsuarioId(long id)
    {
        String sql = "SELECT * FROM usuario WHERE id = ?";
   
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();
                    
             if(result.next())
             {
                 return getUsuario(result);
             }
        }catch (SQLException e)
        {
           System.out.println(String.format("Error: %s", e.getMessage()));
        }
        return null;
    }
    
    public Usuario buscarPeloUsuario(String usuario)
    {
        String sql = "SELECT * FROM usuario WHERE nome = ?";
        
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setString(1, usuario);
            ResultSet result = preparedStatement.executeQuery();
                    
             if(result.next())
             {
                 return getUsuario(result);
             }
        }catch (SQLException e)
        {
            e.printStackTrace();
           System.out.println(String.format("Error: %s", e.getMessage()));
        }
        return null;
    }
    
    public String excluir(long id)
    {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            
            int resultado = preparedStatement.executeUpdate();
            
            return resultado == 1 ? "Usuario excluido com sucesso!" : "Nao foi possivel excluir usuario";
        }catch (SQLException e)
        {
            return String.format("Error: %s", e.getMessage());
        }
    }
        
}
