package com.yugyeong.iamstar.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File

@Service
class S3Service(private val amazonS3: AmazonS3) {

    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucket: String

    fun uploadFile(file: File, filePath: String): String {
        amazonS3.putObject(PutObjectRequest(bucket, filePath, file))
        return amazonS3.getUrl(bucket, filePath).toString()
    }
}
