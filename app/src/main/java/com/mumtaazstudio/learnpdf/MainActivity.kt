package com.mumtaazstudio.learnpdf

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val STORAGE_CODE : Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Handle button click
        btn_save_pdf.setOnClickListener {
            // We need to handle runtime permission for devices with marshmallow and above
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                // System OS >= Marshmallow (6.0), check permission is enabled or not
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    // Permission was not granted, request it
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, STORAGE_CODE)
                } else {
                    // Permission already granted, call savePdf() method
                    savePdf()
                }
            } else {
                // System OS < Marshmallow, call savePdf() method
                savePdf()
            }
        }
    }

    private fun savePdf() {
        // Create object for document class
        val mDocument = Document()

        // Pdf file name
        val mFileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())

        // Pdf file path
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName + ".pdf"
        try {
            // Create instance of PdfWriter class
            PdfWriter.getInstance(mDocument, FileOutputStream(mFilePath))

            // Open the document for writing
            mDocument.open()

            // Get text from EditText i.e. et_description
            val mText = et_description.text.toString()

            // Add author of the document (metadata)
            mDocument.addAuthor("Aswan Abidin")

            // Add paragraph to the document
            mDocument.add(Paragraph(mText))

            // Close document
            mDocument.close()

            // Show file saved message with file name and path
            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT).show()




        } catch (e : Exception) {
            // If anything goes wrong causing exception, get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    // Handle permissions result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission from popup was granted, call savePdf() method
                    savePdf()
                } else {
                    // Permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
