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


    }
}
