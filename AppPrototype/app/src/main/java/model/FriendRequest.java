package model;

import java.util.HashMap;

/**
 * Created by Ethan on 3/17/2016.
 */
public class FriendRequest extends ModelObject{

    private String id;
    private String to;
    private String from;
    private String fromImage;
    private String fromName;

    public FriendRequest(){
        this.id = "";
        this.to = "";
        this.from = "";
        this.fromImage = "";
        this.fromName = "";
    }

    public FriendRequest(String _id, String _to, String _from, String _fromImage, String _fromName){
        this.id = _id;
        this.to = _to;
        this.from = _from;
        this.fromImage = _fromImage;
        this.fromName = _fromName;
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

    public void setFromImage(String fromImage) {
        this.fromImage = fromImage;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
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

    public String getFromName() {
        return fromName;
    }

    public String getFromImage() {
        return fromImage;
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (!this.id.equals("")) {
            map.put("id", this.id);
        }

        map.put("to", this.to);
        map.put("from", this.from);
        map.put("fromImage", this.fromImage);
        map.put("fromName", this.fromName);
        return map;
    }

    @Override
    public String toString(){
        String str = "";
        str += this.id + "\n";
        str += this.to + "\n";
        str += this.from + "\n";
        str += this.fromImage + "\n";
        str += this.fromName + "\n";
        return str;
    }

    public boolean validate(){
        return !this.id.equals("")
                && !this.to.equals("")
                && !this.from.equals("")
                && !this.fromName.equals("");
    }


}
