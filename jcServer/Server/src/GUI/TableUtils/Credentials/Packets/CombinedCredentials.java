package GUI.TableUtils.Credentials.Packets;

import java.util.List;

public record CombinedCredentials(List<AccountCredentials> accountCredentials,
                                  List<CreditCardCredentials> creditCardCredentials) {
}
