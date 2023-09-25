package Packets.Credentials;

public record CreditCardCredentials(String cardHolder, int expirationMonth, int expirationYear,
                                    String creditCardNumber) {
}
