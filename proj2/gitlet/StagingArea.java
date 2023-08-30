package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import static gitlet.Utils.*;

/** The class representing the staging area of gitlet. */
public class StagingArea implements Serializable{
    private HashMap<String,String> AddedList; // A list record all the file will be added in the staging area.
    private HashSet<String> RemovedList; // A list record all the file will be removed in the staging area.
    public StagingArea(){
        this.AddedList = new HashMap<>();
        this.RemovedList = new HashSet<>();
    }

    /** Add a file into staging area, and remove it from remove area
     *
     * @param filename Added file's name
     * @param blobID Blob's id corresponding to the file
     */
    public void addFile(String filename, String blobID){
        AddedList.put(filename,blobID);
        RemovedList.remove(filename);
    }
    public void removeFile(String filename){
        RemovedList.add(filename);
    }

    public HashMap<String, String> getAddedList() {
        return AddedList;
    }

    public HashSet<String> getRemovedList() {
        return RemovedList;
    }
    public void writeStagingAreaFile(){
        File stage = join(Repository.GITLET_DIR, "STAGE");
        writeObject(stage,this);
    }
    public static StagingArea readStagingAreaFile(){
        File stage = join(Repository.GITLET_DIR,"STAGE");
        return readObject(stage,StagingArea.class);
    }
    public String getCorrespondingID(String filename){
        return AddedList.get(filename);
    }
}
