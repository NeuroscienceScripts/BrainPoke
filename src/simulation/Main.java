package simulation;

import org.jfree.chart.ChartFactory;

import java.util.ArrayList;
import java.util.List;

import static simulation.general.General.println;

public class Main {
    public static void main(String[] args) {
        int timeSteps = 1000;
        int numberNeurons = 3;

        Neuron[] neurons = new Neuron[numberNeurons];
        neurons[0] = (new Neuron(Neuron.PrebuiltNeuron.RS, new Location(0, 0, 0)));
        neurons[1] = (new Neuron(Neuron.PrebuiltNeuron.RS, new Location(0, 0, 0)));
        neurons[2] = (new Neuron(Neuron.PrebuiltNeuron.RS, new Location(0, 0, 0)));

        double[] singleNeuronVoltages = new double[timeSteps];

        neurons[0].addSynapse(new Synapse(1,true,20));
        neurons[1].addSynapse(new Synapse(2,true,20));


        List<Synapse> synapses = new ArrayList<Synapse>();
        for (int t = 0; t < timeSteps; t++) {
            neurons[0].changeVoltage(5);
            for(int i=0; i<numberNeurons; i++){
                if(neurons[i].update()){
                    synapses = neurons[i].getSynapses();
                    for(Synapse syn : synapses){
                        neurons[syn.neuronID].changeVoltage(syn.getWeight());
                    }
                }

            }
            singleNeuronVoltages[t] = neurons[2].getVoltage();
            println("***");
        }

        for (int t=0; t<timeSteps; t++){
            println(t + ": " + singleNeuronVoltages[t]+ " volts");
        }
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
