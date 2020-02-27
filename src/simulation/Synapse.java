package simulation;

public class Synapse {
    int neuronID = 0;
    boolean excitatory = false;
    double weight = 0.00;

    /**
     * Each Neuron has a list of post-synaptic connections which are iterated through in Neuron.update()
     * @param neuronID = postsynaptic neuron to call Neuron.changeVoltage (ID handled by main program)
     * @param excitatory = declare whether synapse is excitatory (true) or inhibitory (false)
     * @param weight = synapse strength
     */
    public Synapse(int neuronID, boolean excitatory, double weight){
        this.neuronID = neuronID;
        this. excitatory = excitatory;
        this.weight = weight;
    }

    public double getWeight(){
        return this.excitatory ? this.weight : -this.weight;
    }
}
