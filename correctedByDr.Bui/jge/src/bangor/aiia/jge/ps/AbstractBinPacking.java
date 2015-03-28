package bangor.aiia.jge.ps;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBinPacking {

    protected List<Integer> in;
    protected int binSize;

    public AbstractBinPacking(List<Integer> in, int binSize) {
        this.in = in;
        this.binSize = binSize;
    }

    /**
     * runs algorithm and returns needed bins.
     *
     * @return needed bins
     */
    public abstract List<Bin> getResult();

    /**
     * print the best bin packing determined by getResult().
     */
    //public abstract void printBestBins();
    
    /**
     * runs algorithm and returns needed bins after repacking.
     *
     * @return needed bins after repacking.
     */
    public abstract List<Bin> addBin(List<Bin> bin);
    
    public List<Bin> deepCopy(List<Bin> bins) {
        ArrayList<Bin> copy = new ArrayList<Bin>();
        for (int i = 0; i < bins.size(); i++) {
        	bins.get(i).all=false;
        	bins.get(i).visited=false;
            copy.add(bins.get(i).deepCopy());
        }
        return copy;
    }
}
