package br.uel.perceptron;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import br.uel.evaluation.AdalineEvaluation;
import br.uel.evaluation.HebbianEvaluation;
import br.uel.learning.AdalineRule;
import br.uel.learning.HebbianLearning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.uel.functions.ActivationFunction;
import br.uel.learning.Learning;
import br.uel.validation.AbstractInputReader;

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
    private int columns;              // quantidade de colunas do arquivo
    private double bias;

    final Logger logger = LoggerFactory.getLogger(Perceptron.class);
    private boolean enableBias = false;
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
                
                for (int i = 0; i < length - 1; i++) {
                    trainingSet[count][i] = Double.valueOf(values[i]);
                }

                classes[count] = Double.valueOf(values[length - 1]);

                // Introducing bias in training set
                if (enableBias) trainingSet[count][(values.length - 1)] = bias;
                count++;
            }
        }

        System.out.println("LINE: " + lines);
        inputReader.setData(trainingSet);
        System.out.println("Training size: " + inputReader.getTrainingSize());
    }

    private void instantiateInputs() {
        if (enableBias) {
            trainingSet = new double[lines][columns];
            weight = new double[columns];
        } else {
            trainingSet = new double[lines][(columns - 1)];
            weight = new double[(columns - 1)];
        }

        classes = new double[lines];
    }

    public void learning() {
        weight = learningMethod.learn(this.inputReader, weight, classes);
    }

    public double evaluation() {
        if (learningMethod instanceof AdalineRule) {
            return new AdalineEvaluation(inputReader, weight, classes).avaliate();
        } else if (learningMethod instanceof HebbianLearning) {
            return new HebbianEvaluation(inputReader, weight, classes).avaliate();
        } else {
            return -1.0;
        }
    }

    private void randomWeightInit() {
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < weight.length; i++) {
            weight[i] = minWeight + (maxWeight - minWeight) * random.nextDouble();
        }
        
        logger.info("Pesos aleatórios gerados: " + Arrays.toString(weight));
    }

    private void loadParameters() {
        try (InputStream stream = this.getClass().getResourceAsStream("/perceptron.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            minWeight = Double.parseDouble(properties.getProperty("minWeight"));
            maxWeight = Double.parseDouble(properties.getProperty("maxWeight"));
            enableBias = properties.getProperty("enableBias").equals("1");
            separator = properties.getProperty("separator");
            
            if (enableBias) {
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

    public void createLineChart() {
    	this.learningMethod.getPlot().createLineChart("Erro vs Época", "Época", "Erro Acumulado");
    }
}
