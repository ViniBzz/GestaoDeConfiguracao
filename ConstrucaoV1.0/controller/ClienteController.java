/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.controller;

import com.mycompany.clamixgestao.model.dao.ClienteDao;
import com.mycompany.clamixgestao.model.dominio.Cliente;
import com.mycompany.clamixgestao.view.formulario.ClienteForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author vinicius
 */
public class ClienteController implements ActionListener 
{
    private final ClienteForm clienteForm;
    private ClienteDao clienteDao;
    
    public ClienteController(ClienteForm clienteForm)
    {
        this.clienteForm = clienteForm;
        this.clienteDao = new ClienteDao();
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
        
        Cliente cliente = criarCliente();
        String mensagem = clienteDao.salvar(cliente);
        
        JOptionPane.showMessageDialog(clienteForm, mensagem);
        
        if (mensagem.contains("sucesso"))
        {
            limpar();
            carregarTabela();
        }
    }
    
    private void editar()
    {
        int linhaSelecionada = clienteForm.getTabelaClientes().getSelectedRow();
        
        if (linhaSelecionada == -1)
        {
            JOptionPane.showMessageDialog(clienteForm, "Selecione um cliente para editar!");
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) clienteForm.getTabelaClientes().getModel();
        Long id = (Long) model.getValueAt(linhaSelecionada, 0);
        
        Cliente cliente = clienteDao.buscarPorId(id);
        
        if (cliente != null)
        {
            preencherCampos(cliente);
        }
    }
    
    private void excluir()
    {
        int linhaSelecionada = clienteForm.getTabelaClientes().getSelectedRow();
        
        if (linhaSelecionada == -1)
        {
            JOptionPane.showMessageDialog(clienteForm, "Selecione um cliente para excluir!");
            return;
        }
        
        int confirmar = JOptionPane.showConfirmDialog(clienteForm, 
            "Tem certeza que deseja excluir este cliente?", 
            "Confirmar exclusão", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmar == JOptionPane.YES_OPTION)
        {
            DefaultTableModel model = (DefaultTableModel) clienteForm.getTabelaClientes().getModel();
            Long id = (Long) model.getValueAt(linhaSelecionada, 0);
            
            String mensagem = clienteDao.excluir(id);
            JOptionPane.showMessageDialog(clienteForm, mensagem);
            
            if (mensagem.contains("sucesso"))
            {
                limpar();
                carregarTabela();
            }
        }
    }
    
    private void limpar()
    {
        clienteForm.getTxtNome().setText("");
        clienteForm.getTxtTelefone().setText("");
        clienteForm.getTxtEmail().setText("");
        clienteForm.getTxtCPF().setText("");
        clienteForm.getTxtCNPJ().setText("");
        clienteForm.getTxtCEP().setText("");
        clienteForm.getLabelId().setText("0");
    }
    
    private void buscar()
    {
        String termo = clienteForm.getTxtBuscar().getText().trim();
        
        if (termo.isEmpty())
        {
            carregarTabela();
            return;
        }
        
        List<Cliente> clientes = clienteDao.buscarTodosClientes();
        DefaultTableModel model = (DefaultTableModel) clienteForm.getTabelaClientes().getModel();
        model.setRowCount(0);
        
        for (Cliente cliente : clientes)
        {
            if (cliente.getNome().toLowerCase().contains(termo.toLowerCase()) ||
                cliente.getEmail().toLowerCase().contains(termo.toLowerCase()) ||
                String.valueOf(cliente.getCPF()).contains(termo))
            {
                adicionarLinhaTabela(cliente, model);
            }
        }
    }
    
    private boolean validarCampos()
    {
        if (clienteForm.getTxtNome().getText().trim().isEmpty())
        {
            JOptionPane.showMessageDialog(clienteForm, "O campo Nome é obrigatório!");
            return false;
        }
        
        if (clienteForm.getTxtCPF().getText().trim().isEmpty())
        {
            JOptionPane.showMessageDialog(clienteForm, "O campo CPF é obrigatório!");
            return false;
        }
        
        try
        {
            Long.parseLong(clienteForm.getTxtCPF().getText().trim());
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(clienteForm, "CPF inválido! Use apenas números.");
            return false;
        }
        
        if (!clienteForm.getTxtCEP().getText().trim().isEmpty())
        {
            try
            {
                Long.parseLong(clienteForm.getTxtCEP().getText().trim());
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(clienteForm, "CEP inválido! Use apenas números.");
                return false;
            }
        }
        
        return true;
    }
    
    private Cliente criarCliente()
    {
        Cliente cliente = new Cliente();
        
        String idTexto = clienteForm.getLabelId().getText();
        if (!idTexto.isEmpty() && !idTexto.equals("0"))
        {
            cliente.setId(Long.parseLong(idTexto));
        }
        
        cliente.setNome(clienteForm.getTxtNome().getText().trim());
        cliente.setTelefone(clienteForm.getTxtTelefone().getText().trim());
        cliente.setEmail(clienteForm.getTxtEmail().getText().trim());
        cliente.setCPF(Long.parseLong(clienteForm.getTxtCPF().getText().trim()));
        cliente.setCNPJ(clienteForm.getTxtCNPJ().getText().trim());
        
        String cepTexto = clienteForm.getTxtCEP().getText().trim();
        if (!cepTexto.isEmpty())
        {
            cliente.setCEP(Long.parseLong(cepTexto));
        }
        else
        {
            cliente.setCEP(0L);
        }
        
        return cliente;
    }
    
    private void preencherCampos(Cliente cliente)
    {
        clienteForm.getLabelId().setText(String.valueOf(cliente.getId()));
        clienteForm.getTxtNome().setText(cliente.getNome());
        clienteForm.getTxtTelefone().setText(cliente.getTelefone());
        clienteForm.getTxtEmail().setText(cliente.getEmail());
        clienteForm.getTxtCPF().setText(String.valueOf(cliente.getCPF()));
        clienteForm.getTxtCNPJ().setText(cliente.getCNPJ() != null ? cliente.getCNPJ() : "");
        clienteForm.getTxtCEP().setText(String.valueOf(cliente.getCEP()));
    }
    
    private void carregarTabela()
    {
        List<Cliente> clientes = clienteDao.buscarTodosClientes();
        DefaultTableModel model = (DefaultTableModel) clienteForm.getTabelaClientes().getModel();
        model.setRowCount(0);
        
        for (Cliente cliente : clientes)
        {
            adicionarLinhaTabela(cliente, model);
        }
    }
    
    private void adicionarLinhaTabela(Cliente cliente, DefaultTableModel model)
    {
        Object[] linha = {
            cliente.getId(),
            cliente.getNome(),
            cliente.getTelefone(),
            cliente.getEmail(),
            cliente.getCPF(),
            cliente.getCNPJ() != null && !cliente.getCNPJ().isEmpty() ? cliente.getCNPJ() : "-"
        };
        model.addRow(linha);
    }
}

