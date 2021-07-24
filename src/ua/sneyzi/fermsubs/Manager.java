package ua.sneyzi.fermsubs;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Manager {

	protected static ScheduledExecutorService executor;
	private static Map<String, ScheduledFuture<?>> clearTimedGroupsTasks = new HashMap<>();

	public Manager() {
	}

	static void scheduleTimedGroupsCheck(long nextExpiration, String identifier) {
		ScheduledFuture<?> future = clearTimedGroupsTasks.get(identifier);
		long newDelay = (nextExpiration - (System.currentTimeMillis() / 1000));

		if (future == null || future.isDone() || future.getDelay(TimeUnit.SECONDS) > newDelay) {
			clearTimedGroupsTasks.put(identifier, executor.schedule(new Runnable() {
				@Override
				public void run() {
					User.updateTimedGroups();
					clearTimedGroupsTasks.remove(identifier);
				}
			}, newDelay, TimeUnit.SECONDS));
		}
	}

	public void end() {
		executor.shutdown();
		executor = null;
	}

	public void initTimer() {
		if (executor != null) {
			executor.shutdown();
		}
		executor = Executors.newSingleThreadScheduledExecutor();
	}

	public ScheduledExecutorService getExecutor() {
		return executor;
	}
}