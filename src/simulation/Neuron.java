package simulation;

import simulation.general.Location;

public class Neuron {

    /**
     * Declares the possible pre-built neurons
     */
    public enum PrebuiltNeuron{
        //TODO find different neuron types and list their names here
        RS_EXC, RS_INH, FS_EXC, FS_INH;
    }

    /**
     * Creates an instance of the neuron with the following prebuilt settings
     * @param pbn
     */
    public Neuron(PrebuiltNeuron pbn, Location loc){
        //TODO find the parameters for the prebuilt neurons specified in PrebuiltNeuron
       switch (pbn)
           case CLASS1

    }

    /**
     * Typical creation of a Neuron
     * @param
     */
     public Neuron(int a, int b, int c, int d, Location loc){

     }

}
