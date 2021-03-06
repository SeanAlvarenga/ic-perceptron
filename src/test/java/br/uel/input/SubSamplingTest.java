package br.uel.input;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import br.uel.validation.CrossValidationReader;
import org.junit.Before;
import org.junit.Test;

public class SubSamplingTest {

    private CrossValidationReader method;

    @Before
    public void setUp() throws Exception {
        double[][] data = {
                {1.0, 2.0, 3.0, 1.0},
                {9.0, 2.0, 3.0, 1.0},
                {1.0, 2.0, 3.0, 1.0},
                {1.0, 2.0, 3.0, 1.0},
                {1.0, 2.0, 3.0, 1.0},
                {1.0, 2.0, 3.0, 1.0},
                {1.0, 2.0, 3.0, 1.0}
        };
        method = new CrossValidationReader(data);
    }

    @Test
    public void testAll() {
        for (int i = 0; i < 6; i++) {
            if (method.nextTraining()) {
                method.getInputTraining();
            }
        }
        assertThat(method.nextTraining(), is(false));

        assertThat(method.nextValidation(), is(true));

        assertThat(method.getInputValidation().getData().length, is(equalTo(4)));


    }


}