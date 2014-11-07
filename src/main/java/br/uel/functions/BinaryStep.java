package br.uel.functions;

public class BinaryStep extends ActivationFunction {
	
	public BinaryStep() {
		readProperties();
	}
	
	@Override
	public double function(double value) {
		return (value >= threshold) ? upperBound : lowerBound;
	}
}
