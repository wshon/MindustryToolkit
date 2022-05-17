package toolkit.graphics;

import arc.graphics.g2d.*;
import arc.struct.*;
import mindustry.*;
import mindustry.ai.*;
import mindustry.ai.types.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import toolkit.*;
import toolkit.core.*;

import java.util.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class UnitLineRenderer extends TRenderer.Renderer {
    Seq<Tile> pathTiles = new Seq<>();

    @Override
    protected void init() {
        settings.put("pathLine", true);
        settings.put("unitLine", true);
    }

    @Override
    protected void draw() {
        if (settings.getBool("pathLine")) Vars.spawner.getSpawns().each(t -> {
            Team enemyTeam = Vars.state.rules.waveTeam;
            if (Vars.state.rules.spawns.count(g -> !g.type.naval && !g.type.flying) > 0) {
                drawUnitPath(t, Pathfinder.costGround, enemyTeam, Pathfinder.fieldCore);
            }
            if (Vars.state.rules.spawns.count(g -> g.type.naval) > 0) {
                drawUnitPath(t, Pathfinder.costNaval, enemyTeam, Pathfinder.fieldCore);
            }
//            if (Vars.state.rules.spawns.count(g -> g.type.flying) > 0) {
//                drawTargetLine(t);
//            }
        });
        if (settings.getBool("unitLine")) {
            Groups.unit.each(u -> u.controller() instanceof FlyingAI, this::drawTargetLine);
            Groups.unit.each(u -> u.controller() instanceof GroundAI, u -> {
                if (!(u.controller() instanceof SuicideAI)) drawTargetLine(u);
                drawUnitPath(u);
            });
        }
//        Groups.unit.each(u -> ToolkitUtils.isInCamera(u.x, u.y, u.hitSize), u -> {
//            UnitController c = u.controller();
//        });
    }

    private void drawTargetLine(Unit unit) {
        Lines.stroke(1, unit.team.color);
        Teamc target = ToolkitUtils.getValue(AIController.class, "target", unit.controller());
        if (target != null) {
            Lines.line(unit.x(), unit.y(), target.x(), target.y());
        }
    }

    private void drawUnitPath(Unit unit) {
        drawUnitPath(unit, Pathfinder.fieldCore);
    }

    private void drawUnitPath(Unit unit, int pathTarget) {
        drawUnitPath(unit.tileOn(), unit.pathType(), unit.team, pathTarget);
    }

    private void drawUnitPath(Tile tile, int costType, Team team, int pathTarget) {
        pathTiles.clear();
        findUnitPath(tile, costType, team, pathTarget);
        pathTiles.filter(Objects::nonNull);
        Lines.stroke(1, team.color);
        for (int i = 0; i < pathTiles.size - 1; i++) {
            Tile from = pathTiles.get(i);
            Tile to = pathTiles.get(i + 1);
            if (ToolkitUtils.isBothOutCamera(from, to)) continue;
            Lines.line(from.worldx(), from.worldy(), to.worldx(), to.worldy());
        }
    }

    private void findUnitPath(Tile tile, int costType, Team team, int pathTarget) {
        Tile targetTile;
        while (true) {
            if (tile == null) return;
            targetTile = pathfinder.getTargetTile(tile, pathfinder.getField(team, costType, pathTarget));
            if (tile == targetTile || (costType == Pathfinder.costNaval && !targetTile.floor().isLiquid)) return;
            pathTiles.add(targetTile);
            tile = targetTile;
        }
    }
}
