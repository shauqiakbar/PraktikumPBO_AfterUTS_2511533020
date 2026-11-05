package shop;

/**
 * Identity and personal parameters for the take-home assignment (Tugas).
 * 1. Write your NIM and name.
 * 2. Run: java -cp out tools.NimParams <your NIM>
 * 3. Copy the three printed values below.
 */
public final class StudentInfo {
    public static final String NIM = "2511533020";   
    public static final String NAME = "Dennis Shauqi Akbar";  

    public static final int CARD_ADMIN_PERCENT = 1;   
    public static final long TRANSFER_ADMIN_FEE = 2500;  
    public static final int SMS_MAX_LENGTH = 120;       

    private StudentInfo() {
        // constants only; no objects
    }
}
