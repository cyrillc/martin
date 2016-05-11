package ch.zhaw.psit4.martin.common;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static Date parseToSQLDate(String date) throws ParseException {
        if (date != null) {
            java.util.Date parsed = (new SimpleDateFormat("yyyy-MM-dd")).parse(date);
            java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
            return sqlDate;
        } else {
            throw new ParseException("empty Date given",0);
        }
    }
}
