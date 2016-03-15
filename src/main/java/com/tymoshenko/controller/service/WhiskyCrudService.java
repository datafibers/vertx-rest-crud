package com.tymoshenko.controller.service;

import com.tymoshenko.controller.repository.WhiskyRepository;
import com.tymoshenko.model.Whisky;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Exposes CRUD API for Whisky entity.
 *
 * @author Yakiv Tymoshenko
 * @since 15.03.2016
 */
@Service
public class WhiskyCrudService implements CrudService<Whisky> {

    @Autowired
    private WhiskyRepository whiskyRepository;

    public Whisky save(Whisky whisky) {
        return whiskyRepository.saveAndFlush(whisky);
    }

    public Whisky readOne(Long id) {
        return whiskyRepository.findOne(id);
    }

    public List<Whisky> readAll() {
        return whiskyRepository.findAll();
    }

    public void delete(Long id) {
        whiskyRepository.delete(id);
    }
}
