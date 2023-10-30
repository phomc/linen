/*
 * Copyright (c) 2023 PhoMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.phomc.linen.impl.scheduler;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.thread.ReentrantThreadExecutor;

public abstract class LinenScheduledExecutorService extends AbstractExecutorService implements ScheduledExecutorService {
	private static final AtomicLong TASK_ID = new AtomicLong();
	protected final ReentrantThreadExecutor<?> innerExecutor;
	private final Map<Long, RunnableScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

	public LinenScheduledExecutorService(ReentrantThreadExecutor<?> innerExecutor) {
		this.innerExecutor = innerExecutor;
	}

	public void run() {
		tasks.forEach((id, task) -> {
			if (task.getDelay(TimeUnit.NANOSECONDS) <= 0) {
				this.execute(task);
			}
		});
	}

	void addTask(LinenScheduledFutureTask<?> task) {
		this.tasks.put(task.id, task);
	}

	public void removeTask(long id) {
		this.tasks.remove(id);
	}

	final long nextExecutionTime(long delay, TimeUnit unit) {
		return System.nanoTime() + unit.toNanos(delay);
	}

	@Override
	public void shutdown() {
	}

	@NotNull
	@Override
	public List<Runnable> shutdownNow() {
		return ImmutableList.of();
	}

	@Override
	public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) {
		return false;
	}

	@Override
	public boolean isTerminated() {
		return this.isShutdown();
	}

	@Override
	public void execute(@NotNull Runnable command) {
		this.innerExecutor.execute(command);
	}

	@NotNull
	@Override
	public ScheduledFuture<?> schedule(@NotNull Runnable command, long delay, @NotNull TimeUnit unit) {
		Objects.requireNonNull(command, "command");
		Objects.requireNonNull(unit, "timeUnit");

		var task = new LinenScheduledFutureTask<>(command, null, TASK_ID.getAndIncrement(), nextExecutionTime(delay, unit), 0);
		addTask(task);

		return task;
	}

	@NotNull
	@Override
	public <V> ScheduledFuture<V> schedule(@NotNull Callable<V> callable, long delay, @NotNull TimeUnit unit) {
		Objects.requireNonNull(callable, "callable");
		Objects.requireNonNull(unit, "timeUnit");

		var task = new LinenScheduledFutureTask<>(callable, TASK_ID.getAndIncrement(), nextExecutionTime(delay, unit), 0);
		addTask(task);

		return task;
	}

	@NotNull
	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(@NotNull Runnable command, long initialDelay, long period, @NotNull TimeUnit unit) {
		Objects.requireNonNull(command, "command");
		Objects.requireNonNull(unit, "timeUnit");

		var task = new LinenScheduledFutureTask<>(command, null, TASK_ID.getAndIncrement(), nextExecutionTime(initialDelay, unit), unit.toNanos(period));
		addTask(task);

		return task;
	}

	@NotNull
	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(@NotNull Runnable command, long initialDelay, long delay, @NotNull TimeUnit unit) {
		Objects.requireNonNull(command, "command");
		Objects.requireNonNull(unit, "timeUnit");

		var task = new LinenScheduledFutureTask<>(command, null, TASK_ID.getAndIncrement(), nextExecutionTime(initialDelay, unit), -unit.toNanos(delay));
		addTask(task);

		return task;
	}

	private static class LinenScheduledFutureTask<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {
		private final long id;
		/**
		 * Period for repeating tasks, in nanoseconds.
		 * A positive value indicates fixed-rate execution.
		 * A negative value indicates fixed-delay execution.
		 * A value of 0 indicates a non-repeating (one-shot) task.
		 */
		private final long period;
		RunnableScheduledFuture<V> outerTask = this;
		private volatile long time;

		public LinenScheduledFutureTask(Callable<V> callable, long id, long time, long period) {
			super(callable);
			this.id = id;
			this.time = time;
			this.period = period;
		}

		public LinenScheduledFutureTask(Runnable runnable, V result, long id, long time, long period) {
			super(runnable, result);
			this.id = id;
			this.time = time;
			this.period = period;
		}

		@Override
		public boolean isPeriodic() {
			return this.period != 0;
		}

		@Override
		public long getDelay(@NotNull TimeUnit unit) {
			return unit.convert(time - System.nanoTime(), TimeUnit.NANOSECONDS);
		}

		@Override
		public int compareTo(@NotNull Delayed o) {
			if (o == this) {
				return 0;
			}

			if (o instanceof LinenScheduledFutureTask<?> other) {
				long diff = time - other.time;

				if (diff < 0) {
					return -1;
				} else if (diff > 0) {
					return 1;
				} else if (id < other.id) {
					return -1;
				} else {
					return 1;
				}
			}

			long diff = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
			return (diff < 0) ? -1 : (diff > 0) ? 1 : 0;
		}

		@Override
		public void run() {
			if (!isPeriodic()) {
				super.run();
			} else if (super.runAndReset()) {
				setNextExecutionTime();
			}
		}

		void setNextExecutionTime() {
			if (period > 0) {
				time += period;
			} else {
				time = System.nanoTime() - period;
			}
		}
	}
}
