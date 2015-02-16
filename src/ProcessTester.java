import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class ProcessTester {
	public final static int NUM_PROCESSES = 30;
	public final static int NUM_RUNS = 5;
	public final static int NUM_PRIORITIES = 4;
	public final static int NUM_QUANTA = 100;
	public final static int START_SEED = 1000;
	public static double avgTurnAround, avgWaitTime, avgResponseTime, avgThroughput;
	public static ArrayList<Process> pToRun, pHaveRun;
	public static String processStr = "";

	public static void main(String[] args){
		printHeaderToFile("FCFS non-preemptive");
		for(int run = 0; run< NUM_RUNS; run++){
			reset(run);
			runFCFSnonpre();
			printRunToFile(run);
		}
		printFinalToFile();
		
				
		printHeaderToFile("SJF non-preemptive");
		for(int run = 0; run< NUM_RUNS; run++){
			reset(run);
			runSJFnonpre();
			printRunToFile(run);
		}
		printFinalToFile();

		printHeaderToFile("SRT non-preemptive");
		for(int run = 0; run< NUM_RUNS; run++){
			reset(run);
			runSRTnonpre();
			printRunToFile(run);
		}
		printFinalToFile();

		printHeaderToFile("RR preemptive");
		for(int run = 0; run< NUM_RUNS; run++){
			reset(run);
			runRRpre();
			printRunToFile(run);
		}
		printFinalToFile();
		
		printHeaderToFile("HPF preemptive");
		for(int run = 0; run< NUM_RUNS; run++){
			reset(run);
			runHPFpre();
			printRunToFile(run);
		}
		printFinalToFile();
		
		printHeaderToFile("HPF non-preemptive");
		for(int run = 0; run< NUM_RUNS; run++){
			reset(run);
			runHPFnonpre();
			printRunToFile(run);
		}
		printFinalToFile();
	}
	public static void runFCFSnonpre(){
		ArrayList<Process> runQueue = new ArrayList<Process>(NUM_PROCESSES);

		//runs for 100 time quanta
		for(int i =0;i<NUM_QUANTA;i++){
			//Add any processes from toRun list to the runQueue
			while(pToRun.size()>0 && pToRun.get(0).getStartTime()<=i)
				runQueue.add(pToRun.remove(0));

			//move processes  and any additional actions
			//i.e. preemptive algorithms moving processes around

			//Run queue for one time quanta
			if(runQueue.size()>0){
				//runs first process in queue. removes if process has finished(i.e. method returns false)
				processStr+=runQueue.get(0).getProcessNumber()+",";

				if(runQueue.get(0).run(i));
				else
					pHaveRun.add(runQueue.remove(0));

				//ages rest
				for(int j=1; j<runQueue.size();j++)
					runQueue.get(j).ageProcess();
			}
			else
				processStr+="N,";
		}

		int i = NUM_QUANTA;
		//runs any remaining processes that have started (i.e. response time>=0)
		while(runQueue.size()>0){
			if(runQueue.get(0).getResponseTime()>=0){
				processStr+=runQueue.get(0).getProcessNumber()+",";
				if(runQueue.get(0).run(i));
				else
					pHaveRun.add(runQueue.remove(0));
			}
			else
				runQueue.remove(0);
			i++;
		}
	}
	private static void runSJFnonpre(){
                ArrayList<Process> runQueue = new ArrayList<Process>(NUM_PROCESSES);

		//runs for 100 time quanta
		for(int i =0;i<NUM_QUANTA;i++){
			//Add any processes from toRun list to the runQueue
			while(pToRun.size()>0 && pToRun.get(0).getStartTime()<=i)
				runQueue.add(pToRun.remove(0));

			//move processes  and any additional actions
                        int startPoint = (runQueue.size() > 0) ? 1:0;
                        Collections.sort(runQueue.subList(startPoint, runQueue.size()), Process.compareByRunDuration);

			//i.e. preemptive algorithms moving processes around

			//Run queue for one time quanta
			if(runQueue.size()>0){
                                //System.out.println(runQueue.get(0).getProcessNumber() + " + " + runQueue.get(0).getRunDuration());
				//runs first process in queue. removes if process has finished(i.e. method returns false)
				processStr+=runQueue.get(0).getProcessNumber()+",";

				if(runQueue.get(0).run(i));
				else
					pHaveRun.add(runQueue.remove(0));

				//ages rest
				for(int j=1; j<runQueue.size();j++)
					runQueue.get(j).ageProcess();
			}
			else
				processStr+="N,";
		}
	}
	private static void runSRTnonpre(){
            ArrayList<Process> runQueue = new ArrayList<Process>(NUM_PROCESSES);

		//runs for 100 time quanta
		for(int i =0;i<NUM_QUANTA;i++){
			//Add any processes from toRun list to the runQueue
			while(pToRun.size()>0 && pToRun.get(0).getStartTime()<=i)
				runQueue.add(pToRun.remove(0));

			//move processes  and any additional actions
                        Collections.sort(runQueue.subList(0, runQueue.size()), Process.compareTimeRemaining);

			//i.e. preemptive algorithms moving processes around

			//Run queue for one time quanta
			if(runQueue.size()>0){
                                //System.out.println(runQueue.get(0).getProcessNumber() + " + " + runQueue.get(0).getRunDuration());
				//runs first process in queue. removes if process has finished(i.e. method returns false)
				processStr+=runQueue.get(0).getProcessNumber()+",";

				if(runQueue.get(0).run(i));
				else
					pHaveRun.add(runQueue.remove(0));

				//ages rest
				for(int j=1; j<runQueue.size();j++)
					runQueue.get(j).ageProcess();
			}
			else
				processStr+="N,";
                }
	}
        
	private static void runRRpre(){
		ArrayList<Process> runQueue = new ArrayList<Process>(NUM_PROCESSES);

		//runs for 100 time quanta
		for(int i =0;i<NUM_QUANTA;i++){
			//Add any processes from toRun list to the runQueue
			while(pToRun.size()>0 && pToRun.get(0).getStartTime()<=i)
				runQueue.add(pToRun.remove(0));

			//move processes  and any additional actions
			//i.e. preemptive algorithms moving processes around

			//Run queue for one time quanta
			if(runQueue.size()>0){
				//runs first process in queue. removes if process has finished(i.e. method returns false)
				processStr+=runQueue.get(0).getProcessNumber()+",";

				if(runQueue.get(0).run(i))
					runQueue.add(runQueue.remove(0));
				else
					pHaveRun.add(runQueue.remove(0));

				//ages rest
				for(int j=1; j<runQueue.size();j++)
					runQueue.get(j).ageProcess();
			}
			else
				processStr+="N,";
		}

		int i = NUM_QUANTA;
		//runs any remaining processes that have started (i.e. response time>=0)
		while(runQueue.size()>0){
			if(runQueue.get(0).getResponseTime()>=0){
				processStr+=runQueue.get(0).getProcessNumber()+",";
				if(runQueue.get(0).run(i))
					runQueue.add(runQueue.remove(0));
				else
					pHaveRun.add(runQueue.remove(0));
			}
			else
				runQueue.remove(0);
			i++;
		}
	}
	private static void runHPFpre(){
		ArrayList<ArrayList<Process>> runQueue = new ArrayList<ArrayList<Process>>(4);
		for(int i=0;i<4;i++)
			runQueue.add(new ArrayList<Process>(NUM_PROCESSES));

		//runs for 100 time quanta
		for(int i =0;i<NUM_QUANTA;i++){
			//Add any processes from toRun list to the runQueue
			while(pToRun.size()>0 && pToRun.get(0).getStartTime()<=i)
				runQueue.get(pToRun.get(0).getPriority()).add(pToRun.remove(0));

			//move processes  and any additional actions
			//i.e. preemptive algorithms moving processes around
			
			for(int j=1; j<runQueue.size(); j++)
			{
				ArrayList<Process> a = runQueue.get(j);
				if(a.size()==0 || a.get(0).getPriority()==0)
					continue;
				for (int k=0; k<a.size(); k++)
				{
					Process p = a.get(k);
					if (p.getAge() == 5)
					{
						p.increasePriority();
						runQueue.get(p.getPriority()).add(a.remove(k));
						k--;
					}
				}
			}
			
			boolean haveRun = false;
			//Run queue for one time quanta
			for(ArrayList<Process> a: runQueue)
			{
				if(a.size()==0)
					continue;
				else if(!haveRun){
					haveRun = true;
					//ages rest
					for(int j=1; j<a.size();j++)
						a.get(j).ageProcess();
					
					processStr+=a.get(0).getProcessNumber()+",";
					if(a.get(0).run(i))
						a.add(a.remove(0));
					else
						pHaveRun.add(a.remove(0));
				}
				else{
					for(Process p:a)
						p.ageProcess();					
				}
			}
			if(!haveRun)
				processStr+="N,";
		}
		
		int i = NUM_QUANTA;
		//runs any remaining processes that have started (i.e. response time>=0)
		for(ArrayList<Process> a: runQueue)
		{
			if(a.size()==0)
				continue;
			while(a.size()>0)
			{
				if(a.get(0).getResponseTime()>=0)
				{
					processStr+=a.get(0).getProcessNumber()+",";
					if(a.get(0).run(i))
						a.add(a.remove(0));
					else
						pHaveRun.add(a.remove(0));
				}
				else
					a.remove(0);
			}
		}
	}
	private static void runHPFnonpre(){
		ArrayList<ArrayList<Process>> runQueue = new ArrayList<ArrayList<Process>>(4);
		for(int i=0;i<4;i++)
			runQueue.add(new ArrayList<Process>(NUM_PROCESSES));

		//runs for 100 time quanta
		for(int i =0;i<NUM_QUANTA;i++){
			//Add any processes from toRun list to the runQueue
			while(pToRun.size()>0 && pToRun.get(0).getStartTime()<=i)
				runQueue.get(pToRun.get(0).getPriority()).add(pToRun.remove(0));

			//move processes  and any additional actions
			//i.e. preemptive algorithms moving processes around
			
			for(int j=1; j<runQueue.size(); j++)
			{
				ArrayList<Process> a = runQueue.get(j);
				if(a.size()==0 || a.get(0).getPriority()==0)
					continue;
				for (int k=0; k<a.size(); k++)
				{
					Process p = a.get(k);
					if (p.getAge() == 5)
					{
						p.increasePriority();
						runQueue.get(p.getPriority()).add(a.remove(k));
						k--;
					}
				}
			}
			
			boolean haveRun = false;
			//Run queue for one time quanta
			for(ArrayList<Process> a: runQueue)
			{
				if(a.size()==0)
					continue;
				else if(!haveRun){
					haveRun = true;
					//ages rest
					for(int j=1; j<a.size();j++)
						a.get(j).ageProcess();
					
					processStr+=a.get(0).getProcessNumber()+",";
					if(a.get(0).run(i));
					else
						pHaveRun.add(a.remove(0));
				}
				else{
					for(Process p:a)
						p.ageProcess();					
				}
			}
			if(!haveRun)
				processStr+="N,";
		}
		
		int i = NUM_QUANTA;
		//runs any remaining processes that have started (i.e. response time>=0)
		for(ArrayList<Process> a: runQueue)
		{
			if(a.size()==0)
				continue;
			while(a.size()>0)
			{
				if(a.get(0).getResponseTime()>=0)
				{
					processStr+=a.get(0).getProcessNumber()+",";
					if(a.get(0).run(i));
					else
						pHaveRun.add(a.remove(0));
				}
				else
					a.remove(0);
			}
		}
	}

	private static void printHeaderToFile(String algorithmType){
		avgResponseTime = 0;
		avgTurnAround = 0;
		avgWaitTime = 0;
		avgThroughput = 0;
		
		try {
			FileWriter fw = new FileWriter("statistics.txt", true);
			fw.write("Algorithm: "+algorithmType+System.getProperty("line.separator"));			
			System.out.println("Algorithm: "+algorithmType);
			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}		
	}
	private static void printRunToFile(int run){
		DecimalFormat df = new DecimalFormat("#.##");
		double turnAround=0;
		double waitTime = 0;
		double responseTime = 0;
		double throughput = pHaveRun.size();
		for(Process p: pHaveRun){
			turnAround += p.getTotalRunTime();
			waitTime += p.getWaitTime();
			responseTime += p.getResponseTime();
		}
		turnAround /= throughput;
		waitTime /= throughput;
		responseTime /= throughput;

		avgResponseTime+=responseTime/NUM_RUNS;
		avgWaitTime+=waitTime/NUM_RUNS;
		avgTurnAround+=turnAround/NUM_RUNS;
		avgThroughput+=throughput/NUM_RUNS;

		//write to file
		try {
			FileWriter fw = new FileWriter("statistics.txt", true);
			fw.write("Run: "+run+System.getProperty("line.separator"));
			fw.write("Process Time Line: "+processStr+System.getProperty("line.separator"));
			fw.write("Wait Time: "+df.format(waitTime)+", Response Time: "+df.format(responseTime)+
					", Turn Around: "+df.format(turnAround)+", Throughput: "+df.format(throughput)+System.getProperty("line.separator"));


			System.out.println("Run: "+run);
			System.out.println("Process Time Line: "+processStr);
			System.out.println("Wait Time: "+df.format(waitTime)+", Response Time: "+df.format(responseTime)+
					", Turn Around: "+df.format(turnAround)+", Throughput: "+df.format(throughput));
			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void printFinalToFile(){
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			FileWriter fw = new FileWriter("statistics.txt", true);
			fw.write(System.getProperty("line.separator")+"Final Statistics:"+System.getProperty("line.separator"));
			fw.write("Wait Time: "+df.format(avgWaitTime)+", Response Time: "+df.format(avgResponseTime)+
					", Turn Around: "+df.format(avgTurnAround)+", Throughput: "+df.format(avgThroughput)+System.getProperty("line.separator"));
			fw.write("-----------------------------------------------------------"+System.getProperty("line.separator"));
			System.out.println();
			System.out.println("Final Statistics:");
			System.out.println("Wait Time: "+df.format(avgWaitTime)+", Response Time: "+df.format(avgResponseTime)+
					", Turn Around: "+df.format(avgTurnAround)+", Throughput: "+df.format(avgThroughput));
			System.out.println("-----------------------------------------------------------\n");
			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Resets Everything	and creates new randomly generated process list for seed defined by run #.
	private static void reset(int run){
		Random r = new Random(run+START_SEED);		
		//resets everything
		processStr = "";
		pToRun = new ArrayList<Process>(NUM_PROCESSES);
		pHaveRun = new ArrayList<Process>(NUM_PROCESSES);

		for(int i = 0; i<NUM_PROCESSES; i++){
			pToRun.add(new Process(r.nextInt(NUM_PRIORITIES),r.nextInt(NUM_QUANTA),.1+ 9.9*r.nextDouble(),i));
		}

		Collections.sort(pToRun);		
	}
}