package com.example.cleaningapp.server.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class NotificationService {

    private val log = LoggerFactory.getLogger(NotificationService::class.java)

    /**
     * Заглушка: логирую сообщение вместо фактической отправки через FCM.
     */
    fun sendOrderNotification(orderId: String, userId: Long) {
        // Если позже замените на реальный FCM-клиент, просто уберите следующий лог и добавьте вызов FCM API.
        log.info("Отправка push-уведомления пользователю [$userId] о заказе [$orderId]")
    }
}
