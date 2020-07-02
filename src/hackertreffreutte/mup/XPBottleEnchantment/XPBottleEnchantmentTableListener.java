package hackertreffreutte.mup.XPBottleEnchantment;

import hackertreffreutte.mup.Enchantments.CustomEnchantments;
import hackertreffreutte.mup.XPCalculator.XPCalculator;
import hackertreffreutte.mup.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Arrays;

public class XPBottleEnchantmentTableListener implements Listener {

    @EventHandler
    public void onItemInEnchantmentTable(PrepareItemEnchantEvent event){


        //here is a bug with the lapis
        //if you add the lapis afterwards then there will be a bug and no enchantments will show up


        if(event.getItem().getType().equals(Material.POTION) ) {
            //checks if the item is the right type

            PotionData potionData = ((PotionMeta) event.getItem().getItemMeta()).getBasePotionData();

            if(potionData.getType().equals(PotionType.WATER)) {


                //TODO add checks for the surrounding bookshelves


                event.getOffers()[0] = new EnchantmentOffer(CustomEnchantments.XPBottle, 1, 5);
                event.getOffers()[1] = new EnchantmentOffer(CustomEnchantments.XPBottle, 2, 10);
                event.getOffers()[2] = new EnchantmentOffer(CustomEnchantments.XPBottle, 3, 20);
                event.setCancelled(false);
            }
        }else{
            return;
        }
    }

    @EventHandler
    public void onItemEnchant(EnchantItemEvent event){

        if(event.getItem().getType().equals(Material.POTION)) {
            //checks if the item is the right type

            PotionData potionData = ((PotionMeta) event.getItem().getItemMeta()).getBasePotionData();

            if(potionData.getType().equals(PotionType.WATER)) {

                //this is a bad fix and you should never do this but i have no idea how to do it otherwise ...
                // event.getEnchantsToAdd() is empty and that is the problem and why this workaround exists



                //just a placeholder so that the enchantment works
                event.getEnchantsToAdd().put(CustomEnchantments.XPBottle, 0);

                ItemStack enchantedItem = new ItemStack(Material.EXPERIENCE_BOTTLE);

                //create the enchantment of the item
                enchantedItem.addUnsafeEnchantment(CustomEnchantments.XPBottle, event.whichButton() + 1);

                //level String
                String level = "";
                for(int i = 0; i < event.whichButton() + 1; i++){
                    level += "I";
                }


                //change item meta
                ItemMeta meta = enchantedItem.getItemMeta();
                meta.setLore(Arrays.asList("Exp Bottle: " + level));
                enchantedItem.setItemMeta(meta);

                EnchantingInventory inv = (EnchantingInventory) event.getInventory();

                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
                    @Override
                    public void run() {
                        inv.setItem(enchantedItem);
                    }
                },1L);



                //remove xp from player due to enchanting
                int currentXP = XPCalculator.levelToExp(event.getEnchanter().getLevel());
                int newXP = 0;

                switch (event.getExpLevelCost()){
                    case 5:
                        newXP = currentXP - 55;
                        break;

                    case 10:
                        newXP = currentXP - 160;
                        break;

                    case 20:
                        newXP = currentXP - 550;
                        break;
                }


                //just some catching so that it is not below zero
                if(newXP < 0){
                    newXP = 0;
                }

                //set the new exp level
                event.getEnchanter().setLevel(XPCalculator.expToLevel(newXP));




            }
        }else{
            return;
        }




    }
}
