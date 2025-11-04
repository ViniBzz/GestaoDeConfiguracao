/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clamixgestao.model.exception;

/**
 *
 * @author vinicius
 */
public class NegocioException extends RuntimeException
{

    public NegocioException(String mensagem) 
    {
        super(mensagem);
    }
    
}
