package net.h4bbo.lisbon.game.catalogue;

import net.h4bbo.lisbon.game.item.ItemManager;
import net.h4bbo.lisbon.game.item.base.ItemDefinition;

public class CataloguePackage {
    private String saleCode;
    private int definitionId;
    private int specialSpriteId;
    private int amount;

    public CataloguePackage(String saleCode, int definitionId, int specialSpriteId, int amount) {
        this.saleCode = saleCode;
        this.definitionId = definitionId;
        this.specialSpriteId = specialSpriteId;
        this.amount = amount;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public int getDefinitionId() {
        return definitionId;
    }

    public ItemDefinition getDefinition() {
        return ItemManager.getInstance().getDefinition(this.definitionId);
    }

    public int getSpecialSpriteId() {
        return specialSpriteId;
    }

    public int getAmount() {
        return amount;
    }
}
