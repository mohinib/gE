package bangor.aiia.jge.ps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import bangor.aiia.jge.ps.Bin;

public class FirstFit extends AbstractBinPacking {

    private List<Bin> bins = new ArrayList<Bin>();
    
    public FirstFit(List<Integer> in, int binSize) {
        super(in, binSize);
    }

    @Override
    public List<Bin> getResult() {        
        bins.add(new Bin(binSize)); // add first bin
        for (Integer currentItem : in) {
            // iterate over bins and try to put the item into the first one it fits into
            boolean putItem = false; // did we put the item in a bin?
            int currentBin = 0;
            while (!putItem) {
                if (currentBin == bins.size()) {
                    // item did not fit in last bin. put it in a new bin
                    Bin newBin = new Bin(binSize);
                    newBin.put(currentItem);
                    bins.add(newBin);
                    putItem = true;
                } else if (bins.get(currentBin).put(currentItem)) {
                    // item fit in bin
                    putItem = true;
                } else {
                    // try next bin
                    currentBin++;
                }
            }
        }
        return bins;
    }

  
    
    @Override
    public void printBestBins() {
    	//try{
    	//FileWriter fileWriter = new FileWriter("result.txt");
            // Always wrap FileWriter in BufferedWriter.
       // BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
       // bufferedWriter.write("needed bins :" + getResult() + " Best result : " + bestResult);
        //bufferedWriter.write("Bins:");
    		System.out.println("Bins:");
        if (bins.size() == in.size()) {
        	//bufferedWriter.write("each item is in its own bin");
        	System.out.println("each item is in its own bin");
        } else {
            for (Bin bin : bins) {
            	//bufferedWriter.write(bin.toString()+ "\n");
            	System.out.println(bin.toString());
            }
        }
    	//}
    	//catch(IOException ex){
    		//System.out.println("Error writing to file");
    	//}
    }
}
