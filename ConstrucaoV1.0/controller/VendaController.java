/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.controller;

import com.mycompany.clamixgestao.model.dao.ClienteDao;
import com.mycompany.clamixgestao.model.dao.ProdutoDao;
import com.mycompany.clamixgestao.model.dao.VendaDao;
import com.mycompany.clamixgestao.model.dominio.Cliente;
import com.mycompany.clamixgestao.model.dominio.Produto;
import com.mycompany.clamixgestao.model.dominio.Usuario;
import com.mycompany.clamixgestao.model.dominio.Venda;
import com.mycompany.clamixgestao.model.dominio.VendaItem;
import com.mycompany.clamixgestao.view.formulario.VendaForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author vinicius
 */
public class VendaController implements ActionListener 
{
    private final VendaForm vendaForm;
    private VendaDao vendaDao;
    private ClienteDao clienteDao;
    private ProdutoDao produtoDao;
    private Usuario usuarioLogado;
    private List<VendaItem> itensVenda;
    
    public VendaController(VendaForm vendaForm, Usuario usuarioLogado)
    {
        this.vendaForm = vendaForm;
        this.vendaDao = new VendaDao();
        this.clienteDao = new ClienteDao();
        this.produtoDao = new ProdutoDao();
        this.usuarioLogado = usuarioLogado;
        this.itensVenda = new ArrayList<>();
        carregarClientes();
        carregarProdutos();
        configurarTabelaItens();
        carregarTabelaVendas();
    }

    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        String acao = ae.getActionCommand();
        if (acao == null || acao.isEmpty())
        {
            acao = ae.getSource().toString();
        }
        acao = acao.toLowerCase();
        
        System.out.println("Ação recebida: " + acao);
        
        switch(acao)
        {
            case "adicionar": 
                System.out.println("Chamando adicionarItem...");
                adicionarItem(); 
                break;
            case "remover": 
                System.out.println("Chamando removerItem...");
                removerItem(); 
                break;
            case "finalizar": 
            case "finalizar venda":
                System.out.println("Chamando finalizarVenda...");
                finalizarVenda(); 
                break;
            case "limpar": 
                System.out.println("Chamando limpar...");
                limpar(); 
                break;
            default:
                System.out.println("Ação desconhecida: " + acao);
                break;
        }
    }
    
    private void adicionarItem()
    {
        Produto produto = (Produto) vendaForm.getComboProduto().getSelectedItem();
        if (produto == null || produto.getId() == 0)
        {
            JOptionPane.showMessageDialog(vendaForm, "Selecione um produto!");
            return;
        }
        
        String quantidadeTexto = vendaForm.getTxtQuantidade().getText().trim();
        if (quantidadeTexto.isEmpty())
        {
            JOptionPane.showMessageDialog(vendaForm, "Informe a quantidade!");
            return;
        }
        
        try
        {
            int quantidade = Integer.parseInt(quantidadeTexto);
            if (quantidade <= 0)
            {
                JOptionPane.showMessageDialog(vendaForm, "Quantidade deve ser maior que zero!");
                return;
            }
            
            if (quantidade > produto.getQuantidade())
            {
                JOptionPane.showMessageDialog(vendaForm, "Quantidade insuficiente no estoque!");
                return;
            }
            
            BigDecimal desconto = BigDecimal.ZERO;
            String descontoTexto = vendaForm.getTxtDescontoItem().getText().trim();
            if (!descontoTexto.isEmpty())
            {
                desconto = new BigDecimal(descontoTexto);
            }
            
            BigDecimal total = produto.getPreco().multiply(new BigDecimal(quantidade)).subtract(desconto);
            
            VendaItem item = new VendaItem();
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setDesconto(desconto);
            item.setTotal(total);
            
            itensVenda.add(item);
            atualizarTabelaItens();
            calcularTotais();
            
            vendaForm.getTxtQuantidade().setText("");
            vendaForm.getTxtDescontoItem().setText("");
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(vendaForm, "Valor inválido!");
        }
    }
    
    private void removerItem()
    {
        int linhaSelecionada = vendaForm.getTabelaItens().getSelectedRow();
        
        if (linhaSelecionada == -1)
        {
            JOptionPane.showMessageDialog(vendaForm, "Selecione um item para remover!");
            return;
        }
        
        itensVenda.remove(linhaSelecionada);
        atualizarTabelaItens();
        calcularTotais();
    }
    
    private void finalizarVenda()
    {
        if (itensVenda.isEmpty())
        {
            JOptionPane.showMessageDialog(vendaForm, "Adicione pelo menos um item à venda!");
            return;
        }
        
        Cliente cliente = (Cliente) vendaForm.getComboCliente().getSelectedItem();
        if (cliente != null && cliente.getId() == 0)
        {
            cliente = null; // Cliente opcional
        }
        
        String valorPagoTexto = vendaForm.getTxtValorPago().getText().trim();
        if (valorPagoTexto.isEmpty())
        {
            JOptionPane.showMessageDialog(vendaForm, "Informe o valor pago!");
            return;
        }
        
        try
        {
            BigDecimal valorPago = new BigDecimal(valorPagoTexto);
            BigDecimal totalVenda = calcularTotalVenda();
            BigDecimal descontoGeral = BigDecimal.ZERO;
            
            String descontoTexto = vendaForm.getTxtDesconto().getText().trim();
            if (!descontoTexto.isEmpty())
            {
                descontoGeral = new BigDecimal(descontoTexto);
            }
            
            BigDecimal totalFinal = totalVenda.subtract(descontoGeral);
            
            if (valorPago.compareTo(totalFinal) < 0)
            {
                JOptionPane.showMessageDialog(vendaForm, "Valor pago insuficiente!");
                return;
            }
            
            Venda venda = new Venda();
            venda.setCliente(cliente);
            venda.setUsuario(usuarioLogado);
            venda.setTotalDavenda(totalFinal);
            venda.setValorPago(valorPago);
            venda.setDesconto(descontoGeral);
            venda.setTroco(valorPago.subtract(totalFinal));
            
            System.out.println("Iniciando salvamento da venda...");
            System.out.println("Itens na venda: " + itensVenda.size());
            
            String mensagem = vendaDao.salvar(venda, itensVenda);
            System.out.println("Resultado: " + mensagem);
            
            JOptionPane.showMessageDialog(vendaForm, mensagem);
            
            if (mensagem.contains("sucesso"))
            {
                limpar();
                carregarProdutos(); // Recarregar produtos para atualizar estoque na lista
                carregarTabelaVendas(); // Recarregar tabela de vendas
            }
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(vendaForm, "Valor inválido: " + e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vendaForm, "Erro ao finalizar venda: " + e.getMessage());
        }
    }
    
    private void limpar()
    {
        itensVenda.clear();
        atualizarTabelaItens();
        vendaForm.getComboCliente().setSelectedIndex(0);
        vendaForm.getTxtQuantidade().setText("");
        vendaForm.getTxtDescontoItem().setText("");
        vendaForm.getTxtDesconto().setText("");
        vendaForm.getTxtValorPago().setText("");
        vendaForm.getLabelTotal().setText("R$ 0,00");
        vendaForm.getLabelTroco().setText("R$ 0,00");
    }
    
    private void calcularTotais()
    {
        BigDecimal total = calcularTotalVenda();
        
        BigDecimal descontoGeral = BigDecimal.ZERO;
        String descontoTexto = vendaForm.getTxtDesconto().getText().trim();
        if (!descontoTexto.isEmpty())
        {
            try
            {
                descontoGeral = new BigDecimal(descontoTexto);
            }
            catch (NumberFormatException e)
            {
                // Ignorar
            }
        }
        
        BigDecimal totalFinal = total.subtract(descontoGeral);
        vendaForm.getLabelTotal().setText("R$ " + totalFinal.toString());
        
        String valorPagoTexto = vendaForm.getTxtValorPago().getText().trim();
        if (!valorPagoTexto.isEmpty())
        {
            try
            {
                BigDecimal valorPago = new BigDecimal(valorPagoTexto);
                BigDecimal troco = valorPago.subtract(totalFinal);
                if (troco.compareTo(BigDecimal.ZERO) >= 0)
                {
                    vendaForm.getLabelTroco().setText("R$ " + troco.toString());
                }
                else
                {
                    vendaForm.getLabelTroco().setText("Valor insuficiente");
                }
            }
            catch (NumberFormatException e)
            {
                // Ignorar
            }
        }
        else
        {
            vendaForm.getLabelTroco().setText("R$ 0,00");
        }
    }
    
    private BigDecimal calcularTotalVenda()
    {
        BigDecimal total = BigDecimal.ZERO;
        for (VendaItem item : itensVenda)
        {
            total = total.add(item.getTotal());
        }
        return total;
    }
    
    private void carregarClientes()
    {
        List<Cliente> clientes = clienteDao.buscarTodosClientes();
        javax.swing.JComboBox<Cliente> combo = vendaForm.getComboCliente();
        combo.removeAllItems();
        combo.addItem(new Cliente(0, "Cliente não informado (opcional)", "", "", 0, "", 0));
        
        for (Cliente cliente : clientes)
        {
            combo.addItem(cliente);
        }
    }
    
    private void carregarProdutos()
    {
        List<Produto> produtos = produtoDao.buscarTodosProdutos();
        javax.swing.JComboBox<Produto> combo = vendaForm.getComboProduto();
        combo.removeAllItems();
        combo.addItem(new Produto(0, "Selecione um produto", "", BigDecimal.ZERO, 0, null, null, null));
        
        for (Produto produto : produtos)
        {
            if (produto.getQuantidade() > 0)
            {
                combo.addItem(produto);
            }
        }
    }
    
    private void configurarTabelaItens()
    {
        DefaultTableModel model = new DefaultTableModel();   
        
        model.addColumn("Produto");
        model.addColumn("Quantidade");
        model.addColumn("Preço Unit.");
        model.addColumn("Desconto");
        model.addColumn("Total");
        
        vendaForm.getTabelaItens().setModel(model);
        
        // Garantir que o scroll funcione - a configuração do JScrollPane será feita no formulário
        // através do método setViewportView que já está configurado
    }
    
    private void atualizarTabelaItens()
    {
        DefaultTableModel model = (DefaultTableModel) vendaForm.getTabelaItens().getModel();
        model.setRowCount(0);
        
        for (VendaItem item : itensVenda)
        {
            Object[] linha = {
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getProduto().getPreco(),
                item.getDesconto(),
                item.getTotal()
            };
            model.addRow(linha);
        }
        
        calcularTotais();
    }
    
    public void onDescontoChange()
    {
        calcularTotais();
    }
    
    public void onValorPagoChange()
    {
        calcularTotais();
    }
    
    /**
     * Carrega a tabela de vendas realizadas.
     * Os dados vêm da tabela VENDA (não venda_item).
     * A tabela venda_item é apenas para os itens de cada venda.
     */
    private void carregarTabelaVendas()
    {
        // Buscar todas as vendas da tabela VENDA
        List<Venda> vendas = vendaDao.buscarTodasVendas();
        DefaultTableModel model = (DefaultTableModel) vendaForm.getTabelaVendas().getModel();
        model.setRowCount(0);
        
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Venda venda : vendas)
        {
            String clienteNome = venda.getCliente() != null ? venda.getCliente().getNome() : "Não informado";
            String totalFormatado = "R$ " + df.format(venda.getTotalDavenda());
            String descontoFormatado = "R$ " + df.format(venda.getDesconto() != null ? venda.getDesconto() : BigDecimal.ZERO);
            String valorPagoFormatado = "R$ " + df.format(venda.getValorPago());
            String trocoFormatado = "R$ " + df.format(venda.getTroco() != null ? venda.getTroco() : BigDecimal.ZERO);
            String dataFormatada = venda.getDataHoraDaCriacao() != null ? 
                venda.getDataHoraDaCriacao().format(formatter) : "-";
            
            Object[] linha = {
                venda.getId(),
                clienteNome,
                totalFormatado,
                descontoFormatado,
                valorPagoFormatado,
                trocoFormatado,
                dataFormatada
            };
            model.addRow(linha);
        }
        
        // Forçar atualização do scroll após carregar dados usando SwingUtilities
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Forçar atualização da tabela
                vendaForm.getTabelaVendas().revalidate();
                vendaForm.getTabelaVendas().repaint();
                
                // Forçar atualização do JScrollPane e viewport
                javax.swing.JScrollPane scrollPane = vendaForm.getJScrollPane2();
                scrollPane.getViewport().revalidate();
                scrollPane.getViewport().repaint();
                scrollPane.revalidate();
                scrollPane.repaint();
                
                // Forçar cálculo do scrollbar
                scrollPane.getVerticalScrollBar().setEnabled(true);
                if (model.getRowCount() > 0) {
                    scrollPane.getVerticalScrollBar().setVisible(true);
                }
            }
        });
    }
}


