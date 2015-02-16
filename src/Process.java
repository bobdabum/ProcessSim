import java.util.Comparator;
import java.util.Random;


public class Process implements Comparable<Process>{

	private final double RUN_DURATION;
	private final int START_TIME;
	private int priority, age, timeRunning, timeWaiting, actualStart, processNum;

	public Process(int seed){
		Random r = new Random(seed);

		RUN_DURATION = .1+ 9.9*r.nextDouble();
		START_TIME = r.nextInt(100);
		priority = r.nextInt(4);
		
		age = 0;		
		timeRunning = 0;
		timeWaiting = 0;
		actualStart = -1;
	}
	public Process(int priority, int sTime, double rTime, int pNum){		
		RUN_DURATION = rTime;
		START_TIME = sTime;
		this.priority = priority;
		processNum = pNum;
		
		age = 0;
		timeRunning = 0;
		timeWaiting = 0;
		actualStart = -1;
	}
	/**
	 * Runs the process for one time quanta at the specified time quanta.
	 * @return Returns false if the process has finished running.
	 */
	public boolean run(int timeQuanta){
		if(timeQuanta>=START_TIME){
			//records actual start time for response time calculations
			if(actualStart<0)
				actualStart = timeQuanta;
			
			age = 0;
			timeRunning++;
		}
		
		if (timeRunning >= RUN_DURATION)
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
		priority--;
		age = 0;
	}
	public double getTimeRemaining(){
		return RUN_DURATION-timeRunning;
	}
        
        public static final Comparator<Process> compareTimeRemaining = new 
            Comparator<Process>() {
                @Override
                public int compare(Process p1, Process p2){
                    if(p1.getTimeRemaining() < p2.getTimeRemaining())
                            return -1;
                    else if(p1.getTimeRemaining() == p2.getTimeRemaining())
                            return 0;
                    else return 1;
                }
        };
        
        public static final Comparator<Process> compareByRunDuration = new 
            Comparator<Process>() {
                @Override
                public int compare(Process p1, Process p2){
                    if(p1.getRunDuration() < p2.getRunDuration())
                            return -1;
                    else if(p1.getRunDuration() == p2.getRunDuration())
                            return 0;
                    else return 1;
                }
        };
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Getters and Comparable interface method	
	public int compareTo(Process p){
		if(START_TIME < p.getStartTime())
			return -1;
		else if(START_TIME==p.getStartTime())
			return 0;
		else return 1;
	}
	public int getPriority() {
		return priority;
	}
	public int getStartTime() {
		return START_TIME;
	}
	public double getTotalRunTime() {
		return RUN_DURATION+timeWaiting;
	}
	public int getAge() {
		return age;
	}
	public int getWaitTime(){
		return timeWaiting;
	}
	public int getProcessNumber(){
		return processNum;
	}
	public double getResponseTime(){
		return actualStart - START_TIME;
	}
        public double getRunDuration(){
            return RUN_DURATION;
        }
}