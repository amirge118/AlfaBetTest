import org.example.event.*;
import org.example.repository.EventRepository;
import org.example.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class EventBatchEventsTest {

    @Mock
    private TransactionTemplate transactionTemplate;
    @Mock
    private EventRepository eventRepository;

    @Mock
    private LocationRepository locationRepository;


    @InjectMocks
    private EventBatchService eventBatchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testAddEvents() {
    }





}
