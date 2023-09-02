package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

import static gitlet.Utils.readObject;

/**
 * A class about HEAD file
 */
public class HEAD implements Serializable {
    private String HEADfileID;
    private String currentBranch;

    public HEAD(String HEADfileID, String currentBranch) {
        this.HEADfileID = HEADfileID;
        this.currentBranch = currentBranch;
    }

    public void writeHEADfile() {
        File HEAD = Utils.join(Repository.GITLET_DIR, "HEAD");
        Utils.writeObject(HEAD, this);
    }

    public static HEAD readHEADfile() {
        File HEAD = Utils.join(Repository.GITLET_DIR, "HEAD");
        HEAD returnHEAD;
        returnHEAD = readObject(HEAD, HEAD.class);
        return returnHEAD;
    }

    public static Commit getHEADCommit() {
        HEAD currentHEAD = readHEADfile();
        String HEADfileID = currentHEAD.getHEADfileID();
        return Commit.readCommitFile(HEADfileID);
    }

    public String getHEADfileID() {
        return HEADfileID;
    }

    public String getCurrentBranch() {
        return currentBranch;
    }

    public void setHEADCommit(String commitID) {
        this.HEADfileID = commitID;
    }

    public void setBranch(String branch) {
        this.currentBranch = branch;
    }
    public static LinkedList<String> getHEADfiles(){
        Commit HEADCommit = getHEADCommit();
        LinkedList<String> HEADfiles = new LinkedList<>();
        HEADfiles.addAll(HEADCommit.getBlobs().keySet());
        return HEADfiles;
    }
}
