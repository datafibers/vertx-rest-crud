package com.tymoshenko.controller.repository;

import com.tymoshenko.model.Whisky;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Yakiv Tymoshenko
 * @since 07.03.2016
 */
public interface WhiskyRepository extends JpaRepository<Whisky, Long> {
    // No impl required. The Spring Framework will provide the impl at the runtime
}
