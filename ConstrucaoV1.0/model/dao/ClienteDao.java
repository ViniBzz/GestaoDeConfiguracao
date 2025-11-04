/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.model.dao;

import com.mycompany.clamixgestao.model.conection.Conection;
import com.mycompany.clamixgestao.model.conection.ConexaoSQLMariaDB;
import com.mycompany.clamixgestao.model.dominio.Cliente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vinicius
 */
public class ClienteDao 
{
    private final Conection conexao;
    
    public ClienteDao()
    {
        this.conexao = new ConexaoSQLMariaDB();
    }
    
    public String salvar(Cliente cliente)
    {
        return cliente.getId() == 0L ? adicionar(cliente) : editar(cliente);
    }
    
    private String adicionar(Cliente cliente)
    {
        String sql = "INSERT INTO cliente(nome, telefone, email, CPF, CNPJ, CEP) VALUES(?, ?, ?, ?, ?, ?)";
        
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            
            preencheValorPreparedStatement(preparedStatement, cliente);
            
            int resultado = preparedStatement.executeUpdate();
            
            return resultado == 1 ? "Cliente adicionado com sucesso!" : "Nao foi possivel adicionar cliente";
        }catch (SQLException e)
        {
            return String.format("Error: %s", e.getMessage());
        }
    }
    
    private String editar(Cliente cliente)
    {
        String sql = "UPDATE cliente SET nome = ?, telefone = ?, email = ?, CPF = ?, CNPJ = ?, CEP = ? WHERE id = ?";
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            
            preencheValorPreparedStatement(preparedStatement, cliente);
            preparedStatement.setLong(7, cliente.getId());
            
            int resultado = preparedStatement.executeUpdate();
            
            return resultado == 1 ? "Cliente editado com sucesso!" : "Nao foi possivel editar cliente";
        }catch (SQLException e)
        {
            return String.format("Error: %s", e.getMessage());
        }
    }
    
    private void preencheValorPreparedStatement(PreparedStatement preparedStatement, Cliente cliente) throws SQLException
    {
        preparedStatement.setString(1, cliente.getNome());
        preparedStatement.setLong(2, cliente.getTelefone() != null && !cliente.getTelefone().isEmpty() ? Long.parseLong(cliente.getTelefone()) : 0L);
        preparedStatement.setString(3, cliente.getEmail());
        preparedStatement.setLong(4, cliente.getCPF());
        
        if (cliente.getCNPJ() != null && !cliente.getCNPJ().isEmpty())
        {
            preparedStatement.setString(5, cliente.getCNPJ());
        }
        else
        {
            preparedStatement.setString(5, null);
        }
        
        preparedStatement.setLong(6, cliente.getCEP());
    }
    
    public List<Cliente> buscarTodosClientes()
    {
        String sql = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();
        try
        {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
                    
             while(result.next())
             {
                 clientes.add(getCliente(result));
             }
        }catch (SQLException e)
        {
           System.out.println(String.format("Error: %s", e.getMessage()));
        }
        return clientes;
    }
    
    public Cliente buscarPorId(long id)
    {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();
                    
             if(result.next())
             {
                 return getCliente(result);
             }
        }catch (SQLException e)
        {
           System.out.println(String.format("Error: %s", e.getMessage()));
        }
        return null;
    }
    
    public String excluir(long id)
    {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            
            int resultado = preparedStatement.executeUpdate();
            
            return resultado == 1 ? "Cliente excluido com sucesso!" : "Nao foi possivel excluir cliente";
        }catch (SQLException e)
        {
            return String.format("Error: %s", e.getMessage());
        }
    }
    
    private Cliente getCliente(ResultSet result) throws SQLException
    {
        Cliente cliente = new Cliente();
        
        cliente.setId(result.getLong("id"));
        cliente.setNome(result.getString("nome"));
        
        Long telefone = result.getLong("telefone");
        cliente.setTelefone(telefone != null && telefone > 0 ? String.valueOf(telefone) : "");
        
        cliente.setEmail(result.getString("email"));
        cliente.setCPF(result.getLong("CPF"));
        
        String cnpj = result.getString("CNPJ");
        cliente.setCNPJ(cnpj != null ? cnpj : "");
        
        cliente.setCEP(result.getLong("CEP"));
        
        return cliente;
    }
}


