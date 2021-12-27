package com.jetpack.textaroundimage

import android.graphics.Paint

fun getChunkSize(
    text: String,
    maxWidth: Float,
    paint: Paint,
): Int {
    val length = paint.breakText(text, true, maxWidth, null)

    if (length <= 0 || length >= text.length || text.getOrNull(length - 1) == ' ') {
        return length
    } else if (text.length > length && text.getOrNull(length) == ' ') {
        return length + 1
    }

    var temp = length - 1
    while (text.getOrNull(temp) != ' ') {
        temp--
        if (temp <= 0) {
            return length
        }
    }
    return temp + 1
}

fun getLastChunk(
    text: String,
    maxWidth: Float,
    paint: Paint,
    overflow: TextOverflow
) : String {
    val length = paint.breakText(text, true, maxWidth, null)

    return if (length <= 0 || length >= text.length) {
        text
    } else {
        if (overflow == TextOverflow.Ellipsis) {
            text.substring(0, length - 3).plus("...")
        } else {
            text.substring(0, length)
        }
    }
}

fun calculateContentWidth(
    sizes: List<Size>,
    y: Float
): Float {
    return sizes.filter {
        it.height > y
    }.maxOfOrNull {
        it.width
    } ?: 0f
}

data class Size(
    val width: Float,
    val height: Float
)

enum class AlignContent {
    Left, Right
}

enum class TextAlign {
    Left, Right, Center
}

enum class TextOverflow {
    Clip, Ellipsis
}












