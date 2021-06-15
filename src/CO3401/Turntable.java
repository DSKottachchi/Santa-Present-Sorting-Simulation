package CO3401;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

/**
 * @author DILMI SULAKSHANA KOTTACHCHI
 * @UCLAN ID: G20761896 
 * @UCL ID: 3000031
 */

public class Turntable extends Thread
{
	    
    String id;

    // @DILMI -> PORT NO
    static int N = 0;
    static int E = 1;
    static int S = 2;
    static int W = 3; 
    
    
    // @DILMI -> ARRAY OF 4 CONNECTIONS POINTS (FOR THE DIRECTIONS) 
    Connection[] connections = new Connection[4];
        
    // global lookup: age-range -> SackID
    // @DILMI-> PRESENT FALLS UNDER WHICH SACK CATEGORY
    static HashMap<String, Integer> destinations = new HashMap<>();
    
    // this individual table's lookup: SackID -> output port
    HashMap<Integer, Integer> outputMap = new HashMap<>();
    
    // @DILMI -> STORE IN THE CONVEYOR OBJECT ARRAY
    ArrayList<Conveyor> inputBelts = new ArrayList<Conveyor>();
    ArrayList<Conveyor> outputBelts = new ArrayList<Conveyor>();
    Present present; 
    
    Present currentPresent;
    boolean presentOnTable = false;
    int enteringDestination;
    Sack endSack = null;
    
    
    public Turntable (String ID)
    {
        id = ID;
    }
    
    // @DILMI -> ADDING CONNECTION PORT OF TURNTABLE
    public void addConnection(int port, Connection conn)
    {
    	// @DILMI -> CONNECTIONS[0] = { CONNTYPE, BELT, SACK }
    	// @DILMI -> CONNECTIONS[0] = {"InputBelt", BELT CONNECTED TO, SACK(NULL)}
        connections[port] = conn;
       
        if(conn != null)
        {
        	// @DILMI -> IF THE CONNECTION TYPE IS A OUTPUT BELT
            if(conn.connType == ConnectionType.OutputBelt)
            {
            	// @DILMI -> Iterator is an object that can be used to loop through collections, like ArrayList and HashSet. 
                Iterator<Integer> it = conn.belt.destinations.iterator();
                
                while(it.hasNext())
                {
                	// @DILMI -> FETCHES THE VALUE AND ADD THEM TO THE OUTPUT MAP
                    outputMap.put(it.next(), port);
                }
                setOutputBelt(port, conn);
            }
            
            // @DILMI -> IF CONNECTION OUTPUT SACK DIRECTLY ADD THEM TO THE SACK
            else if(conn.connType == ConnectionType.OutputSack)
            {
                outputMap.put(conn.sack.id, port);
            } 
            
            // @DILMI -> STORING INPUT BELTS IN AN ARRAY
            else if(conn.connType == ConnectionType.InputBelt) {
                setInputBelt(port, conn);
            }
        }
        
        destinations.entrySet().forEach(entry->{
            //System.out.println(entry.getKey() + " " + entry.getValue());  
         });
        
    }
        
    // @DILMI -> STORING INPUT BELT ARRAY
    public void setInputBelt(int port, Connection conn) {
    	// @DILMI -> LOOP THROUGH AND STORE INPUT BELT IN A ARRAY
    	inputBelts.add(conn.belt);
    }
    
    public void setOutputBelt(int port, Connection conn) {
    	// @DILMI -> LOOP THROUGH AND STORE OUTPUT BELT IN A ARRAY
    	outputBelts.add(conn.belt);
    }
    
    public void run()
    {    	
    	while(AssignmentPart1.isRunning) {
    		try {
        		for(int i = 0; i < inputBelts.size(); i++) { 
        			// System.out.println(inputBelts.get(i).GetPresentsWaiting());
        			
                    // CHECK PRESENT DESTINATION 1 2 3
        			GetAvailablePresent();
        			String presentDestination = inputBelts.get(i).GetFirstPresentDestination();
        			
        			Conveyor correctOutputBelt = null;
        			// CHECK WHETHER THE OUTPUT BELT IS THE DESTINATION AFTER YOU GET THE DESTINATION
        			// BY LOOPING THROUGH IT, IF IT IS THE IT IS THE CORRECT OUTPUT BELT
        			
        			// IS OUTPUTBELT == DESTINATION
        			for(Conveyor outputBelt: outputBelts) {
        				//System.out.println("Output Belt--- " + outputBelt.id);
        				 if (outputBelt.IsDestination(presentDestination)) {
                             correctOutputBelt = outputBelt;
                             break; // MOVE TO NEXT BELT
                         }
        			}
        			
        			// @DILMI -> CHECK WHETHER THERE IS SPACE ON THE OUTPUT BELT
                    if (correctOutputBelt != null) {
                        while (!correctOutputBelt.FreeSpaceOnBelt()){
                            // @DILMI -> THREAD SLEEP TILL SPACE ON BELT
                            Thread.sleep(1000);
                        }
                    }
            	}
        	} catch(Exception e) {
        		System.out.println("An error occured while running turntable: " + e);
        	}
    	}
    	
    }
    
    // @DILMI -> FINAL CHECK TO SEE WHETHER GIFTS ON MACHINE STILL REMAINS FOR THE FINAL REPORT
    public boolean giftsOnMachine(){
        for(int i=0;i<connections.length;i++){
            if(connections[i]!=null){
                if(connections[i].belt!=null){
                	// @DILMI -> CHECK ON WHETHER PRESENTS STILL REMAIN ON BELT
                    if (connections[i].belt.noPresentsOnBelt>0){
                        return true;
                    }
                }
            }
        }
        // @DILMI -> NO GIFTS LEFT ON THE MACHINE
        return false;
    }
    
    // @DILMI -> CHECK GET AVAILABLE PRESENT
    // @APPLY MUTEXES
    public void GetAvailablePresent() {
    	for(int i = 0; i < inputBelts.size(); i++) { 
    		if(inputBelts.get(i) != null) {
    			try {
                    present = inputBelts.get(i).removeFromBelt(); // @DILMI -> TAKE THE AVAILABLE PRESENT FROM THE BUFFER
                } catch (InterruptedException ex) {
                   // ERROR OCCURED
                }
    			System.out.println("Turntable " + id + " has present " + present.ageRange + " which needs to go to sack " + present.readDestination());
    			
    			// @IF OUTPUTBELT.AGERANGE = DESTINAITON (THEN OUTPUT TO THAT SACK)
    			if(destinations.containsKey(present.readDestination())) {
    				endSack = connections[i].sack;		
    			}
    			
    			// @DILMI -> IF OUTPUT SACK THEN PUSH THE PRESENT ON TO THE SACK
    			// HERE A PRESENT WILL BE ADDED TO THE SACK -> THIS INVOKES THE SACK CLASS
    			if(endSack!=null) {
    	            if (!endSack.IsFull()){
    	            	endSack.addPresentToSack(currentPresent);
    	            }
    	            if (endSack.IsFull())
    	            {
    	                System.out.println("Sack is full");
    	            }
    	            presentOnTable = false;
    	            // @DILMI -> 0.5 SECONDS WAIT TO REMOVE PRESENT
    	            try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    	            currentPresent = null;
    	        }
    		} else {
    			 System.out.println("The Belt Not Present");
    		}
    	}
    }
    
}
