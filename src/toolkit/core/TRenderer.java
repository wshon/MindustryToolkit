package toolkit.core;

import arc.*;
import mindustry.game.*;
import toolkit.graphics.*;

public class TRenderer {
    public static class Renderer {
        protected void init() {
        }

        protected void draw() {
        }
    }

    public static final UnitLineRenderer unitLine = new UnitLineRenderer();
    public static final ItemBridgeRenderer itemBridge = new ItemBridgeRenderer();
    public static final Renderer[] all = new Renderer[]{unitLine, itemBridge};

    public static void init() {
        Events.run(EventType.Trigger.draw, TRenderer::draw);
        for (Renderer renderer : all) renderer.init();
    }

    private static void draw() {
        for (Renderer renderer : all) renderer.draw();
    }


}
