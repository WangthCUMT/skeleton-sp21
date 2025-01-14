package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static gitlet.Utils.*;

/**
 * The class representing the staging area of gitlet.
 */
public class StagingArea implements Serializable {
    private final HashMap<String, String> AddedList; // A list record all the file will be added in the staging area.
    private final HashSet<String> RemovedList; // A list record all the file will be removed in the staging area.

    public StagingArea() {
        this.AddedList = new HashMap<>();
        this.RemovedList = new HashSet<>();
    }

    /**
     * Add a file into staging area, and remove it from remove area
     *
     * @param filename Added file's name
     * @param blobID   Blob's id corresponding to the file
     */
    public void addFile(String filename, String blobID) {
        AddedList.put(filename, blobID);
        RemovedList.remove(filename);
    }

    public void removeFile(String filename) {
        RemovedList.add(filename);
    }

    public HashMap<String, String> getAddedList() {
        return AddedList;
    }

    public HashSet<String> getRemovedList() {
        return RemovedList;
    }

    /**
     * Return whether the staging area is empty
     *
     * @return true if  staging area is empty
     */
    public boolean isEmpty() {
        return AddedList.isEmpty() && RemovedList.isEmpty();
    }

    public void writeStagingAreaFile() {
        File stage = join(Repository.GITLET_DIR, "STAGE");
        writeObject(stage, this);
    }

    public static StagingArea readStagingAreaFile() {
        File stage = join(Repository.GITLET_DIR, "STAGE");
        return readObject(stage, StagingArea.class);
    }

    public String getCorrespondingID(String filename) {
        return AddedList.get(filename);
    }

    public void printstage() {
        for (String key : getAddedList().keySet()) {
            System.out.println(key);
        }
    }

    /**
     * Remove a file from staging area
     *
     * @param fileId the remove file's ID
     */
    public static void removeStageFile(String fileId) {
        File rmfile = join(Repository.STAGINGAREA_DIR, fileId);
        rmfile.delete();
    }

    /**
     * Clear the satge file
     */
    public void clearStage() {
        AddedList.clear();
        RemovedList.clear();
    }

    public void moveStagefileToBlob(String fileId) {
        File movefile = join(Repository.STAGINGAREA_DIR, fileId);
        File newlocation = join(Repository.BLOB_DIR, movefile.getName());
        movefile.renameTo(newlocation);
        movefile.delete();
    }

    public static LinkedList<String> getStagingfiles() {
        LinkedList<String> stagingfiles = new LinkedList<>();
        StagingArea stage = readStagingAreaFile();
        stagingfiles.addAll(stage.getAddedList().keySet());
        stagingfiles.addAll(stage.getRemovedList());
        return stagingfiles;
    }
}
