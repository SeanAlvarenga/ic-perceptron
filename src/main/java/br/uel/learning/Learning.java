package br.uel.learning;

import br.uel.functions.ActivationFunction;
import br.uel.utils.ChartCreator;
import br.uel.validation.AbstractInputReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Learning {

    protected ActivationFunction activationFunction;
    protected ChartCreator plot = new ChartCreator("Taxa de erro");
  
	protected double threshold;
    protected double learningRate;

    public Learning() {
    }

    public Learning(ActivationFunction function) {
        this.activationFunction = function;
    }

    public abstract double[] learn(AbstractInputReader dataReader, double[] weights, double[] classes);

    protected void readProperties() {
        try (InputStream stream = this.getClass().getResourceAsStream("/perceptron.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            threshold = Double.valueOf(properties.getProperty("threshold"));
            learningRate = Double.valueOf(properties.getProperty("learningRate"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ChartCreator getPlot() {
		return plot;
	}
}
