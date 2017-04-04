package apps.ahqmrf.recapture.util;

import java.util.Comparator;

import apps.ahqmrf.recapture.model.People;

/**
 * Created by bsse0 on 4/1/2017.
 */

public class PeopleComparator implements Comparator<People> {
    @Override
    public int compare(People first, People second) {
        return first.getName().compareTo(second.getName());
    }
}
