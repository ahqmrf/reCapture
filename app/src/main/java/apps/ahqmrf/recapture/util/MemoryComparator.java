package apps.ahqmrf.recapture.util;

import java.util.Comparator;

import apps.ahqmrf.recapture.model.Memory;
import apps.ahqmrf.recapture.model.Time;

/**
 * Created by Lenovo on 4/10/2017.
 */

public class MemoryComparator implements Comparator<Memory> {
    @Override
    public int compare(Memory o1, Memory o2) {
        Time x = o1.getTime();
        Time y = o2.getTime();
        int cmp = Time.compare(x, y);
        if(cmp > 0) return -1;
        if(cmp < 0) return 1;
        cmp = Time.compareTimestamp(x, y);
        if(cmp > 0) return -1;
        if(cmp < 0) return 1;
        return 0;
    }
}
