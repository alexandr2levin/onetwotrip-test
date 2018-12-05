package com.onetwotrip.alexandr.presentation

import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * Just a place to keep all Dagger 2 components in sweet place
 */
object ComponentsHolder {

    const val DEFAULT_TAG = "DEFAULT"

    private val components = mutableMapOf<ComponentKey, Any>()

    private val logger = LoggerFactory.getLogger(javaClass)

    fun <T : Any> addDefault(componentType: KClass<T>, component: T) =
        add(
            DEFAULT_TAG,
            componentType,
            component
        )

    fun add(tag: Any, componentType: KClass<*>, component: Any) {
        logger.debug("adding component '${componentType.simpleName}'")
        components[ComponentKey(
            tag,
            componentType
        )] = component
    }

    inline fun <reified T : Any> getDefault() =
        get<T>(DEFAULT_TAG)
    inline fun <reified T : Any> get(tag: Any) =
        get(tag, T::class)

    fun <T : Any> get(tag: Any, componentType: KClass<T>): T {
        val componentKey = ComponentKey(tag, componentType)
        if(!components.containsKey(componentKey)) {
            throw Exception("There is no component for key '$componentKey'")
        }
        return components[componentKey]!! as T
    }

    fun anyTag(tag: Any) = components.any { it.key.tag == tag }
    fun any(tag: Any, componentType: KClass<*>) = components.contains(
        ComponentKey(tag, componentType)
    )
    fun anyDefault(componentType: KClass<*>) = components.contains(
        ComponentKey(
            DEFAULT_TAG,
            componentType
        )
    )

    fun removeAllWithTag(tag: Any) {
        val componentsWithTag = components.keys.filter { it.tag == tag }

        if(componentsWithTag.isEmpty()) throw Exception("there is no components with tag '$tag'")
        componentsWithTag.forEach { componentKey ->
            logger.debug("removing component '${componentKey.componentType.simpleName}'")
            components.remove(componentKey)
        }
    }

    private fun printState() {
        logger.debug("components count '${components.size}'")
    }

}

data class ComponentKey(
        val tag: Any,
        val componentType: KClass<*>
)