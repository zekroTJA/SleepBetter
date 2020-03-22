package de.zekro.sleepbetter.listeners;

import de.zekro.sleepbetter.SleepBetter;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class SleepListener implements Listener {

    private final int REQUIRED_PLAYER_COUNT = 2;

    private SleepBetter plugin;
    private double sleepPart;
    private int playersInBed;
    private boolean showWakeupMessage = true;

    public SleepListener(SleepBetter plugin) {
        this.plugin = plugin;

        this.sleepPart = this.plugin.getConfig().getDouble("part");
    }

    private int getPlayersInBedNeeded(World world) {
        double playersNeededDouble = world.getPlayers().size() * this.sleepPart;
        int playersNeededInt = (int) playersNeededDouble;
        return playersNeededInt + (playersNeededDouble > playersNeededInt ? 1 : 0);
    }

    @EventHandler
    public void onSleep(PlayerBedEnterEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();

        int playersInWorld = world.getPlayers().size();

        // Cancel action if the player is not capable of going to bed
        // or if the world the player is currently in is not the Overworld.
        if (e.isCancelled() || world.getEnvironment() != World.Environment.NORMAL)
            return;

        this.playersInBed++;

        if (playersInWorld < REQUIRED_PLAYER_COUNT)
            return;

        int playersInBedNeeded = getPlayersInBedNeeded(world);

        world.getPlayers().forEach(p ->
            p.sendMessage(String.format("%d/%d players in bed to sleep",
                this.playersInBed, playersInBedNeeded)));

        if (playersInBedNeeded < this.playersInBed)
            return;

        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
            if (playersInBed < playersInBedNeeded)
                return;

            // This part disables showing the wakeup message which is expected
            // not to be shown after successful sleep. After 20 ticks (1 sec),
            // wake up messages are re-enabled.
            this.showWakeupMessage = false;
            this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                this.showWakeupMessage = true;
            }, 20L);

            // Add time until it's day.
            world.setTime(23450);
            // Disable storm if storm.
            if (world.hasStorm())
                world.setStorm(false);
            // Disable thunder if thunder.
            if (world.isThundering())
                world.setThundering(false);
            // Disable rain if rain.
            if (world.getWeatherDuration() > 0)
                world.setWeatherDuration(0);

            // Wake up player if still in bed.
            if (player.isSleeping())
                player.wakeup(true);
        }, 4 * 20L);
    }

    @EventHandler
    public void onGetUp(PlayerBedLeaveEvent e) {
        this.playersInBed--;
        World world = e.getPlayer().getWorld();

        if (world.getPlayers().size() < REQUIRED_PLAYER_COUNT)
            return;

        if (this.showWakeupMessage) {
            world.getPlayers().forEach(p ->
                p.sendMessage(String.format("%d/%d players in bed to sleep",
                    this.playersInBed, this.getPlayersInBedNeeded(world))));
        }
    }
}
