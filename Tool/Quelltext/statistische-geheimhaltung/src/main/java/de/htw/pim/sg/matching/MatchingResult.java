/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.pim.sg.matching;

/**
 *
 * @author Michael Schidlauske
 */
public class MatchingResult {
    
    double possible_matches = 0;
    double real_matches = 0;
    
    public MatchingResult(double possible_matches, double real_matches)
    {
        this.possible_matches = possible_matches;
        this.real_matches = real_matches;
    }
    
    

    public double getPossible_matches() {
        return possible_matches;
    }

    public double getReal_matches() {
        return real_matches;
    }
}
