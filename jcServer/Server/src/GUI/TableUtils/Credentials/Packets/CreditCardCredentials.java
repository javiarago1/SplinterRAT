package GUI.TableUtils.Credentials.Packets;

public record CreditCardCredentials(String cardHolder, int expirationMonth, int expirationYear,
                                    String creditCardNumber) {
}
