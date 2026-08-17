package com.ztm2.status;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

import static com.ztm2.status.Status.plugin;

public class Utils {
    public static NamespacedKey STATUS_OWNER_KEY = new NamespacedKey(plugin,"status_text_display_owner");
    public static void setStatus(Player player, String status) {
        TextDisplay textDisplay = getStatusTextDisplay(player);
        if(textDisplay == null) textDisplay = spawnStatusTextDisplay(player);
        textDisplay.text(Component.text(status, NamedTextColor.YELLOW));
    }

    public static TextDisplay spawnStatusTextDisplay(Player player) {
        TextDisplay textDisplay = (TextDisplay) player.getWorld().spawnEntity(
                player.getLocation(),
                EntityType.TEXT_DISPLAY
        );

        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.setSeeThrough(false);
        textDisplay.getPersistentDataContainer().set(STATUS_OWNER_KEY,PersistentDataType.STRING,player.getUniqueId().toString());

        Vector3f translation = new Vector3f(0.0f, 0.1f, 0.0f);
        AxisAngle4f rot = new AxisAngle4f();
        Vector3f scale = new Vector3f(1.0f,1.0f,1.0f);
        Transformation transformation = new Transformation(translation, rot, scale, rot);
        textDisplay.setTransformation(transformation);

        player.addPassenger(textDisplay);

        return textDisplay;
    }

    public static  void removeAllStatusTextDisplay() {
        Bukkit.getServer().getWorlds().forEach(w->w.getEntitiesByClasses(TextDisplay.class).stream().filter(t->t.getPersistentDataContainer().has(STATUS_OWNER_KEY)).forEach(Entity::remove));
    }

    public static void clearStatus(Player player) {
        TextDisplay textDisplay = getStatusTextDisplay(player);
        if(textDisplay == null) return;
        textDisplay.remove();
    }

    public static TextDisplay getStatusTextDisplay(Player player) {
        List<TextDisplay> t = player.getWorld().getEntitiesByClasses(TextDisplay.class).stream().filter(e-> e.getPersistentDataContainer().has(STATUS_OWNER_KEY) && UUID.fromString(e.getPersistentDataContainer().get(STATUS_OWNER_KEY, PersistentDataType.STRING)).equals(player.getUniqueId())).map(e->(TextDisplay)e).toList();
        if(t.isEmpty()) return null;
        return t.getFirst();
    }
}
