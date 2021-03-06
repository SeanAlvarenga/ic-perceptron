package br.uel.learning;

import br.uel.functions.ActivationFunction;
import br.uel.validation.AbstractInputReader;
import br.uel.validation.CrossValidationReader;
import br.uel.validation.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AdalineRule extends Learning {


    final Logger logger = LoggerFactory.getLogger(AdalineRule.class);

    private double errorThreshold;

    public AdalineRule(ActivationFunction function) {
        super(function);
    }

    @Override
    protected void readProperties() {
        super.readProperties();
        try (InputStream stream = this.getClass().getResourceAsStream("/delta.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            errorThreshold = Double.valueOf(properties.getProperty("error_threshold"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public double meanLeastSquareError(AbstractInputReader dataReader, double[] weights, double[] classes) {
        dataReader.reset();
        double mle = 0.0;
        double sum;

        double[] matrixLine;
        while (dataReader.nextTraining()) {
            Entry entry = dataReader.getInputTraining();
            matrixLine = entry.getData();
            sum = 0.0;

            for (int j = 0; j < (dataReader.getNumberOfColumns()); j++) {
                sum += weights[j] * matrixLine[j];
            }

            final double delta = (classes[entry.getPosition()] - sum);
            mle += delta * delta;
//            mle += Math.abs(classes[entry.getPosition()] - sum);


        }
        logger.info("mle= " + mle);
        return mle / dataReader.getTrainingSize();
    }

    @Override
    public double[] learn(AbstractInputReader dataReader, double[] weights, double[] classes) {
        readProperties();
        int numEpochs = 0;
        double error = 10e12;
        double curError = 0;
        double sum;                // somatório (entrada * peso)
        double[] matrixLine;

        while (Math.abs(curError - error) > errorThreshold) {

            error = meanLeastSquareError(dataReader, weights, classes);
            if(curError > error) break;
            numEpochs++;

            dataReader.reset();
            int i = 0;

            while (dataReader.nextTraining()) {
                matrixLine = dataReader.getInputTraining().getData();
                sum = 0;


                logger.info("");
                logger.info("*** Epóca " + numEpochs + " Amostra " + (i + 1) + " ***");

                for (int j = 0; j < (dataReader.getNumberOfColumns()); j++) {
                    logger.info("Soma = " + sum + " + (" + weights[j] + " * " + matrixLine[j] + ")");
                    sum += weights[j] * matrixLine[j];
                    logger.info("     = " + sum);
                }

                logger.info("saída encontrada: " + sum + ". valor desejado: " + classes[i]);

                // atualização dos pesos
                for (int j = 0; j < weights.length; j++) {

                    logger.info("Peso[" + j + "] = " + weights[j] + " + (" + learningRate + " * (" + classes[i] + " - " + sum + ") * " + matrixLine[j] + ")");
                    weights[j] += (learningRate * (classes[i] - sum) * matrixLine[j]);
                    // Atualização de limiar
                    //                       threshold += (learningRate * (classes[i] - y) * matrixLine[j]);
                    logger.info("= " + weights[j]);
                }
                i++;
            } // while hasInputs

            AbstractInputReader reader = new CrossValidationReader(dataReader.getData());
            curError = meanLeastSquareError(reader, weights, classes);
            plot.addValue(numEpochs, curError);
            reader = null;

        }
        return weights;
    }
}
