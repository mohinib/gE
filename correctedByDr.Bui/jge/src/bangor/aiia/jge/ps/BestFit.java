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
        for (Integer currentItem : in) {
            // iterate over bins and try to put the item into the best one it fits into 
        	//least space available in the bin
           int BinNumber = bins.size();
            int bestBin = -1;
            int bestBinAmount = 0;
            for (int i = 0; i < BinNumber; i++){
            		if(bestBinAmount < bins.get(i).currentSize && (bins.get(i).currentSize + currentItem) <= binSize){
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
            	if(!bins.get(bestBin).put(currentItem))
            	{
            		//to change : log exception
            		System.out.println("something went wrong : bin not added");
            	}
            }
           
        }
        //System.out.println("BF Bin Size: " +bins.size());
        return bins;
    }
    @Override
    public List<Bin> addBin(List<Bin> targetBin) {        
        for (Integer currentItem : in) {
            // iterate over bins and try to put the item into the best one it fits into 
        	//least space available in the bin
           int BinNumber = targetBin.size();
            int bestBin = -1;
            int bestBinAmount = 0;
            for (int i = 0; i < BinNumber; i++){
            		if(bestBinAmount < targetBin.get(i).currentSize && (targetBin.get(i).currentSize + currentItem) <= binSize){
            			bestBinAmount = targetBin.get(i).currentSize;
            			bestBin = i;
            		}
            }
            if(bestBin == -1){
            	Bin newBin = new Bin(binSize);
                newBin.put(currentItem);
                targetBin.add(newBin);
            }
            else{
            	if(!targetBin.get(bestBin).put(currentItem))
            	{
            		//to change : log exception
            		System.out.println("something went wrong : bin not added");
            	}
            }
           
        }
       // System.out.println("BF TargetBin Size: " +targetBin.size());
        return targetBin;
    }
}
