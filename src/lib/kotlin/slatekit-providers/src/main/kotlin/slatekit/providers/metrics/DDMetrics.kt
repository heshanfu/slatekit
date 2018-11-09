package slatekit.providers.metrics

import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.datadog.DatadogMeterRegistry
import slatekit.common.metrics.*


class DDMetrics(val registry: MeterRegistry,
                override val settings: MetricsSettings) : Metrics {


    override val source: String = "micrometer"


    /**
     * The provider of the metrics ( Micrometer for now )
     */
    override fun provider(): Any = registry


    /**
     * Increment a counter
     */
    override fun count(name: String, tags: List<String>?) {
        registry.counter(name, *(tags?.toTypedArray() ?: arrayOf<String>()))
    }


    /**
     * Set value on a gauge
     */
    override fun <T> gauge(name: String, call: () -> T, tags: List<Tag>?) where T: kotlin.Number {
        registry.gauge(name, toTags(tags ?: listOf()), call(), { it -> it.toDouble() })
    }


    /**
     * Set value on a gauge
     */
    override fun <T> gauge(name: String, value:T) where T: kotlin.Number {
        registry.gauge(name, value)
    }


    /**
     * Times an event
     */
    override fun time(name: String, tags: List<String>?, call:() -> Unit ) {
        registry.timer(name, *(tags?.toTypedArray() ?: arrayOf<String>())).record(call)
    }


    companion object {
        fun toTags(tags: List<Tag>): List<io.micrometer.core.instrument.Tag> {
            return tags.map {
                io.micrometer.core.instrument.ImmutableTag(it.tagName, it.tagVal)
            }
        }

        fun build(settings:MetricsSettings, config:DDConfig, bindMetrics:Boolean):MeterRegistry {
            val registry = DatadogMeterRegistry(config, Clock.SYSTEM)
            if(settings.standardize) {
                val globalTags = toTags(settings.tags.global).toMutableList()
                registry.config().commonTags(globalTags)
            }
            if(bindMetrics) {
                ClassLoaderMetrics().bindTo(registry)
                JvmMemoryMetrics().bindTo(registry)
                JvmGcMetrics().bindTo(registry)
                ProcessorMetrics().bindTo(registry)
                JvmThreadMetrics().bindTo(registry)
            }
            return registry
        }
    }
}