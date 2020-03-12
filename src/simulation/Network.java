package simulation;

import simulation.general.General;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static simulation.general.General.*;

public class Network {
    int numberLayers = 0;
    int layerSizeX = 0;
    int layerSizeY = 0;
    List<Integer> neuronsPerLayer = new ArrayList<>();
    List<NetworkNeuron>[] typesOfNeuronsPerLayerArray;
    List<Double>[] percentOfNeuronsPerLayerArray;

    /**
     * Used to generate a network with the given parameters.  After inputting all the parameters, return a Neuron[] network through Network.generateNetwork()
     * @param numberLayers = number of layers in the model
     * @param layerSizeX = size (mm) in the x direction
     * @param layerSizeY = size (mm) in the y direction
     * @param neuronsPerLayerArray = number of neurons in each layer
     * @param typesOfNeuronsPerLayerArray = Neuron with dimensions [layer][neuron].  For example layer 0 could have 3 neuron types defined by [0][0], [0][1], and [0][2]
     * @param percentOfNeuronsPerLayerArray = Percentage of the Neuron types defined in typesOfNeuronsPerLayerArray. Say 50 percent of layer 0 neurons are of the type defined by typesOfNeuronsArray[0][0]. percentOfNeuronsPerLayerArray[0][0] should equal.5
     */
    public Network(int numberLayers, int layerSizeX, int layerSizeY, int[] neuronsPerLayerArray, List<NetworkNeuron>[] typesOfNeuronsPerLayerArray, List<Double>[] percentOfNeuronsPerLayerArray){
        if(checkDimensions(numberLayers, neuronsPerLayerArray, typesOfNeuronsPerLayerArray, percentOfNeuronsPerLayerArray)) {
            this.numberLayers = numberLayers;
            this.layerSizeX = layerSizeX;
            this.layerSizeY = layerSizeY;
            this.typesOfNeuronsPerLayerArray = typesOfNeuronsPerLayerArray;
            for (int i = 0; i < numberLayers; i++) {
                this.neuronsPerLayer.add(neuronsPerLayerArray[i]);
            }
            this.typesOfNeuronsPerLayerArray = typesOfNeuronsPerLayerArray;
            this.percentOfNeuronsPerLayerArray = percentOfNeuronsPerLayerArray;

            for(int i=0; i<this.percentOfNeuronsPerLayerArray.length; i++){
                for(int j=1; j<this.percentOfNeuronsPerLayerArray[i].size(); j++){
                    this.percentOfNeuronsPerLayerArray[i].set(j, this.percentOfNeuronsPerLayerArray[i].get(j)+this.percentOfNeuronsPerLayerArray[i].get(j-1)); //converts to cumulative percentages
                }
            }
        }
    }

    public boolean checkDimensions(int numberLayers, int[] neuronsPerLayerArray, List<NetworkNeuron>[] typesOfNeuronsPerLayerArray, List<Double>[] percentOfNeuronsPerLayerArray){
        if(neuronsPerLayerArray.length != numberLayers){
            println("ERROR - Length of \"neuronsPerLayerArray\" must be equal to \"numberLayer\"");
            return false;
        }

        if(typesOfNeuronsPerLayerArray.length != numberLayers){
            println("ERROR - Length of \"typesOfNeuronsPerLayerArray\" must be equal to \"numberLayer\"");
            println("(At least one neuron type per layer");
            return false;
        }

        if(percentOfNeuronsPerLayerArray.length != numberLayers){
            println("ERROR - Length of \"percentOfNeuronsPerLayerArray\" must be equal to \"numberLayer\"");
            return false;
        }

        for(int i=0; i<numberLayers; i++){
            if(typesOfNeuronsPerLayerArray[i].size() != percentOfNeuronsPerLayerArray[i].size()){
                println("ERROR- \"typesOfNeuronsPerLayerArray\" must have same dimensions as \"percentOfNeuronsPerLayerArray\"");
                return false;
            }
            if(sumDoubleList(percentOfNeuronsPerLayerArray[i]) != 1){
                println("Error, percentages in layer "+i+" must add up to 1.00");
            }
        }

        return true;
    }

    public Neuron[] generateNetwork(){
        Neuron[] returnNetwork = new Neuron[General.sumIntegerList(this.neuronsPerLayer)];
        int neuronID = 0;

        for (int k=0; k < numberLayers; k++) { // cycle through layers
            for(int i=0; i < neuronsPerLayer.get(k); i++){
                for(int j=0; j<this.percentOfNeuronsPerLayerArray[k].size(); j++){
                    if(i< this.percentOfNeuronsPerLayerArray[k].get(j)*neuronsPerLayer.get(k)){
                        returnNetwork[neuronID] = new Neuron(this.typesOfNeuronsPerLayerArray[k].get(j).neuronType, new Location(ThreadLocalRandom.current().nextDouble(layerSizeX),ThreadLocalRandom.current().nextDouble(layerSizeY),randomDouble(this.typesOfNeuronsPerLayerArray[k].get(j).meanZ,this.typesOfNeuronsPerLayerArray[k].get(j).stdDevZ)));
                        //println(returnNetwork[neuronID].location.x+","+returnNetwork[neuronID].location.y+","+returnNetwork[neuronID].location.z);
                        neuronID++;
                        break;
                    }
                }
            }
        }

        return returnNetwork;
    }

}
