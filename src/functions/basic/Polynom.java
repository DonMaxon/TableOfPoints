package functions.basic;

import functions.Function;

public class Polynom implements Function {
    private double[] coeffs_;

    public Polynom(double[] coeffs){
        coeffs_=coeffs;
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
        double res = 0;
        for (int i = 0; i < coeffs_.length; ++i){
            res+=coeffs_[i]*Math.pow(x, coeffs_.length-i - 1);
        }
        return res;
    }
}
