//TODO Add Java-docs for all classes/functions

package simulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    static final boolean silentMode = true; // Silents output for each timestep/synapse/etc

    //  *** ELECTRODE VARIABLES TO CHANGE ***
    static int numElectrodes = 2;
    static double[] electrodeX = {0, 5};
    static double[] electrodeY =  {0, 5};
    static double[] electrodeZ =  {0, 0};
    static double[] electrodeRadius =  {.1, .1};
    static double[] electrodeFrequency =  {20, 20};
    static double[] electrodeCurrent =  {1000, 1000};
    static double[] electrodeTimeOffset =  {0, 0};

    // *** Simulation Variables to Change ***
    static final int timeSteps = 3000;
    static final int numberLayers = 3;
    static final int layerSizeX = 5;
    static final int layerSizeY = 5;
    static final int numberTargetCells = 10000;
    static final double excitatoryWeight = .5;
    static final double inhibitoryWeight = .5;

    // *** Program sets/changes these variables ***
    static int numCPUs;
    static int[] neuronsPerLayerArray = new int[numberLayers];
    //static ArrayList<Electrode> electrodeArray = new ArrayList<>(); //TODO change to an array list for multiple electrodes...
    static Electrode[] electrodeArray = new Electrode[2];
    static int[] neuronsEffectedByElectrode = new int[electrodeArray.length];
    static int[] targetLayerSpikes = new int[numberTargetCells];
    static int numberOfDifferentTargetCellsSpiked=0;
    static Neuron[] neuronArray;
    static int currentTimeStep;
    static int numSimulations = 0;


    public static void main(String[] args) {
        try{addOutputToFile("Starting Simulations...  *New Network Created*");}catch(Exception e){println("Failed to addOutputToFile");}

        Instant start = Instant.now();
        println("Cores detected: "+Runtime.getRuntime().availableProcessors());
        numCPUs = Runtime.getRuntime().availableProcessors();
        createNetwork();
        addSynapses();

        // ** Electrode current = 1000
        mainLoop();

        electrodeFrequency[0] = 5;
        electrodeFrequency[1] = 5;
        mainLoop();

        electrodeFrequency[0] = 1;
        electrodeFrequency[1] = 1;
        mainLoop();

        //Set to layer 4
        electrodeZ[0] = 3;
        electrodeZ[1] = 3;

        electrodeFrequency[0] = 20;
        electrodeFrequency[1] = 20;
        mainLoop();

        electrodeFrequency[0] = 5;
        electrodeFrequency[1] = 5;
        mainLoop();

        electrodeFrequency[0] = 1;
        electrodeFrequency[1] = 1;
        mainLoop();

        // ** Electrode current = 750
        electrodeZ[0] = 0;
        electrodeZ[1] = 0;
        electrodeCurrent[0] = 750;
        electrodeCurrent[1] = 750;

        electrodeFrequency[0] = 20;
        electrodeFrequency[1] = 20;
        mainLoop();

        electrodeFrequency[0] = 5;
        electrodeFrequency[1] = 5;
        mainLoop();

        electrodeFrequency[0] = 1;
        electrodeFrequency[1] = 1;
        mainLoop();

        // Set to layer 4
        electrodeZ[0] = 3;
        electrodeZ[1] = 3;

        electrodeFrequency[0] = 20;
        electrodeFrequency[1] = 20;
        mainLoop();

        electrodeFrequency[0] = 5;
        electrodeFrequency[1] = 5;
        mainLoop();

        electrodeFrequency[0] = 1;
        electrodeFrequency[1] = 1;
        mainLoop();

        // ** Electrode current = 500
        electrodeZ[0] = 0;
        electrodeZ[1] = 0;
        electrodeCurrent[0] = 500;
        electrodeCurrent[1] = 500;

        electrodeFrequency[0] = 20;
        electrodeFrequency[1] = 20;
        mainLoop();

        electrodeFrequency[0] = 5;
        electrodeFrequency[1] = 5;
        mainLoop();

        electrodeFrequency[0] = 1;
        electrodeFrequency[1] = 1;
        mainLoop();

        // Set to layer 4
        electrodeZ[0] = 3;
        electrodeZ[1] = 3;

        electrodeFrequency[0] = 20;
        electrodeFrequency[1] = 20;
        mainLoop();

        electrodeFrequency[0] = 5;
        electrodeFrequency[1] = 5;
        mainLoop();

        electrodeFrequency[0] = 1;
        electrodeFrequency[1] = 1;
        mainLoop();

        // ** Electrode current = 250
        electrodeZ[0] = 0;
        electrodeZ[1] = 0;
        electrodeCurrent[0] = 250;
        electrodeCurrent[1] = 250;

        electrodeFrequency[0] = 20;
        electrodeFrequency[1] = 20;
        mainLoop();

        electrodeFrequency[0] = 5;
        electrodeFrequency[1] = 5;
        mainLoop();

        electrodeFrequency[0] = 1;
        electrodeFrequency[1] = 1;
        mainLoop();

        // Set to layer 4
        electrodeZ[0] = 3;
        electrodeZ[1] = 3;

        electrodeFrequency[0] = 20;
        electrodeFrequency[1] = 20;
        mainLoop();

        electrodeFrequency[0] = 5;
        electrodeFrequency[1] = 5;
        mainLoop();

        electrodeFrequency[0] = 1;
        electrodeFrequency[1] = 1;
        mainLoop();

        // ** Electrode current = 125
        electrodeZ[0] = 0;
        electrodeZ[1] = 0;
        electrodeCurrent[0] = 125;
        electrodeCurrent[1] = 125;

        electrodeFrequency[0] = 20;
        electrodeFrequency[1] = 20;
        mainLoop();

        electrodeFrequency[0] = 5;
        electrodeFrequency[1] = 5;
        mainLoop();

        electrodeFrequency[0] = 1;
        electrodeFrequency[1] = 1;
        mainLoop();

        // Set to layer 4
        electrodeZ[0] = 3;
        electrodeZ[1] = 3;

        electrodeFrequency[0] = 20;
        electrodeFrequency[1] = 20;
        mainLoop();

        electrodeFrequency[0] = 5;
        electrodeFrequency[1] = 5;
        mainLoop();

        electrodeFrequency[0] = 1;
        electrodeFrequency[1] = 1;
        mainLoop();

        Instant finish = Instant.now();
        try{addOutputToFile("Handled "+numSimulations+" in "+Duration.between(start,finish).toSeconds()+" seconds");}catch(Exception e){println("Failed to addOutputToFile");}
    }

    public static void mainLoop(){
        Instant start = Instant.now();

        addElectrodes();

        if(sumIntegerArray(neuronsEffectedByElectrode) >0) {
            numSimulations++;
            runSimulation();

            Instant finish = Instant.now();
            println("Finished " + timeSteps + " timesteps in +" + Duration.between(start, finish).toSeconds() + " Seconds");
            for (int spikes : targetLayerSpikes)
                if (spikes > 0) numberOfDifferentTargetCellsSpiked++;
            try {
                addOutputToFile(sumIntegerArray(neuronsEffectedByElectrode) + "," + electrodeX + "," + electrodeY + "," + electrodeZ + "," + electrodeRadius + "," + electrodeFrequency + "," + electrodeCurrent + "," + timeSteps + "," + numberLayers + "," + layerSizeX + "," + layerSizeY +
                        "," + numberTargetCells + "," + excitatoryWeight + "," + inhibitoryWeight + "," + sumIntegerArray(targetLayerSpikes) + "," + numberOfDifferentTargetCellsSpiked + "," + numCPUs + "," + Duration.between(start, finish).toSeconds());
            } catch (Exception e) {
                println("Exception thrown when writing output");
            }

            String allTargetCellOutputs = "";
            for (int cellSpikes : targetLayerSpikes)
                allTargetCellOutputs = (allTargetCellOutputs + cellSpikes + ",");

            try {
                addOutputToFile(allTargetCellOutputs);
            } catch (Exception e) {
                println("Exception thrown when writing output");
            }

            numberOfDifferentTargetCellsSpiked = 0;
            for (int i = 0; i < targetLayerSpikes.length; i++) {
                targetLayerSpikes[i] = 0;
            }

            resetNeurons();
            System.gc();
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                println("Failed to sleep for garbage collection");
            }

        }
    }

    public static void addOutputToFile(String textToAppend) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(
                new FileWriter("outputFile.csv", true)  //Set true for append mode
        );
        writer.newLine();   //Add new line
        writer.write(textToAppend);
        writer.close();
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
        int numBatches = (numberTargetCells/10)>(numCPUs*2) ? numberTargetCells/10 : numCPUs;
        CountDownLatch latch = new CountDownLatch(numBatches);
        ExecutorService taskExecutor = Executors.newFixedThreadPool(numCPUs);
        for(int i=0; i<numBatches; i++) {
            taskExecutor.submit(new SynapseBatch(latch, i*neuronArray.length/numBatches, ((i+1)*neuronArray.length/numBatches)-1));
        }
        taskExecutor.shutdown();
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
            if(!silentMode)
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
                        boolean excitatory=false;
                        if(j!=2) {
                            while (numberOfConnections < totalNumberOfConnections) {
                                if (j == 0) {
                                    randomNeuronID = randomInt(neuronArray.length - numberTargetCells);
                                    radialDistance = neuronArray[i].getRadialDistance(neuronArray[randomNeuronID]);
                                    // ***** LAYER 4 CONNECTIVITY *****
                                    if (neuronArray[i].prebuiltClass.equals(EXC)) {
                                        excitatory = true;
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

                                    } else {
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
                                    if (!(alpha == 0)) {
                                        double probabilityOfConnection = Math.exp(-alpha * Math.pow(Math.pow(theta, 2) + Math.pow(radialDistance * 1000, 2), .5));
                                        if (randomDouble(1) < probabilityOfConnection) {
                                            neuronArray[i].addSynapse(new Synapse(randomNeuronID, excitatory, excitatory ? excitatoryWeight:inhibitoryWeight));
                                            numberOfConnections++;
                                        }
                                    }
                                }
                                if (j == 1) {
                                    // ***** LAYER 2/3 CONNECTIVITY *****
                                    randomNeuronID = randomInt(neuronArray.length);
                                    radialDistance = neuronArray[i].getRadialDistance(neuronArray[randomNeuronID]);
                                    if (neuronArray[i].prebuiltClass.equals(EXC)) {
                                        //TODO Fix this simplification:
                                        // Paper bases excitatory connections in layer 2/3 off functional connectivity metrics,
                                        // simplified by copying metrics found between layer 4 and 2/3
                                        excitatory = true;
                                        if (neuronArray[randomNeuronID].getZ() == 2) {
                                            if (neuronArray[randomNeuronID].prebuiltClass.equals(EXC)) {
                                                alpha = .0139;
                                                theta = 207.7;
                                            } else {
                                                alpha = .0148;
                                                theta = 191.8;
                                            }
                                        }
                                        if (neuronArray[randomNeuronID].getZ() == 4) {
                                            if (neuronArray[randomNeuronID].prebuiltClass.equals(EXC)) {
                                                alpha = .0174;
                                                theta = 154.4;
                                            } else {
                                                alpha = .0197;
                                                theta = 131.5;
                                            }
                                        }
                                        if (!(alpha == 0)) {
                                            double probabilityOfConnection = Math.exp(-alpha * Math.pow(Math.pow(theta, 2) + Math.pow(radialDistance * 1000, 2), .5));
                                            if (randomDouble(1) < probabilityOfConnection) {
                                                neuronArray[i].addSynapse(new Synapse(randomNeuronID, excitatory, excitatory ? excitatoryWeight:inhibitoryWeight));
                                                numberOfConnections++;
                                            }
                                        }
                                    } else {
                                        if (neuronArray[randomNeuronID].getZ() == 2) {
                                            if (neuronArray[randomNeuronID].prebuiltClass.equals(EXC)) {
                                                alpha = .0149;
                                                theta = 189.5;
                                            } else {
                                                alpha = .0150;
                                                theta = 188.61;
                                            }
                                            double probabilityOfConnection = Math.exp(-alpha * Math.pow(Math.pow(theta, 2) + Math.pow(radialDistance * 1000, 2), .5));
                                            if (randomDouble(1) < probabilityOfConnection) {
                                                neuronArray[i].addSynapse(new Synapse(randomNeuronID, excitatory, excitatory ? excitatoryWeight:inhibitoryWeight));
                                                numberOfConnections++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                    previousLoopNeurons += neuronsPerLayerArray[j];
                }
            }
            Instant finish = Instant.now();
            if(!silentMode)
                println("Finished synapsing neurons: "+startNeuron+"-"+endNeuron+" in "+ Duration.between(start,finish).toSeconds()+" Seconds");
            latch.countDown();
        }
    }

    public static void addElectrodes(){
        for(int i=0; i < numElectrodes; i++) {
            Electrode singleElectrode = new Electrode(electrodeX[i], electrodeY[i], electrodeZ[i], electrodeRadius[i], electrodeFrequency[i], electrodeCurrent[i], electrodeTimeOffset[i], neuronArray);
            electrodeArray[i] = singleElectrode;
            neuronsEffectedByElectrode[i] = singleElectrode.neuronsInRange.size();
            System.gc();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                println("Failed to sleep for garbage collection");
            }
        }

    }

    public static void runSimulation(){
        for(currentTimeStep = 0; currentTimeStep < timeSteps; currentTimeStep++) {
            updateElectrodes();
            updateNeurons();
        }

        int numberOfTargetCellsFired = 0;
        for(int numberOfSpikes: targetLayerSpikes)
            if(numberOfSpikes>0)
                numberOfTargetCellsFired++;

        println("Number of target layer spikes: "+ sumIntegerArray(targetLayerSpikes));
        println("Number of different cells that have fired: "+numberOfTargetCellsFired);
        println("");
    }

    public static void updateElectrodes(){
        for(Electrode electrode: electrodeArray) {
            if (currentTimeStep % electrode.frequency == 0) {
                if(!silentMode)
                    println("Electrode pulse...");
                Instant start = Instant.now();
                int numBatches = numCPUs;
                CountDownLatch latch = new CountDownLatch(numBatches);
                ExecutorService taskExecutor = Executors.newFixedThreadPool(numCPUs);
                for (int i = 0; i < numBatches; i++) {
                    taskExecutor.submit(new ElectrodeBatch(latch, electrode, i * electrode.neuronsInRange.size() / numBatches, ((i + 1) * electrode.neuronsInRange.size() / numBatches) - 1));
                }

                taskExecutor.shutdown();
                try {
                    latch.await();
                } catch (InterruptedException E) {
                    println("Interrupted thread during Electrode Pulse");
                }

                Instant finish = Instant.now();
                if(!silentMode)
                    println("Finished electrode pulse in " + Duration.between(start, finish).toSeconds() + " Seconds");
            }
        }
    }

    static class ElectrodeBatch implements Runnable {
        private CountDownLatch latch;
        private Electrode electrode;
        private int startNeuron;
        private int endNeuron;

        public ElectrodeBatch(CountDownLatch latch, Electrode electrode, int startNeuron, int endNeuron){
            this.latch = latch;
            this.electrode = electrode;
            this.startNeuron = startNeuron;
            this.endNeuron = endNeuron;
        }

        public void run() {
            for (int electrodeNeuronID = startNeuron; electrodeNeuronID <= endNeuron; electrodeNeuronID++) {
                neuronArray[electrode.neuronsInRange.get(electrodeNeuronID)].changeVoltage(electrode.voltageAtNeurons.get(electrodeNeuronID));
            }
            latch.countDown();
        }
    }

    public static void updateNeurons(){
        if(!silentMode)
            println("Updating Neurons for timestep: "+currentTimeStep);
        Instant start = Instant.now();
        int numBatches = numCPUs;
        CountDownLatch latch = new CountDownLatch(numBatches);
        ExecutorService taskExecutor = Executors.newFixedThreadPool(numCPUs);
        int previousLayerNeurons = 0;
        for(int j=0; j<numberLayers; j++){
            for(int i=0; i<numBatches; i++) {
                taskExecutor.submit(new NeuronUpdate(latch, (i*neuronsPerLayerArray[j]/numBatches+previousLayerNeurons), (((i+1)*neuronsPerLayerArray[j]/numBatches)-1)+previousLayerNeurons));
            }
            previousLayerNeurons += neuronsPerLayerArray[j];
        }
        taskExecutor.shutdown();
        try {
            latch.await();
        } catch (InterruptedException E) {
            println("Interrupted thread during neuron update");
        }

        Instant finish = Instant.now();
        if(!silentMode)
            println("Finished updating neurons in "+Duration.between(start,finish).toSeconds()+" Seconds");
    }

    static class NeuronUpdate implements Runnable{
        private CountDownLatch latch;
        private int startNeuron;
        private int endNeuron;

        public NeuronUpdate(CountDownLatch latch, int startNeuron, int endNeuron){
            this.latch = latch;
            this.startNeuron = startNeuron;
            this.endNeuron = endNeuron;
        }

        public void run(){
            Instant start = Instant.now();
            if(!silentMode)
                println("Started updating neurons: "+startNeuron+"-"+endNeuron);
            for (int currentNeuron = startNeuron; currentNeuron <= endNeuron; currentNeuron++) {
                if(neuronArray[currentNeuron].checkForSpike()){
                    if(currentNeuron > sumIntegerArray(neuronsPerLayerArray)-numberTargetCells-1)
                        targetLayerSpikes[currentNeuron-(sumIntegerArray(neuronsPerLayerArray)-numberTargetCells)]++;
                    for(Synapse synapse: neuronArray[currentNeuron].getSynapses()){
                        neuronArray[synapse.neuronID].changeVoltage(synapse.getWeight());
                    }
                }
            }
            Instant finish = Instant.now();
            if(!silentMode)
                println("Finished updating neurons: "+startNeuron+"-"+endNeuron+" in "+ Duration.between(start,finish).toSeconds()+" Seconds");
            latch.countDown();
        }
    }

    public static void resetNeurons(){
        for(Neuron neuron: neuronArray){
            neuron.resetNeuron();
        }
    }

}
