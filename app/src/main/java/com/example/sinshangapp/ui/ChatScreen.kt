package com.example.sinshangapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sinshangapp.data.model.Message
import com.example.sinshangapp.viewModel.ChatViewModel
import com.example.sinshangapp.viewModel.SendState
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable()
fun ChatScreen(viewModel: ChatViewModel = koinViewModel()){

    val chat by viewModel._chatState.collectAsState()
    val receive = viewModel._receiveState
    var input by remember{ mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()){

        when(chat){
            is SendState.Loading -> Text("Loading")
            is SendState.Error -> Text("Error")
            is SendState.Success -> {
                ChatDetails(receive)
            }
        }

        TextField(value=input, onValueChange = {input = it},
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            label = { Text("Enter Text") },
            trailingIcon = {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send Button",
                    modifier = Modifier.clickable {
                        val msg = Message(input, "User", "1","")
                        scope.launch {
                            viewModel.sendMessage(msg)
                        }
                        input = ""
                    }
                )
            },
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                .fillMaxWidth()
        )
    }

}

@Composable
fun ChatDetails(messages: List<Message>) {

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(messages){ message->
                var textLayoutSize by remember { mutableStateOf(Size.Zero) }
                Column(modifier = Modifier
                    .align(Alignment.TopEnd)
                    .fillMaxWidth()
                    .padding(20.dp)
                    .drawBehind {
                        val padding = 70.dp.toPx()
                        val path = Path().apply {
                            moveTo(padding, 0f) // Start at the top left
                            lineTo(size.width, 0f) // Top right
                            lineTo(size.width, textLayoutSize.height+60f) // right edge
                            lineTo(size.width - 20f, textLayoutSize.height+21f) // Chat pointer
                            lineTo(padding, textLayoutSize.height+21f)
                            close()
                        }
                        drawPath(path, color = Color.LightGray, style = Fill)
                    })
                {
                    Text(message.text,
                        modifier = Modifier.padding(start=77.dp, top=5.dp, bottom = 5.dp, end = 5.dp),
                        onTextLayout = { textLayoutResult ->
                            textLayoutSize = Size(
                                textLayoutResult.size.width.toFloat(),
                                textLayoutResult.size.height.toFloat()
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayPreview(){
    ChatScreen()
}