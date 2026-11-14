package dev.teamuts.point.domain.exppoint.port

import dev.teamuts.point.domain.exppoint.model.ExpPointRecord

interface ExpPointRecordStorePort {
  fun store(expPointRecord: ExpPointRecord): ExpPointRecord
}
