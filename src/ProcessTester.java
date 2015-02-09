import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class ProcessTester {
	public final static int NUM_PROCESSES = 25;
	public final static int START_SEED = 0;
	public static ArrayList<Process> pToRun, pHaveRun;
	
	public static void main(String[] args){
		pToRun = new ArrayList<Process>(NUM_PROCESSES);
		for(int i = START_SEED; i<START_SEED+NUM_PROCESSES; i++){
			pToRun.add(new Process(i));
		}
		Collections.sort(pToRun);
		pHaveRun = new ArrayList<Process>(NUM_PROCESSES);
	}
	public static void runFCFSnonpre(){
		//runs for 100 time quanta
		for(int i =0;i<100;i++){
			if(pToRun.size()>0){
				//runs first process in queue. removes from toRun if process has finished(i.e. method returns false)
				if(pToRun.get(0).run(i));
				else
					pHaveRun.add(pToRun.remove(0));
				
				//ages rest
				for(int j=1; j<pToRun.size();j++)
					pToRun.get(j).ageProcess();
			}
			else
				i = 100;
		}
		//runs any remaining processes that have started (i.e. response time>=0)
	}
	public static void runSJFnonpre(){
		
	}
	public static void runSRTnonpre(){
		
	}
	public static void runRRpre(){
		
	}
	public static void runHPFpre(){
		
	}
	public static void runHPFnonpre(){
		
	}
	public static void printToFile(){
		try {
			FileWriter fw = new FileWriter("statistics.txt", true);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}