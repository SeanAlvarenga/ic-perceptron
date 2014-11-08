package br.uel.evaluation;

import br.uel.validation.AbstractInputReader;

/**
 * Created by pedro on 08/11/14.
 */
public abstract class Evaluation {

    protected final double[] weights;
    protected final double[] classes;
    protected AbstractInputReader inputReader;



    public Evaluation(AbstractInputReader reader, double[] weights, double[] classes) {
        this.weights = weights;
        this.inputReader = reader;
        this.classes = classes;
    }

    public abstract double avaliate();


}
