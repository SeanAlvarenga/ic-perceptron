package br.uel.input;

/**
 * Created by pedro on 16/10/14.
 */
public class Entry {
    private int position;
    private double[] data;

    public Entry(int position, double[] data) {
        this.position = position;
        this.data = data;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }
}
