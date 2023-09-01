package gitlet;

import java.util.LinkedList;

public class TestClass {
    public static void main(String[] args) {
        /**Commit initialCommit = Commit.readCommitFile("5b6bad9fb9c4f9d14cec3bbdf62655d6496a82b2");
        System.out.println(initialCommit.getTimestamp());
        StagingArea stage = StagingArea.readStagingAreaFile();
        stage.printstage(); */
        StagingArea stage = StagingArea.readStagingAreaFile();
        System.out.println(stage.isEmpty());
    }
}
