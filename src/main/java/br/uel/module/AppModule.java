package br.uel.module;

import br.uel.validation.AbstractInputReader;
import br.uel.validation.PercentageSplit;
import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AbstractInputReader.class).toInstance(new PercentageSplit());
    }
}
