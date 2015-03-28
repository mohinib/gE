package bangor.aiia.jge.ps;

import java.util.ArrayList;
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
      //  System.out.println("fF Bin Size: " +bins.size());
        return bins;
    }

    @Override
    public List<Bin> addBin(List<Bin> targetbins){
    	for(Integer currentItem : in){
    		boolean putItem = false; // did we put the item in a bin?
            int currentBin = 0;
            while (!putItem) {
            	if (currentBin == targetbins.size()) {
                    // item did not fit in last bin. put it in a new bin
                    Bin newBin = new Bin(binSize);
                    newBin.put(currentItem);
                    targetbins.add(newBin);
                    putItem = true;
                } else if (targetbins.get(currentBin).put(currentItem)) {
                    // item fit in bin
                    putItem = true;
                } else {
                    // try next bin
                    currentBin++;                    
                }
            }
    	}
    //	System.out.println("FF Target Bins size = " +targetbins.size());
    	return targetbins;
    	
    }
}
