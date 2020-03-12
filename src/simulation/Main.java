package simulation;

import org.jfree.chart.ChartFactory;

import java.util.ArrayList;
import java.util.List;

import simulation.NetworkNeuron;
import static simulation.Neuron.PrebuiltNeuron.*;
import static simulation.general.General.*;

public class Main {
    static final int timeSteps = 200;
    static final int numberLayers = 3;
    static final int layerSizeX = 10;
    static final int layerSizeY = 10;
    static final int numberTargetCells = 100000;

    static int[] neuronsPerLayerArray = new int[numberLayers];
    static ArrayList<Electrode> electrodeArray = new ArrayList<>();
    static int[] targetLayerSpikes = new int[numberTargetCells];
    static Neuron[] neuronArray;

    public static void main(String[] args) {
        createNetwork();
        addSynapses();
        addElectrodes();
        runSimulation();
    }

    public static void createNetwork(){
        for(int i=0; i<numberLayers; i++){
            neuronsPerLayerArray[i] = numberTargetCells; //TODO Update to be biologically relevant for neuron density at each layer
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
        for(int i = 0; i<timeSteps; i++) {

            int count = 0;
            for(Electrode electrode: electrodeArray) {
                if (timeSteps % electrode.frequency == 0) {
                    for (int neuronID : electrode.neuronsInRange) {
                        //println(neuronID + ": " + electrode.voltageAtNeurons.get(count));
                        neuronArray[neuronID].changeVoltage(electrode.voltageAtNeurons.get(count));
                        //println(""+neuronArray[neuronID].getVoltage());
                        count++;
                    }
                }
            }

            count = 0;
            for(Neuron neuron: neuronArray){
                if(neuron.checkForSpike()){
                    if(count > sumIntegerArray(neuronsPerLayerArray)-numberTargetCells)
                        targetLayerSpikes[count-(sumIntegerArray(neuronsPerLayerArray)-numberTargetCells)]++;
                    for(Synapse synapse: neuron.getSynapses()){
                        neuronArray[synapse.neuronID].changeVoltage(synapse.getWeight());
                    }
                }
                count++;
            }


        }

        int numberOfTargetCellsFired = 0;
        for(int numberOfSpikes: targetLayerSpikes)
            if(numberOfSpikes>0)
                numberOfTargetCellsFired++;

        println("Number of target layer spikes: "+ sumIntegerArray(targetLayerSpikes));
        println("Number of different cells that have fired: "+numberOfTargetCellsFired);
    }
}
