package be.kdg.gameservice.round.service.util;

import java.util.Comparator;
import java.util.Map;

/**
 * Used to sort a list of Integers on frequency and then on value both descending order
 * For example
 * List: 12 13 13 4 4 4 13 5 will be sorted as: 13 13 13 4 4 4 12 5
 */
public class SortComparator implements Comparator<Integer> {
    private final Map<Integer, Integer> freqMap;

    // Assign the specified map
    public SortComparator(Map<Integer, Integer> tFreqMap)
    {
        this.freqMap = tFreqMap;
    }

    //TODO: maarten, move documentation to javaDOC.
    // Compare the values
    @Override
    public int compare(Integer k1, Integer k2)
    {
        // Compare value by frequency
        int freqCompare = freqMap.get(k2).compareTo(freqMap.get(k1));

        // Compare value if frequency is equal
        int valueCompare = k2.compareTo(k1);

        // If frequency is equal, then just compare by value, otherwise -
        // compare by the frequency.
        if (freqCompare == 0)
            return valueCompare;
        else
            return freqCompare;
    }
}
