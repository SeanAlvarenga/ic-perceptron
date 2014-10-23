package br.uel.validation;


public abstract class AbstractInputReader {

    protected double[][] data;

    protected int numberOfEntries;

    protected AbstractInputReader() {

    }

    protected AbstractInputReader(double[][] data) {
        this.data = data;
        this.numberOfEntries = data.length;
    }

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
        this.numberOfEntries = data.length;
    }


    public int getNumberOfColumns() {
        if(data != null && data[0] != null) return data[0].length;
        return -1;
    }


    public abstract boolean nextTraining();

    public abstract boolean nextValidation();

    public abstract Entry getInputTraining();

    public abstract Entry getInputValidation();

    public abstract void reset();


    public abstract double getTrainingSize();
}
