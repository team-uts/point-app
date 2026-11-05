package dev.teamuts.point.shared.extensions

fun ByteArray.toStr(maxLength: Int = 1024): String {
    return String(
        bytes = this,
        offset = 0,
        length = this.size.coerceAtMost(maxLength),
        charset = Charsets.UTF_8
    )
}
