package com.friends.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val vm: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Surface(Modifier.fillMaxSize()) {
                        if (vm.user == null) LoginScreen(vm) else ChatScreen(vm)
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(vm: ChatViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var signup by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().padding(24.dp).systemBarsPadding(),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Privet Chat", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))
        if (signup) {
            OutlinedTextField(name, { name = it }, label = { Text("Full Name") },
                singleLine = true, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
        }
        OutlinedTextField(email, { email = it }, label = { Text("Email") },
            singleLine = true, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(pass, { pass = it }, label = { Text("Email") },
            singleLine = true, modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
        vm.error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { if (signup) vm.signUp(name, email, pass) else vm.signIn(email, pass) },
            enabled = !vm.loading,
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (signup) "Sing Up" else "Sing In") }
        TextButton(onClick = { signup = !signup }, modifier = Modifier.fillMaxWidth()) {
            Text(if (signup) "I have an accaount" else "New accaount")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(vm: ChatViewModel) {
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val myUid = vm.user?.uid
    val fmt = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    LaunchedEffect(vm.messages.size) {
        if (vm.messages.isNotEmpty()) listState.animateScrollToItem(vm.messages.lastIndex)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privet Chat") },
                actions = { TextButton(onClick = { vm.signOut() }) { Text("Sing Out") } }
            )
        }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().imePadding()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(vm.messages) { m ->
                    val mine = m.senderUid == myUid
                    Box(Modifier.fillMaxWidth(), contentAlignment = if (mine) Alignment.CenterStart else Alignment.CenterEnd) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (mine) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Column(Modifier.padding(10.dp)) {
                                if (!mine) Text(m.senderName, style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary)
                                Text(m.text)
                                m.time?.let {
                                    Text(fmt.format(it.toDate()), style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }
            Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = input, onValueChange = { input = it },
                    placeholder = { Text("Write a massige...") },
                    modifier = Modifier.weight(1f), maxLines = 4
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = { vm.send(input); input = "" }) { Text("Send") }
            }
        }
    }
}
