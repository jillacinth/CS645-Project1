/*
 * CS645-101 Project 1
 * Ian Hanna, Daniel Marriello, Jillian Jacinto
 * Problem 1 Part 1
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimpleCracker {

    public static void main(String[] args) {
        BufferedReader bufferedFile;
        BufferedReader commonPwd;
        try {
            bufferedFile = new BufferedReader(new FileReader("shadow-simple"));
            String fullLine = bufferedFile.readLine();
            String[] splitLine;
            String currentPwd;
            String hashedPwd;

            while (fullLine != null) {
                splitLine = fullLine.split(":");

                // reads all of the common passwords line by line
                commonPwd = new BufferedReader(new FileReader("common-passwords.txt"));
                currentPwd = commonPwd.readLine();

                while (currentPwd != null) {
                    // hashes every password with the user's salt with MD5
                    hashedPwd = MD5Hashing(currentPwd, splitLine[1]);

                    if (hashedPwd.compareTo(splitLine[2]) == 0) { // if the hash matches, the password from the txt file
                                                                  // is the password
                        System.out.println(splitLine[0] + ":" + currentPwd);
                        break;
                    } else { // if the hash doesn't match, move on to next line in common-passwords.txt
                        currentPwd = commonPwd.readLine();
                    }
                }

                fullLine = bufferedFile.readLine();
            }
            bufferedFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String MD5Hashing(String password, String salt) {
        try {
            String saltedPwd = salt + password; // salt with the common password
            MessageDigest md = MessageDigest.getInstance("MD5"); // Using MD5
            byte[] pwdBytes = saltedPwd.getBytes(); // gets bytes of the salt and password
            byte[] hashedBytes = md.digest(pwdBytes); // put through MD5 algorithm
            return toHex(hashedBytes); // returns the hex string of the hash
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 Algorithm not available");
        }
    }

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
}
