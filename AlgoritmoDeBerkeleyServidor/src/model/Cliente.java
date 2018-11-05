/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dougl
 */
public class Cliente extends Thread {

    private Horario horario = new Horario();
    private Servidor servidor = Servidor.getInstance();
    private Socket conn;
    private int minutosDiferenca;

    public Cliente(Socket conn) {
        this.conn = conn;        
    }

    @Override
    public void run() {

        PrintWriter out = null;
        BufferedReader in = null;
        try {
            
            conn.setSoTimeout(5000);
            out = new PrintWriter(conn.getOutputStream(), true);
            //Mandando horario do servidor para a aplicação cliente
            out.println(servidor.getHorario().getHora() + ":" + servidor.getHorario().getMinuto());

            //Recebe minutos de diferença
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            minutosDiferenca = Integer.parseInt(in.readLine());

            
            
        } catch (SocketException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }

    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

}
