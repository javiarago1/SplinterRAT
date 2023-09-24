package Information.Packets;

public record CreditCardCredentials(String cardHolder, int expirationMonth, int expirationYear,
                                    String creditCardNumber) {
}
