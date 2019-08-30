package matamaticasproyect;

import java.util.ArrayList;

/**
 * @author alex
 */
public class Bases {
        
    private ArrayList<Double> numeros;
    private Double[] raices;
    private String[] raicesRet;
    private boolean cambios;
    private int numeroRaiz;
    
    public Bases(ArrayList<Double> numeros){
        this.numeros = numeros;
        this.cambios = false;
        this.numeroRaiz = 0;
    }
    public Bases() {
        this.numeros = new ArrayList<Double>();
        this.cambios = false;
        this.numeroRaiz = 0;
    }
    
    public int getSize(){
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
       this.raices = new Double[numeros.size()-1];
       this.raicesRet = new String[numeros.size()-1];
    }

    public String[] getRaices(){   
        
        ArrayList<Integer> ti =  divisores(numeros.get(0));
        ArrayList<Integer> cp =  divisores(numeros.get(numeros.size()-1));
        
        Ruffini map, mapPos, mapNeg;
 
        for(int x = 0;x<ti.size() && !cambios;x++){
            for(int y = 0;y<cp.size() && !cambios;y++){
                mapPos = new Ruffini(ti.get(x)/cp.get(y).doubleValue(),this.numeros);
                mapNeg = new Ruffini(-ti.get(x)/cp.get(y).doubleValue(),this.numeros);

                if(mapPos.isRaiz() || mapNeg.isRaiz()){
                    if(mapPos.isRaiz()){
                        raices[numeroRaiz] = (double)ti.get(x)/cp.get(y);
                        upgradeNumeros(new Ruffini(raices[numeroRaiz],this.numeros),numeroRaiz);
                        raicesRet[numeroRaiz++] = this.fraccionToString((float)ti.get(x),(float)cp.get(y));
                    }else if(mapNeg.isRaiz()){
                        raices[numeroRaiz] = (double)-ti.get(x)/cp.get(y);
                        upgradeNumeros(new Ruffini(raices[numeroRaiz],this.numeros),numeroRaiz);
                        raicesRet[numeroRaiz++] = this.fraccionToString((float)-ti.get(x),(float)cp.get(y));
                    }
                    cambios = true;
                }
            }
        }
        if(!cambios){
            if(numeros.size() == 3){
                double a=numeros.get(2),
                        b = numeros.get(1),
                        c = numeros.get(0);

                double arriba[] = {-b+Math.sqrt(b*b-4*a*c),-b-Math.sqrt(b*b-4*a*c)}, 
                        abajo = 2*a;
                if(!Double.isNaN(arriba[0])){
                    for(int x = 0;x<2;x++){
                        raices[numeroRaiz] = arriba[x]/abajo;
                        upgradeNumeros(new Ruffini(raices[numeroRaiz],this.numeros),numeroRaiz);
                        raicesRet[numeroRaiz++] = this.fraccionToString((float)arriba[x],(float)abajo);  
                    }
                }
                cambios = false;
            }else{
                Double x = -100.0;
                double error = Math.pow(10, -3);
                int contador = 0;
                do{
                    map = new Ruffini(x,this.numeros);
                    map.calcular();
                    if(Math.abs(map.getImagen()) < error)
                        error=Math.abs(map.getImagen())/10;
                    x+=error;   
                    contador++;
                }while(error > Math.pow(10, -10) && x < 100 && contador < 200000);

                if(x < 100){                   
                    raices[numeroRaiz] = x;
                    upgradeNumeros(map,numeroRaiz);                    
                    raicesRet[numeroRaiz++] = x.toString();
                    cambios = true;
                }
            }
        }
        if(cambios){
            this.cambios = false;
            this.getRaices();
        }else
            System.out.println("Proceso terminado");
        return raicesRet;
    }

    public void upgradeNumeros(Ruffini map,int lugar){
        map.calcular();
        map.mostrar(lugar);
        this.numeros = map.retornaArray();
    }
    public ArrayList<Integer> divisores(double valor){
        ArrayList<Integer> resultado = new ArrayList<Integer>();
        
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
    public Double toDouble(String numero){
        try {
           return Double.parseDouble(numero);
        } catch (Exception e) { 
           return null;
        }

    }
    public void add(double numero){
        this.numeros.add(numero);
    }
}
