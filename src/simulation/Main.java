package simulation;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static simulation.Neuron.PrebuiltNeuron.*;
import static simulation.general.General.*;

public class Main {
    //  *** ELECTRODE VARIABLES TO CHANGE ***
    static final double electrodeX = 1;
    static final double electrodeY = 1;
    static final double electrodeZ = 0;
    static final double electrodeRadius = .1;
    static final double electrodeFrequency = 1;
    static final double electrodeCurrent = 10;
    static final double electrodeTimeOffset = 0;

    static final int timeSteps = 200;
    static final int numberLayers = 3;
    static final int layerSizeX = 2;
    static final int layerSizeY = 2;
    static final int numberTargetCells = 1000;
    static final int excitatoryWeight = 10;
    static final int inhibitoryWeight = 10;

    static int numCPUs;
    static int[] neuronsPerLayerArray = new int[numberLayers];
    static ArrayList<Electrode> electrodeArray = new ArrayList<>();
    static int[] targetLayerSpikes = new int[numberTargetCells];
    static Neuron[] neuronArray;

    public static void main(String[] args) {
        println("Cores detected: "+Runtime.getRuntime().availableProcessors());
        numCPUs = Runtime.getRuntime().availableProcessors();

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
        typesOfNeuronsPerLayerArray[0].add(new NetworkNeuron(EXC, 3.0, 0.0)); // adds a new neuron type to the layer (in this case RS = regular spiking)
        percentOfNeuronsPerLayerArray[0].add(.85); // adds the percentage of regular spiking neurons declared in the line above
        typesOfNeuronsPerLayerArray[0].add(new NetworkNeuron(INH, 3.0, 0.0));
        percentOfNeuronsPerLayerArray[0].add(.15);

        // *** Layer 2/3 ***
        typesOfNeuronsPerLayerArray[1].add(new NetworkNeuron(EXC, 2, 0.0));
        percentOfNeuronsPerLayerArray[1].add(.85);
        typesOfNeuronsPerLayerArray[1].add(new NetworkNeuron(INH, 2, 0.0));
        percentOfNeuronsPerLayerArray[1].add(.15);

        // *** Layer 6 ***
        typesOfNeuronsPerLayerArray[2].add(new NetworkNeuron(EXC, 4, 0.0));
        percentOfNeuronsPerLayerArray[2].add(.85);
        typesOfNeuronsPerLayerArray[2].add(new NetworkNeuron(INH, 4, 0.0));
        percentOfNeuronsPerLayerArray[2].add(.15);


        Network newNetwork = new Network(numberLayers, layerSizeX, layerSizeY, neuronsPerLayerArray, typesOfNeuronsPerLayerArray, percentOfNeuronsPerLayerArray);
        neuronArray = newNetwork.generateNetwork();
    }

    public static void addSynapses(){
        println("Adding Synapses...");
        Instant start = Instant.now();
        int numBatches = 100;
        CountDownLatch latch = new CountDownLatch(numBatches);
        ExecutorService taskExecutor = Executors.newFixedThreadPool(numCPUs);
        for(int i=0; i<numBatches; i++) {
            taskExecutor.submit(new SynapseBatch(latch, i*neuronArray.length/numBatches, ((i+1)*neuronArray.length/numBatches)-1));
        }

        try {
            latch.await();
        } catch (InterruptedException E) {
            println("Interrupted thread during addSynapses");
        }

        Instant finish = Instant.now();
        println("Finished adding synapses in "+Duration.between(start,finish).toSeconds()+" Seconds");
    }

    static class SynapseBatch implements Runnable{
        private CountDownLatch latch;
        private int startNeuron;
        private int endNeuron;

        public SynapseBatch(CountDownLatch latch, int startNeuron, int endNeuron){
            this.latch = latch;
            this.startNeuron = startNeuron;
            this.endNeuron = endNeuron;
        }

        public void run(){
            Instant start = Instant.now();
            println("Started synapsing neurons: "+startNeuron+"-"+endNeuron);
            for(int i=startNeuron; i<=endNeuron; i++){
                int previousLoopNeurons=0;
                for(int j=0; j<neuronsPerLayerArray.length; j++){ //TODO implement parameter lists for synapsing other layers... Update for total distance?
                    if(i<neuronsPerLayerArray[j]+previousLoopNeurons){ //sequentially figures out which layer neurons are in
                        double alpha=0;
                        double theta=0;
                        int randomNeuronID;
                        double radialDistance;
                        int totalNumberOfConnections=100;
                        if(neuronArray[i].prebuiltClass.equals(INH)){totalNumberOfConnections = 80;}
                        int numberOfConnections = 0;
                        while(numberOfConnections < totalNumberOfConnections) {
                            if (j == 0) {
                                randomNeuronID = randomInt(neuronArray.length-numberTargetCells);
                                radialDistance = neuronArray[i].getRadialDistance(neuronArray[randomNeuronID]);
                                // ***** LAYER 4 CONNECTIVITY *****
                                if (neuronArray[i].prebuiltClass.equals(EXC)) {
                                    if (neuronArray[randomNeuronID].getZ() == 3) {
                                        if (neuronArray[randomNeuronID].prebuiltClass.equals(EXC)) {
                                            alpha = .0139;
                                            theta = 207.7;
                                        } else {
                                            alpha = .0148;
                                            theta = 191.8;
                                        }
                                    }
                                    if (neuronArray[randomNeuronID].getZ() == 2) {
                                        if (neuronArray[randomNeuronID].prebuiltClass.equals(EXC)) {
                                            alpha = .0174;
                                            theta = 154.4;
                                        } else {
                                            alpha = .0197;
                                            theta = 131.5;
                                        }
                                    }

                                }
                                else{
                                    if (neuronArray[randomNeuronID].getZ() == 3) {
                                        if (neuronArray[randomNeuronID].prebuiltClass.equals(EXC)) {
                                            alpha = .0126;
                                            theta = 237.5;
                                        } else {
                                            alpha = .0119;
                                            theta = 256.4;
                                        }
                                    }
                                }
                                if(!(alpha==0)) {
                                    double probabilityOfConnection = Math.exp(-alpha * Math.pow(Math.pow(theta, 2) + Math.pow(radialDistance*1000, 2), .5));
                                    if (randomDouble(1) < probabilityOfConnection) {
                                        neuronArray[i].addSynapse(new Synapse(randomNeuronID, true, excitatoryWeight));
                                        numberOfConnections++;
                                    }
                                }
                            }
                            if (j == 1) {
                                // ***** LAYER 2/3 CONNECTIVITY *****
                                randomNeuronID = randomInt(neuronArray.length);
                                radialDistance = neuronArray[i].getRadialDistance(neuronArray[randomNeuronID]);
                            }
                        }
                        break;
                    }
                }
            }
            Instant finish = Instant.now();
            println("Finished synapsing neurons: "+startNeuron+"-"+endNeuron+" in "+ Duration.between(start,finish).toSeconds()+" Seconds");
            latch.countDown();
        }
    }

    public static void addElectrodes(){
        Electrode singleElectrode = new Electrode(electrodeX, electrodeY, electrodeZ, electrodeRadius, electrodeFrequency, electrodeCurrent, electrodeTimeOffset, neuronArray);
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
