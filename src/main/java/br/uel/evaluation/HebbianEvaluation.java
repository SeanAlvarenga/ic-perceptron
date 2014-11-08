package br.uel.evaluation;

import br.uel.functions.ActivationFunction;
import br.uel.module.AppModule;
import br.uel.validation.AbstractInputReader;
import br.uel.validation.Entry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by pedro on 08/11/14.
 */
public class HebbianEvaluation extends Evaluation {

    final Logger logger = LoggerFactory.getLogger(HebbianEvaluation.class);

    private ActivationFunction activationFunction;

    public HebbianEvaluation(AbstractInputReader reader, double[] weights, double[] classes) {
        super(reader, weights, classes);
        Injector injector = Guice.createInjector(new AppModule());
        this.activationFunction = injector.getInstance(ActivationFunction.class);
    }

    @Override
    public double avaliate() {
        double sum = 0;
        int correct = 0;

        while (inputReader.nextValidation()) {
            Entry entry = inputReader.getInputValidation();

            for (int i = 0; i < entry.getData().length; i++) {
                sum += weights[i] * entry.getData()[i];
            }

            double y = this.activationFunction.function(sum);

            if (y == this.classes[entry.getPosition()]) {
                correct++;
            }

            logger.debug("VALIDATION - Expected class:  " + classes[entry.getPosition()] + ";\t Given value:  " + sum);
            logger.debug("WEIGHTS:   " + Arrays.toString(this.weights));
        }

        return (double) correct / inputReader.getValidationSize();

    }
}
