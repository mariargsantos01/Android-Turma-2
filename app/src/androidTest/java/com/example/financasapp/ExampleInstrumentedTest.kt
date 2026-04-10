package com.example.financasapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Teste instrumentado, executado em um dispositivo Android.
 *
 * Veja a [documentacao de testes](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Contexto do app em teste.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.atividade2", appContext.packageName)
    }
}