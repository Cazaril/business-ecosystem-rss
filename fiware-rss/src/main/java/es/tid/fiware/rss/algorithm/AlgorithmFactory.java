/**
 * Copyright (C) 2015 CoNWeT Lab., Universidad Politécnica de Madrid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.tid.fiware.rss.algorithm;

import java.util.List;

import es.tid.fiware.rss.algorithm.impl.FixedPercentageProcessor;
import es.tid.fiware.rss.exception.RSSException;
import es.tid.fiware.rss.exception.UNICAExceptionType;
import es.tid.fiware.rss.model.Algorithm;
import java.util.ArrayList;

/**
 *
 * @author fdelavega
 */
public class AlgorithmFactory {

    /**
     * Returns the AlgorithmProcessor able to handle the concrete algorithm
     * given as parameter
     * @param algorithmType identifier of the algorithm to be handled
     * @return AlgorithmProcessor instance able to handle the give algorithm
     * @throws RSSException, thrown when no processsor is found
     * for the given algorithm
     */
    public AlgorithmProcessor getAlgorithmProcessor(Algorithms algorithmType) 
        throws RSSException{

        AlgorithmProcessor processor = null;

        if (algorithmType.equals(Algorithms.FIXED_PERCENTAGE)) {
            processor = new FixedPercentageProcessor();
        } else {
            // There is no implementation for the given algorithm
            String[] args = {"No implementation found for the algorithm: " + algorithmType};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }

        return processor;
    }

    /**
     * Returns a list containing all the allowed revenue sharing algorithms
     * @return, List of Algorithm instances containing the valid ones
     */
    public List<Algorithm> getAlgorithms() {
        List<Algorithm> algorithms = new ArrayList<>();

        for(Algorithms alg: Algorithms.values()){
            Algorithm a = new Algorithm();
            a.setAlgorithmId(alg.toString());
            algorithms.add(a);
        }

        return algorithms;
    }
}
