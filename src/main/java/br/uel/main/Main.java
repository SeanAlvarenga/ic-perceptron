package br.uel.main;

import br.uel.module.AppModule;
import br.uel.perceptron.Perceptron;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

    public Main() {
        Injector injector = Guice.createInjector(new AppModule());
        Perceptron perceptron = injector.getInstance(Perceptron.class);
        perceptron.readInput("lista_tabela.tsv");
        perceptron.learning();
        System.out.println("Rate: " + perceptron.evaluation());
        perceptron.createLineChart();
    }

    public static void main(String[] args) {
        new Main();
    }
}
