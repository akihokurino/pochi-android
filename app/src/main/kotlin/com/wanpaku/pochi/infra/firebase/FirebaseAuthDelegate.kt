package com.wanpaku.pochi.infra.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import io.reactivex.SingleEmitter

object FirebaseAuthDelegate {

    fun signIn(email: String, password: String) = Single.create<FirebaseUser> { subscriber ->
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { onFirebaseAuthCompleted(it, subscriber) }
    }

    fun signInWithCredential(credential: AuthCredential) = Single.create<FirebaseUser> { subscriber ->
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { onFirebaseAuthCompleted(it, subscriber) }
    }

    fun createUser(email: String, password: String) = Single.create<FirebaseUser> { subscriber ->
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { onFirebaseAuthCompleted(it, subscriber) }
    }

    fun accessToken(): String? {
        return FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.result?.token
    }

    fun isLoggedIn(): Boolean = FirebaseAuth.getInstance().currentUser != null

    private fun onFirebaseAuthCompleted(task: Task<AuthResult>, subscriber: SingleEmitter<FirebaseUser>) {
        if (!task.isSuccessful) {
            subscriber.onError(task.exception ?: RuntimeException("failed to authenticate firebase"))
            return
        }

        val user = FirebaseAuth.getInstance().currentUser
        val isInvalidUser = user?.getIdToken(false)?.result?.token == null
        if (user == null || isInvalidUser) {
            subscriber.onError(RuntimeException("firebase user is invalid"))
            return
        }

        subscriber.onSuccess(user)
    }

}