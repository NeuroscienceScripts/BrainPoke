package simulation;

import org.jfree.chart.ChartFactory;

import java.util.ArrayList;
import java.util.List;

import simulation.NetworkNeuron;
import static simulation.Neuron.PrebuiltNeuron.*;
import static simulation.general.General.*;

public class Main {
    static Neuron[] neuronArray;
    static int timeSteps = 200;
    static int numberLayers = 3;
    static int layerSizeX = 10;
    static int layerSizeY = 10;
    static int[] neuronsPerLayerArray = new int[numberLayers];
    static ArrayList<Electrode> electrodeArray = new ArrayList<>();

    public static void main(String[] args) {
        createNetwork();
        addSynapses();
        addElectrodes();
        runSimulation();
    }

    public static void createNetwork(){
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
        // *** Layer 4 ***
        typesOfNeuronsPerLayerArray[0].add(new NetworkNeuron(RS, 0.0, 0.0)); // adds a new neuron type to the layer (in this case RS = regular spiking)
        percentOfNeuronsPerLayerArray[0].add(.85); // adds the percentage of regular spiking neurons declared in the line above
        typesOfNeuronsPerLayerArray[0].add(new NetworkNeuron(FS, 0.0, 0.0));
        percentOfNeuronsPerLayerArray[0].add(.15);

        // *** Layer 2/3 ***
        typesOfNeuronsPerLayerArray[1].add(new NetworkNeuron(RS, 10.0, 0.0));
        percentOfNeuronsPerLayerArray[1].add(.85);
        typesOfNeuronsPerLayerArray[1].add(new NetworkNeuron(FS, 10.0, 0.0));
        percentOfNeuronsPerLayerArray[1].add(.15);

        // *** Layer 6 ***
        typesOfNeuronsPerLayerArray[2].add(new NetworkNeuron(RS, 20.0, 0.0));
        percentOfNeuronsPerLayerArray[2].add(.85);
        typesOfNeuronsPerLayerArray[2].add(new NetworkNeuron(FS, 20.0, 0.0));
        percentOfNeuronsPerLayerArray[2].add(.15);


        Network newNetwork = new Network(numberLayers, layerSizeX, layerSizeY, neuronsPerLayerArray, typesOfNeuronsPerLayerArray, percentOfNeuronsPerLayerArray);
        neuronArray = newNetwork.generateNetwork();
    }

    public static void addSynapses(){

    }

    public static void addElectrodes(){
        Electrode singleElectrode = new Electrode(5, 5, 10, 1, 1, 100, 0, neuronArray);
        electrodeArray.add(singleElectrode);
    }

    public static void runSimulation(){
        for(int i = 0; i<1; i++) {

            int count = 0;
            for(Electrode electrode: electrodeArray) {
                if (timeSteps % electrode.frequency == 0) {
                    for (int neuronID : electrode.neuronsInRange) {
                        println(neuronID + ": " + electrode.voltageAtNeurons.get(count));
                        neuronArray[neuronID].changeVoltage(electrode.voltageAtNeurons.get(count));
                        println(""+neuronArray[neuronID].getVoltage());
                        count++;
                    }
                }
            }

            for(Neuron neuron: neuronArray){
                if(neuron.checkForSpike()){
                    for(Synapse synapse: neuron.getSynapses()){
                        neuronArray[synapse.neuronID].changeVoltage(synapse.getWeight());
                    }
                }
            }
        }
    }
}
