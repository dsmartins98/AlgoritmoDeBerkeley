/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Cliente;
import model.Horario;
import model.Servidor;

/**
 *
 * @author dougl
 */
public class Controller implements Observado {

    private static List<Observador> observadores = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();
    private static Servidor servidor = Servidor.getInstance();    

    @Override
    public void addObservador(Observador obs) {
        observadores.add(obs);
    }

    @Override
    public void removeObservador(Observador obs) {
        observadores.remove(obs);
    }

    @Override
    public void conectar(String porta) {

        int portaTCP = Integer.parseInt(porta);        

        try {

            ServerSocket server = new ServerSocket(portaTCP);
            server.setReuseAddress(true);

            while (true) {

                System.out.println("Aguardando conexão com cliente...");
                Socket conn = server.accept();

                //criar Thread para realizar a comunicação
                Cliente cliente = new Cliente(conn);                
                cliente.start();
                clientes.add(cliente);

            }

        } catch (IOException ex) {
            exibirErro(ex.getMessage());
        }

    }

    public static void iniciarHorario() {
        String hora = new SimpleDateFormat("HH").format(new Date());
        String minuto = new SimpleDateFormat("mm").format(new Date());
        String segundo = new SimpleDateFormat("ss").format(new Date());
        servidor.setHorario(new Horario(Integer.parseInt(hora), Integer.parseInt(minuto), Integer.parseInt(segundo)));        
        String horarioAtual = servidor.getHorario().getHora() + ":" + servidor.getHorario().getMinuto() + ":" + servidor.getHorario().getSegundo();
        observadores.get(0).inserirHorarioAtual(horarioAtual);
    }

    private void exibirErro(String erro) {
        observadores.forEach((obs) -> {
            obs.exibirErro(erro);
        });
    }

}
