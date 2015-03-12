package bangor.aiia.jge.ps;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class BinPack {

	public static void main(String[] args) throws IOException
	{
		int size = 0;
		int count = 0;
		// TODO Auto-generated method stub
		Scanner kb = new Scanner(System.in);       
        System.out.println(" Please enter the file name. " );
        String filename = kb.nextLine();
        InputStream input = new FileInputStream("/Users/Borse/Graduation/Evolutionary_Algorithms/jGE/jge/src/bangor/aiia/jge/ps/" + filename);
        List<Integer> in = new ArrayList<Integer>(); 
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
         
         String line; 
        while((line = reader.readLine()) != null){
        	if(count < 2){        		
        		if(count == 0){count++;}
        		else{
        			String[] splited = line.split("\\s+");
        			size = Integer.parseInt(splited[0]);
        			count++;
        		}
        	}
        	else{
        	in.add(Integer.parseInt(line));
        	}
        }
        reader.close();
        Collections.shuffle(in); // sort input by size (big to small)
        FirstFit ffd = new FirstFit(in, size);
        testBinPacking(ffd, "first fit");
        BestFit bfd = new BestFit(in, size);
        testBinPacking(bfd, "best fit");
        WorstFit wfd = new WorstFit(in, size);
        testBinPacking(wfd, "worst fit");
    }
	
	 private static void testBinPacking(AbstractBinPacking algo, String algoName) {
	        long startTime;
	        long estimatedTime;

	        startTime = System.currentTimeMillis();
	        System.out.println("needed bins (" + algoName + "): " + algo.getResult());
	        algo.printBestBins();
	        estimatedTime = System.currentTimeMillis() - startTime;
	        System.out.println("in " + estimatedTime + " ms");

	        System.out.println("\n\n");
	    }
}
