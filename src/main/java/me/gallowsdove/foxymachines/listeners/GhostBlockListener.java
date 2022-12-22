package me.gallowsdove.foxymachines.listeners;

import me.gallowsdove.foxymachines.implementation.materials.GhostBlock;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.persistence.PersistentDataType;

public class GhostBlockListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onHitByFishingRod(PlayerFishEvent e) {
        if (e.getCaught() instanceof FallingBlock b &&
                b.getPersistentDataContainer().has(GhostBlock.KEY, PersistentDataType.STRING)) {
            e.setCancelled(true);
        }
    }

    /**
     * This handler will check for a when a falling block is damaged, check if it's
     * an explosion damage type and if it's a {@link GhostBlock} and, if it is, cancel the event.
     * Will stop the ghost block moving when hit with an explosion
     *
     * @param event The {@link EntityChangeBlockEvent}
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onExplosionHitsGhostBock(EntityDamageEvent event) {
        if (event.getEntity() instanceof FallingBlock b &&
                validDamageType(event.getCause()) &&
                b.getPersistentDataContainer().has(GhostBlock.KEY, PersistentDataType.STRING)) {
            event.setCancelled(true);
        }
    }

    /**
     * This handler will check for a falling block trying to create a block once it
     * lands and, if it's a {@link GhostBlock} it will stop the placement.
     * This is a catch-all for situations that cause the block to land that are not
     * handled else-where.
     *
     * @param event The {@link EntityChangeBlockEvent}
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockLands(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock b &&
                b.getPersistentDataContainer().has(GhostBlock.KEY, PersistentDataType.STRING)) {
            event.setCancelled(true);
        }
    }

    /**
     * A simple method for determining which Damage Causes should be invalidated during
     * {@link GhostBlockListener#onExplosionHitsGhostBock(EntityDamageEvent)}
     * This method only existed as the formatting of the if statement becomes cumbersome when
     * added in there directly.
     *
     * @param cause The {@link org.bukkit.event.entity.EntityDamageEvent.DamageCause}
     */
    private boolean validDamageType(EntityDamageEvent.DamageCause cause) {
        return cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION ||
            cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
    }
}
