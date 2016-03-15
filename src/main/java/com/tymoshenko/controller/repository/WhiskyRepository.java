package com.tymoshenko.controller.repository;

import com.tymoshenko.model.Whisky;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository for Whisky entity.
 * No implementation required - it will be provided for us by Spring at runtime.
 *
 * <strong>
 *     Note, this interface is package-private.
 *     It's API should be exposed via wrapper class (WhiskyCrudService) only.
 * </strong>
 *
 * @author Yakiv Tymoshenko
 * @since 07.03.2016
 */
interface WhiskyRepository extends JpaRepository<Whisky, Long> {
}
