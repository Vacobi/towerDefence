package exception;

import projectile.ProjectilesContainer;

public class ProjectilesContainerAlreadyHasOtherField extends RuntimeException {
    public ProjectilesContainerAlreadyHasOtherField(ProjectilesContainer container)
    {
        super("Projectiles container " + container.toString() + " already has other field");
    }
}
