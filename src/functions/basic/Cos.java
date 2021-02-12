package functions.basic;


public class Cos extends TrigonometricFunction {


    public Cos(double a, double b) {
        super(a, b);
    }



    @Override
    public double getFunctionValue(double x) {
        return  a_*Math.cos(b_*x);
    }
}