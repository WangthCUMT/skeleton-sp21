package gitlet;

public class TestClass {
    public static void main(String[] args) {
        Commit initialCommit = Commit.readCommitFile("a497e1842e2865d93b97cf6e38802025bd776121");
        System.out.println(initialCommit.getTimestamp());
    }
}
