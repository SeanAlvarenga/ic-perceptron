package br.uel.perceptron;

import br.uel.functions.ActivationFunction;
import br.uel.learning.HebbianLearning;
import br.uel.learning.Learning;
import br.uel.validation.AbstractInputReader;
import br.uel.validation.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public class Perceptron {

    private static final String INPUT_PATH = "src/main/resources/input/";

    private ActivationFunction activation;

    private AbstractInputReader inputReader;
    private Learning learningMethod;

    private double[][] trainingSet;
    private double[] classes;
    private double[] weight;
    private double minWeight;
    private double maxWeight;
    private int lines;                // quantidade de linhas do arquivo
    private int columns;            // quantidade de colunas do arquivo
    private double bias;

    final Logger logger = LoggerFactory.getLogger(Perceptron.class);
    private boolean enableBias;
    private String separator;

    public Perceptron() {
    }


    public Perceptron(AbstractInputReader validationType, Learning learningType, ActivationFunction function) {
        this.learningMethod = learningType;
        this.inputReader = validationType;
        this.activation = function;
    }

    public void readInput(String fileName) {
        loadParameters();
        lines = countFileLines(fileName);
        columns = countFileColumns(fileName);

        instantiateInputs();

        randomWeightInit();


        try (Scanner scanner = new Scanner(this.getClass().getResourceAsStream("/input/" + fileName))) {
            int count = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(separator);

                int length = values.length;
                System.out.println(count);
                for (int i = 0; i < length - 1; i++) {

                    trainingSet[count][i] = Double.valueOf(values[i]);
                }

                classes[count] = Double.valueOf(values[length - 1]);

                // Introducing bias in training set
                if (enableBias) trainingSet[count][values.length-1] = bias;
                count++;
            }
        }
        inputReader.setData(trainingSet);
    }

    private void instantiateInputs() {
        if (enableBias) {
            trainingSet = new double[lines][columns];
        } else {
            trainingSet = new double[lines][(columns - 1)];
        }

        classes = new double[lines];
        if (enableBias) {
            weight = new double[columns];
        } else {
            weight = new double[(columns - 1)];
        }
    }

    public void learning() {
        weight = learningMethod.learn(this.inputReader, weight, classes);
    }

    public double evaluation() {

        double sum = 0;
        int correct = 0, wrong = 0;
        while (inputReader.nextValidation()) {
            Entry entry = inputReader.getInputValidation();
            for (int i = 0; i < entry.getData().length; i++) {
                sum += weight[i] * entry.getData()[i];
            }

            double result = activation.function(sum, 0);

            if (result == classes[entry.getPosition()]) {
                correct++;
            } else {
                wrong++;
            }
            logger.debug("VALIDATION - Expected class:  " + classes[entry.getPosition()] + ";\t Given value:  " + result);
            logger.debug("WEIGHTS:   " + Arrays.toString(this.weight));
        }

        return (double) correct / (double) (correct + wrong);


    }

    private void randomWeightInit() {
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < weight.length; i++) {
            weight[i] = minWeight + (maxWeight - minWeight) * random.nextDouble();
        }

        logger.warn(Arrays.toString(weight));
    }

    private void loadParameters() {

        enableBias = false;
        try (InputStream stream = this.getClass().getResourceAsStream("/perceptron.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            minWeight = Double.parseDouble(properties.getProperty("minWeight"));
            maxWeight = Double.parseDouble(properties.getProperty("maxWeight"));
            enableBias = properties.getProperty("enableBias").equals("1");
            separator = properties.getProperty("separator");
            if(enableBias) {
                bias = Double.parseDouble(properties.getProperty("bias"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Erro ao carregar arquivo de Properties.");
        }


    }

    public int countFileLines(String fileName) {
        try (LineNumberReader reader =
                     new LineNumberReader(new FileReader(INPUT_PATH + fileName))) {
            while (true) {
                if (!((reader.readLine()) != null)) break;
            }
            return reader.getLineNumber();
        } catch (Exception ex) {
            return -1;
        }
    }

    public int countFileColumns(String fileName) {
        File file = new File(INPUT_PATH + fileName);

        try (Scanner scanner = new Scanner(file)) {
            String line = scanner.nextLine();
            String[] values = line.split(separator);
            return values.length;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void print() {
        for (int i = 0; i < lines; i++) {
            System.out.println();
            for (int j = 0; j < (columns - 1); j++) {
                System.out.print(trainingSet[i][j] + " ");
            }
        }

//		for (int j = 0; j < lines; j++) {
//			System.out.print(classes[j] + " ");
//		}
    }

}
