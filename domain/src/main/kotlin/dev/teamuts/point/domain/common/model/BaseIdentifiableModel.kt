package dev.teamuts.point.domain.common.model

import java.util.*

abstract class BaseIdentifiableModel<ID>(val id: ID) {
  fun isPersisted(): Boolean {
    return when (id) {
      is Long -> id > 0
      is UUID -> id != UUID(0L, 0L)
      else -> false
    }
  }

  fun checkPersisted() {
    if (!this.isPersisted()) {
      throw RuntimeException("Entity is not persisted yet.")
    }
  }
}
