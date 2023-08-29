package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class

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
    private String parent; //每一个Commit都会有一个指针指向其父提交。
    // 指针指向Commit中文件的内容，即Blob

    public Commit(){
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.parent = null;
    }
    public Commit(String message, String parent){
        this.message = message;
        this.parent = parent;
    }

    public String getParent() {
        return parent;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
