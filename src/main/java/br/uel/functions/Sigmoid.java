package br.uel.functions;

public class Sigmoid extends ActivationFunction {
	
	public Sigmoid() {
		readProperties();
	}
	
    @Override
    public double function(double value) {
        double result = (1.0 / (1 + Math.exp(-value)));
        return (result >= threshold) ? upperBound : lowerBound;
    }
}
