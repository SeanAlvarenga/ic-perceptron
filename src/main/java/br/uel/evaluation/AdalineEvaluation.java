package br.uel.evaluation;

import br.uel.functions.BinaryStep;
import br.uel.validation.AbstractInputReader;
import br.uel.validation.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class AdalineEvaluation extends Evaluation {

    private final Logger logger = LoggerFactory.getLogger(Evaluation.class);

    private double errorThreshold = 0.0;

    public AdalineEvaluation(AbstractInputReader reader, double[] weights, double[] classes) {
        super(reader, weights, classes);
    }

    private void loadParams() {

        try (InputStream stream = this.getClass().getResourceAsStream("/delta.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            errorThreshold = Double.parseDouble(properties.getProperty("error_threshold"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double avaliate() {

        loadParams();

        double sum = 0;
        int correct = 0;

        logger.debug("WEIGHTS:   " + Arrays.toString(this.weights));

        while (inputReader.nextValidation()) {
            Entry entry = inputReader.getInputValidation();

            for (int i = 0; i < entry.getData().length; i++) {
                sum += weights[i] * entry.getData()[i];
            }

            BinaryStep function = new BinaryStep();

            if (function.function(sum) == classes[entry.getPosition()]) {
                correct++;
            }

            logger.debug("VALIDATION - Expected class:  " + classes[entry.getPosition()] + ";\t Given value:  " + sum);
            logger.debug("-----$$ VALIDATION diff: " + Math.abs(classes[entry.getPosition()]-sum) + "  " + errorThreshold);

        }

        logger.debug("correct/size = " + correct + " / " + inputReader.getValidationSize());
        System.out.println("correct/size = " + correct + " / " + inputReader.getValidationSize());

        return (double) correct / inputReader.getValidationSize();
    }
}
