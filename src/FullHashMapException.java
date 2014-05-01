///////////////////////////////////////////////////////////////////////////////
// Main Class File:  LoadBalancerMain.java
// File:             LoadBalancerMain.java
// Semester:         CS367 Spring 2014
//
// Author:           Allen Hung <athung2@wisc.edu>
// CS Login:         ahung
// Lecturer's Name:  Professor Jim Skrentny
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
// Pair Partner:     William Jen <wjen@wisc.edu>
// CS Login:         jen
// Lecturer's Name:  Professor Jim Skrentny
//////////////////////////// 80 columns wide ///////////////////////////////////
/**
 * Exception class for when the hashMap is full
 * @author Allen and Will
 *
 */
public class FullHashMapException extends RuntimeException
{
	/**
	 * constructs a new exception
	 */
    public FullHashMapException()
    {
        super();
    }
    /**
     * Overloaded constructor that includes specified message
     * @param message specified message in constructor 
     */
    public FullHashMapException(String message)
    {
        super(message);
    }
}
