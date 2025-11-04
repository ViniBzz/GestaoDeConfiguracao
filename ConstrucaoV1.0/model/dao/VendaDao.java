/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.model.dao;

import com.mycompany.clamixgestao.model.conection.Conection;
import com.mycompany.clamixgestao.model.conection.ConexaoSQLMariaDB;
import com.mycompany.clamixgestao.model.dominio.Cliente;
import com.mycompany.clamixgestao.model.dominio.Produto;
import com.mycompany.clamixgestao.model.dominio.Usuario;
import com.mycompany.clamixgestao.model.dominio.Venda;
import com.mycompany.clamixgestao.model.dominio.VendaItem;
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
public class VendaDao 
{
    private final Conection conexao;
    
    public VendaDao()
    {
        this.conexao = new ConexaoSQLMariaDB();
    }
    
    public String salvar(Venda venda, List<VendaItem> itens)
    {
        try
        {
            conexao.obterConexao().setAutoCommit(false);
            
            // Salvar venda
            String sqlVenda = "INSERT INTO venda(cliente_id, usuario_id, total_venda, valor_pago, desconto, troco) VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement psVenda = conexao.obterConexao().prepareStatement(sqlVenda, PreparedStatement.RETURN_GENERATED_KEYS);
            
            if (venda.getCliente() != null && venda.getCliente().getId() > 0)
            {
                psVenda.setLong(1, venda.getCliente().getId());
            }
            else
            {
                psVenda.setObject(1, null);
            }
            
            psVenda.setInt(2, (int)venda.getUsuario().getId());
            psVenda.setBigDecimal(3, venda.getTotalDavenda());
            psVenda.setBigDecimal(4, venda.getValorPago());
            psVenda.setBigDecimal(5, venda.getDesconto());
            psVenda.setBigDecimal(6, venda.getTroco());
            
            psVenda.executeUpdate();
            
            // Obter ID da venda criada
            ResultSet rs = psVenda.getGeneratedKeys();
            long vendaId = 0;
            if (rs.next())
            {
                vendaId = rs.getLong(1);
            }
            
            // Salvar itens da venda e descontar do estoque
            String sqlItem = "INSERT INTO venda_item(venda_id, produto_id, quantidade, total, desconto) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement psItem = conexao.obterConexao().prepareStatement(sqlItem);
            
            // Atualizar estoque
            String sqlAtualizarEstoque = "UPDATE estoque SET quantidade = quantidade - ? WHERE id = ?";
            PreparedStatement psEstoque = conexao.obterConexao().prepareStatement(sqlAtualizarEstoque);
            
            for (VendaItem item : itens)
            {
                // Inserir item da venda
                psItem.setInt(1, (int)vendaId);
                psItem.setLong(2, item.getProduto().getId());
                psItem.setInt(3, item.getQuantidade());
                psItem.setBigDecimal(4, item.getTotal());
                psItem.setBigDecimal(5, item.getDesconto());
                psItem.addBatch();
                
                // Descontar do estoque
                psEstoque.setInt(1, item.getQuantidade());
                psEstoque.setLong(2, item.getProduto().getId());
                psEstoque.addBatch();
            }
            
            System.out.println("Executando batch de itens...");
            int[] resultadosItem = psItem.executeBatch();
            System.out.println("Itens salvos: " + resultadosItem.length);
            
            System.out.println("Executando batch de estoque...");
            int[] resultadosEstoque = psEstoque.executeBatch();
            System.out.println("Estoques atualizados: " + resultadosEstoque.length);
            
            // Verificar se todos os itens foram salvos
            for (int i = 0; i < resultadosItem.length; i++)
            {
                if (resultadosItem[i] == PreparedStatement.EXECUTE_FAILED)
                {
                    throw new SQLException("Falha ao salvar item " + (i+1) + " da venda (EXECUTE_FAILED)");
                }
            }
            
            // Verificar se o estoque foi atualizado
            for (int i = 0; i < resultadosEstoque.length; i++)
            {
                if (resultadosEstoque[i] == PreparedStatement.EXECUTE_FAILED)
                {
                    throw new SQLException("Falha ao atualizar estoque do produto " + (i+1) + " (EXECUTE_FAILED)");
                }
                System.out.println("Estoque produto " + (i+1) + " atualizado. Resultado: " + resultadosEstoque[i]);
            }
            
            conexao.obterConexao().commit();
            return "Venda realizada com sucesso!";
        }
        catch (SQLException e)
        {
            try
            {
                conexao.obterConexao().rollback();
            }
            catch (SQLException ex)
            {
                e.printStackTrace();
                return String.format("Erro ao reverter transação: %s", ex.getMessage());
            }
            e.printStackTrace();
            return String.format("Erro ao salvar venda: %s", e.getMessage());
        }
        finally
        {
            try
            {
                conexao.obterConexao().setAutoCommit(true);
            }
            catch (SQLException e)
            {
                // Ignorar
            }
        }
    }
    
    /**
     * Busca todas as vendas da tabela VENDA (não venda_item).
     * A tabela venda_item é apenas para armazenar os itens de cada venda.
     */
    public List<Venda> buscarTodasVendas()
    {
        // Query busca EXCLUSIVAMENTE da tabela VENDA (não venda_item)
        // IMPORTANTE: venda_item é apenas para os itens, não para a venda em si
        String sql = "SELECT v.id, v.cliente_id, v.usuario_id, v.total_venda, v.valor_pago, v.desconto, v.troco, "
                   + "v.data_hora, v.ultima_atualiza, "
                   + "c.nome as cliente_nome, u.nome as usuario_nome "
                   + "FROM venda v "
                   + "LEFT JOIN cliente c ON v.cliente_id = c.id "
                   + "LEFT JOIN usuario u ON v.usuario_id = u.id "
                   + "ORDER BY v.data_hora DESC";
        
        System.out.println("=== BUSCANDO VENDAS DA TABELA 'venda' ===");
        System.out.println("SQL: " + sql);
        
        List<Venda> vendas = new ArrayList<>();
        try
        {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
            
            int contador = 0;
            while(result.next())
            {
                contador++;
                vendas.add(getVenda(result));
            }
            
            System.out.println("Total de vendas encontradas na tabela 'venda': " + contador);
        }catch (SQLException e)
        {
           System.out.println(String.format("Error ao buscar vendas: %s", e.getMessage()));
           e.printStackTrace();
        }
        return vendas;
    }
    
    public Venda buscarPorId(long id)
    {
        String sql = "SELECT v.*, c.nome as cliente_nome, u.nome as usuario_nome "
                   + "FROM venda v "
                   + "LEFT JOIN cliente c ON v.cliente_id = c.id "
                   + "LEFT JOIN usuario u ON v.usuario_id = u.id "
                   + "WHERE v.id = ?";
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();
                    
             if(result.next())
             {
                 return getVenda(result);
             }
        }catch (SQLException e)
        {
           System.out.println(String.format("Error: %s", e.getMessage()));
        }
        return null;
    }
    
    public List<VendaItem> buscarItensVenda(long vendaId)
    {
        String sql = "SELECT vi.*, e.nome as produto_nome, e.preco as produto_preco "
                   + "FROM venda_item vi "
                   + "LEFT JOIN estoque e ON vi.produto_id = e.id "
                   + "WHERE vi.venda_id = ?";
        List<VendaItem> itens = new ArrayList<>();
        try
        {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            preparedStatement.setLong(1, vendaId);
            ResultSet result = preparedStatement.executeQuery();
                    
             while(result.next())
             {
                 itens.add(getVendaItem(result));
             }
        }catch (SQLException e)
        {
           System.out.println(String.format("Error: %s", e.getMessage()));
        }
        return itens;
    }
    
    private Venda getVenda(ResultSet result) throws SQLException
    {
        Venda venda = new Venda();
        
        venda.setId(result.getLong("id"));
        venda.setTotalDavenda(result.getBigDecimal("total_venda"));
        venda.setValorPago(result.getBigDecimal("valor_pago"));
        venda.setDesconto(result.getBigDecimal("desconto"));
        venda.setTroco(result.getBigDecimal("troco"));
        venda.setDataHoraDaCriacao(result.getObject("data_hora", LocalDateTime.class));
        venda.setUltimaAtualizacao(result.getObject("ultima_atualiza", LocalDateTime.class));
        
        // Cliente (pode ser null)
        Long clienteId = result.getLong("cliente_id");
        if (!result.wasNull() && clienteId > 0)
        {
            Cliente cliente = new Cliente();
            cliente.setId(clienteId);
            cliente.setNome(result.getString("cliente_nome"));
            venda.setCliente(cliente);
        }
        else
        {
            venda.setCliente(null);
        }
        
        // Usuario
        Usuario usuario = new Usuario();
        usuario.setId(result.getLong("usuario_id"));
        usuario.setNome(result.getString("usuario_nome"));
        venda.setUsuario(usuario);
        
        return venda;
    }
    
    private VendaItem getVendaItem(ResultSet result) throws SQLException
    {
        VendaItem item = new VendaItem();
        
        item.setQuantidade(result.getInt("quantidade"));
        item.setTotal(result.getBigDecimal("total"));
        item.setDesconto(result.getBigDecimal("desconto"));
        
        // Produto
        Produto produto = new Produto();
        produto.setId(result.getLong("produto_id"));
        produto.setNome(result.getString("produto_nome"));
        produto.setPreco(result.getBigDecimal("produto_preco"));
        item.setProduto(produto);
        
        return item;
    }
}


