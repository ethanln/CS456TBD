package model;

import java.util.HashMap;

/**
 * Created by Ethan on 3/17/2016.
 */
public class FriendRequest extends ModelObject{

    private String id;
    private String to;
    private String from;

    public FriendRequest(){
        this.id = "";
        this.to = "";
        this.from = "";
    }

    public FriendRequest(String _id, String _to, String _from){
        this.id = _id;
        this.to = _to;
        this.from = _from;
    }

    public void setID(String _id){
        this.id = _id;
    }

    public void setTo(String _to){
        this.to = _to;
    }

    public void setFrom(String _from){
        this.from = _from;
    }

    public String getID(){
        return this.id;
    }

    public String getTo(){
        return this.to;
    }

    public String getFrom(){
        return this.from;
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (!this.id.equals("")) {
            map.put("id", this.id);
        }

        map.put("to", this.to);
        map.put("from", this.from);
        return map;
    }

    @Override
    public String toString(){
        String str = "";
        str += this.id + "\n";
        str += this.to + "\n";
        str += this.from + "\n";
        return str;
    }

    public boolean validate(){
        return !this.id.equals("")
                && !this.to.equals("")
                && !this.from.equals("");
    }

}
