package simulation.general;

import java.util.List;

public class General {
    public static void println(String toPrint){
        System.out.println(toPrint);
    }

    public static int sumIntegerList(List<Integer> integerList){
        int returnSum=0;
        for(int i: integerList){
            returnSum = returnSum + i;
        }
        return returnSum;
    }
}
