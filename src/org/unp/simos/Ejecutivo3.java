package org.unp.simos;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Ejecutivo sencillo que modela arribos de tareas y las ejecuta con una 
 * politica FCFS, sin apropiación.
 * @author Francisco E. Paez <franpaez at gmail dot com>
 */
public class Ejecutivo3 {
    
    private static final int MAXTIME = 100;
    
    private PriorityQueue<Evento> colaEventos;
    private Fcfs planificador;
    private int reloj;    
    
    // Tipo de evento
    public enum Tipo { ARRIVO };
    
    // Tiempos de arribo
    private int[] arribos = { 0, 6, 11, 13, 16, 22, 30 };
    // Tiempos de ejecucion
    private int[] ejecucion = { 8, 6, 9, 7, 1, 3, 1 };

    public Ejecutivo3() {
        colaEventos = new PriorityQueue<>(10, new EventoComparator());
        planificador = new Fcfs();
        reloj = 0;
    }
    
    public void runSim() {
        // evento inicial
        colaEventos.add(new Evento(0, reloj, Tipo.ARRIVO));
        
        while (!colaEventos.isEmpty()) {
            Evento evento = colaEventos.poll();            
            reloj = evento.getTime();
            
            evento.accion();
            evento.crearProxEvento();
            
            if (colaEventos.isEmpty()) {                
            } else {                
                planificador.schedule(colaEventos.peek().getTime());
            }
        }        
    }
    
    private class Fcfs {
        private LinkedList<Tarea> colaTareas;

        public Fcfs() {
            colaTareas = new LinkedList<>();
        }
        
        public void add(Tarea tarea) {
            colaTareas.add(tarea);
        }
        
        public void schedule(int time) {
            while (reloj < time) {
                if (colaTareas.isEmpty()) {
                    return;
                }
                
                Tarea tarea = colaTareas.peek();
                if (reloj + tarea.getRuntime() <= time) {
                    reloj = reloj + tarea.getRuntime();
                    colaTareas.poll();
                    System.out.println("t = " + reloj + "\t - Finaliza Tarea " 
                                              + tarea.getId());
                } else {
                    tarea.updateRuntime(time - reloj);
                    reloj = time;
                }
            }
        }
    }
        
    private class Tarea {
        private int id;
        private int runtime;
        
        public Tarea(int id, int runtime) {
            this.id = id;
            this.runtime = runtime;
        }
        
        public int getId() {
            return id;
        }
        
        public int getRuntime() {
            return runtime;
        }
        
        public void updateRuntime(int time) {
            runtime = runtime - time;
        }
    }
    
    /**
     * Evento. Según el tipo del evento, genera un arribo o un desalojo.
     */
    private class Evento {
        
        private int id;
        private int time;
        private Tipo tipo;        
        
        public Evento(int id, int time, Tipo tipo) {
            this.id = id;
            this.time = time;
            this.tipo = tipo;
        }
        
        public int getTime() {
            return time;
        }
        
        public void accion() {       
            System.out.println("t = " + reloj + "\t - Evento " + tipo);
            Evento e;
            
            if (tipo == Tipo.ARRIVO) {
                Tarea tarea = new Tarea(id, ejecucion[id]);
                planificador.add(tarea);       
            }            
        }
        
        public void crearProxEvento() {
            if (tipo == Tipo.ARRIVO) {
                // ¿Hay que generar más eventos arribo?
                if (id + 1 >= arribos.length) {
                    return;
                }
                // Agrego un nuevo evento al ejecutivo
                Evento e = new Evento(id + 1, arribos[id + 1], Tipo.ARRIVO);
                colaEventos.add(e);  
            }
        }
    }
    
    class EventoComparator implements Comparator<Evento> {

        @Override
        public int compare(Evento e1, Evento e2) {            
            return e1.getTime() <= e2.getTime() ? 1 : -1;
        }
    }
}
