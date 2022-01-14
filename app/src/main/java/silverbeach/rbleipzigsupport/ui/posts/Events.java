package silverbeach.rbleipzigsupport.ui.posts;


public class Events {

    public String name;
    public String location;
    public String time;



    public Events(){

    }

    public Events(String name, String location, String time) {
        this.name = name;
        this.location = location;
        this.time = time;
    }

    public String getName() {return name;}
    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}

}
