package functions.basic;

public class Sin extends TrigonometricFunction{

    public Sin(double a, double b) {
        super(a, b);
    }



    @Override
    public double getFunctionValue(double x) {
        return a_*Math.sin(b_*x);
    }
}
