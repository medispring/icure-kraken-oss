/*
 * Copyright (c) 2020. Taktik SA, All rights reserved.
 */

package org.taktik.icure.config

import java.util.concurrent.ConcurrentHashMap
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.MapSession
import org.springframework.session.ReactiveMapSessionRepository
import org.springframework.session.ReactiveSessionRepository
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession
import org.springframework.session.web.server.session.SpringSessionWebSessionStore
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.session.CookieWebSessionIdResolver
import org.springframework.web.server.session.DefaultWebSessionManager
import reactor.core.publisher.Mono

@Configuration
@ConditionalOnProperty(prefix = "spring", name = ["session.enabled"], havingValue = "true", matchIfMissing = false)
@EnableSpringWebSession
class SessionConfig {
	@Bean
	fun reactiveSessionRepository(): ReactiveSessionRepository<MapSession> {
		return ReactiveMapSessionRepository(ConcurrentHashMap(4096))
	}

	@Bean
	fun webSessionIdResolver() = CookieWebSessionIdResolver().apply { addCookieInitializer { cb -> cb.sameSite("None").secure(true) } }

	@Bean
	fun webSessionManager(repository: ReactiveSessionRepository<MapSession>) = object : DefaultWebSessionManager() {
		override fun getSession(exchange: ServerWebExchange) = exchange.request.headers["X-Bypass-Session"]?.let { Mono.empty() } ?: super.getSession(exchange)
	}.apply<DefaultWebSessionManager> { sessionStore = SpringSessionWebSessionStore(repository) }

}
