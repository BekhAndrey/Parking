import com.bekh.parking.model.ParkingType;
import com.bekh.parking.repository.ParkingTypeRepository;
import com.bekh.parking.service.ParkingTypeService;
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

public class ParkingTypeServiceTest {

    @Mock
    private ParkingTypeRepository parkingTypeRepository;

    @InjectMocks
    private ParkingTypeService parkingTypeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnParkingTypeList_whenFindAll_givenNothing() {
        //arrange
        List<ParkingType> parkingTypes = getParkingTypes();
        given(parkingTypeRepository.findAll()).willReturn(parkingTypes);
        //act
        List<ParkingType> actual = parkingTypeService.findAll();
        //assert
        assertNotNull(actual);
        assertEquals(parkingTypes, actual);
    }

    @Test
    public void shouldReturnParkingType_whenFindById_givenId() {
        //arrange
        ParkingType parkingType = getParkingType();
        given(parkingTypeRepository.findById(1L)).willReturn(Optional.of(parkingType));
        //act
        ParkingType actual = parkingTypeService.findById(1L);
        //assert
        assertNotNull(actual);
        assertEquals(parkingType, actual);
    }

    @Test
    public void shouldThrowException_whenFindById_givenUnknownId() {
        //arrange
        ParkingType parkingType = getParkingType();
        given(parkingTypeRepository.findById(1L)).willReturn(Optional.of(parkingType));
        //act
        //assert
        ResponseStatusException actual = assertThrows(ResponseStatusException.class, () -> parkingTypeService.findById(2L));
        assertEquals("404 NOT_FOUND \"Unable to find the resource\"", actual.getMessage());
    }

    @Test
    public void shouldReturnNothing_whenSave_givenParkingType() {
        ParkingType parkingType = getParkingType();
        //arrange
        given(parkingTypeRepository.save(parkingType)).willReturn(parkingType);
        //act
        parkingTypeService.save(parkingType);
        //assert
        verify(parkingTypeRepository, times(1)).save(parkingType);
    }

    @Test
    public void shouldReturnNothing_whenDelete_givenParkingType() {
        ParkingType parkingType = getParkingType();
        //arrange
        given(parkingTypeRepository.save(parkingType)).willReturn(parkingType);
        //act
        parkingTypeService.delete(parkingType);
        //assert
        verify(parkingTypeRepository, times(1)).save(parkingType);
    }

    @Test
    public void shouldReturnParkingType_whenFindByType_givenVehicleType() {
        //arrange
        ParkingType parkingType = getParkingType();
        given(parkingTypeRepository.findByTypeAndDeleted(CAR, false)).willReturn(parkingType);
        //act
        ParkingType actual = parkingTypeService.findByType(CAR);
        //assert
        assertNotNull(actual);
        assertEquals(parkingType, actual);
    }

    private ParkingType getParkingType(){
        ParkingType parkingType = new ParkingType();
        parkingType.setId(1L);
        parkingType.setLotsAmount(50);
        parkingType.setType(CAR);
        return parkingType;
    }

    private List<ParkingType> getParkingTypes(){
        return List.of(getParkingType());
    }
}
