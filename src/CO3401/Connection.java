package CO3401;

/**
* @author DILMI SULAKSHANA KOTTACHCHI
* @UCLAN ID: G20761896 
* @UCL ID: 3000031
*/

public class Connection {
    ConnectionType connType;
    Conveyor belt;
    Sack sack;
    
    public Connection(ConnectionType ct, Conveyor c, Sack s)
    {
        connType = ct; // @DILMI -> INPUT BELT, OUTPUTBELT, OUTPUTSACK
        belt = c; // @DILMI -> IF DIRECTION IS OB/IB ONLY IT WILL CONTAIN THE BELT OBJECT
        sack = s; // @DILMI -> ONLY IF OUTPUT SACK
    }
}
