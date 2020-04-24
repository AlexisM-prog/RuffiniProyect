package matamaticasproyect;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import javax.swing.JTextArea;

/**
 * @author alex
 */
public class Bases {
        
    private ArrayList<Float> numeros;
    private Float[] raices;
    private String[] raicesRet;
    private boolean cambios;
    private int numeroRaiz;
    private JTextArea txtArea;
    private DecimalFormat formatoNum = new DecimalFormat("#.00");
    
    public Bases(ArrayList<Float> numeros, JTextArea txtArea){
        DecimalFormatSymbols separador = new DecimalFormatSymbols();
        separador.setDecimalSeparator('.');
        this.formatoNum = new DecimalFormat("#.00", separador);
        this.numeros = numeros;
        this.cambios = false;
        this.numeroRaiz = 0;
        this.txtArea = txtArea;
    }
    public Bases(JTextArea txtArea) {
        DecimalFormatSymbols separador = new DecimalFormatSymbols();
        separador.setDecimalSeparator('.');
        this.formatoNum = new DecimalFormat("#.00", separador);        
        this.numeros = new ArrayList<>();
        this.cambios = false;
        this.numeroRaiz = 0;
        this.txtArea = txtArea;
    }
    protected int getSize(){
        return numeros.size();
    }  
    public String fraccionToString(Float dividendo, Float divisor){
        if(dividendo % divisor == 0)
            return dividendo/divisor+"";
        if(dividendo < 0 && divisor < 0)
            return -dividendo+"/"+(-divisor);
        if(dividendo < 0 || divisor < 0){
            if(dividendo < 0)
                return "-"+(-dividendo)+"/"+divisor;
            else
                return "-"+dividendo+"/"+(-divisor);
        }
        return dividendo+"/"+divisor;
    }
    public void defRaices(){
       this.raices = new Float[numeros.size()-1];
       this.raicesRet = new String[numeros.size()-1];
    }

    public String[] getRaices(){   
        
        ArrayList<Integer> ti =  divisores(numeros.get(0));
        ArrayList<Integer> cp =  divisores(numeros.get(numeros.size()-1));
        
        Ruffini map, mapPos, mapNeg;
 
        for(int x = 0;x<ti.size() && !cambios;x++){
            for(int y = 0;y<cp.size() && !cambios;y++){
                mapPos = new Ruffini(ti.get(x)/cp.get(y).floatValue(), this.numeros, this.formatoNum);
                mapNeg = new Ruffini(-ti.get(x)/cp.get(y).floatValue(), this.numeros, this.formatoNum);
             
               if(mapPos.isRaiz()){
                    raices[numeroRaiz] = (float)ti.get(x)/cp.get(y);
                    upgradeNumeros(new Ruffini(raices[numeroRaiz],this.numeros, this.formatoNum),numeroRaiz);
                    raicesRet[numeroRaiz++] = this.fraccionToString((float)ti.get(x),(float)cp.get(y));
                    cambios = true;
                }else if(mapNeg.isRaiz()){
                    raices[numeroRaiz] = (float)-ti.get(x)/cp.get(y);
                    upgradeNumeros(new Ruffini(raices[numeroRaiz],this.numeros, this.formatoNum),numeroRaiz);
                    raicesRet[numeroRaiz++] = this.fraccionToString((float)-ti.get(x),(float)cp.get(y));
                    cambios = true;
                }
            }
        }
        if(!cambios){
            if(numeros.size() == 3){
                double a=numeros.get(2),
                        b = numeros.get(1),
                        c = numeros.get(0);
                        
                double arribaRaiz= Math.sqrt(b*b-4*a*c);
                int cambioSigno = -1;
                if(!Double.isNaN(arribaRaiz)){
                    do{
                        raices[numeroRaiz] = Float.parseFloat((-b+(arribaRaiz*cambioSigno))/(2*a)+"");
                        upgradeNumeros(new Ruffini(raices[numeroRaiz], this.numeros, this.formatoNum),numeroRaiz);
                        raicesRet[numeroRaiz++] = this.fraccionToString(
                                Float.parseFloat(-b+(arribaRaiz*cambioSigno)+""),
                                Float.parseFloat((2*a)+"")
                        );  
                        cambioSigno = 1;
                    }while(cambioSigno == 1);
                }
                cambios = false;
            }
        }
        if(cambios){
            this.cambios = false;
            this.getRaices();
        }//else
            //System.out.println("Proceso terminado");
        return raicesRet;
    }

    public void upgradeNumeros(Ruffini map,int lugar){
        map.calcular();
        map.mostrar(lugar,txtArea);
        this.numeros = map.retornaArray();
    }
    public ArrayList<Integer> divisores(float valor){
        ArrayList<Integer> resultado = new ArrayList<>();
        
        if(Math.abs(valor) == 0){
            resultado.add(0);
        }
        else for(int x = 1;x<=Math.abs(valor);x++){
            if(Math.abs(valor) % x == 0)
                resultado.add(x);
        }
        
        return resultado;
    }
    public void print(){
        for(int x = 0;x<numeros.size();x++){
            System.out.println(numeros.get(x));
        }
    }
    public Float toFloat(String numero){
        try {
           return Float.parseFloat(numero);
        } catch (NumberFormatException err) { 
           return null;
        }

    }
    public void add(String numero){
        this.numeros.add(this.toFloat(numero));
    }
}
