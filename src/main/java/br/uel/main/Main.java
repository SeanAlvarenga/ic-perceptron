package br.uel.main;

import br.uel.functions.ActivationFunction;
import br.uel.functions.BinaryStep;
import br.uel.learning.HebbianLearning;
import br.uel.perceptron.Perceptron;
import br.uel.validation.PercentageSplit;

public class Main {

    public static void main(String[] args) {
        Perceptron p = new Perceptron<PercentageSplit, HebbianLearning, BinaryStep>();
        p.readInput("learning_set.csv");
        p.training();
        System.out.println("Rate: " + p.evaluation());
//		p.print();
    }
}
