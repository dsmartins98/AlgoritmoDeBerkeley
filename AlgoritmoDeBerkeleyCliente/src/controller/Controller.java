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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Configuracao;

/**
 *
 * @author dougl
 */
public class Controller implements Observado {

    private static List<Observador> observadores = new ArrayList<>();
    private Configuracao configuracao;
    private Socket conn = null;
    private BufferedReader in = null;
    private PrintWriter out;
    private static String horaAtual;
    private static String minutoAtual;
    private static String segundoAtual;

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
            liberarCamposNovoHorario();
            while (true) {

                // 1 - Realiza a coleta do horario que o servidor esta enviando
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                //Recuperando o horário que o servidor envia
                String horarioServidor = in.readLine();
                int horaServidor = Integer.parseInt(horarioServidor.split(":")[0]);
                int minutoServidor = Integer.parseInt(horarioServidor.split(":")[1]);

                // 2 - Envia a diferença em minutos dos dois horários (Cliente e Servidor)
                out = new PrintWriter(conn.getOutputStream(), true);
                out.print(calcularDiferencaTempo(horaServidor, minutoServidor));

                //Espera receber a média em minutos para ajustar o relógio
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                int mediaMinutos = Integer.parseInt(in.readLine());

                // 3 - Ajusta o relógio com base nos minutos que o servidor retornou
                atualizarHorario(mediaMinutos);

            }

        } catch (IOException ex) {
            atualizaStatus("Não foi possível conectar!");
            exibirErro(ex.getMessage());
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void iniciarHorario() {
        String hora = new SimpleDateFormat("HH").format(new Date());
        String minuto = new SimpleDateFormat("mm").format(new Date());
        String segundo = new SimpleDateFormat("ss").format(new Date());
        horaAtual = hora;
        minutoAtual = minuto;
        segundoAtual = segundo;
        String horarioAtual = horaAtual + ":" + minutoAtual + ":" + segundoAtual;
        observadores.get(0).inserirHorarioAtual(horarioAtual);
    }

    //Este método calcula a diferença de tempo entre o horário do servidor com
    //o horário da máquina cliente
    //* retorno em minutos
    private int calcularDiferencaTempo(int horaServidor, int minutoServidor) {
        int diferenca = 0;
        String horarioServidor = horaServidor + ":" + minutoServidor;
        String horarioCliente = horaAtual + ":" + minutoAtual;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date horaServ = sdf.parse(horarioServidor);
            Date horaCliente = sdf.parse(horarioCliente);
            long horaS = horaServ.getTime();
            long horaC = horaCliente.getTime();
            diferenca = (int) (horaS - horaC) / 1000 / 60;
            System.out.println("Diferença: " + diferenca + " minutos");
        } catch (ParseException ex) {
            exibirErro("Wow! Não foi possível calcular a diferença de horario. \n\n" + ex.getMessage());
        }
        return diferenca;
    }

    //Este método é o ultimo passo do Algoritmo de Berkeley no lado do cliente.
    //Aqui, uma quantidade de minutos (positivo ou negativo) é passada por parâmetro
    //para que o horário do cliente possa se ajustar com este valor
    private void atualizarHorario(int minutosRecebidos) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(0, 0, 0, Integer.parseInt(horaAtual), Integer.parseInt(minutoAtual), Integer.parseInt(segundoAtual));
        gc.add(Calendar.MINUTE, minutosRecebidos);
        horaAtual = new SimpleDateFormat("HH").format(gc.getTime());
        minutoAtual = new SimpleDateFormat("mm").format(gc.getTime());
        segundoAtual = new SimpleDateFormat("ss").format(gc.getTime());
//        System.out.println(horaAtual + ":" + minutoAtual + ":" + segundoAtual);
        atualizaStatus("Um novo horário foi definido!");
    }

    private void atualizaStatus(String status) {
        observadores.forEach((obs) -> {
            obs.atualizaStatus(status);
        });
    }

    private void exibirErro(String erro) {
        observadores.forEach((obs) -> {
            obs.exibirErro(erro);
        });
    }

    private void liberarCamposNovoHorario() {
        observadores.forEach((obs) -> {
            obs.liberarCamposNovoHorario();
        });
    }

}
