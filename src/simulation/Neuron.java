package simulation;

import java.util.ArrayList;
import java.util.List;

import static simulation.general.General.*;

public class Neuron {

    int hz = 10;
    double a = 0;
    double b = 0;
    double c = 0;
    double d = 0;
    double v = 0;
    double u = 0;
    double vThreshold = 30;
    double probabilityOfSynapseHorizontal = 0;
    double probabilityOfSynapseVertical = 0;
    Location location = new Location(0,0,0);
    List<Synapse> synapses = new ArrayList<>();

    /**
     * Declares the possible pre-built neurons
     */
    public enum PrebuiltNeuron{
        //TODO find different neuron types and list their names here
        /**
         * Regular spiking
         */
        RS,
        /**
         * Fast spiking
         */
        FS
    }

    /**
     * Creates an instance of the neuron with the following prebuilt settings
     * @param pbn
     * @param location
     */
    public Neuron(PrebuiltNeuron pbn, Location location){
        //TODO find the parameters for the prebuilt neurons specified in PrebuiltNeuron
        switch (pbn) {
            case RS:
                this.a = .02;
                this.b = .2;
                this.c = -65;
                this.v = -70;
                this.d = 8;
                this.u = .2 * -65;
                this.vThreshold = 30;
                this.probabilityOfSynapseHorizontal = 5;
                this.probabilityOfSynapseVertical = 5;
                this.location = location;
                break;

            case FS:
                this.a = .01;
                this.b = .2;
                this.c = -65;
                this.v = -70;
                this.d = 2.0;
                this.u = .2 * -65;
                this.vThreshold = 30;
                this.probabilityOfSynapseHorizontal = 5;
                this.probabilityOfSynapseVertical = 5;
                this.location = location;
                break;

            default:
                println("ERROR - Predefined Neuron not found");
                break;
        }
    }

    /**
     * Typical creation of a Neuron
     * @param a = recovery time constant (ms^-1) - smaller values result in slower recovery
     * @param b = sensitivity of recovery variable "u" - 1/R  (pA.mV^-1 [10^-9 Ω^-1])
     * @param c = post spike reset value/starting voltage (mV)
     * @param d = outward - inward current (pA)
     * @param vThreshold = voltage to cutoff spike (mV)
     */
     public Neuron(double a, double b, double c, double d, double vThreshold, double probabilityOfSynapseHorizontal, double probabilityOfSynapseVertical,  Location location){
         this.a = a;
         this.b = b;
         this.c = c;
         this.v = c;
         this.d = d;
         this.u = b*v;
         this.vThreshold = vThreshold;
         this.probabilityOfSynapseVertical = probabilityOfSynapseVertical;
         this.probabilityOfSynapseHorizontal = probabilityOfSynapseHorizontal;
         this.location = location;
     }

    /**
     * Neuron ID must be handled by the main script.  Takes
     */
    public void addSynapse(Synapse newSynapse){
         this.synapses.add(newSynapse);
     }

    /**
     * Updates voltage based off Izhikevich model parameters.
     * Presynaptic neurons will have already fired and applied
     * their voltage changes to this Neuron...
     * Checks if this Neuron should fire. If so,
     * iterates through all synapses in which this Neuron is
     * the pre-synaptic neuron and calls Neuron.changeVoltage
     */
    public boolean update(){
        boolean spike = false;
        v = v + (1/hz)*(.04*(v*v)+5*v+140-u);
        u = u + (a*b*v - a*u);
        println("Voltage: "+this.v);
        if(this.v > this.vThreshold) {
           spike = true;

            println("Change v to c: "+this.c);
            v = c;
            u = u + d;
            println("Spike!");
        }
        return spike;
     }

    /**
     * Updates the Neuron voltage
     * @param voltageChange = signed (+/-) voltage change to apply to the Neuron
     */
    public void changeVoltage(double voltageChange){
        this.v = this.v + voltageChange;
        println("Voltage change: "+voltageChange);
     }

    /**
     * Used to get the Neuron's current voltage (for plotting purposes)
     * @return this.v (Neuron's current voltage)
     */
    public double getVoltage(){
        return this.v;
     }

    /**
     * Used to get the Neuron's location Z value
     */
    public double getZ(){
        return this.location.z;
     }

     public List<Synapse> getSynapses(){
        return this.synapses;
     }

}
