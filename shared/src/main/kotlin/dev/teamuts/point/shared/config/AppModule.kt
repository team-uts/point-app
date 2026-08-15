package dev.teamuts.point.shared.config

/**
 * @see SubModule
 * application yaml config name setting
 */
object AppModule {
    private const val DEFAULT = "application"
    private const val DOMAIN = "application-domain"
    private const val PERSISTENCE = "application-persistence"
    private const val INFRA = "application-infra"
    private const val SHARED = "application-shared"

    const val APPLICATION_MODULES = "$DEFAULT,$SHARED,$DOMAIN,$PERSISTENCE,$INFRA"
    const val BATCH_MODULES = "$DEFAULT,$SHARED,$DOMAIN,$PERSISTENCE,$INFRA"
}
