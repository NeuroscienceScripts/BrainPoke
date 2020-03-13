package simulation;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math.*;

import static simulation.general.General.println;

public class Electrode {
    // Variables for Current Spread //
    private static final double pi = 3.14159265358979323846;
    private static final double sigma = .276;

    // Electrode Variables
    double locationX;
    double locationY;
    double locationZ;
    double radius;
    double frequency;
    double current;
    double timeOffset;
    Neuron[] neuronNetwork;
    List<Integer> neuronsInRange = new ArrayList<>();
    List<Double> voltageAtNeurons = new ArrayList<>();

    public Electrode(double locationX, double locationY, double locationZ, double radius, double frequency, double current, double timeOffset, Neuron[] neuronNetwork){
        this.locationX = locationX;
        this.locationY = locationY;
        this.locationZ = locationZ;
        this.radius = radius;
        this.frequency = frequency;
        this.current = current;
        this.timeOffset = timeOffset;
        this.neuronNetwork = neuronNetwork;

        int neuronID = 0;
        println("Max X: "+this.getMaxX());
        println("Min X: "+this.getMinX());
        println("Max Y: "+this.getMaxY());
        println("Min Y: "+this.getMinY());
        println("Max Z: "+this.getMaxZ());
        println("Min Z: "+this.getMinZ());

        for(Neuron neuron: neuronNetwork){
            if(isInRange(neuron)){
                neuronsInRange.add(neuronID);
                voltageAtNeurons.add(this.current / 4*pi*sigma*Math.pow(Math.pow(neuron.location.x-this.locationX,2)+Math.pow(neuron.location.y-this.locationY,2)+Math.pow(neuron.location.z-this.locationZ,2),.5));
            }
            neuronID++;
        }
    }

    public List<Integer> getNeuronsInRange(){
        return this.neuronsInRange;
    }

    public List<Double> getVoltageAtNeurons(){
        return this.voltageAtNeurons;
    }

    /** Checks if the neuron falls within the possible x/y/z value ranges to receive at
     * least .01 mV from the current electrode
     * @param neuron
     * @param current
     * @return
     */
    public boolean isInRange(Neuron neuron){
        if(neuron.location.x > this.getMaxX()
                || neuron.location.x < this.getMinX()
                || neuron.location.y > this.getMaxY()
                || neuron.location.y < this.getMinY()
                || neuron.location.z > this.getMaxZ()
                || neuron.location.z < this.getMinZ())
            return false;

        return true;
    }

    /**
     * Used to optimize simulation, cuts off voltage at <.01mV
     * @return maximum possible x value to apply voltage to
     */
    public double getMaxX(){
        return this.locationX + (this.current / 4*pi*sigma*.01);
    }
    /**
     * Used to optimize simulation, cuts off voltage at <.01mV
     * @return minimum possible x value to apply voltage to
     */
    public double getMinX(){
        return this.locationX - (this.current / 4*pi*sigma*.01);
    }
    /**
     * Used to optimize simulation, cuts off voltage at <.01mV
     * @return maximum possible y value to apply voltage to
     */
    public double getMaxY(){
        return this.locationY + (this.current / 4*pi*sigma*.01);
    }
    /**
     * Used to optimize simulation, cuts off voltage at <.01mV
     * @return minimum possible y value to apply voltage to
     */
    public double getMinY(){
        return this.locationY - (this.current / 4*pi*sigma*.01);
    }
    /**
     * Used to optimize simulation, cuts off voltage at <.01mV
     * @return maximum possible z value to apply voltage to
     */
    public double getMaxZ(){
        return this.locationZ + (this.current / 4*pi*sigma*.01);
    }
    /**
     * Used to optimize simulation, cuts off voltage at <.01mV
     * @return minimum possible z value to apply voltage to
     */
    public double getMinZ(){
        return this.locationZ - (this.current / 4*pi*sigma*.01);
    }


}
