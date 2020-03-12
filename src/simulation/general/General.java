package simulation.general;

import java.util.List;
import java.util.Random;

public class General {
    public static void println(String toPrint){
        System.out.println(toPrint);
    }
    public static void print(String toPrint){
        System.out.print(toPrint);
    }

    public static int sumIntegerList(List<Integer> integerList){
        int returnSum=0;
        for(int i: integerList){
            returnSum = returnSum + i;
        }
        return returnSum;
    }

    public static double sumDoubleList(List<Double> doubleList){
        double returnSum=0;
        for(double i: doubleList){
            returnSum = returnSum + i;
        }
        return returnSum;
    }

    public static double randomDouble(double mean, double stdDev){
        Random r = new Random();
        return mean + stdDev*r.nextGaussian();
    }
}
