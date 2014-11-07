package br.uel.module;

import br.uel.functions.BinaryStep;
import br.uel.functions.Sigmoid;
import br.uel.learning.DeltaLearning;
import br.uel.learning.HebbianLearning;
import br.uel.perceptron.Perceptron;
import br.uel.validation.PercentageSplit;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class AppModule extends AbstractModule {
	
    @Override
    protected void configure() {
    }


    @Provides
    public Perceptron providePerceptron() {
        BinaryStep function = new BinaryStep();
//        Sigmoid function = new Sigmoid();
        PercentageSplit validation = new PercentageSplit();
        HebbianLearning learning = new HebbianLearning(function);

//        DeltaLearning learning = new DeltaLearning(function);

        return  new Perceptron(validation, learning, function);
    }
}
