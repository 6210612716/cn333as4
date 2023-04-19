package com.example.randomimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.randomimage.ui.theme.RandomImageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomImageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RandomImageScreen()
                }
            }
        }
    }
}

@Composable
fun RandomImageScreen() {
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(categories[0]) }
    var userInput by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var feedbackText by remember { mutableStateOf("") }
    var showEditScreen by remember { mutableStateOf(true) }

    fun confirmClick(width: String, height: String) {
        val intWidth = width.toIntOrNull()
        val intHeight = height.toIntOrNull()
        if ((intWidth == null || intWidth < 1) && (intHeight == null || intHeight < 1)) {
            feedbackText = "Please enter valid width and height values"
        } else if (intWidth == null || intWidth < 1) {
            feedbackText = "Please enter a valid width value"
        } else if (intHeight == null || intHeight < 1) {
            feedbackText = "Please enter a valid height value"
        } else {
            showEditScreen = false
            feedbackText = ""
            url = "https://loremflickr.com/$width/$height/$userInput"
        }
    }

    when (showEditScreen) {
        true -> {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextField(
                    value = width,
                    label = {
                        Text(stringResource(R.string.width_label))
                    },
                    onValueChange = {
                        width = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = height,
                    label = {
                        Text(stringResource(R.string.height_label))
                    },
                    onValueChange = {
                        height = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box {
                    OutlinedTextField(
                        value = userInput,
                        onValueChange = {
                            userInput = it
                            selectedOption = it
                        },
                        label = { Text(stringResource(R.string.category_label)) },
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Category Dropdown",
                                    tint = colorResource(id = R.color.black)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .widthIn(max = 200.dp),
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedOption = category
                                    userInput = category
                                    expanded = false
                                }
                            ) {
                                Text(category)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedButton(
                    onClick = {
                        confirmClick(width, height)
                    }
                ) {
                    Text(text = "Confirm")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = feedbackText,
                    fontSize = 14.sp,
                    color = Color.Red,
                )
            }
        } else -> {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DisplayImage(url)
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedButton(
                    onClick = {
                        showEditScreen = true
                    }
                ) {
                    Text(text = "Back")
                }
            }
        }
    }
}

@Composable
fun DisplayImage(src: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(src)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.loading),
        contentDescription = "",
        contentScale = ContentScale.Crop,
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RandomImageTheme {
        RandomImageScreen()
    }
}