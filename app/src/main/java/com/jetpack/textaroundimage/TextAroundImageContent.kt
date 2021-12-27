package com.jetpack.textaroundimage

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TextAroundImageContent(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    paragraphSize: TextUnit = 0.sp,
    fontSize: TextUnit = 14.sp,
    fontStyle: FontStyle = FontStyle.Normal,
    typeface: Typeface = Typeface.DEFAULT,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign = TextAlign.Left,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    alignContent: AlignContent = AlignContent.Left,
    content: @Composable () -> Unit
) {
    val contentSizes = remember { mutableStateOf(listOf<Size>()) }
    val viewSize = remember { mutableStateOf(Size(0f, 0f)) }
    val boxHeight = remember { mutableStateOf(0f) }

    Box(
        modifier = if (boxHeight.value != 0f) {
            modifier.height(
                with(LocalDensity.current) {
                    boxHeight.value.toDp()
                }
            )
        } else {
            modifier
        }
    ) {
        DrawContent(
            alignContent = alignContent,
            sizeChanged = { sizes, size ->
                contentSizes.value = sizes
                viewSize.value = size
            }
        ) {
            content()
        }

        Canvas(
            modifier = Modifier.fillMaxWidth(),
            onDraw = {
                val paint = TextPaint()
                paint.textSize = fontSize.toPx()
                paint.color = color.toArgb()
                paint.textAlign = when (textAlign) {
                    TextAlign.Left -> Paint.Align.LEFT
                    TextAlign.Right -> Paint.Align.RIGHT
                    TextAlign.Center -> Paint.Align.CENTER
                }
                paint.typeface = Typeface.create(
                    typeface,
                    if (fontStyle != FontStyle.Italic) Typeface.ITALIC else Typeface.NORMAL
                )
                if (letterSpacing != TextUnit.Unspecified) {
                    paint.letterSpacing = letterSpacing.toPx()
                }

                val maxHeight = viewSize.value.height
                val paragraph = paragraphSize.toPx()
                var startLineY: Float
                var contentWidth: Float
                var startLineX: Float
                var maxWidth: Float
                val myLineHeight = if (lineHeight != TextUnit.Unspecified && lineHeight >= fontSize) {
                    lineHeight.toPx()
                } else {
                    fontSize.toPx()
                }
                var currentLineText: String
                var chunkSize: Int
                var lineNumber = 1
                var heightLimitReached = false
                var lastLine = maxHeight < myLineHeight * 2 || maxLines == 1
                var needParagraph: Boolean

                val textBlocks = text.split("\n")

                textBlocks.forEach { s ->
                    var textBlock = s
                    needParagraph = true

                    while (textBlock.isNotEmpty() && !heightLimitReached && !lastLine) {
                        if ((lineNumber + 1) * myLineHeight > maxHeight || lineNumber == maxLines) {
                            lastLine = true
                        }

                        startLineY = lineNumber * myLineHeight
                        contentWidth =
                            calculateContentWidth(contentSizes.value, startLineY - myLineHeight)

                        maxWidth = size.width - contentWidth

                        startLineX = if (alignContent == AlignContent.Right) {
                            when (textAlign) {
                                TextAlign.Left -> 0f
                                TextAlign.Right -> size.width - contentWidth
                                TextAlign.Center -> (size.width - contentWidth) / 2
                            }
                        } else {
                            when (textAlign) {
                                TextAlign.Left -> contentWidth
                                TextAlign.Right -> size.width
                                TextAlign.Center -> contentWidth + (size.width - contentWidth) / 2
                            }
                        }

                        if (needParagraph && textAlign == TextAlign.Left) {
                            startLineX += paragraph
                            maxWidth -= paragraph
                        }

                        if (lastLine) {
                            currentLineText = getLastChunk(textBlock, maxWidth, paint, overflow)
                        } else {
                            chunkSize = getChunkSize(textBlock, maxWidth, paint)
                            currentLineText = textBlock.substring(0, chunkSize)
                            textBlock = textBlock.substring(chunkSize)
                        }

                        drawIntoCanvas {
                            it.nativeCanvas.drawText(currentLineText, startLineX, startLineY, paint)
                        }

                        lineNumber++
                        if (lineNumber * myLineHeight > maxHeight) {
                            heightLimitReached = true
                        }
                        needParagraph = false
                    }
                }
                if (!heightLimitReached) {
                    boxHeight.value = (lineNumber - 1) * myLineHeight
                }
            }
        )
    }
}

@Composable
fun DrawContent(
    modifier: Modifier = Modifier,
    alignContent: AlignContent = AlignContent.Left,
    sizeChanged: (List<Size>, Size) -> Unit,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measureables, constraints ->
        val placeables = measureables.map { it.measure(constraints) }
        val sizes = mutableListOf<Size>()
        placeables.forEach { placeable ->
            sizes.add(Size(placeable.width.toFloat(), placeable.height.toFloat()))
        }
        sizeChanged.invoke(
            sizes,
            Size(constraints.maxWidth.toFloat(),
            constraints.maxHeight.toFloat())
        )

        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {
            placeables.forEach { placeable ->
                placeable.placeRelative(
                    x = if (alignContent == AlignContent.Left) 0 else constraints.maxWidth - placeable.width,
                    y = 0
                )
            }
        }
    }
}



















