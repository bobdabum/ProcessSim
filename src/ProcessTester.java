import java.util.ArrayList;
import java.util.Collections;


public class ProcessTester {
	public final static int NUM_PROCESSES = 25;
	public final static int START_SEED = 0;
	public static ArrayList<Process> pToBeRun, pHaveRun;
	
	public static void main(String[] args){
		pToBeRun = new ArrayList<Process>(NUM_PROCESSES);
		for(int i = START_SEED; i<START_SEED+NUM_PROCESSES; i++){
			pToBeRun.add(new Process(i));
		}
		Collections.sort(pToBeRun);
		pHaveRun = new ArrayList<Process>(NUM_PROCESSES);
	}
}
