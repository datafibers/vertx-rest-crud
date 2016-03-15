package com.tymoshenko.controller.repository;

import com.tymoshenko.model.Whisky;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * This test is silly. All smart tests reside in integration-test folder.
 *
 * @author Yakiv
 * @since 15.03.2016
 */
@RunWith(MockitoJUnitRunner.class)
public class WhiskyCrudServiceTest {

    @InjectMocks
    private WhiskyCrudService crudService;

    @Mock
    private WhiskyRepository whiskyRepository;

    @Test
    public void save_ShouldInvokeRepositorySaveAndFlush() throws Exception {
        Whisky whisky = new Whisky();

        crudService.save(whisky);

        verify(whiskyRepository).saveAndFlush(whisky);
    }

    @Test
    public void readOne_ShouldInvokeRepositoryFindOne() throws Exception {
        long id = 1L;

        crudService.readOne(id);

        verify(whiskyRepository).findOne(id);
    }

    @Test
    public void readAll_ShouldInvokeRepositoryReadAll() throws Exception {
        crudService.readAll();

        verify(whiskyRepository).findAll();
    }

    @Test
    public void delete_ShouldInvokeRepositoryDelete() throws Exception {
        long id = 1L;

        crudService.delete(id);

        verify(whiskyRepository).delete(id);
    }
}