package apps.ahqmrf.recapture.model;

/**
 * Created by Lenovo on 4/11/2017.
 */

public class PeopleUtil {
    People people;
    String timeStamp;

    public PeopleUtil(People people, String timeStamp) {
        this.people = people;
        this.timeStamp = timeStamp;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
