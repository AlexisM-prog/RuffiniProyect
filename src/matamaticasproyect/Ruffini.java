
package matamaticasproyect;

import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JTextArea;


public class Ruffini {
    private int grado;
    private Float[][] mapa;
    private DecimalFormat formatoNum;
    
    public Ruffini(Float raiz, ArrayList<Float> funcion,  DecimalFormat formatoNum ){
        this.grado = funcion.size();
        this.mapa = new Float[this.grado+1][3];
        this.mapa[0][1]= raiz;
        this.formatoNum = formatoNum;
        for(int x = 1 ;x<=this.grado;x++)
            this.mapa[x][0] = Float.parseFloat(formatoNum.format(funcion.get(grado-x)));
    }
    double getImagen(){
        return mapa[grado][2];
    }
    void calcular(){
        this.mapa[1][2] = this.mapa[1][0];
        for(int x = 0;x<grado-1;x++){
            this.mapa[x+2][1] = Float.parseFloat(formatoNum.format(this.mapa[x+1][2]*this.mapa[0][1]));
            this.mapa[x+2][2] = Float.parseFloat(formatoNum.format(this.mapa[x+2][1]+this.mapa[x+2][0]));
        }
    }
    boolean isRaizAprox(){
        this.calcular();
        return Math.abs(mapa[grado][2].compareTo(0f)) < 1;
    }
    boolean isRaiz(){
        this.calcular();
        return mapa[grado][2] == 0.0;
    }
    ArrayList<Float> retornaArray(){
        ArrayList<Float> ret =new ArrayList<Float>();
        for(int x = 1 ;x<this.grado;x++)
            ret.add(mapa[grado-x][2]);
        return ret;
    }
    void mostrar(int numero, JTextArea txtArea){
        txtArea.append("\n");
        txtArea.append("Ruffini numero:"+numero+"\n\n");
        
        for(int y = 0;y<=2;y++){
            for(int x = 0;x<grado+1;x++){
                if(x > 0)
                    txtArea.append("_");
                if(this.mapa[x][y] == null)
                    txtArea.append("| ");
                else
                    txtArea.append("|"+this.mapa[x][y]);
                txtArea.append("\t");
            }
            txtArea.append("\n");
        }
    }
}
