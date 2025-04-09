package com.hospital_management_system.service.impl;

import com.hospital_management_system.entity.Billing;
import com.hospital_management_system.entity.Patient;
import com.hospital_management_system.entity.Slot;
import com.hospital_management_system.payload.SlotDTO;
import com.hospital_management_system.repository.BillingRepository;
import com.hospital_management_system.repository.SlotRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
//@ExtendWith(MockitoExtension.class)
public class SlotServiceImplTest {
    @InjectMocks
    private SlotServiceImpl slotService;

    @Mock
    private SlotRepository slotRepository;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Option 2: Manually initialize mocks
    }


    @Test
    public void testCreateSlots() {

        LocalDateTime startTime = LocalDateTime.parse("2025-04-05T09:30");
        LocalDateTime endTime = LocalDateTime.parse("2025-04-05T10:00");

        Slot slot = new Slot(1L, "abc", startTime, endTime, false);
        SlotDTO slotDTO = new SlotDTO(1L, "abc", startTime, endTime, false);

        Mockito.when(slotRepository.save(slot)).thenReturn(slot);
        Mockito.when(modelMapper.map(slotDTO, Slot.class)).thenReturn(slot);
        //Mockito.when(slotRepository.save(slot)).thenReturn(Optional.of(slot));

        //SlotDTO slotDTO = new SlotDTO(1L, "abc", startTime,  endTime, false);

        Slot result = slotService.createSlots(slotDTO);
        //assertEquals(1L, result.getId());
    }

    @Test
    public void testGetAvailableSlots() {
        LocalDateTime startTime = LocalDateTime.parse("2025-04-05T09:30");
        LocalDateTime endTime = LocalDateTime.parse("2025-04-05T10:00");

        Slot slot = new Slot(1L, "abc", startTime, endTime, false);

        LocalDateTime startTime1 = LocalDateTime.parse("2025-04-05T10:00");
        LocalDateTime endTime1 = LocalDateTime.parse("2025-04-05T10:30");

        Slot slot1 = new Slot(2L, "abc", startTime1, endTime1, false);

        List<Slot> slotList = new ArrayList<Slot>();
        slotList.add(slot);
        slotList.add(slot1);


        Mockito.when(slotRepository.findByQueueNameAndIsBookedFalse("abc")).thenReturn(slotList);

        List<Slot> result = slotService.getAvailableSlots("abc");
        assertEquals(2L, result.size());
    }

    @Test
    public void testBookSlot() {
        LocalDateTime startTime = LocalDateTime.parse("2025-04-05T09:30");
        LocalDateTime endTime = LocalDateTime.parse("2025-04-05T10:00");

        Slot slot = new Slot(1L, "abc", startTime, endTime, false);

        LocalDateTime startTime1 = LocalDateTime.parse("2025-04-05T10:00");
        LocalDateTime endTime1 = LocalDateTime.parse("2025-04-05T10:30");

        Slot slot1 = new Slot(2L, "abc", startTime1, endTime1, false);

        List<Slot> slotList = new ArrayList<Slot>();
        slotList.add(slot);
        slotList.add(slot1);


        Mockito.when(slotRepository.findByQueueNameAndIsBookedFalse("abc")).thenReturn(slotList);
        Mockito.when(slotRepository.save(slot)).thenReturn(slot);

        Slot result = slotService.bookSlot("abc", startTime, endTime);
        assertEquals("abc", result.getQueueName());
    }
    @Test
    public void testBookSlotException() {

        LocalDateTime resultstartTime = LocalDateTime.parse("2025-04-05T11:30");
        LocalDateTime resultendTime = LocalDateTime.parse("2025-04-05T12:00");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            slotService.bookSlot("abc", resultstartTime, resultendTime); // Replace with the actual method call
        });

        assertEquals("No available slots for the specified time.", exception.getMessage());
    }
  }


