package CO3401;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

/**
 * @author DILMI SULAKSHANA KOTTACHCHI
 * @UCLAN ID: G20761896 
 * @UCL ID: 3000031
 */

public class Conveyor
{
	// @DILMI -> SEMAPHORE USED SYNCHRONISATION OF ADDING AND REMOVING PRESENTS FROM THE BELT
	Semaphore mutex = new Semaphore(1);
	Semaphore numPresentsOnBelt = new Semaphore(0);
	Semaphore numFreeSpaces;
	
    int id;
    private Present[] presents; // The requirements say this must be a fixed size array
    public  HashSet<Integer> destinations = new HashSet();
    
    int capacity;
    int noPresentsOnBelt;
    int nextPresent = 0;
    int presentOutput = 0;
    
    public Conveyor(int id, int size)
    {
        this.id = id;
        presents = new Present[size];
        this.capacity = size;
        
        // @DILMI -> ONLY ALLOW THE EXACT NUMBER OF PRESENTS ON THE BELT
        numFreeSpaces = new Semaphore(capacity);
    }
    
    // @DILMI -> RETRIEVE DESTINATION
    public void addDestination(int hopperID)
    {
        destinations.add(hopperID);
    }    
   
    // @DILMI -> CHECK ON THE DESTINATION OF THE BELT = SET DESTINATION
    public boolean IsDestination(String destination) {
    	for (int fileDestination : destinations) {
    		String fileDes = String.valueOf(fileDestination);
    		if(destination.equalsIgnoreCase(fileDes)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    boolean isBeltDestination(int des)
    {
        //checks if any of the destination of the belt is equal to the passed in destination
        for(int d = 0; d< destinations.size(); d++)
        {
            if(destinations.contains(des))
            {
                return true;
            }
        }
        return false;
    }
    
    // @DILMI -> RETURN THE ID OF BELT
    public int GetBeltId() {
        return this.id;
    }
    
    // @DILMI -> IS THERE FREE SPACE ON BELT
    public boolean FreeSpaceOnBelt() {
        return noPresentsOnBelt < capacity;
    }	
    
    // @DILMI -> GET THE PRESENTS WAITING
    public int GetPresentsWaiting() {
        return noPresentsOnBelt;
    }
    
    // @DILMI -> ADD PRESENT TO BELT FUNCTION
    void addToBelt(Present present) {
    	// @DILMI -> LOCK THE THREAD WHILE A PRESENT IS BEING ADDED THE THE CONVEYOR
    	// @DILMI -> SO THAT PRESENTS WON'T GO MISSING
    	 try {
			numFreeSpaces.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
         try {
			mutex.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}         
         
    	// @DILMI -> ADDING PRESENTS TO THE BELT
    	presents[nextPresent] = present;
    	noPresentsOnBelt++;
    	nextPresent++;
    	
        if (nextPresent == presents.length){
            nextPresent = 0;
        }
        
    	// @DILMI -> ONCE THE THREAD HAS BEEN EXECUTED UNLOCK IT FOR THE NEXT THREAD
        // @DILMI -> ALWAYS UNLOCK A THREAD AFTER EXECUTION
        numPresentsOnBelt.release(); 
        mutex.release();     
    }
    
    
    public String GetFirstPresentDestination() {
        return presents[presentOutput].readDestination();
    }
    
    public Present removeFromBelt() throws InterruptedException {
    	// @DILMI -> LOCK THE THREAD WHILE A PRESENT IS BEING REMOVED FROM THE CONVEYOR
    	// @DILMI -> SO THAT PRESENTS WON'T GO MISSING
        numPresentsOnBelt.acquire(); 
        mutex.acquire();            
        
        // @DILMI -> REMOVE PRESENTS FROM THE BELT
        Present present = presents[presentOutput];
        presents[presentOutput] = null; 
        noPresentsOnBelt--;
        presentOutput++;
        
        if (presentOutput == presents.length) {
        	presentOutput = 0;
        }
        
        System.out.println("Present Exiting Belt " + this.id);
        
        // @DILMI -> ONCE THE THREAD HAS BEEN EXECUTED UNLOCK IT FOR THE NEXT THREAD
        // @DILMI -> ALWAYS UNLOCK A THREAD AFTER EXECUTION
        mutex.release(); 
        numFreeSpaces.release(); 
        return present;
    }
    
}
