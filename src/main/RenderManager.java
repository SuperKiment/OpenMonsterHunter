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
			classToActions.get(entity.getClass()).Action(entity);
		} catch (Exception e) {
			System.out.println(e);
			classToActions.get(logic.Entity.class).Action(entity);
		}
	}
}
