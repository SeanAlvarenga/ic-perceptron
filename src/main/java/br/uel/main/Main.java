package br.uel.main;

import br.uel.module.AppModule;
import br.uel.perceptron.Perceptron;
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
        perceptron.readInput("dataset_2014-10-30.csv");
        perceptron.learning();
//        System.out.println("Rate: " + perceptron.evaluation());
        perceptron.createLineChart();
    }

    public static void main(String[] args) {
        new Main();
    }
}
