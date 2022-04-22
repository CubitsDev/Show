package network.palace.show.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Marc on 2/29/16
 */
@Getter
@AllArgsConstructor
public class ArmorData {
    private ItemStack head;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack itemInMainHand;
    private ItemStack itemInOffHand;

    @Override
    public String toString() {
        return head.toString() + " " + chestplate.toString() + " " + leggings.toString() + " " + boots.toString() + " " + itemInMainHand.toString() + " " + itemInOffHand.toString();
    }
}
