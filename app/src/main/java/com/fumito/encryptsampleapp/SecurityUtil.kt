package com.fumito.encryptsampleapp

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

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

        if (!keyStore.containsAlias(KEY_STORE_ALIAS)) {
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
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
            val iv = cipher.iv
            val combined = iv + encrypted

            // base64 encode and return it
            return Base64.getEncoder().encodeToString(combined)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun decrypt(value: String): String {
        try {
            // generate key pair
            generateAesKey()

            // get secret key
            val secretKey = keyStore.getKey(KEY_STORE_ALIAS, null) as SecretKey

            // base64 decode
            val decoded = Base64.getDecoder().decode(value)

            // decrypt value
            val iv = decoded.copyOfRange(0, 16)
            val encrypted = decoded.copyOfRange(16, decoded.size)

            val ivSpec = IvParameterSpec(iv)
            val cipher = Cipher.getInstance(TRANSFORMATION)

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
            val decrypted = cipher.doFinal(encrypted)
            return String(decrypted)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}
