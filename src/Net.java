import java.util.ArrayList;

public class Net {
    private ArrayList<Layer> layers = new ArrayList<>();
    private double netError = 0;
    private double recentErrorAverage = 0;

    public Net(int[] layerSizes) {
        for (int layNum = 0; layNum < layerSizes.length; layNum++) {
            Layer layer = new Layer();

            int laySize = layerSizes[layNum];
            int nextLaySize = 0;
            if(layNum < layerSizes.length - 1) {
                nextLaySize = layerSizes[layNum+1];
            }

            for(int neuNum = 0; neuNum <= laySize; neuNum++) {  //1 extra neuron (biased neuron)
                layer.addNeuron(new Neuron(nextLaySize));
            }
            layer.getNeuron(laySize).setOutVal(1.0);

            layers.add(layer);
        }
    }

    public void feedForward(double[] input) throws Exception {
        int inNeuNum = layers.get(0).getNeuronNum();
        if( inNeuNum - 1 != input.length) {
            throw new Exception("ERROR: invalid number of input values. Input: " + input.length + ", neuron number: " + layers.get(0).getNeuronNum());
        }
        for (int i = 0; i < inNeuNum - 1; i++) {
            layers.get(0).getNeuron(i).setOutVal(input[i]);
        }

        for (int layNum = 1; layNum < layers.size(); layNum++) {
            for(int n = 0; n < layers.get(layNum).getNeuronNum() -1; n++) {
                layers.get(layNum).getNeuron(n).feedForward( layers.get(layNum-1), n);
            }
        }
    }

    public void backProp(ArrayList<Integer> targetVals) {
        Layer outputLayer = layers.get(layers.size()-1);
        netError = 0;

        for(int n = 0; n < outputLayer.getNeuronNum()-1; n++) {
            double delta = targetVals.get(n) - outputLayer.getNeuron(n).getOutVal();
            netError += delta * delta;
        }
        netError /= (outputLayer.getNeuronNum() - 1);
        netError = Math.sqrt(netError);

        //recentErrorAverage = ?

        for (int n = 0; n < outputLayer.getNeuronNum() - 1; n++) {
            outputLayer.getNeuron(n).calcOutGradients(targetVals.get(n));
        }

        for (int layNum = layers.size()-2; layNum > 0; layNum--) {
            Layer hiddenLay = layers.get(layNum);
            Layer nextLay = layers.get(layNum+1);

            for(int n = 0; n < hiddenLay.getNeuronNum(); n++) {
                hiddenLay.getNeuron(n).calcHiddenGradients(nextLay);
            }
        }

        for (int layNum = layers.size() - 1; layNum > 0; layNum--) {
            Layer layer = layers.get(layNum);
            Layer prevLayer = layers.get(layNum - 1);

            for(int n = 0; n < layer.getNeuronNum() - 1; n++) {
                layer.getNeuron(n).updateInWeights(prevLayer, n);
            }
        }
    }

    public void getResults(ArrayList<Double> resultVals) {
        resultVals.clear();

        Layer outLay = layers.get(layers.size() - 1);

        for(int n = 0; n < outLay.getNeuronNum(); n++) {
            resultVals.add(outLay.getNeuron(n).getOutVal());
        }
    }

    public void printWeights() {
        for(int lay = 0; lay < layers.size(); lay++) {
            Layer layer = layers.get(lay);
            for(int n = 0; n < layer.getNeuronNum(); n++) {
                Neuron neu = layer.getNeuron(n);
                for(int con = 0; con < neu.getConnections().size(); con++) {
                    Connection connect = neu.getConnections().get(con);
                    System.out.print(connect.getWeight() + " ");
                }
                System.out.println();
            }
        }
    }


}
