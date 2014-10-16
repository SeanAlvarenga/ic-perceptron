package br.uel.module;

import br.uel.input.AbstractInputReader;
import br.uel.input.SubSampling;
import com.google.inject.AbstractModule;

/**
 * Created by pedro on 16/10/14.
 */
public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AbstractInputReader.class).toInstance(new SubSampling());
    }
}
