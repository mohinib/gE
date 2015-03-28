

import java.util.ArrayList;
import java.util.List;


public class WorstFit extends AbstractBinPacking {

    private List<Bin> bins = new ArrayList<Bin>();

    public WorstFit(List<Integer> in, int binSize) {
        super(in, binSize);
    }

    @Override
    public List<Bin> getResult() {        
       // bins.add(new Bin(binSize)); // add first bin
        for (Integer currentItem : in) {
            // iterate over bins and try to put the item into the best one it fits into 
        	//least space available in the bin
           int BinNumber = bins.size();
            int worstBin = -1;
            int worstBinAmount = binSize;
            for (int i = 0; i < BinNumber; i++){
            		if(worstBinAmount > bins.get(i).currentSize && (bins.get(i).currentSize + currentItem) <= bins.get(i).maxSize){
            			worstBinAmount = bins.get(i).currentSize;
            			worstBin = i;
            		}
            }
            if(worstBin == -1){
            	Bin newBin = new Bin(binSize);
                newBin.put(currentItem);
                bins.add(newBin);
            }
            else{
            	if(!bins.get(worstBin).put(currentItem))
             	{
             		//to change : log exception
             		System.out.println("something went wrong : bin not added");
             	}
            }
           
        }
        System.out.println("WF Bin Size: " +bins.size());
        return bins;
    }
    
    public List<Bin> addBin(List<Bin> targetBin) {      
        // bins.add(new Bin(binSize)); // add first bin
         for (Integer currentItem : in) {
             // iterate over bins and try to put the item into the best one it fits into 
         	//least space available in the bin
            int BinNumber = targetBin.size();
             int worstBin = -1;
             int worstBinAmount = binSize;
             for (int i = 0; i < BinNumber; i++){
             		if(worstBinAmount > targetBin.get(i).currentSize && (targetBin.get(i).currentSize + currentItem) <= binSize){
             			worstBinAmount = targetBin.get(i).currentSize;
             			worstBin = i;
             		}
             }
             if(worstBin == -1){
             	Bin newBin = new Bin(binSize);
                 newBin.put(currentItem);
                 targetBin.add(newBin);
             }
             else{
            	 if(!targetBin.get(worstBin).put(currentItem))
             	{
             		//to change : log exception
             		System.out.println("something went wrong : bin not added");
             	}
             }
            
         }
         System.out.println("WF TargetBin Size: " +targetBin.size());
         return targetBin;
     }
    @Override
    public void printBestBins(List<Bin> target) {
    	System.out.println("Bins:");
        /*if (bins.size() == in.size()) {
        	System.out.println("each item is in its own bin");
        } else {*/
            for (Bin bin : bins) {
            	System.out.println(bin.toString() + " Size= " +bin.currentSize);
            }
        //}
    }
}
