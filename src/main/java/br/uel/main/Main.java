package br.uel.main;

import br.uel.functions.ActivationFunction;
import br.uel.functions.BinaryStep;
import br.uel.learning.HebbianLearning;
import br.uel.module.AppModule;
import br.uel.perceptron.Perceptron;
import br.uel.validation.PercentageSplit;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

public class Main {

    @Inject
    @Named("HebbianBinary")
    private Perceptron perceptron;


    public Main() {
        Injector injector = Guice.createInjector(new AppModule());
        perceptron = injector.getInstance(Perceptron.class);
        perceptron.readInput("learning_set.csv");
        perceptron.training();
        System.out.println("Rate: " + perceptron.evaluation());
    }

    public static void main(String[] args) {

        new Main();

    }
}
