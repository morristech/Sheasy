package de.jensklingenberg.sheasy.network.routes

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import de.jensklingenberg.sheasy.data.FileDataSource
import io.ktor.application.call
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.receiveMultipart
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream


fun Route.file(

    fileRepository: FileDataSource
    ) {
    route("file") {
        param("apk") {
            get {

                val packageName = call.parameters["apk"] ?: ""

                val apk = fileRepository.getApplicationInfo(packageName)
                val fileInputStream = FileInputStream(apk.sourceDir)
                with(call) {
                    response.header(
                        HttpHeaders.ContentDisposition, ContentDisposition.Attachment.withParameter(
                            ContentDisposition.Parameters.FileName,
                            "$packageName.apk"
                        ).toString()
                    )
                    respond(fileInputStream.readBytes())
                }


            }
        }

        param("icon") {


            get {

                val packageName = call.parameters["icon"] ?: ""

                val apk = fileRepository.getApplicationInfo(packageName)


                val anImage = (apk.icon as BitmapDrawable).bitmap


                val bmp: Bitmap? = anImage
                val stream = ByteArrayOutputStream()
                bmp!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()

                val fileInputStream = FileInputStream(apk.sourceDir)
                with(call) {
                    response.header(
                        HttpHeaders.ContentDisposition, ContentDisposition.Attachment.withParameter(
                            ContentDisposition.Parameters.FileName,
                            "$packageName.png"
                        ).toString()
                    )
                    respond(byteArray)
                }


            }
        }

        param("upload") {
            post {
                val filePath = call.parameters["upload"] ?: ""


                val multipart = call.receiveMultipart()
                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {

                        }
                        is PartData.FileItem -> {
                            //   val ext = File(part.originalFileName).extension

                            val sourceFile = File(filePath)
                            val destinationFile = File(filePath)
                            sourceFile.copyTo(destinationFile, true)

                            part.streamProvider().use { its ->
                                its.copyTo(sourceFile.outputStream())
                            }
                        }

                    }

                }
            }
        }


        param("download") {
            get {
                val filePath = call.parameters["download"] ?: ""

                if (filePath.startsWith("/storage/emulated/0/") == false) {
                    call.respondText(
                        "path not allowed",
                        ContentType.Text.JavaScript
                    )
                }


                if (filePath.contains(".")) {

                    val fileInputStream = FileInputStream(File(filePath))

                    call.respond(
                        fileInputStream.readBytes()
                    )
                } else {
                    //appsRepository.sendBroadcast(EventCategory.REQUEST, filePath)

                    val fileList = fileRepository.getFiles(filePath)

                    if (fileList.isEmpty()) {
                        call.respondText(
                            "path not found",
                            ContentType.Text.JavaScript
                        )

                    } else {


                        call.apply {
                            response.header(
                                HttpHeaders.AccessControlAllowOrigin,
                                "*"
                            )
                            respond(fileList)
                        }

                    }


                }

            }
        }


        param("shared") {
            get {
                val shared = "/storage/emulated/0/Music"

                val filePath = call.parameters["shared"] ?: ""

                if (filePath.startsWith(shared) == false) {
                    call.respondText(
                        "path not allowed",
                        ContentType.Text.JavaScript
                    )
                }


                if (filePath.contains(".")) {

                    val fileInputStream = FileInputStream(File(filePath))

                    call.respond(
                        fileInputStream.readBytes()
                    )
                } else {

                    val fileList = fileRepository.getFiles(filePath)

                    if (fileList.isEmpty()) {
                        call.respondText(
                            "path not found",
                            ContentType.Text.JavaScript
                        )

                    } else {


                        call.apply {
                            response.header(
                                HttpHeaders.AccessControlAllowOrigin,
                                "*"
                            )


                            respond(fileList)
                        }

                    }


                }

            }
        }

    }
}

