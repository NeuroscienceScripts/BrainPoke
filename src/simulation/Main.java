package simulation;

import org.jfree.chart.ChartFactory;

import java.util.ArrayList;
import java.util.List;

import simulation.NetworkNeuron;
import static simulation.Neuron.PrebuiltNeuron.*;
import static simulation.general.General.*;

public class Main {
    public static void main(String[] args) {
        int timeSteps = 200;

        int numberLayers = 3;
        int layerSizeX = 10;
        int layerSizeY = 10;
        int[] neuronsPerLayerArray = new int[numberLayers];
        for(int i=0; i<numberLayers; i++){
            neuronsPerLayerArray[i] = 100000; //TODO Update to be biologically relevant for neuron density at each layer
        }
        List<NetworkNeuron>[] typesOfNeuronsPerLayerArray = new List[numberLayers];
        List<Double>[] percentOfNeuronsPerLayerArray = new List[numberLayers];

        for(int i=0; i<numberLayers; i++){
            typesOfNeuronsPerLayerArray[i] = new ArrayList<>();
            percentOfNeuronsPerLayerArray[i] = new ArrayList<>();
        }

        //TODO Update for variable amounts of layers
        // *** Update for different neuron types in layers ***
        // *** Layer 1 ***
        typesOfNeuronsPerLayerArray[0].add(new NetworkNeuron(RS, 0.0, 0.0)); // adds a new neuron type to the layer (in this case RS = regular spiking)
        percentOfNeuronsPerLayerArray[0].add(.85); // adds the percentage of regular spiking neurons declared in the line above
        typesOfNeuronsPerLayerArray[0].add(new NetworkNeuron(FS, 0.0, 0.0));
        percentOfNeuronsPerLayerArray[0].add(.15);

        // *** Layer 2 ***
        typesOfNeuronsPerLayerArray[1].add(new NetworkNeuron(RS, 10.0, 0.0));
        percentOfNeuronsPerLayerArray[1].add(.85);
        typesOfNeuronsPerLayerArray[1].add(new NetworkNeuron(FS, 10.0, 0.0));
        percentOfNeuronsPerLayerArray[1].add(.15);

        // *** Layer 3 ***
        typesOfNeuronsPerLayerArray[2].add(new NetworkNeuron(RS, 20.0, 0.0));
        percentOfNeuronsPerLayerArray[2].add(.85);
        typesOfNeuronsPerLayerArray[2].add(new NetworkNeuron(FS, 20.0, 0.0));
        percentOfNeuronsPerLayerArray[2].add(.15);


        Network newNetwork = new Network(numberLayers, layerSizeX, layerSizeY, neuronsPerLayerArray, typesOfNeuronsPerLayerArray, percentOfNeuronsPerLayerArray);
        Neuron[] neuronArray = newNetwork.generateNetwork();

        Electrode singleElectrode = new Electrode(5, 5, 10, 1, 1, 100, 0, neuronArray);

        println("Neurons effected by electrode: ");
        int count = 0;
        for(int neuronID: singleElectrode.neuronsInRange){
            println(neuronID+": "+ singleElectrode.voltageAtNeurons.get(count));
            count++;
        }
        /*Neuron[] neurons = new Neuron[numberNeurons];
        neurons[0] = (new Neuron(Neuron.PrebuiltNeuron.RS, new Location(0, 0, 0)));
        neurons[1] = (new Neuron(Neuron.PrebuiltNeuron.RS, new Location(0, 0, 0)));
        neurons[2] = (new Neuron(Neuron.PrebuiltNeuron.RS, new Location(0, 0, 0)));

        double[] singleNeuronVoltages = new double[timeSteps];

        neurons[0].addSynapse(new Synapse(1,true,20));
        neurons[1].addSynapse(new Synapse(2,true,20));*/

        //TODO: Figure out how to plot the array "singleNeuronVoltages"
        //The array contains the voltage at each timestep.  The above For loop prints out the values
        //at each (for comparison with the graph output).  Java requires an "instance" of each class
        //if the function is referenced in a non-static way.  For example, when I called Neuron,
        //I created an "instance" of the Neuron class called "singleNeuron".  You probably have to do this
        //for JFreeChart.  To access JFreeChart, do File-> Project Structure -> Libraries and then hit the "+"
        //in the top left corner.  Add a module from Maven, and search for jfree:jcommon and jfree:jfreechart
        //The most up to date versions on Maven should be jcommon:1.0.16 and jfreechart:1.0.13


    }
}
