package gitlet;

import java.io.File;
import java.util.HashMap;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Tonghui Wang
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**  Working Directory
     * .gitlet
     *        StagingArea
     *        Commit
     *        Blob
     *        ref/heads/[master]
     *        [HEAD]
     */
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File StagingArea_DIR = join(GITLET_DIR,"StagingArea");
    public static final File Commit_DIR = join(GITLET_DIR, "Commit");
    public static final File Blob_DIR = join(GITLET_DIR, "Blob");
    public static final File ref_DIR = join(GITLET_DIR,"ref");
    public static final File heads_DIR = join(ref_DIR, "heads");



    /** Creates a new Gitlet version-control system in the current directory. This system will automatically start with one commit: a commit that contains no files and has the commit message initial commit (just like that, with no punctuation).
     *  It will have a single branch: master, which initially points to this initial commit, and master will be the current branch.
     * The timestamp for this initial commit will be 00:00:00 UTC, Thursday, 1 January 1970 in whatever format you choose for dates
     * Since the initial commit in all repositories created by Gitlet will have exactly the same content,
     * it follows that all repositories will automatically share this commit (they will all have the same UID) and all commits in all repositories will trace back to it.
     * Create .gitlet dir and other sub dirs.
     * Create an original commit and save
     * Master and HEAD point to the initial commit
     * if already has .gitlet, error
     */
    public static void init(){
        // if already has .gitlet, error
        if (GITLET_DIR.exists() && GITLET_DIR.isDirectory()){
            System.out.println("A Gitlet version-control system already exists in the current directory");
            System.exit(0);
        }
        //Create .gitlet dir and other sub dirs.
        GITLET_DIR.mkdir();
        StagingArea_DIR.mkdir();
        Commit_DIR.mkdir();
        Blob_DIR.mkdir();
        ref_DIR.mkdir();
        heads_DIR.mkdir();
        //Create an original commit and save to Commit
        Commit initialCommit = new Commit();
        initialCommit.writeCommitFile();
        // Create master pointer, whose content is the last file's id in master branch
        String branchName = "master";
        File master = join(heads_DIR,branchName);
        writeContents(master, initialCommit.getId());
        // Create HEAD pointer, whose content is the HEAD file's ID
        File HEAD = join(GITLET_DIR,"HEAD");
        writeContents(HEAD, initialCommit.getId());
        // Create Staging area for later using
        StagingArea stage = new StagingArea();
        stage.writeStagingAreaFile();
    }
    /** Adds a copy of the file as it currently exists to the staging area (see the description of the commit command). For this reason, adding a file is also called staging the file for addition.
     * Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
     * The staging area should be somewhere in .gitlet. If the current working version of the file is identical to the version in the current commit,
     * do not stage it to be added, and remove it from the staging area if it is already there (as can happen when a file is changed, added, and then changed back to it’s original version).
     * The file will no longer be staged for removal (see gitlet rm), if it was at the time of the command.
     * Find the file and check whether it exists
     * If exists, add the file to staging area
     * If the file is same as it in the HEAD commit, do not add it
     * if the file in the staging area, update it
     * At last, save the staging area for later using
     * */
    public static void add(String filename){
        File addfile = join(CWD, filename);
        // Find the file and check whether it exists
        if (!addfile.exists()){
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Commit HEADCommit = Commit.getHEADCommit();  // Get the HEAD commit
        StagingArea stage = StagingArea.readStagingAreaFile(); // Get the current state of staging area
        // Get the added file's ID to check whether it already exist in HEAD commit or Staging area
        Blob addFileblob = new Blob(filename,CWD);
        String addFileID = addFileblob.getId();
        // if the file in this two parts, get its ID to see whether it changed
        String fileinCommitID = HEADCommit.getCorrespondingID(filename);
        String filestageID = stage.getCorrespondingID(filename);
        stage.getRemovedList().remove(filename);
        if (addFileID.equals(fileinCommitID)){
            // if file exits in commit, do nothing
            stage.getAddedList().remove(filename);
        } else if (addFileID.equals(filestageID)) {
            // if file exists in staging area, and content is same, do nothing
            return;
        } else if(stage.getAddedList().containsKey(filename)){
            // if file exists in staging area, but content is different, delete origin file and create new one.
            File deleteFile = join(StagingArea_DIR,stage.getAddedList().get(filename));
            deleteFile.delete();
            stage.addFile(filename,addFileID);
        } else {
            stage.addFile(filename,addFileID);
        }
        // Save result files
        stage.writeStagingAreaFile();
        addFileblob.writeBlobFileStage();
    }
    /** Unstage the file if it is currently staged for addition. If the file is tracked in the current commit,
     * stage it for removal and remove the file from the working directory if the user has not already done so
     * (do not remove it unless it is tracked in the current commit).
     * @param filename the file you want to remove
     * */
    public static void rm(String filename){
        File rmfile = join(CWD,filename);
        // Get HEAD commit and staging area
        Commit HEADCommit = Commit.getHEADCommit();
        StagingArea stage = StagingArea.readStagingAreaFile();
        // Failure case, the file is neither staged nor tracked by the head commit
        if (!(HEADCommit.getBlobs().containsKey(filename) || stage.getAddedList().containsKey(filename))){
            System.out.println("No reason to remove the file");
            System.exit(0);
        }
        // Unstage the file if it is currently staged for addition
        if (stage.getAddedList().containsKey(filename)){
            String rmfileid = stage.getCorrespondingID(filename);
            StagingArea.removeStageFile(rmfileid);
            stage.getAddedList().remove(filename);
        }
        // If the file is tracked in the current commit, stage it for removal and remove the file from the working directory
        if (HEADCommit.getBlobs().containsKey(filename)){
            stage.removeFile(filename);
        }
        // Save stage file
        rmfile.delete();
        stage.writeStagingAreaFile();
    }
    /** Saves a snapshot of tracked files in the current commit and staging area so they can be restored at a later time, creating a new commit.
     * The commit is said to be tracking the saved files. By default, each commit’s snapshot of files will be exactly the same as its parent commit’s snapshot of files;
     * it will keep versions of files exactly as they are, and not update them.
     * A commit will only update the contents of files it is tracking that have been staged for addition at the time of commit,
     * in which case the commit will now include the version of the file that was staged instead of the version it got from its parent.
     * A commit will save and start tracking any files that were staged for addition but weren’t tracked by its parent.
     * Finally, files tracked in the current commit may be untracked in the new commit as a result being staged for removal by the rm command */
    public static void commit(String message){

    }
}
