package com.blank.monitor.admin.notifier

import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import de.codecentric.boot.admin.server.domain.entities.Instance
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository
import de.codecentric.boot.admin.server.domain.events.InstanceEvent
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/**
 * 自定义事件通知处理
 */
@Slf4j
@Component
class CustomNotifier protected constructor(repository: InstanceRepository?) : AbstractEventNotifier(repository!!) {
    override fun doNotify(event: InstanceEvent, instance: Instance): Mono<Void> {
        return Mono.fromRunnable<Void> {
            // 实例状态改变事件
            if (event is InstanceStatusChangedEvent) {
                val registName = instance.registration.name
                val instanceId = event.getInstance().value
                val status = event.statusInfo.status
                log.info {
                    "Instance Status Change: [$registName],[$instanceId],[$status]"
                }
            }
        }
    }
}

