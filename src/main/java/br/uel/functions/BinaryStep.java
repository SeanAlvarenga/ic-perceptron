package br.uel.functions;

public class BinaryStep implements ActivationFunction {
	
	@Override
	public double function(double value, double threshold) {
		return (value >= threshold) ? 1 : -1;
	}
}
