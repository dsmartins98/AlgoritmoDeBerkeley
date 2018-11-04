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
public interface Observado {
    
    public void addObservador(Observador obs);
    public void removeObservador(Observador obs);
    public void conectar(String endereco, int porta);
    
}
