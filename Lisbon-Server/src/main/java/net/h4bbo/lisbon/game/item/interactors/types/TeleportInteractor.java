package net.h4bbo.lisbon.game.item.interactors.types;

import net.h4bbo.lisbon.dao.mysql.ItemDao;
import net.h4bbo.lisbon.dao.mysql.TeleporterDao;
import net.h4bbo.lisbon.game.GameScheduler;
import net.h4bbo.lisbon.game.item.Item;
import net.h4bbo.lisbon.game.item.ItemManager;
import net.h4bbo.lisbon.game.item.base.ItemBehaviour;
import net.h4bbo.lisbon.game.pathfinder.Position;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.room.Room;
import net.h4bbo.lisbon.game.room.entities.RoomPlayer;
import net.h4bbo.lisbon.game.triggers.GenericTrigger;
import net.h4bbo.lisbon.messages.outgoing.rooms.items.BROADCAST_TELEPORTER;
import net.h4bbo.lisbon.messages.outgoing.rooms.user.LOGOUT;

import java.util.concurrent.TimeUnit;

public class TeleportInteractor extends GenericTrigger {
    public static final String TELEPORTER_CLOSE = "FALSE";
    public static final String TELEPORTER_OPEN = "TRUE";
    private static final long TELEPORT_FLASH_DELAY_MS = 500;
    private static final long TELEPORT_EXIT_DELAY_MS = 900;

    public void onInteract(Player player, Room room, Item item, int status) {
        RoomPlayer roomUser = player.getRoomUser();

        if (room == null || roomUser == null) {
            return;
        }

        if (status != 1 && roomUser.getAuthenticateTelporterId() != -1) {
            return;
        }

        Position currentPosition = roomUser.getPosition();
        Position front = getTeleporterFrontSquare(item);

        if (status == 1) {
            if (item.getPosition().equals(currentPosition)) {
                return;
            }

            if (!front.equals(currentPosition)) {
                return;
            }

            roomUser.setPendingTeleporterId(item.getId());
            openTeleporter(item);
            roomUser.walkTo(item.getPosition().getX(), item.getPosition().getY());
            return;
        }

        if (status == 2) {
            if (!item.getPosition().equals(currentPosition)) {
                if (roomUser.getPendingTeleporterId() == item.getId()
                        || (roomUser.getGoal() != null && item.getPosition().equals(roomUser.getGoal()))) {
                    roomUser.setQueuedTeleporterId(item.getId());
                }

                return;
            }
        }

        roomUser.setPendingTeleporterId(-1);
        roomUser.setQueuedTeleporterId(-1);
        roomUser.setWalkingAllowed(false);
        roomUser.setLastItemInteraction(item);

        int pairId = TeleporterDao.getTeleporterId(item.getId());
        Item targetTeleporter = ItemDao.getItem(pairId);

        if (pairId == -1 || targetTeleporter == null || targetTeleporter.getRoom() == null) {
            GameScheduler.getInstance().getService().schedule(() -> {
                roomUser.setWalkingAllowed(true);
                Position exitSquare = getTeleporterFrontSquare(item);
                roomUser.walkTo(exitSquare.getX(), exitSquare.getY());
            }, 500, TimeUnit.MILLISECONDS);
            return;
        }

        Item resolvedTarget = ItemManager.getInstance().resolveItem(pairId);
        Item pairedTeleporter = resolvedTarget != null ? resolvedTarget : targetTeleporter;

        if (pairedTeleporter.getRoomId() == item.getRoomId()) {
            handleSameRoomTeleport(player, room, item, pairedTeleporter);
        } else {
            handleCrossRoomTeleport(player, room, item, pairedTeleporter);
        }
    }

    private void handleSameRoomTeleport(Player player, Room room, Item sourceTeleporter, Item targetTeleporter) {
        RoomPlayer roomUser = player.getRoomUser();

        GameScheduler.getInstance().getService().schedule(() -> {
            if (roomUser.getRoom() != room) {
                return;
            }

            openTeleporter(sourceTeleporter);
            room.send(new BROADCAST_TELEPORTER(sourceTeleporter, player.getDetails().getName(), true));
            roomUser.warp(targetTeleporter.getPosition(), true, true);
            roomUser.setAuthenticateTelporterId(targetTeleporter.getId());
            openTeleporter(targetTeleporter);
            room.send(new BROADCAST_TELEPORTER(targetTeleporter, player.getDetails().getName(), false));
            closeTeleporter(sourceTeleporter);
        }, TELEPORT_FLASH_DELAY_MS, TimeUnit.MILLISECONDS);

        GameScheduler.getInstance().getService().schedule(() -> {
            if (roomUser.getRoom() != room || roomUser.getAuthenticateTelporterId() != targetTeleporter.getId()) {
                return;
            }

            roomUser.setWalkingAllowed(true);

            Position exitSquare = getTeleporterFrontSquare(targetTeleporter);
            roomUser.walkTo(exitSquare.getX(), exitSquare.getY());
        }, TELEPORT_EXIT_DELAY_MS, TimeUnit.MILLISECONDS);
    }

    private void handleCrossRoomTeleport(Player player, Room room, Item sourceTeleporter, Item targetTeleporter) {
        RoomPlayer roomUser = player.getRoomUser();

        GameScheduler.getInstance().getService().schedule(() -> {
            if (roomUser.getRoom() != room) {
                return;
            }

            openTeleporter(sourceTeleporter);
            room.send(new BROADCAST_TELEPORTER(sourceTeleporter, player.getDetails().getName(), true));
        }, TELEPORT_FLASH_DELAY_MS, TimeUnit.MILLISECONDS);

        GameScheduler.getInstance().getService().schedule(() -> {
            if (roomUser.getRoom() != room) {
                return;
            }

            roomUser.setAuthenticateTelporterId(targetTeleporter.getId());
            closeTeleporter(sourceTeleporter);
            room.send(new LOGOUT(player.getRoomUser().getInstanceId()));
            roomUser.setAuthenticateId(targetTeleporter.getRoom().getId());
            targetTeleporter.getRoom().getEntityManager().enterRoom(player, null);
        }, TELEPORT_EXIT_DELAY_MS, TimeUnit.MILLISECONDS);
    }

    private void openTeleporter(Item item) {
        setTeleporterState(item, TELEPORTER_OPEN);
    }

    private void closeTeleporter(Item item) {
        setTeleporterState(item, TELEPORTER_CLOSE);
    }

    private void setTeleporterState(Item item, String state) {
        if (item == null || state.equals(item.getCustomData())) {
            return;
        }

        item.setCustomData(state);
        item.updateStatus();
    }

    public static Position getTeleporterFrontSquare(Item item) {
        if (item.hasBehaviour(ItemBehaviour.REDIRECT_ROTATION_0)) {
            return item.getPosition().getSquareBehind();
        }

        return item.getPosition().getSquareInFront();
    }
}
