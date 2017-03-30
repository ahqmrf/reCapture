package apps.ahqmrf.recapture.util;

/**
 * Created by Lenovo on 3/30/2017.
 */
import java.io.File;
import java.util.Comparator;

/**
 * Created by Lenovo on 3/30/2017.
 */

public class FileComparator implements Comparator<File> {
    @Override
    public int compare(File first, File second) {
        if(first.lastModified() < second.lastModified()) return -1;
        if(first.lastModified() > second.lastModified()) return 1;
        return 0;
    }
}
