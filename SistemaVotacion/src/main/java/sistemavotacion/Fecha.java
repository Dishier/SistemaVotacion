package sistemavotacion;

public class Fecha {
    int d,m,a;

    public Fecha(int d, int m, int a) {
        this.d = d;
        this.m = m;
        this.a = a;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }
    
    public String getDiaFormato(){
        return (d < 10 ? "0" : "") + d;
    }
    
    public String getMesFormato(){
        return (m < 10 ? "0" : "") + m;
    }
    
    public String getAnioFormato(){
        String cad = "" + a;
        return cad.substring(2, 4);
    }
    
    @Override
    public String toString(){
        return getDiaFormato()+"/"+getMesFormato()+"/"+a;
    }
    
}
