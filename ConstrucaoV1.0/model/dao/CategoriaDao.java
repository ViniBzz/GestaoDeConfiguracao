/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.model.dao;

import com.mycompany.clamixgestao.model.conection.Conection;
import com.mycompany.clamixgestao.model.conection.ConexaoSQLMariaDB;
import com.mycompany.clamixgestao.model.dominio.Categoria;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vinicius
 */
public class CategoriaDao 
{
    private final Conection conexao;
    
    public CategoriaDao()
    {
        this.conexao = new ConexaoSQLMariaDB();
    }
    
    public List<Categoria> buscarTodasCategorias()
    {
        String sql = "SELECT * FROM categoria";
        List<Categoria> categorias = new ArrayList<>();
        try
        {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
                    
             while(result.next())
             {
                 categorias.add(getCategoria(result));
             }
        }catch (SQLException e)
        {
           System.out.println(String.format("Error: %s", e.getMessage()));
        }
        return categorias;
    }
    
    public Categoria buscarPorId(long id)
    {
        String sql = "SELECT * FROM categoria WHERE id = ?";
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();
                    
             if(result.next())
             {
                 return getCategoria(result);
             }
        }catch (SQLException e)
        {
           System.out.println(String.format("Error: %s", e.getMessage()));
        }
        return null;
    }
    
    private Categoria getCategoria(ResultSet result) throws SQLException
    {
        Categoria categoria = new Categoria();
        
        categoria.setId(result.getLong("id"));
        categoria.setNome(result.getString("nome"));
        categoria.setDescricao(result.getString("descricao"));
        
        return categoria;
    }
}



