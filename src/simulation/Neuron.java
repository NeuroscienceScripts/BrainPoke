package simulation;

import simulation.general.Location;
import static simulation.general.General.*;

public class Neuron {
    /**
     * Recovery time constant
     */
    double a = 0;

    /**
     * constant 1/R
     */
    double b = 0;

    /**
     * Potential reset value
     */
    double c = 0;

    /**
     * Outward minus inward current
     */
    double d = 0;

    /**
     * Current voltage
     */
    double v = 0;

    /**
     * Neuron's location in a 3d system
     */
    Location location = new Location(0,0,0);

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
     */
    public Neuron(PrebuiltNeuron pbn, Location location){
        //TODO find the parameters for the prebuilt neurons specified in PrebuiltNeuron
       switch (pbn) {
           case RS:
               this.a = .02;
               this.b = .2;
               this.c = -65.0;
               this.d = 8.0;
               this.location = location;

           case FS:
               this.a = .01;
               this.b = .2;
               this.c = -65.0;
               this.d = 2.0;
               this.location = location;

           default:
               println("ERROR - Predefined Neuron not found");
               this.a = 0;
               this.b = 0;
               this.c = 0;
               this.d = 0;
               this.location = location;
       }
    }

    /**
     * Typical creation of a Neuron
     * @param
     */
     public Neuron(double a, double b, double c, double d, Location location){
         this.a = a;
         this.b = b;
         this.c = c;
         this.d = d;
         this.location = location;
     }

}
