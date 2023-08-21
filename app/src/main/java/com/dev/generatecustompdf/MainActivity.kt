package com.dev.generatecustompdf

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.dev.generatecustompdf.databinding.ActivityMainBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val pageHeight = 1120
    private val pageWidth = 792
    private val PERMISSION_REQUEST_CODE = 200
    private lateinit var bmp: Bitmap
    private lateinit var scaledbmp: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)


        bmp = ContextCompat.getDrawable(this,R.drawable.ic_home)!!.toBitmap()
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)

        checkPermissions()

        binding.btnGeneratePDF.setOnClickListener {
            generatePDF()
        }

    }

    private fun generatePDF() {
        val pdfDocument = PdfDocument()


        val paint = Paint()
        val title = Paint()

        val myPageInfo:PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(pageWidth,pageHeight,1).create()

        val myPage:PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        val canvas = myPage.canvas

        canvas.drawBitmap(scaledbmp,56f,40f,paint)

        title.typeface = Typeface.create(Typeface.DEFAULT,Typeface.NORMAL)

        title.textSize = 36f

        title.color = ContextCompat.getColor(this,R.color.purple_200)

        canvas.drawText("A Project for generate custom pdf.",209f,150f,title)
        canvas.drawText("Generate Custom PDF",209f,100f,title)

        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(this,R.color.purple_200)
        title.textSize = 26f

        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.",396f,560f,title)

        pdfDocument.finishPage(myPage)

        val file = File(Environment.getExternalStorageDirectory(),"CustomPDF.pdf")

        try {

            pdfDocument.writeTo(FileOutputStream(file))

            Toast.makeText(this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show()

        }catch (e:IOException){
            e.printStackTrace()
        }

        pdfDocument.close()
    }

    private fun permissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) && PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun checkPermissions() {
        if (permissions()) {
//            generatePDF()
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}