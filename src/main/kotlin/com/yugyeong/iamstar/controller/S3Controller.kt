package com.yugyeong.iamstar.controller

import com.yugyeong.iamstar.service.S3Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File

@RestController
@RequestMapping("/upload")
class S3Controller @Autowired constructor(
    private val s3Service: S3Service
) {

    @PostMapping
    fun uploadFiles(
        @RequestParam("postImage") postImage: MultipartFile
    ): ResponseEntity<Map<String, String>> {
        val postImageFile = convertMultipartFileToFile(postImage)

        val postUrl = s3Service.uploadFile(postImageFile, "posts/${postImage.originalFilename}")

        postImageFile.delete()

        val response = mapOf("postUrl" to postUrl)

        return ResponseEntity.ok(response)
    }

    private fun convertMultipartFileToFile(multipartFile: MultipartFile): File {
        val file = File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.originalFilename)
        multipartFile.transferTo(file)
        return file
    }
}
