/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author dougl
 */
public interface Observador {
    
    public void exibirErro(String erro);
    public void inserirHorarioAtual(String horario);
    
}
