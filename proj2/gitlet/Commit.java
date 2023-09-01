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
 *  @author Tonghui Wang
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
    private String message; // Record the user input message

    /* TODO: fill in the rest of this class. */
    private Date timestamp; // Record timestamp
    private LinkedList<String> parent; // A LinkedList, store all the parent commit SHA1 value
    private HashMap<String,String> Blobs; // HashMap Filename->Blob
    private String id; //save the SHA1 value of the commit as id ，sha1(message, timestamp, parent, blobs)

    /** Initial commit */
    public Commit(){
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.parent = new LinkedList<>();
        this.Blobs = new HashMap<>();
        this.id = sha1(message, timestamp.toString());
    }
    /** make a copy of HEAD commit */
    public Commit(String message){
        this.message = message;
        this.timestamp = new Date();
        Commit HEADCommit = HEAD.getHEADCommit();
        this.parent = new LinkedList<>(HEADCommit.getParent());
        this.parent.add(HEADCommit.id);
        this.Blobs = HEADCommit.getBlobs(); // Get parent's blobs
        this.id = HEADCommit.getId();
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
    /** Save a commit to a file, named by its SHA1 value */
    public void writeCommitFile(){
        String filename = getId();
        File outputCommitFile = join(Repository.Commit_DIR,filename);
        writeObject(outputCommitFile,this);
    }
    /** read a commit from a commit file
     * @param file_id the SHA1 ID of the file */
    public static Commit readCommitFile(String file_id){
        Commit returnCommit;
        File infile = join(Repository.Commit_DIR,file_id);
        returnCommit = readObject(infile, Commit.class);
        return returnCommit;
    }

    /** Get the file's blob's ID
     *
     * @param filename File name you want to get
     * @return Its corresponding blob's id
     */
    public String getCorrespondingID(String filename){
        return Blobs.get(filename);
    }
    /** Change the id of a commit */
    public void setId(String id) {
        this.id = id;
    }
}
