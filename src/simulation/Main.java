package simulation;

import java.io.BufferedWriter;
import java.io.File;
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
    //  *** ELECTRODE VARIABLES TO CHANGE ***
    static double electrodeX = 1;
    static double electrodeY = 1;
    static double electrodeZ = 1;
    static double electrodeRadius = .1;
    static double electrodeFrequency = 5;
    static double electrodeCurrent = 500;
    static double electrodeTimeOffset = 0;

    static final int timeSteps = 2000;
    static final int numberLayers = 3;
    static final int layerSizeX = 5;
    static final int layerSizeY = 5;
    static final int numberTargetCells = 10000;
    static final int excitatoryWeight = 5;
    static final int inhibitoryWeight = 5;

    static int numCPUs;
    static int[] neuronsPerLayerArray = new int[numberLayers];
    static ArrayList<Electrode> electrodeArray = new ArrayList<>();
    static int[] targetLayerSpikes = new int[numberTargetCells];
    static int numberOfDifferentTargetCellsSpiked=0;
    static Neuron[] neuronArray;
    static int currentTimeStep;
    private static File filePath;

    public static void main(String[] args) {
        // ** Electrode current = 500
        mainLoop();

        electrodeFrequency = 20;
        mainLoop();

        electrodeZ=3;
        electrodeFrequency = 5;
        mainLoop();

        electrodeFrequency = 20;
        mainLoop();

        // ** Electrode current = 100
        electrodeCurrent = 100;
        mainLoop();

        electrodeFrequency = 20;
        mainLoop();

        electrodeZ=3;
        electrodeFrequency = 5;
        mainLoop();

        electrodeFrequency = 20;
        mainLoop();

    }

    public static void mainLoop(){
        Instant start = Instant.now();

        println("Cores detected: "+Runtime.getRuntime().availableProcessors());
        numCPUs = Runtime.getRuntime().availableProcessors();
        createNetwork();
        addSynapses();
        addElectrodes();
        runSimulation();

        Instant finish = Instant.now();
        println("Finished "+timeSteps+" timesteps in +"+Duration.between(start,finish).toSeconds()+" Seconds");
        for(int spikes: targetLayerSpikes)
            if(spikes>0) numberOfDifferentTargetCellsSpiked ++;
        try{
            addOutputToFile(electrodeX+","+electrodeY+","+electrodeZ+","+electrodeRadius+","+electrodeFrequency+","+electrodeCurrent+","+timeSteps+","+numberLayers+","+layerSizeX+","+layerSizeY+
                    ","+numberTargetCells+","+excitatoryWeight+","+inhibitoryWeight+","+sumIntegerArray(targetLayerSpikes)+","+numberOfDifferentTargetCellsSpiked+","+numCPUs+","+Duration.between(start,finish).toSeconds());
        }catch(Exception e){
            println("Exception thrown when writing output");
        }

        numberOfDifferentTargetCellsSpiked = 0;
        for(int i=0; i<targetLayerSpikes.length; i++){
            targetLayerSpikes[i] = 0;
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
            //println("Started synapsing neurons: "+startNeuron+"-"+endNeuron);
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
            //println("Finished synapsing neurons: "+startNeuron+"-"+endNeuron+" in "+ Duration.between(start,finish).toSeconds()+" Seconds");
            latch.countDown();
        }
    }

    public static void addElectrodes(){
        Electrode singleElectrode = new Electrode(electrodeX, electrodeY, electrodeZ, electrodeRadius, electrodeFrequency, electrodeCurrent, electrodeTimeOffset, neuronArray);
        electrodeArray.add(singleElectrode);
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
                println("Electrode pulse...");
                Instant start = Instant.now();
                int numBatches = electrode.neuronsInRange.size()/numCPUs;
                CountDownLatch latch = new CountDownLatch(numBatches);
                ExecutorService taskExecutor = Executors.newFixedThreadPool(numCPUs);
                for (int i = 0; i < numBatches; i++) {
                    taskExecutor.submit(new ElectrodeBatch(latch, electrode, i * electrode.neuronsInRange.size() / numBatches, ((i + 1) * electrode.neuronsInRange.size() / numBatches) - 1));
                }

                try {
                    latch.await();
                } catch (InterruptedException E) {
                    println("Interrupted thread during Electrode Pulse");
                }

                Instant finish = Instant.now();
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
                //println(neuronID + ": " + electrode.voltageAtNeurons.get(count));
                neuronArray[electrode.neuronsInRange.get(electrodeNeuronID)].changeVoltage(electrode.voltageAtNeurons.get(electrodeNeuronID));
                //println(""+neuronArray[neuronID].getVoltage());
                latch.countDown();
            }
        }
    }

    public static void updateNeurons(){
        println("Updating Neurons for timestep: "+currentTimeStep);
        Instant start = Instant.now();
        int numBatches = 100;
        CountDownLatch latch = new CountDownLatch(numBatches);
        ExecutorService taskExecutor = Executors.newFixedThreadPool(numCPUs);
        for(int i=0; i<numBatches; i++) {
            taskExecutor.submit(new NeuronUpdate(latch, i*neuronArray.length/numBatches, ((i+1)*neuronArray.length/numBatches)-1));
        }

        try {
            latch.await();
        } catch (InterruptedException E) {
            println("Interrupted thread during neuron update");
        }

        Instant finish = Instant.now();
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
            //println("Started updating neurons: "+startNeuron+"-"+endNeuron);
            for (int currentNeuron = startNeuron; currentNeuron <= endNeuron; currentNeuron++) {
                if(neuronArray[currentNeuron].checkForSpike()){
                    if(currentNeuron > sumIntegerArray(neuronsPerLayerArray)-numberTargetCells)
                        targetLayerSpikes[currentNeuron-(sumIntegerArray(neuronsPerLayerArray)-numberTargetCells)]++;
                    for(Synapse synapse: neuronArray[currentNeuron].getSynapses()){
                        neuronArray[synapse.neuronID].changeVoltage(synapse.getWeight());
                    }
                }
            }
            Instant finish = Instant.now();
            //println("Finished updating neurons: "+startNeuron+"-"+endNeuron+" in "+ Duration.between(start,finish).toSeconds()+" Seconds");
            latch.countDown();
        }
    }

    }
