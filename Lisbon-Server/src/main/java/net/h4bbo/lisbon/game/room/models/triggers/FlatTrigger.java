package net.h4bbo.lisbon.game.room.models.triggers;

import net.h4bbo.lisbon.dao.mysql.PetDao;
import net.h4bbo.lisbon.game.entity.Entity;
import net.h4bbo.lisbon.game.item.Item;
import net.h4bbo.lisbon.game.item.interactors.InteractionType;
import net.h4bbo.lisbon.game.item.interactors.types.PetNestInteractor;
import net.h4bbo.lisbon.game.pathfinder.Position;
import net.h4bbo.lisbon.game.pets.PetDetails;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.triggers.GenericTrigger;
import net.h4bbo.lisbon.messages.outgoing.rooms.items.PLACE_FLOORITEM;
import net.h4bbo.lisbon.messages.outgoing.rooms.items.STUFFDATAUPDATE;

public class FlatTrigger extends GenericTrigger {
    @Override
    public void onRoomEntry(Entity entity, Room room, boolean firstEntry, Object... customArgs) {
        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;

        /*player.send(new MessageComposer() {
            @Override
            public void compose(NettyResponse response) {
                response.writeBool(true);
            }

            @Override
            public short getHeader() {
                return 356; // E`
            }
        });*/

        if (firstEntry) {
            for (Item item : room.getItemManager().getFloorItems().stream().filter(item -> item.getDefinition().getInteractionType() == InteractionType.PET_NEST).toList()) {
                PetNestInteractor interactor = (PetNestInteractor) InteractionType.PET_NEST.getTrigger();

                PetDetails petDetails = PetDao.getPetDetails(item.getId());

                if (petDetails != null) {
                    Position position = new Position(petDetails.getX(), petDetails.getY());
                    position.setRotation(petDetails.getRotation());

                    interactor.addPet(room, petDetails, position);
                }
            }
        }

        // Fix showing water amount, doesn't show on initial load
        // Also can't interact with waterbowl until it's been placed again so this is a workaround
        for (Item item : room.getItemManager().getFloorItems().stream().filter(item -> item.getDefinition().getInteractionType() == InteractionType.PET_WATER_BOWL).toList()) {
            player.send(new PLACE_FLOORITEM(item));
            player.send(new STUFFDATAUPDATE(item));
        }
    }

    @Override
    public void onRoomLeave(Entity entity, Room room, Object... customArgs)  {

    }
}
