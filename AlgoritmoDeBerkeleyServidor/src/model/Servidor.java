/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author dougl
 */
public class Servidor {

    private Horario horario;
    private static Servidor instance;
    

    private Servidor(){}
    
    public synchronized static Servidor getInstance() {
        if (instance == null) {
            instance = new Servidor();
        }
        return instance;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

}
