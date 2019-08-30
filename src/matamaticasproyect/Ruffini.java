
package matamaticasproyect;

import java.util.ArrayList;


public class Ruffini {
    private int grado;
    private Double[][] mapa;

    
    public Ruffini(double raiz, ArrayList<Double> funcion){
        this.grado = funcion.size();
        this.mapa = new Double[this.grado+1][3];
        this.mapa[0][1]= raiz;
        for(int x = 1 ;x<=this.grado;x++){
            this.mapa[x][0] = funcion.get(grado-x);
        }
    }
    double getImagen(){
        return mapa[grado][2];
    }
    void calcular(){
        this.mapa[1][2] = this.mapa[1][0];
        for(int x = 0;x<grado-1;x++){
            this.mapa[x+2][1] = this.mapa[x+1][2]*this.mapa[0][1];
            this.mapa[x+2][2] = this.mapa[x+2][1]+this.mapa[x+2][0];
        }
    }
    boolean isRaizAprox(){
        this.calcular();
        return Math.abs(mapa[grado][2].compareTo(0.0)) < 1;
    }
    boolean isRaiz(){
        this.calcular();
        return mapa[grado][2] == 0.0;
    }
    ArrayList<Double> retornaArray(){
        ArrayList<Double> ret =new ArrayList<Double>();
        for(int x = 1 ;x<this.grado;x++){
            ret.add(mapa[grado-x][2]);
        }
        return ret;
    }
    void mostrar(int numero){
        System.out.println("----------");
        System.out.println("Ruffini nro:"+numero);
        System.out.println("----------");
        
        for(int y = 0;y<=2;y++){
            for(int x = 0;x<grado+1;x++){
                System.out.print(this.mapa[x][y]+"|");
            }
            System.out.println();
        }
    }
}
