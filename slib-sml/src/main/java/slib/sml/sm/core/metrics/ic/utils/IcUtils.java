/*

 Copyright or © or Copr. Ecole des Mines d'Alès (2012) 

 This software is a computer program whose purpose is to 
 process semantic graphs.

 This software is governed by the CeCILL  license under French law and
 abiding by the rules of distribution of free software.  You can  use, 
 modify and/ or redistribute the software under the terms of the CeCILL
 license as circulated by CEA, CNRS and INRIA at the following URL
 "http://www.cecill.info". 

 As a counterpart to the access to the source code and  rights to copy,
 modify and redistribute granted by the license, users are provided only
 with a limited warranty  and the software's author,  the holder of the
 economic rights,  and the successive licensors  have only  limited
 liability. 

 In this respect, the user's attention is drawn to the risks associated
 with loading,  using,  modifying and/or developing or reproducing the
 software by the user in light of its specific status of free software,
 that may mean  that it is complicated to manipulate,  and  that  also
 therefore means  that it is reserved for developers  and  experienced
 professionals having in-depth computer knowledge. Users are therefore
 encouraged to load and test the software's suitability as regards their
 requirements in conditions enabling the security of their systems and/or 
 data to be ensured and,  more generally, to use and operate it in the 
 same conditions as regards security. 

 The fact that you are presently reading this means that you have had
 knowledge of the CeCILL license and that you accept its terms.

 */
package slib.sml.sm.core.metrics.ic.utils;

import java.util.Iterator;
import java.util.Set;
import slib.sglib.model.graph.elements.V;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.ex.SLIB_Ex_Warning;
import slib.utils.ex.SLIB_Exception;
import slib.utils.impl.ResultStack;
import slib.utils.impl.SetUtils;

public class IcUtils {

    /**
     * 
     * @param ancA
     * @param ancB
     * @param icResults
     * @return the Most Informative Common Ancestor regarding the given metric results, null if none is found
     * @throws SLIB_Exception 
     */
    public static V searchMICA(Set<V> ancA,
            Set<V> ancB,
            ResultStack<V, Double> icResults) throws SLIB_Exception {

        Set<V> intersec = SetUtils.intersection(ancA, ancB);

        if (intersec.isEmpty()) {
            throw new SLIB_Ex_Critic("Error detecting the common ancestors with the maximal IC\nSearching a max from an empty collection, be sure the compare concepts are locate under the specified root...");
        } else if (icResults == null) {
            throw new SLIB_Ex_Critic("Empty IC result stack... Treatment cannot be performed");
        }

        Iterator<V> it = intersec.iterator();
        V mica = null;
        double max = -Double.MAX_VALUE; 

        while (it.hasNext()) {

            V v = it.next();
            if (max < icResults.get(v)) {
                max = icResults.get(v);
                mica = v;
            }
        }
        System.out.println("MICA "+mica+"\t"+IcUtils.class.getName());
        return mica;
    }

    /**
     *
     * @param ancA
     * @param ancB
     * @param icResults
     * @return
     * @throws SLIB_Ex_Critic if the intersection is empty or the result stack
     * is null
     */
    public static double searchMax_IC_MICA(Set<V> ancA,
            Set<V> ancB,
            ResultStack<V, Double> icResults) throws SLIB_Exception {
        
        V mica = searchMICA(ancA, ancB, icResults);
        return icResults.get(mica);
    }

    public static double searchMin_pOc_MICA(
            Set<V> ancA,
            Set<V> ancB,
            ResultStack<V, Double> icResults) throws SLIB_Exception {

        Set<V> intersec = SetUtils.intersection(ancA, ancB);

        if (intersec.isEmpty()) {
            throw new SLIB_Ex_Warning("Searching a min from an empty collection, be sure the compared concepts are locate under the specified root...");
        }

        if (icResults == null) {
            throw new SLIB_Ex_Critic("Empty result set");
        }

        Iterator<V> it = intersec.iterator();
        double min = icResults.get(it.next()); // TODO check empty intersection

        while (it.hasNext()) {
            V v = it.next();
            if (min > icResults.get(v)) {
                min = icResults.get(v);
            }
        }

        return min;
    }
}
