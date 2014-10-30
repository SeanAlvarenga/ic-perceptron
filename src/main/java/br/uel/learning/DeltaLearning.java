package br.uel.learning;

import br.uel.functions.ActivationFunction;
import br.uel.validation.AbstractInputReader;
import br.uel.validation.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by pedro on 23/10/14.
 */
public class DeltaLearning extends Learning {


    final Logger logger = LoggerFactory.getLogger(HebbianLearning.class);

    private double errorThreshold;

    public DeltaLearning(ActivationFunction function) {
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
        double mle = 0L;
        double sum;

        double[] matrixLine;


        while (dataReader.nextTraining()) {
            Entry entry = dataReader.getInputTraining();
            matrixLine = entry.getData();
            sum = 0;

            for (int j = 0; j < (dataReader.getNumberOfColumns() - 1); j++) {
                sum += weights[j] * matrixLine[j];
            }

            mle += Math.pow((classes[entry.getPosition()] - sum), 2.0);

        }

        return mle / dataReader.getTrainingSize();
    }

    @Override
    public double[] learn(AbstractInputReader dataReader, double[] weights, double[] classes) {
        readProperties();
        int numEpochs = 0, errorCount;
        double error = 10e12;
        double curError = 0;
        double sum;                // somatório (entrada * peso)
        double[] matrixLine;

        while (Math.abs(curError - error) > errorThreshold) {
            error = meanLeastSquareError(dataReader, weights, classes);
            numEpochs++;
            errorCount = 0;

            dataReader.reset();
            int i = 0;
            
            while (dataReader.nextTraining()) {
                matrixLine = dataReader.getInputTraining().getData();
                sum = 0;

                for (int j = 0; j < (dataReader.getNumberOfColumns() - 1); j++) {
                    sum += weights[j] * matrixLine[j];
                }

                logger.info("");
                logger.info("*** Epóca " + numEpochs + " Amostra " + (i + 1) + " ***");
                logger.info("Soma: " + sum);
//                sum += threshold;

                logger.info("saída encontrada: " + sum + ". valor desejado: " + classes[i]);

                // atualização dos pesos
                for (int j = 0; j < weights.length; j++) {
                    weights[j] += (learningRate * (classes[i] - sum) * matrixLine[j]);
                    // Atualização de limiar
                    //                       threshold += (learningRate * (classes[i] - y) * matrixLine[j]);
                    logger.info(" = " + weights[j]);
                }
                
                i++;
                curError = meanLeastSquareError(dataReader, weights, classes);
            } // while hasInputs
            
            logger.info("ERROR: " + error + "    CURERROR:  " + curError + "  DELTA: " + Math.abs(error - curError));
        }
        return weights;
    }
}
