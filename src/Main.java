import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static void printMnistMatrix(final MnistMatrix matrix) {
        System.out.println("label: " + matrix.getLabel());
        for (int r = 0; r < matrix.getNumberOfRows(); r++ ) {
            for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                System.out.print(matrix.getValue(r, c) + " ");
            }
            System.out.println();
        }
    }

    private static void radArray(final MnistMatrix[] matrix) {
        for(int i = 0; i < matrix.length; i++) {
            matrix[i].radicalizeVals(100);
        }
    }

    public static void main(String[] args) throws IOException {
        MnistMatrix[] mnistMatrixTrain = new MnistDataReader().readData("data/train-images.idx3-ubyte", "data/train-labels.idx1-ubyte");
        radArray(mnistMatrixTrain);

        int[] labledEach = new int[10];
        for(int i = 0; i < mnistMatrixTrain.length; i++) {
            labledEach[mnistMatrixTrain[i].getLabel()]++;
        }
        double[] lableFreq = new double[10];
        for(int i = 0; i < 10; i++) {
            lableFreq[i] = (double) labledEach[i] / 60000;
        }

        //printMnistMatrix(mnistMatrixTrain[0]);
        MnistMatrix[] mnistMatrixTest = new MnistDataReader().readData("data/t10k-images.idx3-ubyte", "data/t10k-labels.idx1-ubyte");
        radArray(mnistMatrixTest);

        int matColNum = mnistMatrixTrain[0].getNumberOfColumns();
        int matRowNum = mnistMatrixTrain[0].getNumberOfRows();
        int pixNum =  matColNum * matRowNum;

        int layers[] = {pixNum, 30, 30, 30, 10};
        Net net = new Net(layers);  //inLayerSize = 28 * 28; outLayerSize...number of possible results

        double mnistInput[] = new double[pixNum];
        ArrayList<Integer> targetVals = new ArrayList<>();
        ArrayList<Double> resultVals = new ArrayList<>();

        for(int i = 0; i < 200/*mnistMatrixTrain.length*/; i++) {

            for(int r = 0; r < matRowNum; r++) {
                for( int c = 0; c < matColNum; c++) {
                    mnistInput[(r * matColNum) + c] = (double) mnistMatrixTrain[i].getValue(r, c) / 128.0 - 1.0 /*- 0.16*/;   //TODO: change magic value?
                }
            }

            int label = mnistMatrixTrain[i].getLabel();

            System.out.println("\n");
            System.out.println("Set " + i + " Actual: " + label);

            try {
                net.feedForward(mnistInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
            net.getResults(resultVals);

            System.out.print("Out: ");
            for(int res = 0; res < resultVals.size() - 1; res++) {
                if(res != 0) {
                    System.out.print("; ");
                }
                String s = String.format("%.6f", resultVals.get(res));
                System.out.print(res + ": " + resultVals.get(res));
            }
            System.out.println();

            double highestVal = -1;
            double highestInd = -1;
            for(int hi = 0; hi < resultVals.size() - 1; hi++) {
                if(resultVals.get(hi) > highestVal) {
                    highestInd = hi;
                    highestVal = resultVals.get(hi);
                }
            }
            System.out.println("Result: " + highestInd + " with val " + highestVal);

            targetVals.clear();

            for(int t = 0; t < 10; t++) {
                if(t == label) {
                    targetVals.add(1);
                }
                else {
                    targetVals.add(0);
                }
            }
            net.backProp(targetVals);


            //net.printWeights();
            if(highestVal == -1) {

                break;
            }

        }

        int[][] confusion = new int[10][10];

        for(int i = 0; i < mnistMatrixTest.length; i++) {
            for(int r = 0; r < matRowNum; r++) {
                for( int c = 0; c < matColNum; c++) {
                    mnistInput[(r * matColNum) + c] = (double) mnistMatrixTest[i].getValue(r, c) - 0.16;   //TODO: change magic value?
                }
            }

            int label = mnistMatrixTest[i].getLabel();

            try {
                net.feedForward(mnistInput);
            } catch (Exception e) {
                e.printStackTrace();
            }
            net.getResults(resultVals);

            double highestVal = -1;
            int highestInd = -1;
            for(int hi = 0; hi < resultVals.size() - 1; hi++) {
                if(resultVals.get(hi) > highestVal) {
                    highestInd = hi;
                    highestVal = resultVals.get(hi);
                }
            }

            if(highestInd!= -1) {
                confusion[highestInd][label]++;
            }

            //System.out.println("Result: " + highestInd + " with val " + highestVal);
        }

        int total = 0, good = 0;
        for(int x = 0; x < 10; x++) {
            for(int y = 0; y < 10; y++) {
                int print = confusion[x][y];
                if(print < 1000) {
                    System.out.print(" ");
                }
                if(print < 100) {
                    System.out.print(" ");
                }
                if(print < 10) {
                    System.out.print(" ");
                }
                System.out.print( print + " ");

                total += print;
                if(x == y) {
                    good += print;
                }
            }
            System.out.print("\n");
        }
        float acc = (float) good/total;
        System.out.println("Accuracy: " + acc);
    }

}
