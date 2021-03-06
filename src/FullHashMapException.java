///////////////////////////////////////////////////////////////////////////////
// Main Class File:  LoadBalancerMain.java
// File:             FullHashMapException.java
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
 * Exception class when the hashmap cannot be expanded further.
 */
public class FullHashMapException extends RuntimeException
{
    /**
     * Constructs a new FullHashMapException.
     */
    public FullHashMapException()
    {
        super();
    }

    /**
     * Constructs a new FullHashMapException with the specified message
     * 
     * @param message
     *            Exception message to be displayed
     */
    public FullHashMapException(String message)
    {
        super(message);
    }
}
