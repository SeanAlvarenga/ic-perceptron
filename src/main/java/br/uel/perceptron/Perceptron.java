package br.uel.perceptron;

import br.uel.functions.ActivationFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

public class Perceptron {

    private static final String INPUT_PATH = "src/main/resources/input/";
    private static final String OUTPUT_PATH = "src/main/resources/output/";

    private ActivationFunction activation;
    private double[][] trainingSet;
    private double[] classes;
    private double[] weight;
    private double threshold;        // theta ou limiar de ativação
    private double learningRate;
    private double minWeight;
    private double maxWeight;
    private int lines;                // quantidade de linhas do arquivo
    private int columns;            // quantidade de colunas do arquivo

    final Logger logger = LoggerFactory.getLogger(Perceptron.class);

    public Perceptron(ActivationFunction activation) {
        this.activation = activation;
    }

    public void readInput(String fileName) {
        lines = countFileLines(fileName);
        columns = countFileColumns(fileName);
        loadParameters();
        randomWeightInit();

        File file = new File(INPUT_PATH + fileName);

        try (Scanner scanner = new Scanner(file)) {
            int count = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");

                for (int i = 0; i < values.length - 1; i++) {
                    trainingSet[count][i] = Double.valueOf(values[i]);
                }

                classes[count] = Double.valueOf(values[values.length - 1]);
                count++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void training(int limitEpochs) {
        int numEpochs = 0;
        boolean hasError = true;
        double sum;                // somatório (entrada * peso)
        double y;                // saída da perceptron
        double[] matrixLine;

        while (hasError) {
            numEpochs++;
            hasError = false;

            for (int i = 0; i < lines; i++) {
                matrixLine = trainingSet[i];
                sum = 0;

                for (int j = 0; j < (columns - 1); j++) {
                    sum += weight[j] * matrixLine[j];
                }
                logger.info("");
                logger.info("*** Epóca " + numEpochs + " Amostra " + (i + 1) + " ***");
                logger.info("Soma: " + sum);
                sum -= threshold;
                y = activation.function(sum, 0);

                logger.info("saída encontrada: " + y + ". valor desejado: " + classes[i]);

                if (y != classes[i]) {
                    hasError = true;

                    // atualização dos pesos
                    for (int j = 0; j < weight.length; j++) {
                        logger.info("Peso[" + j + "] = " + weight[j] + " + (" + learningRate + " * (" + classes[i] + " - " + y + ") * " + matrixLine[j] + ")");
                        weight[j] += (learningRate * (classes[i] - y) * matrixLine[j]);
                        threshold += (learningRate * (classes[i] - y ) * matrixLine[j]);
                        logger.info(" = " + weight[j]);
                    }
                }
            }
            if(limitEpochs != -1 && numEpochs > limitEpochs) break;
        }
    }

    public double evaluation(double[] data) {
        assert data.length == weight.length : "Tamanho do vetor de dados incorreto.";

        double sum = 0;

        for (int i = 0; i < data.length; i++) {
            sum += weight[i] * data[i];
        }
        return activation.function(sum, 0);
    }

    private void randomWeightInit() {
        Random random = new Random();

        for (int i = 0; i < weight.length; i++) {
            weight[i] = minWeight + (maxWeight - minWeight) * random.nextDouble();
        }

        logger.warn(Arrays.toString(weight));
    }

    private void loadParameters() {
        trainingSet = new double[lines][(columns - 1)];
        classes = new double[lines];
        weight = new double[(columns - 1)];

        try (FileInputStream stream = new FileInputStream("perceptron.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            threshold = Double.valueOf(properties.getProperty("threshold"));
            learningRate = Double.valueOf(properties.getProperty("learningRate"));
            minWeight = Double.valueOf(properties.getProperty("minWeight"));
            maxWeight = Double.valueOf(properties.getProperty("maxWeight"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar arquivo de Properties.");
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
            String[] values = line.split(",");
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

    public void training() {
        training(-1);
    }
}
