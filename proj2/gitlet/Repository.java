package gitlet;

import java.io.File;
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

    /**  文件保存架构
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



    /* TODO: fill in the rest of this class. */
    /** Creates a new Gitlet version-control system in the current directory. This system will automatically start with one commit: a commit that contains no files and has the commit message initial commit (just like that, with no punctuation).
     *  It will have a single branch: master, which initially points to this initial commit, and master will be the current branch.
     * The timestamp for this initial commit will be 00:00:00 UTC, Thursday, 1 January 1970 in whatever format you choose for dates (this is called “The (Unix) Epoch”, represented internally by the time 0.)
     * Since the initial commit in all repositories created by Gitlet will have exactly the same content,
     * it follows that all repositories will automatically share this commit (they will all have the same UID) and all commits in all repositories will trace back to it.
     * TODO:建立一个.gitlet文件夹和其中所有的子文件夹
     * TODO:创建一个原始提交并保存
     * TODO：将master指针和HEAD指针指向initial commit
     * TODO：如果已经有.gitlet文件夹，弹出错误
     */
    public static void init(){
        // 如果已经存在一个.gitlet文件夹，弹出错误
        if (GITLET_DIR.exists() && GITLET_DIR.isDirectory()){
            System.out.println("A Gitlet version-control system already exists in the current directory");
            System.exit(0);
        }
        //创建各种文件夹以及子文件夹
        GITLET_DIR.mkdir();
        StagingArea_DIR.mkdir();
        Commit_DIR.mkdir();
        Blob_DIR.mkdir();
        ref_DIR.mkdir();
        heads_DIR.mkdir();
        //创建一个初始提交并写入到Commit中
        Commit initialCommit = new Commit();

    }
    public void writeCommitFile(Commit inputCommit){
        String filename = inputCommit.getId();
        File outputCommitFile = join(Repository.Commit_DIR,filename);
        writeObject(outputCommitFile,inputCommit);
    }
}
