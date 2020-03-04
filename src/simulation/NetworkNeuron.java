package simulation;

public class NetworkNeuron {
    Neuron.PrebuiltNeuron neuronType;
    double meanZ;
    double stdDevZ;

    /**
     * Used in Network class to define types of neurons at each layer
     * @param neuronType
     * @param meanZ = average height(z) for neurons of this type in this layer (starting at outermost layer)
     * @param stdDevZ = standard deviation of height(z)
     */
    public NetworkNeuron(Neuron.PrebuiltNeuron neuronType, double meanZ, double stdDevZ){
        this.neuronType = neuronType;
        this.meanZ = meanZ;
        this.stdDevZ = stdDevZ;
    }
}
