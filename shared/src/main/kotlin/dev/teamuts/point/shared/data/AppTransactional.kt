package dev.teamuts.point.shared.data

import org.springframework.core.annotation.AliasFor
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Transactional(transactionManager = TransactionConstants.APP_TRANSACTION_MANAGER)
annotation class AppTransactional(
    /**
     * @see [Transactional.readOnly]
     */
    @get:AliasFor(attribute = "readOnly", annotation = Transactional::class)
    val readOnly: Boolean = false,

    /**
     * @see [Transactional.propagation]
     */
    @get:AliasFor(attribute = "propagation", annotation = Transactional::class)
    val propagation: Propagation = Propagation.REQUIRED
)
