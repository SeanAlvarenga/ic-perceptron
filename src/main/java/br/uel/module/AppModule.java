package br.uel.module;

import br.uel.functions.BinaryStep;
import br.uel.learning.DeltaLearning;
import br.uel.learning.HebbianLearning;
import br.uel.perceptron.Perceptron;
import br.uel.validation.AbstractInputReader;
import br.uel.validation.PercentageSplit;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
    }


    @Provides
    public Perceptron providePerceptron() {
        BinaryStep binaryStep = new BinaryStep();
        PercentageSplit validation = new PercentageSplit();
//        HebbianLearning learning = new HebbianLearning(binaryStep);

        DeltaLearning learning = new DeltaLearning(binaryStep);

        return  new Perceptron(validation, learning, binaryStep);
    }
}
