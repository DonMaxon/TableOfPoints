package functions.basic;

import functions.Function;

public abstract class TrigonometricFunction implements Function {

    protected double a_, b_;

    public TrigonometricFunction(double a, double b){
        a_=a;
        b_=b;
    }


    @Override
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }
    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getA() {
        return a_;
    }
    public double getB(){
        return b_;
    }
}
