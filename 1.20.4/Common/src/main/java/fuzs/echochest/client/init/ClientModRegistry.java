package fuzs.echochest.client.init;

import fuzs.echochest.EchoChest;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;

public class ClientModRegistry {
    // moved here from client initializer class to avoid loading Sheets class too early
    public static final Material ECHO_CHEST_LOCATION = new Material(Sheets.CHEST_SHEET, EchoChest.id("entity/chest/echo"));
}
