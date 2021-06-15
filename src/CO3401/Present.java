package CO3401;

/**
 * @author DILMI SULAKSHANA KOTTACHCHI
 * @UCLAN ID: G20761896 
 * @UCL ID: 3000031
 */

public class Present
{
    String ageRange;
    static int count = 0;

    // CONSTRUCTOR
    public Present(String destination)
    {
    	// @DILMI -> CREATE PRESENT OBJECTS AS THE SIMULATION STARTS
    	System.out.println("Present object has been created. And the destination is: " + destination);
    	// @DILMI -> STORE THE AGE GROUP (DESTINATION) PRESENT CLASS
        ageRange = destination;
        // @DILMI -> STORE THE COUNT (NUMBER OF PRESENTS BEING CREATED EACH TIME)
        count++;
    }
    
    // @DILMI -> METHOD TO READ THE DESTINATION OF PRESENT
    public String readDestination() {
        return ageRange;
    }
    
    // @DILMI -> GET THE PRESENT COUNT FOR THE HOPPERS
    public int GetCount() {
        return count;
    }
    
}
