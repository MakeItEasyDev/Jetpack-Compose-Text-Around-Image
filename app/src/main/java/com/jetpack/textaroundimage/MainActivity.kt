package com.jetpack.textaroundimage

import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetpack.textaroundimage.ui.theme.TextAroundImageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextAroundImageTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Text Around Image",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            )
                        }
                    ) {
                        TextAroundImage(
                            "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident. \n" +
                                    " Similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. \n" +
                                    " Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. \n" +
                                    " Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat."
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TextAroundImage(content: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TextAroundImageContent(
            text = content,
            color = Color.Black,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic,
            lineHeight = 30.sp,
            textAlign = TextAlign.Left,
            letterSpacing = 0.02f.sp,
            overflow = TextOverflow.Ellipsis,
            typeface = Typeface.DEFAULT,
            maxLines = 22,
            paragraphSize = 20.sp,
            alignContent = AlignContent.Left,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.makeiteasy),
                contentDescription = "Image",
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    .size(150.dp)
                    .background(color = Color.Cyan)
            )
        }
    }
}




















