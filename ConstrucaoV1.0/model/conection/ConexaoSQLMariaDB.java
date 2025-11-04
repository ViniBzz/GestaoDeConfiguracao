/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.model.conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author vinicius
 */
public class ConexaoSQLMariaDB implements Conection
{
    private final static String USUARIO = "root";
    private final static String SENHA = "";
    private final static String URL = "jdbc:mysql://localhost:3306/ClamixGestao?useTimezone=America/Sao_Paulo";
    private Connection conectar;
    
    // useSSL=false&serverTimezone=America/Sao_Paulo
    
    @Override
    public Connection obterConexao() throws SQLException 
    {   
        if(conectar == null)
        {
            conectar = DriverManager.getConnection(URL, USUARIO, SENHA);
        }
        
        return conectar;
    }
    
}
