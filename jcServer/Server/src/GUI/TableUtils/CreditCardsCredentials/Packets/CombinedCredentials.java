package GUI.TableUtils.CreditCardsCredentials.Packets;

import java.util.List;

public record CombinedCredentials(List<AccountCredentials> accountCredentials,
                                  List<CreditCardCredentials> creditCardCredentials) {
}
