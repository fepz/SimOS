/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unp.simos;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Ejecutivo muy sencillo que genera eventos de tipo Arribo.
 * @author Francisco E. Paez <franpaez at gmail dot com>
 */
public class Ejecutivo1 {
    
    private static final int MAXTIME = 100;
    
    private PriorityQueue<Evento> colaEventos;
    private int clock;    
    
    public Ejecutivo1() {
        colaEventos = new PriorityQueue<>(10, new EventoComparator());
        clock = 0;
    }    
    
    public void runSim() {
        // evento inicial
        colaEventos.add(new Evento(1, clock));
        
        while (clock < MAXTIME) {
            Evento evento = colaEventos.poll();
            clock = evento.getTime();
            evento.accion();
            colaEventos.add(evento.crearProxEvento());
        }        
    }
    
    private class Evento {
        
        private int id;
        private int time;
        
        public Evento(int id, int time) {
            this.id = id;
            this.time = time;
        }
        
        public int getTime() {
            return time;
        }
        
        public void accion() {
            System.out.println("Soy el evento nro " + id + " en el tiempo " 
                                                         + time);
        }
        
        public Evento crearProxEvento() {
            int delta = (int) (Math.random() * 5);           
            Evento next = new Evento(id + 1, time + delta);
            return next;
        }
        
    }
    
    class EventoComparator implements Comparator<Evento> {

        @Override
        public int compare(Evento e1, Evento e2) {
            if (e1.getTime() <= e2.getTime()) {
                return -1;
            } else {
                return 1;
            }
        }
    }
    
}
