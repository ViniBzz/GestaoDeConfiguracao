/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.controller;

import com.mycompany.clamixgestao.model.dao.UsuarioDao;
import com.mycompany.clamixgestao.model.dominio.Perfil;
import com.mycompany.clamixgestao.model.dominio.Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Controller para gerenciamento de usuários.
 * 
 * IMPORTANTE: Este controller requer que o formulário Usuario tenha os seguintes métodos públicos:
 * - getTxtNome(): JTextField
 * - getTxtUsuario(): JTextField
 * - getTxtSenha(): JPasswordField
 * - getComboPerfil(): JComboBox<Perfil>
 * - getCheckEstado(): JCheckBox
 * - getLabelId(): JLabel
 * - getTxtBuscar(): JTextField
 * - getTabelaUsuarios(): JTable
 * 
 * @author vinicius
 */
public class UsuarioController implements ActionListener 
{
    private final com.mycompany.clamixgestao.view.formulario.Usuario usuarioForm;
    private UsuarioDao usuarioDao;
    
    public UsuarioController(com.mycompany.clamixgestao.view.formulario.Usuario usuarioForm)
    {
        this.usuarioForm = usuarioForm;
        this.usuarioDao = new UsuarioDao();
        carregarComboPerfil();
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
        
        Usuario usuario = criarUsuario();
        String mensagem = usuarioDao.salvar(usuario);
        
        JOptionPane.showMessageDialog(usuarioForm, mensagem);
        
        if (mensagem.contains("sucesso"))
        {
            limpar();
            carregarTabela();
        }
    }
    
    private void editar()
    {
        int linhaSelecionada = usuarioForm.getTabelaUsuarios().getSelectedRow();
        
        if (linhaSelecionada == -1)
        {
            JOptionPane.showMessageDialog(usuarioForm, "Selecione um usuário para editar!");
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) usuarioForm.getTabelaUsuarios().getModel();
        Long id = (Long) model.getValueAt(linhaSelecionada, 0);
        
        Usuario usuario = usuarioDao.buscarPeloUsuarioId(id);
        
        if (usuario != null)
        {
            preencherCampos(usuario);
        }
    }
    
    private void excluir()
    {
        int linhaSelecionada = usuarioForm.getTabelaUsuarios().getSelectedRow();
        
        if (linhaSelecionada == -1)
        {
            JOptionPane.showMessageDialog(usuarioForm, "Selecione um usuário para excluir!");
            return;
        }
        
        int confirmar = JOptionPane.showConfirmDialog(usuarioForm, 
            "Tem certeza que deseja excluir este usuário?", 
            "Confirmar exclusão", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmar == JOptionPane.YES_OPTION)
        {
            DefaultTableModel model = (DefaultTableModel) usuarioForm.getTabelaUsuarios().getModel();
            Long id = (Long) model.getValueAt(linhaSelecionada, 0);
            
            String mensagem = usuarioDao.excluir(id);
            JOptionPane.showMessageDialog(usuarioForm, mensagem);
            
            if (mensagem.contains("sucesso"))
            {
                limpar();
                carregarTabela();
            }
        }
    }
    
    private void limpar()
    {
        usuarioForm.getTxtNome().setText("");
        usuarioForm.getTxtUsuario().setText("");
        usuarioForm.getTxtSenha().setText("");
        usuarioForm.getComboPerfil().setSelectedIndex(0);
        usuarioForm.getCheckEstado().setSelected(true);
        usuarioForm.getLabelId().setText("0");
    }
    
    private void buscar()
    {
        String termo = usuarioForm.getTxtBuscar().getText().trim();
        
        if (termo.isEmpty())
        {
            carregarTabela();
            return;
        }
        
        List<Usuario> usuarios = usuarioDao.buscarTodosUsuarios();
        DefaultTableModel model = (DefaultTableModel) usuarioForm.getTabelaUsuarios().getModel();
        model.setRowCount(0);
        
        for (Usuario usuario : usuarios)
        {
            if (usuario.getNome().toLowerCase().contains(termo.toLowerCase()) ||
                usuario.getUsuario().toLowerCase().contains(termo.toLowerCase()) ||
                usuario.getPerfil().name().toLowerCase().contains(termo.toLowerCase()))
            {
                adicionarLinhaTabela(usuario, model);
            }
        }
    }
    
    private boolean validarCampos()
    {
        if (usuarioForm.getTxtNome().getText().trim().isEmpty())
        {
            JOptionPane.showMessageDialog(usuarioForm, "O campo Nome é obrigatório!");
            return false;
        }
        
        if (usuarioForm.getTxtUsuario().getText().trim().isEmpty())
        {
            JOptionPane.showMessageDialog(usuarioForm, "O campo Usuário é obrigatório!");
            return false;
        }
        
        String idTexto = usuarioForm.getLabelId().getText();
        boolean isNovo = idTexto.isEmpty() || idTexto.equals("0");
        
        if (isNovo && usuarioForm.getTxtSenha().getPassword().length == 0)
        {
            JOptionPane.showMessageDialog(usuarioForm, "O campo Senha é obrigatório!");
            return false;
        }
        
        if (!isNovo && usuarioForm.getTxtSenha().getPassword().length > 0)
        {
            String senha = new String(usuarioForm.getTxtSenha().getPassword());
            if (senha.trim().length() < 3)
            {
                JOptionPane.showMessageDialog(usuarioForm, "A senha deve ter pelo menos 3 caracteres!");
                return false;
            }
        }
        
        return true;
    }
    
    private Usuario criarUsuario()
    {
        Usuario usuario = new Usuario();
        
        String idTexto = usuarioForm.getLabelId().getText();
        if (!idTexto.isEmpty() && !idTexto.equals("0"))
        {
            usuario.setId(Long.parseLong(idTexto));
        }
        
        usuario.setNome(usuarioForm.getTxtNome().getText().trim());
        usuario.setUsuario(usuarioForm.getTxtUsuario().getText().trim());
        
        // Se estiver editando e senha estiver vazia, não atualiza a senha
        String senha = new String(usuarioForm.getTxtSenha().getPassword());
        if (!senha.trim().isEmpty())
        {
            usuario.setSenha(senha);
        }
        else if (usuario.getId() == 0L)
        {
            // Novo usuário precisa de senha
            usuario.setSenha(senha);
        }
        else
        {
            // Editando sem mudar senha - buscar senha atual do banco
            Usuario usuarioAtual = usuarioDao.buscarPeloUsuarioId(usuario.getId());
            if (usuarioAtual != null)
            {
                usuario.setSenha(usuarioAtual.getSenha());
            }
        }
        
        Perfil perfilSelecionado = (Perfil) usuarioForm.getComboPerfil().getSelectedItem();
        usuario.setPerfil(perfilSelecionado);
        usuario.setEstado(usuarioForm.getCheckEstado().isSelected());
        
        return usuario;
    }
    
    private void preencherCampos(Usuario usuario)
    {
        usuarioForm.getLabelId().setText(String.valueOf(usuario.getId()));
        usuarioForm.getTxtNome().setText(usuario.getNome());
        usuarioForm.getTxtUsuario().setText(usuario.getUsuario());
        usuarioForm.getTxtSenha().setText(""); // Não preenche senha por segurança
        usuarioForm.getComboPerfil().setSelectedItem(usuario.getPerfil());
        usuarioForm.getCheckEstado().setSelected(usuario.isEstado());
    }
    
    private void carregarTabela()
    {
        List<Usuario> usuarios = usuarioDao.buscarTodosUsuarios();
        DefaultTableModel model = (DefaultTableModel) usuarioForm.getTabelaUsuarios().getModel();
        model.setRowCount(0);
        
        for (Usuario usuario : usuarios)
        {
            adicionarLinhaTabela(usuario, model);
        }
    }
    
    private void adicionarLinhaTabela(Usuario usuario, DefaultTableModel model)
    {
        Object[] linha = {
            usuario.getId(),
            usuario.getNome(),
            usuario.getUsuario(),
            usuario.getPerfil().name(),
            usuario.isEstado() ? "Ativo" : "Inativo"
        };
        model.addRow(linha);
    }
    
    private void carregarComboPerfil()
    {
        JComboBox<Perfil> comboPerfil = usuarioForm.getComboPerfil();
        comboPerfil.removeAllItems();
        comboPerfil.addItem(Perfil.Admin);
        comboPerfil.addItem(Perfil.Padrao);
    }
}
