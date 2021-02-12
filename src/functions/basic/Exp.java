package functions.basic;

import functions.Function;

public class Exp implements Function {

    private double base_, a_;

    public Exp(double a, double base){
        a_=a;
        base_=base;
    }


    @Override
    public double getLeftDomainBorder() {
        return Double.MIN_VALUE;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.MAX_VALUE;
    }

    @Override
    public double getFunctionValue(double x) {
        return a_*Math.pow(base_, x);
    }
}
