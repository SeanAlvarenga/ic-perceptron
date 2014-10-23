package br.uel.learning;

import br.uel.validation.AbstractInputReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Learning<F> {

    protected F activationFunction;

    protected double threshold;
    protected double learningRate;

    public Learning() {
        try {
            this.activationFunction = (F) activationFunction.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
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
}
