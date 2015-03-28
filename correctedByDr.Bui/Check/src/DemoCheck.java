import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class DemoCheck {
	private static double calculateAverage(List<Integer> in) {
		Integer sum = 0;
		if (!in.isEmpty()) {
			for (Integer el : in) {
				sum += el;
			}
			return sum.doubleValue() / in.size();
		}
		return sum;
	}

	public static void main(String[] args) throws Exception {
		String[] content = null;
		Scanner kb = new Scanner(System.in);
		System.out.println(" Please enter the file name. ");
		String filename = kb.nextLine();
		InputStream input = new FileInputStream(
				"/Users/Borse/Graduation/Evolutionary_Algorithms/jGE/Check/src/"
						+ filename);

		List<List<Integer>> group = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		// Individual<String, String> obj = new Individual<String, String>();

		int[] size = null;
		String line, t;
		int instances = 0;
		while ((line = reader.readLine()) != null) {
			instances = Integer.parseInt(line);
			size = new int[instances];
			group = new ArrayList<List<Integer>>(instances);
			content = new String[instances];
			List<Integer> in = null;
			line = reader.readLine();
			for (int a = 0; a < instances; a++) {
				int index = 0;
				String con;
				if (line != null
						&& (line.charAt(0) == 't' || line.charAt(0) == 's' || line
								.charAt(0) == 'u')) {
					in = new ArrayList<Integer>();
					con = reader.readLine();
					content[a] = con;
					String[] splited = con.split("\\s+");
					if (line.charAt(0) == 's') {
						size[a] = Integer.parseInt(splited[0]);
					} else {
						size[a] = (int) (10 * Double.parseDouble(splited[0]));
					}
					while (index < Integer.parseInt(splited[1])) {
						if ((t = reader.readLine()) != null) {
							if (line.charAt(0) == 's') {
								in.add(Integer.parseInt(t));
							} else {
								in.add((int) (10 * Double.parseDouble(t)));
							}
						}
						index++;
					}
					group.add(in);
					line = reader.readLine();
				} else if (line == null) {
					break;
				}

			}
		}
		reader.close();
		File file = new File("heuristic_resultsSchollOn500.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		double sum=0;
		for (int b = 0; b < group.size(); b++) {
			int min_elem = Collections.min(group.get(b));
			int max_elem = Collections.max(group.get(b));
			double average_elem = calculateAverage(group.get(b));
			//Collections.sort(group.get(b), Collections.reverseOrder());
			//Collections.shuffle(group.get(b));
			FirstFit ff = new FirstFit(group.get(b), size[b]);
			List<Bin> newobj = new ArrayList<Bin>();
			newobj = ff.getResult();
			bw.write(content[b] + "FF Solution: " +newobj.size() +":\r \n");
			DemoD hd = new DemoD(newobj, min_elem, max_elem, average_elem,
					size[b]);
			sum+=hd.evaluate(bw);
		}
		double avg=sum/group.size();
		bw.write("Average for all: " +avg +"\r \n");
		bw.close();

	}
}