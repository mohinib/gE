package bangor.aiia.jge.ps;

import java.util.ArrayList;
import java.util.List;

import bangor.aiia.jge.ps.Bin;

public class BestFit extends AbstractBinPacking {

    private List<Bin> bins = new ArrayList<Bin>();

    public BestFit(List<Integer> in, int binSize) {
        super(in, binSize);
    }

    @Override
    public List<Bin> getResult() {        
       // bins.add(new Bin(binSize)); // add first bin
        for (Integer currentItem : in) {
            // iterate over bins and try to put the item into the best one it fits into 
        	//least space available in the bin
           int BinNumber = bins.size();
            int bestBin = -1;
            int bestBinAmount = 0;
            for (int i = 0; i < BinNumber; i++){
            		if(bestBinAmount < bins.get(i).currentSize && (bins.get(i).currentSize + currentItem) <= bins.get(i).maxSize){
            			bestBinAmount = bins.get(i).currentSize;
            			bestBin = i;
            		}
            }
            if(bestBin == -1){
            	Bin newBin = new Bin(binSize);
                newBin.put(currentItem);
                bins.add(newBin);
            }
            else{
            	bins.get(bestBin).put(currentItem);
            }
           
        }
        return bins;
    }
    
    @Override
    public void printBestBins() {
    	System.out.println("Bins:");
        if (bins.size() == in.size()) {
        	System.out.println("each item is in its own bin");
        } else {
            for (Bin bin : bins) {
            	System.out.println(bin.toString());
            }
        }
    }
}
