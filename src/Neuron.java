import java.util.ArrayList;

public class Neuron {
    static double eta = 0.01, alpha = 0.01;
    private double inVal;
    private double outVal;
    private double gradient;
    private ArrayList<Connection> connections = new ArrayList<>();

    public Neuron(int outNum) {
        for(int i = 0; i < outNum; i++) {
            connections.add(new Connection());
        }
    }

    public double getInVal() {
        return inVal;
    }

    public void setInVal(double inVal) {
        this.inVal = inVal;
    }

    public double getOutVal() {
        return outVal;
    }

    public void setOutVal(double outVal) {
        this.outVal = outVal;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public double getGradient() {
        return gradient;
    }

    public void feedForward(Layer prevLayer, int indexOfThis) {
        double sum = 0;

        for(int n = 0; n < prevLayer.getNeuronNum(); n++) {
            Neuron neu = prevLayer.getNeuron(n);
            //System.out.println(indexOfThis + " " + n);
            sum += neu.getOutVal() * neu.getConnections().get(indexOfThis).getWeight();
        }

        outVal = transFunc(sum);
        //System.out.println("index: " + indexOfThis + "; new out Value: " + outVal + "; sum used: " + sum);
    }

    private double transFunc(double x) {
        //return  1 / (1 + Math.exp(-x));
        return Math.tanh(x);
    }

    private double transFuncDerivative(double x) {
        //return transFunc(x) * (1 - transFunc(x));
        return 1.0 - Math.tanh(x) * Math.tanh(x);
    }

    public void calcOutGradients(int targetVal) {
        double delta = targetVal - outVal;
        gradient = delta * transFuncDerivative(outVal);
    }

    public void calcHiddenGradients(Layer nextLay) {
        double dow = sumDOW(nextLay);   //sum of derivatives of weights
        gradient = dow * transFuncDerivative(outVal);
    }

    private double sumDOW(Layer nextLay) {
        double sum = 0;
        for(int n = 0; n < nextLay.getNeuronNum() - 1; n++ ){
            sum += connections.get(n).getWeight() * nextLay.getNeuron(n).getGradient();
        }
        return sum;
    }

    public void updateInWeights(Layer prevLayer, int indexOfThis) {
        for(int n = 0; n < prevLayer.getNeuronNum(); n++ ) {
            Neuron upNeu = prevLayer.getNeuron(n);

            double oldDeltaWeight = upNeu.getConnections().get(indexOfThis).getWeight();
            double newDeltaWeight = eta * upNeu.getOutVal() * gradient + alpha * oldDeltaWeight;

            Connection c =  upNeu.getConnections().get(indexOfThis);
            c.setDeltaWeight(newDeltaWeight);
            c.addToWeight(newDeltaWeight);
        }
    }
}
