package com.tymoshenko.controller.repository;

import com.tymoshenko.controller.context.SpringConfig;
import com.tymoshenko.model.Whisky;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;

/**
 * TODO : add tests for exceptional cases / border conditions.
 * TODO : use separate DB for tests.
 * TODO : clean up the DB after tests.
 * @author Yakiv
 * @since 15.03.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class WhiskyCrudServiceIT {

    @Autowired
    private CrudService<Whisky> whiskyCrudService;

    @Test
    public void testCreate() throws Exception {
        Whisky whisky = new Whisky("Bourbon", "USA");

        whisky = whiskyCrudService.save(whisky);

        assertNotNull(whisky);
    }

    @Test
    public void testReadOne() throws Exception {
        final long _id = 2L;
        Whisky whisky = whiskyCrudService.readOne(_id);

        assertNotNull(whisky);
        assertEquals(_id, whisky.getId());
        assertEquals("John Walker", whisky.getName());
    }

    @Test
    public void testReadAll() throws Exception {
        List<Whisky> whiskyList = whiskyCrudService.readAll();

        assertNotNull(whiskyList);
        assertTrue(whiskyList.size() == 3);
    }

    @Test
    public void testUpdate() throws Exception {
        Whisky oldWhisky = whiskyCrudService.readOne(2L);
        Whisky newWhisky = new Whisky(oldWhisky);
        newWhisky.setName("newName");
        newWhisky.setOrigin("newOrigin");

        newWhisky = whiskyCrudService.save(newWhisky);

        assertEquals("newName", newWhisky.getName());
    }

    @Test
    public void testDelete() throws Exception {
        final long _id = 3L;
        whiskyCrudService.delete(_id);

        assertNull(whiskyCrudService.readOne(_id));
    }
}