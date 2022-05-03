import com.bekh.parking.model.*;
import com.bekh.parking.repository.OrderRepository;
import com.bekh.parking.repository.ParkingLotRepository;
import com.bekh.parking.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.bekh.parking.model.Status.ONGOING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ParkingLotRepository parkingLotRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnOrderList_whenFindAll_givenNothing() {
        //arrange
        List<Order> orders = getOrders();
        given(orderRepository.findAll()).willReturn(orders);
        //act
        List<Order> actual = orderService.findAll();
        //assert
        assertNotNull(actual);
        assertEquals(orders, actual);
    }

    @Test
    public void shouldReturnOrder_whenFindById_givenId() {
        //arrange
        Order order = getOrder();
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));
        //act
        Order actual = orderService.findById(1L);
        //assert
        assertNotNull(actual);
        assertEquals(order, actual);
    }

    @Test
    public void shouldThrowException_whenFindById_givenUnknownId() {
        //arrange
        Order order = getOrder();
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));
        //act
        //assert
        ResponseStatusException actual = assertThrows(ResponseStatusException.class, () -> orderService.findById(2L));
        assertEquals("404 NOT_FOUND \"Unable to find the resource\"", actual.getMessage());
    }

    @Test
    public void shouldReturnNothing_whenSave_givenOrder() {
        Order order = getOrder();
        //arrange
        given(orderRepository.save(order)).willReturn(order);
        //act
        orderService.save(order);
        //assert
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void shouldReturnNothing_whenDelete_givenOrder() {
        Order order = getOrder();
        //arrange
        given(orderRepository.save(order)).willReturn(order);
        //act
        orderService.delete(order);
        //assert
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void shouldReturnOrderList_whenFindByUser_givenUser() {
        //arrange
        List<Order> orders = getOrders();
        User user = getUser();
        given(orderRepository.findByUserAndDeleted(user, false)).willReturn(orders);
        //act
        List<Order> actual = orderService.findByUser(user);
        //assert
        assertNotNull(actual);
        assertEquals(orders, actual);
    }

    @Test
    public void shouldReturnOrderList_whenFindByStatus_givenStatus() {
        //arrange
        List<Order> orders = getOrders();
        given(orderRepository.findByStatusAndDeleted(ONGOING, false)).willReturn(orders);
        //act
        List<Order> actual = orderService.findByStatus(ONGOING);
        //assert
        assertNotNull(actual);
        assertEquals(orders, actual);
    }

    @Test
    public void shouldReturnOrder_whenFindByParkingLot_givenId() {
        //arrange
        Order order = getOrder();
        ParkingLot parkingLot = getParkingLot();
        given(orderRepository.findByParkingLotAndDeleted(parkingLot, false)).willReturn(order);
        //act
        Order actual = orderService.findByParkingLot(parkingLot);
        //assert
        assertNotNull(actual);
        assertEquals(order, actual);
    }

    @Test
    public void shouldReturnNothing_whenUpdateUserOrdersStatus_givenUser() {
        //arrange
        User user = getUser();
        Order order = getOrder();
        ParkingLot parkingLot = getParkingLot();
        order.setParkingLot(parkingLot);
        given(orderRepository.findByUserAndDeleted(user, false)).willReturn(List.of(order));
        doNothing().when(parkingLotRepository).delete(parkingLot);
        //act
        orderService.updateUserOrdersStatus(user);
        //assert
        verify(parkingLotRepository, times(1)).delete(parkingLot);
    }

    private Order getOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setPrice("15 BYN");
        order.setStatus(ONGOING);
        order.setUser(getUser());
        return order;
    }

    private User getUser() {
        User user = new User();
        user.setApproved(true);
        user.setEmail("someEmail@gmail.com");
        user.setCreatedAt(LocalDate.now());
        user.setPassword("1234");
        return user;
    }

    private ParkingLot getParkingLot(){
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(1L);
        parkingLot.setEnterDate(LocalDate.now());
        parkingLot.setExitDate(LocalDate.now());
        return parkingLot;
    }

    private List<Order> getOrders() {
        return List.of(getOrder());
    }
}
