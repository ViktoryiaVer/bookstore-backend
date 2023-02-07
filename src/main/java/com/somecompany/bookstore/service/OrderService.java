package com.somecompany.bookstore.service;

import com.somecompany.bookstore.exception.NotFoundException;
import com.somecompany.bookstore.exception.ServiceException;
import com.somecompany.bookstore.model.entity.Order;
import com.somecompany.bookstore.model.entity.enums.OrderStatus;
import com.somecompany.bookstore.model.repository.OrderRepository;
import com.somecompany.bookstore.model.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final MessageSource messageSource;

    public Page<Order> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order getById(Long id) {
        return orderRepository.findByIdOrException(id);
    }

    @Transactional
    public Order save(Order order) {
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    @Transactional
    public Order update(Order order) {
        if (!orderRepository.existsById(order.getId())) {
            throw new ServiceException(messageSource.getMessage("msg.error.order.update", null,
                    LocaleContextHolder.getLocale()));
        }
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Order> existingOrder = orderRepository.findById(id);
        if (existingOrder.isEmpty()) {
            throw new NotFoundException(messageSource.getMessage("msg.error.order.find.by.id", null,
                    LocaleContextHolder.getLocale()));
        }

        if (!OrderStatus.CANCELED.equals(existingOrder.get().getStatus())) {
            throw new ServiceException(messageSource.getMessage("msg.error.order.delete.status", null,
                    LocaleContextHolder.getLocale()));
        }

        if (paymentRepository.existsPaymentByOrder(existingOrder.get())) {
            throw new ServiceException(messageSource.getMessage("msg.error.order.delete.payment", null,
                    LocaleContextHolder.getLocale()));
        }

        orderRepository.deleteById(id);
    }
}
