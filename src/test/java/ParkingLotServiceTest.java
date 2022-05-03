import com.bekh.parking.model.ParkingLot;
import com.bekh.parking.model.Vehicle;
import com.bekh.parking.repository.ParkingLotRepository;
import com.bekh.parking.service.ParkingLotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.bekh.parking.model.VehicleType.CAR;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class ParkingLotServiceTest {

    @Mock
    private ParkingLotRepository parkingLotRepository;

    @InjectMocks
    private ParkingLotService parkingLotService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnParkingLotList_whenFindAll_givenNothing() {
        //arrange
        List<ParkingLot> parkingLots = getParkingLots();
        given(parkingLotRepository.findAll()).willReturn(parkingLots);
        //act
        List<ParkingLot> actual = parkingLotService.findAll();
        //assert
        assertNotNull(actual);
        assertEquals(parkingLots, actual);
    }

    @Test
    public void shouldReturnParkingLot_whenFindById_givenId() {
        //arrange
        ParkingLot parkingLot = getParkingLot();
        given(parkingLotRepository.findById(1L)).willReturn(Optional.of(parkingLot));
        //act
        ParkingLot actual = parkingLotService.findById(1L);
        //assert
        assertNotNull(actual);
        assertEquals(parkingLot, actual);
    }

    @Test
    public void shouldThrowException_whenFindById_givenUnknownId() {
        //arrange
        ParkingLot parkingLot = getParkingLot();
        given(parkingLotRepository.findById(1L)).willReturn(Optional.of(parkingLot));
        //act
        //assert
        ResponseStatusException actual = assertThrows(ResponseStatusException.class, () -> parkingLotService.findById(2L));
        assertEquals("404 NOT_FOUND \"Unable to find the resource\"", actual.getMessage());
    }

    @Test
    public void shouldReturnNothing_whenSave_givenOrder() {
        ParkingLot parkingLot = getParkingLot();
        //arrange
        given(parkingLotRepository.save(parkingLot)).willReturn(parkingLot);
        //act
        parkingLotService.save(parkingLot);
        //assert
        verify(parkingLotRepository, times(1)).save(parkingLot);
    }

    @Test
    public void shouldReturnNothing_whenDelete_givenOrder() {
        ParkingLot parkingLot = getParkingLot();
        //arrange
        given(parkingLotRepository.save(parkingLot)).willReturn(parkingLot);
        //act
        parkingLotService.delete(parkingLot);
        //assert
        verify(parkingLotRepository, times(1)).save(parkingLot);
    }

    @Test
    public void shouldReturnParkingLotList_whenFindAllCurrentlyParked_givenDate() {
        //arrange
        List<ParkingLot> parkingLots = getParkingLots();
        given(parkingLotRepository.findAllCurrentlyParked(LocalDate.now())).willReturn(parkingLots);
        //act
        List<ParkingLot> actual = parkingLotService.findAllCurrentlyParked(LocalDate.now());
        //assert
        assertNotNull(actual);
        assertEquals(parkingLots, actual);
    }

    @Test
    public void shouldReturnParkingLotList_whenFindCurrentlyParkedByVehicleNumber_givenDateAndCarNumber() {
        //arrange
        List<ParkingLot> parkingLots = getParkingLots();
        given(parkingLotRepository.findCurrentlyParkedByVehicleNumber(LocalDate.now(), "Number")).willReturn(parkingLots);
        //act
        List<ParkingLot> actual = parkingLotService.findCurrentlyParkedByVehicleNumber(LocalDate.now(), "Number");
        //assert
        assertNotNull(actual);
        assertEquals(parkingLots, actual);
    }

    @Test
    public void shouldReturnParkingLotList_whenFindAllByVehicle_givenVehicle() {
        //arrange
        List<ParkingLot> parkingLots = getParkingLots();
        Vehicle vehicle = getVehicle();
        given(parkingLotRepository.findAllByVehicleAndDeleted(vehicle, false)).willReturn(parkingLots);
        //act
        List<ParkingLot> actual = parkingLotService.findAllByVehicle(vehicle);
        //assert
        assertNotNull(actual);
        assertEquals(parkingLots, actual);
    }
    
    private ParkingLot getParkingLot(){
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(1L);
        parkingLot.setEnterDate(LocalDate.now());
        parkingLot.setExitDate(LocalDate.now());
        return parkingLot;
    }

    private List<ParkingLot> getParkingLots(){
        return List.of(getParkingLot());
    }
    
    private Vehicle getVehicle(){
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setVehicleName("Name");
        vehicle.setVehicleType(CAR);
        vehicle.setVehicleNumber("Number");
        return vehicle;
    }
}
