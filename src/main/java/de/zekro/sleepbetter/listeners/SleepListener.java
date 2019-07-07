package de.zekro.sleepbetter.listeners;

import de.zekro.sleepbetter.SleepBetter;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class SleepListener implements Listener {

    private final int REQUIRED_PLAYER_COUNT = 1;

    private SleepBetter plugin;
    private double sleepPart;

    private int playersInBed;

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

        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            @Override
            public void run() {
                if (playersInBed < playersInBedNeeded)
                    return;

                world.setTime(23450);
                if (world.isThundering()) {
                    world.setThundering(false);
                    world.setStorm(false);
                }

                if (player.isSleeping())
                    player.wakeup(true);
            }
        }, 4 * 20L);
    }

    @EventHandler
    public void onGetUp(PlayerBedLeaveEvent e) {
        this.playersInBed--;
        World world = e.getPlayer().getWorld();

        if (world.getPlayers().size() < REQUIRED_PLAYER_COUNT)
            return;

        world.getPlayers().forEach(p ->
            p.sendMessage(String.format("%d/%d players in bed to sleep",
                this.playersInBed, this.getPlayersInBedNeeded(world))));
    }
}
