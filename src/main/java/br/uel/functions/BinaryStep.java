package br.uel.functions;

public class BinaryStep extends ActivationFunction {

    public BinaryStep() {
        super();
    }

    @Override
    public double function(double value) {
        return (value >= threshold) ? upperBound : lowerBound;
    }
}
