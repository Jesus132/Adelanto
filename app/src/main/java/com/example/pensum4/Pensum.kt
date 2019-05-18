package com.example.pensum4

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_pensum.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.sql.Types.NULL

class Pensum : AppCompatActivity() {

    var textView_Creditos: TextView? = null//Texto
    var matb = arrayOf('0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0','0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0','0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0')//50-Lista de colores
    var dt=0
    var aux=""
    //Tabla
    val ROWS = 4
    val COLUMNS = 3
    val tableLayout by lazy { TableLayout(this) }
    var materias: MutableList<Button> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pensum)
        //Datos
        var ListaCreditos = arrayOf<String>()
        var ListaMaterias = arrayOf<String>()
        var Datos = arrayOf('0', '0', '0', '0', '5', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0','0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0','0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '2', '1')//50
        //Jalar el texto dejado en el spinner
        val objetoIntent: Intent = intent
        var carre = objetoIntent.getStringExtra("carrera")
        //Mostrar el texto
        txtCarrera.text = "  $carre"

        //Manejo de archivo
        if (fileList().contains("$carre.txt")) try {
            val archivo = InputStreamReader(openFileInput("$carre.txt"))
            val br = BufferedReader(archivo)
            var linea = br.readLine()
            val todo = StringBuilder()
            while (linea != null) {
                todo.append(linea)
                linea = br.readLine()
            }
            br.close()
            archivo.close()
            for (i in 0..49) {
                Datos[i] = todo[i]
            }
        } catch (e: IOException) {
        }
        /*
        for (i in 0..dt) {
            println("i: $i")
            println(Datos[i])
        }
        */
        val txt: String = applicationContext.assets.open("$carre.txt").bufferedReader().use{
            it.readText()
        }
        /*
        val deepArray = arrayOf(
            arrayOf(1),
            arrayOf(2, 3),
            arrayOf(4, 5, 6)
        )

        println(deepArray.flatten())
        */
        var cont=0
        for(i in txt) {
            if(i != ',' && i != ';')//Armar palabras
                aux += i
            if (i == ',' && cont == 0){//materia
                ListaMaterias += aux
                aux  = ""
            }
            if (i == ',' && cont == 1){//credito
                ListaCreditos += aux
                aux = ""
            }
            if(i == ';') {//Salto de linea
                aux = ""
                cont++
            }
        }
        /*
        for (i in 0..49) {
            println(ListaMaterias[i])
            println(ListaCreditos[i])
        }
        //println(ListaMaterias[7])
        if(ListaMaterias[8] == "") {
            println("ENTRO----------------------------------------------------------------------------------------")
        }*/
        boton1.setOnClickListener {
            try {
                val archivo = OutputStreamWriter(openFileOutput("$carre.txt", Activity.MODE_PRIVATE))
                for (i in 0..49) {
                    archivo.write(Datos[i] + "\n")
                }
                archivo.flush()
                archivo.close()
            } catch (e: IOException) {
            }
            Toast.makeText(this, "Los datos fueron grabados", Toast.LENGTH_SHORT).show()
        }


        val Tab = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        tableLayout.apply {
            layoutParams = Tab
            isShrinkAllColumns = true
        }
        Crear_Malla(ROWS, COLUMNS, ListaMaterias, ListaCreditos)


    }
    fun Crear_Malla(rows: Int, cols: Int, LisMat: Array<String>, ListCred: Array<String>) {
        var cont=-1
        var C='1'
        for (i in 0 until rows ) {

            val row = TableRow(this)
            row.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )//Algun if con break a ver si deja de producir botones

            for (j in 0 until cols) {
                cont += 1
                val button = Button(this)
                button.apply {
                    layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                    materias.add(button)
                    text = LisMat[cont] //+ "\n" + ListCred[cont]
                    id = cont
                    if (LisMat[cont] == "")
                        button.visibility=INVISIBLE
                    button.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    setOnClickListener {
                        if (matb[id] == '3') {
                            materias[id].setBackgroundColor(Color.parseColor("#FFFFFF"))
                            matb[id] = '0'
                        } else if (matb[id] == '0') {
                            materias[id].setBackgroundColor(Color.parseColor("#1A5276"))
                            matb[id] = '1'
                        } else if (matb[id] == '1') {
                            materias[id].setBackgroundColor(Color.parseColor("#7DFC51"))
                            matb[id] = '2'
                        } else if (matb[id] == '2') {
                            materias[id].setBackgroundColor(Color.parseColor("#F52727"))
                            matb[id] = '3'
                        }
                    }
                }
                row.addView(button)
            }
            tableLayout.addView(row)
        }
        linearLayout.addView(tableLayout)
    }
}
