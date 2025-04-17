package exception;

import tower.TowersContainer;

public class TowersContainerAlreadyHasOtherField extends RuntimeException {
    public TowersContainerAlreadyHasOtherField(TowersContainer container)
    {
        super("Towers container " + container.toString() + " already has other field");
    }
}
