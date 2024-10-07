import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Cracker {

    public static void main(String[] args) {
        BufferedReader bufferedFile;
        BufferedReader commonPwd;
        try {
            bufferedFile = new BufferedReader(new FileReader("shadow"));
            String fullLine = bufferedFile.readLine();
            String[] splitLine;
            String[] splitHash;
            String salt;
            String hash;
            String currentPwd;
            String hashedPwd;

            while (fullLine != null) {
                splitLine = fullLine.split(":");
                splitHash = splitLine[1].split("\\$");
                salt = splitHash[2];
                hash = splitHash[3];

                Boolean pwdFound = false; // if password not found, this will be false

                // reads all of the common passwords line by line
                commonPwd = new BufferedReader(new FileReader("common-passwords.txt"));
                currentPwd = commonPwd.readLine();

                while (currentPwd != null) {
                    // hashes every password with the user's salt with MD5
                    hashedPwd = MD5Shadow.crypt(currentPwd, salt);

                    if (hashedPwd.compareTo(hash) == 0) { // if the hash matches, the password from the txt file
                                                          // is the password
                        System.out.println(splitLine[0] + ":" + currentPwd);
                        pwdFound = true;
                        break;
                    } else { // if the hash doesn't match, move on to next line in common-passwords.txt
                        currentPwd = commonPwd.readLine();
                    }
                }

                if (pwdFound == false) {
                    System.out.println(splitLine[0] + ":Password Not Found");
                }

                fullLine = bufferedFile.readLine();
            }
            bufferedFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
