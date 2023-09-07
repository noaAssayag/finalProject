package Model;

import java.util.ArrayList;

public class userPersonalInfo {
    private ArrayList<String> hobbies;
    private String description;


    public userPersonalInfo(ArrayList<String> hobbies, String description) {
        this.hobbies = hobbies;
        this.description = description;
    }
    public userPersonalInfo()
    {

    }

    public ArrayList<String> getHobbies() {
        return hobbies;
    }
    public String getHobbiesString() {
        String hobbies="";
        int count=0;
        for(String str:getHobbies())
        {
            count++;
            hobbies+=str;
            if(count<getHobbies().size()) {
                hobbies += " ,";
            }
        }
        return  hobbies;
    }

    public void setHobbies(ArrayList<String> hobbies) {
        this.hobbies = hobbies;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
