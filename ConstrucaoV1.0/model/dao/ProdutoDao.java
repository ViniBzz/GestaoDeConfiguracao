/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.model.dao;

import com.mycompany.clamixgestao.model.conection.Conection;
import com.mycompany.clamixgestao.model.conection.ConexaoSQLMariaDB;
import com.mycompany.clamixgestao.model.dominio.Categoria;
import com.mycompany.clamixgestao.model.dominio.Produto;
import com.mycompany.clamixgestao.model.dominio.Usuario;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vinicius
 */
public class ProdutoDao 
{
    private final Conection conexao;
    
    public ProdutoDao()
    {
        this.conexao = new ConexaoSQLMariaDB();
    }
    
    public String salvar(Produto produto)
    {
        return produto.getId() == 0L ? adicionar(produto) : editar(produto);
    }
    
    private String adicionar(Produto produto)
    {
        String sql = "INSERT INTO estoque(nome, descricao, preco, quantidade, categoria_id, usuario_id) VALUES(?, ?, ?, ?, ?, ?)";
        
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            
            preencheValorPreparedStatement(preparedStatement, produto);
            
            int resultado = preparedStatement.executeUpdate();
            
            return resultado == 1 ? "Produto adicionado com sucesso!" : "Nao foi possivel adicionar produto";
        }catch (SQLException e)
        {
            return String.format("Error: %s", e.getMessage());
        }
    }
    
    private String editar(Produto produto)
    {
        String sql = "UPDATE estoque SET nome = ?, descricao = ?, preco = ?, quantidade = ?, categoria_id = ?, usuario_id = ? WHERE id = ?";
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            
            preencheValorPreparedStatement(preparedStatement, produto);
            preparedStatement.setLong(7, produto.getId());
            
            int resultado = preparedStatement.executeUpdate();
            
            return resultado == 1 ? "Produto editado com sucesso!" : "Nao foi possivel editar produto";
        }catch (SQLException e)
        {
            return String.format("Error: %s", e.getMessage());
        }
    }
    
    private void preencheValorPreparedStatement(PreparedStatement preparedStatement, Produto produto) throws SQLException
    {
        preparedStatement.setString(1, produto.getNome());
        preparedStatement.setString(2, produto.getDescricao());
        preparedStatement.setBigDecimal(3, produto.getPreco());
        preparedStatement.setInt(4, produto.getQuantidade());
        preparedStatement.setInt(5, (int)produto.getCategoria().getId());
        preparedStatement.setInt(6, (int)produto.getUsuario().getId());
    }
    
    public List<Produto> buscarTodosProdutos()
    {
        String sql = "SELECT e.*, c.nome as categoria_nome, c.descricao as categoria_descricao, u.nome as usuario_nome "
                   + "FROM estoque e "
                   + "LEFT JOIN categoria c ON e.categoria_id = c.id "
                   + "LEFT JOIN usuario u ON e.usuario_id = u.id";
        List<Produto> produtos = new ArrayList<>();
        try
        {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
                    
             while(result.next())
             {
                 produtos.add(getProduto(result));
             }
        }catch (SQLException e)
        {
           System.out.println(String.format("Error: %s", e.getMessage()));
        }
        return produtos;
    }
    
    public Produto buscarPorId(long id)
    {
        String sql = "SELECT e.*, c.nome as categoria_nome, c.descricao as categoria_descricao, u.nome as usuario_nome "
                   + "FROM estoque e "
                   + "LEFT JOIN categoria c ON e.categoria_id = c.id "
                   + "LEFT JOIN usuario u ON e.usuario_id = u.id "
                   + "WHERE e.id = ?";
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();
                    
             if(result.next())
             {
                 return getProduto(result);
             }
        }catch (SQLException e)
        {
           System.out.println(String.format("Error: %s", e.getMessage()));
        }
        return null;
    }
    
    public String excluir(long id)
    {
        String sql = "DELETE FROM estoque WHERE id = ?";
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            
            int resultado = preparedStatement.executeUpdate();
            
            return resultado == 1 ? "Produto excluido com sucesso!" : "Nao foi possivel excluir produto";
        }catch (SQLException e)
        {
            return String.format("Error: %s", e.getMessage());
        }
    }
    
    private Produto getProduto(ResultSet result) throws SQLException
    {
        Produto produto = new Produto();
        
        produto.setId(result.getLong("id"));
        produto.setNome(result.getString("nome"));
        produto.setDescricao(result.getString("descricao"));
        produto.setPreco(result.getBigDecimal("preco"));
        produto.setQuantidade(result.getInt("quantidade"));
        produto.setDataHoraCriacao(result.getObject("data_hora_criacao", LocalDateTime.class));
        
        // Categoria
        Categoria categoria = new Categoria();
        categoria.setId(result.getLong("categoria_id"));
        categoria.setNome(result.getString("categoria_nome"));
        categoria.setDescricao(result.getString("categoria_descricao"));
        produto.setCategoria(categoria);
        
        // Usuario
        Usuario usuario = new Usuario();
        usuario.setId(result.getLong("usuario_id"));
        usuario.setNome(result.getString("usuario_nome"));
        produto.setUsuario(usuario);
        
        return produto;
    }
}



