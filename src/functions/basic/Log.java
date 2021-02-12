package functions.basic;

import functions.Function;

public class Log implements Function {
    private double base_, a_;
    public Log(double a, double base){
        a_=a;
        base_=base;
    }

    @Override
    public double getLeftDomainBorder() {
        return 0;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.MAX_VALUE;
    }

    @Override
    public double getFunctionValue(double x) {
        return a_*Math.log(x) / Math.log(base_);

    }
}
