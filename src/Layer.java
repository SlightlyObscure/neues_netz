import java.util.ArrayList;

public class Layer {
    private ArrayList<Neuron> neurons = new ArrayList<>();

    public Neuron getNeuron(int i) {
        return neurons.get(i);
    }
    public void addNeuron (Neuron n) {
        neurons.add(n);
    }

    public int getNeuronNum() {
        return neurons.size();
    }
}
