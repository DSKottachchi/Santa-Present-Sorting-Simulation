package CO3401;

import java.util.ArrayList;

/**
 * @author DILMI SULAKSHANA KOTTACHCHI
 * @UCLAN ID: G20761896 
 * @UCL ID: 3000031
 */

public class Hopper extends Thread
{
    int id;
    Conveyor belt;
    int speed;
    int capacity;
    int count = 0;
    Conveyor connectedBelt;
    Conveyor enterBelt;
    Present[] collection;
    int presentAddCounter=0;
    long stoppedtime;
    
    public Hopper(int id, Conveyor con, int capacity, int speed, Conveyor[] beltCollection, int connectedBeltId)
    {
        collection = new Present[capacity];
        this.id = id;
        this.belt = con;
        this.speed = speed;
        this.capacity = capacity;
       
        // @DILMI -> THIS STORE THE BELT THE HOPPER IS CONNECTED TO
        this.enterBelt = beltCollection[connectedBeltId];
    }
    
    // @DILMI -> STORE PRESENTS IN THE HOPPERS
    public void fill(Present p)
    {
    	collection[count] = p;
    	count++;	
    }
    
    
    // @DILMI -> LOAD THE PRESENTS IN THE HOPPER TO THE ENTER BELT
    public void run()
    {
    	while(collection.length > 0) {
    		addPresentConveyor();
    	}
    }
    
   
    // @DILMI -> ADDING PRESENTS FROM HOPPER TO CONVEYOR BELT
    public void addPresentConveyor() {
    	for (Present p : collection) {
    		if (p != null) { 
    			 try {
    				 // @DILMI -> IF THERE IS NO FREE SPACE ON THE BELT (SLEEP THREAD)
                     while (!belt.FreeSpaceOnBelt()) {
                    	 // @DILMI -> BELT FULL SO THREAD WILL SLEEP UNTIL FREE SPACE ON BELT
                         Thread.sleep(speed);
                         //System.out.println("Belt is full with Presents");
                     }
                     
                     // @DILMI -> KEEP TRACK OF PRESENTS BEING ADDED TO THE BELT
                     presentAddCounter++;
                     // @DILMI -> ADD PRESENT TO BELT
                     enterBelt.addToBelt(p);
                     System.out.println("Hopper " + this.id + " added P [" + p.readDestination() + "] to the buffer"); 
                     p = null; // @DILMI -> NO PRESENTS AVAILABLE IN HOPPER  
                     sleep(speed); // Sleep to simulate present being added to the belt            
                 } catch (InterruptedException ex) {
                	 // @DILMI -> FAILED TO ADD PRESENTS TO THE BELT DUE TO AN ERROR
                     System.out.println("Failed adding or sleeping");
                 }
    		}
    	}
    }
    
    public void myTestFunction(int i) {
    	//System.out.println(this.collection[i].ageRange);
    }
    
}
