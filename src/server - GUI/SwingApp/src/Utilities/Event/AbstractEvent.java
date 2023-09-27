package Utilities.Event;

import Utilities.ManagerInterface;

public interface AbstractEvent extends Runnable, ManagerInterface {
    void handleGuiError();
}
