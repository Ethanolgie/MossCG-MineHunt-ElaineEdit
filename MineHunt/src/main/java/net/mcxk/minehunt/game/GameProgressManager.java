package net.mcxk.minehunt.game;

import net.mcxk.minehunt.MineHunt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;

/**
 * 处理新的进度事件
 */
public class GameProgressManager {
    private final MineHunt plugin = MineHunt.getInstance();
    private final Set<GameProgress> unlocked = new HashSet<>();

    /**
     * 检查和解锁新的游戏进度
     *
     * @param progress 游戏进度
     */
    public void unlockProgress(GameProgress progress) {
        if (plugin.getGame().getStatus() != GameStatus.GAME_STARTED) {
            return;
        }
        if (!unlocked.add(progress)) {
            return;
        }
        processProgress(progress);
    }

    private void processProgress(GameProgress progress) {
        switch (progress) {
            case NOT_STARTED:
            case GAME_STARTING:
                plugin.getGame().getInGamePlayers().forEach(player -> player.getInventory().addItem(new ItemStack(Material.BREAD, 5)));
                break;
            case STONE_AGE:
            case IRON_MINED:
                broadcastProgress(progress, false, true);
                plugin.getGame().getInGamePlayers().forEach(player -> player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 1)));
                break;
            case COMPASS_UNLOCKED:
                broadcastProgress(progress, false, false);
                break;
            case ENTER_NETHER:
                broadcastProgress(progress, false, false);
                break;
            case GET_BLAZE_ROD:
                broadcastProgress(progress, false, true);
                plugin.getGame().getInGamePlayers().forEach(player -> player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300, 1)));
                break;
            case GET_ENDER_PERAL:
                broadcastProgress(progress, false, false);
                break;
            case ENTER_END:
                broadcastProgress(progress, false, false);
                break;
            case KILLED_DRAGON:
                broadcastProgress(progress, false, false);
                break;
        }
    }

    private void broadcastProgress(GameProgress progress, boolean item, boolean buff) {
        Bukkit.broadcastMessage(ChatColor.AQUA + "达到新的游戏阶段 " + ChatColor.GREEN + "[" + progress.getDisplay() + "]");
        if (item) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "奖励补给？你在想屁吃!");
        }
        if (buff) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "奖励药水效果已应用，请查看！");
        }
    }
}
