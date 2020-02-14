package simulation;

public class Synapse {
    Neuron postsynapticNeuron = new Neuron(0.0,0.0,0.0,0.0,0.0,new Location(0,0,0));
    boolean excitatory = false;
    double weight = 0.00;

    /**
     * Each Neuron has a list of post-synaptic connections which are iterated through in Neuron.update()
     * @param postsynaptic = postsynaptic neuron to call Neuron.changeVoltage(Neuron postsynaptic)
     * @param excitatory = declare whether synapse is excitatory (true) or inhibitory (false)
     * @param weight = synapse strength
     */
    public Synapse(Neuron postsynapatic, boolean excitatory, double weight){
        this.postsynapticNeuron = postsynapticNeuron;
        this. excitatory = excitatory;
        this.weight = weight;
    }
}
