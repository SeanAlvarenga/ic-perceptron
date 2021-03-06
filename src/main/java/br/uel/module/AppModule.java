package br.uel.module;

import br.uel.functions.ActivationFunction;
import br.uel.functions.BinaryStep;
import br.uel.functions.Sigmoid;
import br.uel.learning.AdalineRule;
import br.uel.learning.HebbianLearning;
import br.uel.perceptron.Perceptron;
import br.uel.validation.CrossValidationReader;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class AppModule extends AbstractModule {
	
    @Override
    protected void configure() {
        bind(ActivationFunction.class).to(getFunctionClass());
    }


    @Provides
    public Perceptron providePerceptron() {
//        BinaryStep function = new BinaryStep();
        ActivationFunction function = null;
        try {
            function = (BinaryStep) getFunctionClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        CrossValidationReader validation = new CrossValidationReader();
        HebbianLearning learning = new HebbianLearning(function);

//        AdalineRule learning = new AdalineRule(function);

        return new Perceptron(validation, learning, function);
    }


    public Class getFunctionClass() {
        return BinaryStep.class;
    }


}
