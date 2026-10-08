package com.friends.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private var listener: ListenerRegistration? = null

    var user: FirebaseUser? by mutableStateOf(auth.currentUser)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var loading by mutableStateOf(false)
        private set
    val messages = mutableStateListOf<Message>()

    init {
        if (user != null) startListening()
    }

    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) { error = "أدخل البريد وكلمة المرور"; return }
        loading = true; error = null
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener { onAuth() }
            .addOnFailureListener { loading = false; error = "فشل الدخول: ${it.localizedMessage}" }
    }

    fun signUp(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.length < 6) {
            error = "أدخل الاسم والبريد وكلمة مرور (6 أحرف على الأقل)"; return
        }
        loading = true; error = null
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener { result ->
                val update = com.google.firebase.auth.userProfileChangeRequest { displayName = name.trim() }
                result.user?.updateProfile(update)?.addOnCompleteListener { onAuth() }
            }
            .addOnFailureListener { loading = false; error = "فشل التسجيل: ${it.localizedMessage}" }
    }

    private fun onAuth() {
        loading = false
        user = auth.currentUser
        startListening()
    }

    private fun startListening() {
        listener?.remove()
        listener = db.collection("chat")
            .orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener { snap, e ->
                if (e != null) { error = "تعذر تحميل الرسائل: ${e.localizedMessage}"; return@addSnapshotListener }
                messages.clear()
                snap?.toObjects(Message::class.java)?.let { messages.addAll(it) }
            }
    }

    fun send(text: String) {
        val u = user ?: return
        if (text.isBlank()) return
        db.collection("chat").add(
            Message(
                text = text.trim(),
                senderUid = u.uid,
                senderName = u.displayName ?: u.email.orEmpty(),
                time = Timestamp.now()
            )
        ).addOnFailureListener { error = "تعذر الإرسال: ${it.localizedMessage}" }
    }

    fun signOut() {
        listener?.remove()
        messages.clear()
        auth.signOut()
        user = null
    }

    override fun onCleared() {
        listener?.remove()
    }
}
