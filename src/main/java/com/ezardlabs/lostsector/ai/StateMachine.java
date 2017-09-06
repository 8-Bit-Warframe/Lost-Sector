package com.ezardlabs.lostsector.ai;

import java.util.ArrayList;
import java.util.HashMap;

class StateMachine<T extends Enum> {
	private final HashMap<T, Transition<T>[]> stateTransitions = new HashMap<>();
	private final ArrayList<Transition<T>> anyTransitions = new ArrayList<>();
	private T state;

	void init(T initialState) {
		state = initialState;
	}

	@SafeVarargs
	final void addState(T name, Transition<T>... transitions) {
		stateTransitions.put(name, transitions);
	}

	final void addTransitionFromAnyState(Transition<T> transition) {
		anyTransitions.add(transition);
	}

	void update() {
		for (Transition<T> transition : anyTransitions) {
			if (transition.getTargetState() != state && transition.isValid()) {
				state = transition.execute();
				return;
			}
		}
		for (Transition<T> transition : stateTransitions.get(state)) {
			if (transition.isValid()) {
				state = transition.execute();
				return;
			}
		}
	}

	T getState() {
		return state;
	}

	static class Transition<T> {
		private final T targetState;
		private final Condition condition;
		private final Action action;

		Transition(T targetState, Condition condition) {
			this(targetState, condition, null);
		}

		Transition(T targetState, Condition condition, Action action) {
			this.targetState = targetState;
			this.condition = condition;
			this.action = action;
		}

		T getTargetState() {
			return targetState;
		}

		boolean isValid() {
			return condition.check();
		}

		T execute() {
			if (action != null) {
				action.run();
			}
			return targetState;
		}
	}

	interface Condition {
		boolean check();
	}

	interface Action {
		void run();
	}
}
