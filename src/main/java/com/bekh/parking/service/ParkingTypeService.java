package com.bekh.parking.service;

import com.bekh.parking.model.ParkingType;
import com.bekh.parking.model.VehicleType;
import com.bekh.parking.repository.ParkingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service("parkingTypeService")
public class ParkingTypeService {

    @Autowired
    private ParkingTypeRepository parkingTypeRepository;

    public List<ParkingType> findAll() {
        return parkingTypeRepository.findAllByDeleted(false);
    }

    public ParkingType findById(Long id) {
        return parkingTypeRepository.findByIdAndDeleted(id, false).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find the resource"));
    }

    public void save(ParkingType parkingType){
        parkingTypeRepository.save(parkingType);
    }

    public void delete(ParkingType parkingType) {
        parkingType.setDeleted(true);
        parkingTypeRepository.save(parkingType);
    }

    public ParkingType findByType(VehicleType type) {
        return parkingTypeRepository.findByTypeAndDeleted(type, false);
    }
}
