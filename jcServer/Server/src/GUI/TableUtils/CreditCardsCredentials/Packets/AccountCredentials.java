package GUI.TableUtils.CreditCardsCredentials.Packets;

import java.util.Date;

public record AccountCredentials(String originUrl, String actionUrl, String username, String password,
                                 Date creationDate, Date lastUsedDate) {
}
