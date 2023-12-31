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

import net.minecraft.server.MinecraftServer;

import dev.phomc.linen.api.scheduler.ServerExecutorService;

public final class ServerScheduledExecutorService extends LinenScheduledExecutorService implements ServerExecutorService {
	public ServerScheduledExecutorService(MinecraftServer server) {
		super(server);
	}

	@Override
	public boolean isShutdown() {
		return !((MinecraftServer) this.innerExecutor).isRunning();
	}
}
