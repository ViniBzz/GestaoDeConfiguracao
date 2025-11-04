/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.controller;

import com.mycompany.clamixgestao.model.dao.CategoriaDao;
import com.mycompany.clamixgestao.model.dao.ProdutoDao;
import com.mycompany.clamixgestao.model.dominio.Categoria;
import com.mycompany.clamixgestao.model.dominio.Produto;
import com.mycompany.clamixgestao.model.dominio.Usuario;
import com.mycompany.clamixgestao.view.formulario.EstoqueForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author vinicius
 */
public class EstoqueController implements ActionListener 
{
    private final EstoqueForm estoqueForm;
    private ProdutoDao produtoDao;
    private CategoriaDao categoriaDao;
    private Usuario usuarioLogado;
    
    public EstoqueController(EstoqueForm estoqueForm, Usuario usuarioLogado)
    {
        this.estoqueForm = estoqueForm;
        this.produtoDao = new ProdutoDao();
        this.categoriaDao = new CategoriaDao();
        this.usuarioLogado = usuarioLogado;
        carregarCategorias();
        carregarTabela();
    }

    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        String acao = ae.getActionCommand().toLowerCase();
        
        switch(acao)
        {
            case "salvar": salvar(); break;
            case "editar": editar(); break;
            case "excluir": excluir(); break;
            case "limpar": limpar(); break;
            case "buscar": buscar(); break;
        }
    }
    
    private void salvar()
    {
        if (!validarCampos())
        {
            return;
        }
        
        Produto produto = criarProduto();
        String mensagem = produtoDao.salvar(produto);
        
        JOptionPane.showMessageDialog(estoqueForm, mensagem);
        
        if (mensagem.contains("sucesso"))
        {
            limpar();
            carregarTabela();
        }
    }
    
    private void editar()
    {
        int linhaSelecionada = estoqueForm.getTabelaProdutos().getSelectedRow();
        
        if (linhaSelecionada == -1)
        {
            JOptionPane.showMessageDialog(estoqueForm, "Selecione um produto para editar!");
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) estoqueForm.getTabelaProdutos().getModel();
        Long id = (Long) model.getValueAt(linhaSelecionada, 0);
        
        Produto produto = produtoDao.buscarPorId(id);
        
        if (produto != null)
        {
            preencherCampos(produto);
        }
    }
    
    private void excluir()
    {
        int linhaSelecionada = estoqueForm.getTabelaProdutos().getSelectedRow();
        
        if (linhaSelecionada == -1)
        {
            JOptionPane.showMessageDialog(estoqueForm, "Selecione um produto para excluir!");
            return;
        }
        
        int confirmar = JOptionPane.showConfirmDialog(estoqueForm, 
            "Tem certeza que deseja excluir este produto?", 
            "Confirmar exclusão", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmar == JOptionPane.YES_OPTION)
        {
            DefaultTableModel model = (DefaultTableModel) estoqueForm.getTabelaProdutos().getModel();
            Long id = (Long) model.getValueAt(linhaSelecionada, 0);
            
            String mensagem = produtoDao.excluir(id);
            JOptionPane.showMessageDialog(estoqueForm, mensagem);
            
            if (mensagem.contains("sucesso"))
            {
                limpar();
                carregarTabela();
            }
        }
    }
    
    private void limpar()
    {
        estoqueForm.getTxtNome().setText("");
        estoqueForm.getTxtDescricao().setText("");
        estoqueForm.getTxtPreco().setText("");
        estoqueForm.getTxtQuantidade().setText("");
        estoqueForm.getComboCategoria().setSelectedIndex(0);
        estoqueForm.getLabelId().setText("0");
    }
    
    private void buscar()
    {
        String termo = estoqueForm.getTxtBuscar().getText().trim();
        
        if (termo.isEmpty())
        {
            carregarTabela();
            return;
        }
        
        List<Produto> produtos = produtoDao.buscarTodosProdutos();
        DefaultTableModel model = (DefaultTableModel) estoqueForm.getTabelaProdutos().getModel();
        model.setRowCount(0);
        
        for (Produto produto : produtos)
        {
            if (produto.getNome().toLowerCase().contains(termo.toLowerCase()) ||
                produto.getDescricao().toLowerCase().contains(termo.toLowerCase()))
            {
                adicionarLinhaTabela(produto, model);
            }
        }
    }
    
    private boolean validarCampos()
    {
        if (estoqueForm.getTxtNome().getText().trim().isEmpty())
        {
            JOptionPane.showMessageDialog(estoqueForm, "O campo Nome é obrigatório!");
            return false;
        }
        
        if (estoqueForm.getTxtPreco().getText().trim().isEmpty())
        {
            JOptionPane.showMessageDialog(estoqueForm, "O campo Preço é obrigatório!");
            return false;
        }
        
        try
        {
            BigDecimal preco = new BigDecimal(estoqueForm.getTxtPreco().getText().trim());
            if (preco.compareTo(BigDecimal.ZERO) < 0)
            {
                JOptionPane.showMessageDialog(estoqueForm, "O preço não pode ser negativo!");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(estoqueForm, "Preço inválido! Use apenas números.");
            return false;
        }
        
        if (estoqueForm.getTxtQuantidade().getText().trim().isEmpty())
        {
            JOptionPane.showMessageDialog(estoqueForm, "O campo Quantidade é obrigatório!");
            return false;
        }
        
        try
        {
            int quantidade = Integer.parseInt(estoqueForm.getTxtQuantidade().getText().trim());
            if (quantidade < 0)
            {
                JOptionPane.showMessageDialog(estoqueForm, "A quantidade não pode ser negativa!");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(estoqueForm, "Quantidade inválida! Use apenas números inteiros.");
            return false;
        }
        
        if (estoqueForm.getComboCategoria().getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(estoqueForm, "Selecione uma categoria!");
            return false;
        }
        
        return true;
    }
    
    private Produto criarProduto()
    {
        Produto produto = new Produto();
        
        String idTexto = estoqueForm.getLabelId().getText();
        if (!idTexto.isEmpty() && !idTexto.equals("0"))
        {
            produto.setId(Long.parseLong(idTexto));
        }
        
        produto.setNome(estoqueForm.getTxtNome().getText().trim());
        produto.setDescricao(estoqueForm.getTxtDescricao().getText().trim());
        produto.setPreco(new BigDecimal(estoqueForm.getTxtPreco().getText().trim()));
        produto.setQuantidade(Integer.parseInt(estoqueForm.getTxtQuantidade().getText().trim()));
        
        Categoria categoriaSelecionada = (Categoria) estoqueForm.getComboCategoria().getSelectedItem();
        produto.setCategoria(categoriaSelecionada);
        
        produto.setUsuario(usuarioLogado);
        
        return produto;
    }
    
    private void preencherCampos(Produto produto)
    {
        estoqueForm.getLabelId().setText(String.valueOf(produto.getId()));
        estoqueForm.getTxtNome().setText(produto.getNome());
        estoqueForm.getTxtDescricao().setText(produto.getDescricao());
        estoqueForm.getTxtPreco().setText(produto.getPreco().toString());
        estoqueForm.getTxtQuantidade().setText(String.valueOf(produto.getQuantidade()));
        
        // Selecionar categoria no combo
        javax.swing.JComboBox<Categoria> combo = estoqueForm.getComboCategoria();
        for (int i = 0; i < combo.getItemCount(); i++)
        {
            Categoria cat = combo.getItemAt(i);
            if (cat.getId() == produto.getCategoria().getId())
            {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void carregarCategorias()
    {
        List<Categoria> categorias = categoriaDao.buscarTodasCategorias();
        javax.swing.JComboBox<Categoria> combo = estoqueForm.getComboCategoria();
        combo.removeAllItems();
        combo.addItem(new Categoria(0, "Selecione uma categoria", ""));
        
        for (Categoria categoria : categorias)
        {
            combo.addItem(categoria);
        }
    }
    
    private void carregarTabela()
    {
        List<Produto> produtos = produtoDao.buscarTodosProdutos();
        DefaultTableModel model = (DefaultTableModel) estoqueForm.getTabelaProdutos().getModel();
        model.setRowCount(0);
        
        for (Produto produto : produtos)
        {
            adicionarLinhaTabela(produto, model);
        }
    }
    
    private void adicionarLinhaTabela(Produto produto, DefaultTableModel model)
    {
        Object[] linha = {
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getQuantidade(),
            produto.getCategoria().getNome()
        };
        model.addRow(linha);
    }
}




