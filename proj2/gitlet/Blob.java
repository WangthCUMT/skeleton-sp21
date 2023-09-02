package gitlet;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import static gitlet.Utils.*;

/**
 * Abstract data structure to store the content of files
 */
public class Blob implements Serializable {
    private final String sourceFileName; // The source file name to generate a Blob
    private final byte[] content; // Blob's content
    private final String id; // Blob's SHA1 value, calculate by sha1(filename, content)

    /**
     * Create a Blob
     *
     * @param sourceFileName Source file's name
     * @param cwd            Working directory of the file you want to transform
     */
    public Blob(String sourceFileName, File cwd) {
        this.sourceFileName = sourceFileName;
        File sourceFile = join(cwd, sourceFileName);
        this.content = readContents(sourceFile);
        this.id = sha1(this.sourceFileName, this.content); // Blob's ID
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public byte[] getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getContentAsString() {
        return new String(content, StandardCharsets.UTF_8);
    }

    /**
     * Save a blob object to Blob directory
     */
    public void writeBlobFile() {
        String blobName = getId();
        File outputBlobFile = join(Repository.Blob_DIR, blobName);
        writeObject(outputBlobFile, this);
    }

    /**
     * read a blob from a blob file
     *
     * @param file_id the SHA1 ID of the file
     */
    public static Blob readBlobFile(String file_id) {
        Blob returnBlob;
        File infile = join(Repository.Blob_DIR, file_id);
        returnBlob = readObject(infile, Blob.class);
        return returnBlob;
    }

    public void writeBlobFileStage() {
        String blobName = getId();
        File outputBlobFile = join(Repository.StagingArea_DIR, blobName);
        writeObject(outputBlobFile, this);
    }

    public static Blob readBlobFileStage(String file_id) {
        Blob returnBlob;
        File infile = join(Repository.StagingArea_DIR, file_id);
        returnBlob = readObject(infile, Blob.class);
        return returnBlob;
    }

    public void blobToFile() {
        File sourceFile = join(Repository.CWD, this.sourceFileName);
        writeContents(sourceFile, this.content);
    }
    public static boolean isSameContent(String blobID, String filename){
        Blob blob = readBlobFile(blobID);
        File compare = join(Repository.CWD,filename);
        return blob.getContentAsString().equals(readContentsAsString(compare));
    }
    public static void writeBlobAsFile(String blobID){
        Blob blob = readBlobFile(blobID);
        File blobFile = join(Repository.CWD, blob.getSourceFileName());
        writeContents(blobFile,blob.getContentAsString());
    }
}
