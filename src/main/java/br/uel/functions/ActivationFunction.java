package br.uel.functions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class ActivationFunction {

    public ActivationFunction() {
        readProperties();
    }
	
	protected double lowerBound;
	protected double upperBound;
	protected double threshold;
	
	public abstract double function(double value);
	
    protected void readProperties() {
        try (InputStream stream = this.getClass().getResourceAsStream("/function.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            lowerBound = Double.valueOf(properties.getProperty("lowerBound"));
            upperBound = Double.valueOf(properties.getProperty("upperBound"));
            threshold = Double.valueOf(properties.getProperty("threshold"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
