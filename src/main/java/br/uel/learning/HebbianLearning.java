package br.uel.learning;

import br.uel.functions.ActivationFunction;
import br.uel.validation.AbstractInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HebbianLearning extends Learning {

    final Logger logger = LoggerFactory.getLogger(HebbianLearning.class);

    private long limitEpochs = -1;
    
    public HebbianLearning(ActivationFunction function) {
        super(function);
    }

    @Override
    protected void readProperties() {
        super.readProperties();
        
        try (InputStream stream = this.getClass().getResourceAsStream("/hebb.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            limitEpochs = Long.valueOf(properties.getProperty("limitEpochs"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double[] learn(AbstractInputReader dataReader, double[] weights, double[] classes) {
        readProperties();
        int numEpochs = 0, errorCount = 0;
        boolean hasError = true;
        double sum;                // somatório (entrada * peso)
        double y;                // saída da perceptron
        double[] matrixLine;

        while (hasError) {
            numEpochs++;
            errorCount = 0;
            hasError = false;

            dataReader.reset();
            int i = 0;
            
            while (dataReader.nextTraining()) {
                matrixLine = dataReader.getInputTraining().getData();
                sum = 0;
                
                logger.info("");
                logger.info("*** Epóca " + numEpochs + " Amostra " + (i + 1) + " ***");
                
                for (int j = 0; j < dataReader.getNumberOfColumns(); j++) {
                	logger.info("Soma = " + sum + " + (" + weights[j] + " * " + matrixLine[j] + ")");
                	sum += weights[j] * matrixLine[j];
                	logger.info("     = " + sum);
                }
                
                sum -= threshold;
                logger.info("Soma - threshold: " + sum);
                
                y = activationFunction.function(sum);

                logger.info("Saída encontrada: " + y + ". Valor desejado: " + classes[i]);
                
                if (y != classes[i]) {
                    hasError = true;
                    errorCount++;
                    
                    logger.info("Atualizando pesos...");
                    // atualização dos pesos
                    for (int j = 0; j < weights.length; j++) {
                        logger.info("Peso[" + j + "] = " + weights[j] + " + (" + learningRate + " * (" + classes[i] + " - " + y + ") * " + matrixLine[j] + ")");
                        weights[j] += (learningRate * (classes[i] - y) * matrixLine[j]);
                        // Atualização de limiar
 //                       threshold += (learningRate * (classes[i] - y) * matrixLine[j]);
                        logger.info("        = " + weights[j]);
                    }
                }
                i++;
            }
            
            logger.info("Erros acumulados: " + errorCount);
            this.plot.addValue(numEpochs, errorCount);
            
            if (limitEpochs != -1 && numEpochs > limitEpochs) break;
        }

        return weights;
    }
}
