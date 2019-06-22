public class Connection {
    private double weight;
    private double deltaWeight;

    public Connection() {
        weight = Math.random();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void addToWeight(double weight) {
        this.weight += weight;
    }

    public double getDeltaWeight() {
        return deltaWeight;
    }

    public void setDeltaWeight(double deltaWeight) {
        this.deltaWeight = deltaWeight;
    }
}
