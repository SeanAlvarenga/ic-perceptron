package br.uel.validation;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PercentageSplit extends AbstractInputReader {

    private double validationSize;
    private double trainingSize;

    public int currentEntryTraining = 0;
    public int currentEntryValidation;


    public PercentageSplit() {
        this(new double[0][0]);
    }

    public PercentageSplit(double[][] data) {
        super(data);


        Properties properties = new Properties();
        try (InputStream stream = this.getClass().getResourceAsStream("/validation.properties")){
            properties.load(stream);
            this.validationSize = Double.parseDouble(properties.getProperty("validation_size"));
            this.trainingSize = Double.parseDouble(properties.getProperty("training_size"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        currentEntryValidation = numberOfEntries - (int) Math.floor(validationSize * numberOfEntries);
    }

    @Override
    public void setData(double[][] data) {
        super.setData(data);
        currentEntryValidation = numberOfEntries - (int) Math.floor(validationSize * numberOfEntries);
    }

    @Override
    public boolean nextTraining() {
        return (currentEntryTraining <= Math.ceil(trainingSize * numberOfEntries));
    }

    @Override
    public boolean nextValidation() {
        return  currentEntryValidation >= (numberOfEntries - Math.ceil(validationSize*numberOfEntries)) &&
                currentEntryValidation < numberOfEntries;
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
        currentEntryValidation = numberOfEntries - (int) Math.floor(validationSize * numberOfEntries);
    }



    @Override
    public double getTrainingSize() {
        return Math.ceil(numberOfEntries * trainingSize);
    }

    public  int getCurrentEntryTraining() {
        return currentEntryTraining;
    }

    public  int getCurrentEntryValidation() {
        return currentEntryValidation;
    }
}
