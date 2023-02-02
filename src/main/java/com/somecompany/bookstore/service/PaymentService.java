package com.somecompany.bookstore.service;

import com.somecompany.bookstore.exception.payment.PaymentAlreadyExistsException;
import com.somecompany.bookstore.exception.payment.PaymentNotFoundException;
import com.somecompany.bookstore.exception.payment.PaymentServiceException;
import com.somecompany.bookstore.model.entity.Payment;
import com.somecompany.bookstore.model.entity.enums.OrderStatus;
import com.somecompany.bookstore.model.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final MessageSource messageSource;

    public Page<Payment> getAll(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    public Payment getById(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> {
            throw new PaymentNotFoundException(messageSource.getMessage("msg.error.payment.find.by.id", null,
                    LocaleContextHolder.getLocale()));
        });
    }

    @Transactional
    public Payment save(Payment payment) {
        if (paymentRepository.existsByOrderIdAndUserId(payment.getOrder().getId(), payment.getUser().getId())) {
            throw new PaymentAlreadyExistsException(messageSource.getMessage("msg.error.payment.exists", null,
                    LocaleContextHolder.getLocale()));
        }

        if (!OrderStatus.AWAITING_PAYMENT.equals(payment.getOrder().getStatus())) {
            throw new PaymentServiceException(messageSource.getMessage("msg.error.payment.save", null,
                    LocaleContextHolder.getLocale()));
        }
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment update(Payment payment) {
        if (!paymentRepository.existsById(payment.getId())) {
            throw new PaymentServiceException(messageSource.getMessage("msg.error.payment.update", null,
                    LocaleContextHolder.getLocale()));
        }

        Optional<Payment> existingPayment = paymentRepository.findByOrderIdAndUserId(payment.getOrder().getId(), payment.getUser().getId());
        if (existingPayment.isPresent() && !Objects.equals(existingPayment.get().getId(), payment.getId())) {
            throw new PaymentAlreadyExistsException(messageSource.getMessage("msg.error.payment.exists", null,
                    LocaleContextHolder.getLocale()));
        }
        return paymentRepository.save(payment);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new PaymentNotFoundException(messageSource.getMessage("msg.error.payment.find.by.id", null,
                    LocaleContextHolder.getLocale()));
        }

        paymentRepository.deleteById(id);
    }
}
