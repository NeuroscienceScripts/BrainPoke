package simulation;

import org.jfree.chart.ChartFactory;

import static simulation.general.General.println;

public class Main {
    public static void main(String[] args) {
        int timeSteps = 1000;
        Neuron singleNeuron = new Neuron(Neuron.PrebuiltNeuron.RS, new Location(0, 0, 0));

        double[] singleNeuronVoltages = new double[timeSteps];

        for (int t = 0; t < timeSteps; t++) {
            singleNeuron.changeVoltage(5);
            singleNeuron.update();
            singleNeuronVoltages[t] = singleNeuron.getVoltage();
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
