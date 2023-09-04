package gitlet;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static gitlet.Utils.*;


/**
 * Represents a gitlet repository.
 *
 * @author Tonghui Wang
 */
public class Repository {
    /**
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
    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File StagingArea_DIR = join(GITLET_DIR, "StagingArea");
    public static final File Commit_DIR = join(GITLET_DIR, "Commit");
    public static final File Blob_DIR = join(GITLET_DIR, "Blob");
    public static final File ref_DIR = join(GITLET_DIR, "ref");
    public static final File heads_DIR = join(ref_DIR, "heads");


    /**
     * Creates a new Gitlet version-control system in the current directory. This system will automatically start with one commit: a commit that contains no files and has the commit message initial commit (just like that, with no punctuation).
     * It will have a single branch: master, which initially points to this initial commit, and master will be the current branch.
     * The timestamp for this initial commit will be 00:00:00 UTC, Thursday, 1 January 1970 in whatever format you choose for dates
     * Since the initial commit in all repositories created by Gitlet will have exactly the same content,
     * it follows that all repositories will automatically share this commit (they will all have the same UID) and all commits in all repositories will trace back to it.
     * Create .gitlet dir and other sub dirs.
     * Create an original commit and save
     * Master and HEAD point to the initial commit
     * if already has .gitlet, error
     */
    public static void init() {
        // if already has .gitlet, error
        if (GITLET_DIR.exists() && GITLET_DIR.isDirectory()) {
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
        File save = join(Repository.GITLET_DIR, "Initial");
        writeObject(save, initialCommit);
        // Create master pointer, whose content is the last file's id in master branch
        String branchName = "master";
        File master = join(heads_DIR, branchName);
        writeContents(master, initialCommit.getId());
        // Create HEAD pointer, whose content is the HEAD file's ID
        HEAD HEADponiter = new HEAD(initialCommit.getId(), branchName);
        HEADponiter.writeHEADfile();
        // Create Staging area for later using
        StagingArea stage = new StagingArea();
        stage.writeStagingAreaFile();
    }

    /**
     * Adds a copy of the file as it currently exists to the staging area (see the description of the commit command). For this reason, adding a file is also called staging the file for addition.
     * Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
     * The staging area should be somewhere in .gitlet. If the current working version of the file is identical to the version in the current commit,
     * do not stage it to be added, and remove it from the staging area if it is already there (as can happen when a file is changed, added, and then changed back to it’s original version).
     * The file will no longer be staged for removal (see gitlet rm), if it was at the time of the command.
     * Find the file and check whether it exists
     * If exists, add the file to staging area
     * If the file is same as it in the HEAD commit, do not add it
     * if the file in the staging area, update it
     * At last, save the staging area for later using
     */
    public static void add(String filename) {
        File addfile = join(CWD, filename);
        // Find the file and check whether it exists
        if (!addfile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Commit HEADCommit = HEAD.getHEADCommit();  // Get the HEAD commit
        StagingArea stage = StagingArea.readStagingAreaFile(); // Get the current state of staging area
        // Get the added file's ID to check whether it already exist in HEAD commit or Staging area
        Blob addFileblob = new Blob(filename, CWD);
        String addFileID = addFileblob.getId();
        // if the file in this two parts, get its ID to see whether it changed
        String fileinCommitID = HEADCommit.getCorrespondingID(filename);
        String filestageID = stage.getCorrespondingID(filename);
        stage.getRemovedList().remove(filename);
        if (addFileID.equals(fileinCommitID)) {
            // if file exits in commit, do nothing
            stage.getAddedList().remove(filename);
        } else if (addFileID.equals(filestageID)) {
            // if file exists in staging area, and content is same, do nothing
            return;
        } else if (stage.getAddedList().containsKey(filename)) {
            // if file exists in staging area, but content is different, delete origin file and create new one.
            File deleteFile = join(StagingArea_DIR, stage.getAddedList().get(filename));
            deleteFile.delete();
            stage.addFile(filename, addFileID);
        } else {
            stage.addFile(filename, addFileID);
        }
        // Save result files
        stage.writeStagingAreaFile();
        addFileblob.writeBlobFileStage();
    }

    /**
     * Unstage the file if it is currently staged for addition. If the file is tracked in the current commit,
     * stage it for removal and remove the file from the working directory if the user has not already done so
     * (do not remove it unless it is tracked in the current commit).
     *
     * @param filename the file you want to remove
     */
    public static void rm(String filename) {
        File rmfile = join(CWD, filename);
        // Get HEAD commit and staging area
        Commit HEADCommit = HEAD.getHEADCommit();
        StagingArea stage = StagingArea.readStagingAreaFile();
        // Failure case, the file is neither staged nor tracked by the head commit
        if (!(HEADCommit.getBlobs().containsKey(filename) || stage.getAddedList().containsKey(filename))) {
            System.out.println("No reason to remove the file");
            System.exit(0);
        } else if (stage.getAddedList().containsKey(filename)) {
            // Unstage the file if it is currently staged for addition
            String rmfileid = stage.getCorrespondingID(filename);
            StagingArea.removeStageFile(rmfileid);
            stage.getAddedList().remove(filename);
            rmfile.delete();
        } else if (HEADCommit.getBlobs().containsKey(filename)) {
            // If the file is tracked in the current commit, stage it for removal and remove the file from the working directory
            stage.getRemovedList().add(filename);
            rmfile.delete();
        }
        // Save stage file
        stage.writeStagingAreaFile();
    }

    /**
     * Saves a snapshot of tracked files in the current commit and staging area so they can be restored at a later time, creating a new commit.
     * The commit is said to be tracking the saved files. By default, each commit’s snapshot of files will be exactly the same as its parent commit’s snapshot of files;
     * it will keep versions of files exactly as they are, and not update them.
     * A commit will only update the contents of files it is tracking that have been staged for addition at the time of commit,
     * in which case the commit will now include the version of the file that was staged instead of the version it got from its parent.
     * A commit will save and start tracking any files that were staged for addition but weren’t tracked by its parent.
     * Finally, files tracked in the current commit may be untracked in the new commit as a result being staged for removal by the rm command
     * Copy the parent commit
     */
    public static void commit(String message) {
        //Failure case
        if (Objects.equals(message, "")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        StagingArea stage = StagingArea.readStagingAreaFile();
        if (stage.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        // Copy the HEAD commit and add HEADcommit's id in newCommit's parent list
        Commit newCommit = new Commit(message);
        // Add all the files in staging area to commit's blobs
        for (Map.Entry<String, String> item : stage.getAddedList().entrySet()) {
            String filename = item.getKey();
            String fileID = item.getValue();
            newCommit.getBlobs().put(filename, fileID);
            stage.moveStagefileToBlob(fileID);
        }
        // Remove all the file in staging area remove
        for (String filename : stage.getRemovedList()) {
            newCommit.getBlobs().remove(filename);
        }
        // Change newcommit's id
        newCommit.setId(sha1(message, newCommit.getTimestamp().toString(), newCommit.getParent().toString(), newCommit.getBlobs().toString()));
        stage.clearStage();
        stage.writeStagingAreaFile();
        // Save new commit
        newCommit.writeCommitFile();
        // Set HEAD to this commit and the branch
        HEAD currentHEAD = HEAD.readHEADfile();
        currentHEAD.setHEADCommit(newCommit.getId());
        currentHEAD.writeHEADfile();
        // Set branch file
        String branchName = currentHEAD.getCurrentBranch();
        File branchfile = join(Repository.heads_DIR, branchName);
        writeContents(branchfile, currentHEAD.getHEADfileID());
    }

    /**
     * Starting at the current head commit, display information about each commit backwards along
     * the commit tree until the initial commit, following the first parent commit links, ignoring any second parents found in merge commits.
     * (In regular Git, this is what you get with git log --first-parent). This set of commit nodes is called the commit’s history.
     * For every node in this history, the information it should display is the commit id, the time the commit was made, and the commit message.
     */
    public static void log() {
        StringBuilder sb = new StringBuilder();
        Commit HEADCommit = HEAD.getHEADCommit();
        Commit initialCommit = readObject(join(Repository.GITLET_DIR, "Initial"), Commit.class);
        while (HEADCommit.getParent().size() > 0) {
            sb.append(HEADCommit.getCommitasString());
            HEADCommit = Commit.readCommitFile(HEADCommit.getParent().get(0));
        }
        sb.append(initialCommit.getCommitasString());
        System.out.println(sb);
    }

    /**
     * Like log, except displays information about all commits ever made. The order of the commits does not matter.
     */
    public static void global_log() {
        StringBuilder sb = new StringBuilder();
        List<String> commitNames = plainFilenamesIn(Commit_DIR);
        for (String commitName : commitNames) {
            Commit tempcommit = Commit.readCommitFile(commitName);
            sb.append(tempcommit.getCommitasString());
        }
        System.out.println(sb);
    }

    /**
     * Prints out the ids of all commits that have the given commit message, one per line.
     * If there are multiple such commits, it prints the ids out on separate lines.
     */
    public static void find(String message) {
        StringBuilder sb = new StringBuilder();
        List<String> commitNames = plainFilenamesIn(Commit_DIR);
        for (String commitName : commitNames) {
            Commit tempcommit = Commit.readCommitFile(commitName);
            if (tempcommit.getMessage().equals(message)) {
                sb.append(tempcommit.getId() + "\n");
            }
        }
        if (sb.length() == 0) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
        System.out.println(sb);
    }

    /**
     * Takes the version of the file as it exists in the head commit and puts it in the working directory,
     * overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.
     */
    public static void checkoutHEADCommit(String filename) {
        Commit HEADCommit = HEAD.getHEADCommit();
        if (!(HEADCommit.getBlobs().containsKey(filename))) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            String blobID = HEADCommit.getBlobs().get(filename);
            Blob fileBlob = Blob.readBlobFile(blobID);
            fileBlob.blobToFile();
        }
    }

    /**
     * Takes the version of the file as it exists in the commit with the given id, and puts it in the working directory,
     * overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.
     */
    public static void checkoutCommit(String commitID, String filename) {
        Commit commit = Commit.readCommitFile(commitID);
        if (!(commit.getBlobs().containsKey(filename))) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            String blobID = commit.getBlobs().get(filename);
            Blob fileBlob = Blob.readBlobFile(blobID);
            fileBlob.blobToFile();
        }
    }

    /**
     * Takes all files in the commit at the head of the given branch, and puts them in the working directory,
     * overwriting the versions of the files that are already there if they exist. Also, at the end of this command,
     * the given branch will now be considered the current branch (HEAD).
     * Any files that are tracked in the current branch but are not present in the checked-out branch are deleted.
     * The staging area is cleared, unless the checked-out branch is the current branch
     */
    public static void checkoutBranch(String branchName) {
        //Failure case
        // no such branch
        List<String> branchNames = plainFilenamesIn(heads_DIR);
        if (!branchNames.contains(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        // Is the current branch
        HEAD head = HEAD.readHEADfile();
        if (head.getCurrentBranch().equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        // Untracked files will be overwritten
        Commit branchCommit = Commit.getBranchCommit(branchName);
        LinkedList<String> untrackedfiles = getUntrackFile();
        for (String filename : branchCommit.getBlobs().keySet()) {
            if (untrackedfiles.contains(filename) && !(Blob.isSameContent(branchCommit.getBlobs().get(filename), filename))) {
                // If the file is untracked and will be overwritten
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
        Commit HEADCommit = HEAD.getHEADCommit();
        for (String filename : branchCommit.getBlobs().keySet()) {
            String blobID = branchCommit.getBlobs().get(filename);
            Blob.writeBlobAsFile(blobID);
        }
        for (String filename : HEADCommit.getBlobs().keySet()) {
            if (!branchCommit.getBlobs().containsKey(filename)) {
                File deleteFile = join(CWD, filename);
                deleteFile.delete();
            }
        }
        // Clear stage and save
        StagingArea stage = StagingArea.readStagingAreaFile();
        stage.clearStage();
        stage.writeStagingAreaFile();
        // Move HEAD to the branch
        head.setHEADfileID(branchCommit.getId());
        head.setBranch(branchName);
        head.writeHEADfile();
    }

    /**
     * Displays what branches currently exist, and marks the current branch with a *. Also displays what files have been staged for addition or removal.
     */
    public static void status() {
        StringBuilder sb = new StringBuilder();
        // Branches part, first add head branch, then other branches
        sb.append("=== Branches ===\n");
        HEAD head = HEAD.readHEADfile();
        List<String> branchNames = plainFilenamesIn(heads_DIR);
        for (String branchname : branchNames) {
            if (branchname.equals(head.getCurrentBranch())) {
                sb.append("*" + branchname + "\n");
                break;
            }
        }
        if (branchNames.size() > 1) {
            LinkedList<String> branchNamesLink = new LinkedList<>();
            branchNamesLink.addAll(branchNames);
            branchNamesLink.remove(head.getCurrentBranch());
            for (String branchname : branchNamesLink) {
                sb.append(branchname + "\n");
            }
        }
        sb.append("\n");

        sb.append("=== Staged Files ===\n");
        StagingArea stage = StagingArea.readStagingAreaFile();
        for (String addfile : stage.getAddedList().keySet()) {
            sb.append(addfile + "\n");
        }
        sb.append("\n");

        sb.append("=== Removed Files ===\n");
        for (String removefile : stage.getRemovedList()) {
            sb.append(removefile + "\n");
        }
        sb.append("\n");

        sb.append("=== Modifications Not Staged For Commit ===\n");
        sb.append("\n");

        sb.append("=== Untracked Files ===\n");
        for (String UntrackedFile : getUntrackFile()) {
            sb.append(UntrackedFile + "\n");
        }
        sb.append("\n");
        System.out.println(sb);
    }

    /**
     * Creates a new branch with the given name, and points it at the current head commit.
     * A branch is nothing more than a name for a reference (a SHA-1 identifier) to a commit node.
     * This command does NOT immediately switch to the newly created branch (just as in real Git).
     * Before you ever call branch, your code should be running with a default branch called master.
     */
    public static void branch(String branchName) {
        List<String> branchNames = plainFilenamesIn(heads_DIR);
        if (branchNames != null && branchNames.contains(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        Commit HEADCommit = HEAD.getHEADCommit();
        String HEADCommitID = HEADCommit.getId();
        File newbranch = join(heads_DIR, branchName);
        writeContents(newbranch, HEADCommitID);
    }

    /**
     * Get all untracked files
     */
    private static LinkedList<String> getUntrackFile() {
        LinkedList<String> Untrackedfiles = new LinkedList<>();
        List<String> allfiles = plainFilenamesIn(CWD);
        LinkedList<String> stagingfiles = StagingArea.getStagingfiles();
        LinkedList<String> HEADfiles = HEAD.getHEADfiles();
        for (String file : allfiles) {
            if (!stagingfiles.contains(file) && !HEADfiles.contains(file)) {
                Untrackedfiles.add(file);
            }
        }
        return Untrackedfiles;
    }

    /**
     * Deletes the branch with the given name. This only means to delete the pointer associated with the branch;
     * it does not mean to delete all commits that were created under the branch, or anything like that.
     */
    public static void rm_branch(String branchName) {
        HEAD head = HEAD.readHEADfile();
        List<String> branchNames = plainFilenamesIn(heads_DIR);
        if (!branchNames.contains(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (branchName.equals(head.getCurrentBranch())) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        File deleteBranch = join(heads_DIR, branchName);
        deleteBranch.delete();
    }

    /**
     * Checks out all the files tracked by the given commit. Removes tracked files that are not present in that commit.
     * Also moves the current branch’s head to that commit node. See the intro for an example of what happens to the head pointer after using reset.
     * The [commit id] may be abbreviated as for checkout. The staging area is cleared.
     * The command is essentially checkout of an arbitrary commit that also changes the current branch head.
     */
    public static void reset(String commitID) {
        List<String> commits = plainFilenamesIn(Commit_DIR);
        if (!commits.contains(commitID)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit HEADCommit = HEAD.getHEADCommit();
        Commit resetCommit = Commit.readCommitFile(commitID);
        LinkedList<String> untrackedfiles = getUntrackFile();
        for (String filename : resetCommit.getBlobs().keySet()) {
            if (untrackedfiles.contains(filename) && !(Blob.isSameContent(resetCommit.getBlobs().get(filename), filename))) {
                // If the file is untracked and will be overwritten
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
        // Replace all the files
        // Move the current branch head and HEAD to the commit
        for (String filename : resetCommit.getBlobs().keySet()) {
            String blobID = resetCommit.getBlobs().get(filename);
            Blob.writeBlobAsFile(blobID);
        }
        for (String filename : HEADCommit.getBlobs().keySet()) {
            if (!resetCommit.getBlobs().containsKey(filename)) {
                File deleteFile = join(CWD, filename);
                deleteFile.delete();
            }
        }
        // Clear stage and save
        StagingArea stage = StagingArea.readStagingAreaFile();
        stage.clearStage();
        stage.writeStagingAreaFile();
        // Move HEAD
        HEAD head = HEAD.readHEADfile();
        head.setHEADfileID(resetCommit.getId());
        head.writeHEADfile();
        //Move Branch
        File branch = join(heads_DIR, head.getCurrentBranch());
        writeContents(branch, resetCommit.getId());
    }

    public static String abbCommit(String abbID) {
        String returnstr = "111";
        List<String> commits = plainFilenamesIn(Commit_DIR);
        if (abbID.length() != 6) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        } else {
            for (String commit : commits) {
                String str = commit.substring(0, 6);
                if (abbID.equals(str)) {
                    returnstr = commit;
                    break;
                }
            }
        }
        return returnstr;
    }
    public static void merge(String branchName){
        
    }
}
