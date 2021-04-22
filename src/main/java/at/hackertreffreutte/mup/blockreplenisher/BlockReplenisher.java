package at.hackertreffreutte.mup.blockreplenisher;

import at.hackertreffreutte.mup.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockReplenisher implements Listener {


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){

        //placed last item
        if(event.getItemInHand().getAmount() == 1){


            //TODO
            //workaround fix later otherwise shovel will be removed when making a path
            if(event.getItemInHand().getMaxStackSize() == 1){
                return;
            }

            final PlayerInventory inv = event.getPlayer().getInventory();

            //get material of placed item
            Material placedMaterial = event.getItemInHand().getType();

            //remove placed item
            if(event.getHand() == EquipmentSlot.HAND){
                event.getItemInHand().setAmount(0);
            }else{
                event.getPlayer().getEquipment().getItemInOffHand().setAmount(0);
            }


            for(int i= 0; i < inv.getContents().length; i++){
                final ItemStack item = inv.getItem(i);

                if(item != null){
                    if(item.getType().equals(placedMaterial)){
                        //found same item like that that was placed

                        //replenish the item (move the item from the inventory to the main hand)
                        Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
                            public void run() {
                                if(event.getHand() == EquipmentSlot.HAND){
                                    inv.setItemInMainHand(item.clone());
                                    item.setAmount(0);
                                }else if(event.getHand() == EquipmentSlot.OFF_HAND){
                                    inv.setItemInOffHand(item.clone());
                                    item.setAmount(0);
                                }
                            }
                        },1L);

                        break;
                    }
                }
            }
        }
    }
}
