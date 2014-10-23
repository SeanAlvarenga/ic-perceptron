package br.uel.functions;

public class Sigmoid implements ActivationFunction{
    @Override
    public double function(double value, double threshold) {
        double result = (1.0/(1+Math.exp(-value)));
        return (result > threshold) ? 1 : -1;
    }
}
