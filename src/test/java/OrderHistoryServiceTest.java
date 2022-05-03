import com.bekh.parking.model.OrderHistory;
import com.bekh.parking.model.User;
import com.bekh.parking.repository.OrderHistoryRepository;
import com.bekh.parking.service.OrderHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.bekh.parking.model.Status.ONGOING;
import static com.bekh.parking.model.VehicleType.CAR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class OrderHistoryServiceTest {

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @InjectMocks
    private OrderHistoryService orderHistoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnOrderHistoryList_whenFindAll_givenNothing() {
        //arrange
        List<OrderHistory> orderHistories = getOrderHistories();
        given(orderHistoryRepository.findAll()).willReturn(orderHistories);
        //act
        List<OrderHistory> actual = orderHistoryService.findAll();
        //assert
        assertNotNull(actual);
        assertEquals(orderHistories, actual);
    }

    @Test
    public void shouldReturnOrderHistory_whenFindById_givenId() {
        //arrange
        OrderHistory orderHistory = getOrderHistory();
        given(orderHistoryRepository.findById(1L)).willReturn(Optional.of(orderHistory));
        //act
        OrderHistory actual = orderHistoryService.findById(1L);
        //assert
        assertNotNull(actual);
        assertEquals(orderHistory, actual);
    }

    @Test
    public void shouldThrowException_whenFindById_givenUnknownId() {
        //arrange
        OrderHistory orderHistory = getOrderHistory();
        given(orderHistoryRepository.findById(1L)).willReturn(Optional.of(orderHistory));
        //act
        //assert
        ResponseStatusException actual = assertThrows(ResponseStatusException.class, () -> orderHistoryService.findById(2L));
        assertEquals("404 NOT_FOUND \"Unable to find the resource\"", actual.getMessage());
    }

    @Test
    public void shouldReturnNothing_whenSave_givenOrderHistory() {
        //arrange
        OrderHistory orderHistory = getOrderHistory();
        given(orderHistoryRepository.save(orderHistory)).willReturn(orderHistory);
        //act
        orderHistoryService.save(orderHistory);
        //assert
        verify(orderHistoryRepository, times(1)).save(orderHistory);
    }

    @Test
    public void shouldReturnNothing_whenDelete_givenOrderHistory() {
        //arrange
        OrderHistory orderHistory = getOrderHistory();
        given(orderHistoryRepository.save(orderHistory)).willReturn(orderHistory);
        //act
        orderHistoryService.delete(orderHistory);
        //assert
        verify(orderHistoryRepository, times(1)).save(orderHistory);
    }

    @Test
    public void shouldReturnOrderHistory_whenFindByParkingLotId_givenId() {
        //arrange
        OrderHistory orderHistory = getOrderHistory();
        given(orderHistoryRepository.findByParkingLotIdAndDeleted(1L, false)).willReturn(Optional.of(orderHistory));
        //act
        OrderHistory actual = orderHistoryService.findByParkingLotId(1L);
        //assert
        assertNotNull(actual);
        assertEquals(orderHistory, actual);
    }

    @Test
    public void shouldReturnOrderHistoryList_whenFindAllByUser_givenUser() {
        //arrange
        List<OrderHistory> orderHistories = getOrderHistories();
        User user = getUser();
        given(orderHistoryRepository.findAllByUserAndDeleted(user,  Sort.by("enterDate").descending(), false)).willReturn(orderHistories);
        //act
        List<OrderHistory> actual = orderHistoryService.findAllByUser(user);
        //assert
        assertNotNull(actual);
        assertEquals(orderHistories, actual);
    }

    @Test
    public void shouldReturnNothing_whenUpdateUserHistoryStatus_givenUser() {
        //arrange
        User user = getUser();
        OrderHistory orderHistory = getOrderHistory();
        given(orderHistoryRepository.findAllByUserAndDeleted(any(User.class),  any(Sort.class), any(Boolean.class))).willReturn(List.of(orderHistory));
        given(orderHistoryRepository.save(any(OrderHistory.class))).willReturn(orderHistory);
        //act
        orderHistoryService.updateUserHistoryStatus(user);
        //assert
        verify(orderHistoryRepository, times(1)).save(orderHistory);
    }

    @Test
    public void shouldReturnOrderHistoryList_whenFindAllByUserAndStatus_givenUserAndStatus() {
        //arrange
        List<OrderHistory> orderHistories = getOrderHistories();
        User user = getUser();
        given(orderHistoryRepository.findAllByUserAndStatusAndDeleted(user, ONGOING,  Sort.by("enterDate").descending(), false)).willReturn(orderHistories);
        //act
        List<OrderHistory> actual = orderHistoryService.findAllByUserAndStatus(user, ONGOING);
        //assert
        assertNotNull(actual);
        assertEquals(orderHistories, actual);
    }

    private OrderHistory getOrderHistory() {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setId(1L);
        orderHistory.setParkingLotId(1L);
        orderHistory.setPrice("15 BYN");
        orderHistory.setStatus(ONGOING);
        orderHistory.setVehicleType(CAR);
        orderHistory.setEnterDate(LocalDate.now());
        orderHistory.setExitDate(LocalDate.now());
        orderHistory.setUser(getUser());
        return orderHistory;
    }

    private User getUser() {
        User user = new User();
        user.setApproved(true);
        user.setEmail("someEmail@gmail.com");
        user.setCreatedAt(LocalDate.now());
        user.setPassword("1234");
        return user;
    }

    private List<OrderHistory> getOrderHistories() {
        return List.of(getOrderHistory());
    }

}
