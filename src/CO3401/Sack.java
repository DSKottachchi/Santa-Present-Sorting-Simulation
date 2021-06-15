package CO3401;

/**
 * @author DILMI SULAKSHANA KOTTACHCHI
 * @UCLAN ID: G20761896 
 * @UCL ID: 3000031
 */

public class Sack
{
    int id;
    Present[] accumulation;
    int presentCount = 0;
    boolean sackFull=false;
    private int capacity;
    
    public Sack(int id, int capacity)
    {
        accumulation = new Present[capacity];
        this.id = id;
        this.capacity = capacity;
    }
    
    // @DILMI -> GET CAPACITY
    public int GetCapacity() {
        return capacity;
    }

    // @DILMI -> IS SACK FULL
    public boolean IsFull() {
        return capacity == presentCount;
    }

    // @DILMI -> ADDING PRESENTS TO THE SACK
    public void addPresentToSack(Present present) {
       // @DILMI -> CHECK ON WHETHER THE SACK IF FULL
       if(presentCount < capacity) {
    	   accumulation[presentCount] = present;
           System.out.println("Present: " + accumulation[presentCount].readDestination() + " has entered sack " + id);
           presentCount++;
       } else {
    	   // @DILMI -> IF SACK FULL REPLACE SACK
    	   sackFull = true;
    	   System.out.println("Sack is Full, Please Replace Sack");
       }
    }
    
}
