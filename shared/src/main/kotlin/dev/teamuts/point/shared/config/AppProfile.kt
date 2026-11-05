package dev.teamuts.point.shared.config

import org.springframework.context.EnvironmentAware
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class AppProfile : EnvironmentAware {
    companion object {
        const val LOCAL = "local"
        const val ALPHA = "alpha"
    }

    private var profiles: Array<String> = emptyArray()

    override fun setEnvironment(environment: Environment) {
        val activeProfiles = environment.activeProfiles
        val defaultProfiles = environment.defaultProfiles

        if (activeProfiles.isNotEmpty()) {
            this.profiles = activeProfiles
            return
        }

        if (defaultProfiles.isNotEmpty()) {
            this.profiles = defaultProfiles
        }
    }

    fun isLocal(): Boolean {
        return hasProfile(LOCAL)
    }

    fun isAlpha(): Boolean {
        return hasProfile(ALPHA)
    }

    private fun hasProfile(profile: String): Boolean {
        return profiles.toList().contains(profile)
    }
}
