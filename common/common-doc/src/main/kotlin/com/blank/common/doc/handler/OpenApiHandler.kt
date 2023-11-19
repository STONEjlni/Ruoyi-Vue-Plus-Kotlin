package com.blank.common.doc.handler

import cn.hutool.core.io.IoUtil
import cn.hutool.core.util.StrUtil
import com.blank.common.core.annotation.Slf4j
import io.swagger.v3.core.jackson.TypeNameResolver
import io.swagger.v3.core.util.AnnotationsUtils
import io.swagger.v3.oas.annotations.tags.Tags
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.tags.Tag
import org.springdoc.core.customizers.OpenApiBuilderCustomizer
import org.springdoc.core.customizers.ServerBaseUrlCustomizer
import org.springdoc.core.properties.SpringDocConfigProperties
import org.springdoc.core.providers.JavadocProvider
import org.springdoc.core.service.OpenAPIService
import org.springdoc.core.service.SecurityService
import org.springdoc.core.utils.PropertyResolverUtils
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.util.CollectionUtils
import org.springframework.web.method.HandlerMethod
import java.io.StringReader
import java.lang.reflect.Method
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * 自定义 openapi 处理器
 * 对源码功能进行修改 增强使用
 */
@Slf4j
class OpenApiHandler
/**
 * Instantiates a new Open api builder.
 *
 * @param openAPI                   the open api
 * @param securityParser            the security parser
 * @param springDocConfigProperties the spring doc config properties
 * @param propertyResolverUtils     the property resolver utils
 * @param openApiBuilderCustomizers the open api builder customisers
 * @param serverBaseUrlCustomizers  the server base url customizers
 * @param javadocProvider           the javadoc provider
 */(
    openAPI: Optional<OpenAPI>,
    securityParser: SecurityService,
    springDocConfigProperties: SpringDocConfigProperties,
    propertyResolverUtils: PropertyResolverUtils,
    openApiBuilderCustomizers: Optional<MutableList<OpenApiBuilderCustomizer>>,
    serverBaseUrlCustomizers: Optional<MutableList<ServerBaseUrlCustomizer>>,
    javadocProvider: Optional<JavadocProvider>
) : OpenAPIService(
    openAPI,
    securityParser,
    springDocConfigProperties,
    propertyResolverUtils,
    openApiBuilderCustomizers,
    serverBaseUrlCustomizers,
    javadocProvider
) {

    companion object {
        /**
         * The Basic error controller.
         */
        private val basicErrorController: Class<*>? = null
    }

    /**
     * The Security parser.
     */
    private var securityParser: SecurityService? = securityParser

    /**
     * The Mappings map.
     */
    private val mappingsMap: MutableMap<String, Any> = mutableMapOf()

    /**
     * The Springdoc tags.
     */
    private val springdocTags: MutableMap<HandlerMethod, Tag> = mutableMapOf()

    /**
     * The Open api builder customisers.
     */
    private var openApiBuilderCustomisers: Optional<MutableList<OpenApiBuilderCustomizer>>? = openApiBuilderCustomizers

    /**
     * The server base URL customisers.
     */
    private var serverBaseUrlCustomizers: Optional<MutableList<ServerBaseUrlCustomizer>>? = serverBaseUrlCustomizers

    /**
     * The Spring doc config properties.
     */
    private var springDocConfigProperties: SpringDocConfigProperties? = springDocConfigProperties

    /**
     * The Cached open api map.
     */
    private val cachedOpenAPI: MutableMap<String, OpenAPI> = mutableMapOf()

    /**
     * The Property resolver utils.
     */
    private var propertyResolverUtils: PropertyResolverUtils? = propertyResolverUtils

    /**
     * The javadoc provider.
     */
    private var javadocProvider: Optional<JavadocProvider>? = javadocProvider

    /**
     * The Context.
     */
    private val context: ApplicationContext? = null

    /**
     * The Open api.
     */
    private var openAPI: OpenAPI? = null

    /**
     * The Is servers present.
     */
    private var isServersPresent = false

    /**
     * The Server base url.
     */
    private val serverBaseUrl: String? = null


    init {
        if (openAPI.isPresent) {
            this.openAPI = openAPI.get()
            if (this.openAPI?.getComponents() == null) this.openAPI?.setComponents(Components())
            if (this.openAPI?.paths == null) this.openAPI?.paths = Paths()
            if (!CollectionUtils.isEmpty(this.openAPI?.servers)) this.isServersPresent = true
        }
        if (springDocConfigProperties.isUseFqn) TypeNameResolver.std.setUseFqn(true)
    }

    override fun buildTags(
        handlerMethod: HandlerMethod,
        operation: Operation,
        openAPI: OpenAPI,
        locale: Locale
    ): Operation {
        val tags: MutableSet<Tag> = HashSet()
        var tagsStr: MutableSet<String> = HashSet()
        buildTagsFromMethod(handlerMethod.method, tags, tagsStr, locale)
        buildTagsFromClass(handlerMethod.beanType, tags, tagsStr, locale)
        if (!CollectionUtils.isEmpty(tagsStr)) tagsStr = tagsStr.stream()
            .map { str: String? ->
                propertyResolverUtils!!.resolve(
                    str,
                    locale
                )
            }
            .collect(Collectors.toSet())
        if (springdocTags.containsKey(handlerMethod)) {
            val tag = springdocTags[handlerMethod]
            tagsStr.add(tag!!.name)
            if (openAPI.tags == null || !openAPI.tags.contains(tag)) {
                openAPI.addTagsItem(tag)
            }
        }
        if (!CollectionUtils.isEmpty(tagsStr)) {
            if (CollectionUtils.isEmpty(operation.tags)) operation.tags = ArrayList(tagsStr) else {
                val operationTagsSet: MutableSet<String> = HashSet(operation.tags)
                operationTagsSet.addAll(tagsStr)
                operation.tags.clear()
                operation.tags.addAll(operationTagsSet)
            }
        }
        if (isAutoTagClasses(operation)) {
            if (javadocProvider!!.isPresent) {
                val description = javadocProvider!!.get().getClassJavadoc(handlerMethod.beanType)
                if (StrUtil.isNotBlank(description)) {
                    val tag = Tag()

                    // 自定义部分 修改使用java注释当tag名
                    val list: MutableList<String> = IoUtil.readLines(StringReader(description), ArrayList())
                    // tag.setName(tagAutoName);
                    tag.name = list[0]
                    operation.addTagsItem(list[0])
                    tag.description = description
                    if (openAPI.tags == null || !openAPI.tags.contains(tag)) {
                        openAPI.addTagsItem(tag)
                    }
                }
            } else {
                val tagAutoName = splitCamelCase(handlerMethod.beanType.getSimpleName())
                operation.addTagsItem(tagAutoName)
            }
        }
        if (!CollectionUtils.isEmpty(tags)) {
            // Existing tags
            val openApiTags = openAPI.tags
            if (!CollectionUtils.isEmpty(openApiTags)) tags.addAll(openApiTags)
            openAPI.tags = ArrayList(tags)
        }

        // Handle SecurityRequirement at operation level
        val securityRequirements = securityParser?.getSecurityRequirements(handlerMethod)
        if (securityRequirements != null) {
            if (securityRequirements.isEmpty()) operation.security =
                emptyList() else securityParser!!.buildSecurityRequirement(securityRequirements, operation)
        }
        return operation
    }

    private fun buildTagsFromMethod(
        method: Method,
        tags: MutableSet<Tag>,
        tagsStr: MutableSet<String>,
        locale: Locale
    ) {
        // method tags
        val tagsSet = AnnotatedElementUtils
            .findAllMergedAnnotations(method, Tags::class.java)
        val methodTags = tagsSet.stream()
            .flatMap { x: Tags ->
                Stream.of(
                    *x.value
                )
            }.collect(Collectors.toSet())
        methodTags.addAll(
            AnnotatedElementUtils.findAllMergedAnnotations(
                method,
                io.swagger.v3.oas.annotations.tags.Tag::class.java
            )
        )
        if (!CollectionUtils.isEmpty(methodTags)) {
            tagsStr.addAll(methodTags.stream().map { tag: io.swagger.v3.oas.annotations.tags.Tag? ->
                propertyResolverUtils!!.resolve(
                    tag!!.name, locale
                )
            }.collect(Collectors.toSet()))
            val allTags: MutableList<io.swagger.v3.oas.annotations.tags.Tag> = ArrayList(methodTags)
            addTags(allTags, tags, locale)
        }
    }

    private fun addTags(
        sourceTags: MutableList<io.swagger.v3.oas.annotations.tags.Tag>,
        tags: MutableSet<Tag>,
        locale: Locale
    ) {
        val optionalTagSet = AnnotationsUtils
            .getTags(sourceTags.toTypedArray<io.swagger.v3.oas.annotations.tags.Tag>(), true)
        optionalTagSet.ifPresent { tagsSet: MutableSet<Tag> ->
            tagsSet.forEach(
                Consumer { tag: Tag ->
                    tag.name(propertyResolverUtils!!.resolve(tag.name, locale))
                    tag.description(propertyResolverUtils!!.resolve(tag.description, locale))
                    if (tags.stream()
                            .noneMatch { t: Tag? -> t!!.name == tag.name }
                    ) tags.add(tag)
                })
        }
    }

}
