package matamaticasproyect;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import javax.swing.JTextArea;

/**
 * @author alex
 */
public class Bases {
        
    private ArrayList<Float> polinomio;
    private Float[] raices;
    private String[] raicesEnString;
    private boolean continuoElCiclo;
    private int contNumeroDeRaices;
    private JTextArea txtAreaDeVRuffini;
    private DecimalFormat formatoNum = new DecimalFormat("#.00");
    
    public Bases(ArrayList<Float> numeros, JTextArea txtArea){
        DecimalFormatSymbols separador = new DecimalFormatSymbols();
        separador.setDecimalSeparator('.');
        this.formatoNum = new DecimalFormat("#.00", separador);
        this.polinomio = numeros;
        this.contNumeroDeRaices = 0;
        this.txtAreaDeVRuffini = txtArea;
    }
    public Bases(JTextArea txtArea) {
        DecimalFormatSymbols separador = new DecimalFormatSymbols();
        separador.setDecimalSeparator('.');
        this.formatoNum = new DecimalFormat("#.00", separador);        
        this.polinomio = new ArrayList<>();
        this.contNumeroDeRaices = 0;
        this.txtAreaDeVRuffini = txtArea;
    }
    protected int tomarGradoDelPolinomio(){
        return polinomio.size();
    }  
    public String fraccionAString(Float dividendo, Float divisor){
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
       this.raices = new Float[polinomio.size()-1];
       this.raicesEnString = new String[polinomio.size()-1];
    }

    public String[] agarrarRaices(){   
        
        ArrayList<Integer> ti =  tomarDivisores(polinomio.get(0));
        ArrayList<Integer> cp =  tomarDivisores(polinomio.get(polinomio.size()-1));
        
        Ruffini map, mapPos, mapNeg;
        this.continuoElCiclo = false;

        // teorema de raiz racional
        for(int x = 0;x<ti.size() && !continuoElCiclo;x++){
            for(int y = 0;y<cp.size() && !continuoElCiclo;y++){
                mapPos = new Ruffini(ti.get(x)/cp.get(y).floatValue(), this.polinomio, this.formatoNum);
                mapNeg = new Ruffini(-ti.get(x)/cp.get(y).floatValue(), this.polinomio, this.formatoNum);
             
               if(mapPos.esRaiz()){
                    raices[contNumeroDeRaices] = (float)ti.get(x)/cp.get(y);
                    actualizarNumeros(new Ruffini(raices[contNumeroDeRaices],this.polinomio, this.formatoNum),contNumeroDeRaices);
                    raicesEnString[contNumeroDeRaices++] = this.fraccionAString((float)ti.get(x),(float)cp.get(y));
                    continuoElCiclo = true;
                }if(mapNeg.esRaiz()){
                    raices[contNumeroDeRaices] = (float)-ti.get(x)/cp.get(y);
                    actualizarNumeros(new Ruffini(raices[contNumeroDeRaices],this.polinomio, this.formatoNum),contNumeroDeRaices);
                    raicesEnString[contNumeroDeRaices++] = this.fraccionAString((float)-ti.get(x),(float)cp.get(y));
                    continuoElCiclo = true;
                }
            }
        }
        if(!continuoElCiclo){
            if(polinomio.size() == 3){  
                // baskhara
                double a=polinomio.get(2),
                        b = polinomio.get(1),
                        c = polinomio.get(0);
                        
                double arribaRaiz= Math.sqrt(b*b-4*a*c);
                int cambioSigno = -1;
                if(!Double.isNaN(arribaRaiz)){
                    do{
                        raices[contNumeroDeRaices] = Float.parseFloat((-b+(arribaRaiz*cambioSigno))/(2*a)+"");
                        actualizarNumeros(new Ruffini(raices[contNumeroDeRaices], this.polinomio, this.formatoNum),contNumeroDeRaices);
                        raicesEnString[contNumeroDeRaices] = this.fraccionAString(
                                Float.parseFloat(-b+(arribaRaiz*cambioSigno)+""),
                                Float.parseFloat((2*a)+"")
                        );
                        contNumeroDeRaices++;
                        cambioSigno = 1;
                    }while(cambioSigno == 1 && contNumeroDeRaices < 2);
                }
            }
            continuoElCiclo = false;
        }
        if(continuoElCiclo)
            this.agarrarRaices();
        return raicesEnString;
    }

    public void actualizarNumeros(Ruffini map,int lugar){
        map.hacerCalculosDeLaTabla();
        map.mostrarTablaRuffini(lugar,txtAreaDeVRuffini);
        this.polinomio = map.tomarArray();
    }
    public ArrayList<Integer> tomarDivisores(float valor){
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
    public void imprimirRaices(){
        for(int x = 0;x<polinomio.size();x++){
            System.out.println(polinomio.get(x));
        }
    }
    public Float pasarAFloat(String numero){
        try {
           return Float.parseFloat(numero);
        } catch (NumberFormatException err) { 
           return null;
        }

    }
    public void agregarTerminoAlPolinomio(String numero){
        this.polinomio.add(this.pasarAFloat(numero));
    }
}
