package com.blank.common.oss.core

import cn.hutool.core.io.IoUtil
import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.StrUtil
import com.amazonaws.ClientConfiguration
import com.amazonaws.HttpMethod
import com.amazonaws.Protocol
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CreateBucketRequest
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.blank.common.core.utils.DateUtils.datePath
import com.blank.common.oss.constant.OssConstant
import com.blank.common.oss.entity.UploadResult
import com.blank.common.oss.enumd.AccessPolicyType
import com.blank.common.oss.enumd.PolicyType
import com.blank.common.oss.exception.OssException
import com.blank.common.oss.properties.OssProperties
import org.apache.commons.lang3.StringUtils
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.util.*

/**
 * S3 存储协议 所有兼容S3协议的云厂商均支持
 * 阿里云 腾讯云 七牛云 minio
 */
class OssClient(
    private val configKey: String,
    private val ossProperties: OssProperties
) {

    private val client: AmazonS3

    init {
        try {
            val endpointConfig = EndpointConfiguration(ossProperties.endpoint, ossProperties.region)
            val credentials: AWSCredentials = BasicAWSCredentials(ossProperties.accessKey, ossProperties.secretKey)
            val credentialsProvider: AWSCredentialsProvider = AWSStaticCredentialsProvider(credentials)
            val clientConfig = ClientConfiguration()
            if (OssConstant.IS_HTTPS == ossProperties.isHttps) {
                clientConfig.protocol = Protocol.HTTPS
            } else {
                clientConfig.protocol = Protocol.HTTP
            }
            val build = AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfig)
                .withClientConfiguration(clientConfig)
                .withCredentials(credentialsProvider)
                .disableChunkedEncoding()
            if (!StringUtils.containsAny(ossProperties.endpoint, *OssConstant.CLOUD_SERVICE)) {
                // minio 使用https限制使用域名访问 需要此配置 站点填域名
                build.enablePathStyleAccess()
            }
            this.client = build.build()
            createBucket()
        } catch (e: Exception) {
            if (e is OssException) {
                throw e
            }
            throw OssException("配置错误! 请检查系统配置:[${e.message}]")
        }
    }

    fun createBucket() {
        try {
            val bucketName: String = ossProperties.bucketName!!
            if (client.doesBucketExistV2(bucketName)) {
                return
            }
            val createBucketRequest = CreateBucketRequest(bucketName)
            val accessPolicy = getAccessPolicy()
            createBucketRequest.cannedAcl = accessPolicy.acl
            client.createBucket(createBucketRequest)
            client.setBucketPolicy(bucketName, getPolicy(bucketName, accessPolicy.policyType))
        } catch (e: java.lang.Exception) {
            throw OssException("创建Bucket失败, 请核对配置信息:[${e.message}]")
        }
    }

    fun upload(data: ByteArray?, path: String, contentType: String?): UploadResult {
        return upload(ByteArrayInputStream(data), path, contentType)
    }

    fun upload(inputStream: InputStream, path: String, contentType: String?): UploadResult {
        var inputStream = inputStream
        if (inputStream !is ByteArrayInputStream) {
            inputStream = ByteArrayInputStream(IoUtil.readBytes(inputStream))
        }
        try {
            val metadata = ObjectMetadata()
            metadata.contentType = contentType
            metadata.contentLength = inputStream.available().toLong()
            val putObjectRequest = PutObjectRequest(ossProperties.bucketName, path, inputStream, metadata)
            // 设置上传对象的 Acl 为公共读
            putObjectRequest.cannedAcl = getAccessPolicy().acl
            client.putObject(putObjectRequest)
        } catch (e: java.lang.Exception) {
            throw OssException("上传文件失败，请检查配置信息:[${e.message}]")
        }
        return UploadResult().apply {
            url = "${getUrl()}/$path"
            filename = path
        }
        // return UploadResult.builder().url(getUrl() + "/" + path).filename(path).build()
    }

    fun upload(file: File?, path: String): UploadResult {
        try {
            val putObjectRequest = PutObjectRequest(ossProperties.bucketName, path, file)
            // 设置上传对象的 Acl 为公共读
            putObjectRequest.cannedAcl = getAccessPolicy().acl
            client.putObject(putObjectRequest)
        } catch (e: java.lang.Exception) {
            throw OssException("上传文件失败，请检查配置信息:[${e.message}]")
        }
        return UploadResult().apply {
            url = "${getUrl()}/$path"
            filename = path
        }
        // return UploadResult.builder().url(getUrl() + "/" + path).filename(path).build()
    }

    fun delete(path: String) {
        var path = path
        path = path.replace(getUrl() + "/", "")
        try {
            client.deleteObject(ossProperties.bucketName, path)
        } catch (e: java.lang.Exception) {
            throw OssException("删除文件失败，请检查配置信息:[${e.message}]")
        }
    }

    fun uploadSuffix(data: ByteArray?, suffix: String, contentType: String?): UploadResult {
        return upload(data, getPath(ossProperties.prefix!!, suffix), contentType)
    }

    fun uploadSuffix(inputStream: InputStream, suffix: String, contentType: String?): UploadResult {
        return upload(inputStream, getPath(ossProperties.prefix!!, suffix), contentType)
    }

    fun uploadSuffix(file: File?, suffix: String): UploadResult {
        return upload(file, getPath(ossProperties.prefix!!, suffix))
    }

    /**
     * 获取文件元数据
     *
     * @param path 完整文件路径
     */
    fun getObjectMetadata(path: String): ObjectMetadata {
        var path = path
        path = path.replace(getUrl() + "/", "")
        val `object` = client.getObject(ossProperties.bucketName, path)
        return `object`.objectMetadata
    }

    fun getObjectContent(path: String): InputStream {
        var path = path
        path = path.replace(getUrl() + "/", "")
        val `object` = client.getObject(ossProperties.bucketName, path)
        return `object`.objectContent
    }

    fun getUrl(): String {
        val domain: String = ossProperties.domain!!
        val endpoint: String = ossProperties.endpoint!!
        val header = if (OssConstant.IS_HTTPS == ossProperties.isHttps) "https://" else "http://"
        // 云服务商直接返回
        if (StringUtils.containsAny(endpoint, *OssConstant.CLOUD_SERVICE)) {
            return if (StrUtil.isNotBlank(domain)) {
                header + domain
            } else "$header + ${ossProperties.bucketName}.$endpoint"
        }
        // minio 单独处理
        return if (StrUtil.isNotBlank(domain)) {
            "$header$domain/${ossProperties.bucketName}"
        } else "$header$endpoint/${ossProperties.bucketName}"
    }

    fun getPath(prefix: String, suffix: String): String {
        // 生成uuid
        val uuid = IdUtil.fastSimpleUUID()
        // 文件路径
        var path = datePath() + "/" + uuid
        if (StrUtil.isNotBlank(prefix)) {
            path = "$prefix/$path"
        }
        return path + suffix
    }


    fun getConfigKey(): String {
        return configKey
    }

    /**
     * 获取私有URL链接
     *
     * @param objectKey 对象KEY
     * @param second    授权时间
     */
    fun getPrivateUrl(objectKey: String?, second: Int): String {
        val generatePresignedUrlRequest = GeneratePresignedUrlRequest(ossProperties.bucketName, objectKey)
            .withMethod(HttpMethod.GET)
            .withExpiration(Date(System.currentTimeMillis() + 1000L * second))
        val url = client.generatePresignedUrl(generatePresignedUrlRequest)
        return url.toString()
    }

    /**
     * 检查配置是否相同
     */
    fun checkPropertiesSame(properties: OssProperties): Boolean {
        return ossProperties == properties
    }

    /**
     * 获取当前桶权限类型
     *
     * @return 当前桶权限类型code
     */
    fun getAccessPolicy(): AccessPolicyType {
        return AccessPolicyType.getByType(ossProperties.accessPolicy)
    }

    private fun getPolicy(bucketName: String, policyType: PolicyType): String {
        val builder = StringBuilder()
        builder.append("{\n\"Statement\": [\n{\n\"Action\": [\n")
        builder.append(
            when (policyType) {
                PolicyType.WRITE -> "\"s3:GetBucketLocation\",\n\"s3:ListBucketMultipartUploads\"\n"
                PolicyType.READ_WRITE -> "\"s3:GetBucketLocation\",\n\"s3:ListBucket\",\n\"s3:ListBucketMultipartUploads\"\n"
                else -> "\"s3:GetBucketLocation\"\n"
            }
        )
        builder.append("],\n\"Effect\": \"Allow\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::")
        builder.append(bucketName)
        builder.append("\"\n},\n")
        if (policyType == PolicyType.READ) {
            builder.append("{\n\"Action\": [\n\"s3:ListBucket\"\n],\n\"Effect\": \"Deny\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::")
            builder.append(bucketName)
            builder.append("\"\n},\n")
        }
        builder.append("{\n\"Action\": ")
        builder.append(
            when (policyType) {
                PolicyType.WRITE -> "[\n\"s3:AbortMultipartUpload\",\n\"s3:DeleteObject\",\n\"s3:ListMultipartUploadParts\",\n\"s3:PutObject\"\n],\n"
                PolicyType.READ_WRITE -> "[\n\"s3:AbortMultipartUpload\",\n\"s3:DeleteObject\",\n\"s3:GetObject\",\n\"s3:ListMultipartUploadParts\",\n\"s3:PutObject\"\n],\n"
                else -> "\"s3:GetObject\",\n"
            }
        )
        builder.append("\"Effect\": \"Allow\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::")
        builder.append(bucketName)
        builder.append("/*\"\n}\n],\n\"Version\": \"2012-10-17\"\n}\n")
        return builder.toString()
    }
}
