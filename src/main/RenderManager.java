package main;

import java.util.HashMap;

public class RenderManager {

	private HashMap<Class, RenderAction> classToActions;
	public static RenderManager renderManager;
	private processing.core.PApplet pap;

	public RenderManager(processing.core.PApplet p) {
		classToActions = new HashMap<Class, RenderAction>();
		this.pap = p;

		// TODO Faire les renders
		// Entité de base
		classToActions.put(logic.Entity.class, new RenderAction(p) {
			public void Action(logic.Entity e) {
				pap.push();
				pap.translate(e.pos.x, e.pos.y);
				pap.rotate(e.remanantDir.heading());
				pap.ellipse(0, 0, 20, 20);
				pap.ellipse(-10, 0, 10, 10);

				pap.pop();
			}
		});

		// Player
		classToActions.put(logic.Player.class, new RenderAction(p) {
			public void Action(logic.Entity e) {
				pap.push();
				pap.translate(e.pos.x, e.pos.y);
				pap.rotate(e.remanantDir.heading());
				pap.ellipse(0, 0, 20, 20);
				pap.ellipse(0, 10, 10, 10);
				pap.ellipse(0, -10, 10, 10);

				pap.pop();
			}
		});

		// Dog
		classToActions.put(logic.Dog.class, new RenderAction(p) {
			public void Action(logic.Entity e) {
				pap.ellipse(e.pos.x, e.pos.y, 20, 20);
				pap.ellipse(e.pos.x, e.pos.y, 5, 5);

			}
		});
	}

	// ========================================
	private class RenderAction {
		processing.core.PApplet pap;

		public RenderAction(processing.core.PApplet p) {
			pap = p;
		}

		public void Action(logic.Entity e) {
		}
	}
	// ========================================

	public void Render(logic.Entity entity) {
		try {
			System.out.println("sheeesh");

			classToActions.get(entity.getClass()).Action(entity);
		} catch (Exception e) {
			System.out.println(e);
			classToActions.get(logic.Entity.class).Action(entity);
		}
	}
}
