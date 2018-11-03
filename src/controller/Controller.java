/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Configuracao;

/**
 *
 * @author dougl
 */
public class Controller implements Observado {

    private List<Observador> observadores = new ArrayList<>();
    private Configuracao configuracao;
    private Socket conn = null;
    private BufferedReader in = null;
    private PrintWriter out;

    @Override
    public void addObservador(Observador obs) {
        observadores.add(obs);
    }

    @Override
    public void removeObservador(Observador obs) {
        observadores.remove(obs);
    }

    @Override
    public void conectar(String endereco, int porta) {
        //Setando configurações da conexão
        configuracao = new Configuracao(endereco, porta);
        atualizaStatus("Tentando conectar...");
        try {
            conn = new Socket(configuracao.getEndereco(), configuracao.getPorta());
            atualizaStatus("Conectado!");
            
            //realizar aqui um loop infinito para receber e enviar dados do servidor
            
        } catch (IOException ex) {
            atualizaStatus("Não foi possível conectar!");
            exibirErro(ex.getMessage());
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaStatus(String status) {
        for (Observador obs : observadores) {
            obs.atualizaStatus(status);
        }
    }

    private void exibirErro(String erro) {
        for (Observador obs : observadores) {
            obs.exibirErro(erro);
        }
    }

}
