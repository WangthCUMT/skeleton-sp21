package gitlet;


import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

/**
 * Represents a gitlet commit object.
 *
 * @author Tonghui Wang
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /**
     * The message of this Commit.
     */
    private final String message; // Record the user input message

    private final Date timestamp; // Record timestamp
    private final LinkedList<String> parent; // A LinkedList, store all the parent commit SHA1 value
    private final HashMap<String, String> Blobs; // HashMap Filename->Blob
    private String id; //save the SHA1 value of the commit as id ，sha1(message, timestamp, parent, blobs)

    /**
     * Initial commit
     */
    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.parent = new LinkedList<>();
        this.Blobs = new HashMap<>();
        this.id = sha1(message, timestamp.toString());
    }

    /**
     * make a copy of HEAD commit
     */
    public Commit(String message) {
        this.message = message;
        this.timestamp = new Date();
        Commit HEADCommit = HEAD.getHEADCommit();
        this.parent = new LinkedList<>();
        this.parent.addFirst(HEADCommit.id);
        this.Blobs = HEADCommit.getBlobs(); // Get parent's blobs
        this.id = HEADCommit.getId();
    }
    public Commit(String message, LinkedList<String> parents){
        this.message = message;
        this.timestamp = new Date();
        this.parent = parents;
        Commit HEADCommit = HEAD.getHEADCommit();
        this.Blobs = HEADCommit.getBlobs();
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

    /**
     * Save a commit to a file, named by its SHA1 value
     */
    public void writeCommitFile() {
        String filename = getId();
        File outputCommitFile = join(Repository.COMMIT_DIR, filename);
        writeObject(outputCommitFile, this);
    }

    /**
     * read a commit from a commit file
     *
     * @param fileId the SHA1 ID of the file
     */
    public static Commit readCommitFile(String fileId) {
        Commit returnCommit;
        File infile = join(Repository.COMMIT_DIR, fileId);
        returnCommit = readObject(infile, Commit.class);
        return returnCommit;
    }

    /**
     * Get the file's blob's ID
     *
     * @param filename File name you want to get
     * @return Its corresponding blob's id
     */
    public String getCorrespondingID(String filename) {
        return Blobs.get(filename);
    }

    /**
     * Change the id of a commit
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getDateString() {
        // Thu Nov 9 20:00:05 2017 -0800
        DateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        return df.format(timestamp);
    }

    public String getCommitasString() {
        StringBuffer sb = new StringBuffer();
        sb.append("===\n");
        sb.append("commit " + this.id + "\n");
        if (parent.size() == 2) {
            sb.append("Merge: " + parent.get(0).substring(0, 7) + " " + parent.get(1).substring(0, 7) + "\n");
        }
        sb.append("Date: " + this.getDateString() + "\n");
        sb.append(this.message + "\n\n");
        return sb.toString();
    }

    /**
     * Give a branch name, return the branch commit
     *
     * @param branchName give a branch name
     * @return the branch head commit
     */
    public static Commit getBranchCommit(String branchName) {
        File branchFile = join(Repository.HEADS_DIR, branchName);
        String branchID = readContentsAsString(branchFile);
        return readCommitFile(branchID);
    }
}
