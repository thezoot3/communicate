package games.adlsv.communicate.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SimpleItem {
    private final ItemStack item;
    private final Material material;
    private ItemMeta meta;
    private String[] lores;
    private String name;
    public SimpleItem(Material material) {
        item = new ItemStack(material);
        this.material = material;
    }
    public SimpleItem(Material material, String name) {
        item = new ItemStack(material);
        this.material = material;
        TextComponent n = Component.text(name);
        meta = item.getItemMeta();
        meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', name)));
        item.setItemMeta(meta);
    }
    public SimpleItem(Material material, String name, String[] lores) {
        item = new ItemStack(material);
        this.material = material;
        meta = item.getItemMeta();
        meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', name)));
        meta.lore(stringLoreToComponent(lores));
        item.setItemMeta(meta);
    }
    public ItemStack getItemStack() {
        return item;
    }
    public SimpleItem setSimpleLore(String[] lores) {
        meta.lore(stringLoreToComponent(lores));
        item.setItemMeta(meta);
        this.lores = lores;
        return this;
    }
    public Material getMaterial() {
        return material;
    }
    public String[] getOriginalLores() {
        return lores;
    }
    public List<Component> getLores() {
        return stringLoreToComponent(lores);
    }
    public ItemMeta getItemMeta() {
        return meta;
    }
    public SimpleItem setItemMeta(ItemMeta meta) {
        item.setItemMeta(meta);
        return this;
    }
    public SimpleItem setName(String name) {
        this.name = name;
        meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', name)));
        item.setItemMeta(meta);
        return this;
    }
    private static List<Component> stringLoreToComponent(String[] s) {
        List<Component> list = new ArrayList<>();
        for(String st: s) {
            list.add(Component.text(ChatColor.translateAlternateColorCodes('&',st)));
        }
        return list;
    }
}
