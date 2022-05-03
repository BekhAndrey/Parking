import com.bekh.parking.model.Vehicle;
import com.bekh.parking.repository.VehicleRepository;
import com.bekh.parking.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.bekh.parking.model.VehicleType.CAR;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnVehicles_whenFindAll_givenNothing() {
        //arrange
        List<Vehicle> vehicles = getVehicles();
        given(vehicleRepository.findAll()).willReturn(vehicles);
        //act
        List<Vehicle> actual = vehicleService.findAll();
        //assert
        assertNotNull(actual);
        assertEquals(vehicles, actual);
    }

    @Test
    public void shouldReturnVehicle_whenFindById_givenId() {
        //arrange
        Vehicle vehicle = getVehicle();
        given(vehicleRepository.findById(1L)).willReturn(Optional.of(vehicle));
        //act
        Vehicle actual = vehicleService.findById(1L);
        //assert
        assertNotNull(actual);
        assertEquals(vehicle, actual);
    }

    @Test
    public void shouldThrowException_whenFindById_givenUnknownId() {
        //arrange
        Vehicle vehicle = getVehicle();
        given(vehicleRepository.findById(1L)).willReturn(Optional.of(vehicle));
        //act
        //assert
        ResponseStatusException actual = assertThrows(ResponseStatusException.class, () -> vehicleService.findById(2L));
        assertEquals("404 NOT_FOUND \"Unable to find the resource\"", actual.getMessage());
    }

    @Test
    public void shouldReturnNothing_whenSave_givenVehicle() {
        Vehicle vehicle = getVehicle();
        //arrange
        given(vehicleRepository.save(vehicle)).willReturn(vehicle);
        //act
        vehicleService.save(vehicle);
        //assert
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    public void shouldReturnNothing_whenDelete_givenVehicle() {
        Vehicle vehicle = getVehicle();
        //arrange
        given(vehicleRepository.save(vehicle)).willReturn(vehicle);
        //act
        vehicleService.delete(vehicle);
        //assert
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    public void shouldReturnVehicles_whenFindByOwnerId_givenNothing() {
        //arrange
        List<Vehicle> vehicles = getVehicles();
        given(vehicleRepository.findAllByOwnerIdAndDeleted(1L, false)).willReturn(vehicles);
        //act
        List<Vehicle> actual = vehicleService.findAllByOwnerId(1L);
        //assert
        assertNotNull(actual);
        assertEquals(vehicles, actual);
    }

    @Test
    public void shouldReturnVehicle_whenFindByVehicleNumber_givenId() {
        //arrange
        Vehicle vehicle = getVehicle();
        given(vehicleRepository.findByVehicleNumberAndDeleted("Number", false)).willReturn(vehicle);
        //act
        Vehicle actual = vehicleService.findByVehicleNumber("Number");
        //assert
        assertNotNull(actual);
        assertEquals(vehicle, actual);
    }

    private Vehicle getVehicle(){
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setVehicleName("Name");
        vehicle.setVehicleType(CAR);
        vehicle.setVehicleNumber("Number");
        return vehicle;
    }

    private List<Vehicle> getVehicles(){
        return List.of(getVehicle());
    }


}
