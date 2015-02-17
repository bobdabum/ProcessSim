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
	public static double[] avgTurnAroundHPF, avgWaitTimeHPF, avgResponseTimeHPF, avgThroughputHPF;
	public static ArrayList<Process> pToRun, pHaveRun;
	public static ArrayList<ArrayList<Process>> pHaveRunHPF;
	public static String[] processStrHPF;
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

		printHeaderToFile("SRT preemptive");
		for(int run = 0; run< NUM_RUNS; run++){
			reset(run);
			runSRTpre();
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
			printHPFRunToFile();
		}
		printHPFFinalToFile();
		printFinalToFile();

		printHeaderToFile("HPF non-preemptive");
		for(int run = 0; run< NUM_RUNS; run++){
			reset(run);
			runHPFnonpre();
			printRunToFile(run);
			printHPFRunToFile();
		}
		printHPFFinalToFile();
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
				//ages rest
				for(int j=1; j<runQueue.size();j++)
					runQueue.get(j).ageProcess();
				
				//runs process
				processStr+=runQueue.get(0).getProcessNumber()+",";
				if(runQueue.get(0).run(i));
				else
					pHaveRun.add(runQueue.remove(0));
				
				i++;
			}
			else
				runQueue.remove(0);
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
		
		int i = NUM_QUANTA;
		for(int j =0; j<runQueue.size();j++)
			if(runQueue.get(j).getResponseTime()<0){
				runQueue.remove(j);
				j--;
			}
		while(runQueue.size()>0){
			int startPoint = (runQueue.size() > 0) ? 1:0;
			Collections.sort(runQueue.subList(startPoint, runQueue.size()), Process.compareByRunDuration);
			
			processStr+=runQueue.get(0).getProcessNumber()+",";
			if(runQueue.get(0).run(i));
			else
				pHaveRun.add(runQueue.remove(0));

			//ages rest
			for(int j=1; j<runQueue.size();j++)
				runQueue.get(j).ageProcess();
			//increment time quanta
			i++;
		}
	}
	private static void runSRTpre(){
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
		for(int j =0; j<runQueue.size();j++)
			if(runQueue.get(j).getResponseTime()<0){
				runQueue.remove(j);
				j--;
			}
		while(runQueue.size()>0){
			Collections.sort(runQueue.subList(0, runQueue.size()), Process.compareTimeRemaining);

			processStr+=runQueue.get(0).getProcessNumber()+",";
			if(runQueue.get(0).run(i));
			else
				pHaveRun.add(runQueue.remove(0));

			//ages rest
			for(int j=1; j<runQueue.size();j++)
				runQueue.get(j).ageProcess();
			
			//increment time quanta
			i++;
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
				//ages rest
				for(int j=1; j<runQueue.size();j++)
					runQueue.get(j).ageProcess();
				
				//runs first process in queue. removes if process has finished(i.e. method returns false)
				processStr+=runQueue.get(0).getProcessNumber()+",";

				if(runQueue.get(0).run(i))
					runQueue.add(runQueue.remove(0));
				else
					pHaveRun.add(runQueue.remove(0));
			}
			else
				processStr+="N,";
		}

		int i = NUM_QUANTA;
		//runs any remaining processes that have started (i.e. response time>=0)
		while(runQueue.size()>0){
			if(runQueue.get(0).getResponseTime()>=0){
				//ages rest
				for(int j=1; j<runQueue.size();j++)
					runQueue.get(j).ageProcess();
				
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
		ArrayList<ArrayList<Process>> runQueue = new ArrayList<ArrayList<Process>>(NUM_PRIORITIES);
		for(int i=0;i<NUM_PRIORITIES;i++)
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
				if(a.size()==0)
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

			//Run queue for first 100 time quanta
			boolean haveRun = false;
			int k=-1;
			for(ArrayList<Process> a: runQueue)
			{
				k++;
				if(a.size()==0){
					processStrHPF[k]+="N,";
					continue;
				}
				else if(!haveRun){
					haveRun = true;
					//ages rest
					for(int j=1; j<a.size();j++)
						a.get(j).ageProcess();

					processStr+=a.get(0).getProcessNumber()+",";
					processStrHPF[k]+=a.get(0).getProcessNumber()+",";
					if(a.get(0).run(i))
						a.add(a.remove(0));
					else{
						pHaveRunHPF.get(a.get(0).getInitialPriority()).add(a.get(0));
						pHaveRun.add(a.remove(0));
					}
				}
				else{
					processStrHPF[k]+="N,";
					for(Process p:a)
						p.ageProcess();					
				}
			}
			if(!haveRun)
				processStr+="N,";
		}

		int i = NUM_QUANTA;
		int sum = 0;
		for(ArrayList<Process> a:runQueue){
			for(int j=0; j< a.size(); j++){
				if(a.get(j).getResponseTime()<0){
					a.remove(j);
					j--;
				}
			}
			sum+=a.size();
		}
		
		//runs any remaining processes that have started (i.e. response time>=0)
		while(sum>0){
			for(int j=1; j<runQueue.size(); j++)
			{
				ArrayList<Process> a = runQueue.get(j);
				if(a.size()==0)
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
			int k=-1;
			for(ArrayList<Process> a: runQueue)
			{
				k++;
				if(a.size()==0){
					processStrHPF[k]+="N,";
					continue;
				}
				else if(!haveRun){
					haveRun = true;
					//ages rest
					for(int j=1; j<a.size();j++)
						a.get(j).ageProcess();

					processStr+=a.get(0).getProcessNumber()+",";
					processStrHPF[k]+=a.get(0).getProcessNumber()+",";
					if(a.get(0).run(i)){
						a.add(a.remove(0));
					}
					else{
						pHaveRunHPF.get(a.get(0).getInitialPriority()).add(a.get(0));
						pHaveRun.add(a.remove(0));
						sum--;
					}
				}
				else{
					processStrHPF[k]+="N,";
					for(Process p:a)
						p.ageProcess();					
				}
			}
			i++;
		}
	}
	private static void runHPFnonpre(){
		ArrayList<ArrayList<Process>> runQueue = new ArrayList<ArrayList<Process>>(NUM_PRIORITIES);
		for(int i=0;i<NUM_PRIORITIES;i++)
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
				if(a.size()==0)
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

			//Run queue for one time quanta
			boolean haveRun = false;
			int k =-1;
			for(ArrayList<Process> a: runQueue)
			{
				k++;
				if(a.size()==0){
					processStrHPF[k]+="N,";
					continue;
					}
				else if(!haveRun){
					haveRun = true;
					//ages rest
					for(int j=1; j<a.size();j++)
						a.get(j).ageProcess();

					processStr+=a.get(0).getProcessNumber()+",";
					processStrHPF[k]+=a.get(0).getProcessNumber()+",";
					if(a.get(0).run(i));
					else{
						pHaveRunHPF.get(a.get(0).getInitialPriority()).add(a.get(0));
						pHaveRun.add(a.remove(0));
					}
				}
				else{
					processStrHPF[k]+="N,";
					for(Process p:a)
						p.ageProcess();					
				}
			}
			if(!haveRun)
				processStr+="N,";
		}

		//runs any remaining processes that have started (i.e. response time>=0)
		
		int i = NUM_QUANTA;
		int sum = 0;
		for(ArrayList<Process> a:runQueue){
			for(int j=0; j< a.size(); j++){
				if(a.get(j).getResponseTime()<0){
					a.remove(j);
					j--;
				}
			}
			sum+=a.size();
		}
		while(sum>0){
			for(int j=1; j<runQueue.size(); j++)
			{
				ArrayList<Process> a = runQueue.get(j);
				if(a.size()==0)
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
			int k =-1;
			for(ArrayList<Process> a: runQueue)
			{
				k++;
				if(a.size()==0){
					processStrHPF[k]+="N,";
					continue;
				}
				else if(!haveRun){
					haveRun = true;
					//ages rest
					for(int j=1; j<a.size();j++)
						a.get(j).ageProcess();

					processStr+=a.get(0).getProcessNumber()+",";
					processStrHPF[k]+=a.get(0).getProcessNumber()+",";
					if(a.get(0).run(i));
					else{
						pHaveRunHPF.get(a.get(0).getInitialPriority()).add(a.get(0));
						pHaveRun.add(a.remove(0));
						sum--;
					}
				}
				else{
					processStrHPF[k]+="N,";
					for(Process p:a)
						p.ageProcess();					
				}
			}
			i++;
		}
	}

	private static void printHeaderToFile(String algorithmType){
		avgResponseTime = 0;
		avgTurnAround = 0;
		avgWaitTime = 0;
		avgThroughput = 0;
		avgTurnAroundHPF = new double[NUM_PRIORITIES];
		avgWaitTimeHPF = new double[NUM_PRIORITIES];
		avgResponseTimeHPF = new double[NUM_PRIORITIES];
		avgThroughputHPF = new double[NUM_PRIORITIES];

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
			fw.write(System.getProperty("line.separator")+"Run: "+run+System.getProperty("line.separator"));
			fw.write("Process Time Line: "+processStr+System.getProperty("line.separator"));
			fw.write("Wait Time: "+df.format(waitTime)+", Response Time: "+df.format(responseTime)+
					", Turn Around: "+df.format(turnAround)+", Throughput: "+df.format(throughput)+System.getProperty("line.separator"));


			System.out.println("\nRun: "+run);
			System.out.println("Process Time Line: "+processStr);
			System.out.println("Wait Time: "+df.format(waitTime)+", Response Time: "+df.format(responseTime)+
					", Turn Around: "+df.format(turnAround)+", Throughput: "+df.format(throughput));
			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void printHPFRunToFile(){
		DecimalFormat df = new DecimalFormat("#.##");
		for(int i=0; i<NUM_PRIORITIES;i++){
			double turnAround=0;
			double waitTime = 0;
			double responseTime = 0;
			double throughput = pHaveRunHPF.get(i).size();
			for(Process p: pHaveRunHPF.get(i)){
				turnAround += p.getTotalRunTime();
				waitTime += p.getWaitTime();
				responseTime += p.getResponseTime();
			}
			if(throughput>0){
				turnAround /= throughput;
				waitTime /= throughput;
				responseTime /= throughput;
			}
			avgResponseTimeHPF[i]+=responseTime/NUM_RUNS;
			avgWaitTimeHPF[i]+=waitTime/NUM_RUNS;
			avgTurnAroundHPF[i]+=turnAround/NUM_RUNS;
			avgThroughputHPF[i]+=throughput/NUM_RUNS;

			//write to file
			try {
				FileWriter fw = new FileWriter("statistics.txt", true);
				fw.write("Priority Queue: "+i+System.getProperty("line.separator"));
				fw.write("Process Time Line: "+processStrHPF[i]+System.getProperty("line.separator"));
				fw.write("Wait Time: "+df.format(waitTime)+", Response Time: "+df.format(responseTime)+
						", Turn Around: "+df.format(turnAround)+", Throughput: "+df.format(throughput)+System.getProperty("line.separator"));


				System.out.println("Priority Queue: "+i);
				System.out.println("Process Time Line: "+processStrHPF[i]);
				System.out.println("Wait Time: "+df.format(waitTime)+", Response Time: "+df.format(responseTime)+
						", Turn Around: "+df.format(turnAround)+", Throughput: "+df.format(throughput));
				fw.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private static void printFinalToFile(){
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			FileWriter fw = new FileWriter("statistics.txt", true);
			fw.write(System.getProperty("line.separator")+"Final Average Statistics:"+System.getProperty("line.separator"));
			fw.write("Wait Time: "+df.format(avgWaitTime)+", Response Time: "+df.format(avgResponseTime)+
					", Turn Around: "+df.format(avgTurnAround)+", Throughput: "+df.format(avgThroughput)+System.getProperty("line.separator"));
			fw.write("-----------------------------------------------------------"+System.getProperty("line.separator"));
			System.out.println();
			System.out.println("Final Average Statistics:");
			System.out.println("Wait Time: "+df.format(avgWaitTime)+", Response Time: "+df.format(avgResponseTime)+
					", Turn Around: "+df.format(avgTurnAround)+", Throughput: "+df.format(avgThroughput));
			System.out.println("-----------------------------------------------------------\n");
			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void printHPFFinalToFile(){
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			FileWriter fw = new FileWriter("statistics.txt", true);
			fw.write("-----------------------------------------------------------"+System.getProperty("line.separator"));
			System.out.println("-----------------------------------------------------------\n");
			for(int i =0;i<NUM_PRIORITIES;i++){
				fw.write("Average Priority Queue "+i+" Statistics:"+System.getProperty("line.separator"));
				fw.write("Wait Time: "+df.format(avgWaitTimeHPF[i])+", Response Time: "+df.format(avgResponseTimeHPF[i])+
						", Turn Around: "+df.format(avgTurnAroundHPF[i])+", Throughput: "+df.format(avgThroughputHPF[i])+System.getProperty("line.separator"));
				System.out.println("Average Priority Queue "+i+" Statistics:");
				System.out.println("Wait Time: "+df.format(avgWaitTimeHPF[i])+", Response Time: "+df.format(avgResponseTimeHPF[i])+
						", Turn Around: "+df.format(avgTurnAroundHPF[i])+", Throughput: "+df.format(avgThroughputHPF[i]));
			}
			fw.write("-----------------------------------------------------------"+System.getProperty("line.separator"));
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
		processStrHPF = new String[NUM_PRIORITIES];
		for(int i = 0; i<NUM_PRIORITIES;i++){
			processStrHPF[i] = "";
		}
		pToRun = new ArrayList<Process>(NUM_PROCESSES);
		pHaveRun = new ArrayList<Process>(NUM_PROCESSES);
		pHaveRunHPF = new ArrayList<ArrayList<Process>>(NUM_PRIORITIES);
		for(int i=0;i<NUM_PRIORITIES;i++)
			pHaveRunHPF.add(new ArrayList<Process>(NUM_PROCESSES));

		for(int i = 0; i<NUM_PROCESSES; i++){
			pToRun.add(new Process(r.nextInt(NUM_PRIORITIES),r.nextInt(NUM_QUANTA),.1+ 9.9*r.nextDouble(),i));
		}
		Collections.sort(pToRun);
	}
}