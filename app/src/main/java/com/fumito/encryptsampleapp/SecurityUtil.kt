package com.fumito.encryptsampleapp

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object SecurityUtil {

    private const val PROVIDER = "AndroidKeyStore"
    private const val KEY_STORE_ALIAS = "KeyStoreAlias"
    private const val TRANSFORMATION = "AES/CBC/PKCS7Padding"


    private val keyStore = KeyStore.getInstance(PROVIDER).apply {
        this.load(null)
    }

    private fun generateAesKey() {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            PROVIDER
        )

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_STORE_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    fun encrypt(value: String): String {
        try {
            // generate key pair
            generateAesKey()

            // get secret key
            val secretKey = keyStore.getKey(KEY_STORE_ALIAS, null) as SecretKey

            // encrypt value
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            val encrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
            val combined = cipher.iv + encrypted

            // base64 encode and return it
            return Base64.getEncoder().encodeToString(combined)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun decrypt(value: String): String {
        // base64 decode

        // decrypt value

        return value
    }
}
