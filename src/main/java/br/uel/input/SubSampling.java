package br.uel.input;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SubSampling extends AbstractInputReader {

    private double validationSize;
    private double trainingSize;

    public static int currentEntryTraining = 0;
    public static int currentEntryValidation;


    public SubSampling() {
        this(new double[0][0]);
    }

    public SubSampling(double[][] data) {
        super(data);


        currentEntryValidation = (int) Math.floor(validationSize * numberOfEntries);

        InputStream stream = this.getClass().getResourceAsStream("/validation.properties");
        Properties properties = new Properties();
        try {
            properties.load(stream);
            this.validationSize = Double.parseDouble(properties.getProperty("validation_size"));
            this.trainingSize = Double.parseDouble(properties.getProperty("training_size"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean nextTraining() {
        return (currentEntryTraining <= Math.ceil(trainingSize * numberOfEntries));
    }

    @Override
    public boolean nextValidation() {
        return (currentEntryValidation <= Math.ceil(validationSize * numberOfEntries));
    }

    @Override
    public Entry getInputTraining() {
        return new Entry(currentEntryTraining, data[currentEntryTraining++]);
    }

    @Override
    public Entry getInputValidation() {
        return new Entry(currentEntryValidation, data[currentEntryValidation++]);
    }

    public void reset() {
        currentEntryTraining = 0;
        currentEntryValidation = (int) Math.floor(validationSize * numberOfEntries);
    }

}