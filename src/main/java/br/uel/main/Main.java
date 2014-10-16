package br.uel.main;

import br.uel.functions.ActivationFunction;
import br.uel.functions.BinaryStep;
import br.uel.perceptron.Perceptron;

public class Main {

    public static void main(String[] args) {
        ActivationFunction function = new BinaryStep();
        Perceptron p = new Perceptron(function);
        p.readInput("learning_set.csv");
        p.training(20000);
        System.out.println("Rate: " + p.evaluation());
//		p.print();
    }
}
