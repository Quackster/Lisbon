package net.h4bbo.lisbon.game.item.roller;

import net.h4bbo.lisbon.game.item.Item;
import net.h4bbo.lisbon.game.pathfinder.Position;
import net.h4bbo.lisbon.game.room.Room;

public interface RollingAnalysis<T> {
    public Position canRoll(T rollingType, Item roller, Room room);
    public void doRoll(T rollingType, Item roller, Room room, Position fromPosition, Position nextPosition);
}
