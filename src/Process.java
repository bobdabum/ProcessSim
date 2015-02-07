import java.util.Random;


public class Process implements Comparable<Process>{
	
private final double RUN_TIME;
private int priority, arrivalTime, age, timeRunning, timeWaiting;

	public Process(int seed){
		Random r = new Random(seed);
		
		RUN_TIME = .1+ 9.9*r.nextDouble();		
		priority = r.nextInt(4);
		priority = r.nextInt(100);
		age = 0;		
		timeRunning = 0;
		timeWaiting = 0;
	}
	public Process(int priority, int aTime, double rTime){		
		RUN_TIME = rTime;
		arrivalTime = aTime;
		this.priority = priority;
		age = 0;
		timeRunning = 0;
		timeWaiting = 0;
	}
	/**
	 * Runs the process for one time quanta
	 * @return Returns false if the process has finished running.
	 */
	public boolean run(){
		age = 0;
		timeRunning++;
		if (timeRunning >= RUN_TIME)
			return false;
		else 
			return true;
	}
	/**
	 * Ages process by one time quanta.
	 * @return Returns the age of the process.
	 */
	public int ageProcess(){
		timeWaiting++;
		age++;
		return age;
	}
	/**
	 * Increases Priority of process.
	 */
	public void increasePriority(){
		priority++;
		age = 0;
	}
	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//Getters and Comparable interface method	
	public int compareTo(Process p){
		if(arrivalTime < p.getArrivalTime())
			return -1;
		else if(arrivalTime==p.getArrivalTime())
			return 0;
		else return 1;
	}
	public int getPriority() {
		return priority;
	}
	public int getArrivalTime() {
		return arrivalTime;
	}
	public double getRunTime() {
		return RUN_TIME;
	}
	public int getAge() {
		return age;
	}
	public int getTimeWaiting(){
		return timeWaiting;
	}
}