package gitlet;

// TODO: any imports you need here

import java.io.File;
import static gitlet.Utils.*;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message; // 记录提交中用户输入的信息

    /* TODO: fill in the rest of this class. */
    private Date timestamp; // 记录时间戳
    private LinkedList<String> parent; //父提交，是一个字符串列表，保存其每一个父提交的SHA1值
    private HashMap<String,String> Blobs; // 用一个TreeMap保存提交中文件名和Blob的对应关系。 文件名->Blob
    private String id; //保存这个Commit的SHA1值作为文件名和id，具体计算方式是sha1(message, timestamp, parent, blobs)

    /** 初始提交 */
    public Commit(){
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.parent = new LinkedList<>();
        this.Blobs = new HashMap<>();
        this.id = sha1(message, timestamp.toString());
    }
    public Commit(String message, LinkedList<String> parent){
        this.message = message;
        this.parent = parent;
    }

    public List<String> getParent() {
        return parent;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public HashMap<String, String> getBlobs() {
        return Blobs;
    }

}
