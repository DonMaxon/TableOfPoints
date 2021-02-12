package functions.basic;

public class Tan extends TrigonometricFunction {

    public Tan(double a, double b) {
        super(a, b);
    }

    @Override
    public double getFunctionValue(double x) {
        return a_*Math.tan(b_*x);
    }
}
