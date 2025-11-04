/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.clamixgestao.model.conection;

import java.sql.Connection;

import java.sql.SQLException;
/**
 *
 * @author vinicius
 */
public interface Conection 
{
    public Connection obterConexao() throws SQLException;
}
