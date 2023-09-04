package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author Tonghui Wang
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                Repository.init();
                break;
            case "add":
                validateNumArgs("add", args, 2);
                Repository.add(args[1]);
                break;
            case "commit":
                validateNumArgs("commit", args, 2);
                Repository.commit(args[1]);
                break;
            case "rm":
                validateNumArgs("rm", args, 2);
                Repository.rm(args[1]);
                break;
            case "log":
                validateNumArgs("log", args, 1);
                Repository.log();
                break;
            case "global-log":
                validateNumArgs("global-log", args, 1);
                Repository.global_log();
                break;
            case "find":
                validateNumArgs("find", args, 2);
                Repository.find(args[1]);
                break;
            case "checkout":
                int len = args.length;
                if (len < 2 || len > 4) {
                    System.out.println("Invalid number of arguments for:checkout");
                    System.exit(0);
                }
                if (len == 2) {
                    // java gitlet.Main checkout [branch name]
                    Repository.checkoutBranch(args[1]);
                }
                // java gitlet.Main checkout -- [file name]
                else if (len == 3) {
                    Repository.checkoutHEADCommit(args[2]);
                } else {
                    // java gitlet.Main checkout [commit id] -- [file name]
                    if (args[1].length() < 40) {
                        String commitID = Repository.abbCommit(args[1]);
                        Repository.checkoutCommit(commitID, args[3]);
                    } else {
                        Repository.checkoutCommit(args[1], args[3]);
                    }
                }
                break;
            case "status":
                validateNumArgs("status", args, 1);
                Repository.status();
                break;
            case "branch":
                validateNumArgs("branch", args, 2);
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                validateNumArgs("rm-branch", args, 2);
                Repository.rm_branch(args[1]);
                break;
            case "reset":
                //commit ID
                validateNumArgs("reset", args, 2);
                if (args[1].length() < 40) {
                    String commitID = Repository.abbCommit(args[1]);
                    Repository.reset(commitID);
                } else {
                    Repository.reset(args[1]);
                }
                break;
            case "merge":
                 validateNumArgs("merge",args,2);
                Repository.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
        }
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param cmd  Name of command you are validating
     * @param args Argument array from command line
     * @param n    Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }

}
