package ch.zhaw.psit4.martin.common;

import java.util.Set;

import edu.stanford.nlp.util.ArraySet;

public class MartinHelper {
    
    
    /**
     * Creates a new ArraySet out of the given set if it is null.
     * If the set is not null it will return it unchanged
     * @param set
     * @return
     */
    public static <T> Set<T> initSetifNull(Set<T> set){
        if(set == null) {
            set = new ArraySet<>();
        }
        return set;
    }

}
